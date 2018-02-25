package com.roche.pharma.customerportal.core.beans;

import org.apache.commons.lang3.StringUtils;

import com.day.cq.commons.feed.Feed;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is page link bean class.
 */
public class PageLinkBean {
    
    private String pageName;
    private String pagePath;
    private String pageSuffix;
    private boolean isVisibleOnMobile;
    private String linkBehaviour;
    private String contentType;
    
    /**
     * @return pageName
     */
    public String getPageName() {
        return pageName;
    }
    
    /**
     * @param pageName
     */
    public void setPageName(final String pageName) {
        this.pageName = pageName;
    }
    
    /**
     * @return pagePath
     */
    public String getPagePath() {
        return pagePath;
    }
    
    /**
     * @param pagePath
     */
    public void setPagePath(final String pagePath) {
        this.pagePath = pagePath;
    }
    
    /**
     * This method returns pageSuffix html if pagePath is internal and not dam assePath and not external path
     * @return pageSuffix
     */
    public String getPageSuffix() {
        pageSuffix = StringUtils.EMPTY;
        if (CommonUtils.isInternalLink(pagePath) && !CommonUtils.isAssetPath(pagePath)) {
            pageSuffix = Feed.SUFFIX_HTML;
        }
        return pageSuffix;
    }
    
    /**
     * @param pageSuffix
     */
    public void setPageSuffix(final String pageSuffix) {
        this.pageSuffix = pageSuffix;
    }
    
    /**
     * @return isVisibleOnMobile
     */
    public boolean isVisibleOnMobile() {
        return isVisibleOnMobile;
    }
    
    /**
     * @param isVisibleOnMobile
     */
    public void setVisibleOnMobile(final boolean isVisibleOnMobile) {
        this.isVisibleOnMobile = isVisibleOnMobile;
    }
    
    /**
     * @return linkBehaviour
     */
    public String getLinkBehaviour() {
        return linkBehaviour;
    }
    
    /**
     * @param linkBehaviour
     */
    public void setLinkBehaviour(final String linkBehaviour) {
        this.linkBehaviour = linkBehaviour;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }
    
}
