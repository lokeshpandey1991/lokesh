/**
 * 
 */
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

/**
 * @author agu207
 */
public class ImageGalleryTest {
    
    final static String galleryPath = "/content/roche/us/en/home-page/jcr:content/imagegallery";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testFileReference1() throws LoginException {
        ImageGallery modelObject = context.resourceResolver().getResource(galleryPath).adaptTo(ImageGallery.class);
        Assert.assertEquals("/content/dam/roche/customerportal/products/13/82/63/138263.jpg",
                modelObject.getFileReference1());
    }
    
    @Test
    public void testAltText1() throws LoginException {
        ImageGallery modelObject = context.resourceResolver().getResource(galleryPath).adaptTo(ImageGallery.class);
        Assert.assertEquals("this is test image", modelObject.getAltText1());
    }
    
    @Test
    public void testFileReference2() throws LoginException {
        ImageGallery modelObject = context.resourceResolver().getResource(galleryPath).adaptTo(ImageGallery.class);
        Assert.assertEquals("/content/dam/roche/customerportal/products/13/82/63/138263.jpg",
                modelObject.getFileReference2());
    }
    
    @Test
    public void testAltText2() throws LoginException {
        ImageGallery modelObject = context.resourceResolver().getResource(galleryPath).adaptTo(ImageGallery.class);
        Assert.assertEquals("this is test image", modelObject.getAltText2());
    }
    
    @Test
    public void testFileReference3() throws LoginException {
        ImageGallery modelObject = context.resourceResolver().getResource(galleryPath).adaptTo(ImageGallery.class);
        Assert.assertEquals("/content/dam/roche/customerportal/products/13/82/63/138263.jpg",
                modelObject.getFileReference3());
    }
    
    @Test
    public void testAltText3() throws LoginException {
        ImageGallery modelObject = context.resourceResolver().getResource(galleryPath).adaptTo(ImageGallery.class);
        Assert.assertEquals("this is test image", modelObject.getAltText3());
    }
    
    @Test
    public void testFileReference4() throws LoginException {
        ImageGallery modelObject = context.resourceResolver().getResource(galleryPath).adaptTo(ImageGallery.class);
        Assert.assertEquals("/content/dam/roche/customerportal/products/13/82/63/138263.jpg",
                modelObject.getFileReference4());
    }
    
    @Test
    public void testAltText4() throws LoginException {
        ImageGallery modelObject = context.resourceResolver().getResource(galleryPath).adaptTo(ImageGallery.class);
        Assert.assertEquals("this is test image", modelObject.getAltText4());
    }
    
    @Test
    public void testFileReference5() throws LoginException {
        ImageGallery modelObject = context.resourceResolver().getResource(galleryPath).adaptTo(ImageGallery.class);
        Assert.assertEquals("/content/dam/roche/customerportal/products/13/82/63/138263.jpg",
                modelObject.getFileReference5());
    }
    
    @Test
    public void testAltText5() throws LoginException {
        ImageGallery modelObject = context.resourceResolver().getResource(galleryPath).adaptTo(ImageGallery.class);
        Assert.assertEquals("this is test image", modelObject.getAltText5());
    }
    
    @Test
    public void testFileReference6() throws LoginException {
        ImageGallery modelObject = context.resourceResolver().getResource(galleryPath).adaptTo(ImageGallery.class);
        Assert.assertEquals("/content/dam/roche/customerportal/products/13/82/63/138263.jpg",
                modelObject.getFileReference6());
    }
    
    @Test
    public void testAltText6() throws LoginException {
        ImageGallery modelObject = context.resourceResolver().getResource(galleryPath).adaptTo(ImageGallery.class);
        Assert.assertEquals("this is test image", modelObject.getAltText6());
    }
    
    @Test
    public void testImageHeadline() throws LoginException {
        ImageGallery modelObject = context.resourceResolver().getResource(galleryPath).adaptTo(ImageGallery.class);
        Assert.assertEquals("Image headline", modelObject.getImageHeadline());
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
