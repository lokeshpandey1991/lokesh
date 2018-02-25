package com.roche.pharma.customerportal.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockRocheContent;

public class SystemSpecificationsModelTest {
    
    final static String productNodePath = "/content/roche/customerportal/us/en/home/roche54353";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testProductDetailModel() throws LoginException {
        SystemSpecificationsModel modelObject = context.resourceResolver().getResource(productNodePath + "/jcr:content")
                .adaptTo(SystemSpecificationsModel.class);
        Assert.assertEquals(
                "https://pim-eservices.roche.com/eLD_SF/be/en/Documents/GetDocument?documentId=b1192884-1d78-e711-5790-00215a9b3428",
                modelObject.getSystemSpecificationDoc());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/pdp.json", productNodePath);
        }
    };
    
}
