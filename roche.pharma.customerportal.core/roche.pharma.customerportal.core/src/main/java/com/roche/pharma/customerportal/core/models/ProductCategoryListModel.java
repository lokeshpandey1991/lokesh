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
import com.roche.pharma.customerportal.core.beans.ProductCategoryListBean;
import com.roche.pharma.customerportal.core.beans.TagBean;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This Product category filter model class is called from productCategoryList component it returns the list of
 * ProductCategoryListBean which contains the categories title, description and URL
 * @version 1.0
 * @author Ritika Sharma
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = ProductCategoryListModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class ProductCategoryListModel {
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/productCategoryList";

    
    private List<ProductCategoryListBean> categoryListing = new ArrayList<>();
    
    private String componentName;
    
    @Self
    private Resource resource;
    
    @ValueMapValue
    private String[] listing;
    
    @ValueMapValue
    private String filterTag;
    
    @PostConstruct
    protected void postConstruct() {
        final Page currentPage = CommonUtils.getCurrentPage(resource);
        final Locale pageLocale = CommonUtils.getPageLocale(currentPage);
        TagBean tagBean;
        tagBean = CommonUtils.getTagBean(filterTag, pageLocale, resource);
        filterTag = tagBean.getTagName();
        categoryListing = getMultifieldValue(listing, categoryListing);
        componentName = CommonUtils.getComponentName(resource);
    }
    
    private List<ProductCategoryListBean> getMultifieldValue(final String[] jsonArray,
            final List<ProductCategoryListBean> beanList) {
        if (jsonArray != null) {
            for (final String json : jsonArray) {
                final ProductCategoryListBean listingBean = CommonUtils.getMultifield(json,
                        ProductCategoryListBean.class);
                setPageContentType(listingBean);
                beanList.add(listingBean);
            }
        }
        return beanList;
    }
    
    /**
     * This method set page content type if page is internal page
     * @param pageLinkBean
     */
    private void setPageContentType(final ProductCategoryListBean bean) {
        String link = bean.getCategoryURL();
        if (CommonUtils.isInternalLink(link) && !CommonUtils.isAssetPath(link)) {
            link = CommonUtils.getPagepathWithExtension(link);
            bean.setCategoryURL(link);
            bean.setContentType(CommonUtils.getPageTypeProperty(link, resource.getResourceResolver()));
        }
    }
    
    public String getComponentName() {
        return componentName;
    }
    
    public String getFilterTag() {
        return filterTag;
    }
    
    public String[] getListing() {
        return listing == null ? null : listing.clone();
    }
    
    public List<ProductCategoryListBean> getCategoryListing() {
        return categoryListing;
    }
    
}
