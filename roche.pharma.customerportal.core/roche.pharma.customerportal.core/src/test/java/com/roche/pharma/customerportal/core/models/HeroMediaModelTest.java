package com.roche.pharma.customerportal.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.resourceresolver.MockResourceResolverFactory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockRocheContent;

public class HeroMediaModelTest {
    
    final static String mediaPath = "/content/roche/us/en/home-page/jcr:content/heroMedia";
    final static String heroMediaInternal = "/content/roche/us/en/home-page/jcr:content/heroMedia1";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testFileReference() throws LoginException {
        HeroMediaModel modelObject = context.resourceResolver().getResource(mediaPath).adaptTo(HeroMediaModel.class);
        Assert.assertEquals("/content/dam/roche/customerportal/products/13/82/63/138263.jpg",
                modelObject.getFileReference());
    }
    
    @Test
    public void testContentType() throws LoginException {
        HeroMediaModel modelObject = context.resourceResolver().getResource(heroMediaInternal).adaptTo(HeroMediaModel.class);
        Assert.assertEquals("home",
                modelObject.getContentType());
    }
    
    @Test
    public void testCTALink() throws LoginException {
        HeroMediaModel modelObject = context.resourceResolver().getResource(mediaPath).adaptTo(HeroMediaModel.class);
        Assert.assertEquals("https://www.google.com", modelObject.getCTALink());
        
    }
    
    @Test
    public void testIsExternal() throws LoginException {
        HeroMediaModel modelObject = context.resourceResolver().getResource(mediaPath).adaptTo(HeroMediaModel.class);
        Assert.assertEquals("true", modelObject.getIsExternal());
        
    }
    
    @Test
    public void testAltText() throws LoginException {
        HeroMediaModel modelObject = context.resourceResolver().getResource(mediaPath).adaptTo(HeroMediaModel.class);
        Assert.assertEquals("this is hero image", modelObject.getAltText());
    }
    
    @Test
    public void testHeadline() throws LoginException {
        HeroMediaModel modelObject = context.resourceResolver().getResource(mediaPath).adaptTo(HeroMediaModel.class);
        Assert.assertEquals("headlne", modelObject.getHeadline());
    }
    
    @Test
    public void testSubHeadline() throws LoginException {
        HeroMediaModel modelObject = context.resourceResolver().getResource(mediaPath).adaptTo(HeroMediaModel.class);
        Assert.assertEquals("sub-headline2", modelObject.getSubHeadline());
    }
    
    @Test
    public void testHeadlinePosition() throws LoginException {
        HeroMediaModel modelObject = context.resourceResolver().getResource(mediaPath).adaptTo(HeroMediaModel.class);
        Assert.assertEquals("topleft", modelObject.getHeadlinePosition());
    }
    
    @Test
    public void testTextPosition() throws LoginException {
        HeroMediaModel modelObject = context.resourceResolver().getResource(mediaPath).adaptTo(HeroMediaModel.class);
        Assert.assertEquals("rightInsideImage", modelObject.getTextPosition());
    }
    
    @Test
    public void testQuoteText() throws LoginException {
        HeroMediaModel modelObject = context.resourceResolver().getResource(mediaPath).adaptTo(HeroMediaModel.class);
        Assert.assertEquals("this is quote", modelObject.getQuoteText());
    }
    
    @Test
    public void testAssetType() throws LoginException {
        HeroMediaModel modelObject = context.resourceResolver().getResource(mediaPath).adaptTo(HeroMediaModel.class);
        Assert.assertEquals("image", modelObject.getAssetType());
        
    }
    
    @Test
    public void testVariationType() throws LoginException {
        HeroMediaModel modelObject = context.resourceResolver().getResource(mediaPath).adaptTo(HeroMediaModel.class);
        Assert.assertEquals("heroBanner", modelObject.getVariationType());
        
    }
    
    @Test
    public void testTransparentOverlay() throws LoginException {
        HeroMediaModel modelObject = context.resourceResolver().getResource(mediaPath).adaptTo(HeroMediaModel.class);
        Assert.assertEquals("false", modelObject.getTransparentOverlay());
        
    }
    
    @Test
    public void testComponentName() throws LoginException {
        HeroMediaModel modelObject = context.resourceResolver().getResource(mediaPath).adaptTo(HeroMediaModel.class);
        Assert.assertEquals("heroMedia", modelObject.getComponentName());
        
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.load(context);
            context.load().json("/json/image.json", "/content/dam/roche/customerportal/products/13/82/63");
        }
    };
}
