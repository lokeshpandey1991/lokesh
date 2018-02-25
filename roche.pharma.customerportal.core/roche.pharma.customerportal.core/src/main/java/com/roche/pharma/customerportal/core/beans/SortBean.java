package com.roche.pharma.customerportal.core.beans;

/**
 * This is sort bean class.
 * @author mhuss3
 */
public class SortBean {
    
    private String searchValue;
    
    private String searchType;
    
    private String searchLabel;
    
    /**
     * Gets search value
     * @return
     */
    public String getSearchValue() {
        return searchValue;
    }
    
    /**
     * Sets the search value
     * @param searchValue
     */
    public void setSearchValue(final String searchValue) {
        this.searchValue = searchValue;
    }
    
    /**
     * Gets search type
     * @return
     */
    public String getSearchType() {
        return searchType;
    }
    
    /**
     * Sets search type
     * @param searchType
     */
    public void setSearchType(final String searchType) {
        this.searchType = searchType;
    }
    
    public String getSearchLabel() {
        return searchLabel;
    }
    
    public void setSearchLabel(final String searchLabel) {
        this.searchLabel = searchLabel;
    }
    
}
