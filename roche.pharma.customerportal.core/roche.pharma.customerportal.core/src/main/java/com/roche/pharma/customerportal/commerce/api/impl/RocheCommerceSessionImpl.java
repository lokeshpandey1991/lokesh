package com.roche.pharma.customerportal.commerce.api.impl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.common.AbstractJcrCommerceService;
import com.adobe.cq.commerce.common.AbstractJcrCommerceSession;

/**
 * The Class RocheCommerceSessionImpl.
 */
public class RocheCommerceSessionImpl extends AbstractJcrCommerceSession {
    
    /**
     * Instantiates a new roche commerce session impl.
     * @param commerceService the commerce service
     * @param request the request
     * @param response the response
     * @param resource the resource
     * @throws CommerceException the commerce exception
     */
    public RocheCommerceSessionImpl(AbstractJcrCommerceService commerceService, SlingHttpServletRequest request,
            SlingHttpServletResponse response, Resource resource) throws CommerceException {
        super(commerceService, request, response, resource);
    }
    
}
