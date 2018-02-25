
package com.roche.pharma.customerportal.core.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The Class Tag.
 */
public class Tag {

    /** The node path. */
    @SerializedName("nodePath")
    @Expose
    private String nodePath;
    
    /** The node id. */
    @SerializedName("nodeId")
    @Expose
    private String nodeId;
    
    /** The node name. */
    @SerializedName("nodeName")
    @Expose
    private String nodeName;
    
    /** The node level. */
    @SerializedName("nodeLevel")
    @Expose
    private String nodeLevel;
    
    /** The previous node id. */
    @SerializedName("previousNodeId")
    @Expose
    private String previousNodeId;
    
    /** The node type. */
    @SerializedName("nodeType")
    @Expose
    private String nodeType;
    
    /** The creation date. */
    @SerializedName("creationDate")
    @Expose
    private String creationDate;
    
    /** The last modification date. */
    @SerializedName("lastModificationDate")
    @Expose
    private String lastModificationDate;
    
    /** The deletion date. */
    @SerializedName("deletionDate")
    @Expose
    private String deletionDate;

    /**
     * Gets the node path.
     *
     * @return the node path
     */
    public String getNodePath() {
        return nodePath;
    }

    /**
     * Sets the node path.
     *
     * @param nodePath the new node path
     */
    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * Sets the node id.
     *
     * @param nodeId the new node id
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * Gets the node name.
     *
     * @return the node name
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Sets the node name.
     *
     * @param nodeName the new node name
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Gets the node level.
     *
     * @return the node level
     */
    public String getNodeLevel() {
        return nodeLevel;
    }

    /**
     * Sets the node level.
     *
     * @param nodeLevel the new node level
     */
    public void setNodeLevel(String nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    /**
     * Gets the previous node id.
     *
     * @return the previous node id
     */
    public String getPreviousNodeId() {
        return previousNodeId;
    }

    /**
     * Sets the previous node id.
     *
     * @param previousNodeId the new previous node id
     */
    public void setPreviousNodeId(String previousNodeId) {
        this.previousNodeId = previousNodeId;
    }

    /**
     * Gets the node type.
     *
     * @return the node type
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * Sets the node type.
     *
     * @param nodeType the new node type
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * Gets the creation date.
     *
     * @return the creation date
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date.
     *
     * @param creationDate the new creation date
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the last modification date.
     *
     * @return the last modification date
     */
    public String getLastModificationDate() {
        return lastModificationDate;
    }

    /**
     * Sets the last modification date.
     *
     * @param lastModificationDate the new last modification date
     */
    public void setLastModificationDate(String lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    /**
     * Gets the deletion date.
     *
     * @return the deletion date
     */
    public String getDeletionDate() {
        return deletionDate;
    }

    /**
     * Sets the deletion date.
     *
     * @param deletionDate the new deletion date
     */
    public void setDeletionDate(String deletionDate) {
        this.deletionDate = deletionDate;
    }

}
