package com.roche.pharma.customerportal.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is model class for CookieDisclaimerModel.
 */
/**
 * @author Nitin Kumar
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = CookieDisclaimerModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")

public class CookieDisclaimerModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/cookiedisclaimer";
    /**
     * Self resource
     */
    @Self
    private Resource resource;
    
    /** The is cookie notification disabled. */
    private boolean isCookieNotificationDisabled;
    
    /**
     * The url for the cookie disclaimer model
     */
    private String url;
    
    /**
     * @return url from the language configurations
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * Checks if is cookie notification disabled.
     * @return true, if is cookie notification disabled
     */
    public boolean isCookieNotificationDisabled() {
        return isCookieNotificationDisabled;
    }
    
    /**
     * Post construct method for cookie disclaimer model
     */
    @PostConstruct
    protected void postConstruct() {
        final LanguageConfigurationsModel languageConfigurationsModel = CommonUtils.getlanguageConfigurations(resource);
        if (null != languageConfigurationsModel) {
            final String link = languageConfigurationsModel.getCookieDisclaimerLink();
            isCookieNotificationDisabled = languageConfigurationsModel.isCookieNotificationDisabled();
            if (null != link) {
                url = CommonUtils.getPagepathWithExtension(resource.getResourceResolver().map(link));
            }
        }
    }
}
