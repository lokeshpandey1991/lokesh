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

public class CarouselModelTest {
    
    final static String dataPath = "/content/roche/us/en/home-page/jcr:content/carousel";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testSecTitle() throws LoginException {
        CarouselModel modelObject = context.resourceResolver().getResource(dataPath).adaptTo(CarouselModel.class);
        Assert.assertEquals("Roche Title", modelObject.getSecTitle());
    }
    
    @Test
    public void testButtonTitle() throws LoginException {
        CarouselModel modelObject = context.resourceResolver().getResource(dataPath).adaptTo(CarouselModel.class);
        Assert.assertEquals("Learn More", modelObject.getButtonTitle());
    }
    
    @Test
    public void testAssetHeading() throws LoginException {
        CarouselModel modelObject = context.resourceResolver().getResource(dataPath).adaptTo(CarouselModel.class);
        Assert.assertEquals(1, modelObject.getAssetList().size());
    }
    
    @Test
    public void testCarouselData() throws LoginException {
        CarouselModel modelObject = context.resourceResolver().getResource(dataPath).adaptTo(CarouselModel.class);
        Assert.assertTrue(modelObject.getCarouselData() != null);
        Assert.assertEquals("home", modelObject.getAssetList().get(0).getContentType());
        Assert.assertEquals("carousel", modelObject.getComponentName());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.load(context);
        }
    };
}
