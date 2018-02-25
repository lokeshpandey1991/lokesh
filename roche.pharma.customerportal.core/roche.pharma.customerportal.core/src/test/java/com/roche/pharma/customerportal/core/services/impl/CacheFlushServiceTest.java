package com.roche.pharma.customerportal.core.services.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockScheduler;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class CacheFlushServiceTest {
    
    private static CacheFlushSchedulerService cacheFlushScheduler;
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testSchedulerImediate() {
        Map<String, Object> _config;
        _config = new HashMap<String, Object>();
        _config.put("cache.name", "exampleset");
        _config.put("scheduler.expression", "0 0/15 * 1/1 * ? *");
        context.registerInjectActivateService(cacheFlushScheduler, _config);
    }
    
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            
            final EhCacheManagerServiceImpl configurationServiceImpl = new EhCacheManagerServiceImpl();
            context.registerInjectActivateService(configurationServiceImpl);
            // Mock Scheduler
            cacheFlushScheduler = new CacheFlushSchedulerService();
            final Runnable task = cacheFlushScheduler.new CacheFlushTask();
            final Scheduler scheduler = new MockScheduler(task);
            context.registerService(Scheduler.class, scheduler);
            
        }
    };
}
