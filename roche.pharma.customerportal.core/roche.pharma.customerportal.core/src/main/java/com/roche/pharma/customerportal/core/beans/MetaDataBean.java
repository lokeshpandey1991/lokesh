package com.roche.pharma.customerportal.core.beans;

/**
 * This is meta data bean class.
 */
public class MetaDataBean {
    private String tagPath;
    private String metaMap;
    
    /**
     * gets the tag path.
     * @return registrationStatus
     */
    public String getTagPath() {
        return tagPath;
    }
    
    /**
     * Sets the tagPath.
     * @param tagPath
     */
    public void setTagPath(final String tagPath) {
        this.tagPath = tagPath;
    }
    
    /**
     * gets the meta map
     * @return metaMap
     */
    public String getMetaMap() {
        return metaMap;
    }
    
    /**
     * Sets the meta map
     * @param metaMap
     */
    public void setMetaMap(final String metaMap) {
        this.metaMap = metaMap;
    }
    
}
