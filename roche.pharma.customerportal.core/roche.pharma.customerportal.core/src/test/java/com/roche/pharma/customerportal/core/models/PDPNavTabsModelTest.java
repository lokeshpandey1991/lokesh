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

public class PDPNavTabsModelTest {
    
    final static String pdpPath = "/content/roche/customerportal/us/en/home/pdp/jcr:content/pdpnavtabs";
    final static String pdpPath_one = "/content/roche/customerportal/us/en/home/pdp/jcr:content/pdpnavtabs1";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testDefaultTabVisiblility() throws LoginException {
        PDPNavTabsModel modelObject = context.resourceResolver().getResource(pdpPath).adaptTo(PDPNavTabsModel.class);
        Assert.assertEquals("tab1", String.valueOf(modelObject.getDefaultTab()));
    }
    
    @Test
    public void testDefaultTabVisiblility1() throws LoginException {
        PDPNavTabsModel modelObject = context.resourceResolver().getResource(pdpPath_one)
                .adaptTo(PDPNavTabsModel.class);
        Assert.assertEquals("tab4", String.valueOf(modelObject.getDefaultTab()));
    }
    
    @Test
    public void testTabsVisiblility() throws LoginException {
        PDPNavTabsModel modelObject = context.resourceResolver().getResource(pdpPath).adaptTo(PDPNavTabsModel.class);
        Assert.assertEquals("true", String.valueOf(modelObject.getShowTab1()));
        Assert.assertEquals("false", String.valueOf(modelObject.getShowTab2()));
        Assert.assertEquals("true", String.valueOf(modelObject.getShowTab3()));
        Assert.assertEquals("false", String.valueOf(modelObject.getShowTab4()));
    }
    
    @Test
    public void testTabsVisiblility1() throws LoginException {
        PDPNavTabsModel modelObject = context.resourceResolver().getResource(pdpPath_one)
                .adaptTo(PDPNavTabsModel.class);
        Assert.assertEquals("false", String.valueOf(modelObject.getShowTab1()));
        Assert.assertEquals("false", String.valueOf(modelObject.getShowTab2()));
        Assert.assertEquals("false", String.valueOf(modelObject.getShowTab3()));
        Assert.assertEquals("true", String.valueOf(modelObject.getShowTab4()));
    }
    
    @Test
    public void testTabsTitle() throws LoginException {
        PDPNavTabsModel modelObject = context.resourceResolver().getResource(pdpPath).adaptTo(PDPNavTabsModel.class);
        Assert.assertEquals("rdoe_pdpnavtabs.productInformation", String.valueOf(modelObject.getTitleTab1()));
        Assert.assertEquals("rdoe_pdpnavtabs.productSpecs", String.valueOf(modelObject.getTitleTab2()));
        Assert.assertEquals("rdoe_pdpnavtabs.documentation", String.valueOf(modelObject.getTitleTab3()));
        Assert.assertEquals("rdoe_pdpnavtabs.relatedProducts", String.valueOf(modelObject.getTitleTab4()));
    }
    
    @Test
    public void testElabDocEnabled() throws LoginException {
        PDPNavTabsModel modelObject = context.resourceResolver().getResource(pdpPath).adaptTo(PDPNavTabsModel.class);
        Assert.assertEquals(false, modelObject.isElabDocEnabledForAssays());
        Assert.assertEquals(false, modelObject.isElabDocEnabled());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForClasses(PDPNavTabsModel.class);
            MockRocheContent.loadfile(context, "/json/roche/roche.json", "/content/roche/customerportal");
            MockRocheContent.loadfile(context, "/json/roche/us/us.json", "/content/roche/customerportal/us");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/pdp.json",
                    "/content/roche/customerportal/us/en/home/pdp");
        }
    };
}
