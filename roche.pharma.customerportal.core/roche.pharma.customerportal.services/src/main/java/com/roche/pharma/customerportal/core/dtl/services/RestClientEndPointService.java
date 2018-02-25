package com.roche.pharma.customerportal.core.dtl.services;

import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;

import com.roche.pharma.customerportal.core.framework.EndPoint;
import com.roche.pharma.customerportal.core.framework.EndPointSingleton;

// TODO: Auto-generated Javadoc
/**
 * The Class RestClientEndPointService.
 * @author Avinash kumar
 */
@Component(immediate = true, metatype = true, label = "DTL client end point service.",
        description = "DTL rest client end point service.")
@Service(RestClientEndPointService.class)
@Properties({
        @Property(name = "EndpointService.endpoints", label = "Endpoint URLs", value = "http://localhost:3000/",
                description = "Define end points of dtl")
})
public class RestClientEndPointService {
    
    /** The Constant ENDPOINTS. */
    private static final String ENDPOINTS = "EndpointService.endpoints";
    
    /** The eps. */
    private final EndPointSingleton eps = EndPointSingleton.getInstance();
    
    /**
     * Activate.
     * @param componentContext the component context
     */
    @Activate
    protected void activate(final ComponentContext componentContext) {
        final Map<String, String> properties = (Map<String, String>) componentContext.getProperties();
        
        final EndPoint endPoint = new EndPoint(properties.get(ENDPOINTS), true);
        eps.setActiveEndPoint(endPoint.getUrl());
    }
}
