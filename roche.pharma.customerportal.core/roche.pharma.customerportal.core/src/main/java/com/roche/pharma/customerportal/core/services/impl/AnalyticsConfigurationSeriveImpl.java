package com.roche.pharma.customerportal.core.services.impl;

import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roche.pharma.customerportal.core.services.AnalyticsConfigurationService;

/**
 * @author asi130 Gets the configurable values for analytics service
 */
@Service(value = AnalyticsConfigurationService.class)
@Component(immediate = true, metatype = true, label = "Roche Analytics Configuration Service")
@Properties({
        @Property(name = "service.description", value = "Service for Global Configuration"),
        @Property(name = "analytics.service.url", label = "Analytics Service Url",
                description = "Set the Analytics service URL", value = ""),
        @Property(name = "analytics.server.name", label = "Server Name", description = "Enter the server name",
                value = "")
})
public class AnalyticsConfigurationSeriveImpl implements AnalyticsConfigurationService {
    
    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(AnalyticsConfigurationSeriveImpl.class);
    
    /** The Constant CLASS_NAME. */
    private static final String CLASS_NAME = "com.roche.pharma.customerportal.core.services.impl.AnalyticsConfigurationSeriveImpl";
    
    /** The image service url. */
    private String analyticsServiceUrl;
    
    /** The video service url. */
    private String analyticsServerName;
    
    /**
     * Activate Method.
     *
     * @param properties the properties
     */
    @Activate
    public void activate(final Map<String, Object> properties) {
        LOG.info(CLASS_NAME + "activating Analytis configuration service");
        readProperties(properties);
    }
    
    private void readProperties(final Map<String, Object> properties) {
        this.analyticsServiceUrl = properties.get("analytics.service.url").toString();
        this.analyticsServerName = properties.get("analytics.server.name").toString();
    }
    
    @Override
    public String getAnalyticsUrl() {
        return this.analyticsServiceUrl;
    }
    
    @Override
    public String getServername() {
        return this.analyticsServerName;
    }
    
}
