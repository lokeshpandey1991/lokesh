package com.roche.pharma.customerportal.core.services.impl;

import java.lang.reflect.Type;
import java.util.Dictionary;

import javax.ws.rs.core.Response;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.reflect.TypeToken;
import com.roche.pharma.customerportal.core.dtl.services.InvocationBuilderService;
import com.roche.pharma.customerportal.core.dto.Products;
import com.roche.pharma.customerportal.core.framework.ServiceResponse;
import com.roche.pharma.customerportal.core.services.ProductImportService;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;
import com.roche.pharma.customerportal.core.services.models.Header;
import com.roche.pharma.customerportal.core.services.utils.JsonUtil;
import com.roche.pharma.customerportal.core.services.utils.RestClientUtil;

/**
 * The Class ProductImportServiceImpl.
 * @author Karansheel
 */

@Component(metatype = true, immediate = true, label = "Product Import Service",
        name = "com.roche.pharma.customerportal.core.dtl.services.impl.ProductImportService")
@Service(ProductImportService.class)
@Properties({
        @Property(name = "bulk.product.url", label = "Bulk Product Import URL", value = "api/products/bulk",
                description = "URL for Importing All Products"),
        @Property(name = "multiple.product.url", label = "Multiple Product Import URL", value = "api/product/multiple/",
                description = "URL for Importing Multiple Products")
})
public class ProductImportServiceImpl implements ProductImportService {
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductImportServiceImpl.class);
    
    private static final String BULK_IMPORT_PRODUCT_URL = "bulk.product.url";
    
    private static final String MULTIPLE_IMPORT_PRODUCT_URL = "multiple.product.url";
    
    /** The url. */
    private String multipleURL;
    /** The url. */
    private String bulkURL;
    /** The service. */
    @Reference
    private InvocationBuilderService service;
    
    /**
     * Activate.
     * @param context the context
     */
    @Activate
    protected final void activate(final ComponentContext context) {
        final Dictionary<String, String> properties = context.getProperties();
        bulkURL = properties.get(BULK_IMPORT_PRODUCT_URL);
        multipleURL = properties.get(MULTIPLE_IMPORT_PRODUCT_URL);
        
    }
    
    @Override
    public ServiceResponse<Products> getProductList(SlingHttpServletRequest slingRequest) throws WebserviceException {
        final Header header = new Header();
        LOGGER.debug("Product Import Service Start time:" + System.currentTimeMillis());
        final Response response = RestClientUtil.executeGetRequest(bulkURL, header, service);
        LOGGER.debug("View  watch list Service Response  time:" + System.currentTimeMillis());
        final Type type = new TypeToken<ServiceResponse<Products>>() {}.getType();
        return JsonUtil.unmarshalResponse(response, type);
    }
    
    /*
     * (non-Javadoc)
     * @see com.roche.pharma.customerportal.core.services.ProductImportService#getProductList(org.apache.sling.api.
     * SlingHttpServletRequest)
     */
    @Override
    public ServiceResponse<Products> getProductList(SlingHttpServletRequest slingRequest, String productList)
            throws WebserviceException {
        final Header header = new Header();
        LOGGER.debug("Product Import Service Start time:" + System.currentTimeMillis());
        String url = multipleURL;
        url += productList;
        final Response response = RestClientUtil.executeGetRequest(url, header, service);
        LOGGER.debug("View  watch list Service Response  time:" + System.currentTimeMillis());
        final Type type = new TypeToken<ServiceResponse<Products>>() {}.getType();
        return JsonUtil.unmarshalResponse(response, type);
    }
}
