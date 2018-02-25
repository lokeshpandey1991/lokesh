
package com.roche.pharma.customerportal.core.dto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The Class Products.
 */
public class Products {
    
    /** The products. */
    @SerializedName("products")
    @Expose
    private List<Product> products = null;
    
    /**
     * Gets the products.
     * @return the products
     */
    public List<Product> getProducts() {
        return products;
    }
    
    /**
     * Sets the products.
     * @param products the new products
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
}
