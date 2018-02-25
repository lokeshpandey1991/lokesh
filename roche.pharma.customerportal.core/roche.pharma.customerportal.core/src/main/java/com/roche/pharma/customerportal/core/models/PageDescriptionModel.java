package com.roche.pharma.customerportal.core.models;

import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.utils.CommonUtils;
import com.roche.pharma.customerportal.core.utils.RocheDateUtil;

/**
 * This PageDescriptionModel class is called from pageDetail component returns the component properties from jcr node,.
 * @author Avinash kumar
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = PageDescriptionModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class PageDescriptionModel {
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/pageDescription";

    private static String evnetTemplate = "/apps/roche/pharma/customerportal/templates/eventDetailTemplate";
    
    @Self
    private Resource resource;
    
    /** The title one. */
    @ValueMapValue
    private String titleOne;
    
    /** The title one. */
    @ValueMapValue
    private String titleThree;
    
    /** The page description one. */
    @ValueMapValue
    private String pageDescriptionOne;
    
    /** The two column view. */
    @ValueMapValue
    private String twoColumnView;
    
    /** The title two. */
    @ValueMapValue
    private String titleTwo;
    
    /** The page description two. */
    @ValueMapValue
    private String pageDescriptionTwo;
    
    /** The label. */
    @ValueMapValue
    private String label;
    
    /** The target. */
    @ValueMapValue
    private String target;
    
    /** The url. */
    @ValueMapValue
    private String url;
    
    private boolean eventPage;
    
    private String location;
    
    private String publishDate;
	
	 /** The title one. */
    @ValueMapValue
    private String viewType;
    
    /** The title one. */
    @ValueMapValue
    private String fileReference;
    
    /** The title one. */
    @ValueMapValue
    private String altText;

    public String getViewType() {
        return viewType;
    }

    public String getFileReference() {
        return fileReference;
    }

    public String getAltText() {
        return altText;
    }
    
    /**
     * Gets the title one entered by Author.
     * @return the title one
     */
    public String getTitleOne() {
        return titleOne;
    }
    
    /**
     * Gets the eventName oOf event name.
     * @return the title one
     */
    public String getTitleThree() {
        return titleThree;
    }
    
    /**
     * Gets the page description one entered by Author.
     * @return the page description one
     */
    public String getPageDescriptionOne() {
        return pageDescriptionOne;
    }
    
    /**
     * Gets the two column view entered by Author.
     * @return the two column view
     */
    public boolean isTwoColumnView() {
        return StringUtils.isNotEmpty(twoColumnView);
    }
    
    /**
     * Gets the title two entered by Author.
     * @return the title two
     */
    public String getTitleTwo() {
        return titleTwo;
    }
    
    /**
     * Gets the page description two entered by Author.
     * @return the page description two
     */
    public String getPageDescriptionTwo() {
        return pageDescriptionTwo;
    }
    
    /**
     * Gets the label entered by Author.
     * @return the label
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * Gets the target entered by Author.
     * @return the target
     */
    public String getTarget() {
        return target;
    }
    
    /**
     * Gets the url entered by Author.
     * @return the url
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * Gets the Event .
     * @return the url
     */
    public String getUrl() {
        return CommonUtils.getPagepathWithExtension(url);
    }
    
    /**
     * Gets the event page.
     * @return the event page
     */
    public boolean isEventPage() {
        return eventPage;
        
    }
    
    @PostConstruct
    protected void postConstruct() {
        
        final Page currentPage = CommonUtils.getCurrentPage(resource);
        final String pageTemplate = (String) currentPage.getProperties().getOrDefault("cq:template", "");
        eventPage = evnetTemplate.equalsIgnoreCase(pageTemplate);
        location = (String) currentPage.getProperties().getOrDefault("location", StringUtils.EMPTY);
        // Parsing the Calendar input date to localize formated Date
        final Calendar eventStartDate = (Calendar) currentPage.getProperties().get("eventStartDate");
        final Calendar eventEndDate = (Calendar) currentPage.getProperties().get("eventEndDate");
        if (eventStartDate != null && eventEndDate != null) {
            publishDate = RocheDateUtil.getEventDate(eventStartDate, eventEndDate);
        }
        
    }
    
    public String getPublishDate() {
        return publishDate;
    }
}
