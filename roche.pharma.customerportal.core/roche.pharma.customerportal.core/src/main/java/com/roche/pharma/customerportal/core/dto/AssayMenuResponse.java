
package com.roche.pharma.customerportal.core.dto;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * The Class Assay Menu Response.
 * @author Avinash kumar
 */
public class AssayMenuResponse {

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("relatedAssays")
    @Expose
    private List<RelatedAssay> relatedAssays;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<RelatedAssay> getRelatedAssays() {
        return relatedAssays;
    }

    public void setRelatedAssays(List<RelatedAssay> relatedAssays) {
        this.relatedAssays = relatedAssays;
    }

}
