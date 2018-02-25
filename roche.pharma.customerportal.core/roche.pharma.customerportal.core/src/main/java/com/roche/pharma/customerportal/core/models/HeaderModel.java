package com.roche.pharma.customerportal.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.beans.PageLinkBean;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is model class for Header.
 */

@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = HeaderModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class HeaderModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/header";
    
    @Self
    private Resource resource;
    
    private String logoPath;
    private String logoAltText;
    private String homePagePath;
    private String countryCode;
    private String countrySelectorPath;
    private boolean isPersonaDisabled;
    private String persona;
    private String personaSelectorPath;
    private String searchUrl;
    private String searchLimit;
    private String searchResultPath;
    private String currentChannel;
    private String currentResolvedPath;
    private List<PageLinkBean> headerLinksList = new ArrayList<>();
    
    public String getLogoPath() {
        return logoPath;
    }
    
    public String getLogoAltText() {
        return logoAltText;
    }
    
    public String getHomePagePath() {
        return homePagePath;
    }
    
    public String getCountryCode() {
        return countryCode;
    }
    
    public String getCountrySelectorPath() {
        return countrySelectorPath;
    }
    
    public boolean isPersonaDisabled() {
        return isPersonaDisabled;
    }
    
    public String getPersona() {
        return persona;
    }
    
    public String getPersonaSelectorPath() {
        return personaSelectorPath;
    }
    
    public List<PageLinkBean> getHeaderLinksList() {
        return new ArrayList<PageLinkBean>(headerLinksList);
    }
    
    public String getSearchUrl() {
        return searchUrl;
    }
    
    public String getSearchLimit() {
        return searchLimit;
    }
    
    public String getSearchResultPath() {
        return searchResultPath;
    }
    
    public String getCurrentChannel() {
        return currentChannel;
    }
    
    public String getCurrentResolvedPath() {
        return currentResolvedPath;
    }
    
    @PostConstruct
    protected void postConstruct() {
        final Page currentPage = CommonUtils.getCurrentPage(resource);
        final Page regionalLanguagePage = CommonUtils.getRegionalLanguagePage(currentPage);
        final LanguageHeaderFooterModel languageHeaderFooterModel = CommonUtils
                .getGlobalHeaderFooterConfigurations(regionalLanguagePage);
        setHeaderConfiguredValues(languageHeaderFooterModel);
        final LanguageConfigurationsModel languageConfigurationsModel = CommonUtils
                .getlanguageConfigurations(regionalLanguagePage);
        setLanguageConfiguredValues(languageConfigurationsModel);
        final Page regionalPage = CommonUtils.getRegionalPage(currentPage);
        countryCode = regionalPage.getTitle().toUpperCase(CommonUtils.getPageLocale(currentPage));
        setCurrentChannel(currentPage);
        currentResolvedPath = CommonUtils.getResolvedPathWithHtml(currentPage.getPath(), resource);
    }
    
    /**
     * This method set header values based on global header footer configurations
     * @param languageHeaderFooterModel
     */
    private void setHeaderConfiguredValues(final LanguageHeaderFooterModel languageHeaderFooterModel) {
        if (languageHeaderFooterModel != null) {
            logoPath = languageHeaderFooterModel.getFileReference();
            logoAltText = languageHeaderFooterModel.getLogoAltText();
            homePagePath = languageHeaderFooterModel.getHomePagePath();
            headerLinksList = languageHeaderFooterModel.getHeaderList();
        }
    }
    
    /**
     * This method set header value based on global configurations
     * @param languageConfigurationsModel
     */
    private void setLanguageConfiguredValues(final LanguageConfigurationsModel languageConfigurationsModel) {
        if (languageConfigurationsModel != null) {
            countrySelectorPath = languageConfigurationsModel.getCountrySelectorPagePath();
            personaSelectorPath = languageConfigurationsModel.getPersonaSelectorPagePath();
            isPersonaDisabled = languageConfigurationsModel.isPersonaDisabled();
            searchLimit = languageConfigurationsModel.getSearchLimit();
            searchUrl = languageConfigurationsModel.getSearchUrl();
            searchResultPath = languageConfigurationsModel.getSearchPagePath();
        }
    }
    
    private void setCurrentChannel(final Page currentPage) {
        if (null == currentPage.getAbsoluteParent(RocheConstants.THREE)) {
            currentChannel = "";
        } else {
            currentChannel = currentPage.getAbsoluteParent(RocheConstants.TWO).getName() + RocheConstants.SLASH
                    + currentPage.getAbsoluteParent(RocheConstants.THREE).getName();
        }
    }
    
}
