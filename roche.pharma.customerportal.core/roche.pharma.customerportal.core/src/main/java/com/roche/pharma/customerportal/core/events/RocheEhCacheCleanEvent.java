package com.roche.pharma.customerportal.core.events;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roche.pharma.customerportal.core.services.CacheInstance;
import com.roche.pharma.customerportal.core.services.CacheManagerService;

@Component(policy = ConfigurationPolicy.REQUIRE, label = "Roche EhCache Clean Event",
        description = "Roche EhCache Clean Event", metatype = true)
@Service
@Properties({
        // Scope the paths as tightly as possible based on your use-case.
        @Property(label = "Paths",
                description = "[ Required ] A list of resource paths this listener will listen for change events.",
                name = ResourceChangeListener.PATHS, value = {
                    "/content/customerportal/cacheClean"
                }),
        @Property(label = "Change Types",
                description = "[ Optional ] The change event types this listener will listener for. ",
                name = ResourceChangeListener.CHANGES, value = {
                    "CHANGED"
                })
})
public class RocheEhCacheCleanEvent implements ResourceChangeListener {
    
    @Reference
    private CacheManagerService cacheManagerService;
    
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(RocheEhCacheCleanEvent.class);
    
    @Override
    public void onChange(@Nonnull final List<ResourceChange> changes) {
        
        LOG.info("RocheEhCacheCleanEvent started");
        for (final ResourceChange change : changes) {
            final Set<String> changedProps = change.getChangedPropertyNames();
            for (final String changedProp : changedProps) {
                final CacheInstance<String> cacheInstance = cacheManagerService.getCache(changedProp);
                if (cacheInstance != null) {
                    cacheInstance.clear();
                }
            }
        }
    }
    
}
