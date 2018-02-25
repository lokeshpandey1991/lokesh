
package com.roche.pharma.customerportal.core.beans;

import java.util.List;

public class DTLTag {
    
    private String nodeId;
    
    private String path;
    
    private String nodeLevel;
    
    private String structureName;
    
    private List<Title> titles;
    
    /**
     * @return the id
     */
    public String getNodeId() {
        return nodeId;
    }
    
    /**
     * @param id the id to set
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }
    
    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
    
    /**
     * @return the nodeLevel
     */
    public String getNodeLevel() {
        return nodeLevel;
    }
    
    /**
     * @param nodeLevel the nodeLevel to set
     */
    public void setNodeLevel(String nodeLevel) {
        this.nodeLevel = nodeLevel;
    }
    
    /**
     * @return the titles
     */
    public List<Title> getTitles() {
        return titles;
    }
    
    /**
     * @param titles the titles to set
     */
    public void setTitles(List<Title> titles) {
        this.titles = titles;
    }
    
    /**
     * @return the structureName
     */
    public String getStructureName() {
        return structureName;
    }
    
    /**
     * @param structureName the structureName to set
     */
    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }
    
}
