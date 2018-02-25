package com.roche.pharma.customerportal.commerce.api.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jcr.Node;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;

import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.CommerceService;
import com.adobe.cq.commerce.api.CommerceSession;
import com.adobe.cq.commerce.api.Product;
import com.adobe.cq.commerce.common.AbstractJcrCommerceService;
import com.adobe.cq.commerce.common.CommerceHelper;
import com.adobe.cq.commerce.common.ServiceContext;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * The Class RocheCommerceServiceImpl.
 */
public class RocheCommerceServiceImpl extends AbstractJcrCommerceService implements CommerceService {
    
    /** The resource. */
    final private Resource resource;
    
    /**
     * Instantiates a new roche commerce service impl.
     * @param serviceContext the service context
     * @param resource the resource
     */
    public RocheCommerceServiceImpl(ServiceContext serviceContext, Resource resource) {
        super(serviceContext, resource);
        this.resource = resource;
    }
    
    /*
     * (non-Javadoc)
     * @see com.adobe.cq.commerce.api.CommerceService#isAvailable(java.lang.String)
     */
    public boolean isAvailable(String serviceType) {
        return "commerce-service".equals(serviceType);
    }
    
    /*
     * (non-Javadoc)
     * @see com.adobe.cq.commerce.api.CommerceService#getProduct(java.lang.String)
     */
    public Product getProduct(String path) throws CommerceException {
        Resource productRresourceesource = this.resolver.getResource(path);
        if ((productRresourceesource != null) && (RocheProductImpl.isAProductOrVariant(productRresourceesource))) {
            return new RocheProductImpl(productRresourceesource);
        }
        return null;
    }
    
    /*
     * (non-Javadoc)
     * @see
     * com.adobe.cq.commerce.common.AbstractJcrCommerceService#productRolloutHook(com.adobe.cq.commerce.api.Product,
     * com.day.cq.wcm.api.Page, com.adobe.cq.commerce.api.Product)
     */
    public void productRolloutHook(Product productData, Page productPage, Product product) throws CommerceException {
        try {
            boolean changed = false;
            if (CommerceHelper.copyTags(productData, productPage.getContentResource(), new Predicate() {
                public boolean evaluate(Object o) {
                    return ((Tag) o).getNamespace().getName().equals("customerportal");
                }
            })) {
                changed = true;
            }
            final String language = StringUtils.substringBefore(CommonUtils.getPageLocale(productPage).toString(), "_");
            final Resource contentResource = productPage.getContentResource();
            final ModifiableValueMap map = contentResource != null ? contentResource.adaptTo(ModifiableValueMap.class)
                    : null;
            if (map != null) {
                map.put("jcr:title", productData.getTitle(language));
                String productId =  StringUtils.substringAfterLast(productData.getPath(), "/");
                map.put("productId",productId);
                changed = true;
            }
            
            if (changed) {
                productPage.getPageManager().touch((Node) productPage.adaptTo(Node.class), true, Calendar.getInstance(),
                        false);
            }
        } catch (Exception e) {
            throw new CommerceException("Product rollout hook failed: ", e);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.adobe.cq.commerce.api.CommerceService#getCountries()
     */
    @Override
    public List<String> getCountries() throws CommerceException {
        return new ArrayList<String>();
    }
    
    /*
     * (non-Javadoc)
     * @see com.adobe.cq.commerce.api.CommerceService#getCreditCardTypes()
     */
    @Override
    public List<String> getCreditCardTypes() throws CommerceException {
        return new ArrayList<String>();
    }
    
    /*
     * (non-Javadoc)
     * @see com.adobe.cq.commerce.api.CommerceService#getOrderPredicates()
     */
    @Override
    public List<String> getOrderPredicates() throws CommerceException {
        return new ArrayList<String>();
    }
    
    /*
     * (non-Javadoc)
     * @see com.adobe.cq.commerce.api.CommerceService#login(org.apache.sling.api.SlingHttpServletRequest,
     * org.apache.sling.api.SlingHttpServletResponse)
     */
    @Override
    public CommerceSession login(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws CommerceException {
        return new RocheCommerceSessionImpl(this, request, response, this.resource);
    }
    
}
