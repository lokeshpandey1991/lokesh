package com.roche.pharma.customerportal.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.beans.ContinentBean;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.helper.CountrySelectorModelHelper;
import com.roche.pharma.customerportal.core.services.CountrySelectorService;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This CountrySelectorModel class is called from countryselector component returns the list of countrySelectorList,
 * globalHomePagePath, globalCountryCode & globalLanguageCode
 * @version 1.0
 * @author vrawa6
 */

@Model(adaptables = {
		SlingHttpServletRequest.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = CountrySelectorModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class CountrySelectorModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/countryselector";
    
    @OSGiService
    private CountrySelectorService countrySelectorService;
    
    @Self
    private SlingHttpServletRequest slingRequest;
    
    @ValueMapValue
    private String globalHomePagePath;
    
    @ValueMapValue
    private String globalCountryCode;
    
    @ValueMapValue
    private String globalLanguageCode;
    
    private List<ContinentBean> countrySelectorList = new ArrayList<>();
    
    private String locale;
    
    private String componentName;
    
    private String globalChannel;
    
    public String getLocale() {
        return locale;
    }
    
    public List<ContinentBean> getCountrySelectorList() {
        return countrySelectorList;
    }
    
    public String getGlobalHomePagePath() {
        return globalHomePagePath;
    }
    
    public String getGlobalCountryCode() {
        return globalCountryCode;
    }
    
    public String getGlobalLanguageCode() {
        return globalLanguageCode;
    }
    
    public String getGlobalChannel() {
        return globalChannel;
    }
    
    @PostConstruct
    protected void postConstruct() {
        final Map<String, String> redirects = countrySelectorService.getRedirectURL();
        final String mappingPath = countrySelectorService.getMappingPath();
        final String tempGlobalHomePagePath = StringUtils.isNotBlank(getGlobalHomePagePath()) ? getGlobalHomePagePath()
                : "/content/customerportal/us/en/home";
        globalHomePagePath = CommonUtils.getResolvedPathWithHtml(tempGlobalHomePagePath, slingRequest.getResource());
        globalCountryCode = StringUtils.isNotBlank(getGlobalCountryCode()) ? getGlobalCountryCode().toLowerCase()
                : "us";
        globalLanguageCode = StringUtils.isNotBlank(getGlobalLanguageCode()) ? getGlobalLanguageCode().toLowerCase()
                : "en";
        countrySelectorList = CountrySelectorModelHelper.getCountryContinentList(slingRequest, redirects, mappingPath);
        locale = CountrySelectorModelHelper.getLocaleAvailable(slingRequest);
        componentName = CommonUtils.getComponentName(slingRequest.getResource());
        setGlobalChannel(tempGlobalHomePagePath, slingRequest.getResource());
    }
    
    public String getComponentName() {
        return componentName;
    }
    
    private void setGlobalChannel(final String path, final Resource resource) {
        final Resource globalResource = resource.getResourceResolver().getResource(path);
        final Page globalPage = CommonUtils.getCurrentPage(globalResource);
        if (null == globalPage.getAbsoluteParent(RocheConstants.THREE)) {
            globalChannel = "";
        } else {
            globalChannel = globalPage.getAbsoluteParent(RocheConstants.TWO).getName() + RocheConstants.SLASH
                    + globalPage.getAbsoluteParent(RocheConstants.THREE).getName();
        }
    }
}
