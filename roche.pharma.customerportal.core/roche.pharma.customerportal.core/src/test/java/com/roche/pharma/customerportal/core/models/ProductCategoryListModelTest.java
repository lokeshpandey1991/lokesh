package com.roche.pharma.customerportal.core.models;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.beans.ProductCategoryListBean;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class ProductCategoryListModelTest {

    final static String PRODUCT_CATEGORY_LIST = "/content/roche/customerportal/us/en/home-page/product-category/jcr:content/body-parsys/productcategorylist";
    final static String[] categoryData = {
            "{\"categoryTitle\":\"Category Name1\",\"categoryDescription\":\"Category Description1\",\"categoryURL\":\"/content/roche/customerportal/us/en/home-page\"}",
            "{\"categoryTitle\":\"Category Name2\",\"categoryDescription\":\"category Description 2\",\"categoryURL\":\"/content/roche/us/en/home\"}",
            "{\"categoryTitle\":\"category name 3\",\"categoryDescription\":\"Category Description 3\",\"categoryURL\":\"/content/roche/us/en/home\"}"
    };
    final static String PRODUCT_CATEGORY_TAG = "productSolution";
    final static ProductCategoryListBean bean = new ProductCategoryListBean();

    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();

    @Test
    public void testjcrDataListing() throws LoginException {
        final ProductCategoryListModel modelObject = context.resourceResolver().getResource(PRODUCT_CATEGORY_LIST)
                .adaptTo(ProductCategoryListModel.class);
        Assert.assertArrayEquals(categoryData, modelObject.getListing());
        Assert.assertEquals(PRODUCT_CATEGORY_TAG, modelObject.getFilterTag());
    }

    @Test
    public void testCategoryTitle() throws LoginException {
        final ProductCategoryListModel modelObject = context.resourceResolver().getResource(PRODUCT_CATEGORY_LIST)
                .adaptTo(ProductCategoryListModel.class);
        bean.setCategoryDescription(modelObject.getCategoryListing().get(0).getCategoryDescription());
        bean.setCategoryTitle(modelObject.getCategoryListing().get(0).getCategoryTitle());
        bean.setCategoryURL(modelObject.getCategoryListing().get(0).getCategoryURL());

        Assert.assertEquals("Category Name1", modelObject.getCategoryListing().get(0).getCategoryTitle());
    }

    @Test
    public void testCategoryDescription() throws LoginException {
        final ProductCategoryListModel modelObject = context.resourceResolver().getResource(PRODUCT_CATEGORY_LIST)
                .adaptTo(ProductCategoryListModel.class);
        Assert.assertEquals("Category Description1", modelObject.getCategoryListing().get(0).getCategoryDescription());
        // Assert.assertEquals("home", modelObject.getCategoryListing().get(0).getContentType());
        Assert.assertEquals("productCategoryList", modelObject.getComponentName());
    }
    
    @Test
    public void testCategoryURL() throws LoginException {
        final ProductCategoryListModel modelObject = context.resourceResolver().getResource(PRODUCT_CATEGORY_LIST)
                .adaptTo(ProductCategoryListModel.class);
        Assert.assertEquals("/content/roche/customerportal/us/en/home-page.html",
                modelObject.getCategoryListing().get(0).getCategoryURL());
    }

    /*
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.loadProductCategory(context);
        }
    };

}
