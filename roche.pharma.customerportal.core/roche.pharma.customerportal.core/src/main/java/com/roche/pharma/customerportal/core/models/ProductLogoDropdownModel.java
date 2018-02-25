package com.roche.pharma.customerportal.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.commerce.common.ValueMapDecorator;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.EmptyDataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.roche.pharma.customerportal.core.services.ConfigurationService;

/**
 * Class ProductLogoDropdownModel used to set the value in the dropdown to select the logo in the page properties of the
 * Product Detail Page. It sets the dropdown value as request attribute with name of datasource.
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = ProductLogoDropdownModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class ProductLogoDropdownModel {
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/productLogoDropdown";

    
    /** The sling request. */
    @Self
    private SlingHttpServletRequest slingRequest;
    
    /** The resource resolver. */
    
    private ResourceResolver resourceResolver;
    
    /** The configuration service. */
    @OSGiService
    private ConfigurationService configurationService;

    private static final Logger LOG = LoggerFactory.getLogger(ProductLogoDropdownModel.class);
    
    /**
     * This Method is called when the resource is adapted to the ProductLogoDropdownModel. It calls the populateDropdown
     * method to populate the set dropdown list in the request attribute.
     */
    @PostConstruct
    protected void postConstruct() {
        
        resourceResolver = slingRequest.getResourceResolver();
        populateDropDown();
    }
    
    /**
     * This method is used to get the data source resource list. It reads the ProductLogo Authoring page path from
     * Configuration Service. Add the products logo data from the page in the Select on PDP Page properties. By Default
     * the "No logo" value will be returned. If the productLogoResourrce is null then we are returning the default list.
     * We are Iterating the author confirgured list and adding the values in the list. It calls getDropDownValue method
     * which will return the value with the separator added in it.
     * @return the data source resource list
     */
    private List<Resource> getDataSourceResourceList() {
        ValueMap dropDownMap;
        
        final String productLogoPagePath = configurationService.getProductLogoPath() + "/jcr:content/par";
        LOG.info(">>>>>>>" + productLogoPagePath);
        dropDownMap = new ValueMapDecorator(new HashMap<String, Object>());
        dropDownMap.put("value", "null");
        dropDownMap.put("text", "No Logo");
        final List<Resource> dataSourceResourceList = new ArrayList<Resource>();
        dataSourceResourceList
                .add(new ValueMapResource(resourceResolver, new ResourceMetadata(), "nt:unstructured", dropDownMap));
        final Resource productLogoResource = resourceResolver.getResource(productLogoPagePath);
        if (null == productLogoResource) {
            LOG.info("Product Logo Resource is not null");
            return dataSourceResourceList;
        }
        for (final Resource productLogo : productLogoResource.getChildren()) {
            final ValueMap productLogoPropertyMap = productLogo.getValueMap();
            LOG.info("ProductLogoDropdownModel inside the resource list");
            
            if (null != productLogoPropertyMap.get("logoStatus")) {
                
                dropDownMap = new ValueMapDecorator(new HashMap<String, Object>());
                
                dropDownMap.put("value", getDropDownValue(productLogoPropertyMap));
                dropDownMap.put("text", productLogoPropertyMap.get("logoName").toString());
                LOG.info("ProductLogoDropdownModel Text+ value" + getDropDownValue(productLogoPropertyMap)
                        + productLogoPropertyMap.get("logoName").toString());
                dataSourceResourceList.add(
                        new ValueMapResource(resourceResolver, new ResourceMetadata(), "nt:unstructured", dropDownMap));
                
            }
            
        }
        
        return dataSourceResourceList;
        
    }
    
    /**
     * This method creates the select value for the dropdown where logoname,description and imagepath are separated by
     * the separator. This method is called by getDataSourceResourceList method to set the value in the dropDownMap.
     * @param productLogoPropertyMap properties of the Product Logo Map.
     * @return the drop down value
     */
    private String getDropDownValue(ValueMap productLogoPropertyMap) {
        
        final String SEPARATOR = "'''";
        return productLogoPropertyMap.get("logoName").toString() + SEPARATOR
                + productLogoPropertyMap.get("logoDescription").toString() + SEPARATOR
                + productLogoPropertyMap.get("logoReference").toString();
    }
    
    /**
     * This method sets the dropdown list in the page properties. Fallback is created to set the bydefault as "no logo".
     * It calls the local getDataSourceResourceList() method which returns the list of resources for dropdown options
     * Then it is set in the request attribute to show on the dropdown .
     */
    private void populateDropDown() {
        
        slingRequest.setAttribute(DataSource.class.getName(), EmptyDataSource.instance());
        final List<Resource> dataSourceResourceList = getDataSourceResourceList();
        
        if (null != dataSourceResourceList) {
            final DataSource ds = new SimpleDataSource(dataSourceResourceList.iterator());
            slingRequest.setAttribute(DataSource.class.getName(), ds);
        }
        
    }
    
}
