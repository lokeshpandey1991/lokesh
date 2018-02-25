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
import com.roche.pharma.customerportal.core.beans.SocialLinkBean;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is model class for footer component.
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = FooterModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")

public class FooterModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/footer";
    
    @Self
    private Resource resource;
    private String disclaimer;
    private String tradeMark;
    private String language;
    private String countryCode;
    private List<PageLinkBean> footerLinksList = new ArrayList<>();
    private List<SocialLinkBean> socialList = new ArrayList<>();
    private String countrySelectorPath;
    
    /**
     * @return countrySelectorPath
     */
    public String getCountrySelectorPath() {
        return countrySelectorPath;
    }
    
    /**
     * @return disclaimer
     */
    public String getDisclaimer() {
        return disclaimer;
    }
    
    /**
     * @return tradeMark
     */
    public String getTradeMark() {
        return tradeMark;
    }
    
    /**
     * @return language
     */
    public String getLanguage() {
        return language;
    }
    
    /**
     * @return countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }
    
    /**
     * @return footerLinksList
     */
    public List<PageLinkBean> getFooterLinksList() {
        return footerLinksList;
    }
    
    /**
     * @return socialList
     */
    public List<SocialLinkBean> getSocialList() {
        return socialList;
    }
    
    @PostConstruct
    protected void postConstruct() {
        final Page currentPage = CommonUtils.getCurrentPage(resource);
        final Page regionalLanguagePage = CommonUtils.getRegionalLanguagePage(currentPage);
        final LanguageHeaderFooterModel languageHeaderFooterModel = CommonUtils
                .getGlobalHeaderFooterConfigurations(regionalLanguagePage);
        setLanguageFooterConfiguredValues(languageHeaderFooterModel);
        final Page regionalPage = CommonUtils.getRegionalPage(currentPage);
        if (regionalPage != null) {
            countryCode = regionalPage.getTitle().toUpperCase(CommonUtils.getPageLocale(currentPage));
        }
        language = CommonUtils.getPageLocale(currentPage).getDisplayLanguage();
        final LanguageConfigurationsModel languageConfigurationsModel = CommonUtils
                .getlanguageConfigurations(regionalLanguagePage);
        setGlobalConfiguredFooterValues(languageConfigurationsModel);
    }
    
    /**
     * This method set footer values based on language header footer configurations
     * @param languageHeaderFooterModel
     */
    private void setLanguageFooterConfiguredValues(final LanguageHeaderFooterModel languageHeaderFooterModel) {
        if (languageHeaderFooterModel != null) {
            disclaimer = languageHeaderFooterModel.getDisclaimerText();
            tradeMark = languageHeaderFooterModel.getTradeMark();
            footerLinksList = languageHeaderFooterModel.getFooterList();
        }
    }
    
    /**
     * This method set footer value based on language configurations
     * @param languageHeaderFooterModel
     */
    private void setGlobalConfiguredFooterValues(final LanguageConfigurationsModel languageConfigurationsModel) {
        if (languageConfigurationsModel != null) {
            countrySelectorPath = languageConfigurationsModel.getCountrySelectorPagePath();
            socialList = languageConfigurationsModel.getSocialList();
        }
    }
    
}
