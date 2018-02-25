package com.roche.pharma.customerportal.core.constants;

/**
 * EhCacheConstants contains constants for EH Cache implementation.
 * @author matschin
 */

public class EhCacheConstants {
    
    public static final String DEFAULT_CACHE_MANAGER_NAME = "roche-pharma-customerportal";
    
    public static final String DEFAULT_DISK_STORE_PATH = "tmp/cache/ehcache/";
    
    public static final String DEFAULT_DISK_STORE_PATH_DESCRIPTION = "String - The path to the directory where data for the 'persisted' caches"
            + " will be stored.  -- The path configured here will be added to one of two base paths coming from the java system"
            + " properties (either ehcache.disk.store.dir or java.io.tmpdir). If the setting for ehcache.disk.store.dir has been set"
            + " (Java system settings can be set on the command line e.g., java -Dehcache.disk.store.dir=/opt/cq/tmpdir) it will be"
            + " used by default.  Otherwise, the value for the Java temp directory (java.io.tmpdir) is used as the base.  The String"
            + " set here in the OSGi configuration is added to the base path (Final path = basepath + diskStorePath).    "
            + " NB -- Changes made here are NOT DYNAMIC i.e., it is necessary to restart the bundle (stop and restart the core bundle)"
            + " for a change to the diskstore path to take effect.";
    
    public static final String DEFAULT_CACHE_NAME = "defaultCache";
    
    public static final Integer DEFAULT_NUMBER_OF_STRIPES = 128;
    
    public static final String NUMBER_OF_STRIPES_DESCRIPTION = "numberOfStripes is for blocking caches. It defines how many stripes to use"
            + " the keys against. It must be a non-zero even number. This is a trade-off between memory use and concurrency - a"
            + " higher number uses more memory but also provides for more concurent requests.";
    
    public static final String EH_CACHE_FACTORY_DESCRIPTION = "A factory configuration class which provides a way to create multiple EhCache"
            + " instances via osgi factory configurataions. Each configuration is the source of a cache, created as a service that is"
            + " managed in the EhCacheManagerService where the 'cacheName' can be used to reference it. NB - when making a change to"
            + " a configuration (via web console or by changing the sling:OsgiConfig node) the cache will be 'removed' and then "
            + " 'created' anew.";
    
    public static final String CACHE_NAME_DESCRIPTION = "String - The name of the cache. -- This must be unique as it is used to reference the"
            + " cache and it will serve as the identifier when creating and removing the cache from the EhCacheManagerService.";
    
    public static final String MAX_ENTRIES_LOCAL_HEAP_DESCRIPTION = "Integer - The maximum entries to be held in memory. -- This defines"
            + " the maximum number of elements to be held in JVM memory and therefore should be calculated in relation to the total"
            + " memory available in the JVM.  The actual memory footprint depends on the amount of data in each element.  This setting"
            + " replaces 'maxElementsInMemory' which has been deprecated. If the total desired cache size is too big to be stored in "
            + " memory it is also possibe to 'overFlowToDisk' by turning on the 'persisted' option and setting 'maxEntriesLocalDisk'"
            + " to 0 (no limit) or to a higher number than the entry for 'maxEntriesLocalHeap' entered here.";
    
    public static final String MEMORY_STORE_EVICTION_POLICY_DESCRIPTION = "String representation of the policy used to evict elements from"
            + " the MemoryStore.. One of 'LRU', 'LFU' or 'FIFO'. The default value is 'LRU'";
    
    public static final String ETERNAL_DESCRIPTION = "Whether elements are eternal. If eternal, timeouts ('timeToLiveSeconds' and"
            + " 'timeToIdleSeconds') are ignored and elements never expire.";
    
    public static final String TIME_TO_LIVE_SECONDS_DESCRIPTION = "Long - The maximum number of seconds an element can exist in the cache "
            + " regardless of use. The element expires at this limit and will no longer be returned from the cache. -- It is only"
            + " used if the element is not eternal. The default value is 0, which means no TTL eviction takes place (infinite lifetime)."
            + " A value of 0 means do not check time to live.";
    
    public static final String TIME_TO_IDLE_SECONDS_DESCRIPTION = "Long - The maximum number of seconds an element can exist in the cache without"
            + " being accessed. The element expires at this limit and will no longer be returned from the cache. -- Is only used"
            + " if the element is not eternal. The default value is 0, which means no TTI eviction takes place (infinite lifetime)."
            + " A value of 0 means do not check for idling.";
    
    public static final String DISK_PERSISTENCE_DESCRIPTION = "Whether elements are persisted to disk using 'Standard open source (non"
            + " fault-tolerant) on-disk persistence'. -- If set to 'true', cached data is saved to disk asynchronously and can"
            + " be recovered after a clean shutdown or planned flush. To prevent corrupt or inconsistent data from being"
            + " returned, EhCache performs checking measures upon a restart, and if any discrepancy is found, the cache that"
            + " was stored on disk is emptied and must be reloaded from the data source.";
    
    public static final String MAX_ENTRIES_LOCAL_DISK_DESCRIPTION = "Integer - Defines the max number of elements that are stored on disk. -- A"
            + " setting of 0 means 'no limit'. In order to use a strategy of 'overFlowToDisk', turn on 'persisted' and set a number"
            + " here for 'maxEntriesLocalDisk' which is higher than the 'maxEntriesLocalHeap' setting. All of the entries will be "
            + " persisted but only a limited number will be directly accessible in the JVM memory.";
    
    public static final String DISK_SEARCHABLE_DESCRIPTION = "Whether the key and value fields are searchable via the ehcache query api. --"
            + " It should be noted that the searchable option is only available if the cache is NOT persisted.  If the cache is"
            + " persisted, this configuration will be ignored.";
    
    public static final String LOCK_TIMEOUT_DESCRIPTION = "Integer - If multiple threads are trying to access a key which"
            + "does not exist in cache, the blocking cache will block all the threads. In order to avoid blocking and cache"
            + "throwing LockTimeoutException, this field has to be provided with a positive time in milliseconds after which"
            + "the exception will be thrown. Please note that the value should be positive and very small. Default value of"
            + "0 is considered as no lockTimeout specified. This field is very specific to BlockingCache implementation.";
    
}
