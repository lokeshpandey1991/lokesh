package com.roche.pharma.customerportal.core.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.apache.sling.event.jobs.JobManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(policy = ConfigurationPolicy.REQUIRE, label = "Roche Product Resource Change Listener",
        description = "Roche Product Resource Change Listener", metatype = true)
@Service
@Properties({
        // Scope the paths as tightly as possible based on your use-case.
        @Property(label = "Paths",
                description = "[ Required ] A list of resource paths this listener will listen for change events.",
                name = ResourceChangeListener.PATHS, value = {
                    "/etc/commerce/products/customerportal"
                }),
        @Property(label = "Change Types",
                description = "[ Optional ] The change event types this listener will listener for. ",
                name = ResourceChangeListener.CHANGES, value = {
                        "CHANGED", "REMOVED"
                })
})
public class RocheProductResourceChangeListner implements ResourceChangeListener {
    
    @Reference
    private JobManager jobManager;
    
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(RocheProductResourceChangeListner.class);
    
    @Override
    public void onChange(@Nonnull final List<ResourceChange> changes) {
        
        LOG.info("RocheProductResourceChangeListner started");
        for (final ResourceChange change : changes) {
            final Map<String, Object> props = new HashMap<String, Object>();
            final String path = change.getPath();
            LOG.info("RocheProductResourceChangeListner path::" + path);
            LOG.info("RocheProductResourceChangeListner event::" + change);
            props.put("path", path);
            
            switch (change.getType()) {
                case ADDED:
                    props.put("event", "add");
                    jobManager.addJob("com/roche/customerportal/productImport", props);
                    break;
                case CHANGED:
                    props.put("event", "update");
                    jobManager.addJob("com/roche/customerportal/productImport", props);
                    break;
                case REMOVED:
                    props.put("event", "removed");
                    jobManager.addJob("com/roche/customerportal/productImport", props);
                    break;
                default:
                    // Do nothing
            }
        }
    }
    
}
