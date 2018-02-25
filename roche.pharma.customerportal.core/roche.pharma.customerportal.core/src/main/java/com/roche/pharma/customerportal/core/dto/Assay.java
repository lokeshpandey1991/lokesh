package com.roche.pharma.customerportal.core.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The Class Assay.
 * @author Avinash kumar
 */
public class Assay {
    
    /** The product id. */
    @SerializedName("productId")
    @Expose
    private String productId;

    /** The assay title. */
    @SerializedName("assayTitle")
    @Expose
    private String assayTitle;

    /** The assay url. */
    @SerializedName("assayUrl")
    @Expose
    private String assayUrl;

    /** The legend. */
    @SerializedName("legend")
    @Expose
    private String legend;
    
    /**
     * Gets the product id.
     * @return the product id
     */
    public String getProductId() {
        return productId;
    }
    
    /**
     * Sets the product id.
     * @param productId the new product id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    /**
     * Gets the assay title.
     * @return the assay title
     */
    public String getAssayTitle() {
        return assayTitle;
    }
    
    /**
     * Sets the assay title.
     * @param assayTitle the new assay title
     */
    public void setAssayTitle(String assayTitle) {
        this.assayTitle = assayTitle;
    }
    
    /**
     * Gets the assay url.
     * @return the assay url
     */
    public String getAssayUrl() {
        return assayUrl;
    }
    
    /**
     * Sets the assay url.
     * @param assayUrl the new assay url
     */
    public void setAssayUrl(String assayUrl) {
        this.assayUrl = assayUrl;
    }
    
    /**
     * Gets the legend.
     * @return the legend
     */
    public String getLegend() {
        return legend;
    }
    
    /**
     * Sets the legend.
     * @param legend the new legend
     */
    public void setLegend(String legend) {
        this.legend = legend;
    }
    
}
