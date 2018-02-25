
package com.roche.pharma.customerportal.core.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * The Class Indication.
 * @author Avinash kumar
 */
public class Indication {

    @SerializedName("nodeId")
    @Expose
    private String nodeId;
    @SerializedName("nodeName")
    @Expose
    private String nodeName;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

}
