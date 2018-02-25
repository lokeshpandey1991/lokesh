/**
 * 
 */
package com.roche.pharma.customerportal.core.beans;

/**
 * @author agu207
 */
public class SystemSpecificationBean {
    
    /** Sub heading for system specification section */
    private String subHeading;
    
    /** Sub description for system specification section */
    private String bodyText;
    
    /**
     * @return the subHeading
     */
    public String getSubHeading() {
        return subHeading;
    }
    
    /**
     * @param subHeading the subHeading to set
     */
    public void setSubHeading(String subHeading) {
        this.subHeading = subHeading;
    }
    
    /**
     * @return the bodyText
     */
    public String getBodyText() {
        return bodyText;
    }
    
    /**
     * @param bodyText the bodyText to set
     */
    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }
    
}
