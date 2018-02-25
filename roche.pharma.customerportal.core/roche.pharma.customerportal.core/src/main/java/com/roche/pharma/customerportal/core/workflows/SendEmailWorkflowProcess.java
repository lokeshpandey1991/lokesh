package com.roche.pharma.customerportal.core.workflows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.util.WorkflowHelper;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.constants.WorkflowConstants;
import com.roche.pharma.customerportal.core.services.RocheEmailService;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is custom workflow process to send the email to list of user in the group based on the arguments sent from the
 * process.
 * @author rsha66
 */
@Component(immediate = true)
@Properties({
        @Property(label = "Workflow Label", name = "process.label", value = "customerportal Send Email process")
})
@Service
public class SendEmailWorkflowProcess implements WorkflowProcess {
    
    /* Services used by the the class */
    @Reference
    private RocheEmailService emailService;
    
    @Reference
    private WorkflowHelper workflowHelper;
    
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    /** Private variable for path wfassignee and templatepath **/
    private String path = StringUtils.EMPTY;
    
    private String wfAssignee = StringUtils.EMPTY;
    
    private String templatePath = StringUtils.EMPTY;
    
    private UserManager userManager;
    /* Logger for the class */
    private static final Logger LOG = LoggerFactory.getLogger(SendEmailWorkflowProcess.class);
    
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
            throws WorkflowException {
        
        final WorkflowData workflowData = workItem.getWorkflowData();
        path = workflowData.getPayload() == null ? StringUtils.EMPTY : workflowData.getPayload().toString();
        setWorkflowArgument(metaDataMap);
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "rocheUser");
        
        final ResourceResolver resourceResolver = CommonUtils.getResourceResolverFromSubService(resolverFactory,
                paramMap);
        
