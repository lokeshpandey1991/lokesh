package com.roche.pharma.customerportal.core.services;

import org.osgi.service.component.ComponentContext;

/**
 * Interface that allows for the implementation of services that provide factory configurations for multiple cache
 * instances (CacheInstance) such as Ehcache or JCS cache.
 * @author asingh131
 */
@FunctionalInterface
public interface CacheFactory {
    
    /**
     * Gets the ComponentContext for this CacheFactory Config
     * @return ComponentContext
     */
    ComponentContext getContext();
    
}
