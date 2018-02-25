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

public class ProductSysSpecificationModelTest {
    
    final static String dataPath = "/content/roche/us/en/home-page/jcr:content/systemspecification";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testSecTitle() throws LoginException {
        ProductSysSpecificationModel modelObject = context.resourceResolver().getResource(dataPath)
                .adaptTo(ProductSysSpecificationModel.class);
        Assert.assertEquals("Roche Title", modelObject.getSecTitle());
    }
    
    @Test
    public void testComponentName() throws LoginException {
        ProductSysSpecificationModel modelObject = context.resourceResolver().getResource(dataPath)
                .adaptTo(ProductSysSpecificationModel.class);
        Assert.assertEquals("systemspecification", modelObject.getComponentName());
    }
    
    @Test
    public void testMoreLink() throws LoginException {
        ProductSysSpecificationModel modelObject = context.resourceResolver().getResource(dataPath)
                .adaptTo(ProductSysSpecificationModel.class);
        Assert.assertEquals(true, modelObject.getMoreLinkCheck());
    }
    
    @Test
    public void testSysSpecData() throws LoginException {
        ProductSysSpecificationModel modelObject = context.resourceResolver().getResource(dataPath)
                .adaptTo(ProductSysSpecificationModel.class);
        Assert.assertTrue(modelObject.getProSysDataList() != null);
        Assert.assertEquals("subdescrition test", modelObject.getProSysDataList().get(0).getBodyText());
        Assert.assertEquals("subheading test", modelObject.getProSysDataList().get(0).getSubHeading());
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
