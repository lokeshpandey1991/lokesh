package com.roche.pharma.customerportal.core.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

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

public class VideoModelTest {
    
    final static String RESOURCE_PATH = "/content/roche/customerportal/us/en/pdp/jcr:content/video";
    final static String RESOURCE_PATH2 = "/content/roche/customerportal/us/en/pdp/jcr:content/video2";
    final static String PATH = "/content/roche/customerportal/ww/en/home-page";
    final static String rocheBasePath = "/content/roche";
    private static final String VIDEO_SERVICE_URL = "https://progressive-eu.assetsadobe.com/rochestaging";
    private static final String IMAGE_SERVICE_URL = "https://roche-staging-h.assetsadobe2.com/is/image";
    Map<String, Object> _config;
    private ConfigurationServiceImpl ConfigurationServiceImpl = new ConfigurationServiceImpl();
    
    @Before
    public void setUp() throws Exception {
        _config = new HashMap<String, Object>();
        _config.put("image.service.url", IMAGE_SERVICE_URL);
        _config.put("video.service.url", VIDEO_SERVICE_URL);
        context.registerInjectActivateService(ConfigurationServiceImpl, _config);
    }
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testVideoModelGetters() {
        VideoModel modelObject = context.resourceResolver().getResource(RESOURCE_PATH).adaptTo(VideoModel.class);
        Assert.assertEquals(
                "https://progressive-eu.assetsadobe.com/rochestaging/_renditions_/37a/37af2177-30e8-433e-bf34-bcfc4f42e55a/avs/demo-0x720-2600k.mp4",
                modelObject.getAssetPath());
        Assert.assertEquals("sample headline", modelObject.getHeadline());
        Assert.assertEquals("/content/dam/roche/customerportal/hero-banner.png", modelObject.getPosterLink());
        Assert.assertEquals("false", modelObject.getTransparentOverlay());
    }
    
    @Test
    public void testVideoModelGettersExternal() {
        VideoModel modelObject = context.resourceResolver().getResource(RESOURCE_PATH2).adaptTo(VideoModel.class);
        Assert.assertEquals(
                "https://progressive-eu.assetsadobe.com/rochestaging/_renditions_/2f1/2f195f9e-f0ac-470a-8f8f-fd6c9de5ea0f/avs/small.mp4",
                modelObject.getAssetPath());
        Assert.assertEquals("sample headline", modelObject.getHeadline());
        Assert.assertEquals("/content/dam/roche/customerportal/hero-banner.png", modelObject.getPosterLink());
        Assert.assertEquals("false", modelObject.getTransparentOverlay());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.loadfile(context, "/json/roche/roche.json", "/content");
            MockRocheContent.loadfile(context, "/json/roche/us/us.json", "/content/roche/customerportal/us");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/pdp.json", "/content/roche/customerportal/us/en/pdp");
            MockRocheContent.loadfile(context, "/json/roche/us/dam/image.json",
                    "/content/dam/roche/customerportal/demo.mp4");
        }
    };
    
}
