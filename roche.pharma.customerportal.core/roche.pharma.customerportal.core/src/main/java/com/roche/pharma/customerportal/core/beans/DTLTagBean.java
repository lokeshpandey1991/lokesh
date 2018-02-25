
package com.roche.pharma.customerportal.core.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DTLTagBean {
    
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("responseMsg")
    @Expose
    private String responseMsg;
    @SerializedName("data")
    @Expose
    private Data data;
    
    public Integer getResponseCode() {
        return responseCode;
    }
    
    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }
    
    public String getResponseMsg() {
        return responseMsg;
    }
    
    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
    
    public Data getData() {
        return data;
    }
    
    public void setData(Data data) {
        this.data = data;
    }
    
}
