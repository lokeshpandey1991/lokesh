package com.roche.pharma.customerportal.core.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is HeadlineBody Model Class.
 * @author aayush
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = HeadlineBodyModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class HeadlineBodyModel {
    
    public static final String RESOURCE_TYPE = "roche/customerportal/components/headlinebody";
    
    @Self
    private Resource resource;
    
    @ValueMapValue
    private String headlineType;
    
    @ValueMapValue
    private String headlinePosition;
    
    @ValueMapValue
    private String headlineText;
    
    @ValueMapValue
    private String authorName;
    
    @ValueMapValue
    private Calendar publishDate;
    
    @ValueMapValue
    private String bodyText;
    
    @ValueMapValue
    private String[] tagsId;
    
    @ValueMapValue
    private String socialMedia;
    
    private String searchUrl;
    
    private List<String> localTagTitles;
    
    private String formatedPublishDate;
    
    private Boolean socialCheck;
    
    @PostConstruct
    protected void postConstruct() {
        
        final Page currentPage = CommonUtils.getCurrentPage(resource);
        
        final Page regnlPage = CommonUtils.getRegionalLanguagePage(currentPage);
        
        final Locale pageLocale = CommonUtils.getPageLocale(currentPage);
        
        final LanguageConfigurationsModel glblConf = CommonUtils.getlanguageConfigurations(regnlPage);
        
        // Rendering tags from Dialog of the Component
        if (tagsId != null) {
            
            setLocalTagTitles(CommonUtils.initCqTagsByIds(tagsId, pageLocale, resource));
            
            if (glblConf != null) {
                
                setSearchUrl(CommonUtils.getPagepathWithExtension(glblConf.getSearchPagePath()));
            }
        }
        
        // Parsing the Calendar input date to localize formated Date
        String datePattern = RocheConstants.DEFAULT_DATE_DISPLAY_FORMAT;
        if (glblConf != null) {
            datePattern = glblConf.getDateFormatPattern();
        }
        final SimpleDateFormat format = new SimpleDateFormat(datePattern, pageLocale);
        
        if (publishDate != null) {
            format.setTimeZone(publishDate.getTimeZone());
            setFormatedPublishDate(CommonUtils.getFormattedDate(publishDate.getTime(), format));
        }
        
        // Social media Check
        if (null == socialMedia) {
            setSocialCheck(false);
        } else {
            setSocialCheck(true);
        }
        
    }
    
    // return the array of tagsID enter by the author
    public String[] getTagsId() {
        return null == tagsId ? null : tagsId.clone();
    }
    
    // return the headline type
    public String getHeadlineType() {
        return headlineType;
    }
    
    // return the headline position
    public String getHeadlinePosition() {
        return headlinePosition;
    }
    
    // return the headline
    public String getHeadlineText() {
        return headlineText;
    }
    
    // return the Author Name
    public String getAuthorName() {
        return authorName;
    }
    
    // return the Publish date
    public String getPublishDate() {
        return getFormatedPublishDate();
    }
    
    // return the Body Copy
    public String getBodyText() {
        return bodyText;
    }
    
    // return the check for social media
    public Boolean getSocialMedia() {
        return getSocialCheck();
    }
    
    // return the link for tags
    public String getSearchUrl() {
        return searchUrl;
    }
    
    public void setSearchUrl(final String searchUrl) {
        
        this.searchUrl = searchUrl;
    }
    
    // return the list of tag titles
    public List<String> getLocalTagTitles() {
        return localTagTitles;
    }
    
    public void setLocalTagTitles(final List<String> localTagTitles) {
        this.localTagTitles = localTagTitles;
    }
    
    public String getFormatedPublishDate() {
        return formatedPublishDate;
    }
    
    public void setFormatedPublishDate(final String formatedPublishDate) {
        this.formatedPublishDate = formatedPublishDate;
    }
    
    public Boolean getSocialCheck() {
        return socialCheck;
    }
    
    public void setSocialCheck(final Boolean socialCheck) {
        this.socialCheck = socialCheck;
    }
    
}
