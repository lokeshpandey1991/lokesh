package com.roche.pharma.customerportal.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.roche.pharma.customerportal.core.beans.PageLinkBean;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is model class for LanguageHeaderFooter.
 * @author mayank
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = LanguageHeaderFooterModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")

public class LanguageHeaderFooterModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/languageheaderfooter";
    
    @Self
    private Resource resource;
    
    @ValueMapValue
    private String disclaimerText;
    
    @ValueMapValue
    private String tradeMark;
    
    @ValueMapValue
    @Named("altText")
    private String logoAltText;
    
    @ValueMapValue
    private String fileReference;
    
    @ValueMapValue
    private String homePagePath;
    
    @ValueMapValue
    private String[] footerLinks;
    
    @ValueMapValue
    private String[] hamburgerMenuLeftLinks;
    
    @ValueMapValue
    private String[] hamburgerMenuRightLinks;
    
    @ValueMapValue
    private String[] headerLinks;
    
    private List<PageLinkBean> footerList = new ArrayList<>();
    
    private List<PageLinkBean> headerList = new ArrayList<>();
    
    private List<PageLinkBean> hamburgerMenuLeftList = new ArrayList<>();
    
    private List<PageLinkBean> hamburgerMenuRightList = new ArrayList<>();
    
    private String componentName;
    
    /**
     * @return disclaimerText
     */
    public String getDisclaimerText() {
        return disclaimerText;
    }
    
    /**
     * @return tradeMark
     */
    public String getTradeMark() {
        return tradeMark;
    }
    
    /**
     * @return logoAltText
     */
    public String getLogoAltText() {
        return logoAltText;
    }
    
    /**
     * @return fileReference
     */
    public String getFileReference() {
        return fileReference;
    }
    
    /**
     * @return homePagePath
     */
    public String getHomePagePath() {
        return homePagePath;
    }
    
    /**
     * @return footerList
     */
    public List<PageLinkBean> getFooterList() {
        return footerList;
    }
    
    /**
     * @return headerList
     */
    public List<PageLinkBean> getHeaderList() {
        return headerList;
    }
    
    /**
     * @return hamburgerMenuLeftList
     */
    public List<PageLinkBean> getHamburgerMenuLeftList() {
        return hamburgerMenuLeftList;
    }
    
    /**
     * @return hamburgerMenuRightList
     */
    public List<PageLinkBean> getHamburgerMenuRightList() {
        return hamburgerMenuRightList;
    }
    
    @PostConstruct
    protected void postConstruct() {
        footerList = getMultifieldValue(footerLinks, footerList);
        headerList = getMultifieldValue(headerLinks, headerList);
        hamburgerMenuLeftList = getMultifieldValue(hamburgerMenuLeftLinks, hamburgerMenuLeftList);
        hamburgerMenuRightList = getMultifieldValue(hamburgerMenuRightLinks, hamburgerMenuRightList);
        componentName = CommonUtils.getComponentName(resource);
    }
    
    private List<PageLinkBean> getMultifieldValue(final String[] jsonArr, final List<PageLinkBean> pageList) {
        if (jsonArr != null) {
            for (final String json : jsonArr) {
                final PageLinkBean pageLinkBean = CommonUtils.getMultifield(json, PageLinkBean.class);
                setPageContentType(pageLinkBean);
                addPageToList(pageList, pageLinkBean);
            }
        }
        
        return pageList;
    }
    
    /**
     * This method set page content type if page is internal page
     * @param pageLinkBean
     */
    private void setPageContentType(final PageLinkBean pageLinkBean) {
        if (CommonUtils.isInternalLink(pageLinkBean.getPagePath())
                && !CommonUtils.isAssetPath(pageLinkBean.getPagePath())) {
            pageLinkBean.setContentType(
                    CommonUtils.getPageTypeProperty(pageLinkBean.getPagePath(), resource.getResourceResolver()));
        }
    }
    
    /**
     * This method add pageBean to list if page is external or internal page with no hideinnav
     * @param pageList
     * @param pageLinkBean
     */
    private void addPageToList(final List<PageLinkBean> pageList, final PageLinkBean pageLinkBean) {
        if (pageLinkBean != null && (CommonUtils.isExternalLink(pageLinkBean.getPagePath())
                || CommonUtils.isInternalLink(pageLinkBean.getPagePath())
                        && !CommonUtils.isHideInNav(pageLinkBean.getPagePath(), resource.getResourceResolver()))) {
            pageList.add(pageLinkBean);
        }
    }
    
    public String getComponentName() {
        return componentName;
    }
    
}
