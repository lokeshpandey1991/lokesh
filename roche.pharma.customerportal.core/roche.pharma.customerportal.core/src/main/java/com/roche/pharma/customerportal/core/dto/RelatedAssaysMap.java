package com.roche.pharma.customerportal.core.dto;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The Class RelatedAssaysMap.
 * @author Avinash kumar
 */
public class RelatedAssaysMap {

    /** The category name. */
    @SerializedName("categoryName")
    @Expose
    private String categoryName;

    /** The assays. */
    @SerializedName("assays")
    @Expose
    private List<Assay> assays;

    /**
     * Gets the category name.
     * @return the category name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the category name.
     * @param categoryName the new category name
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Gets the assays.
     * @return the assays
     */

    public List<Assay> getAssays() {
        return assays;
    }

    /**
     * Sets the assays.
     * @param assays the new assays
     */
    public void setAssays(List<Assay> assays) {
        this.assays = assays;
    }

}
