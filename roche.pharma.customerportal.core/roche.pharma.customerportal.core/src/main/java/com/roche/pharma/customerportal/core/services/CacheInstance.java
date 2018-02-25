package com.roche.pharma.customerportal.core.services;

import java.util.Collection;
import java.util.Map;

/**
 * A CacheInstance should be managed by the cache manager service. It can be used to hold an instance of all types of
 * cache such as ehcache, jccache or jcscache as long as the implementation is provided.
 * @param <T> the parameter of the class
 */
public interface CacheInstance<T> {
    
    /**
     * put is used to put an object into cache with particular key.
     * @param key key is to be used
     * @param value value is to be used
     * @return return the value
     */
    T put(String key, T value);
    
    /**
     * Test for a non expired entry in the cache.
     * @param key The cache key.
     * @return true if the key maps to a non-expired cache entry, false if not.
     */
    boolean containsKey(String key);
    
    /**
     * Get the non expired entry, or null if not there (or expired)
     * @param key The cache key.
     * @return The cached data, or null if key is not found (NB - one should use containsKey() to remove ambiguity
     *         between exists and expired).
     */
    T get(String key);
    
    /**
     * Clear all entries.
     */
    void clear();
    
    /**
     * Remove this entry from the cache.
     * @param key The cache key.
     * @return return the value
     */
    boolean remove(String key);
    
    /**
     * Remove the key and any child keys from the cache, this is an expensive operation.
     * @param key The cache key.
     */
    void removeChildren(String key);
    
    /**
     * Returns the values available in the cache.
     * @return collection of values
     */
    Collection<T> values();
    
    /**
     * Returns the keys available in the cache.
     * @return collection of keys
     */
    Collection<T> keys();
    
    /**
     * Method to put a collection of key value pair into Ehcache directly. Note: Preferably please pass an
     * implementation of LinkedHashMap to the putAll method instead of a HashMap.
     * @param dataElements Map of Key value pairs where key is of string type and value of type T.
     * @param <T> Type of value.
     */
    <T> void putAll(Map<String, T> dataElements);
}
