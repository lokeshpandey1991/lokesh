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
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.roche.pharma.customerportal.core.beans.TimeDetailsListBean;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * The Class GeneralMapModel.
 * @author kbhar6
 * @version 1.0
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = GeneralMapModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")

public class GeneralMapModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/generalmap";
    /** The resource. */
    @Self
    private Resource resource;
    
    /** The longitude. */
    @ValueMapValue
    private String longitude;
    
    /** The latitude. */
    @ValueMapValue
    private String latitude;
    
    /** The address. */
    @ValueMapValue
    private String address;
    
    /** The heading. */
    @ValueMapValue
    private String heading;
    
    /** The cta label. */
    @ValueMapValue
    private String ctaLabel;
    
    /** The cta link. */
    @ValueMapValue
    private String ctaLink;
    
    /** The phone. */
    @ValueMapValue
    private String phoneNumber;
    
    /** The email. */
    @ValueMapValue
    private String email;
    
    /** The link behaviour. */
    @ValueMapValue
    private String linkBehaviour;
    
    /** The timings. */
    @ValueMapValue
    private String[] timings;
    
    /** The link type. */
    @ValueMapValue
    private String linkType;
    
    /** The time detail listing. */
    private List<TimeDetailsListBean> timeDetailListing = new ArrayList<TimeDetailsListBean>();
    
    /**
     * Post construct.
     */
    @PostConstruct
    protected void postConstruct() {
        timeDetailListing = getMultifieldValue(timings, timeDetailListing);
    }
    
    /**
     * Gets the multifield value.
     * @param jsonArray the json array
     * @param beanList the bean list
     * @return the multifield value
     */
    private List<TimeDetailsListBean> getMultifieldValue(final String[] jsonArray,
            final List<TimeDetailsListBean> beanList) {
        if (jsonArray != null) {
            for (final String json : jsonArray) {
                final TimeDetailsListBean listingBean = CommonUtils.getMultifield(json, TimeDetailsListBean.class);
                beanList.add(listingBean);
            }
        }
        return beanList;
    }
    
    /**
     * Gets the time detail listing.
     * @return the time detail listing
     */
    public List<TimeDetailsListBean> getTimeDetailListing() {
        return new ArrayList<TimeDetailsListBean>(timeDetailListing);
    }
    
    /**
     * Gets the longitude.
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }
    
    /**
     * Gets the latitude.
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }
    
    /**
     * Gets the address.
     * @return the address
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * Gets the heading.
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }
    
    /**
     * Gets the cta label.
     * @return the cta label
     */
    public String getCtaLabel() {
        return ctaLabel;
    }
    
    /**
     * Gets the cta link.
     * @return the cta link
     */
    public String getCtaLink() {
        return CommonUtils.getPagepathWithExtension(ctaLink);
    }
    
    /**
     * Gets the phone number.
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Gets the email.
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Gets the link behaviour.
     * @return the link behaviour
     */
    public String getLinkBehaviour() {
        return linkBehaviour;
    }
    
    /**
     * Checks if is link type.
     * @return true, if is link type
     */
    public String getLinkType() {
        return CommonUtils.getLinkType(ctaLink);
    }
    
}
