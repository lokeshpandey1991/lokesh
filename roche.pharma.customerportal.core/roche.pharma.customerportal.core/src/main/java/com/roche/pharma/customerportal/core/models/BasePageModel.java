package com.roche.pharma.customerportal.core.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.commons.Externalizer;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.utils.CommonUtils;
import com.roche.pharma.customerportal.core.utils.RocheDateUtil;

/**
 * This is basepage model class.
 * @author mhuss3
 */

@Model(adaptables = {
        SlingHttpServletRequest.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = BasePageModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class BasePageModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/basepage";
    
    /** The published date. */
    @ValueMapValue
    private Calendar publishedDate;
    
    /** The page type. */
    @ValueMapValue
    @Named("cq:lastModified")
    private Calendar lastModifiedDate;
    
    /** The description. */
    @ValueMapValue
    @Named(JcrConstants.JCR_DESCRIPTION)
    private String description;
    
    /** The page type. */
    @ValueMapValue
    private String pageType;
    
    /** The form type. */
    @ValueMapValue
    private String formType;
    
    /** The meta values. */
    @ValueMapValue
    private String eventType;
    
    /** The meta values. */
    @ValueMapValue
    private String storyType;
    
    /** The persona tags. */
    @ValueMapValue
    private String[] personaTags;
    
    /** The persona tag. */
    private String personaTag;
    
    /** The replicated date. */
    private String replicatedDate;
    
    /** The event date format. */
    private String eventDateFormat;
    
    private String eventDateRange;
    
    /** The locale. */
    private String locale;
    
    /** The robots. */
    private String robots;
    
    /** The google map key. */
    private String googleMapKey;
    
    /** The resource. */
    private Resource resource;
    
    /** The title. */
    @ValueMapValue
    @Named(JcrConstants.JCR_TITLE)
    private String title;
    
    /** The file reference. */
    @ValueMapValue
    private String fileReference;
    
    /** The alt text. */
    @ValueMapValue
    private String altText;
    
    /** The featured product. */
    @ValueMapValue
    private boolean featuredProduct;
    
    /** The new product. */
    @ValueMapValue
    private boolean newProduct;
    
    /** The event start date. */
    @ValueMapValue
    private Calendar eventStartDate;
    
    /** The event end date. */
    @ValueMapValue
    private Calendar eventEndDate;
    
    /** The location. */
    @ValueMapValue
    private String location;
    
    /** The sling request. */
    @Self
    private SlingHttpServletRequest slingRequest;
    
    /**
     * Collection name for SnP
     */
    private String collectionName;
    
    @OSGiService
    private Externalizer externalizer;
    
    private String currentPageURL;
    
    private String imageFullPath;
    
    private static final String DEFAULT_PERSONA_PATH = "/etc/tags/customerportal/persona";
    
    /**
     * Gets the page type.
     * @return pageType
     */
    public String getPageType() {
        return pageType;
    }
    
    /**
     * Gets the event type.
     * @return eventType
     */
    public String getEventType() {
        return eventType;
    }
    
    /**
     * Gets the story type.
     * @return storyType
     */
    public String getStoryType() {
        return storyType;
    }
    
    /**
     * Gets the replicated date.
     * @return replicatedDate
     */
    public String getReplicatedDate() {
        return replicatedDate;
    }
    
    /**
     * Sets the replicated date.
     * @param replicatedDate the replicatedDate to set
     */
    public void setReplicatedDate(final String replicatedDate) {
        this.replicatedDate = replicatedDate;
    }
    
    /**
     * Gets the locale.
     * @return locale
     */
    public String getLocale() {
        return locale;
    }
    
    /**
     * Gets the robots.
     * @return no follow,no index based on hide in search page property
     */
    public String getRobots() {
        return robots;
    }
    
    /**
     * Gets the alt text.
     * @return Image Alt
     */
    public String getAltText() {
        return altText;
    }
    
    /**
     * Gets the file reference.
     * @return Image Path
     */
    public String getFileReference() {
        return fileReference;
    }
    
    /**
     * Gets the featured product.
     * @return featuredProduct
     */
    public String getFeaturedProduct() {
        return featuredProduct ? "yes" : "no";
    }
    
    /**
     * Gets the new product.
     * @return the new product
     */
    
    public String getNewProduct() {
        return newProduct ? "yes" : "no";
    }
    
    /**
     * Gets the title.
     * @return featuredProduct
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Gets the location.
     * @return the location
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * Gets the Event Dates Range.
     * @return the eventDateRange
     */
    public String getEventDateRange() {
        return eventDateRange;
    }
    
    /**
     * Sets the Event Dates Range.
     * @param eventDateRange the eventDateRange to set
     */
    public void setEventDateRange(final String eventDateRange) {
        this.eventDateRange = eventDateRange;
    }
    
    /**
     * Gets the event date format.
     * @return the eventDateFormat
     */
    public String getEventDateFormat() {
        return eventDateFormat;
    }
    
    /**
     * Gets the language.
     * @return the language
     */
    public String getLanguage() {
        return StringUtils.substringBefore(locale, "_");
    }
    
    /**
     * Gets the country.
     * @return the country
     */
    public String getCountry() {
        return StringUtils.substringAfter(locale, "_");
    }
    
    /**
     * Sets the event date format.
     * @param eventDateFormat the eventDateFormat to set
     */
    public void setEventDateFormat(final String eventDateFormat) {
        this.eventDateFormat = eventDateFormat;
    }
    
    /**
     * @return currentPageURL
     */
    public String getCurrentPageURL() {
        return currentPageURL;
    }
    
    /**
     * @return collectionName
     */
    
    public String getCollectionName() {
        return collectionName;
    }
    
    /**
     * @return imageFullPath
     */
    
    public String getImageFullPath() {
        return imageFullPath;
    }
    
    /**
     * @return formType
     */
    public String getFormType() {
        return formType;
    }
    
    /**
     * Gets the google map key.
     * @return the google map key
     */
    public String getGoogleMapKey() {
        return googleMapKey;
    }
    
    /**
     * Post construct.
     */
    @PostConstruct
    protected void postConstruct() {
        
        resource = slingRequest.getResource();
        final SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy HH:mm:ss z", Locale.ENGLISH);
        final Page currentPage = CommonUtils.getCurrentPage(slingRequest.getResource());
        final Page regnlPage = CommonUtils.getRegionalLanguagePage(currentPage);
        final GlobalConfigurationsModel globalConfigurationsModel = CommonUtils
                .getGlobalConfigurations(slingRequest.getResource());
        if (globalConfigurationsModel != null) {
            googleMapKey = globalConfigurationsModel.getGoogleMapKey();
        }
        final LanguageConfigurationsModel glblConf = CommonUtils.getlanguageConfigurations(regnlPage);
        final Locale pageLocale = CommonUtils.getPageLocale(currentPage);
        publishedDate = null == publishedDate ? lastModifiedDate : publishedDate;
        if (publishedDate != null) {
            sdf.setTimeZone(publishedDate.getTimeZone());
            replicatedDate = sdf.format(publishedDate.getTime());
            setEventDateFormat(evaluateDate(publishedDate, pageLocale, RocheConstants.DATE_DISPLAY_FORMAT_TWO));
        }
        if (glblConf != null) {
            collectionName = glblConf.getSearchCollection() == null ? pageLocale.toString()
                    : glblConf.getSearchCollection();
        }
        if (eventStartDate != null && eventEndDate != null) {
            setEventDateRange(RocheDateUtil.getEventDate(eventStartDate, eventEndDate));
            setEventDateFormat(evaluateDate(eventStartDate, pageLocale, RocheConstants.DATE_DISPLAY_FORMAT_TWO));
            setReplicatedDate(evaluateDate(eventStartDate, pageLocale, "MMM d, yyyy HH:mm:ss z"));
        }
        final Resource productResource = currentPage.getContentResource().getChild("product");
        if (null != productResource) {
            final ProductDetailNameModel model = productResource.adaptTo(ProductDetailNameModel.class);
            setProductMeta(model);
        }
        locale = pageLocale.toString();
        robots = CommonUtils.isHideInSearch(currentPage) ? RocheConstants.ROBOT_VALUE : "";
        setOgData();
        getPersonaTagName(personaTags);
    }
    
    /**
     * This method set the product related meta tags.
     * @param model the new product meta
     */
    public void setProductMeta(final ProductDetailNameModel model) {
        if (null != model) {
            fileReference = StringUtils.isNotBlank(model.getFileReference()) ? model.getFileReference() : fileReference;
            altText = StringUtils.isNotBlank(model.getAltText()) ? model.getAltText() : altText;
            title = StringUtils.isNotBlank(model.getProductName()) ? model.getProductName() : title;
        }
    }
    
    /**
     * This method returns date in required format.
     * @param date the date parameter
     * @param pageLocale given locale
     * @param datePattern required date pattern
     * @return Formatted date
     */
    public String evaluateDate(final Calendar date, final Locale pageLocale, final String datePattern) {
        final SimpleDateFormat format = new SimpleDateFormat(datePattern, pageLocale);
        format.setTimeZone(date.getTimeZone());
        return CommonUtils.getFormattedDate(date.getTime(), format);
    }
    
    /**
     * This method will set page url and source based on externalizer configurations
     */
    private void setOgData() {
        final Page currentPage = CommonUtils.getCurrentPage(slingRequest.getResource());
        if (currentPage != null) {
            currentPageURL = externalizer.absoluteLink(slingRequest, slingRequest.getScheme(),
                    CommonUtils.getResolvedPathWithHtml(currentPage.getPath(), slingRequest.getResource()));
        }
        String source = externalizer.absoluteLink(slingRequest, slingRequest.getScheme(), StringUtils.EMPTY);
        if (StringUtils.isNotBlank(source) && StringUtils.contains(source, RocheConstants.SLASH)
                && StringUtils.isNotBlank(fileReference)) {
            source = StringUtils.stripEnd(source, RocheConstants.SLASH);
            imageFullPath = source + fileReference;
        }
    }
    
    /**
     * This method will return value for description meta tag.
     * @return description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the persona tag.
     * @param personaTag the new persona tag
     */
    public void setPersonaTag(final String personaTag) {
        this.personaTag = personaTag;
    }
    
    /**
     * Gets the persona tag name.
     * @param personaTags List of the persona tags on the page.
     * @return the persona tag name
     */
    private void getPersonaTagName(@Nonnull final String[] personaTags) {
        final StringBuilder personaTagsName = new StringBuilder();
        final TagManager tagManager = resource.getResourceResolver().adaptTo(TagManager.class);
        if (tagManager != null && null != personaTags) {
            for (final String localPersonaTag : personaTags) {
                final Tag tag = tagManager.resolve(localPersonaTag);
                getPersonaMetaData(personaTagsName, tag);
            }
        } else {
            getDefaultPersonaTag(personaTagsName);
        }
        setPersonaTag(StringUtils.isNotBlank(personaTagsName.toString()) ? personaTagsName.toString() : null);
    }
    
    /**
     * Set default persona meta tag if not authored All personas will be added to meta tag and these pages will be
     * available across all personas in search
     * @param personaTagsName
     */
    private void getDefaultPersonaTag(final StringBuilder personaTagsName) {
        if (resource.getResourceResolver() != null
                && resource.getResourceResolver().getResource(DEFAULT_PERSONA_PATH) != null) {
            final Iterator<Resource> childTags = getChildTags(
                    resource.getResourceResolver().getResource(DEFAULT_PERSONA_PATH));
            if (childTags != null) {
                while (childTags.hasNext()) {
                    final Tag tag = childTags.next().adaptTo(Tag.class);
                    getPersonaMetaData(personaTagsName, tag);
                }
            }
        }
    }
    
    /**
     * Get child persona tags
     * @param tagResource
     * @return
     */
    private Iterator<Resource> getChildTags(final Resource tagResource) {
        if (tagResource != null) {
            return tagResource.listChildren();
        }
        return null;
    }
    
    /**
     * @param personaTagsName
     * @param tag
     */
    private void getPersonaMetaData(final StringBuilder personaTagsName, final Tag tag) {
        if (tag != null) {
            if (personaTagsName.length() != 0) {
                personaTagsName.append(',');
            }
            personaTagsName.append(tag.getName());
        }
    }
    
    /**
     * Gets the persona tags.
     * @return the persona tags
     */
    public String[] getPersonaTags() {
        return null == personaTags ? null : personaTags.clone();
    }
    
    /**
     * Gets the persona tag.
     * @return the persona tag
     */
    public String getPersonaTag() {
        return personaTag;
    }
    
}
