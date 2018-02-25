package com.roche.pharma.customerportal.core.workflows;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.util.WorkflowHelper;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * Sample dynamic participant step that determines the participant based on a path given as argument.
 * @author rsha66
 */
@Component
@Service
@Properties({
        @Property(name = Constants.SERVICE_DESCRIPTION, value = "A implementation of a dynamic participant chooser."),
        @Property(name = ParticipantStepChooser.SERVICE_PROPERTY_LABEL,
                value = "customerportal dynamic Participant Chooser")
})
public class AssignToDynamicParticipant implements ParticipantStepChooser {
    
    /* Services used by the class. */
    @Reference
    private WorkflowHelper workflowHelper;
    
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    /* Logger for the class */
    private static final Logger LOG = LoggerFactory.getLogger(AssignToDynamicParticipant.class);
    
    @Override
    public String getParticipant(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args)
            throws WorkflowException {
        
        String wfAssignee = null;
        wfAssignee = args.get("PROCESS_ARGS", StringUtils.EMPTY);
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "rocheUser");
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = CommonUtils.getResourceResolverFromSubService(resolverFactory, paramMap);
            if (resourceResolver != null) {
                wfAssignee = getCountrySpecificGroup(wfAssignee, resourceResolver, workItem);                
            }
        } catch (RepositoryException e) {
           LOG.error("Repository exception while reading node",e);
        }finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
        
        return wfAssignee;
    }
    
    /**
     * Returns the country specific workflow Assignee groups.
     * @param wfAssignee
     * @param resourceResolver
     * @param path
     * @return
     * @throws RepositoryException 
     */
    private String getCountrySpecificGroup(String wfAssignee, ResourceResolver resourceResolver, WorkItem workItem) throws RepositoryException {
        final WorkflowData workflowData = workItem.getWorkflowData();
        final String path = workflowData.getPayload().toString();
        final Resource resource = resourceResolver.getResource(path);
        String localAssignee = StringUtils.EMPTY;
        if (null != resource) {
            final UserManager userManager = resourceResolver.adaptTo(UserManager.class);
            final String countryCode = CommonUtils.getCountryCode(resource).toLowerCase();
                localAssignee = getLocalAssignee(wfAssignee, workItem, userManager, countryCode);
            
        }
        return localAssignee;
        
    }

    private String getLocalAssignee(String wfAssignee, WorkItem workItem, final UserManager userManager,
            final String countryCode) throws RepositoryException {
        String localAssignee;
        if (StringUtils.isEmpty(wfAssignee)) {
            localAssignee = workItem.getWorkflow().getInitiator();
        } else {
            localAssignee = countryCode == StringUtils.EMPTY ? wfAssignee : wfAssignee
                    + RocheConstants.UNDERSCORE + countryCode;
            
            if (userManager != null && userManager.getAuthorizable(localAssignee) != null) {
                localAssignee = wfAssignee;
            }
        }
        return localAssignee;
    }
}
