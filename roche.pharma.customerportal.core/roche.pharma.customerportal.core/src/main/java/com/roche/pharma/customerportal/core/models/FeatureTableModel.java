package com.roche.pharma.customerportal.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * This is Model class for Feature Table.
 * @author mhuss3
 * @version 1.0
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = FeatureTableModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")

public class FeatureTableModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/featuretable";
    
    @ValueMapValue
    private String text;
    
    @ValueMapValue
    private String tableHeading;
    
    @ValueMapValue
    private String tableDescription;
    
    @ValueMapValue
    private String tableDisclaimer;
    
    /**
     * Gets the table text
     * @return text
     */
    public String getTable() {
        return text;
    }
    
    /**
     * Gets the table heading
     * @return tableHeading
     */
    public String getTableHeading() {
        return tableHeading;
    }
    
    /**
     * Gets the table description
     * @return tableDescription
     */
    public String getTableDescription() {
        return tableDescription;
    }
    
    /**
     * Gets the table Disclaimer
     * @return tableDisclaimer
     */
    public String getTableDisclaimer() {
        return tableDisclaimer;
    }
}
