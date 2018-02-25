package com.roche.pharma.customerportal.core.services;

import org.apache.sling.api.SlingHttpServletRequest;

import com.roche.pharma.customerportal.core.dto.AssayMenuResponse;
import com.roche.pharma.customerportal.core.framework.ServiceResponse;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;

/**
 * The Interface AssayMenuService.
 * @author Avinash kumar
 */
@FunctionalInterface
public interface AssayMenuService {

    /**
     * View assay menu.
     * @param request the request
     * @param productId the product id
     * @param language the language
     * @return the service response
     * @throws WebserviceException the webservice exception
     */
    public ServiceResponse<AssayMenuResponse> viewAssayMenu(SlingHttpServletRequest request, String productId,
            String language) throws WebserviceException;
}
