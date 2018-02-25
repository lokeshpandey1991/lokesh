package com.roche.pharma.customerportal.core.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.beans.ProductCategoryFilterBean;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class ProductCategoryFilterModelTest {
    
    final static String PRODUCT_CATEGORY_FILTER = "/content/roche/customerportal/us/en/home-page/product-category/jcr:content/body-parsys/productcategoryfilte";
    final static String[] filterData = {
            "{\"filterTitle\":\"products\",\"filterTag\":\"/etc/tags/roche/customerportal/pim/product/productSolution\"}",
            "{\"filterTitle\":\"health topics\",\"filterTag\":\"/etc/tags/roche/customerportal/pim/product/healthTopics\"}"
    };
    final static ProductCategoryFilterBean bean = new ProductCategoryFilterBean();
    private List<ProductCategoryFilterBean> categoryFilterData = new ArrayList<>();
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void testFilterData() throws LoginException {
        ProductCategoryFilterModel modelObject = context.resourceResolver().getResource(PRODUCT_CATEGORY_FILTER)
                .adaptTo(ProductCategoryFilterModel.class);
        Assert.assertArrayEquals(filterData, modelObject.getFilters());
    }
    
    @Test
    public void testFilterTag() throws LoginException {
        ProductCategoryFilterModel modelObject = context.resourceResolver().getResource(PRODUCT_CATEGORY_FILTER)
                .adaptTo(ProductCategoryFilterModel.class);
        bean.setFilterTag(modelObject.getCategoryFilter().get(0).getFilterTag());
        bean.setFilterTitle(modelObject.getCategoryFilter().get(0).getFilterTitle());
        categoryFilterData.add(bean);
        Assert.assertEquals("productSolution", modelObject.getCategoryFilter().get(0).getFilterTag());
    }
    
    @Test
    public void testFilterTitle() throws LoginException {
        ProductCategoryFilterModel modelObject = context.resourceResolver().getResource(PRODUCT_CATEGORY_FILTER)
                .adaptTo(ProductCategoryFilterModel.class);
        Assert.assertEquals("products", modelObject.getCategoryFilter().get(0).getFilterTitle());
    }
    
    /*
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.loadfile(context, "/json/roche/tags.json", "/etc/tags/roche");
            MockRocheContent.loadfile(context, "/json/productCategory.json", "/content/roche");
        }
    };
    
}
