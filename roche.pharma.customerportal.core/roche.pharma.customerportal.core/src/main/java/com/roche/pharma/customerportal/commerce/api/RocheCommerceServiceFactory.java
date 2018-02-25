package com.roche.pharma.customerportal.commerce.api;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;

import com.adobe.cq.commerce.api.CommerceService;
import com.adobe.cq.commerce.api.CommerceServiceFactory;
import com.adobe.cq.commerce.common.AbstractJcrCommerceServiceFactory;
import com.roche.pharma.customerportal.commerce.api.impl.RocheCommerceServiceImpl;

/**
 * A factory for creating RocheCommerceService objects.
 */
@Component
@Service
@Properties({
        @org.apache.felix.scr.annotations.Property(name = "service.description", value = {
                "Factory for reference implementation commerce service"
        }), @org.apache.felix.scr.annotations.Property(name = "commerceProvider", value = {
                "customerportal"
        }, propertyPrivate = true)
})
public class RocheCommerceServiceFactory extends AbstractJcrCommerceServiceFactory implements CommerceServiceFactory {
    
    /*
     * (non-Javadoc)
     * @see com.adobe.cq.commerce.api.CommerceServiceFactory#getCommerceService(org.apache.sling.api.resource.Resource)
     */
    public CommerceService getCommerceService(Resource res) {
        return new RocheCommerceServiceImpl(getServiceContext(), res);
    }
}
