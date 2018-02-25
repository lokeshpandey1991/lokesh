package com.roche.pharma.customerportal.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockRocheContent;

public class ProductDescriptionModelTest {
    final static String PDP_PATH = "/content/roche/customerportal/us/en/home/pdp/jcr:content/productDescription";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testHeading() {
        ProductDescriptionModel modelObject = context.resourceResolver().getResource(PDP_PATH)
                .adaptTo(ProductDescriptionModel.class);
        Assert.assertEquals("Headline", modelObject.getProductDescriptionHeading());
    }
    
    @Test
    public void testDescription() {
        ProductDescriptionModel modelObject = context.resourceResolver().getResource(PDP_PATH)
                .adaptTo(ProductDescriptionModel.class);
        Assert.assertEquals("This is sample description", modelObject.getProductDescription());
    }
    
    @Test
    public void testViewFlag() {
        ProductDescriptionModel modelObject = context.resourceResolver().getResource(PDP_PATH)
                .adaptTo(ProductDescriptionModel.class);
        Assert.assertEquals("false", String.valueOf(modelObject.getIsColumnView()));
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForClasses(ProductDescriptionModel.class);
            MockRocheContent.loadfile(context, "/json/roche/roche.json", "/content/roche/customerportal");
            MockRocheContent.loadfile(context, "/json/roche/us/us.json", "/content/roche/customerportal/us");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/pdp.json",
                    "/content/roche/customerportal/us/en/home/pdp");
        }
    };
    
}
