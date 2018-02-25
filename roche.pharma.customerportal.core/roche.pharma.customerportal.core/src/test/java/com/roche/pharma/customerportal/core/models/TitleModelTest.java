package com.roche.pharma.customerportal.core.models;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.adobe.xmp.schema.rng.model.Context;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.WCMException;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class TitleModelTest {
    
    final static String RESOURCE_PATH = "/content/roche/us/en/home/jcr:content/title";
    final static String RESOURCE_PATH2 = "/content/roche/us/en/pdp/jcr:content/title2";
    

    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testTitleModelGetters() {
        context.load().json("/json/roche/us/pages/home.json", RESOURCE_PATH);
        TitleModel modelObject = context.resourceResolver().getResource(RESOURCE_PATH).adaptTo(TitleModel.class);
        Assert.assertEquals("rdoe_title.News", modelObject.getPageType());
        Assert.assertEquals("home page", modelObject.getTitle());
    }
    
    @Test
    public void testTitleModelPageType() {
        context.load().json("/json/roche/us/pages/pdp.json", RESOURCE_PATH2);
        TitleModel modelObject = context.resourceResolver().getResource(RESOURCE_PATH2).adaptTo(TitleModel.class);
        Assert.assertEquals("News_Health", modelObject.getPageType());
        Assert.assertEquals("ROCHE54353", modelObject.getTitle());
    }
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException, WCMException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
        }
    };
    
}
