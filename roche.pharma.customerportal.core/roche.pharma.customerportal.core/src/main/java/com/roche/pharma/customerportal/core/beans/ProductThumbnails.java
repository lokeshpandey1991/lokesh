package com.roche.pharma.customerportal.core.beans;

import java.util.List;

/**
 * The Class ProductThumbnails.
 */
public class ProductThumbnails {
    
    /** The product path. */
    private String productPath;
    
    /** The product title. */
    private String productTitle;
    
    /** The alt text. */
    private String altText;
    
    /** The file reference. */
    private String fileReference;
    
    /** The tags id. */
    private List<String> tagsId;
    
    /**
     * Gets the product path.
     *
     * @return the productPath
     */
    public String getProductPath() {
        return productPath;
    }
    
    /**
     * Sets the product path.
     *
     * @param productPath the productPath to set
     */
    public void setProductPath(String productPath) {
        this.productPath = productPath;
    }
    
    /**
     * Gets the product title.
     *
     * @return the productTitle
     */
    public String getProductTitle() {
        return productTitle;
    }
    
    /**
     * Sets the product title.
     *
     * @param productTitle the productTitle to set
     */
    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
    
    /**
     * Gets the alt text.
     *
     * @return the altText
     */
    public String getAltText() {
        return altText;
    }
    
    /**
     * Sets the alt text.
     *
     * @param altText the altText to set
     */
    public void setAltText(String altText) {
        this.altText = altText;
    }
    
    /**
     * Gets the file reference.
     *
     * @return the fileReference
     */
    public String getFileReference() {
        return fileReference;
    }
    
    /**
     * Sets the file reference.
     *
     * @param fileReference the fileReference to set
     */
    public void setFileReference(String fileReference) {
        this.fileReference = fileReference;
    }
    
    /**
     * Gets the tags id.
     *
     * @return the tagsId
     */
    public List<String> getTagsId() {
        return tagsId;
    }
    
    /**
     * Sets the tags id.
     *
     * @param tagsId the tagsId to set
     */
    public void setTagsId(List<String> tagsId) {
        this.tagsId = tagsId;
    }
    
}
