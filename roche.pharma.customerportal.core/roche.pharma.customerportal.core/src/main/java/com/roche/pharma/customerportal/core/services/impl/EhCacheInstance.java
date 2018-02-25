package com.roche.pharma.customerportal.core.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.BlockingCache;
import net.sf.ehcache.statistics.StatisticsGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roche.pharma.customerportal.core.constants.EhCacheConstants;
import com.roche.pharma.customerportal.core.services.CacheInstance;

/**
 * Implementation for CacheInstance interface, internally this uses the appropriate classes of net.sf.ehcache in order
 * to perform the low level operations.
 * @param <T> the parameter of the class
 */
public class EhCacheInstance<T> implements CacheInstance<T> {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EhCacheInstance.class);
    
    private static final String FILE_SEPARATOR = "/";
    /**
     * Percentage to find out hits and misses.
     */
    private static final int PERCENTAGE = 100;
    
    /**
     * Mod to calculate when to do a stats check.
     */
    private static final int MOD_THOUSAND = 1000;
    
    /**
     * Name of the cache. This should refer to a cache that has already been created with a configuration for the
     * EhCacheFactory using a sling:OsgiConfig node. Suggested approach is to use the following node naming convention.
     * Use the PID of the class and the name of your cache :
     * "com.roche.pharma.customerportal.core.services.EhCacheFactory-CACHENAME"
     */
    private final String cacheName;
    
    /**
     * EHCache reference held by this EhCacheInstance.
     */
    private final Ehcache cache;
    
    /**
     * Number of times a miss has occured on this cache.
     */
    private long miss;
    
    /**
     * Number of hits in the cache.
     */
    private long hits;
    
    /**
     * Number of get calls to this cache.
     */
    private long gets;
    
    /**
     * Constructor initialising the EhCacheInstance
     * @param cache value to be used
     * @param lockTimeoutInMillis value to be used
     */
    public EhCacheInstance(final Cache cache, final int lockTimeoutInMillis) {
        this.cacheName = cache.getName();
        this.cache = new BlockingCache(cache, EhCacheConstants.DEFAULT_NUMBER_OF_STRIPES);
        // Setting lockTimeOut in milliseconds. The default value of 0 means that no lockTimeout is configured which
        // is the default value. Also typecasting is done since the method is specific implementation of BlockingCache.
        ((BlockingCache) this.cache).setTimeoutMillis(lockTimeoutInMillis);
    }
    
    /**
     * Constructor initialising the EhCacheInstance
     * @param cacheName the cache name
     */
    public EhCacheInstance(final String cacheName) {
        this.cacheName = cacheName;
        this.cache = CacheManager.getCacheManager(EhCacheConstants.DEFAULT_CACHE_MANAGER_NAME).getCache(cacheName);
    }
    
    /**
     * {@inheritDoc} <br />
     * <br />
     * @see com.roche.pharma.customerportal.core.services.CacheInstance#clear()
     */
    @Override
    public void clear() {
        this.cache.removeAll();
    }
    
    /**
     * {@inheritDoc} <br />
     * <br />
     * @see com.roche.pharma.customerportal.core.services.CacheInstance#containsKey(java.lang.String)
     */
    @Override
    public boolean containsKey(final String key) {
        return this.cache.isKeyInCache(key);
    }
    
    /**
     * {@inheritDoc} <br />
     * <br />
     * @see com.roche.pharma.customerportal.core.services.CacheInstance#get(java.lang.String)
     */
    @Override
    public T get(final String key) {
        final Element element = this.cache.get(key);
        final T cachedInstance = element == null ? this.stats(null) : this.stats(element.getObjectValue());
        if (cachedInstance == null) {
            this.cache.put(new Element(key, null));
        }
        
        return cachedInstance;
    }
    
    /**
     * When it's necessary to get more information about the cached entry it can be more convenient to use the Element
     * object and the numerous methods it provides. <br />
     * <br />
     * @param key value is be used.
     * @return the Object Value.
     * @see net.sf.ehcache.Element
     */
    public Element getElement(final String key) {
        return this.cache.get(key);
    }
    
    /**
     * Records stats
     * @param objectValue
     * @return the Object Value
     */
    @SuppressWarnings("unchecked")
    private T stats(final Object cachedInstance) {
        if (cachedInstance == null) {
            this.miss++;
        } else {
            this.hits++;
        }
        this.gets++;
        if (this.gets % EhCacheInstance.MOD_THOUSAND == 0) {
            final long hitPercentage = EhCacheInstance.PERCENTAGE * this.hits / this.gets;
            final long missPercentage = EhCacheInstance.PERCENTAGE * this.miss / this.gets;
            LOGGER.debug("{} CCacheInstanceStats hits {} ({}%), misses {} ({}%), calls {}", new Object[] {
                    this.cacheName, this.hits, hitPercentage, this.miss, missPercentage, this.gets
            });
        }
        return (T) cachedInstance;
    }
    
    /**
     * Get the StatisticsGateway for statistics on the usage of the cache.
     * @see net.sf.ehcache.statistics.StatisticsGateway If it is considered useful, stats can be provided with wrapper
     *      methods in a future implementation of this class
     */
    public StatisticsGateway getStats() {
        final StatisticsGateway stats = cache.getStatistics();
        final Long hitcount = stats.cacheHitCount();
        LOGGER.debug("getStats() overall hitcount = [{}]", hitcount.toString());
        return stats;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public T put(final String key, final T instance) {
        T previous = null;
        if (this.cache.isKeyInCache(key)) {
            final Element element = this.cache.get(key);
            if (element != null) {
                previous = (T) element.getObjectValue();
            }
        }
        this.cache.put(new Element(key, instance));
        return previous;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(final String key) {
        return this.cache.remove(key);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeChildren(final String key) {
        StringBuilder keyToRemove = new StringBuilder(key);
        this.cache.remove(keyToRemove);
        if (!keyToRemove.toString().endsWith(EhCacheInstance.FILE_SEPARATOR)) {
            keyToRemove = keyToRemove.append(EhCacheInstance.FILE_SEPARATOR);
        }
        final List<?> keys = this.cache.getKeys();
        for (final Object currentKey : keys) {
            if (((String) currentKey).startsWith(keyToRemove.toString())) {
                this.cache.remove(keyToRemove);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<T> values() {
        final List<?> keys = this.cache.getKeys();
        final List<T> values = new ArrayList<>();
        for (final Object key : keys) {
            final Element element = this.cache.get(key);
            if (element != null) {
                values.add((T) element.getObjectValue());
            }
        }
        return values;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<T> keys() {
        return this.cache.getKeys();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "EhCacheInstance of an EhCache [cache=" + this.cache.toString() + "]";
    }
    
    /**
     * Method to put a collection of key value pair into Ehcache directly. Method iterates over the passed KeyValue pair
     * map and converts to list of {@link Element} to invoke putAll method of Ehcache. Note: Preferably please pass an
     * implementation of LinkedHashMap to the putAll method instead of a HashMap.
     * @param dataElements Map of Key value pairs where key is of string type and value of type T.
     * @param <T> Type of value.
     */
    @Override
    public <T> void putAll(final Map<String, T> dataElements) {
        
        final List<Element> elementCollections = dataElements.entrySet().stream()
                .map(entry -> new Element(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        
        this.cache.putAll(elementCollections);
        
    }
    
}
