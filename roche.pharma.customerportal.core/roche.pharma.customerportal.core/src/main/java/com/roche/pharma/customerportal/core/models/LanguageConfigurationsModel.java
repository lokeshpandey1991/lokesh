package com.roche.pharma.customerportal.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.roche.pharma.customerportal.core.beans.SocialLinkBean;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is Model Class for Language Configurations.
 * @author mayank
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = LanguageConfigurationsModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")

public class LanguageConfigurationsModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/languageconfigurations";
    @Self
    private Resource resource;
    
    @ValueMapValue
    private String countrySelectorPagePath;
    
    @ValueMapValue
    private String personaSelectorPagePath;
    
    @ValueMapValue
    private String dateFormatPattern;
    
    @ValueMapValue
    private boolean isPersonaDisabled;
    
    @ValueMapValue
    private String searchPagePath;
    
    @ValueMapValue
    private String relatedProductsSearchPagePath;
    
    @ValueMapValue
    private String[] socialLinks;
    
    @ValueMapValue
    private String fromEmail;
    
    @ValueMapValue
    private String searchUrl;
    
    @ValueMapValue
    private String searchLimit;
    
    @ValueMapValue
    private String searchCollection;
    
    @ValueMapValue
    private String cookieDisclaimerLink;
    
    @ValueMapValue
    private boolean isCookieNotificationDisabled;
    
    private String componentName;
    
    @ValueMapValue
    private String shareId;
    
    @ValueMapValue
    private String[] socialShareLinks;
    
    @ValueMapValue
    private boolean isCaptchaDisabled;
    
    @ValueMapValue
    private String[] topFacetTags;
    
    private List<SocialLinkBean> socialList = new ArrayList<>();
    
    public List<SocialLinkBean> getSocialList() {
        return socialList;
    }
    
    public String getCountrySelectorPagePath() {
        return countrySelectorPagePath;
    }
    
    /**
     * @return searchCollection
     */
    public String getSearchCollection() {
        return searchCollection;
    }
    
    /**
     * @return searchUrl
     */
    public String getSearchUrl() {
        return searchUrl;
    }
    
    /**
     * @return
     */
    public String getFromEmail() {
        return fromEmail;
    }
    
    /**
     * @return shareId
     */
    public String getShareId() {
        return shareId;
    }
    
    /**
     * @return socialShareLinks
     */
    public String[] getSocialShareLinks() {
        return socialShareLinks;
    }
    
    /**
     * @return searchLimit
     */
    public String getSearchLimit() {
        return searchLimit;
    }
    
    public String getPersonaSelectorPagePath() {
        return personaSelectorPagePath;
    }
    
    public boolean isPersonaDisabled() {
        return isPersonaDisabled;
    }
    
    public String getSearchPagePath() {
        if (searchPagePath != null && resource.getResourceResolver() != null) {
            searchPagePath = resource.getResourceResolver().map(searchPagePath);
        }
        return searchPagePath;
    }
    
    public String getRelatedProductsSearchPagePath() {
        return relatedProductsSearchPagePath;
    }
    
    public String getDateFormatPattern() {
        
        return StringUtils.isBlank(dateFormatPattern) ? RocheConstants.DEFAULT_DATE_DISPLAY_FORMAT : dateFormatPattern;
        
    }
    
    /**
     * Checks if is captcha disabled.
     * @return true, if is captcha disabled
     */
    public boolean isCaptchaDisabled() {
        return isCaptchaDisabled;
    }
    
    public String getCookieDisclaimerLink() {
        return cookieDisclaimerLink;
    }
    
    public boolean isCookieNotificationDisabled() {
        return isCookieNotificationDisabled;
    }
    
    @PostConstruct
    protected void postConstruct() {
        socialList = getMultifieldValue(socialLinks, socialList);
        componentName = CommonUtils.getComponentName(resource);
    }
    
    private List<SocialLinkBean> getMultifieldValue(final String[] jsonArr, final List<SocialLinkBean> pageList) {
        if (jsonArr != null) {
            for (final String json : jsonArr) {
                final SocialLinkBean socialLinkBean = CommonUtils.getMultifield(json, SocialLinkBean.class);
                pageList.add(socialLinkBean);
            }
        }
        
        return pageList;
    }
    
    public String getComponentName() {
        return componentName;
    }
    
    public String[] getTopFacetTags() {
        return topFacetTags;
    }
}
