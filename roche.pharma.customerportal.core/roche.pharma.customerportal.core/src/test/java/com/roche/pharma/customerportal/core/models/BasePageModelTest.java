package com.roche.pharma.customerportal.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.WCMException;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.mock.MockExteranlizer;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;

public class BasePageModelTest {
    
    final static String pdpPath = "/content/customerportal/us/en/home/pdp/jcr:content";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testBaseModel() throws LoginException {
        final BasePageModel modelObject = context.request().adaptTo(BasePageModel.class);
        Assert.assertEquals("no", modelObject.getFeaturedProduct());
        Assert.assertEquals("no", modelObject.getNewProduct());
        Assert.assertEquals(RocheConstants.ROBOT_VALUE, modelObject.getRobots());
        Assert.assertEquals("en_US", modelObject.getLocale());
        Assert.assertEquals("en", modelObject.getLanguage());
        Assert.assertEquals("US", modelObject.getCountry());
        Assert.assertEquals("product", modelObject.getPageType());
        Assert.assertEquals("contact-sales", modelObject.getFormType());
        // Assert.assertEquals("Jul 28, 2017 18:10:09 IST", modelObject.getReplicatedDate());
        Assert.assertEquals("alt text", modelObject.getAltText());
        Assert.assertEquals("/content/dam/roche/customerportal/products/product-7.png", modelObject.getFileReference());
        Assert.assertEquals("Cobas® 6800 System", modelObject.getTitle());
        Assert.assertTrue(StringUtils.isNotEmpty(modelObject.getEventDateRange()));
        Assert.assertEquals("Munich, Germany", modelObject.getLocation());
        Assert.assertTrue(StringUtils.isNotEmpty(modelObject.getEventDateFormat()));
        Assert.assertEquals("/content/customerportal/us/en/home/pdp.html", modelObject.getCurrentPageURL());
        Assert.assertEquals("Webinar", modelObject.getEventType());
        Assert.assertEquals("This is product page", modelObject.getDescription());
        Assert.assertTrue(StringUtils.isNotEmpty(modelObject.getReplicatedDate()));
        Assert.assertEquals("researcher,healthcare", modelObject.getPersonaTag());
        Assert.assertEquals("roche:customerportal/persona/researcher", modelObject.getPersonaTags()[0]);
        Assert.assertEquals("medicalValue", modelObject.getStoryType());
    }
    
    @Test
    public void testBaseModelDefaultPersona() throws LoginException {
        context.load().json("/json/roche/tags.json", "/etc/tags/customerportal/persona");
        final Resource res = context.request().getResourceResolver()
                .getResource("/content/customerportal/us/en/home/persona-page/jcr:content");
        context.request().setResource(res);
        final BasePageModel modelObject = context.request().adaptTo(BasePageModel.class);
        Assert.assertNotNull(modelObject.getPersonaTag());
        
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
            MockRocheContent.loadfile(context, "/json/roche/us/pages/persona-page.json",
                    "/content/customerportal/us/en/home/persona-page");
            context.load().json("/json/roche/tags.json", "/etc/tags/roche");
            final Resource res = context.request().getResourceResolver().getResource(pdpPath);
            context.request().setResource(res);
            context.registerService(Externalizer.class, new MockExteranlizer());
        }
    };
}
