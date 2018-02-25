package com.roche.pharma.customerportal.core.models;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is model class for productDetailName Component.
 * @author mhuss3
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = ProductDetailNameModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class ProductDetailNameModel {
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/productDetailName";

    
    @ValueMapValue
    private String productName;
    
    @ValueMapValue
    private String valueProposition;
    
    @ValueMapValue
    private String fileReference;
    
    @ValueMapValue
    private String altText;
    
    @Self
    private Resource resource;
    
    @PostConstruct
    protected void postConstruct() {
        if (StringUtils.isBlank(productName)) {
            final Page currentPage = CommonUtils.getCurrentPage(resource);
            if (currentPage != null) {
                productName = currentPage.getTitle();
            }
        }
    }
    
    public String getProductName() {
        return productName;
    }
    
    public String getValueProposition() {
        return valueProposition;
    }
    
    public String getFileReference() {
        return fileReference;
    }
    
    public String getAltText() {
        return altText;
    }
    
}
