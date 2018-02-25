package com.roche.pharma.customerportal.core.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roche.pharma.customerportal.core.services.ConfigurationService;

/**
 * The Class ConfigurationServiceImpl.
 */
/**
 * @author sku154
 */

@Service(value = ConfigurationService.class)
@Component(immediate = true, metatype = true, label = "Global Configuration Service")
@Properties({
        @Property(name = "service.rootPath", value = "/content/customerportal",
                description = "Root path for Roche website"),
        @Property(name = "productlogo.service.pagePath", value = "/content/customerportal/author-product-logos",
                description = "Page path for Product Logo"),
        @Property(name = "service.description", value = "Service for Global Configuration"),
        @Property(name = "image.service.url", label = "Dynamic Media Image Service Url",
                description = "Set the Url for Dynamic Image Service",
                value = "https://roche-staging-h.assetsadobe2.com/is/image"),
        @Property(name = "video.service.url", label = "Dynamic Media Video Service Url",
                description = "Set the Url for Dynamic Video Service",
                value = "https://gateway-eu.assetsadobe.com/DMGateway/"),
        @Property(
                name = "media.service.configuration",
                label = "Dynamic Media Configuration",
                value = {
                    "herobanner-desktop-view=600,400,0.2,0.6,0.8,0.9"
                },
                description = "Use syntax like herobanner-desktop-view=600,400,0.2,0.6,0.8,0.9 ({componentname-devicetype-view=width,height,crop-corordinates})",
                unbounded = PropertyUnbounded.ARRAY, cardinality = 20),
        @Property(name = "activate.pdp.on.dtl.event", label = "Activate PDP On DTL Event",
                description = "Activate PDP On DTL Event", boolValue = false),
        @Property(name = "assay.menu.pages", label = "Assay Menu Pages Paths", value = {
                "/content/customerportal/global/en/instrument", "/content/customerportal/us/en/instrument"
        }, description = "Assay Menu Pages Paths", unbounded = PropertyUnbounded.ARRAY, cardinality = 50),
        @Property(name = "dispatcher.flush.url", label = "Dispatcher Flush URL", value = {
            "http://localhost/dispatcher/invalidate.cache"
        }, unbounded = PropertyUnbounded.ARRAY, cardinality = 20),
})
public class ConfigurationServiceImpl implements ConfigurationService {
    
    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationServiceImpl.class);
    
    /** The Constant CLASS_NAME. */
    private static final String CLASS_NAME = "com.roche.pharma.customerportal.core.services.impl.ConfigurationServiceImpl";
    
    /** The image service url. */
    private String imageServiceUrl;
    
    /** The product logo path. */
    private String productLogoPath;
    
    /** The video service url. */
    private String videoServiceUrl;
    
    private List<String> dispatcherFlushURL;
    
    /** The dynamic media conf map. */
    private Map<String, String> dynamicMediaConfMap;
    
    private List<String> instrumentPages;
    
    /** The root path of Roche website *. */
    private String rootPath;
    
    Boolean activatePDPOnDTLUpdate;
    
    /**
     * Activate.
     * @param properties the properties
     */
    @Activate
    public void activate(final Map<String, Object> properties) {
        readProperties(properties);
    }
    
    /**
     * Read properties.
     * @param properties the properties this method set the properties
     */
    protected void readProperties(final Map<String, Object> properties) {
        LOG.info(properties.toString());
        this.setImageServiceUrl(properties.get("image.service.url").toString());
        this.setVideoServiceUrl(properties.get("video.service.url").toString());
        this.setDynamicMediaConfMap(PropertiesUtil.toMap(properties.get("media.service.configuration"), new String[0]));
        this.setRootPath(properties.get("service.rootPath").toString());
        this.setProductLogoPath(properties.get("productlogo.service.pagePath").toString());
        final String[] asseyMenuPaths = PropertiesUtil.toStringArray(properties.get("assay.menu.pages"));
        final String[] disFlushURLs = PropertiesUtil.toStringArray(properties.get("dispatcher.flush.url"));
        this.setInstrumentPages(Arrays.asList(asseyMenuPaths));
        this.setDispatcherFlushURL(Arrays.asList(disFlushURLs));
        this.setActivatePDPOnDTLUpdate(PropertiesUtil.toBoolean(properties.get("activate.pdp.on.dtl.event"), false));
    }
    
    /**
     * Sets the Image service url. return Image Service URL
     * @return the image service url
     */
    @Override
    public String getImageServiceUrl() {
        return imageServiceUrl;
    }
    
    /**
     * Sets the image service url.
     * @param imageServiceUrl the new image service url
     */
    public void setImageServiceUrl(final String imageServiceUrl) {
        this.imageServiceUrl = imageServiceUrl;
    }
    
    /**
     * Sets the video service url. return Video Service URL
     * @return the video service url
     */
    @Override
    public String getVideoServiceUrl() {
        return videoServiceUrl;
    }
    
    /**
     * Sets the video service url.
     * @param videoServiceUrl the new video service url
     */
    public void setVideoServiceUrl(final String videoServiceUrl) {
        this.videoServiceUrl = videoServiceUrl;
    }
    
    /**
     * Gets the dynamic media conf map.
     * @return the dynamic media conf map
     */
    @Override
    public Map<String, String> getDynamicMediaConfMap() {
        return dynamicMediaConfMap;
    }
    
    /**
     * Sets the dynamic media conf map.
     * @param dynamicMediaConfMap the dynamic media conf map
     */
    public void setDynamicMediaConfMap(final Map<String, String> dynamicMediaConfMap) {
        this.dynamicMediaConfMap = dynamicMediaConfMap;
    }
    
    /*
     * (non-Javadoc)
     * @see com.roche.pharma.customerportal.core.services.ConfigurationService#getRootPath()
     */
    @Override
    public String getRootPath() {
        return rootPath;
    }
    
    /**
     * Sets the root path.
     * @param rootPath the new root path
     */
    public void setRootPath(final String rootPath) {
        this.rootPath = rootPath;
    }
    
    /**
     * Gets the product logo path.
     * @return the product logo path
     */
    @Override
    public String getProductLogoPath() {
        return productLogoPath;
    }
    
    /**
     * Sets the product logo path.
     * @param productLogoPath the new product logo path
     */
    public void setProductLogoPath(final String productLogoPath) {
        this.productLogoPath = productLogoPath;
    }
    
    /*
     * (non-Javadoc)
     * @see com.roche.pharma.customerportal.core.services.ConfigurationService#getInstrumentPages()
     */
    @Override
    public List<String> getInstrumentPages() {
        return instrumentPages;
    }
    
    /**
     * @param instrumentPages
     */
    public void setInstrumentPages(final List<String> instrumentPages) {
        this.instrumentPages = instrumentPages;
    }
    
    /**
     * @return
     */
    @Override
    public List<String> getDispatcherFlushURL() {
        return dispatcherFlushURL;
    }
    
    /**
     * @param dispatcherFlushURL
     */
    public void setDispatcherFlushURL(final List<String> dispatcherFlushURL) {
        this.dispatcherFlushURL = dispatcherFlushURL;
    }
    
    /*
     * (non-Javadoc)
     * @see com.roche.pharma.customerportal.core.services.ConfigurationService#isActivatePDPOnDTLUpdate()
     */
    @Override
    public Boolean isActivatePDPOnDTLUpdate() {
        return activatePDPOnDTLUpdate;
    }
    
    /**
     * @param activatePDPOnDTLUpdate
     */
    public void setActivatePDPOnDTLUpdate(final Boolean activatePDPOnDTLUpdate) {
        this.activatePDPOnDTLUpdate = activatePDPOnDTLUpdate;
    }
    
}
