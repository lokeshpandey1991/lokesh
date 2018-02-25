package com.roche.pharma.customerportal.core.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * SocialShareModel class
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = SocialShareModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class SocialShareModel {
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/socialShare";
	
    @Self
    private Resource resource;
    private String shareId;
    private List<String> socialShareLinks = new ArrayList<>();
    
    /**
     * @return shareId
     */
    public String getShareId() {
        return shareId;
    }

    /**
     * @return socialShareLinks
     */
    public List<String> getSocialShareLinks() {
        return new ArrayList<>(socialShareLinks);
    }
    
    @PostConstruct
    protected void postConstruct() {
        final LanguageConfigurationsModel languageConfigurationsModel = CommonUtils.getlanguageConfigurations(resource);
        if (languageConfigurationsModel != null && languageConfigurationsModel.getSocialShareLinks() != null) {
            shareId = languageConfigurationsModel.getShareId();
            socialShareLinks = Arrays.asList(languageConfigurationsModel.getSocialShareLinks());
        }
    }
    
}
