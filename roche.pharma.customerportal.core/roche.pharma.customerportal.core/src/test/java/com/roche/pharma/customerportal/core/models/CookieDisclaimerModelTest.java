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

public class CookieDisclaimerModelTest {
    
    final static String homepage = "/content/customerportal/global-master-blueprint/en/home";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testProductDetailNameModel() throws LoginException {
        CookieDisclaimerModel modelObject = context.resourceResolver().getResource(homepage + "/jcr:content")
                .adaptTo(CookieDisclaimerModel.class);
        Assert.assertEquals(
                "/content/customerportal/global-master-blueprint/en/home/product-category-page/product-listing-new/pdp.html",
                modelObject.getUrl());
        Assert.assertEquals(true,modelObject.isCookieNotificationDisabled());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/cookiedisclaimer.json", "/content/customerportal");
        }
    };
    
}
