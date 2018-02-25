package com.roche.pharma.customerportal.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is model class for productDetail component.
 * @author nitin
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = SearchBarFilterModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class SearchBarFilterModel {
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/searchBarFilter";

    
    /** The resource. */
    @Self
    private Resource resource;
    
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
