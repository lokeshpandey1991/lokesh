package com.roche.pharma.customerportal.core.services.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyOption;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roche.pharma.customerportal.core.constants.EhCacheConstants;
import com.roche.pharma.customerportal.core.services.CacheFactory;

/**
 * A service for factory configurations of the EhCaches which are added to the CacheManagerService provided by
 * EhCacheManagerService class. This class acts as an interface to create multiple EhCache instances via osgi factory
 * configurataions. Each instance will be created as a service where the 'cacheName' can be used to reference it. The
 * EhCacheManagerService is bound to this class and does all the workf of adding and removing caches to the cacheManager
 * based on the configurations added here in this configurationFactory
 * @author asingh131
 */

@Service(EhCacheFactory.class)
@Component(label = "EhCacheFactory - Factory configurations for the EhCacheManagerService", immediate = true,
        enabled = true, metatype = true, policy = ConfigurationPolicy.REQUIRE, configurationFactory = true,
        description = EhCacheConstants.EH_CACHE_FACTORY_DESCRIPTION)
public class EhCacheFactory implements CacheFactory {
    /**
     * logger for this class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EhCacheFactory.class);
    
    @Property(name = "ehcache.cacheName", label = "cacheName", value = EhCacheConstants.DEFAULT_CACHE_NAME,
            propertyPrivate = false, description = EhCacheConstants.CACHE_NAME_DESCRIPTION)
    private String cacheName;
    
    @Property(name = "ehcache.maxEntriesLocalHeap", label = "maxEntriesLocalHeap", intValue = 1003,
            propertyPrivate = false, description = EhCacheConstants.MAX_ENTRIES_LOCAL_HEAP_DESCRIPTION)
    private Integer maxEntriesLocalHeap;
    
    @Property(name = "ehcache.memoryStoreEvictionPolicy", label = "memoryStoreEvictionPolicy", propertyPrivate = false,
            options = {
                    @PropertyOption(name = "LRU", value = "LRU - Least Recently Used - the default value"),
                    @PropertyOption(name = "LFU", value = "LFU - Less Frequently Used"),
            @PropertyOption(name = "FIFO",
                            value = "FIFO - First In First Out, the oldest element by creation time")
            }, description = EhCacheConstants.MEMORY_STORE_EVICTION_POLICY_DESCRIPTION)
    private String msep;
    
    @Property(name = "ehcache.eternal", label = "eternal", boolValue = false, propertyPrivate = false,
            description = EhCacheConstants.ETERNAL_DESCRIPTION)
    private Boolean eternal;
    
    @Property(name = "ehcache.timeToLiveSeconds", label = "timeToLiveSeconds", longValue = 0, propertyPrivate = false,
            description = EhCacheConstants.TIME_TO_LIVE_SECONDS_DESCRIPTION)
    private Long timeToLiveSeconds;
    
    @Property(name = "ehcache.timeToIdleSeconds", label = "timeToIdleSeconds", longValue = 0, propertyPrivate = false,
            description = EhCacheConstants.TIME_TO_IDLE_SECONDS_DESCRIPTION)
    private Long timeToIdleSeconds;
    
    @Property(name = "ehcache.persisted", label = "persisted", boolValue = false, propertyPrivate = false,
            description = EhCacheConstants.DISK_PERSISTENCE_DESCRIPTION)
    private Boolean persisted;
    
    @Property(name = "ehcache.maxEntriesLocalDisk", label = "maxEntriesLocalDisk", intValue = 0,
            propertyPrivate = false, description = EhCacheConstants.MAX_ENTRIES_LOCAL_DISK_DESCRIPTION)
    private Integer maxEntriesLocalDisk;
    
    @Property(name = "ehcache.searchable", label = "searchable", boolValue = false, propertyPrivate = false,
            description = EhCacheConstants.DISK_SEARCHABLE_DESCRIPTION)
    private Boolean searchable;
    
    @Property(name = "ehcache.lockTimeoutInMillis", label = "lockTimeoutInMillis", intValue = 0,
            propertyPrivate = false, description = EhCacheConstants.LOCK_TIMEOUT_DESCRIPTION)
    private Integer lockTimeoutInMillis;

    /**
     * Since the ComponentContext is used in the EhCacheManagerService to initialise the cache and to create the actual
     * service, the field is required here.
     */
    private ComponentContext componentContext;
    
    /**
     * The properties from the ComponentContext are used in the EhCacheManagerService to initialise the cache. The
     * cacheName property is used to identify each cache. If the cacheName is changed in the configuration we need a
     * variable to hold the currentName so that we can first remove that cache and then create a new one with the new
     * cacheName.
     */
    private String currentName;
    
    /**
     * When a new configuration is provided this method is called. The bindEhCacheFactory() method in
     * EhCacheManagerService is also called, and that is where the real work of creating and 'managing' the cache is
     * carried out.
     * @param context
     */
    @Activate
    private void activate(final ComponentContext context) {
        LOGGER.debug("activate():: ACTIVATE entry");
        cacheName = PropertiesUtil.toString(context.getProperties().get("ehcache.cacheName"),
                EhCacheConstants.DEFAULT_CACHE_NAME);
        LOGGER.debug("activate():: need to add cache [{}]", cacheName);
        this.componentContext = context;
        this.currentName = cacheName;
    }
    
    /**
     * When a configuration is deleted, or when it is modified, this method is called. The unbindEhCacheFactory() method
     * in EhCacheManagerService is also called, and that is where the real work of removing the cache is carried out.
     * When a configuration is 'modified' the process happens in two steps: first the deactivate() and
     * unbindEhCacheFactory() methods are called so the cache is removed. In the second step the activate() and
     * bindEhCacheFactory() methods are called and a new cache is created with the new settings.
     * @param context
     */
    @Deactivate
    private void deactivate(final ComponentContext context) {
        LOGGER.debug("deactivate():: DE-ACTIVATE entry");
        cacheName = PropertiesUtil.toString(context.getProperties().get("ehcache.cacheName"),
                EhCacheConstants.DEFAULT_CACHE_NAME);
        LOGGER.debug("deactivate():: need to remove cache [" + cacheName + "]");
        this.componentContext = context;
    }
    
    /**
     * The EhCacheManagerService is where the real work of creating and 'managing' the cache is carried out so it needs
     * to be able to get the ComponentContext from the EhCacheFactory.
     */
    @Override
    public ComponentContext getContext() {
        return componentContext;
    }
    
    /**
     * The EhCacheManagerService needs the currentName of the cache in case the cacheName has been changed in the
     * configuration.
     * @return String currentName
     */
    public String getCurrentName() {
        return currentName;
    }
    
}
