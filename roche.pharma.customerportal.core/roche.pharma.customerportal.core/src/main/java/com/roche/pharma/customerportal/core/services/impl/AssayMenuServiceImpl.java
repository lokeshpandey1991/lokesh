package com.roche.pharma.customerportal.core.services.impl;

import java.lang.reflect.Type;
import java.util.Dictionary;

import javax.ws.rs.core.Response;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.reflect.TypeToken;
import com.roche.pharma.customerportal.core.dtl.services.InvocationBuilderService;
import com.roche.pharma.customerportal.core.dto.AssayMenuResponse;
import com.roche.pharma.customerportal.core.framework.ServiceResponse;
import com.roche.pharma.customerportal.core.services.AssayMenuService;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;
import com.roche.pharma.customerportal.core.services.models.Header;
import com.roche.pharma.customerportal.core.services.utils.JsonUtil;
import com.roche.pharma.customerportal.core.services.utils.RestClientUtil;

/**
 * The Class AssayMenuServiceImpl.
 * @author Avinash kumar
 */
@Component(metatype = false, immediate = true, name = "com.roche.pharma.customerportal.core.dtl.services.impl.AssayMenuService")
@Service(AssayMenuService.class)
public class AssayMenuServiceImpl implements AssayMenuService {
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AssayMenuServiceImpl.class);
    
    /** The Constant GET_EXISTING_PDODUCTID. */
    @Property(label = "View Assay menu lists", value = "api/product/relatedAssays/<language>/<productId>",
            description = "URL for Existing Assay product")
    private static final String GET_EXISTING_PDODUCTID = "jboss.view.products";
    
    /** The view product id. */
    private String viewProductId;
    
    /** The Constant PRODUCTIDDATA. */
    private static final String PRODUCTIDDATA = "<productId>";
    private static final String LANGUAGEDATA = "<language>";
    
    /** The service. */
    @Reference
    private InvocationBuilderService service;
    
    /**
     * Activate.
     * @param context the context
     */
    @SuppressWarnings("unchecked")
    protected final void activate(final ComponentContext context) {
        final Dictionary<String, String> properties = context.getProperties();
        viewProductId = properties.get(GET_EXISTING_PDODUCTID);
        
    }

    
    @Override
    public ServiceResponse<AssayMenuResponse> viewAssayMenu(SlingHttpServletRequest slingRequest, String productId,
            String language) {
        final Header header = new Header();
        String url = viewProductId.replace(LANGUAGEDATA, language);
        url = url.replace(PRODUCTIDDATA, productId);
        LOGGER.debug("View Assay menu  Service Start time:" + System.currentTimeMillis());
        try {
            final Response response = RestClientUtil.executeGetRequest(url, header, service);
            
            LOGGER.debug("View  watch list Service Response  time:" + System.currentTimeMillis());
            final Type type = new TypeToken<ServiceResponse<AssayMenuResponse>>() {}.getType();
            
            return JsonUtil.unmarshalResponse(response, type);
        } catch (final WebserviceException e) {
            LOGGER.error("Connection refused to connect " + url, e);
        }
        return null;
    }
}
