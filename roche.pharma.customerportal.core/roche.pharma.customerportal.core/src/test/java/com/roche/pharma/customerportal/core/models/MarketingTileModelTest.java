package com.roche.pharma.customerportal.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;
import java.io.IOException;
import javax.jcr.RepositoryException;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;

public class MarketingTileModelTest {
    
    final static String marketingTilePath = "/content/roche/us/en/home-page/jcr:content/marketingTile";
    final static String marketingTilePathBlank = "/content/roche/us/en/home-page/jcr:content/marketingTileBlank";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testContentType() throws LoginException {
        MarketingTileModel modelObjectBlank = context.resourceResolver().getResource(marketingTilePathBlank)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("home", modelObjectBlank.getContentType());
    }
    
    @Test
    public void testArticleId() throws LoginException {
        
        MarketingTileModel modelObject = context.resourceResolver().getResource(marketingTilePath)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("/content/roche/us/en/home-page/products-page/products/roche54353.html",
                modelObject.getArticleId());
        MarketingTileModel modelObjectBlank = context.resourceResolver().getResource(marketingTilePathBlank)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("", modelObjectBlank.getArticleId());
        
    }
    
   
    
    @Test
    public void testHeadline() throws LoginException {
        
        MarketingTileModel modelObject = context.resourceResolver().getResource(marketingTilePath)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("headlne", modelObject.getHeadline());
    }
    
    @Test
    public void testArticleTitle() throws LoginException {
        MarketingTileModel modelObject = context.resourceResolver().getResource(marketingTilePath)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("Coba", modelObject.getArticleTitle());
        MarketingTileModel modelObjectBlank = context.resourceResolver().getResource(marketingTilePathBlank)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("title", modelObjectBlank.getArticleTitle());
    }
    
    
    @Test
    public void testArticleSummary() throws LoginException {
        MarketingTileModel modelObject = context.resourceResolver().getResource(marketingTilePath)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("article Summary", modelObject.getArticleSummary());
    }
    
    @Test
    public void testCtaLabel() throws LoginException {
        MarketingTileModel modelObject = context.resourceResolver().getResource(marketingTilePath)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("label", modelObject.getCtaLabel());
    }
    
    @Test
    public void testCtaLink() throws LoginException {
        MarketingTileModel modelObject = context.resourceResolver().getResource(marketingTilePath)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("www.google.com", modelObject.getCtaLink());
    }
    
    @Test
    public void testFileReference() throws LoginException {
        MarketingTileModel modelObject = context.resourceResolver().getResource(marketingTilePath)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("/content/dam/roche/customerportal/products/11/04/58/110458.png",
                modelObject.getFileReference());
        MarketingTileModel modelObjectBlank = context.resourceResolver().getResource(marketingTilePathBlank)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("/content/dam/roche/customerportal/products/13/82/63/138263.jpg",
                modelObjectBlank.getFileReference());
        
    }
    
    @Test
    public void testAltText() throws LoginException {
        MarketingTileModel modelObject = context.resourceResolver().getResource(marketingTilePath)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("alt text", modelObject.getAltText());
        MarketingTileModel modelObjectBlank = context.resourceResolver().getResource(marketingTilePathBlank)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("this is hero image", modelObjectBlank.getAltText());
    }
    
    @Test
    public void testImagePosition() throws LoginException {
        MarketingTileModel modelObject = context.resourceResolver().getResource(marketingTilePath)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("left", modelObject.getImagePosition());
    }
    
    @Test
    public void testVariationType() throws LoginException {
        MarketingTileModel modelObject = context.resourceResolver().getResource(marketingTilePath)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("marketingTile", modelObject.getVariationType());
    }
    
    @Test
    public void testComponentName() throws LoginException {
        MarketingTileModel modelObject = context.resourceResolver().getResource(marketingTilePath)
                .adaptTo(MarketingTileModel.class);
        Assert.assertEquals("marketingTile", modelObject.getComponentName());
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