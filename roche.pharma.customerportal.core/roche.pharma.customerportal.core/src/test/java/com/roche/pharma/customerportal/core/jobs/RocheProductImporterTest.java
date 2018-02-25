package com.roche.pharma.customerportal.core.jobs;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.day.cq.replication.Replicator;
import com.roche.pharma.customerportal.core.mock.MockHelper;
import com.roche.pharma.customerportal.core.mock.MockJobImpl;
import com.roche.pharma.customerportal.core.mock.MockJobManager;
import com.roche.pharma.customerportal.core.mock.MockReplicatorImpl;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;
import com.roche.pharma.customerportal.core.services.impl.ConfigurationServiceImpl;

public class RocheProductImporterTest {
    private static JobManager jobmanager;
    private static RocheProductImporter consumer = new RocheProductImporter();
    private static MockJobImpl jobInstrumentRemoved;
    private static MockJobImpl jobAssayRemoved;
    private static MockJobImpl jobInstrumentUpdate;
    private static MockJobImpl jobAssayUpdate;
    private static MockJobImpl jobAssayAdd;
    private static MockJobImpl jobInstrumentAdd;
    private static MockJobImpl jobBlank;
    static Map<String, String> jobParams = new HashMap<String, String>();
    static Map<String, String> jobParams1 = new HashMap<String, String>();
    static Map<String, String> jobParams2 = new HashMap<String, String>();
    static Map<String, String> jobParams3 = new HashMap<String, String>();
    static Map<String, String> jobParams4 = new HashMap<String, String>();
    static Map<String, String> jobParams5 = new HashMap<String, String>();
    static Map<String, String> jobParamsEmpty = new HashMap<String, String>();
   
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Before
    public void setUp() throws Exception {
        jobParams.put("path", "/etc/commerce/products/customerportal/INS/_12/3/INS_123");
        jobParams.put("event", "removed");
        jobParams1.put("path", "/etc/commerce/products/customerportal/TAP/_12/3/TAP_123");
        jobParams1.put("event", "removed");
        jobParams2.put("path", "/etc/commerce/products/customerportal/TAP/_12/3/INS_123");
        jobParams2.put("event", "update");
        jobParams3.put("path", "/etc/commerce/products/customerportal/TAP/_12/3/TAP_123");
        jobParams3.put("event", "update");
        jobParams4.put("path", "/etc/commerce/products/customerportal/TAP/_12/3/TAP_123");
        jobParams4.put("event", "add");
        jobParams5.put("path", "/etc/commerce/products/customerportal/INS/_12/3/INS_123");
        jobParams5.put("event", "add");
        context.registerInjectActivateService(consumer);
        jobInstrumentRemoved = new MockJobImpl(jobParams);
        jobAssayRemoved = new MockJobImpl(jobParams1);
        jobInstrumentUpdate = new MockJobImpl(jobParams2);
        jobAssayUpdate = new MockJobImpl(jobParams3);
        jobInstrumentAdd = new MockJobImpl(jobParams5);
        jobAssayAdd = new MockJobImpl(jobParams4);
        jobBlank = new MockJobImpl(jobParamsEmpty);
        
    }
    
    @Test
    public void testInstrumentRemove() throws LoginException {
        consumer.process(jobInstrumentRemoved);
    }
    
    @Test
    public void testInstrumentAdd() throws LoginException {
        consumer.process(jobInstrumentAdd);
    }
    
    @Test
    public void testAssayAdd() throws LoginException {
        consumer.process(jobAssayAdd);
    }
    
    @Test
    public void testAssayRemove() throws LoginException {
        consumer.process(jobAssayRemoved);
    }
    
    @Test
    public void testInstrumentUpdate() throws LoginException {
        consumer.process(jobInstrumentUpdate);
    }
    
    @Test
    public void testAssayUpdate() throws LoginException {
        consumer.process(jobAssayUpdate);
    }
    
    @Test
    public void testProcessBlank() throws LoginException {
        consumer.process(jobBlank);
    }
    
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            
        	ConfigurationServiceImpl configurationServiceImpl = new ConfigurationServiceImpl();
            Map<String, Object> _config1;
            _config1 = new HashMap<String, Object>();
            _config1.put("assay.menu.pages", "[/content/customerportal/global/en/instrument,/content/customerportal/us/en/instrument]");
            _config1.put("dispatcher.flush.url","http://localhost/dispatcher/invalidate.cache");
            _config1.put("activate.pdp.on.dtl.event","true");
            context.registerInjectActivateService(configurationServiceImpl, _config1);
            
            // Mock Query Builder
            List<String> pdpPaths = new ArrayList<String>();
            pdpPaths.add("/content/roche/customerportal/us/en/home/pdp");
            pdpPaths.add("/content/customerportal/global-master-blueprint/en/home/roche54353");
            MockHelper.loadQuery(context, pdpPaths);
            
            // Mock Replicator
            Replicator replicator = new MockReplicatorImpl();
            context.registerService(Replicator.class, replicator);
            
            jobmanager = new MockJobManager();
            context.registerService(JobManager.class, jobmanager);
            
            context.load().json("/json/roche/product.json",  "/etc/commerce/products/customerportal/INS/_12/3/INS_123");
            context.load().json("/json/roche/product.json",  "/etc/commerce/products/customerportal/TAP/_12/3/TAP_123");
            MockRocheContent.loadfile(context, "/json/roche/roche.json", "/content/roche/customerportal");
            MockRocheContent.loadfile(context, "/json/roche/us/us.json", "/content/roche/customerportal/us");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/pdp.json",
                    "/content/roche/customerportal/us/en/home/pdp");
           

        }
    };
}
