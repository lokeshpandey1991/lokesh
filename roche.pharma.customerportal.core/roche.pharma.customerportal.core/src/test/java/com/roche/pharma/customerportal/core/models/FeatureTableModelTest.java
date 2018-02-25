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

public class FeatureTableModelTest {
    
    final static String pdpPath = "/content/roche/customerportal/us/en/home/pdp/jcr:content/productSpecsPar/featuretable";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testTableHeading() throws LoginException {
        FeatureTableModel modelObject = context.resourceResolver().getResource(pdpPath)
                .adaptTo(FeatureTableModel.class);
        Assert.assertEquals("Anticyricin", modelObject.getTableHeading());
    }
    
    @Test
    public void testTableDescription() throws LoginException {
        FeatureTableModel modelObject = context.resourceResolver().getResource(pdpPath)
                .adaptTo(FeatureTableModel.class);
        Assert.assertEquals("Sample Table description", modelObject.getTableDescription());
    }
    
    @Test
    public void testTableDisclaimer() throws LoginException {
        FeatureTableModel modelObject = context.resourceResolver().getResource(pdpPath)
                .adaptTo(FeatureTableModel.class);
        Assert.assertEquals("* General test results", modelObject.getTableDisclaimer());
    }
    
    @Test
    public void testTable() throws LoginException {
        FeatureTableModel modelObject = context.resourceResolver().getResource(pdpPath)
                .adaptTo(FeatureTableModel.class);
        Assert.assertEquals(
                "<table><tbody><tr><th scope='col'> Result</th><th scope='col'> cobas HPV Test</th><th scope='col'> Test</th><th scope='col'> Test</th></tr><tr><th scope='row'>Result</th><td>test 1</td><td>test 2</td><td>test 3</td></tr><tr><th scope='row'>Result</th><td>test 1</td><td>test 2</td><td>test 3</td></tr><tr><th scope='row'>Result</th><td>test 1</td><td>test 2</td><td>test 3</td></tr></tbody></table>",
                modelObject.getTable());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForClasses(FeatureTableModel.class);
            MockRocheContent.loadfile(context, "/json/roche/roche.json", "/content/roche/customerportal");
            MockRocheContent.loadfile(context, "/json/roche/us/us.json", "/content/roche/customerportal/us");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/pdp.json",
                    "/content/roche/customerportal/us/en/home/pdp");
        }
    };
    
}
