package com.roche.pharma.customerportal.core.services.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class EhCacheManagerServiceImplTest {
    private static EhCacheManagerServiceImpl cacheService = new EhCacheManagerServiceImpl();
    final static EhCacheFactory cacheFactory = new EhCacheFactory();
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testEhCacheManger() {
        
        Map<String, Object> _config;
        _config = new HashMap<String, Object>();
        _config.put("ehcache.cacheName", "example");
        _config.put("persisted", "true");
        final Map<String, String> dataElements = new HashMap<String, String>();
        dataElements.put("junit1", "junit1");
        dataElements.put("junit2", "junit2");
        context.registerInjectActivateService(cacheService, _config);
        
        cacheService.bindEhCacheFactory(cacheFactory);
        cacheService.put("example", "test", "true");
        cacheService.put("invalidCache", "test", "true");
        Assert.assertEquals("true", cacheService.get("example", "test"));
        Assert.assertEquals(null, cacheService.get("example", "invlaidkey"));
        Assert.assertEquals(null, cacheService.get("invalidcache", "invalid"));
        final EhCacheInstance<Object> ehCacheInstanceInvalid = (EhCacheInstance<Object>) cacheService
                .getCache("invalid");
        final EhCacheInstance<Object> ehCacheInstance = (EhCacheInstance<Object>) cacheService.getCache("example");
        Assert.assertEquals(true, ehCacheInstance.containsKey("test"));
        Assert.assertEquals("true", ehCacheInstance.getElement("test").getValue());
        ehCacheInstance.put("ehcache", "test");
        ehCacheInstance.putAll(dataElements);
        Assert.assertEquals(true, ehCacheInstance.remove("ehcache"));
        Assert.assertEquals(null, ehCacheInstance.get("invalidkey"));
        Assert.assertEquals("true", ehCacheInstance.put("test", "abc"));
        Assert.assertEquals(3, ehCacheInstance.getStats().getSize());
        Assert.assertEquals(true, ehCacheInstance.containsKey("test"));
        Assert.assertEquals(true, ehCacheInstance.values().contains("junit1"));
        Assert.assertEquals(true, ehCacheInstance.keys().contains("junit1"));
        ehCacheInstance.toString();
        ehCacheInstance.clear();
        Assert.assertEquals(false, ehCacheInstance.containsKey("test"));
        ehCacheInstance.removeChildren("ehcache/");
        
        cacheService.unbindEhCacheFactory(cacheFactory);
        
    }
    
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.registerService(EhCacheManagerServiceImpl.class, cacheService);
            Map<String, Object> _configFactory;
            _configFactory = new HashMap<String, Object>();
            _configFactory.put("ehcache.cacheName", "example");
            _configFactory.put("persisted", "true");
            context.registerInjectActivateService(cacheFactory, _configFactory);
        }
    };
    
}
