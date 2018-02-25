package com.roche.pharma.customerportal.core.workflows;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
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
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.replication.Replicator;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.constants.WorkflowConstants;
import com.roche.pharma.customerportal.core.mock.MockHelper;
import com.roche.pharma.customerportal.core.mock.MockMetaDataMap;
import com.roche.pharma.customerportal.core.mock.MockReplicatorImpl;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;
import com.roche.pharma.customerportal.core.mock.MockWorkflowSession;

public class PcapNumberProcessTest {

    final static String PAYLOAD_PATH = "/content/customerportal/us/en/home";

    /* Logger for the class */
    private static final Logger LOG = LoggerFactory.getLogger(PcapNumberProcessTest.class);

    private static PcapNumberProcess pcapNumberProcess = new PcapNumberProcess();
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
    .build();

    @Test
    public void testPcapNumberWorkflowProcess() {
        try {
            context.resourceResolver().getResource(PAYLOAD_PATH + RocheConstants.SLASH + JcrConstants.JCR_CONTENT);
            final MetaDataMap mockMetaDataMap = new MockMetaDataMap();
            mockMetaDataMap.put(WorkflowConstants.PCAP_PROPERTY, "123456");
            mockMetaDataMap.put(WorkflowConstants.START_COMMENT_PROPERTY, "comment");
            final WorkflowSession workflowSession = new MockWorkflowSession(context.resourceResolver());
            final WorkItem workflowItem = MockHelper.getWorkFlowItem(PAYLOAD_PATH, mockMetaDataMap);
            pcapNumberProcess = context.getService(PcapNumberProcess.class);
            pcapNumberProcess.execute(workflowItem, workflowSession, mockMetaDataMap);
        } catch (final WorkflowException e) {
            LOG.error("Unable to execute test case for PCAP workflow process", e.getMessage(), e);
        }
    }

    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context) throws PersistenceException, IOException, javax.jcr.LoginException,
                RepositoryException {
            MockRocheContent.loadfile(context, "/json/roche/us/pages/home.json", "/content/customerportal/us/en/home");
            final Replicator replicator = new MockReplicatorImpl();
            final WorkflowHelper helper = new WorkflowHelperImpl();
            context.registerService(Replicator.class, replicator);
            context.registerService(WorkflowHelper.class, helper);
            context.registerService(PcapNumberProcess.class, pcapNumberProcess);
            context.registerInjectActivateService(pcapNumberProcess);
        }
    };
}
