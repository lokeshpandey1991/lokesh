package com.roche.pharma.customerportal.core.services;

import java.util.Map;

/**
 * CountrySelectorService.
 * @author vrawa6
 * @version 1.0
 */
public interface CountrySelectorService {
    
    /**
     * Gets the Redirect URLs
     * @return the Redirect URLs
     */
    Map<String, String> getRedirectURL();
    
    /**
     * gets the mapping path
     * @return mapping path
     */
    String getMappingPath();
}
