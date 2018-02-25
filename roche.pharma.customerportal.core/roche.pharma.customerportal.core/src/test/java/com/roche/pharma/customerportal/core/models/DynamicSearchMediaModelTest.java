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

public class DynamicSearchMediaModelTest {

    private static final String[] DYNAMIC_MEDIA_CONFIGURATION = {
            "productTile-desktop=1200,800,0.2,0.6,0.8,0.9", "image-desktop-view1=1200,800", "image-desktop-view2=1200",
            "featuredTile-desktop-view3=1200,,0.2,0.6,0.8,0.9", "image-tabletP=500,450,0.2,0.6,0.8,0.9",
            "image-tabletL=600,400,0.2,0.6,0.8,0.9", "image-mobileP=200,150,0.2,0.6,0.8,0.9",
            "image-mobileL=300,100,0.2,0.6,0.8,0.9", "image-desktop-view4=,,0.2,0.6,0.8,0.9"
    };
    
    final static String DYNAMIC_IMAGE_RESOURCE_PATH = "/content/roche/customerportal/us/en/home/jcr:content/par/image";
    private static final String IMAGE_SERVICE_URL = "https://roche-staging-h.assetsadobe2.com/is/image";
    Map<String, Object> _config;

    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    private final ConfigurationServiceImpl ConfigurationServiceImpl = new ConfigurationServiceImpl();

    @Before
    public void setUp() throws Exception {
        _config = new HashMap<String, Object>();
        _config.put("image.service.url", IMAGE_SERVICE_URL);
        _config.put("media.service.configuration", DYNAMIC_MEDIA_CONFIGURATION);
        context.registerInjectActivateService(ConfigurationServiceImpl, _config);
    }

    @Test
    public void testGetRendition() {
        final DynamicSearchMediaModel modelObject = context.resourceResolver().getResource(DYNAMIC_IMAGE_RESOURCE_PATH)
                .adaptTo(DynamicSearchMediaModel.class);
        Assert.assertEquals("https://roche-staging-h.assetsadobe2.com/is/image", modelObject.getImageServiceUrl());
        Assert.assertTrue(!modelObject.getRenditionMap().isEmpty());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.loadfile(context, "/json/roche/roche.json", "/content");
            MockRocheContent.loadfile(context, "/json/roche/us/us.json", "/content/roche/customerportal/us");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/home.json",
                    "/content/roche/customerportal/us/en/home");
        }
    };

}
