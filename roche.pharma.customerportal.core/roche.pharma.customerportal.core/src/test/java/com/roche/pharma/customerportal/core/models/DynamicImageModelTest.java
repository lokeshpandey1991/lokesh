package com.roche.pharma.customerportal.core.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockRocheContent;
import com.roche.pharma.customerportal.core.services.impl.ConfigurationServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class DynamicImageModelTest {
    
    final static String DYNAMIC_IMAGE_RESOURCE_PATH = "/content/roche/customerportal/us/en/home/jcr:content/par/image";
    final static String DYNAMIC_IMAGE_RESOURCE_PATH_WITHOUT_HEIGHT = "/content/roche/customerportal/us/en/home/jcr:content/par/image_without_height";
    final static String DYNAMIC_IMAGE_RESOURCE_PATH_WITHOUT_HEIGHT1 = "/content/roche/customerportal/us/en/home/jcr:content/par/image_without_height1";
    final static String DYNAMIC_IMAGE_RESOURCE_PATH_WITHOUT_CROP = "/content/roche/customerportal/us/en/home/jcr:content/par/image_without_crop";
    final static String DYNAMIC_IMAGE_RESOURCE_PATH_WITH_CROP = "/content/roche/customerportal/us/en/home/jcr:content/par/image_crop";
    private static final String VIDEO_SERVICE_URL = "https://gateway-eu.assetsadobe.com/DMGateway/";
    private static final String IMAGE_SERVICE_URL = "https://roche-staging-h.assetsadobe2.com/is/image";
    private static final String[] DYNAMIC_MEDIA_CONFIGURATION = {
            "image-desktop=1200,800,0.2,0.6,0.8,0.9", "image-desktop-view1=1200,800", "image-desktop-view2=1200",
            "image-desktop-view3=1200,,0.2,0.6,0.8,0.9", "image-tabletP=500,450,0.2,0.6,0.8,0.9",
            "image-tabletL=600,400,0.2,0.6,0.8,0.9", "image-mobileP=200,150,0.2,0.6,0.8,0.9",
            "image-mobileL=300,100,0.2,0.6,0.8,0.9", "image-desktop-view4=,,0.2,0.6,0.8,0.9"
    };
    Map<String, Object> _config;
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    private ConfigurationServiceImpl ConfigurationServiceImpl = new ConfigurationServiceImpl();
    
    @Before
    public void setUp() throws Exception {
        _config = new HashMap<String, Object>();
        _config.put("image.service.url", IMAGE_SERVICE_URL);
        _config.put("video.service.url", VIDEO_SERVICE_URL);
        _config.put("media.service.configuration", DYNAMIC_MEDIA_CONFIGURATION);
        context.registerInjectActivateService(ConfigurationServiceImpl, _config);
    }
    
    @Test
    public void testGetAltText() {
        Resource res = context.request().getResourceResolver().getResource(DYNAMIC_IMAGE_RESOURCE_PATH);
        context.request().setResource(res);
        DynamicImageModel modelObject = context.request().adaptTo(DynamicImageModel.class);
        Assert.assertEquals("Roche Image", modelObject.getAltText());
    }
    
    @Test
    public void testGetView() {
        Resource res = context.request().getResourceResolver().getResource(DYNAMIC_IMAGE_RESOURCE_PATH_WITH_CROP);
        context.request().setResource(res);
        DynamicImageModel modelObject = context.request().adaptTo(DynamicImageModel.class);
        Assert.assertEquals("view4", modelObject.getView());
    }
    
    @Test
    public void testGetDynamicMediaUrl() {
        Resource res = context.request().getResourceResolver().getResource(DYNAMIC_IMAGE_RESOURCE_PATH);
        context.request().setResource(res);
        DynamicImageModel modelObject = context.request().adaptTo(DynamicImageModel.class);
        Assert.assertEquals(
                "https://roche-staging-h.assetsadobe2.com/is/image/content/dam/roche/customerportal/products/11/04/56/110456.jpg",
                modelObject.getFileReference());
    }
    
    @Test
    public void testGetDesktopUrl() {
        Resource res = context.request().getResourceResolver().getResource(DYNAMIC_IMAGE_RESOURCE_PATH);
        context.request().setResource(res);
        DynamicImageModel modelObject = context.request().adaptTo(DynamicImageModel.class);
        Assert.assertEquals(
                "https://roche-staging-h.assetsadobe2.com/is/image/content/dam/roche/customerportal/products/11/04/56/110456.jpg?"
                        + "wid=1200&hei=800&cropn=0.2,0.6,0.8,0.9",
                modelObject.getDesktopUrl());
        Assert.assertEquals(
                "https://roche-staging-h.assetsadobe2.com/is/image/content/dam/roche/customerportal/products/11/04/56/110456.jpg?"
                        + "wid=600&hei=400&cropn=0.2,0.6,0.8,0.9",
                modelObject.getTabletLandscapeUrl());
        Assert.assertEquals(
                "https://roche-staging-h.assetsadobe2.com/is/image/content/dam/roche/customerportal/products/11/04/56/110456.jpg?"
                        + "wid=500&hei=450&cropn=0.2,0.6,0.8,0.9",
                modelObject.getTabletPortraitUrl());
        Assert.assertEquals(
                "https://roche-staging-h.assetsadobe2.com/is/image/content/dam/roche/customerportal/products/11/04/56/110456.jpg?"
                        + "wid=300&hei=100&cropn=0.2,0.6,0.8,0.9",
                modelObject.getMobileLandscapeUrl());
        Assert.assertEquals(
                "https://roche-staging-h.assetsadobe2.com/is/image/content/dam/roche/customerportal/products/11/04/56/110456.jpg?"
                        + "wid=200&hei=150&cropn=0.2,0.6,0.8,0.9",
                modelObject.getMobilePortraitUrl());
    }
    
    @Test
    public void testGetDesktopUrlView1WithOutCrop() {
        Resource res = context.request().getResourceResolver().getResource(DYNAMIC_IMAGE_RESOURCE_PATH_WITHOUT_CROP);
        context.request().setResource(res);
        DynamicImageModel modelObject = context.request().adaptTo(DynamicImageModel.class);
        Assert.assertEquals(
                "https://roche-staging-h.assetsadobe2.com/is/image/content/dam/roche/customerportal/products/11/04/56/110456.jpg?wid=1200&hei=800",
                modelObject.getDesktopUrl());
    }
    
    @Test
    public void testGetDesktopUrlView1WithOutHeight() {
        Resource res = context.request().getResourceResolver().getResource(DYNAMIC_IMAGE_RESOURCE_PATH_WITHOUT_HEIGHT);
        context.request().setResource(res);
        DynamicImageModel modelObject = context.request().adaptTo(DynamicImageModel.class);
        Assert.assertEquals(
                "https://roche-staging-h.assetsadobe2.com/is/image/content/dam/roche/customerportal/products/11/04/56/110456.jpg?wid=1200",
                modelObject.getDesktopUrl());
    }
    
    @Test
    public void testGetDesktopUrlView1WithOutHeightWithCrop() {
        Resource res = context.request().getResourceResolver().getResource(DYNAMIC_IMAGE_RESOURCE_PATH_WITHOUT_HEIGHT1);
        context.request().setResource(res);
        DynamicImageModel modelObject = context.request().adaptTo(DynamicImageModel.class);
        Assert.assertEquals(
                "https://roche-staging-h.assetsadobe2.com/is/image/content/dam/roche/customerportal/products/11/04/56/110456.jpg?wid=1200&cropn=0.2,0.6,0.8,0.9",
                modelObject.getDesktopUrl());
    }
    
    @Test
    public void testGetDesktopUrlView1WithCrop() {
        Resource res = context.request().getResourceResolver().getResource(DYNAMIC_IMAGE_RESOURCE_PATH_WITH_CROP);
        context.request().setResource(res);
        DynamicImageModel modelObject = context.request().adaptTo(DynamicImageModel.class);
        Assert.assertEquals(
                "https://roche-staging-h.assetsadobe2.com/is/image/content/dam/roche/customerportal/products/11/04/56/110456.jpg?cropn=0.2,0.6,0.8,0.9",
                modelObject.getDesktopUrl());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.loadfile(context, "/json/roche/roche.json", "/content");
            MockRocheContent.loadfile(context, "/json/roche/us/us.json", "/content/roche/customerportal/us");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/home.json",
                    "/content/roche/customerportal/us/en/home");
        }
    };
    
}
