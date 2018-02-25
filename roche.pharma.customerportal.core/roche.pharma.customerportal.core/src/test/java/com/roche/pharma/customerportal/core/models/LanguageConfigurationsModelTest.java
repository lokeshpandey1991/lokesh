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

public class LanguageConfigurationsModelTest {

    private static final String LANGUAGE_CONFIG_PATH = "/content/roche/customerportal/us/en/jcr:content/languageconfig";
    private static final String LANGUAGE_CONFIG__CN_PATH = "/content/roche/customerportal/cn/zh/jcr:content/languageconfig";
    private static final String LANGUAGE_CONFIG__NON_EXISTING_PATH = "/content/roche/customerportal/de/en/jcr:content/languageconfig";
    final static String rocheBasePath = "/content/roche/customerportal";
    @Rule
    public AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK).build();

    @Test
    public void testLanguageConfigurations() {
        final LanguageConfigurationsModel modelObject = context.resourceResolver().getResource(LANGUAGE_CONFIG_PATH)
                .adaptTo(LanguageConfigurationsModel.class);
        Assert.assertEquals("/content/roche/us", modelObject.getCountrySelectorPagePath());
        Assert.assertEquals("/content/roche/us", modelObject.getPersonaSelectorPagePath());
        Assert.assertEquals("/content/roche/us", modelObject.getSearchPagePath());
        Assert.assertEquals(true, modelObject.isPersonaDisabled());
        Assert.assertEquals(1, modelObject.getSocialList().size());
        Assert.assertEquals("MMM dd, yyyy", modelObject.getDateFormatPattern());
        Assert.assertEquals("admin@roche.com", modelObject.getFromEmail());
        Assert.assertEquals("languageconfig", modelObject.getComponentName());
        Assert.assertEquals("123", modelObject.getShareId());
        Assert.assertEquals(2, modelObject.getSocialShareLinks().length);
    }

    @Test
    public void testMultifiledNull() {
        final LanguageConfigurationsModel modelObject = context.resourceResolver().getResource(LANGUAGE_CONFIG__CN_PATH)
                .adaptTo(LanguageConfigurationsModel.class);
        Assert.assertTrue(modelObject.getSocialList().isEmpty());
    }

    @Test
    public void testLanguageConfigurationResource() {
        final Resource resource = context.resourceResolver().getResource(LANGUAGE_CONFIG_PATH);
        final LanguageConfigurationsModel modelObject = CommonUtils.getlanguageConfigurations(resource);
        Assert.assertEquals("/content/roche/us", modelObject.getCountrySelectorPagePath());
        Assert.assertEquals("20", modelObject.getSearchLimit());
        Assert.assertEquals("searchCollection", modelObject.getSearchCollection());
        Assert.assertEquals("searchUrl", modelObject.getSearchUrl());
        Assert.assertEquals("/content/roche/us", modelObject.getPersonaSelectorPagePath());
        Assert.assertEquals("/content/roche/us", modelObject.getSearchPagePath());
        Assert.assertEquals(true, modelObject.isPersonaDisabled());
        Assert.assertEquals(1, modelObject.getSocialList().size());
        Assert.assertEquals("MMM dd, yyyy", modelObject.getDateFormatPattern());
    }

    @Test
    public void testLanguageConfigurationResourceNull() {
        final Resource resource = context.resourceResolver().getResource(LANGUAGE_CONFIG__NON_EXISTING_PATH);
        final LanguageConfigurationsModel modelObject = CommonUtils.getlanguageConfigurations(resource);
        Assert.assertNull(modelObject);
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
            MockRocheContent.loadfile(context, "/json/roche/cn/cn.json", "/content/roche/customerportal/cn");

        }
    };

}
