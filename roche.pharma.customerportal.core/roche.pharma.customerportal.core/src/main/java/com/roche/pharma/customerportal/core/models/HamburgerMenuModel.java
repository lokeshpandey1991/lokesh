package com.roche.pharma.customerportal.core.models;

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
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is model class for Hamburger menu.
 */

@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = HamburgerMenuModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class HamburgerMenuModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/hamburgermenue";
    
    @Self
    private Resource resource;
    private String languageCode;
    private String countryCode;
    private List<PageLinkBean> hamburgerMenuLeftLinks;
    private List<PageLinkBean> hamburgerMenuRightLinks;
    private String countrySelectorPath;
    private String personaSelectorPath;
    private boolean isPersonaDisabled;
    private String logoAltText;
    
    /**
     * @return logoAltText
     */
    public String getLogoAltText() {
        return logoAltText;
    }
    
    /**
     * @return isPersonaDisabled
     */
    public boolean isPersonaDisabled() {
        return isPersonaDisabled;
    }
    
    /**
     * @return personaSelectorPath
     */
    public String getPersonaSelectorPath() {
        return personaSelectorPath;
    }
    
    /**
     * @return languageCode
     */
    public String getLanguageCode() {
        return languageCode;
    }
    
    /**
     * @return countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }
    
    /**
     * @return hamburgerMenuLeftLinks
     */
    public List<PageLinkBean> getHamburgerMenuLeftLinks() {
        return hamburgerMenuLeftLinks;
    }
    
    /**
     * @return hamburgerMenuRightLinks
     */
    public List<PageLinkBean> getHamburgerMenuRightLinks() {
        return hamburgerMenuRightLinks;
    }
    
    /**
     * @return countrySelectorPath
     */
    public String getCountrySelectorPath() {
        return countrySelectorPath;
    }
    
    /**
     * The post construct method of HamburgerMenuModel class.
     */
    @PostConstruct
    public void postConstruct() {
        final Page currentPage = CommonUtils.getCurrentPage(resource);
        final Page regionalLanguagePage = CommonUtils.getRegionalLanguagePage(currentPage);
        final LanguageHeaderFooterModel globalHeaderFooterModel = CommonUtils
                .getGlobalHeaderFooterConfigurations(regionalLanguagePage);
        setHamburgerLinks(globalHeaderFooterModel);
        final LanguageConfigurationsModel globalConfigurationsModel = CommonUtils
                .getlanguageConfigurations(regionalLanguagePage);
        if (globalConfigurationsModel != null) {
            countrySelectorPath = globalConfigurationsModel.getCountrySelectorPagePath();
            personaSelectorPath = globalConfigurationsModel.getPersonaSelectorPagePath();
            isPersonaDisabled = globalConfigurationsModel.isPersonaDisabled();
        }
        languageCode = CommonUtils.getPageLocale(currentPage).getDisplayLanguage();
        final Page regionalPage = CommonUtils.getRegionalPage(currentPage);
        if (regionalPage != null) {
            countryCode = regionalPage.getTitle().toUpperCase(CommonUtils.getPageLocale(currentPage));
        }
    }
    
    /**
     * This method set hamburger links from languageHeaderFooter configurations
     * @param globalHeaderFooterModel
     */
    private void setHamburgerLinks(final LanguageHeaderFooterModel globalHeaderFooterModel) {
        if (globalHeaderFooterModel != null) {
            logoAltText = globalHeaderFooterModel.getLogoAltText();
            hamburgerMenuLeftLinks = globalHeaderFooterModel.getHamburgerMenuLeftList();
            hamburgerMenuRightLinks = globalHeaderFooterModel.getHamburgerMenuRightList();
            setMobileEnabledFooterLinks(globalHeaderFooterModel.getFooterList());
        }
    }
    
    /**
     * This method iterates footer links and add mobile view enabled links to hambuger menu
     * @param footerLinks
     */
    private void setMobileEnabledFooterLinks(final List<PageLinkBean> footerLinks) {
        if (!footerLinks.isEmpty()) {
            for (final PageLinkBean pageLinkBean : footerLinks) {
                addFooterLinksInHamurger(pageLinkBean);
            }
        }
    }
    
    /**
     * This method add mobile enabled footer links to right hamburger menu list
     * @param pageLinkBean
     */
    private void addFooterLinksInHamurger(final PageLinkBean pageLinkBean) {
        if (pageLinkBean != null && pageLinkBean.isVisibleOnMobile()) {
            hamburgerMenuRightLinks.add(pageLinkBean);
        }
    }
}
