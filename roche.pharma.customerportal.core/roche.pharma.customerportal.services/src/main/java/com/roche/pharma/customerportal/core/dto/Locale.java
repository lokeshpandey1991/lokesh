
package com.roche.pharma.customerportal.core.dto;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The Class Locale.
 */
public class Locale {
    
    /** The locale. */
    @SerializedName("locale")
    @Expose
    private String locale;
    
    /** The product id. */
    @SerializedName("productId")
    @Expose
    private String productId;
    
    /** The product global name. */
    @SerializedName("productGlobalName")
    @Expose
    private String productGlobalName;
    
    /** The product locale name. */
    @SerializedName("productLocaleName")
    @Expose
    private String productLocaleName;
    
    /** The product type. */
    @SerializedName("productType")
    @Expose
    private String productType;
    
    /** The tags. */
    @SerializedName("tags")
    @Expose
    private List<Tag> tags = new ArrayList<Tag>();
    
    /**
     * Gets the locale.
     * @return the locale
     */
    public String getLocale() {
        return locale;
    }
    
    /**
     * Sets the locale.
     * @param locale the new locale
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }
    
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
     * Gets the product global name.
     * @return the product global name
     */
    public String getProductGlobalName() {
        return productGlobalName;
    }
    
    /**
     * Sets the product global name.
     * @param productGlobalName the new product global name
     */
    public void setProductGlobalName(String productGlobalName) {
        this.productGlobalName = productGlobalName;
    }
    
    /**
     * Gets the product locale name.
     * @return the product locale name
     */
    public String getProductLocaleName() {
        return productLocaleName;
    }
    
    /**
     * Sets the product locale name.
     * @param productLocaleName the new product locale name
     */
    public void setProductLocaleName(String productLocaleName) {
        this.productLocaleName = productLocaleName;
    }
    
    /**
     * Gets the product type.
     * @return the product type
     */
    public String getProductType() {
        return productType;
    }
    
    /**
     * Sets the product type.
     * @param productType the new product type
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }
    
    /**
     * Gets the tags.
     * @return the tags
     */
    public List<Tag> getTags() {
        return tags;
    }
    
    /**
     * Sets the tags.
     * @param tags the new tags
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
    
}
