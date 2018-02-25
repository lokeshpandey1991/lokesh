package com.roche.pharma.customerportal.core.models;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import com.day.cq.dam.api.Asset;
import com.roche.pharma.customerportal.core.services.ConfigurationService;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * The Class VideoModel.
 * @author mhuss3
 * @version 1.0
 */

@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = VideoModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class VideoModel {
	
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/video";
    
    /** The resource. */
    @Self
    private Resource resource;
    
    /** The asset path. */
    @ValueMapValue
    private String assetPath;
    
    /** The headline. */
    @ValueMapValue
    private String headline;
    
    /** The transparent overlay. */
    @ValueMapValue
    private String transparentOverlay;
    
    /** The poster link. */
    @ValueMapValue
    private String posterLink;
    
    /** The file reference. */
    @ValueMapValue
    private String fileReference;
    
    /** The sling settings. */
    @OSGiService
    private SlingSettingsService slingSettings;
    
    /** The configuration service. */
    @OSGiService
    private ConfigurationService configurationService;
    
    /**
     * Post construct.
     */
    @PostConstruct
    protected void postConstruct() {
        if (StringUtils.isEmpty(assetPath) && StringUtils.isNotEmpty(fileReference)) {
            if (isAuthorMode()) {
                this.assetPath = fileReference;
            } else {
                final ResourceResolver resourceResolver = resource.getResourceResolver();
                final Asset mediaAsset = resourceResolver.resolve(fileReference).adaptTo(Asset.class);
                this.assetPath = mediaAsset == null ? fileReference : CommonUtils.getDynamicMediaVideoUrl(mediaAsset,
                        configurationService);
            }
        }
    }
    
    /**
     * Checks if is author mode.
     * @return true, if is author mode
     */
    private boolean isAuthorMode() {
        final Set<String> runModes = slingSettings.getRunModes();
        return runModes.contains("author") ? true : false;
    }
    
    /**
     * Gets the asset path.
     * @return the asset path
     */
    public String getAssetPath() {
        return assetPath;
    }
    
    /**
     * Gets the headline.
     * @return the headline
     */
    public String getHeadline() {
        return headline;
    }
    
    /**
     * Gets the transparent overlay.
     * @return the transparent overlay
     */
    public String getTransparentOverlay() {
        return transparentOverlay;
    }
    
    /**
     * Gets the poster link.
     * @return the poster link
     */
    public String getPosterLink() {
        return posterLink;
    }
    
}
