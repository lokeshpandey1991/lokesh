package com.roche.pharma.customerportal.core.services;

import java.util.List;
import java.util.Map;


/**
 * The Interface ConfigurationService.
 */
public interface ConfigurationService {
    
    /**
     * Gets the video service url.
     * @return the video service url
     */
    String getVideoServiceUrl();
    
    /**
     * Gets the image service url.
     * @return the image service url
     */
    String getImageServiceUrl();
    
    /**
     * Gets the dynamic media configuration map.
     * @return the dynamic media configuration map
     */
    Map<String, String> getDynamicMediaConfMap();
    
    /**
     * Gets the root path.
     * @return the root path
     */
    String getRootPath();
    
    /**
     * Gets the product logo path.
     * @return the product logo path
     */
    String getProductLogoPath();
    
    Boolean isActivatePDPOnDTLUpdate();
    
    List<String> getDispatcherFlushURL();
    
    List<String> getInstrumentPages();

}
