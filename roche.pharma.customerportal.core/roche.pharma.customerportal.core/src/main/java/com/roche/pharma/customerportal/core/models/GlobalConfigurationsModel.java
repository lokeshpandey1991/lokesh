package com.roche.pharma.customerportal.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is model class for Global configurations. Configuration which are common across all the websites will get
 * configured on Global level
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = GlobalConfigurationsModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")

public class GlobalConfigurationsModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/globalconfiguration";
    
    /** The resource. */
    @Self
    private Resource resource;
    
    /** The website path. */
    @ValueMapValue
    private String websitePath;
    
    /** The elab URL. */
    @ValueMapValue
    private String elabURL;
    
    /** The google map key. */
    @ValueMapValue
    private String googleMapKey;
    
    /** The component name. */
    private String componentName;
    
    /** The content type. */
    private String contentType;
    
    /** The is captcha disabled. */
    @ValueMapValue
    private boolean isCaptchaDisabled;
    
    /** The config fot DTL is enabled */
    @ValueMapValue
    private boolean isDTLEnabled;
    
    /** The config for meta tag for product data is enabled. */
    @ValueMapValue
    private boolean isProductMetaEnabled;
    
    /** The config for meta tag for elab doc is enabled. */
    @ValueMapValue
    private boolean isElabDocEnabledForAssays;
    
    /** The config for meta tag for elab doc is enabled. */
    @ValueMapValue
    private boolean isElabDocEnabled;
    
    /** The meta values. */
    @ValueMapValue
    private String[] metaTagMap;
    
    /**
     * Post construct.
     */
    @PostConstruct
    protected void postConstruct() {
        componentName = CommonUtils.getComponentName(resource);
        if (!CommonUtils.isExternalLink(websitePath)) {
            contentType = CommonUtils.getPageTypeProperty(websitePath, resource.getResourceResolver());
        }
        
    }
    
    /**
     * Checks if is captcha disabled.
     * @return true, if is captcha disabled
     */
    public boolean isCaptchaDisabled() {
        return isCaptchaDisabled;
    }
    
    /**
     * Gets the configuration for DTL is enabled
     * @return is DTL enabled
     */
    public boolean isDTLEnabled() {
        return isDTLEnabled;
    }
    
    /**
     * Gets the configuration for meta tag for product is enabled
     * @return is product metadata enabled
     */
    public boolean isProductMetaEnabled() {
        return isProductMetaEnabled;
    }
    
    /**
     * Gets the configuration for elab doc to be enabled For assays
     * @return is elab doc enabled
     */
    public boolean isElabDocEnabledForAssays() {
        return isElabDocEnabledForAssays;
    }
    
    /**
     * Gets the configuration for elab doc to be enabled
     * @return is elab doc enabled
     */
    public boolean isElabDocEnabled() {
        return isElabDocEnabled;
    }
    
    /**
     * Gets the component name.
     * @return the component name
     */
    public String getComponentName() {
        return componentName;
    }
    
    /**
     * Gets the website path.
     * @return the website path
     */
    public String getWebsitePath() {
        return websitePath;
    }
    
    /**
     * Gets the elab URL.
     * @return the elab URL
     */
    public String getElabURL() {
        return elabURL;
    }
    
    /**
     * Gets the content type.
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }
    
    /**
     * Gets the google map key.
     * @return the google map key
     */
    public String getGoogleMapKey() {
        return googleMapKey;
    }
    
    /**
     * Gets the meta tags mapping.
     * @return the meta tag map
     */
    public String[] getMetaTagMap() {
        return null == metaTagMap ? null : metaTagMap.clone();
    }
}
