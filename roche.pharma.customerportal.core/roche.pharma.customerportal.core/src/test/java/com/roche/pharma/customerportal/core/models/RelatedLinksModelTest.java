package com.roche.pharma.customerportal.core.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockHelper;
import com.roche.pharma.customerportal.core.services.impl.SearchServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class RelatedLinksModelTest {
    final static String DEFAULT_PATH = "/content/customerportal/us/en/home-page/jcr:content/par/relatedlinks";
    final static String PAGE_PATH = "/content/customerportal/us/en/home-page/jcr:content/par/relatedlinks1";
    final static String DYNAMIC_PATH = "/content/customerportal/us/en/home-page/jcr:content/par/relatedlinks2";
    final static String DYNAMIC_PATH_LIMIT = "/content/customerportal/us/en/home-page/jcr:content/par/relatedlinks3";
    final static String DEFAULT_PATH_PAGE_TYPE = "/content/customerportal/us/en/product-page/jcr:content/parCatalog/relatedlinks";
    final static String STATIC_TYPE = "/content/customerportal/us/en/home-page/jcr:content/par/relatedlinks4";
    final static String rocheBasePath = "/content";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();

    @Test
    public void testRelatedLinksStaticContent() {
        final RelatedLinksModel modelObject = context.resourceResolver().getResource(DEFAULT_PATH)
                .adaptTo(RelatedLinksModel.class);
        Assert.assertEquals(1, modelObject.getRelatedList().size());
        Assert.assertEquals("Test", modelObject.getHeadline());
    }
    
    @Test
    public void testRelatedLinksStaticContentPageProperties() {
        final RelatedLinksModel modelObject = context.resourceResolver().getResource(PAGE_PATH)
                .adaptTo(RelatedLinksModel.class);
        Assert.assertEquals(1, modelObject.getRelatedList().size());
    }

    @Test
    public void testRelatedLinksDynamicContentPageProperties() {
        final RelatedLinksModel modelObject = context.resourceResolver().getResource(DYNAMIC_PATH)
                .adaptTo(RelatedLinksModel.class);
        Assert.assertEquals(0, modelObject.getRelatedList().size());
    }

    @Test
    public void testRelatedLinksDynamicContentLimitPageProperties() {
        final RelatedLinksModel modelObject = context.resourceResolver().getResource(DYNAMIC_PATH_LIMIT)
                .adaptTo(RelatedLinksModel.class);
        Assert.assertEquals(0, modelObject.getRelatedList().size());
    }
    
    @Test
    public void testRelatedLinksStaticContentEmptyList() {
        final RelatedLinksModel modelObject = context.resourceResolver().getResource(STATIC_TYPE)
                .adaptTo(RelatedLinksModel.class);
        Assert.assertEquals(0, modelObject.getRelatedList().size());
        Assert.assertEquals("Test", modelObject.getHeadline());
    }

    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            context.load().json("/json/roche-testContent.json", rocheBasePath);
            // Mock Query Builder
            final List<String> pdpPaths = new ArrayList<String>();
            pdpPaths.add("/content/customerportal/us/en/home-page");
            pdpPaths.add("/content/customerportal/us/en/products-page");
            MockHelper.loadQuery(context, pdpPaths);
            final SearchServiceImpl searchServiceImpl = new SearchServiceImpl();
            context.registerInjectActivateService(searchServiceImpl);
        }
    };

}
