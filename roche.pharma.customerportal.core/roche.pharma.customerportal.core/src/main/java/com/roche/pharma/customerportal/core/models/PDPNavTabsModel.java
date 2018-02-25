package com.roche.pharma.customerportal.core.models;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
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
 * This is model class for PDPnavtabs component.
 * @author mhuss3
 * @version 2.0
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = PDPNavTabsModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class PDPNavTabsModel {
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/pDPNavTabs";

    /**
     * Resource
     */
    @Self
    private Resource resource;
    
    @ValueMapValue
    private String titleTab1;
    
    @ValueMapValue
    private String titleTab2;
    
    @ValueMapValue
    private String titleTab3;
    
    @ValueMapValue
    private String titleTab4;
    
    @ValueMapValue
    private String hideTab1;
    
    @ValueMapValue
    private String hideTab2;
    
    @ValueMapValue
    private String hideTab3;
    
    @ValueMapValue
    private String hideTab4;
    
    /**
     * Default tab property
     */
    @ValueMapValue
    private String defaultTab;
    
    /**
     * Post construct method. Here default tab value is set.
     */
    @PostConstruct
    protected void postConstruct() {
        final Page currentPage = CommonUtils.getCurrentPage(resource);
        if (null != currentPage && currentPage.getProperties().containsKey(RocheConstants.ASSAY_TYPE)) {
            defaultTab = null == defaultTab ? "tab3" : defaultTab;
        }
    }
    
    /**
     * Gets the default tab
     * @return default tab
     */
    public String getDefaultTab() {
        return StringUtils.isBlank(defaultTab) ? "tab1" : defaultTab;
    }
    
    /**
     * gets if tab1 is to be shown.
     * @return show tab1
     */
    public Boolean getShowTab1() {
        return !RocheConstants.TRUE_VALUE.equalsIgnoreCase(hideTab1);
    }
    
    /**
     * gets if tab2 is to be shown.
     * @return show tab2
     */
    public Boolean getShowTab2() {
        return !RocheConstants.TRUE_VALUE.equalsIgnoreCase(hideTab2);
    }
    
    /**
     * gets if tab3 is to be shown.
     * @return show tab3
     */
    public Boolean getShowTab3() {
        Resource parentResource = resource.getParent();
        if (null != parentResource) {
            String resourceType = parentResource.getResourceType();
            if ((RocheConstants.SMALL_PRODUCT_RESOURCE.equalsIgnoreCase(resourceType) && !isElabDocEnabledForAssays())
                    || (RocheConstants.PRODUCT_RESOURCE.equalsIgnoreCase(resourceType) && !isElabDocEnabled())) {
                return false;
            } else {
                return !RocheConstants.TRUE_VALUE.equalsIgnoreCase(hideTab3);
            }
        } else {
            return !RocheConstants.TRUE_VALUE.equalsIgnoreCase(hideTab3);
        }
    }
    
    /**
     * gets if tab4 is to be shown.
     * @return show tab4
     */
    public Boolean getShowTab4() {
        return !RocheConstants.TRUE_VALUE.equalsIgnoreCase(hideTab4);
    }
    
    /**
     * gets title for tab1
     * @return title tab1
     */
    public String getTitleTab1() {
        return StringUtils.isBlank(titleTab1) ? "rdoe_pdpnavtabs.productInformation" : titleTab1;
    }
    
    /**
     * gets title for tab2
     * @return title tab2
     */
    public String getTitleTab2() {
        return StringUtils.isBlank(titleTab2) ? "rdoe_pdpnavtabs.productSpecs" : titleTab2;
    }
    
    /**
     * gets title for tab3
     * @return title tab3
     */
    public String getTitleTab3() {
        return StringUtils.isBlank(titleTab3) ? "rdoe_pdpnavtabs.documentation" : titleTab3;
    }
    
    /**
     * gets title for tab4
     * @return title tab4
     */
    public String getTitleTab4() {
        return StringUtils.isBlank(titleTab4) ? "rdoe_pdpnavtabs.relatedProducts" : titleTab4;
    }
    
    /**
     * Gets the configuration for elab doc is enabled
     * @return is elab doc enabled
     */
    public boolean isElabDocEnabledForAssays() {
        final boolean isElabDocEnabledForAssays;
        isElabDocEnabledForAssays = CommonUtils.getGlobalConfigurations(resource) == null ? false
                : CommonUtils.getGlobalConfigurations(resource).isElabDocEnabledForAssays();
        return isElabDocEnabledForAssays;
    }
    
    /**
     * Gets the configuration for elab doc is enabled
     * @return is elab doc enabled
     */
    public boolean isElabDocEnabled() {
        final boolean isElabDocEnabled;
        isElabDocEnabled = CommonUtils.getGlobalConfigurations(resource) == null ? false
                : CommonUtils.getGlobalConfigurations(resource).isElabDocEnabled();
        return isElabDocEnabled;
    }
}
