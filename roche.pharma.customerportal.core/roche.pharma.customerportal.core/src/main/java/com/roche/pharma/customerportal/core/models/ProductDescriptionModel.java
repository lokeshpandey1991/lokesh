/**
 * This is Sling Model class for productDescription Component.
 * @author nitin kumar
 * @version 1.0
 */
package com.roche.pharma.customerportal.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.roche.pharma.customerportal.core.constants.RocheConstants;

/**
 * This is productDescriptionModel Class. This class contains methods to return Authored values.
 * @author nitin kumar
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = ProductDescriptionModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class ProductDescriptionModel {
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/productDescription";

    
    /**
     * product Description Heading property
     */
    @ValueMapValue
    private String productDescrHeading;
    
    /**
     * product Description property
     */
    @ValueMapValue
    private String productDescription;
    
    /**
     * column view property
     */
    @ValueMapValue
    private String columnView;
    
    /**
     * This method returns the headline entered by Author.
     * @return productDescrHeading
     */
    public String getProductDescriptionHeading() {
        return productDescrHeading;
    }
    
    /**
     * This method return the description entered by Author.
     * @return productDescription
     */
    public String getProductDescription() {
        return productDescription;
    }
    
    /**
     * This method returns the view flag as selected by Author.
     * @return columnView
     */
    public Boolean getIsColumnView() {
        return RocheConstants.TRUE_VALUE.equalsIgnoreCase(columnView);
    }
    
}
