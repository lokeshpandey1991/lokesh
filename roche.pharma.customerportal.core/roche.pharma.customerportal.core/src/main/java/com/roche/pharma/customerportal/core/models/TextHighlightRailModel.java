package com.roche.pharma.customerportal.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * The Class TextHighlightRailModel.
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = TextHighlightRailModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class TextHighlightRailModel {
	
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/textHighlightRail";

    /** The resource. */
    @Self
    private Resource resource;

    /** The headline. */
    @ValueMapValue
    private String headline;
    
    /** The body text. */
    @ValueMapValue
    private String bodyText;
    
    /** The cta name. */
    @ValueMapValue
    private String ctaName;
    
    /** The cta link. */
    @ValueMapValue
    private String ctaLink;

    /** The content type. */
    private String contentType;
    


    /** The is external. */
    private boolean isExternal;

    /**
     * Method called after injection of values in Sling Model.
     */
    @PostConstruct
    protected void postConstruct() {
        setPageContentType(getCtaLink());
        setExternal(CommonUtils.isExternalLink(getCtaLink()));
    }
    
    /**
     * Gets the headline.
     * @return the headline
     */
    public String getHeadline() {
        return headline;
    }
    
    /**
     * Gets the body text.
     * @return the body text
     */
    public String getBodyText() {
        return bodyText;
    }
    
    /**
     * Gets the cta name.
     * @return the cta name
     */
    public String getCtaName() {
        return ctaName;
    }
    
    /**
     * Gets the cta link.
     * @return the cta link
     */
    public String getCtaLink() {
        return ctaLink;
    }
    
    /**
     * This method set page content type if page is internal page.
     * @param link the new page content type
     */
    private void setPageContentType(final String link) {
        if (CommonUtils.isInternalLink(link) && !CommonUtils.isAssetPath(link)) {
            this.contentType = CommonUtils.getPageTypeProperty(link, resource.getResourceResolver());
        }
    }
    
    /**
     * Checks if is external.
     * @return true, if is external
     */
    public boolean isExternal() {
        return isExternal;
    }

    /**
     * Sets the external.
     * @param isExternal the new external
     */
    public void setExternal(boolean isExternal) {
        this.isExternal = isExternal;
    }
    
    /**
     * Gets the content type.
     *
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }

}
