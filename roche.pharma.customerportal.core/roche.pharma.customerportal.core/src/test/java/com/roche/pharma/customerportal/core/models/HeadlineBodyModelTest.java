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

public class HeadlineBodyModelTest {
    
    final static String dataPath = "/content/roche/us/en/home-page/jcr:content/headlineBody";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testHeadlineType() throws LoginException {
        HeadlineBodyModel modelObject = context.resourceResolver().getResource(dataPath)
                .adaptTo(HeadlineBodyModel.class);
        Assert.assertEquals("h1", modelObject.getHeadlineType());
    }
    
    @Test
    public void testHeadlinePosition() throws LoginException {
        HeadlineBodyModel modelObject = context.resourceResolver().getResource(dataPath)
                .adaptTo(HeadlineBodyModel.class);
        Assert.assertEquals("top", modelObject.getHeadlinePosition());
    }
    
    @Test
    public void testHeadlineText() throws LoginException {
        HeadlineBodyModel modelObject = context.resourceResolver().getResource(dataPath)
                .adaptTo(HeadlineBodyModel.class);
        Assert.assertEquals("Headingtest", modelObject.getHeadlineText());
    }
    
    @Test
    public void testAuthorName() throws LoginException {
        HeadlineBodyModel modelObject = context.resourceResolver().getResource(dataPath)
                .adaptTo(HeadlineBodyModel.class);
        Assert.assertEquals("authortest", modelObject.getAuthorName());
    }
    
    @Test
    public void testPublishDate() throws LoginException {
        HeadlineBodyModel modelObject = context.resourceResolver().getResource(dataPath)
                .adaptTo(HeadlineBodyModel.class);
        Assert.assertTrue(modelObject.getPublishDate() != null);
    }
    
    @Test
    public void testBodyText() throws LoginException {
        HeadlineBodyModel modelObject = context.resourceResolver().getResource(dataPath)
                .adaptTo(HeadlineBodyModel.class);
        Assert.assertEquals("<p>This is new testing</p>", modelObject.getBodyText());
    }
    
    @Test
    public void testSocialMedia() throws LoginException {
        HeadlineBodyModel modelObject = context.resourceResolver().getResource(dataPath)
                .adaptTo(HeadlineBodyModel.class);
        Assert.assertEquals(true, modelObject.getSocialMedia());
    }
    
    @Test
    public void testTags() throws LoginException {
        HeadlineBodyModel modelObject = context.resourceResolver().getResource(dataPath)
                .adaptTo(HeadlineBodyModel.class);
        Assert.assertTrue(modelObject.getTagsId().length != 0);
    }
    
    @Test
    public void testLocalTagTitles() throws LoginException {
        HeadlineBodyModel modelObject = context.resourceResolver().getResource(dataPath)
                .adaptTo(HeadlineBodyModel.class);
        Assert.assertTrue(modelObject.getLocalTagTitles() != null);
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.load(context);
        }
    };
}
