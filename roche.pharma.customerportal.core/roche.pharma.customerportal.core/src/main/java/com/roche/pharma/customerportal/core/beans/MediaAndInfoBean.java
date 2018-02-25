package com.roche.pharma.customerportal.core.beans;

/**
 * This is Media info bean class.
 */
public class MediaAndInfoBean {
    
    private String imageHeading;
    
    private String imageText;
    
    private String imagePath;
    
    private String altText;
    
    /**
     * Gets image heading.
     * @return
     */
    public String getImageHeading() {
        return imageHeading;
    }
    
    /**
     * sets image heading.
     * @param imageHeading
     */
    public void setImageHeading(final String imageHeading) {
        this.imageHeading = imageHeading;
    }
    
    /**
     * @return
     */
    public String getImageText() {
        return imageText;
    }
    
    /**
     * @param imageText
     */
    public void setImageText(final String imageText) {
        this.imageText = imageText;
    }
    
    /**
     * @return
     */
    public String getImagePath() {
        return imagePath;
    }
    
    /**
     * Sets the image path.
     * @param imagePath the new image path
     */
    public void setimagePath(final String imagePath) {
        this.imagePath = imagePath;
    }
    
    /**
     * @return
     */
    public String getAltText() {
        return altText;
    }
    
    /**
     * @param altText
     */
    public void setAltText(final String altText) {
        this.altText = altText;
    }
    
}
