package com.roche.pharma.customerportal.core.services;

/**
 * The CacheManagerService provides the base for the cache manager implementations such as EhCacheManagerService. A
 * cacheManagerService can be implemented to provide a service for managing caches. An abstrat class 'getCache()' should
 * be overridden to provide a way to get the specific type of cache needed.
 */
public interface CacheManagerService {
    
    /**
     * Get a cache to contain a specified type. The cache returned by this method maintains a reference to the
     * CacheInstance which serves all the low level operations. All operations invoked on this cache are dispatched to
     * the underlying CacheInstance.
     * @param <T> The type of the elements.
     * @param name of the cache.
     * @return the appropriate cache (based on implementation)
     */
    <T> CacheInstance<T> getCache(String name);
    
    /**
     * get method returns data form a particular cache based on cache key.
     * @param cacheName the value is to be used
     * @param key the key is to be used.
     * @return Object return the object
     */
    Object get(String cacheName, String key);
    
    /**
     * put method is used to put data into a particular cache based on cache key.
     * @param cacheName the value is to be used
     * @param key the key is to be used.
     * @param value return the object
     */
    void put(String cacheName, String key, Object value);
    
    /**
     * invalidate method is used to invalidate data from a particular cache based on cache key.
     * @param cacheName the value is to be used
     * @param key the key is to be used.
     */
    void invalidate(String cacheName, String key);
    
}
