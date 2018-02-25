package com.roche.pharma.customerportal.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roche.pharma.customerportal.core.exceptions.BusinessExecutionException;
import com.roche.pharma.customerportal.core.services.AnalyticsConfigurationService;

/**
 * Model for returning the onload analytics values required
 * @author asi130
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = AnalyticsModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")

public class AnalyticsModel {
    
    @Self
    private Resource resource;
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/analytics";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsModel.class);
    
    @OSGiService
    private AnalyticsConfigurationService analyticsConfService;
    
    private String analyticsUrl;
    private String sectionLevelOne;
    private String sectionLevelTwo;
    private String sectionLevelThree;
    private String sectionLevelFour;
    
    private static final int PAGELENGTH = 5;
    
    private String serverName;
    
    @PostConstruct
    protected void postConstruct() {
        try {
            final Resource parent = resource.getParent();
            if (null != parent) {
                final String resourcePath = parent.getPath();
                final String[] resourceLevel = resourcePath.split("/");
                if (resourceLevel.length > PAGELENGTH) {
                    setSectionlevelOne(PAGELENGTH, resourceLevel);
                }
                this.analyticsUrl = analyticsConfService.getAnalyticsUrl();
                this.serverName = analyticsConfService.getServername();
            }
        } catch (final BusinessExecutionException e) {
            LOGGER.error("Exception caught in analytics model " + e);
        }
    }
    
    private void setSectionlevelOne(int length, String[] resourceLevel) {
        this.sectionLevelOne = resourceLevel[length];
        final int updatedLength = length + 1;
        if (resourceLevel.length > updatedLength) {
            setSectionlevelTwo(updatedLength, resourceLevel);
        }
    }
    
    private void setSectionlevelTwo(int length, String[] resourceLevel) {
        this.sectionLevelTwo = resourceLevel[length];
        final int updatedLength = length + 1;
        if (resourceLevel.length > updatedLength) {
            setSectionlevelThree(updatedLength, resourceLevel);
        }
    }
    
    private void setSectionlevelThree(int length, String[] resourceLevel) {
        this.sectionLevelThree = resourceLevel[length];
        final int updatedLength = length + 1;
        if (resourceLevel.length > updatedLength) {
            setSectionlevelFour(updatedLength, resourceLevel);
        }
    }
    
    private void setSectionlevelFour(int length, String[] resourceLevel) {
        this.sectionLevelFour = resourceLevel[length];
    }
    
    /**
     * @return sectionLevelOne
     */
    public String getSectionLevelOne() {
        return sectionLevelOne;
    }
    
    /**
     * @return sectionLevelTwo
     */
    public String getSectionLevelTwo() {
        return sectionLevelTwo;
    }
    
    /**
     * @return sectionLevelThree
     */
    public String getSectionLevelThree() {
        return sectionLevelThree;
    }
    
    /**
     * @return sectionLevelFour
     */
    public String getSectionLevelFour() {
        return sectionLevelFour;
    }
    
    /**
     * @return analyticsUrl
     */
    public String getAnalyticsUrl() {
        return analyticsUrl;
    }
    
    /**
     * @return serverName
     */
    public String getServerName() {
        return serverName;
    }
    
}
