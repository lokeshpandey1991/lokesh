package com.roche.pharma.customerportal.core.models;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockRocheContent;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class LanguageHeaderFooterModelTest {
    
    private static final String GLOBAL_HEADER_FOOTER_PATH = "/content/roche/us/en/jcr:content/headerfooterconfig";
    private static final String GLOBAL_HEADER_FOOTER_CN_PATH = "/content/roche/cn/en/jcr:content/headerfooterconfig";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testGlobalHeaderFooterConfiguration() throws LoginException {
        LanguageHeaderFooterModel modelObject = context.resourceResolver().getResource(GLOBAL_HEADER_FOOTER_PATH)
                .adaptTo(LanguageHeaderFooterModel.class);
        Assert.assertEquals("copyright", modelObject.getTradeMark());
        Assert.assertEquals("disclaimer", modelObject.getDisclaimerText());
        Assert.assertEquals("/content/dam/roche/customerportal/products/11/04/58/110458.png",
                modelObject.getFileReference());
        Assert.assertEquals(1, modelObject.getFooterList().size());
        Assert.assertEquals(2, modelObject.getHamburgerMenuLeftList().size());
        Assert.assertEquals(2, modelObject.getHamburgerMenuRightList().size());
        Assert.assertEquals(2, modelObject.getHeaderList().size());
        Assert.assertEquals("/content/roche/us", modelObject.getHomePagePath());
        Assert.assertEquals("alt text", modelObject.getLogoAltText());
        Assert.assertEquals("headerfooterconfig", modelObject.getComponentName());
    }
    
    @Test
    public void testMutifieldNull() throws LoginException {
        LanguageHeaderFooterModel modelObject = context.resourceResolver().getResource(GLOBAL_HEADER_FOOTER_CN_PATH)
                .adaptTo(LanguageHeaderFooterModel.class);
        Assert.assertTrue(modelObject.getFooterList().isEmpty());
        
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.load(context);
        }
    };
    
}
