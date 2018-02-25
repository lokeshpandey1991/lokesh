package com.roche.pharma.customerportal.core.models;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.dam.api.Asset;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * model class for heromMedia and mediaContainer component.
 * @author asi130
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = HeroMediaModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
/**
 * Sling model class to get the media parameters
 */
public class HeroMediaModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/heromedia";
    
    @Self
    private Resource resource;
    
    @ValueMapValue
    private String altText;
    
    @ValueMapValue
    private String fileReference;
    
    @ValueMapValue
    private String headlinePosition;
    
    @ValueMapValue
    private String headline;
    
    @ValueMapValue
    private String subHeadline;
    
    @ValueMapValue
    private String quoteText;
    
    @ValueMapValue
    private String textPosition;
    
    @ValueMapValue
    private String ctaLabel;
    
    @ValueMapValue
    private String ctaLink;
    
    @ValueMapValue
    private String variationType;
    
    @ValueMapValue
    private String transparentOverlay;
    
    private String assetType = "image";
    
    private String isExternal = "false";
    
    private String videoName;
    
    private String videoLanguage;
    
    private String componentName;
    
    private String contentType;
    
    @PostConstruct
    protected void postConstruct() {
        final ResourceResolver resourceResolver = resource.getResourceResolver();
        if (StringUtils.isNotBlank(fileReference)) {
            final Resource mediaAsset = resourceResolver.resolve(fileReference);
            final Asset asset = mediaAsset.adaptTo(Asset.class);
            componentName = CommonUtils.getComponentName(resource);
            if (null != asset && !asset.getMimeType().contains("image")) {
                this.assetType = "video";
                this.videoName = asset.getMetadataValue("dc:title");
                this.videoLanguage = asset.getMetadataValue("dc:language");
            }
        }
        if (null != this.ctaLink && StringUtils.isNotEmpty(this.ctaLink.replaceAll("\\s+", ""))
                && !this.ctaLink.startsWith("/content")) {
            this.isExternal = "true";
        }
        if (!RocheConstants.TRUE_VALUE.equalsIgnoreCase(this.isExternal) && StringUtils.isNotBlank(ctaLink)) {
            final Resource ctaResource = resourceResolver.resolve(ctaLink + "/jcr:content");
            if (!ResourceUtil.isNonExistingResource(ctaResource)) {
                this.contentType = (String) ctaResource.getValueMap().get("pageType");
            }
        }
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
     * @return the headlinePosition
     */
    public String getHeadlinePosition() {
        return headlinePosition;
    }
    
    /**
     * @return the headline
     */
    public String getHeadline() {
        return headline;
    }
    
    /**
     * @return the subHeadline
     */
    public String getSubHeadline() {
        return subHeadline;
    }
    
    /**
     * @return the quoteText
     */
    public String getQuoteText() {
        return quoteText;
    }
    
    /**
     * @return the textPosition
     */
    public String getTextPosition() {
        return textPosition;
    }
    
    /**
     * @return the cTALabel
     */
    public String getCTALabel() {
        return ctaLabel;
    }
    
    /**
     * @return the cTALink
     */
    public String getCTALink() {
        return ctaLink;
    }
    
    /**
     * @return the assetType
     */
    public String getAssetType() {
        return assetType;
    }
    
    /**
     * @return the isExternal
     */
    public String getIsExternal() {
        return isExternal;
    }
    
    /**
     * @return the variationType
     */
    public String getVariationType() {
        return variationType;
    }
    
    /**
     * @return the transparentOverlay
     */
    public String getTransparentOverlay() {
        return transparentOverlay;
    }
    
    /**
     * @return videoName
     */
    public String getVideoName() {
        return videoName;
    }
    
    /**
     * @return videoLanguage
     */
    public String getVideoLanguage() {
        return videoLanguage;
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
