package com.roche.pharma.customerportal.core.beans;


/**
 * This is bean class for product category page listing component configurations.
 *
 * @author Ritika Sharma
 * @version 1.0
 */
public class ProductCategoryListBean {
    
    /** The category title. */
    private String categoryTitle;
    
    /** The category description. */
    private String categoryDescription;
    
    /** The category URL. */
    private String categoryURL;
    
    /** The content type. */
    private String contentType;
    
    /**
     * Gets the category title.
     *
     * @return the category title
     */
    public String getCategoryTitle() {
        return categoryTitle;
    }
    
    /**
     * Sets the category title.
     *
     * @param categoryTitle the new category title
     */
    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
    
    /**
     * Gets the category description.
     *
     * @return the category description
     */
    public String getCategoryDescription() {
        return categoryDescription;
    }
    
    /**
     * Sets the category description.
     *
     * @param categoryDescription the new category description
     */
    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
    
    /**
     * Gets the category URL.
     *
     * @return the category URL
     */
    public String getCategoryURL() {
        return categoryURL;
    }
    
    /**
     * Sets the category URL.
     *
     * @param categoryURL the new category URL
     */
    public void setCategoryURL(String categoryURL) {
        this.categoryURL = categoryURL;
    }

    /**
     * Gets the content type.
     *
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the content type.
     *
     * @param contentType the new content type
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
}
