package com.roche.pharma.customerportal.core.models;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockRocheContent;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class GlobalConfigurationModelTest {
    
    private static final String GLOBAL_CONFIG_PATH = "/content/customerportal/jcr:content/par/globalconfig";
    private static final String GLOBAL_CONFIG_NON_EXISTING_PATH = "/content/customerportal/jcr:content/globalconfig";
    final static String rocheBasePath = "/content/roche";
    
    @Rule
    public AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK).build();
    
    @Test
    public void testGlobalConfigurations() {
        GlobalConfigurationsModel modelObject = context.resourceResolver().getResource(GLOBAL_CONFIG_PATH)
                .adaptTo(GlobalConfigurationsModel.class);
        Assert.assertEquals("/content/roche/customerportal/us/en/home", modelObject.getWebsitePath());
        Assert.assertEquals("AIzaSyAWxty08aW6_xCCrYkOuckYvlKMMm8R8_U", modelObject.getGoogleMapKey());
        
    }
    
    @Test
    public void testGetGlobalConfigurationsResource() {
        Resource resource = context.resourceResolver().getResource(GLOBAL_CONFIG_PATH);
        GlobalConfigurationsModel modelObject = CommonUtils.getGlobalConfigurations(resource);
        Assert.assertEquals("/content/roche/customerportal/us/en/home", modelObject.getWebsitePath());
    }
    
    @Test
    public void testGetGlobalConfigurationsResourceNull() {
        Resource resource = context.resourceResolver().getResource(GLOBAL_CONFIG_NON_EXISTING_PATH);
        GlobalConfigurationsModel modelObject = CommonUtils.getGlobalConfigurations(resource);
        Assert.assertNull(modelObject);
    }
    
    @Test
    public void testComponentName() {
        GlobalConfigurationsModel modelObject = context.resourceResolver().getResource(GLOBAL_CONFIG_PATH)
                .adaptTo(GlobalConfigurationsModel.class);
        Assert.assertEquals("globalconfig", modelObject.getComponentName());
    }
    
    @Test
    public void testElabUrl() {
        GlobalConfigurationsModel modelObject = context.resourceResolver().getResource(GLOBAL_CONFIG_PATH)
                .adaptTo(GlobalConfigurationsModel.class);
        Assert.assertEquals("https://pim-eservices.roche.com/eLD_SF/be/en/Documents", modelObject.getElabURL());
    }
    
    @Test
    public void testContentType() {
        GlobalConfigurationsModel modelObject = context.resourceResolver().getResource(GLOBAL_CONFIG_PATH)
                .adaptTo(GlobalConfigurationsModel.class);
        Assert.assertEquals("", modelObject.getContentType());
    }
    
    @Test
    public void testDTLConfig() {
        GlobalConfigurationsModel modelObject = context.resourceResolver().getResource(GLOBAL_CONFIG_PATH)
                .adaptTo(GlobalConfigurationsModel.class);
        Assert.assertEquals(true, modelObject.isDTLEnabled());
        Assert.assertEquals(true, modelObject.isProductMetaEnabled());
        Assert.assertEquals(false, modelObject.isElabDocEnabledForAssays());
        Assert.assertEquals(false, modelObject.isElabDocEnabled());
        Assert.assertEquals(false, modelObject.isCaptchaDisabled());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.loadfile(context, "/json/roche/roche.json", "/content/customerportal");
        }
    };
    
}
