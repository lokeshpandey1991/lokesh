package com.roche.pharma.customerportal.core.models;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockRocheContent;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class SearchSortModelTest {
    
    final static String rocheBasePath = "/content/roche";
    final static String searchSortPath = "/content/roche/us/en/home-page/jcr:content/searchsort";
    final static String listingSortPath = "/content/roche/us/en/home-page/products-page/plp/jcr:content/searchsort";
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testSearchSortList() {
        final SearchSortModel modelObject = context.resourceResolver().getResource(searchSortPath)
                .adaptTo(SearchSortModel.class);
        Assert.assertEquals("rdoe_searchsort.titleDsc", modelObject.getSortList().get(0).getSearchLabel());
        Assert.assertEquals("all", modelObject.getSortList().get(0).getSearchType());
        Assert.assertEquals("titleDsc", modelObject.getSortList().get(0).getSearchValue());
        Assert.assertEquals(2, modelObject.getDefaultOption().size());
    }
    
    @Test
    public void testListingSortList() {
        final SearchSortModel modelObject = context.resourceResolver().getResource(listingSortPath)
                .adaptTo(SearchSortModel.class);
        Assert.assertEquals("rdoe_searchsort.relevance", modelObject.getSortList().get(0).getSearchLabel());
        Assert.assertEquals("relevance", modelObject.getSortList().get(0).getSearchValue());
        Assert.assertEquals("title", modelObject.getDefaultSort());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.load(context);
        }
    };
    
}
