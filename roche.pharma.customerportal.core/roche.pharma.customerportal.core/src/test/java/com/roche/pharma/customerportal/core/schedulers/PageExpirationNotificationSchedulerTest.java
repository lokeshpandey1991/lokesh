package com.roche.pharma.customerportal.core.schedulers;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.day.cq.replication.Replicator;
import com.roche.pharma.customerportal.core.mock.MockHelper;
import com.roche.pharma.customerportal.core.mock.MockReplicatorImpl;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;
import com.roche.pharma.customerportal.core.services.RocheEmailService;
import com.roche.pharma.customerportal.core.services.impl.RocheEmailServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class PageExpirationNotificationSchedulerTest {
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    final static String basePath = "/content/customerportal/us/en/home";
    final static String productNodePath1 = "/content/customerportal/us/en/home/page1";
    final static String productNodePath2 = "/content/customerportal/us/en/home/page2";
    
    private static PageExpirationNotificationScheduler scheduler;
    
    @Before
    public void setUp() throws Exception {
        /*
         * final Runnable job = new PageExpirationNotificationScheduler(); context.registerService(job);
         */
        // register OSGi service
    }
    
    @Test
    public void test_GivenItsMasterInstance_WhenRunIsInvoked_ThenCallsService() throws Exception {
        scheduler.run();
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context) throws PersistenceException, IOException,
                javax.jcr.LoginException, RepositoryException, ParseException {
            MockRocheContent.loadfile(context, "/json/roche/workflow.json", basePath);
            final List<String> pdpPaths = new ArrayList<String>();
            pdpPaths.add(productNodePath1);
            pdpPaths.add(productNodePath2);
            MockHelper.loadQuery(context, pdpPaths);
            final RocheEmailService rocheEmailService = new RocheEmailServiceImpl();
            final Replicator replicator = new MockReplicatorImpl();
            context.registerService(Replicator.class, replicator);
            context.registerService(rocheEmailService);
            scheduler = new PageExpirationNotificationScheduler();
            context.registerInjectActivateService(scheduler);
        }
    };
    
}
