package com.roche.pharma.customerportal.core.framework;

/**
 * The Class EndPoint.
 *
 * @author Avinash kumar
 */
public class EndPoint {
    

    /** The url. */
    private String url;
    
    /** The active. */
    private boolean active;
    
    /**
     * Instantiates a new end point.
     *
     * @param url the url
     * @param active the active
     */
    public EndPoint(final String url, final boolean active) {
        super();
        this.url = url;
        this.active = active;
    }
    
    /**
     * Gets the url.
     *
     * @return the url
     */
    public final String getUrl() {
        return url;
    }
    
    /**
     * Sets the url.
     *
     * @param url the new url
     */
    public final void setUrl(final String url) {
        this.url = url;
    }
    
    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    public final boolean isActive() {
        return active;
    }
    
    /**
     * Sets the active.
     *
     * @param active the new active
     */
    public final void setActive(final boolean active) {
        this.active = active;
    }
}
