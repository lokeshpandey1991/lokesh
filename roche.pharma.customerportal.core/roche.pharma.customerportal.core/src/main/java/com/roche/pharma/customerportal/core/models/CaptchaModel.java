package com.roche.pharma.customerportal.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.roche.pharma.customerportal.core.services.CaptchaService;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * The Class CaptchaModel.
 * @author kbhar6
 * @version 1.0
 */

@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = CaptchaModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class CaptchaModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/captcha";
    /** The resource. */
    @Self
    private Resource resource;
    
    /** The captcha service. */
    @OSGiService
    private CaptchaService captchaService;
    
    /** The clientSideKey. */
    private String clientSideKey;
    
    /** The is captcha disabled. */
    private boolean isCaptchaDisabled;
    
    /**
     * Post construct.
     */
    @PostConstruct
    protected void postConstruct() {
        this.clientSideKey = captchaService.getClientSideKey();
        isCaptchaDisabled = CommonUtils.isCaptchaDisabled(resource);
    }
    
    /**
     * Gets the client side key.
     * @return the client side key
     */
    public String getClientSideKey() {
        return clientSideKey;
    }
    
    /**
     * Checks if is captcha disabled.
     * @return true, if is captcha disabled
     */
    public boolean isCaptchaDisabled() {
        return isCaptchaDisabled;
    }
    
}
