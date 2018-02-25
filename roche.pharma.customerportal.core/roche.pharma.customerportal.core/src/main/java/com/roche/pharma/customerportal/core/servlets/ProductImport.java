package com.roche.pharma.customerportal.core.servlets;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import com.roche.pharma.customerportal.core.services.ProductImportService;
import com.roche.pharma.customerportal.core.services.utils.ProductImportUtils;

/**
 * The Class ProductNodeCreate.
 */
@SlingServlet(paths = "/bin/createProducts", methods = "POST", metatype = true)
public class ProductImport extends org.apache.sling.api.servlets.SlingAllMethodsServlet {
    
    /** The product import service. */
    @Reference
    private ProductImportService productImportService;
    
    /*
     * (non-Javadoc)
     * @see org.apache.sling.api.servlets.SlingSafeMethodsServlet#doGet(org.apache.sling.api.SlingHttpServletRequest,
     * org.apache.sling.api.SlingHttpServletResponse)
     */
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        ProductImportUtils productImportUtils = new ProductImportUtils();
        productImportUtils.getJSONString(request, productImportService);
    }
    
}