        if (resourceResolver != null) {
            try {
                userManager = resourceResolver.adaptTo(UserManager.class);
                final List<String> faliurList = sendEmail(resourceResolver, workItem);
                if (faliurList == null || faliurList.isEmpty()) {
                    LOG.error("Unable to send email to following email ID", faliurList);
                }
            } finally {
                if (resourceResolver.isLive()) {
                    resourceResolver.close();
                }
            }
        }
        
    }
    
    /**
     * This method returns the params send in workflow email.
     * @param emailParams
     * @param emailvalueMap
     * @param workItem
     * @return emailParams
     */
    private Map<String, String> getEmailParams(Map<String, String> emailParams, Map<String, Object> emailvalueMap,
            WorkItem workItem) {
        final WorkflowData workflowData = workItem.getWorkflowData();
        final MetaDataMap dataMap = workflowData.getMetaDataMap();
        final String intiator = StringUtils.defaultIfEmpty(workItem.getWorkflow().getInitiator(),
                "Unknown Workflow User");
        
        emailParams.put(WorkflowConstants.EMAIL_TEMPLATE, (String) emailvalueMap.get("emailTemplate"));
        emailParams.put(WorkflowConstants.EMAIL_BODY, (String) emailvalueMap.get("emailBody"));
        emailParams.put(WorkflowConstants.EMAIL_SUBJECT, (String) emailvalueMap.get("emailSubject"));
        emailParams.put(WorkflowConstants.EMAIL_CONTENTPATH, path);
        emailParams.put(WorkflowConstants.PCAP_PROPERTY, dataMap.get(WorkflowConstants.PCAP_PROPERTY).toString());
        if (dataMap.get(WorkflowConstants.START_COMMENT_PROPERTY) != null) {
            emailParams.put(WorkflowConstants.START_COMMENT_PROPERTY,
                    dataMap.get(WorkflowConstants.START_COMMENT_PROPERTY).toString());
        }
        emailParams.put(WorkflowConstants.EMAIL_REQUESTER, intiator);
        return emailParams;
    }
    
    /**
     * This method returns the list of all the users to whom email is to be sent.
     * @param usermanager
     * @param wfAssignee
     * @param resourceResolver
     * @return wfAssigne
     */
    private String[] getWfAssigne(String wfAssignee, ResourceResolver resourceResolver) {
        final List<String> wfAssigne = new ArrayList<>();
        try {
            if (userManager != null && userManager.getAuthorizable(wfAssignee) != null
                    && userManager.getAuthorizable(wfAssignee).isGroup()) {
                
                final Resource groupRes = resourceResolver
                        .getResource(userManager.getAuthorizable(wfAssignee).getPath());
                final Group group = groupRes == null ? null : groupRes.adaptTo(Group.class);
                if (group != null) {
                    getMemberEmailID(group, wfAssigne);
                }
                
            }
        } catch (final RepositoryException e) {
            LOG.error("Unable to Send email to the mentioned group in Custion Send email Workflow Process", e);
        }
        return wfAssigne.toArray(new String[wfAssigne.size()]);
    }
    
    /**
     * Returns the country specific workflow assignee groups.
     * @param wfAssignee
     * @param resourceResolver
     * @param path
     * @return localAssignee
     */
    private String getCountrySpecificGroup(String wfAssignee, ResourceResolver resourceResolver, String path) {
        final Resource resource = resourceResolver.getResource(path);
        String countryCode;
        String localAssignee;
        countryCode = CommonUtils.getCountryCode(resource).toLowerCase();
        localAssignee = countryCode == StringUtils.EMPTY ? wfAssignee
                : wfAssignee + RocheConstants.UNDERSCORE + countryCode;
        
        return localAssignee;
        
    }
    
    /**
     * send email template provided in arguments to given workflow assignee.
     * @param wfAssignee
     * @param resourceResolver
     * @param templatePath
     * @param workItem
     * @return faliur list
     */
    private List<String> sendEmail(ResourceResolver resourceResolver, WorkItem workItem) {
        final String localAssignee = getCountrySpecificGroup(wfAssignee, resourceResolver, path);
        final Resource emailTemplate = resourceResolver.getResource(templatePath + "/jcr:content/emailContent");
        Map<String, String> emailParams = new HashMap<>();
        final Map<String, Object> emailvalueMap = emailTemplate == null ? new HashMap<>() : emailTemplate.getValueMap();
        emailParams = getEmailParams(emailParams, emailvalueMap, workItem);
        try {
            if (userManager != null) {
                wfAssignee = userManager.getAuthorizable(localAssignee) == null ? wfAssignee : localAssignee;
                final String[] emailIDs = getWfAssigne(wfAssignee, resourceResolver);
                return emailService.sendEmail(emailParams, emailIDs);
            }
            
        } catch (final RepositoryException e) {
            LOG.error("Unable to check authorized user", e);
        } finally {
            if (resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
        return new ArrayList<>();
    }
    
    /**
     * This method sets the paramters provided by the workflow arguments.
     * @param metaDataMap
     */
    private void setWorkflowArgument(MetaDataMap metaDataMap) {
        final String[] arguments = metaDataMap.get("PROCESS_ARGS", StringUtils.EMPTY).split(";");
        this.wfAssignee = arguments.length > 0 ? arguments[0] : StringUtils.EMPTY;
        this.templatePath = arguments.length > 1 ? arguments[1] : StringUtils.EMPTY;
    }
    
    /**
     * this method returns email ID of all the members in the group.
     * @param group
     * @param wfAssigne
     * @return memebrsEMail ID
     * @throws RepositoryException
     */
    private List<String> getMemberEmailID(Group group, List<String> wfAssigne) throws RepositoryException {
        final Iterator<Authorizable> memberslist = group.getMembers();
        while (memberslist.hasNext()) {
            final Authorizable user = memberslist.next();
            if (user != null && !user.isGroup() && user.getProperty(WorkflowConstants.PROFILE_EMAIL) != null) {
                wfAssigne.add(user.getProperty(WorkflowConstants.PROFILE_EMAIL)[0].toString());
            }
        }
        return wfAssigne;
    }
}
