package com.roche.pharma.customerportal.core.schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Rule;
import org.junit.Test;

import com.day.cq.replication.Replicator;
import com.roche.pharma.customerportal.core.mock.MockHelper;
import com.roche.pharma.customerportal.core.mock.MockReplicatorImpl;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;
import com.roche.pharma.customerportal.core.mock.MockScheduler;
import com.roche.pharma.customerportal.core.services.impl.ConfigurationServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class SearchIndexSchedulerTest {
    
    private static SearchIndexScheduler searchIndexScheduler;
    
    final static String productNodePath = "/content/roche/customerportal/us/en/home/roche54353";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testSchedulerUnSceduled() {
        Map<String, Object> _config;
        _config = new HashMap<String, Object>();
        _config.put("executionMode", "disabled");
        context.registerInjectActivateService(searchIndexScheduler, _config);
    }
    
    @Test
    public void testSchedulerSceduled() {
        Map<String, Object> _config;
        _config = new HashMap<String, Object>();
        _config.put("executionMode", "scheduled");
        _config.put("cronJobExpression", "0 0/15 * 1/1 * ? *");
        context.registerInjectActivateService(searchIndexScheduler, _config);
    }
    
    @Test
    public void testSchedulerImediate() {
        Map<String, Object> _config;
        _config = new HashMap<String, Object>();
        _config.put("executionMode", "immediate");
        context.registerInjectActivateService(searchIndexScheduler, _config);
    }
    
    @Test
    public void testSchedulerImediateForSingleIndex() {
        Map<String, Object> _config;
        _config = new HashMap<String, Object>();
        _config.put("executionMode", "immediate");
        _config.put("singleIndexNode", true);
        context.registerInjectActivateService(searchIndexScheduler, _config);
    }
    
    @Test
    public void testSchedulerImediateForMultipleIndex() {
        Map<String, Object> _config;
        _config = new HashMap<String, Object>();
        _config.put("executionMode", "immediate");
        _config.put("singleIndexNode", false);
        context.registerInjectActivateService(searchIndexScheduler, _config);
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            
            MockRocheContent.loadfile(context, "/json/roche/us/pages/en.json", "/content/roche/customerportal/us/en");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/pdp.json", productNodePath);
            
            // Mock Replicator
            Replicator replicator = new MockReplicatorImpl();
            context.registerService(Replicator.class, replicator);
            
            // Mock Query Builder
            List<String> pdpPaths = new ArrayList<String>();
            pdpPaths.add(productNodePath + "/jcr:content");
            MockHelper.loadQuery(context, pdpPaths);
            
            // Set Run Mode
            String[] runModes = {
                    "author"
            };
            context.runMode(runModes);
            
            ConfigurationServiceImpl configurationServiceImpl = new ConfigurationServiceImpl();
            Map<String, Object> _config;
            _config = new HashMap<String, Object>();
            _config.put("service.rootPath", "/content/roche/customerportal");
            context.registerInjectActivateService(configurationServiceImpl, _config);
            
            // Mock Scheduler
            searchIndexScheduler = new SearchIndexScheduler();
            Runnable task = searchIndexScheduler.new SearchIndexTask();
            Scheduler scheduler = new MockScheduler(task);
            context.registerService(Scheduler.class, scheduler);
            
        }
    };
}
