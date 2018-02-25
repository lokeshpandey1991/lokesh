package com.roche.pharma.customerportal.core.services.impl;

import java.lang.reflect.Type;
import java.util.Dictionary;

import javax.ws.rs.core.Response;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.reflect.TypeToken;
import com.roche.pharma.customerportal.core.beans.Data;
import com.roche.pharma.customerportal.core.dtl.services.InvocationBuilderService;
import com.roche.pharma.customerportal.core.framework.ServiceResponse;
import com.roche.pharma.customerportal.core.services.RocheTagsImportService;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;
import com.roche.pharma.customerportal.core.services.models.Header;
import com.roche.pharma.customerportal.core.services.utils.JsonUtil;
import com.roche.pharma.customerportal.core.services.utils.RestClientUtil;

/**
 * This is implementation class of the service RocheTagsImportService
 * @author Nitin Kumar
 */
@Component(metatype = true, immediate = true, enabled = true,
        name = "com.roche.pharma.customerportal.core.services.RocheTagsImportService")
@Service(RocheTagsImportService.class)
public class RocheTagsImportServiceImpl implements RocheTagsImportService {
    
    /**
     * This is rest service url which will give json for the tags to be imported so that all tags can be created for
     * roche
     */
    @Property(label = "Product Import URL", value = "/api/products/tags", description = "URL for Importing all tags")
    private static final String IMPORT_PRODUCT_URL = "import.tag.url";
    
    /**
     * Url for rest service
     */
    private String url;
    
    /**
     * Apache sling ResourceResolverFactory
     */
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    /**
     * Service to call rest api
     */
    @Reference
    private InvocationBuilderService service;
    
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(RocheTagsImportServiceImpl.class);
    
    /**
     * This is activate method to get properties from configuration
     * @param context component context for this service
     */
    @SuppressWarnings("unchecked")
    protected final void activate(final ComponentContext context) {
        final Dictionary<String, String> properties = context.getProperties();
        url = properties.get(IMPORT_PRODUCT_URL);
    }
    
    /*
     * This method is used to fetch all tags from the json from DTL rest service (non-Javadoc)
     * @see com.roche.pharma.customerportal.core.services.RocheTagsImportService#getAllTags()
     */
    @Override
    public ServiceResponse<Data> getAllTags() throws WebserviceException {
        LOG.info("RocheTagsImportService getAllTags started");
        
        final Header header = new Header();
        final Response res = RestClientUtil.executeGetRequest(url, header, service);
        final Type type = new TypeToken<ServiceResponse<Data>>() {}.getType();
        return JsonUtil.unmarshalResponse(res, type);
    }
    
}
