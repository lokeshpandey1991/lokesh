package com.roche.pharma.customerportal.core.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

import com.roche.pharma.customerportal.core.services.ConfigurationService;

/**
 * DynamicSearchMediaModel.
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = DynamicSearchMediaModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")

public class DynamicSearchMediaModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/dynamicsearch";
    /** The configuration service. */
    @OSGiService
    private ConfigurationService configurationService;
    
    /** constant productTile. */
    private static final String PRODUCT_TILE = "productTile";
    
    /** constant featuredTile. */
    private static final String FAETURED_TILE = "featuredTile";
    
    /** The Constant DESKTOP. */
    private static final String DESKTOP = "desktop";
    
    /** The Constant TABLETLANDSCAPE. */
    private static final String TABLETLANDSCAPE = "tabletL";
    
    /** The Constant TABLETPORTRAIT. */
    private static final String TABLETPORTRAIT = "tabletP";
    
    /** The Constant MOBILELANDSCAPE. */
    private static final String MOBILELANDSCAPE = "mobileL";
    
    /** The Constant MOBILEPORTRAIT. */
    private static final String MOBILEPORTRAIT = "mobileP";
    
    /** The Constant HYPHEN. */
    private static final String HYPHEN = "-";
    
    /** The Constant DATA ATTRIBUTE. */
    private static final String DATA_ATTRIBURE = "data-";
    
    /** renditionMap. */
    private final Map<String, Map<String, String>> renditionMap = new HashMap<>();
    
    /**
     * Gets the dynamic media configuration.
     * @return the dynamic media configuration
     */
    public Map<String, String> getDynamicMediaConfiguration() {
        return configurationService.getDynamicMediaConfMap();
    }
    
    /**
     * Gets the rendition map.
     * @return renditionMap
     */
    public Map<String, Map<String, String>> getRenditionMap() {
        return renditionMap;
    }
    
    /**
     * Gets the image service URL.
     * @return the image service URL
     */
    public String getImageServiceUrl() {
        return configurationService.getImageServiceUrl();
    }
    
    /**
     * Post construct.
     */
    @PostConstruct
    protected void postConstruct() {
        if (getDynamicMediaConfiguration() != null && !getDynamicMediaConfiguration().isEmpty()) {
            Map<String, String> renditions = new HashMap<>();
            renditions = getRenditionValues(PRODUCT_TILE, renditions);
            renditionMap.put(PRODUCT_TILE, renditions);
            renditions = new HashMap<>();
            renditions = getRenditionValues(FAETURED_TILE, renditions);
            renditionMap.put(FAETURED_TILE, renditions);
        }
    }
    
    /**
     * This will read rendition configuration from dynamic service and fetch values corresponding to product and
     * featured tile renditions will be fetched for all view ports.
     * @param key the key
     * @param renditions the renditions
     * @return the rendition values
     */
    private Map<String, String> getRenditionValues(final String key, final Map<String, String> renditions) {
        getTileRenditions(key, DESKTOP, renditions);
        getTileRenditions(key, TABLETLANDSCAPE, renditions);
        getTileRenditions(key, TABLETPORTRAIT, renditions);
        getTileRenditions(key, MOBILELANDSCAPE, renditions);
        getTileRenditions(key, MOBILEPORTRAIT, renditions);
        if (!renditions.isEmpty()) {
            renditionMap.put(key, renditions);
        }
        return renditions;
    }
    
    /**
     * This will created map with key in format data-viewort and value as renditions defined for corresponding viewport.
     * @param keyValue the key value
     * @param view the view
     * @param renditions the renditions
     * @return the tile renditions
     */
    private void getTileRenditions(final String keyValue, final String view, final Map<String, String> renditions) {
        if (getDynamicMediaConfiguration().containsKey(keyValue + HYPHEN + view)) {
            renditions.put(DATA_ATTRIBURE + view, getDynamicMediaConfiguration().get(keyValue + HYPHEN + view));
        }
    }
}
