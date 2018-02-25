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
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.day.cq.replication.Replicator;
import com.roche.pharma.customerportal.core.mock.MockHelper;
import com.roche.pharma.customerportal.core.mock.MockReplicatorImpl;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;
import com.roche.pharma.customerportal.core.mock.MockScheduler;
import com.roche.pharma.customerportal.core.schedulers.SearchIndexScheduler;
import com.roche.pharma.customerportal.core.services.impl.ConfigurationServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class IndexServletTest {
    private static IndexServlet servlet;
    private static SearchIndexScheduler searchIndexScheduler;
    final static String productNodePath = "/content/roche/customerportal/us/en/home/roche54353";
    final static String INDEXNODEPATH = "/content/roche/customerportal/us/en";
    final static String OUTPUT = "<!DOCTYPE html><HTML>\n<HEAD></HEAD>\n<BODY>\n<a href=\"http:/content/roche/customerportal/us/en/home/roche54353.html\">http:/content/roche/customerportal/us/en/home/roche54353.html</a><br /> \n<a href=\"http:/content/roche/customerportal/us/en/home/product-category-page.html\">http:/content/roche/customerportal/us/en/home/product-category-page.html</a><br /> \n\n</BODY></HTML><!DOCTYPE html><HTML>\n<HEAD></HEAD>\n<BODY>\n<a href=\"http:/content/roche/customerportal/us/en/home/roche54353.html\">http:/content/roche/customerportal/us/en/home/roche54353.html</a><br /> \n<a href=\"http:/content/roche/customerportal/us/en/home/product-category-page.html\">http:/content/roche/customerportal/us/en/home/product-category-page.html</a><br /> \n\n</BODY></HTML>";
    final static String OUTPUT1 = "<!DOCTYPE html><HTML>\n<HEAD></HEAD>\n<BODY>\n<a href=\"http:/content/roche/customerportal/us/en/landing-home/product-category/product-detail.html\">http:/content/roche/customerportal/us/en/landing-home/product-category/product-detail.html</a><br /> \n\n</BODY></HTML>";
    final static String OUTPUT2 = "<!DOCTYPE html><HTML>\n<HEAD></HEAD>\n<BODY>\n<a href=\"http:/content/roche/customerportal/us/en/home/roche54353/_jcr_content.html\">http:/content/roche/customerportal/us/en/home/roche54353/_jcr_content.html</a><br /> \n\n</BODY></HTML>";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testIndexServletForSelector1() throws ServletException, IOException {
        context.requestPathInfo().setSelectorString("crawl_inr");
        servlet.doGet(context.request(), context.response());
        context.request().setResource(context.currentResource(INDEXNODEPATH + "/home"));
        servlet.doGet(context.request(), context.response());
        Assert.assertEquals(OUTPUT, context.response().getOutputAsString());
    }
    
    @Test
    public void testIndexServletForSelector2() throws ServletException, IOException {
        context.requestPathInfo().setSelectorString("crawl_del");
        servlet.doGet(context.request(), context.response());
        Assert.assertEquals(OUTPUT1, context.response().getOutputAsString());
        
    }
    
    @Test
    public void testIndexServletForSelector3() throws ServletException, IOException {
        context.requestPathInfo().setSelectorString("crawl");
        servlet.doGet(context.request(), context.response());
        Assert.assertEquals(OUTPUT2, context.response().getOutputAsString());
        
    }
    
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            
            context.request().setMethod(HttpConstants.METHOD_GET);
            context.requestPathInfo().setExtension("html");
            
            // Mock Replicator
            Replicator replicator = new MockReplicatorImpl();
            context.registerService(Replicator.class, replicator);
            
            // Mock Query Builder
            List<String> pdpPaths = new ArrayList<String>();
            pdpPaths.add(productNodePath + "/jcr:content");
            MockHelper.loadQuery(context, pdpPaths);
            
            ConfigurationServiceImpl configurationServiceImpl = new ConfigurationServiceImpl();
            Map<String, Object> _config1;
            _config1 = new HashMap<String, Object>();
            _config1.put("service.rootPath", "/content/roche/customerportal");
            context.registerInjectActivateService(configurationServiceImpl, _config1);
            
            Map<String, Object> _config;
            _config = new HashMap<String, Object>();
            _config.put("executionMode", "immediate");
            _config.put("singleIndexNode", true);
            searchIndexScheduler = new SearchIndexScheduler();
            Runnable task = searchIndexScheduler.new SearchIndexTask();
            Scheduler scheduler = new MockScheduler(task);
            context.registerService(Scheduler.class, scheduler);
            context.registerInjectActivateService(searchIndexScheduler, _config);
            servlet = MockHelper.getServlet(context, IndexServlet.class);
            
            MockRocheContent.loadfile(context, "/json/roche/us/pages/en.json", INDEXNODEPATH);
            context.request().setResource(context.currentResource(INDEXNODEPATH));
            
        }
    };
    
}
