package com.roche.pharma.customerportal.core.services;

import org.apache.sling.api.SlingHttpServletRequest;

import com.roche.pharma.customerportal.core.dto.Products;
import com.roche.pharma.customerportal.core.framework.ServiceResponse;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;

public interface ProductImportService {
        
    ServiceResponse<Products> getProductList(SlingHttpServletRequest slingRequest,String productList) throws WebserviceException;

    ServiceResponse<Products> getProductList(SlingHttpServletRequest slingRequest) throws WebserviceException;
}
