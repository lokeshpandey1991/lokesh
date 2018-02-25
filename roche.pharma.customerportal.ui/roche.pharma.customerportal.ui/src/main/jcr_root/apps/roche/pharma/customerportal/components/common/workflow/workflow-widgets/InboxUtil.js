/*
 * Copyright 1997-2008 Day Management AG
 * Barfuesserplatz 6, 4001 Basel, Switzerland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Day Management AG, ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Day.
 */

CQ.workflow.InboxUtil = function() {
    var FILTER_PREFIX = "filter-";
    
    var titleTpl = new CQ.Ext.XTemplate(
        '<tpl if="payloadExists">',
            '<div>{title}</div>',
        '</tpl>',
        '<tpl if="!payloadExists">',
            '<div class="payload-error">{payloadErrorMsg}</div>',
        '</tpl>',
        '<tpl if="comment">',
            '<div class="comment">{comment}</div>',
        '</tpl>',
    '<tpl if="pcapNumber">',
            '<div class="pcapNumber">PCAP Number= {pcapNumber}</div>',
        '</tpl>');
        
    var payloadTpl = new CQ.Ext.XTemplate(
        '<div class="payload-summary">',
            '<tpl if="icon"><div class="payload-summary-icon"><img src="{icon}"></div></tpl>',
            '<tpl if="payloadExists">',
                '<div><a href="javascript:CQ.shared.Util.open(\'{url}\');void(0);">{title}</a></div>',
            '</tpl>',
            '<tpl if="!payloadExists">',
                '<div>{payload}</div><div class="payload-error">{payloadErrorMsg}</div>',
            '</tpl>',
            '<tpl if="description"><div class="payload-summary-description">{description}</div></tpl>',
        '</div>');
    
    titleTpl.compile();
    payloadTpl.compile();
    
    return CQ.Ext.apply(new CQ.Ext.util.Observable (), {
        getInbox: function() {
            return CQ.Ext.getCmp(CQ.workflow.Inbox.List.ID);
        },
        
        getPayloadTitle: function(payload, record) {
            var title = record.get("payloadTitle");
            if (!title) {
                title = CQ.shared.XSS.getXSSRecordPropertyValue(record, "payloadPath")
            }
            if (!title && "Task" === record.get("inboxItemType") && record.json && record.json.taskInfo) {
                if (CQ.workflow.InboxUtil.isProjectTask(record)) {
                   title = CQ.I18n.getMessage("Project Management");
                } else {
                    // if there is still no title and we're dealing with a path,
                    // show the tasks 'contentPath';
                    title = CQ.shared.XSS.getXSSValue(record.json.taskInfo.contentPath);
                }
            }
            return CQ.I18n.getVarMessage(title);
        },
        
        renderTitleAndComment: function(value, p, record) {
            return titleTpl.apply({
                "title": CQ.I18n.getVarMessage(CQ.workflow.InboxUtil.getTitleValue(value, record)),
                "comment": record.get("comment"),
                "pcapNumber": record.data.metaData.instance.pcapNumber,
                "payloadExists": CQ.workflow.InboxUtil.getPayloadValue(record),
                "payloadErrorMsg": CQ.I18n.getMessage("Unknown resource")
            });
        },

        renderPayload: function(value, p, record) {
            if (!value && CQ.workflow.InboxUtil.isProjectTask(record)) {
                value = CQ.workflow.InboxUtil.getProjectAdminUrl(record);
            }
            var url = CQ.HTTP.externalize(CQ.shared.XSS.getXSSValue(value));
            var title = CQ.I18n.getVarMessage(CQ.workflow.InboxUtil.getPayloadTitle(value, record));
            var icon = record.get("payloadSummary").icon;
            return payloadTpl.apply({
                "title": title,
                "url": url,
                "description": CQ.I18n.getVarMessage(record.get("payloadSummary").description),
                "icon": icon ? CQ.HTTP.externalize(CQ.shared.XSS.getXSSValue(CQ.HTTP.encodePath(icon))) : null,
                "payload": CQ.shared.XSS.getXSSValue(CQ.workflow.InboxUtil.getPayloadValue(record)),
                "payloadExists": CQ.workflow.InboxUtil.getPayloadValue(record),
                "payloadErrorMsg": CQ.I18n.getMessage("The resource used to start this instance does not exist anymore.")
            });
        },

        getPayloadValue: function(record) {
            if ("Task" ===record.get("inboxItemType")) {
                if (record.json.taskInfo.contentPath) {
                    return record.json.taskInfo.contentPath;
                }
                return "no content specified";
            }
            return record.get("payloadPath");
        },

        getTitleValue: function(value, record) {
            if ("Task" !== record.get("inboxItemType")) {
                return value;
            }
            // for tasks always show the name
            // task names need to be XSS escaped
            return CQ.shared.XSS.getXSSValue(record.json.taskInfo.name);
        },

        isProjectTask: function(record) {
            if (record.json && record.json.taskInfo && record.json.taskInfo.taskType && "project" === record.json.taskInfo.taskType.toLowerCase()) {
                return true;
            }
            return false;
        },

        getProjectAdminUrl: function(record) {
            if (CQ.workflow.InboxUtil.isProjectTask(record)) {
                var result = "libs/cq/taskmanagement/content/taskmanager.html#/tasks/";
                var taskUiPath = "";
                // use the ID as the UI path, however strip off everything until the first /

                var taskId = record.json.taskInfo.id;
                if (taskId.indexOf("/") != -1) {
                    var indexAfterSlash = 1 + taskId.indexOf("/");
                    if (indexAfterSlash < taskId.length) {
                        taskUiPath = taskId.substring(indexAfterSlash);
                    }
                    return result + taskUiPath;
                }
                return result + record.json.taskInfo.name;
            }
            return "";
        },

        formatDate: function(date) {
            if (typeof date == "number") {
                date = new Date(date);
            }
            return date.format(CQ.I18n.getMessage("d-M-Y H:i", null, "Date format for ExtJS GridPanel (short, eg. two-digit year, http://extjs.com/deploy/ext/docs/output/Date.html)"));
        },

        getFilterFields: function() {
            var fields = [];
            var inbox = CQ.workflow.InboxUtil.getInbox();
            
            var tbar = inbox.getTopToolbar();
            if (tbar && tbar.items) {
                tbar.items.each(function(item) {
                    if (item instanceof CQ.Ext.form.Field) {
                        if (item.getName().match("^" + FILTER_PREFIX) == FILTER_PREFIX) {
                            fields.push(item);
                        }
                    }
                });
            }
            return fields;
        },
        
        registerForInboxSelection: function(btn) {

            var inbox = CQ.workflow.InboxUtil.getInbox();

            inbox.getSelectionModel().on("selectionchange", function(selModel) {

                if (selModel.hasSelection()) {

                    var records = selModel.getSelections();
                    if (records && records.length > 0) {
                        if (btn.supportedItemTypes) {
                            // loop over all records and only enable menu items available for all records
                            var recordNum;
                            var showItem = true;
                            for (recordNum=0; recordNum < records.length; recordNum++){
                                if (!btn.supportedItemTypes[records[recordNum].get("inboxItemType")]) {
                                    showItem = false;
                                    break;
                                }
                            }
                            btn.setDisabled(!showItem);
                        } else {
                            btn.setDisabled(false);
                        }
                    } else {
                        btn.setDisabled(true);
                    }
                } else {
                    btn.setDisabled(true);
                }
            });
        },

        showDetails: function() {
            var inbox = CQ.workflow.InboxUtil.getInbox();
            var items = inbox.getSelectionModel().getSelections();
            var action = CQ.Ext.getCmp("cq-workflow-inbox-details");

            if (items) {
                var itemType;
                if (items[0] && items[0].data && items[0].data.inboxItemType) {
                    itemType = items[0].data.inboxItemType;
                }
                if (itemType === "Task") {
                    CQ.workflow.InboxUtil.showTaskDetails(items[0].json.taskInfo);
                } else if (itemType === "FailureItem") {
                    var failureInfo = {
                        message: items[0].json.failureInfo.failureMessage,
                        stepTitle: CQ.I18n.getVarMessage(items[0].json.failureInfo.failedNodeTitle),
                        stack: items[0].json.failureInfo.failureStack
                    };

                    var dialog = new CQ.workflow.FailureInfoDialog({}, failureInfo);
                    dialog.show();
                }
            }
        },

        showTaskDetails: function(taskInfo) {
            if (taskInfo === undefined) {
                return;
            }

            CQ.taskmanagement.TaskManagementAdmin.showTaskDetails(taskInfo, function(updatedTaskInfo) {
                var inbox = CQ.workflow.InboxUtil.getInbox();
                var items = inbox.getSelectionModel().getSelections();

                items[0].json.taskInfo = updatedTaskInfo;
                items[0].description = CQ.I18n.getVarMessage(updatedTaskInfo.description);
                items[0].desc = CQ.I18n.getVarMessage(updatedTaskInfo.description);
                items[0].participant = updatedTaskInfo.taskOwner;
                items[0].currentAssignee = updatedTaskInfo.taskOwner;
                /* shouldn't have to reload the entire store, however can't find an alternative. */
                CQ.workflow.InboxUtil.getInbox().reload();
            });
        }
    });
}();
