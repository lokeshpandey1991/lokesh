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

public class HamburgerMenuModelTest {
    final static String DEFAULT_PATH = "/content/roche/customerportal/us/en/home-page";
    final static String PATH = "/content/roche/customerportal/ww/en/home-page";
    final static String EMPTY_FOOTER_PATH = "/content/roche/customerportal/cn/en/home-page";
    final static String rocheBasePath = "/content/roche";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testHamburgerMenuModelGetters() {
        final HamburgerMenuModel modelObject = context.resourceResolver().getResource(DEFAULT_PATH)
                .adaptTo(HamburgerMenuModel.class);
        Assert.assertEquals("CUSTOMERPORTAL", modelObject.getCountryCode());
        Assert.assertEquals("/content/roche/us", modelObject.getCountrySelectorPath());
        Assert.assertEquals("/content/roche/us", modelObject.getPersonaSelectorPath());
        Assert.assertTrue(modelObject.isPersonaDisabled());
        Assert.assertEquals(2, modelObject.getHamburgerMenuLeftLinks().size());
        Assert.assertEquals("_self", modelObject.getHamburgerMenuLeftLinks().get(0).getLinkBehaviour());
        Assert.assertEquals(2, modelObject.getHamburgerMenuRightLinks().size());
        Assert.assertEquals("English", modelObject.getLanguageCode());
        Assert.assertEquals("alt text", modelObject.getLogoAltText());
    }
    
    @Test
    public void testHamburgerMenuModelGettersNull() {
        final HamburgerMenuModel modelObject = context.resourceResolver().getResource(PATH)
                .adaptTo(HamburgerMenuModel.class);
        Assert.assertNull(modelObject.getCountrySelectorPath());
    }
    
    @Test
    public void testHamburgerMenuModelEmptyFooterList() {
        final HamburgerMenuModel modelObject = context.resourceResolver().getResource(EMPTY_FOOTER_PATH)
                .adaptTo(HamburgerMenuModel.class);
        Assert.assertEquals(2, modelObject.getHamburgerMenuRightLinks().size());
    }
    
    @Test
    public void testHamburgerMenuModelNullRegionalPage() {
        final HamburgerMenuModel modelObject = context.resourceResolver().getResource("/content")
                .adaptTo(HamburgerMenuModel.class);
        Assert.assertNull(modelObject.getCountryCode());
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
