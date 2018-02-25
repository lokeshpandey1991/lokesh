package com.roche.pharma.customerportal.core.servlets;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.day.cq.replication.Replicator;
import com.roche.pharma.customerportal.core.mock.MockHelper;
import com.roche.pharma.customerportal.core.mock.MockReplicatorImpl;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;
import com.roche.pharma.customerportal.core.services.impl.ConfigurationServiceImpl;

public class RocheCacheCleanUpServletTest {
    
    private static RocheCacheCleanUpServlet servlet;
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    final static String OUTPUT1 = "[\"{{{EhCache Clean Request Started}}} for relatedAssay\"]";
    
    final static String OUTPUT2 = "[\"{{{Dispatcher Cache Clean Request Started}}} for assayMenuDispCache\"]";
    
    @Test
    public void testRelatedAssayCacheClean() throws ServletException, IOException {
        context.requestPathInfo().setSelectorString("relatedAssay");
        servlet.doGet(context.request(), context.response());
        Assert.assertEquals(OUTPUT1, context.response().getOutputAsString());
    }
    
    @Test
    public void testAssayMenuDispCache() throws ServletException, IOException {
        context.requestPathInfo().setSelectorString("assayMenuDispCache");
        servlet.doGet(context.request(), context.response());
        Assert.assertEquals(OUTPUT2, context.response().getOutputAsString());
    }
    
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context) throws PersistenceException, IOException,
                javax.jcr.LoginException, RepositoryException {
            context.requestPathInfo().setExtension("html");
            context.requestPathInfo().setResourcePath("roche/customerportal/components/ehCacheClean");
            final ConfigurationServiceImpl configurationServiceImpl = new ConfigurationServiceImpl();
            Map<String, Object> _config1;
            _config1 = new HashMap<String, Object>();
            _config1.put("assay.menu.pages",
                    "[/content/customerportal/global/en/instrument,/content/customerportal/us/en/instrument]");
            _config1.put("dispatcher.flush.url", "http://localhost/dispatcher/invalidate.cache");
            _config1.put("activate.pdp.on.dtl.event", "true");
            context.registerInjectActivateService(configurationServiceImpl, _config1);
            
            // Mock Replicator
            final Replicator replicator = new MockReplicatorImpl();
            context.registerService(Replicator.class, replicator);
            servlet = MockHelper.getServlet(context, RocheCacheCleanUpServlet.class);
            
            MockRocheContent.loadfile(context, "/json/cacheNode.json", "/content/customerportal/cacheClean");
        }
    };
}
