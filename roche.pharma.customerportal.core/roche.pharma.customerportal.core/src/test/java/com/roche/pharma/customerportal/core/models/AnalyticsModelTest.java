package com.roche.pharma.customerportal.core.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.services.impl.AnalyticsConfigurationSeriveImpl;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class AnalyticsModelTest {
    final static String homePagePath = "/content/customerportal/us/en/home-page/jcr:content";
    final static String productpagePath = "/content/customerportal/us/en/home-page/products-page/products/roche54353/jcr:content";
    
    Map<String, Object> _config;
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    private AnalyticsConfigurationSeriveImpl analyticsConfigurationService = new AnalyticsConfigurationSeriveImpl();
    
    @Before
    public void setUp() throws Exception {
        _config = new HashMap<String, Object>();
        _config.put("analytics.service.url", "adobe.analytics.com");
        _config.put("analytics.server.name", "localhost");
        analyticsConfigurationService = context.registerInjectActivateService(analyticsConfigurationService, _config);
        analyticsConfigurationService.activate(_config);
    }
    
    @Test
    public void testSectionLevelTwo() throws LoginException {
        final AnalyticsModel analyticsModel = context.resourceResolver().getResource(productpagePath)
                .adaptTo(AnalyticsModel.class);
        Assert.assertEquals("products-page", analyticsModel.getSectionLevelTwo());
    }
    
    @Test
    public void testSectionLevelThree() throws LoginException {
        final AnalyticsModel analyticsModel = context.resourceResolver().getResource(productpagePath)
                .adaptTo(AnalyticsModel.class);
        Assert.assertEquals("products", analyticsModel.getSectionLevelThree());
    }
    
    @Test
    public void testSectionLevelOne() throws LoginException {
        final AnalyticsModel analyticsModel = context.resourceResolver().getResource(homePagePath)
                .adaptTo(AnalyticsModel.class);
        Assert.assertEquals("home-page", analyticsModel.getSectionLevelOne());
    }
    
    @Test
    public void testAnalyticsUrl() throws LoginException {
        final AnalyticsModel analyticsModel = context.resourceResolver().getResource(homePagePath)
                .adaptTo(AnalyticsModel.class);
        Assert.assertEquals("adobe.analytics.com", analyticsModel.getAnalyticsUrl());
    }
    
    @Test
    public void testServername() throws LoginException {
        final AnalyticsModel analyticsModel = context.resourceResolver().getResource(homePagePath)
                .adaptTo(AnalyticsModel.class);
        Assert.assertEquals("localhost", analyticsModel.getServerName());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            // MockRocheContent.load(context);
            context.load().json("/json/roche.json", "/content/customerportal");
        }
    };
}
