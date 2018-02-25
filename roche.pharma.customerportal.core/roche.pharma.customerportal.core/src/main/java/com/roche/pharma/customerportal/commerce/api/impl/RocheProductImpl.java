package com.roche.pharma.customerportal.commerce.api.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.adobe.cq.commerce.common.AbstractJcrProduct;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * The Class RocheProductImpl.
 */
public class RocheProductImpl extends AbstractJcrProduct {
    
    /** The resource resolver. */
    protected final ResourceResolver resourceResolver;
    
    /** The page manager. */
    protected final PageManager pageManager;
    
    /** The product page. */
    protected Page productPage;
    

    
    /**
     * Instantiates a new roche product impl.
     * @param resource the resource
     */
    public RocheProductImpl(Resource resource) {
        super(resource);
        this.resourceResolver = resource.getResourceResolver();
        this.pageManager = ((PageManager) this.resourceResolver.adaptTo(PageManager.class));
        if (null != pageManager) {
            this.productPage = this.pageManager.getContainingPage(resource);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.adobe.cq.commerce.api.Product#getSKU()
     */
    public String getSKU() {
        return getProperty("identifier", String.class);
    }
    
}
