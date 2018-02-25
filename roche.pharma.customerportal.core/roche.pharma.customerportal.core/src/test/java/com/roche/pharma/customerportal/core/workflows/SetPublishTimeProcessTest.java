package com.roche.pharma.customerportal.core.workflows;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.util.WorkflowHelper;
import com.adobe.acs.commons.util.impl.WorkflowHelperImpl;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.Replicator;
import com.roche.pharma.customerportal.core.constants.WorkflowConstants;
import com.roche.pharma.customerportal.core.mock.MockHelper;
import com.roche.pharma.customerportal.core.mock.MockMetaDataMap;
import com.roche.pharma.customerportal.core.mock.MockReplicatorImpl;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;
import com.roche.pharma.customerportal.core.mock.MockWorkflowSession;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class SetPublishTimeProcessTest {
    
    final static String PAYLOAD_PATH = "/content/customerportal/us/en/home";
    
    /* Logger for the class */
    private static final Logger LOG = LoggerFactory.getLogger(SetPublishTimeProcessTest.class);
    
    private static SetPublishTimeProcess setPublishTimeProcess = new SetPublishTimeProcess();
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testPcapNumberWorkflowProcess() {
        try {
            ResourceResolver resourceResolver = context.resourceResolver();
            MetaDataMap mockMetaDataMap = new MockMetaDataMap();
            mockMetaDataMap.put(WorkflowConstants.PCAP_PROPERTY, "123456");
            WorkflowSession workflowSession = new MockWorkflowSession(resourceResolver);
            WorkItem workflowItem = MockHelper.getWorkFlowItem(PAYLOAD_PATH, mockMetaDataMap);
            setPublishTimeProcess = context.getService(SetPublishTimeProcess.class);
            setPublishTimeProcess.execute(workflowItem, workflowSession, mockMetaDataMap);
        } catch (WorkflowException e) {
            LOG.error("Unable to execute test case for PCAP workflow process", e);
        }
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            
            MockRocheContent.loadfile(context, "/json/roche/us/pages/home.json", "/content/customerportal/us/en/home");
            Replicator replicator = new MockReplicatorImpl();
            WorkflowHelper helper = new WorkflowHelperImpl();
            context.registerService(Replicator.class, replicator);
            context.registerService(WorkflowHelper.class, helper);
            context.registerService(SetPublishTimeProcess.class, setPublishTimeProcess);
            context.registerInjectActivateService(setPublishTimeProcess);
        }
    };
}
