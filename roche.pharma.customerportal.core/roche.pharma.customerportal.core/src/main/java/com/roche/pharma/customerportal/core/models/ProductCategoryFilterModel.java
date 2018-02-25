package com.roche.pharma.customerportal.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.beans.ProductCategoryFilterBean;
import com.roche.pharma.customerportal.core.beans.TagBean;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This Product category filter model class is called from productCategoryFilter component it returns the list of
 * ProductCategoryFilterBean which contains the filter name and tag
 * @version 1.0
 * @author Ritika Sharma
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = ProductCategoryFilterModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class ProductCategoryFilterModel {
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/productCategoryFilter";

    
    @Self
    private Resource resource;
    
    @ValueMapValue
    private String[] filters;
    
    private List<ProductCategoryFilterBean> categoryFilter = new ArrayList<>();
    
    @PostConstruct
    protected void postConstruct() {
        final Page currentPage = CommonUtils.getCurrentPage(resource);
        final Locale pageLocale = CommonUtils.getPageLocale(currentPage);
        categoryFilter = getMultifieldValue(filters, categoryFilter, pageLocale);
    }
    
    private List<ProductCategoryFilterBean> getMultifieldValue(final String[] jsonArray,
            final List<ProductCategoryFilterBean> filterList, final Locale pageLocale) {
        if (jsonArray != null) {
            for (final String json : jsonArray) {
                TagBean tagBean;
                final ProductCategoryFilterBean filterBean = CommonUtils.getMultifield(json,
                        ProductCategoryFilterBean.class);
                tagBean = CommonUtils.getTagBean(filterBean.getFilterTag(), pageLocale, resource);
                filterBean.setFilterTag(tagBean.getTagName());
                filterList.add(filterBean);
            }
        }
        return filterList;
    }
    
    public List<ProductCategoryFilterBean> getCategoryFilter() {
        return categoryFilter;
    }
    
    public String[] getFilters() {
        return null == filters ? null : filters.clone();
    }
    
}
