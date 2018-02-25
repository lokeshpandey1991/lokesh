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

public class SocialFeedModelTest {
    
    final static String socialFeedNodePath = "/content/customerportal/us/en/home";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testProductDetailNameModel() throws LoginException {
        SocialFeedModel modelObject = context.resourceResolver()
                .getResource(socialFeedNodePath + "/jcr:content/socialfeed").adaptTo(SocialFeedModel.class);
        Assert.assertEquals("This is limit authored from the dialog", "5", modelObject.getLimit());
        Assert.assertEquals("There user name from the dialog authored.", "@Roche", modelObject.getUserName());
        Assert.assertEquals("This is the name of component.", "socialfeed", modelObject.getComponentName());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/socialfeed.json", socialFeedNodePath);
            
        }
    };
}
