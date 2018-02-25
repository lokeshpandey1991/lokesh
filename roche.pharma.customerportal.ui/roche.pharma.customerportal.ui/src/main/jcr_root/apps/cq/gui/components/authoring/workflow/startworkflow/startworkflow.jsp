<%--
  ADOBE CONFIDENTIAL

  Copyright 2014 Adobe Systems Incorporated
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and may be covered by U.S. and Foreign Patents,
  patents in process, and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.

  The modal to start workflows.


--%><%
%><%@page import="com.adobe.granite.ui.components.Config,
                  com.adobe.granite.workflow.WorkflowException,
                  com.adobe.granite.workflow.WorkflowSession,
                  com.adobe.granite.workflow.metadata.MetaDataMap,
                  com.adobe.granite.workflow.model.WorkflowModel,
                  com.day.cq.i18n.I18n,
                  com.day.cq.wcm.api.Page,
                  org.apache.sling.api.resource.Resource,
                  java.util.Arrays"%><%
%><%
%><%@include file="/libs/granite/ui/global.jsp" %><%!

    private boolean doInclude(WorkflowModel model, String[] tags, boolean doStrict, String exclude) {
        if (tags.length == 0) {
            return true;
        }

        MetaDataMap metaData = model.getMetaDataMap();
        String tagStr = metaData.get("tags", String.class) != null ? metaData.get("tags", String.class) : null;
        String tagStrSplits[] = (tagStr != null && !tagStr.equals("")) ? tagStr.trim().split(",") : new String[0];
        if (exclude != null &&
            exclude.equals("excludeWorkflows") &&
            Arrays.asList(tagStrSplits).contains("publish")) {
            return false;
        }
        if (tagStrSplits.length == 0 && !doStrict) {
            // for backward compatibility
            return true;
        } else {
            for (String tag : tagStrSplits) {
                for (String checkTag : tags) {
                    if (checkTag.equals(tag)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

%><%

    Page targetPage = null;

    // get page object from suffix
    String pagePath = slingRequest.getRequestPathInfo().getSuffix();
    if (pagePath != null) {
        Resource pageResource = slingRequest.getResourceResolver().resolve(pagePath);
        if (pageResource != null) {
            targetPage = pageResource.adaptTo(Page.class);
        }
    }

    if (targetPage == null) {
        return;
    }

    I18n wfI18n = new I18n(slingRequest);
    Config wfCfg = new Config(resource);
    String exclude = wfCfg.get("exclude", String.class);

%>
<form action="<%= request.getContextPath() %>/etc/workflow/instances" method="post" class="coral-Modal coral-Form coral-Form--vertical cq-workflow-start-modal">
    <div class="coral-Modal-header">
        <i class="coral-Modal-typeIcon coral-Icon coral-Icon--sizeS"></i>
        <h2 class="coral-Modal-title coral-Heading coral-Heading--2"><%= wfI18n.get("Start Workflow") %></h2>
        <button type="reset" class="coral-MinimalButton coral-Modal-closeButton" title="Close" data-dismiss="modal">
            <i class="coral-Icon coral-Icon--sizeXS coral-Icon--close coral-MinimalButton-icon "></i>
        </button>
    </div>
    <div class="coral-Modal-body">
        <div>
            <input type="hidden" name="_charset_" value="utf-8">
            <input type="hidden" name=":status" value="browser">
            <input type="hidden" name="payloadType" value="JCR_PATH">
            <input type="hidden" name="payload" value="<%= xssAPI.encodeForHTMLAttr(request.getContextPath() + targetPage.getPath()) %>">
            <span class="coral-Select coral-Form-field cq-common-admin-timeline-toolbar-actions-workflow-select" data-init="select" aria-required="true">
                <button type="button" class="coral-Select-button coral-MinimalButton cq-common-admin-timeline-toolbar-actions-workflow-select-button">
                    <span class="coral-Select-button-text"><%= wfI18n.get("Select a Workflow Model") %></span>
                </button>
                <select id ="workflowmodel" name="model" autocomplete="off" class="coral-Select-select">
                    <option value=""><%= wfI18n.get("Select a Workflow Model") %></option><%
                    WorkflowSession wfSession = slingRequest.getResourceResolver().adaptTo(WorkflowSession.class);
                    WorkflowModel[] models;
                    try {
                        models = wfSession.getModels();
                        String[] tags = {"wcm"};

                        for (WorkflowModel model : models) {
                            if (doInclude(model, tags, false, exclude)) {
                                %><option value="<%= model.getId() %>"><%= xssAPI.encodeForHTML(wfI18n.getVar(model.getTitle())) %></option><%
                            }
                        }
                    } catch (WorkflowException e) {
                        //throw new ServletException("Error fetching workflow models", e);
                    }
                    %>
                </select>
            </span>
            <input type="text" class="coral-Textfield coral-Form-field" name="workflowTitle" placeholder="<%= wfI18n.get("Enter title of workflow") %>">
			<%--			
			Added check for customerportal project.
			--%>
            <%
                        if(pagePath.contains("/content/customerportal")){
				%>
            <input type="text" class="coral-Textfield coral-Form-field coral-Form-field-pcapnumber" name="pcapNumber" placeholder="<%= wfI18n.get("Enter PCAP number of workflow") %>">

            <%
                    }
			%>
        </div>
    </div>
    <div class="coral-Modal-footer">
        <button type="reset" class="coral-Button" data-dismiss="modal"><%= wfI18n.get("Close") %></button>
        <button type="button" class="coral-Button coral-Button--primary start-workflow-activator-start" data-dismiss="modal" disabled="disabled"><%= wfI18n.get("Start Workflow") %></button>
    </div>
</form>
