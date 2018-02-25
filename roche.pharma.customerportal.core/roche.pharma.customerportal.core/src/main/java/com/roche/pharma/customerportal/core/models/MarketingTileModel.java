package com.roche.pharma.customerportal.core.models;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.commons.feed.Feed;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * model class for marketingTile component
 * @author asi130
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = MarketingTileModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")

public class MarketingTileModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/marketingtile";
    @Self
    private Resource resource;
    
    @ValueMapValue
    private String articleId;
    
    @ValueMapValue
    private String articleTitle;
    
    @ValueMapValue
    private String articleSummary;
    
    @ValueMapValue
    private String altText;
    
    @ValueMapValue
    private String fileReference;
    
    @ValueMapValue
    private String headline;
    
    @ValueMapValue
    private String ctaLabel;
    
    @ValueMapValue
    private String ctaLink;
    
    @ValueMapValue
    private String imagePosition;
    
    @ValueMapValue
    private String variationType;
    
    private String componentName;
    
    private String contentType;
    
    @PostConstruct
    protected void postConstruct() {
        this.componentName = CommonUtils.getComponentName(resource);
        if (!StringUtils.isBlank(this.articleId)) {
            final Resource articleResource = resource.getResourceResolver()
                    .getResource(articleId + "/jcr:content/product");
            final Resource descriptionResource = resource.getResourceResolver()
                    .getResource(articleId + "/jcr:content/productDescription");
            checkArticleTitle(articleResource);
            checkFileReference(articleResource);
            checkAltText(articleResource);
            checkArticleSummary(descriptionResource);
        }
        checkContentType();
    }
    
    /**
     * sets the contentType of the page configured in CTA link
     */
    private void checkContentType() {
        if (CommonUtils.isInternalLink(this.ctaLink)) {
            final Resource ctaResource = resource.getResourceResolver().resolve(ctaLink + "/jcr:content");
            if (!ResourceUtil.isNonExistingResource(ctaResource)) {
                this.contentType = (String) ctaResource.getValueMap().get("pageType");
            }
        }
    }
    
    /**
     * Checks file reference is author over written then update the value
     * @param resource parameter for the method
     */
    public void checkFileReference(final Resource resource) {
        if (resource != null && StringUtils.isBlank(this.fileReference)) {
            final Map<String, Object> pageValueMap = resource.getValueMap();
            this.fileReference = (String) pageValueMap.get("fileReference");
        }
    }
    
    /**
     * Checks altText is author over written then update the value
     * @param resource parameter for the method
     */
    public void checkAltText(final Resource resource) {
        if (resource != null && StringUtils.isBlank(this.altText)) {
            final Map<String, Object> valueMap = resource.getValueMap();
            this.altText = (String) valueMap.get("altText");
        }
    }
    
    /**
     * Checks articleTitle is author over written then update the value
     * @param resource parameter for the method
     */
    public void checkArticleTitle(final Resource resource) {
        if (resource != null && StringUtils.isBlank(this.articleTitle)) {
            final Map<String, Object> valueMap = resource.getValueMap();
            this.articleTitle = (String) valueMap.get("productName");
        }
    }
    
    /**
     * Checks articleSummary is author over written then update the value
     * @param resource parameter for the method
     */
    public void checkArticleSummary(final Resource resource) {
        if (resource != null && StringUtils.isBlank(this.articleSummary)) {
            final Map<String, Object> valueMap = resource.getValueMap();
            this.articleSummary = (String) valueMap.get("productDescription");
        }
    }
    
    /**
     * @return the articleId
     */
    public String getArticleId() {
        if (!StringUtils.isBlank(articleId)) {
            return articleId + Feed.SUFFIX_HTML;
        }
        return articleId;
    }
    
    /**
     * @return the articleTitle
     */
    public String getArticleTitle() {
        return articleTitle;
    }
    
    /**
     * @return the articleSummary
     */
    public String getArticleSummary() {
        return articleSummary;
    }
    
    /**
     * @return the altText
     */
    public String getAltText() {
        return altText;
    }
    
    /**
     * @return the fileReference
     */
    public String getFileReference() {
        return fileReference;
    }
    
    /**
     * @return the headline
     */
    public String getHeadline() {
        return headline;
    }
    
    /**
     * @return the ctaLabel
     */
    public String getCtaLabel() {
        return ctaLabel;
    }
    
    /**
     * @return the ctaLink
     */
    public String getCtaLink() {
        if (CommonUtils.isInternalLink(this.articleId) && StringUtils.isBlank(this.ctaLink)) {
            return this.articleId + Feed.SUFFIX_HTML;
        } else {
            if (CommonUtils.isInternalLink(this.ctaLink)) {
                return this.ctaLink + Feed.SUFFIX_HTML;
                
            }
        }
        return ctaLink;
    }
    
    /**
     * @return the imagePosition
     */
    public String getImagePosition() {
        return imagePosition;
    }
    
    /**
     * @return the variationType
     */
    public String getVariationType() {
        return variationType;
    }
    
    /**
     * @return componentName
     */
    public String getComponentName() {
        return componentName;
    }
    
    /**
     * @return contentType
     */
    public String getContentType() {
        return contentType;
    }
}
