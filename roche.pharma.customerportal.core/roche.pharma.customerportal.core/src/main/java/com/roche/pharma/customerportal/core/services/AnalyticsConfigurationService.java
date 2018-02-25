package com.roche.pharma.customerportal.core.services;

/**
 * This is interface for Analytics configuration.
 * @author mhuss3
 */
public interface AnalyticsConfigurationService {
    
    /**
     * gets the analytics URL
     * @return the analytic URL
     */
    String getAnalyticsUrl();
    
    /**
     * gets the server name.
     * @return server name.
     */
    String getServername();
    
}
