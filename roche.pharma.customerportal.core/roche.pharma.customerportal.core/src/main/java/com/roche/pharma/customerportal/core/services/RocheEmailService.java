package com.roche.pharma.customerportal.core.services;

import java.util.List;
import java.util.Map;

/**
 * Service to send email for workflow, contact us etc
 * @author asi130
 */
public interface RocheEmailService {
    
    /**
     * Send email method
     * @param emailParams the map of email parameters.
     * @param recipients the array of recipients.
     * @return list of string.
     */
    List<String> sendEmail(Map<String, String> emailParams, String[] recipients);
    
    void sendEmail(Map<String, Object> emailParams);
    
}
