package com.roche.pharma.customerportal.core.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockHelper;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;
import com.roche.pharma.customerportal.core.services.impl.ConfigurationServiceImpl;
import com.roche.pharma.customerportal.core.services.impl.SearchServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class RelatedProductsModelTest {
    
    final static String productNodePath = "/content/customerportal/us/en/home/product-category-page";
    final static SearchServiceImpl searchServiceImpl = new SearchServiceImpl();
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testProductDetailNameModel() throws LoginException {
        RelatedProductsModel modelObject = context.resourceResolver()
                .getResource(productNodePath + "/sss/jcr:content/relatedproducts").adaptTo(RelatedProductsModel.class);
        Assert.assertEquals("This is section heading and authored value is expected to Related Products",
                "Related Products", modelObject.getSectionHeading());
        Assert.assertEquals("There should have been 2 beans in this list", 2, modelObject.getPdpBeanList().size());
        Assert.assertEquals("This is the name of component.", "relatedproducts", modelObject.getComponentName());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/productPage.json", productNodePath);
            context.load().json("/json/roche/businesstags.json", "/etc/tags/customerportal");
            
            ConfigurationServiceImpl configurationServiceImpl = new ConfigurationServiceImpl();
            Map<String, Object> _config;
            _config = new HashMap<String, Object>();
            _config.put("service.rootPath", "/content/customerportal");
            context.registerInjectActivateService(configurationServiceImpl, _config);
            
            // Mock Query Builder
            List<String> pdpPaths = new ArrayList<String>();
            pdpPaths.add(productNodePath + "/cobas");
            MockHelper.loadQuery(context, pdpPaths);
            context.registerInjectActivateService(searchServiceImpl);
            
        }
    };
}
