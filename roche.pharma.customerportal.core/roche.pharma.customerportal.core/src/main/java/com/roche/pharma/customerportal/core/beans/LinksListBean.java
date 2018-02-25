package com.roche.pharma.customerportal.core.beans;

/**
 * This is links list bean class.
 */
public class LinksListBean {
    
    private String linkText;
    
    private String linkURL;
    
    private String pageType;
    
    public String getLinkText() {
        return linkText;
    }
    
    public void setLinkText(final String linkText) {
        this.linkText = linkText;
    }
    
    public String getLinkURL() {
        return linkURL;
    }
    
    public void setLinkURL(final String linkURL) {
        this.linkURL = linkURL;
    }
    
    public String getPageType() {
        return pageType;
    }
    
    public void setPageType(final String pageType) {
        this.pageType = pageType;
    }
    
}
