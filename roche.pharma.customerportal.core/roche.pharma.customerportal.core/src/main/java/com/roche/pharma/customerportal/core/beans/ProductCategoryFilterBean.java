package com.roche.pharma.customerportal.core.beans;

/**
 * This is bean class for product category page filter configurations
 * @author Ritika Sharma
 * @version 1.0
 */
public class ProductCategoryFilterBean {
    
    private String filterTitle;
    
    private String filterTag;
    
    /**
     * @return
     */
    public String getFilterTitle() {
        return filterTitle;
    }
    
    /**
     * @param filterTitle
     */
    public void setFilterTitle(final String filterTitle) {
        this.filterTitle = filterTitle;
    }
    
    /**
     * @return
     */
    public String getFilterTag() {
        return filterTag;
    }
    
    /**
     * @param filterTag
     */
    public void setFilterTag(final String filterTag) {
        this.filterTag = filterTag;
    }
    
}
