package com.roche.pharma.customerportal.core.services.impl;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.Searchable;
import net.sf.ehcache.management.ManagementService;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roche.pharma.customerportal.core.constants.EhCacheConstants;
import com.roche.pharma.customerportal.core.exceptions.BusinessExecutionException;
import com.roche.pharma.customerportal.core.services.CacheInstance;
import com.roche.pharma.customerportal.core.services.CacheManagerService;

/**
 * Service which provides a Cache Manager Service for EhCache. It starts up the cachemanager with a configurable
 * diskStorePath. It also initialises the EhCache JMX bean for monitoring and adds a shutdownhook for graceful shutdown
 * of the cachemanager. <br />
 * <br />
 * The EhCacheManagerService is bound to the EhCacheFactory class. It is here in the cache manager service that all the
 * work of adding and removing caches to the cacheManager is done. Each cache is a service created here based on the
 * OSGi factory configurations added for EhCacheFactory.
 * @author asingh131
 */
@Service(CacheManagerService.class)
@Component(
        immediate = true,
        label = "EhCacheManagerService for EhCache",
        metatype = true,
        description = "Service that starts the Cachemanger and manages the EhCache instances that are configured via factory configurations"
                + " (EhCacheFactory configurations)")
@Property(name = org.osgi.framework.Constants.SERVICE_RANKING, intValue = 1, propertyPrivate = true)
@javax.annotation.concurrent.NotThreadSafe
public class EhCacheManagerServiceImpl implements CacheManagerService {
    private static final int THOUSAND = 1000;

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EhCacheManagerServiceImpl.class);
    
    @Property(name = "ehCacheManagerService.diskStorePath", label = "diskStorePath",
            value = EhCacheConstants.DEFAULT_DISK_STORE_PATH, propertyPrivate = false,
            description = EhCacheConstants.DEFAULT_DISK_STORE_PATH_DESCRIPTION)
    private String diskStorePath;
    
    @Reference(referenceInterface = EhCacheFactory.class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC)
    private List<EhCacheFactory> cacheServicesList;
    
    @SuppressWarnings("rawtypes")
    private Map<String, EhCacheInstance> ehCacheInstancesMap;
    
    private CacheManager cacheManager;
    private Configuration cacheManagerConfig;
    private DiskStoreConfiguration diskStoreConfig;
    private static final String CACHEMANAGERNAME = EhCacheConstants.DEFAULT_CACHE_MANAGER_NAME;
    
    private Thread shutdownHook;
    
    private ComponentContext cacheFactoryComponentContext;
    private CacheConfiguration cacheConfig;
    private String cacheName;
    private Integer maxEntriesLocalHeap;
    private Boolean eternal;
    private Long timeToLiveSeconds;
    private Long timeToIdleSeconds;
    private Boolean persisted;
    private Integer maxEntriesLocalDisk;
    private Boolean searchable;
    private String msep;
    private int lockTimeoutInMillis;
    
    /**
     * When a new configuration is provided in EhCacheFactory the ComponentContext is fetched from the EhCacheFactory
     * and a new cache is created with a call to createCache()
     */
    @SuppressWarnings("rawtypes")
    protected synchronized void bindEhCacheFactory(final EhCacheFactory cacheService) {
        LOGGER.debug("bindEhCacheFactory():: entry BIND");
        if (cacheServicesList == null) {
            cacheServicesList = new ArrayList<>();
        }
        
        if (ehCacheInstancesMap == null) {
            this.ehCacheInstancesMap = new HashMap<>();
        }
        
        listCacheServices(cacheService);
        
        cacheFactoryComponentContext = cacheService.getContext();
        
        getProps(cacheFactoryComponentContext);
        
        createCache(cacheFactoryComponentContext);
        
        cacheServicesList.add(cacheService);
        LOGGER.info("bindEhCacheFactory():: EhCacheFactory [{}] has been BOUND", cacheService.toString());
    }
    
    /**
     * When a configuration in EhCacheFactory is deleted, or when it is modified, this method is called. The currentName
     * of the cache is fetched and this name is used to identify the cache to be removed. When a configuration is
     * modified the process happens in two steps: first unbindEhCacheFactory() method is called so that the cache is
     * removed. In the second step the bindEhCacheFactory() method is called and a new cache is created with the new
     * settings.
     */
    protected synchronized void unbindEhCacheFactory(final EhCacheFactory cacheService) {
        LOGGER.debug("unbindEhCacheFactory():: entry UN-BIND");
        
        listCacheServices(cacheService);
        
        cacheFactoryComponentContext = cacheService.getContext();
        
        final String currentName = cacheService.getCurrentName();
        // When renaming a cache, the 'current name' is needed to delete the existing cache before creating a new one
        // with the new name.
        // Also, when deleting the last configuration via OSGi Console, the component context does not provide the
        // current name so we
        // get the currentName to be sure to delete the correct configuration.
        LOGGER.info("unbindEhCacheFactory():: currentName is [{}]", currentName);
        
        getProps(cacheFactoryComponentContext);
        cacheServicesList.removeIf(s -> s.getCurrentName().equalsIgnoreCase(currentName));
        removeCache(currentName);
        LOGGER.info("unbindEhCacheFactory():: EhCacheFactory [{}] has been UN-BOUND", cacheService.toString());
    }
    
    /**
     * Properties provided in the ComponentContext coming from the EhCacheFactory are used to create a cache with the
     * corresponding configuration * The cacheManager will already have been initialised when this service is activated
     * (see the activate() method).
     */
    @SuppressWarnings("deprecation")
    private void createCache(final ComponentContext context) {
        if (null != context) {
            cacheName = PropertiesUtil.toString(context.getProperties().get("ehcache.cacheName"),
                    EhCacheConstants.DEFAULT_CACHE_NAME);
            maxEntriesLocalHeap = PropertiesUtil.toInteger(context.getProperties().get("ehcache.maxEntriesLocalHeap"),
                    THOUSAND);
            eternal = PropertiesUtil.toBoolean(context.getProperties().get("ehcache.eternal"), false);
            timeToLiveSeconds = PropertiesUtil.toLong(context.getProperties().get("ehcache.timeToLiveSeconds"), 0);
            timeToIdleSeconds = PropertiesUtil.toLong(context.getProperties().get("ehcache.timeToIdleSeconds"), 0);
            persisted = PropertiesUtil.toBoolean(context.getProperties().get("ehcache.persisted"), false);
            maxEntriesLocalDisk = PropertiesUtil.toInteger(context.getProperties().get("ehcache.maxEntriesLocalDisk"),
                    0);
            msep = PropertiesUtil.toString(context.getProperties().get("ehcache.memoryStoreEvictionPolicy"), "LRU");
            searchable = PropertiesUtil.toBoolean(context.getProperties().get("ehcache.searchable"), false);
            lockTimeoutInMillis = PropertiesUtil.toInteger(context.getProperties().get("ehcache.lockTimeoutInMillis"),
                    0);
            
            if (this.cacheManager == null) {
                LOGGER.error("createCache():: Could not load cacheManager [{}]", CACHEMANAGERNAME);
            } else {
                // existing cacheNames
                final String[] existingCaches = cacheManager.getCacheNames();
                for (final String existingCache : existingCaches) {
                    LOGGER.debug("createCache():: existingCache is called [{}]", existingCache);
                }
                
                LOGGER.debug("createCache():: Check if we can add cache with cacheName [{}]", cacheName);
                if (cacheManager.cacheExists(cacheName) || "default".equalsIgnoreCase(cacheName)) {
                    LOGGER.error(
                            "createCache():: Cannot create cache because the cache [{}] already exists in cacheManager [{}]",
                            cacheName, CACHEMANAGERNAME);
                } else {
                    cacheConfig = new CacheConfiguration(cacheName, maxEntriesLocalHeap).eternal(eternal)
                            .timeToLiveSeconds(timeToLiveSeconds).timeToIdleSeconds(timeToIdleSeconds)
                            .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.fromString(msep))
                            .maxEntriesLocalDisk(maxEntriesLocalDisk);
                    if (persisted) {
                        // OSGi config currently only gives one option for 'persisted' so we set both diskPersistent and
                        // overflowToDisk to 'true'
                        cacheConfig.overflowToDisk(true);
                        // This option provides an extra tier for storage during cache operation, but this disk storage
                        // is not persisted.
                        // After a restart, the disk tier is cleared of any cache data.
                        cacheConfig.diskPersistent(true);
                        // If 'diskPersistent' and 'overflowToDisk' are both set to â€œtrueâ€�, cached data is saved to disk
                        // asynchronously and
                        // can be recovered after a clean shutdown or planned flush. To prevent corrupt or inconsistent
                        // data from being returned,
                        // checking measures are performed upon a restart, and if any discrepancy is found, the cache
                        // that was stored on disk
                        // is emptied and must be reloaded from the data source.
                        
                    } else if (searchable) {
                        final Searchable searchableUpdated = new Searchable();
                        searchableUpdated.setKeys(true);
                        searchableUpdated.setValues(true);
                        cacheConfig.addSearchable(searchableUpdated);
                    }
                    
                    final Cache anEhCache = new Cache(cacheConfig);
                    
                    LOGGER.debug("createCache():: Adding a cache now called [{}]", cacheName);
                    cacheManager.addCacheIfAbsent(anEhCache);
                    
                    LOGGER.debug("createCache():: cacheManager status = " + cacheManager.getStatus());
                    
                    final EhCacheInstance<?> cacheToStore = new EhCacheInstance<>(anEhCache, lockTimeoutInMillis);
                    
                    ehCacheInstancesMap.put(cacheName, cacheToStore);
                }
            }
        }
    }
    
    /**
     * Remove the cache identifed by the cacheName.
     */
    private void removeCache(final String cacheName) {
        LOGGER.debug("removeCache(cacheName):: Trying now to remove cache [{}]", cacheName);
        
        if (this.cacheManager == null || cacheName == null) {
            LOGGER.error("removeCache(cacheName):: Could not load cache manager");
        } else {
            
            final String[] existingCaches = cacheManager.getCacheNames();
            for (final String existingCache : existingCaches) {
                LOGGER.debug("removeCache(cacheName):: cacheManager [{}] has an existing cache named [{}]",
                        CACHEMANAGERNAME, existingCache);
            }
            
            final Cache cache = cacheManager.getCache(cacheName);
            
            if (cache == null) {
                LOGGER.error(
                        "removeCache(cacheName):: cache [{}] did not exist in cacheManager [{}]. Could not remove cache.",
                        cacheName, CACHEMANAGERNAME);
            } else {
                // if requested cache exists, let's remove it.
                LOGGER.debug("removeCache(cacheName):: DISPOSING cache [{}] ", cacheName);
                cache.dispose();
                LOGGER.debug("removeCache(cacheName):: cache [{}] DISPOSED", cacheName);
                ehCacheInstancesMap.remove(cacheName);
                cacheManager.removeCache(cacheName);
                LOGGER.debug("removeCache(cacheName):: cache [{}] has now been removed from cacheManager [{}]",
                        cacheName, CACHEMANAGERNAME);
            }
            LOGGER.debug("removeCache(cacheName):: cacheManager status = " + cacheManager.getStatus());
        }
    }
    
    /**
     * Log all props that are available in ComponentContext for debugging purpouses (trace)
     */
    private Dictionary<?, ?> getProps(final ComponentContext context) {
        
        final Dictionary<?, ?> props = context.getProperties();
        for (final Enumeration<?> e = props.keys(); e.hasMoreElements();) {
            final String k = (String) e.nextElement();
            final Object v = props.get(k);
            LOGGER.debug("getProps():: {} = [{}]", k, v.toString());
        }
        
        return props;
    }
    
    /**
     * Log all cache services that are available in the EhCacheManager. Each configuration in EhCacheFactory should
     * correspond to one cache service in this list.
     */
    private void listCacheServices(final EhCacheFactory cacheService) {
        for (final EhCacheFactory ehCacheFactory : cacheServicesList) {
            LOGGER.debug("listCacheServices()::  ehCacheFactory [{}]", ehCacheFactory.toString());
            if (ehCacheFactory.equals(cacheService)) {
                LOGGER.debug("SAME as currently bound cache service");
            } else {
                LOGGER.debug("DIFFERENT from currently bound cache service");
            }
        }
    }
    
    /**
     * activate() starts up the cachemanager and sets up a JMX bean for monitoring. It also creates a shutdownhook for
     * graceful shutdown. The diskStorePath is configurable in OSGi console so the property is used here to set the path
     * on activation.
     * @param context This the component context passed
     */
    @Activate
    public void activate(final ComponentContext context) {
        LOGGER.debug("activate():: Entry");
        String start;
        final String finish;
        
        try {
            cacheManagerConfig = new Configuration();
            
            // ehcache has a built in system to establish the base path for the diskstore:
            // "To support legacy configurations, four explicit string tokens are replaced with their associated
            // Java system property values. This substitution happens for the first matching token only (e.g.
            // java.io.tmpdir/ehcache/java.io.tmpdir â†’ /var/tmp/ehcache/java.io.tmpdir).
            // user.home - the user's home directory
            // user.dir - the current working directory
            // java.io.tmpdir - the default temp file path
            // ehcache.disk.store.dir - a system property you would normally specify on the command line, e.g.
            // java -Dehcache.disk.store.dir=/u01/myapp/diskdir
            // These, and all other system properties can also be substituted using the familiar syntax:
            // ${property-name}.
            // Using this syntax all token instances are replaced (e.g. ${java.io.tmpdir}/ehcache/${java.io.tmpdir} â†’
            // /var/tmp/ehcache/var/tmp).
            
            // Choice at Roche is to use ehcache.disk.store.dir as base variable, if it is set. Otherwise, we use
            // java.io.tmpdir.
            // Then, the 'default path' or a configured path is added.
            start = "${java.io.tmpdir}";
            if (System.getProperty("ehcache.disk.store.dir") != null) {
                start = "${ehcache.disk.store.dir}";
            }
            finish = PropertiesUtil.toString(context.getProperties().get("ehCacheManagerService.diskStorePath"),
                    EhCacheConstants.DEFAULT_DISK_STORE_PATH);
            diskStorePath = start + finish;
            
            if (diskStorePath != null) {
                diskStoreConfig = new DiskStoreConfiguration();
                diskStoreConfig.setPath(diskStorePath);
                cacheManagerConfig.addDiskStore(diskStoreConfig);
                
                LOGGER.debug("activate():: diskStorePath = [{}]", diskStorePath);
            }
            
            cacheManagerConfig.setName(CACHEMANAGERNAME);
            
            this.cacheManager = CacheManager.getCacheManager(CACHEMANAGERNAME);
            
            if (this.cacheManager == null) {
                LOGGER.debug("activate():: trying to create cachemanger with config [{}] and CACHEMANAGERNAME [{}]",
                        cacheManagerConfig.toString(), CACHEMANAGERNAME);
                
                cacheManager = new CacheManager(cacheManagerConfig);
                
                LOGGER.debug("activap():: ");
                cacheManager = CacheManager.getCacheManager(CACHEMANAGERNAME);
                if (cacheManager == null) {
                    LOGGER.debug("activate():: Unsuccessful - unable to create cachemanager [{}]", CACHEMANAGERNAME);
                } else {
                    LOGGER.debug("activate():: Successfully - created cachemanager [{}]", CACHEMANAGERNAME);
                }
            } else {
                LOGGER.debug("activate():: Cachemanager with CACHEMANAGERNAME [{}] already exists", CACHEMANAGERNAME);
            }
            
            this.addShutdownHook();
            
            final List<?> cacheManagers = CacheManager.ALL_CACHE_MANAGERS;
            
            for (final Object name : cacheManagers) {
                final CacheManager cm = (CacheManager) name;
                LOGGER.debug("activate():: List of cachemangers - cachmanagername = [{}] ", cm.getName());
            }
            
            // register the cache manager with JMX
            LOGGER.debug("activate():: Register the cacheManager [{}] with JMX as MBean", CACHEMANAGERNAME);
            ManagementService.registerMBeans(this.cacheManager, ManagementFactory.getPlatformMBeanServer(), true, true,
                    true, true, true);
            
            // log existing cacheNames
            final String[] existingCaches = cacheManager.getCacheNames();
            for (final String existingCache : existingCaches) {
                LOGGER.debug("activate():: list of cahces - cacheName = [{}]", existingCache);
            }
        } catch (final BusinessExecutionException ex) {
            LOGGER.error("activate():: Could not create cacheManager:", ex);
        }
    }
    
    /**
     * called while exiting cacheManager
     */
    @Deactivate
    public void deactivate() {
        LOGGER.debug("deactivate():: EhCacheManagerService:deactivate()::Entry");
        LOGGER.info("deactivate():: Shutting down CacheManager!!");
        if (this.cacheManager != null) {
            this.cacheManager.shutdown();
            this.cacheManager = null;
        }
        this.removeShutdownHook();
    }
    
    /**
     * Add shutdown hook for disposing of the CacheManager gracefully.
     */
    private void addShutdownHook() {
        final Thread localShutdownHook = new Thread() {
            
            /**
             * {@inherited}
             */
            @Override
            public void run() {
                /**
                 * clear shutdown hook reference to prevent deactivate to remove it during shutdown
                 */
                shutdownHook = null;
                deactivate();
            }
        };
        Runtime.getRuntime().addShutdownHook(localShutdownHook);
        this.shutdownHook = localShutdownHook;
    }
    
    /**
     * Remove the shutdown hook to prevent leaving orphaned CacheManagers around.
     */
    private void removeShutdownHook() {
        if (this.shutdownHook != null) {
            // remove shutdown hook
            try {
                final boolean ishookRemoved = Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
                LOGGER.info("removeShutdownHook():: Shutdown Hook Removed: {}", ishookRemoved);
            } catch (final IllegalStateException ex) {
                /**
                 * This will be thrown if the VM is shutting down. In this case we do not need to worry about leaving
                 * references to CacheManagers lying around and the call is OK to fail.
                 */
                LOGGER.error("removeShutdownHook():: IllegalStateException due to attempt to remove a shutdown "
                        + "hook while the VM is actually shutting down.", ex);
            }
            this.shutdownHook = null;
        }
    }
    
    /**
     * {@inheritDoc} <br />
     * <br />
     * This implementation takes the cacheName and instantiates an EhCacheInstance with a 'blocking' cache. It is this
     * blocking cache which is returned.<br />
     * <br />
     * If the cache cannot be instantiated (not available or not configured) this method will return null.
     */
    @Override
    public <T> CacheInstance<T> getCache(final String cacheName) {
        LOGGER.debug("getCache(String cacheName):: Entry");
        
        try {
            final Cache cache = cacheManager.getCache(cacheName);
            
            if (null == cache) {
                LOGGER.debug("getCache(String cacheName):: attempt to get cache [{}] returned null", cacheName);
                return null;
            }
            synchronized (ehCacheInstancesMap) {
                @SuppressWarnings("unchecked")
                final EhCacheInstance<T> cacheToReturn = this.ehCacheInstancesMap.get(cacheName);
                
                LOGGER.debug("getCache(String cacheName):: returning CacheInstance {}", cacheName);
                return cacheToReturn;
            }
        } catch (final BusinessExecutionException e) {
            LOGGER.error("getCache(String cacheName):: unable to instantiate cache [{}] exception = [{}]", cacheName, e);
            return null;
        }
    }
    
    @Override
    public Object get(final String cacheName, final String key) {
        
        final CacheInstance<String> cache = this.getCache(cacheName);
        
        if (cache == null) {
            LOGGER.debug("get(String cacheName, String key):: attempt to get cache [{}] returned null", cacheName);
            return null;
        } else {
            if (cache.containsKey(key)) {
                return cache.get(key);
            } else {
                return null;
            }
        }
        
    }
    
    @Override
    public void put(final String cacheName, final String key, final Object value) {
        try {
            final CacheInstance<Object> cache = this.getCache(cacheName);
            
            if (cache == null) {
                LOGGER.debug(
                        "put(String cacheName, String key, Object value):: attempt to get cache [{}] returned null",
                        cacheName);
            } else {
                cache.put(key, value);
            }
        } catch (final BusinessExecutionException e) {
            LOGGER.error(
                    "put(String cacheName, String key, Object value):: unable to instantiate cache [{}] exception = [{}]",
                    cacheName, e);
        }
    }
    
    @Override
    public void invalidate(final String cacheName, final String key) {
        try {
            final CacheInstance<String> cache = this.getCache(cacheName);
            
            if (cache == null) {
                LOGGER.debug("invalidate(String cacheName, String key):: attempt to get cache [{}] returned null",
                        cacheName);
            } else {
                if (cache.containsKey(key)) {
                    cache.remove(key);
                }
            }
        } catch (final BusinessExecutionException e) {
            LOGGER.error(
                    "invalidate(String cacheName, String key):: unable to instantiate cache [{}] exception = [{}]",
                    cacheName, e);
        }
    }
}
