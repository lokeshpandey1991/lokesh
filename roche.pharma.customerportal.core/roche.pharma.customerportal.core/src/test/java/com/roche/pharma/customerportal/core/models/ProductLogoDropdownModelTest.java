package com.roche.pharma.customerportal.core.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockRocheContent;
import com.roche.pharma.customerportal.core.services.impl.ConfigurationServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

/**
 * The Class ProductLogoDropdownModelTest.
 */
public class ProductLogoDropdownModelTest {
    
    /** The context. */
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    /** The Configuration service impl. */
    private final ConfigurationServiceImpl ConfigurationServiceImpl = new ConfigurationServiceImpl();
    
    /** The config. */
    Map<String, Object> _config;
    
    /** The Constant PRODUCT_LOGO_PATH. */
    private static final String PRODUCT_LOGO_PATH = "/content/customerportal/author-product-logos";

    /**
     * Sets the up.
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        _config = new HashMap<String, Object>();
        _config.put("productlogo.service.pagePath", PRODUCT_LOGO_PATH);
        context.registerInjectActivateService(ConfigurationServiceImpl, _config);
    }
    
    /**
     * Test get product logo dropdown.
     */
    @Test
    public void testGetProductLogoDropdown() {
        
        Resource res = context.resourceResolver().getResource(PRODUCT_LOGO_PATH);
        context.request().setResource(res);
        ProductLogoDropdownModel productLogoModel = context.request().adaptTo(ProductLogoDropdownModel.class);
        
    }

    /** The Constant SETUP_CALLBACK. */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.loadfile(context, "/json/roche/author_product_logo.json", PRODUCT_LOGO_PATH);

        }
    };

}
