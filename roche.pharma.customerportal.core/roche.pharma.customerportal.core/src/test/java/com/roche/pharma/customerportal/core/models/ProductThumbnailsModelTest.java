package com.roche.pharma.customerportal.core.models;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.beans.ProductThumbnails;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class ProductThumbnailsModelTest {
    final static String pdthPath = "/content/roche/us/en/home-page/products-page/jcr:content/parCatalog/productThumbnailsList";
    final static String pdthPath1 = "/content/roche/us/en/home-page/products-page/jcr:content/parCatalog/productThumbnailsList1";
    final static String pdthPath2 = "/content/roche/us/en/home-page/products-page/jcr:content/parCatalog/productThumbnailsList2";
    final static String pdpPath = "/content/roche/customerportal/us/en/home/pdp/jcr:content";
    
    final static String[] mediaLinksData = {
            "{\"productTitle1\":\"test\",\"altText\":\"Product Image1\",\"productPath1\":\"/content/customerportal/global-master-blueprint/en/home/roche54353\",\"fileReference1\":\"/content/dam/roche/customerportal/products/carouselmachine_01.png\"}",
            "{\"productTitle2\":\"test\",\"altText\":\"Product Image2\",\"productPath2\":\"/content/customerportal/global-master-blueprint/en/home/roche54353\",\"fileReference2\":\"/content/dam/roche/customerportal/products/carouselmachine_02.png\"}"
    };
    
    final static ProductThumbnails productThumbnails = new ProductThumbnails();
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testMediaListData() throws LoginException {
        ProductThumbnailsModel modelObject = context.resourceResolver().getResource(pdthPath)
                .adaptTo(ProductThumbnailsModel.class);
        Assert.assertEquals("text1", modelObject.getProductThumbnailsList().get(0).getAltText());
        Assert.assertEquals("test1", modelObject.getProductThumbnailsList().get(0).getProductTitle());
        Assert.assertEquals("/content/roche/customerportal/us/en/home/pdp.html",
                modelObject.getProductThumbnailsList().get(0).getProductPath());
        Assert.assertEquals("/content/dam/customerportal/cobas.png",
                modelObject.getProductThumbnailsList().get(0).getFileReference());
        
    }
    
    @Test
    public void testMediaListDataNew() throws LoginException {
        ProductThumbnailsModel modelObject = context.resourceResolver().getResource(pdthPath1)
                .adaptTo(ProductThumbnailsModel.class);
        Assert.assertEquals("alt text", modelObject.getProductThumbnailsList().get(0).getAltText());
        Assert.assertEquals("Cobas® 6800 System", modelObject.getProductThumbnailsList().get(0).getProductTitle());
        Assert.assertEquals("/content/roche/customerportal/us/en/home/pdp.html",
                modelObject.getProductThumbnailsList().get(0).getProductPath());
        Assert.assertEquals("/content/dam/roche/customerportal/products/product-7.png",
                modelObject.getProductThumbnailsList().get(0).getFileReference());
        
    }
    
    @Test
    public void testMediaListDataNewTwo() throws LoginException {
        ProductThumbnailsModel modelObject = context.resourceResolver().getResource(pdthPath2)
                .adaptTo(ProductThumbnailsModel.class);
        Assert.assertEquals("text1", modelObject.getProductThumbnailsList().get(0).getAltText());
        Assert.assertEquals("test1", modelObject.getProductThumbnailsList().get(0).getProductTitle());
        Assert.assertEquals("/content/dam/customerportal/cobas.png",
                modelObject.getProductThumbnailsList().get(0).getFileReference());
        
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
            MockRocheContent.loadfile(context, "/json/roche/roche.json", "/content/roche/customerportal");
            MockRocheContent.loadfile(context, "/json/roche/us/us.json", "/content/roche/customerportal/us");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/pdp.json",
                    "/content/roche/customerportal/us/en/home/pdp");
            context.load().json("/json/roche/tags.json", "/etc/tags/roche");
            final Resource res = context.request().getResourceResolver().getResource(pdpPath);
            context.request().setResource(res);
        }
    };
    
}
