package com.roche.pharma.customerportal.core.services.impl;

import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.ComponentContext;

import com.roche.pharma.customerportal.core.services.CacheInstance;
import com.roche.pharma.customerportal.core.services.CacheManagerService;

/**
 * Scheduler service to clean particular cache the scheduler time needs to be provided in cron format cache name needs
 * to be passed
 * @author asi130
 */
@Component(immediate = true, metatype = true, label = "Roche ehcache flush scheduler")
public class CacheFlushSchedulerService {
    @Property(name = "scheduler.expression", label = "Schedule",
            description = "Set the running schedule in cron-format. \"\r\n"
                    + "			+ \"See http://en.wikipedia.org/wiki/Cron (note: here, the first number represents seconds).\", value = \"0 00 1 ? * *")
    private String schedulerExpression;
    
    @Property(name = "cache.name", label = "cache name", description = "name of the cache to be flushed")
    private String cacheName;
    @Reference
    private CacheManagerService cacheManagerService;
    @Reference
    private Scheduler scheduler;
    
    /**
     * @param componentContext
     */
    @Activate
    protected void activate(final ComponentContext componentContext) {
        final Map<String, String> properties = (Map<String, String>) componentContext.getProperties();
        this.cacheName = properties.get("cache.name");
        this.schedulerExpression = properties.get("scheduler.expression");
        // run the job as configured
        final CacheFlushTask task = new CacheFlushTask();
        final ScheduleOptions schedulerOptions = scheduler.EXPR(schedulerExpression);
        schedulerOptions.canRunConcurrently(false);
        schedulerOptions.config(null);
        scheduler.schedule(task, schedulerOptions);
    }
    
    /**
     * This class is to provide a task to run the schedular
     */
    public class CacheFlushTask implements Runnable {
        
        @Override
        public void run() {
            if (cacheManagerService.getCache(cacheName) != null) {
                
                final CacheInstance<String> cacheInstance = cacheManagerService.getCache(cacheName);
                cacheInstance.clear();
            }
        }
    }
}
