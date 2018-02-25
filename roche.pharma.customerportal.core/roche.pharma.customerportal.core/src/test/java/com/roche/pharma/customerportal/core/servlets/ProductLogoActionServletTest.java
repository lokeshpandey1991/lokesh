package com.roche.pharma.customerportal.core.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockHelper;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class ProductLogoActionServletTest {
    
    final static String PRODUCT_NODE_PATH = "/content/customerportal/us/en/home/roche54353";
    final static String PRODUCT_LOGO_PATH = "/content/customerportal";
    final static String INDEXNODEPATH = "/content/roche/customerportal/us/en";
    
    private static ProductLogoActionServlet productLogoServlet;
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();

    @Test
    public void testUpdateProductLogo() throws ServletException, IOException {
        
        Map<String, Object> params = new HashMap<String, Object>();
        
        params.put("logoName", "Logo1");
        params.put("logoDescription", "Logo1 Description");
        params.put("logoReference", "photo");
        params.put("logoStatus", "true");
        params.put("action", "update");
        
        context.request().setParameterMap(params);
        
        productLogoServlet.doGet(context.request(), context.response());
        Assert.assertEquals(200, context.response().getStatus());
    }

    @Test
    public void testGetProductLogoPages() throws ServletException, IOException {
        
        Map<String, Object> params = new HashMap<String, Object>();
        
        params.put("logoName", "Logo1");
        params.put("action", "page");
        
        context.request().setParameterMap(params);
        
        productLogoServlet.doGet(context.request(), context.response());
        Assert.assertEquals(200, context.response().getStatus());
    }

    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            
            MockRocheContent.loadfile(context, "/json/roche/us/pages/pdp.json", PRODUCT_NODE_PATH);
            context.request().setResource(context.currentResource(PRODUCT_NODE_PATH));
            setSlingRequest(context);
            // Mock Query Builder
            List<String> pdpPaths = new ArrayList<String>();
            pdpPaths.add(PRODUCT_NODE_PATH + "/jcr:content");
            MockHelper.loadQuery(context, pdpPaths);

            productLogoServlet = MockHelper.getServlet(context, ProductLogoActionServlet.class);

            /** The config. */
            Map<String, Object> _config;
            _config = new HashMap<String, Object>();
            _config.put("customerportal.productLogo.query.path", PRODUCT_LOGO_PATH);
            context.registerInjectActivateService(productLogoServlet, _config);
            
        }
    };

    private static void setSlingRequest(AemContext context) {
        
        context.request().setServletPath("/bin/Customerportal/productlogoupdateservlet");
        context.request().setMethod(HttpConstants.METHOD_GET);

        context.requestPathInfo().setExtension("html");

    }
    
}
