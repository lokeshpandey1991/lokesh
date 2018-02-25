
package com.roche.pharma.customerportal.core.dto;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * The Class Related Assay.
 * @author Avinash kumar
 */
public class RelatedAssay {

    @SerializedName("productGlobalName")
    @Expose
    private String productGlobalName;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("indications")
    @Expose
    private List<Indication> indications;

    public String getProductGlobalName() {
        return productGlobalName;
    }

    public void setProductGlobalName(String productGlobalName) {
        this.productGlobalName = productGlobalName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<Indication> getIndications() {
        return indications;
    }

    public void setIndications(List<Indication> indications) {
        this.indications = indications;
    }

}
