package com.roche.pharma.customerportal.core.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockHelper;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class SearchServiceImplTest {
    final static String DEFAULT_PATH = "/content/roche/customerportal/us/en/home-page";
    final static String rocheBasePath = "/content/roche";
    final static SearchServiceImpl searchServiceImpl = new SearchServiceImpl();

    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();

    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            // Mock Query Builder
            final List<String> pdpPaths = new ArrayList<String>();
            pdpPaths.add(DEFAULT_PATH + "/jcr:content");
            MockHelper.loadQuery(context, pdpPaths);
            context.registerInjectActivateService(searchServiceImpl);
            context.load().json("/json/roche-testContent.json", rocheBasePath);
        }
    };

    @Test
    public void testsearchResult() throws ServletException, IOException {
        final Map<String, String> paramMap = new HashMap<>();
        final Resource resource = context.resourceResolver().getResource(DEFAULT_PATH);
        Assert.assertEquals(1,
                searchServiceImpl.getSearchResults(resource.getResourceResolver(), paramMap).getHits().size());
    }
    
}
