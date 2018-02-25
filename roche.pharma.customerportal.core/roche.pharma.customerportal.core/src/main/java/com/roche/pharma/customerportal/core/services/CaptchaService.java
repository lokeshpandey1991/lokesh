package com.roche.pharma.customerportal.core.services;

/**
 * The Interface ConfigurationService.
 */
public interface CaptchaService {
      
    boolean verify(String gRecaptchaResponse);
    String getClientSideKey();   
    
}
