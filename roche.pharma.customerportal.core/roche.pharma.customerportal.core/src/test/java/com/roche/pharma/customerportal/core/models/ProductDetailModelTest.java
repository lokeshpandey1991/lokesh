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

public class ProductDetailModelTest {
    
    final static String productNodePath = "/content/roche/customerportal/us/en/home/roche54353";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testProductDetailModel() throws LoginException {
        ProductDetailModel modelObject = context.resourceResolver().getResource(productNodePath + "/jcr:content")
                .adaptTo(ProductDetailModel.class);
        Assert.assertEquals("11876333122", modelObject.getProductId());
        Assert.assertEquals("<!--#include virtual=\"/product/en_US/11876333122.json\" -->",
                modelObject.getProductSSIPath());
        Assert.assertEquals("<!--#include virtual=\"/productmeta/en_US/11876333122.json\" -->",
                modelObject.getProductMeta());
        Assert.assertEquals("Logo1", modelObject.getLogoName());
        Assert.assertEquals("Logo1 Description", modelObject.getLogoDescription());
        Assert.assertEquals("/content/dam/roche/customerportal/products/11/04/56/110456.jpg", modelObject.getLogoPath());
        Assert.assertEquals(true, modelObject.getIsExists());
        Assert.assertEquals(false, modelObject.isDTLEnabled());
        Assert.assertEquals(false, modelObject.isProductMetaEnabled());
        Assert.assertEquals(false, modelObject.isElabDocEnabledForAssays());
        Assert.assertEquals(false, modelObject.isElabDocEnabled());
    }
    
    @Test
    public void testFeatureProduct() throws LoginException {
        ProductDetailModel modelObject = context.resourceResolver().getResource(productNodePath + "/jcr:content")
                .adaptTo(ProductDetailModel.class);
        Assert.assertEquals("yes", modelObject.getFeaturedProduct());
        Assert.assertEquals("no", modelObject.getNewProduct());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/pdp.json", productNodePath);
        }
    };
    
}
