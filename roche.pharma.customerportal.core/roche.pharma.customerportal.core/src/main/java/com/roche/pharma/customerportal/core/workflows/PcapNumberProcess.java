package com.roche.pharma.customerportal.core.workflows;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
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
import com.day.cq.commons.jcr.JcrConstants;
import com.roche.pharma.customerportal.core.constants.WorkflowConstants;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * this is a custom workflow process which add the PCAP number provided by the author while initiating the approval
 * process to payload resource.
 * @author rsha66
 */
@Component
@Properties({
        @Property(label = "Workflow Label", name = "process.label", value = "customerportal PCAP number Process")
})
@Service
public class PcapNumberProcess implements WorkflowProcess {
    
    /* Logger for the class */
    private static final Logger LOG = LoggerFactory.getLogger(PcapNumberProcess.class);
    
    /* Services used by the class. */
    @Reference
    private WorkflowHelper workflowHelper;
    
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException {
        
        final WorkflowData workflowData = workItem.getWorkflowData();
        final MetaDataMap dataMap = workflowData.getMetaDataMap();
        final String path = workflowData.getPayload().toString();
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "rocheUser");
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = CommonUtils.getResourceResolverFromSubService(resolverFactory, paramMap);
            final Resource resource = resourceResolver
                    .getResource(path + WorkflowConstants.SLASH + JcrConstants.JCR_CONTENT);
            if (resource != null) {
                final ModifiableValueMap map = resource.adaptTo(ModifiableValueMap.class);
                if (map != null && dataMap.get(WorkflowConstants.PCAP_PROPERTY) != null) {
                    map.put(WorkflowConstants.PCAP_PROPERTY, dataMap.get(WorkflowConstants.PCAP_PROPERTY).toString());
                    if (dataMap.get(WorkflowConstants.START_COMMENT_PROPERTY) != null) {
                        map.put(WorkflowConstants.COMMENT_PROPERTY,
                                dataMap.get(WorkflowConstants.START_COMMENT_PROPERTY).toString());
                    }
                } else {
                    workflowSession.terminateWorkflow(workItem.getWorkflow());
                }
                resourceResolver.commit();
            }
            
        } catch (final PersistenceException e) {
            LOG.error("PCAP number was not added to the resource", e);
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
    }
    
}
