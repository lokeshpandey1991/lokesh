package com.roche.pharma.customerportal.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.day.cq.wcm.api.WCMException;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;

public class MetaTagModelTest {
    
    final static String pdpPath = "/content/customerportal/us/en/home/pdp/jcr:content";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testMetaModel() throws LoginException {
        final MetaTagModel modelObject = context.request().adaptTo(MetaTagModel.class);
        Assert.assertNotNull(modelObject.getMetaDataValues());
        Assert.assertNotNull(modelObject.getListingMeta());
        Assert.assertEquals("false", modelObject.getShowFeaturedProduct());
        Assert.assertEquals("false", String.valueOf(modelObject.isEnableSSI()));
    }
    
    @Test
    public void testListingTag() throws LoginException {
        MockRocheContent.loadfile(context, "/json/roche/us/pages/persona-page.json",
                "/content/customerportal/us/en/home/persona-page");
        final Resource res = context.request().getResourceResolver()
                .getResource("/content/customerportal/us/en/home/persona-page/jcr:content");
        context.request().setResource(res);
        final MetaTagModel modelObject = context.request().adaptTo(MetaTagModel.class);
        Assert.assertNotNull(modelObject.getListingMeta());
        Assert.assertEquals(
                "<!--#include virtual=\"/searchnpromote/sp10050fca/en_US/products/hTopicsL1/Blood+Management.json\" -->",
                modelObject.getSnPSSIUrl());
        Assert.assertEquals("true", String.valueOf(modelObject.isEnableSSI()));
        
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context) throws PersistenceException, IOException,
                javax.jcr.LoginException, RepositoryException, WCMException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.loadfile(context, "/json/roche/roche.json", "/content/customerportal");
            MockRocheContent.loadfile(context, "/json/roche/us/us.json", "/content/customerportal/us");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/pdp.json", "/content/customerportal/us/en/home/pdp");
            context.load().json("/json/roche/tags.json", "/etc/tags");
            final Resource res = context.request().getResourceResolver().getResource(pdpPath);
            context.request().setResource(res);
        }
    };
}
