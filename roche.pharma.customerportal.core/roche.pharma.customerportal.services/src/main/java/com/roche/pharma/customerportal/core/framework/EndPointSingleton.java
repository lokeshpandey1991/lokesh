package com.roche.pharma.customerportal.core.framework;

/**
 * The Class EndPointSingleton.
 *
 * @author Avinash kumar
 */
public final class EndPointSingleton {
    
    /** The singleton. */
    private static EndPointSingleton singleton = new EndPointSingleton();
    
    /** The active end point. */
    private volatile String activeEndPoint;
    
    /**
     * Instantiates a new end point singleton.
     */
    /*
     * A private Constructor prevents any other class from instantiating.
     */
    private EndPointSingleton() {
        
    }
    
    /**
     * Gets the single instance of EndPointSingleton.
     *
     * @return single instance of EndPointSingleton
     */
    /* Static 'instance' method */
    public static EndPointSingleton getInstance() {
        return singleton;
    }
    
    /**
     * Gets the active end point.
     *
     * @return the active end point
     */
    /* Other methods protected by singleton-ness */
    public String getActiveEndPoint() {
        return this.activeEndPoint;
    }
    
    /**
     * Sets the active end point.
     *
     * @param activeEndPoint the new active end point
     */
    /* Other methods protected by singleton-ness */
    public void setActiveEndPoint(final String activeEndPoint) {
        this.activeEndPoint = activeEndPoint;
    }
    
}
