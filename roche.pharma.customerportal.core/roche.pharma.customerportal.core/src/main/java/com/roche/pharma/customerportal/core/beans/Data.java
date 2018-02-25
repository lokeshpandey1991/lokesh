
package com.roche.pharma.customerportal.core.beans;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    
    @SerializedName("tags")
    @Expose
    private List<DTLTag> dtlTags = null;
    
    /**
     * @return the dtlTags
     */
    public List<DTLTag> getDtlTags() {
        return dtlTags;
    }
    
    /**
     * @param dtlTags the dtlTags to set
     */
    public void setDtlTags(List<DTLTag> dtlTags) {
        this.dtlTags = dtlTags;
    }
    
}
