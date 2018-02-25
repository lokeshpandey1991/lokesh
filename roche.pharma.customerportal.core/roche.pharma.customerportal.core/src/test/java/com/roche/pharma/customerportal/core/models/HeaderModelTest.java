package com.roche.pharma.customerportal.core.models;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class HeaderModelTest {
    
    final static String DEFAULT_PATH = "/content/roche/customerportal/us/en/home-page";
    final static String PATH = "/content/roche/customerportal/ww/en/home-page";
    final static String rocheBasePath = "/content/roche";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testHeaderModelGetters() {
        final HeaderModel modelObject = context.resourceResolver().getResource(DEFAULT_PATH).adaptTo(HeaderModel.class);
        Assert.assertEquals("CUSTOMERPORTAL", modelObject.getCountryCode());
        Assert.assertEquals("/content/roche/us", modelObject.getCountrySelectorPath());
        Assert.assertEquals("/content/roche/us", modelObject.getSearchResultPath());
        Assert.assertEquals("searchUrl", modelObject.getSearchUrl());
        Assert.assertEquals("20", modelObject.getSearchLimit());
        Assert.assertEquals("/content/roche/us", modelObject.getPersonaSelectorPath());
        Assert.assertEquals(2, modelObject.getHeaderLinksList().size());
        Assert.assertEquals("/content/roche/us", modelObject.getHomePagePath());
        Assert.assertEquals("alt text", modelObject.getLogoAltText());
        Assert.assertEquals("/content/dam/roche/customerportal/products/11/04/58/110458.png", modelObject.getLogoPath());
        Assert.assertTrue(modelObject.isPersonaDisabled());
        Assert.assertNull(modelObject.getPersona());
    }
    
    @Test
    public void testHeaderModelGettersNull() {
        final HeaderModel modelObject = context.resourceResolver().getResource(PATH).adaptTo(HeaderModel.class);
        Assert.assertNull(modelObject.getCountrySelectorPath());
        Assert.assertNull(modelObject.getLogoPath());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            context.load().json("/json/roche-testContent.json", rocheBasePath);
            // MockRocheContent.load(context);
        }
    };
    
}
