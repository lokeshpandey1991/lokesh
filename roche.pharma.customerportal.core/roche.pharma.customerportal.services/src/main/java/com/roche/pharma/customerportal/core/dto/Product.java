
package com.roche.pharma.customerportal.core.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The Class Product.
 */
public class Product {
    
    /** The id. */
    @SerializedName("_id")
    @Expose
    private String id;
    
    /** The locales. */
    @SerializedName("locales")
    @Expose
    private List<Locale> locales = new ArrayList<Locale>();
    
    /**
     * Gets the id.
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Gets the locales.
     * @return the locales
     */
    public List<Locale> getLocales() {
        return locales;
    }
    
    /**
     * Sets the locales.
     * @param locales the new locales
     */
    public void setLocales(List<Locale> locales) {
        this.locales = locales;
    }
    
}
