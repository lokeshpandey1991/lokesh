package com.roche.pharma.customerportal.core.services.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrUtil;
import com.roche.pharma.customerportal.core.dto.Locale;
import com.roche.pharma.customerportal.core.dto.Product;
import com.roche.pharma.customerportal.core.dto.Products;
import com.roche.pharma.customerportal.core.dto.Tag;
import com.roche.pharma.customerportal.core.framework.ServiceResponse;
import com.roche.pharma.customerportal.core.services.ProductImportService;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;

/**
 * The Class ProductImportUtils.
 */
public class ProductImportUtils {
    
    private static final String customerportal_PIM_PRODUCT = "customerportal:pim/product";
    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(ProductImportUtils.class);
    /** The Constant ETC_COMMERCE_PRODUCTS. */
    private static final String ETC_COMMERCE_PRODUCTS = "/etc/commerce/products";
    
    /** The Constant customerportal. */
    private static final String customerportal = "customerportal";
    
    /** The Constant PRODUCT_ROOT_PATH. */
    private static final String PRODUCT_ROOT_PATH = "/etc/commerce/products/customerportal";
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2598426539166789515L;
    
    /** The Constant SLING_FOLDER. */
    private static final String SLING_FOLDER = "sling:Folder";
    
    /** The Constant UNSTRUCTURED_PROPERTY_CQ_TAGS. */
    private static final String UNSTRUCTURED = "nt:unstructured";
    
    /**
     * Gets the JSON string.
     * @param request the request
     * @param productImportService the product import service
     * @return the JSON string
     */
    public void getJSONString(SlingHttpServletRequest request, ProductImportService productImportService) {
        ResourceResolver resolver = request.getResourceResolver();
        String productId = request.getParameter("productId");
        productId = productId.trim();
        ServiceResponse<Products> serviceResponse = new ServiceResponse<Products>();
        try {
            serviceResponse = productId.equalsIgnoreCase("bulk") ? productImportService.getProductList(request)
                    : productImportService.getProductList(request, productId);
            
            final Products products = serviceResponse.getData();
            final List<com.roche.pharma.customerportal.core.dto.Product> productList = products.getProducts();
            if (null != resolver && resolver.isLive() && !productList.isEmpty()) {
                if (productId.equalsIgnoreCase("bulk")) {
                    deleteProductNode(resolver, PRODUCT_ROOT_PATH);
                    createProductRootPath(resolver);
                }
                createProducts(productList, resolver);
            }
        } catch (WebserviceException e) {
            LOG.error("WebserviceException Error in ProductNodeCreate::doGet() " + e.getMessage());
        } catch (PersistenceException e) {
            LOG.error("PersistenceException Error in ProductNodeCreate::doGet() " + e.getMessage());
        } catch (RepositoryException e) {
            LOG.error("RepositoryException Error in ProductNodeCreate::doGet() " + e.getMessage());
        }
    }
    
    /**
     * Create all products.
     * @param productList the product list
     * @param resolver the resolver
     * @return the all products
     * @throws PersistenceException the persistence exception
     * @throws RepositoryException the repository exception
     */
    public static void createProducts(List<Product> productList, ResourceResolver resolver)
            throws PersistenceException, RepositoryException {
        
        final Session session = resolver.adaptTo(Session.class);
        for (final Product product : productList) {
            final List<Locale> localeList = product.getLocales();
            if (!localeList.isEmpty()) {
                for (final Locale locale : localeList) {
                    createProductNode(locale, session, resolver);
                }
            }
        }
        if (session != null && session.isLive()) {
            session.save();
            session.logout();
        }
    }
    
    /**
     * Delete product node.
     * @param resolver the resolver
     * @param path the path
     * @throws PersistenceException the persistence exception
     */
    private static void deleteProductNode(ResourceResolver resolver, String path) throws PersistenceException {
        final Resource rocheProduct = resolver.getResource(path);
        if(null != rocheProduct){
        resolver.delete(rocheProduct);
        resolver.commit();
        }
    }
    
    /**
     * Creates the product root path.
     * @param resolver the resolver
     * @throws PersistenceException the persistence exception
     */
    private static void createProductRootPath(ResourceResolver resolver) throws PersistenceException {
        final Resource productRoot = resolver.getResource(ETC_COMMERCE_PRODUCTS);
        if(null != productRoot){
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("jcr:primaryType", "sling:Folder");
        properties.put("cq:commerceProvider", customerportal);
        resolver.create(productRoot, customerportal, properties);
        resolver.commit();
        }
    }
    
    /**
     * Creates the product node.
     * @param locale the locale
     * @param session the session
     * @param resolver the resolver
     * @throws RepositoryException the repository exception
     * @throws PersistenceException the persistence exception
     */
    private static void createProductNode(Locale locale, Session session, ResourceResolver resolver)
            throws RepositoryException, PersistenceException {
        final String productId = locale.getProductId();
        final String productGlobalName = locale.getProductGlobalName();
        final String productLocaleName = locale.getProductLocaleName();
        final String lang = locale.getLocale();
        Node productNode = JcrUtil.createPath(getProductOverlayPath(PRODUCT_ROOT_PATH, productId), SLING_FOLDER,
                SLING_FOLDER, session, false);
        if (StringUtils.startsWith(lang, "en") && StringUtils.isNotBlank(productGlobalName)) {
            LOG.debug("Creating produt node for product ID {}", productId);
            createProductNode(locale, resolver, productId, productGlobalName, productNode);
        } else if (StringUtils.isNotBlank(productLocaleName)) {
            productNode.setProperty("jcr:title." + locale.getLocale(), productLocaleName);
        }
    }
    
    /**
     * Creates the product node.
     * @param locale the locale
     * @param resolver the resolver
     * @param productId the product id
     * @param productGlobalName the product global name
     * @param productNode the product node
     * @throws RepositoryException the repository exception
     * @throws PersistenceException the persistence exception
     */
    private static void createProductNode(Locale locale, ResourceResolver resolver, final String productId,
            final String productGlobalName, Node productNode) throws RepositoryException, PersistenceException {
        Resource productResource = resolver.getResource(productNode.getPath());
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("jcr:primaryType", "nt:unstructured");
        properties.put("sling:resourceType", "commerce/components/product");
        properties.put("cq:commerceType", "product");
        properties.put("jcr:title", productGlobalName);
        properties.put("productType", locale.getProductType());
        properties.put("productImportDate", Calendar.getInstance());
        Resource resource = resolver.create(productResource, productId, properties);
        if (!locale.getTags().isEmpty()) {
            if (resource != null) {
                final ModifiableValueMap map = resource.adaptTo(ModifiableValueMap.class);
                if (map != null) {
                    map.put("cq:tags", getTagList(locale));
                    map.put("jcr:mixinTypes", "cq:Taggable");
                }
            }
        }
        resolver.commit();
    }
    
    /**
     * Creates the node property.
     * @param locale the locale
     * @return the tag list
     */
    private static String[] getTagList(Locale locale) {
        final List<Tag> tagList = locale.getTags();
        String[] tags = new String[tagList.size()];
        int i = 0;
        for (final Tag tag : tagList) {
            tags[i] = customerportal_PIM_PRODUCT + tag.getNodePath();
            i++;
        }
        return tags;
        
    }
    
    /**
     * Gets the product overlay path.
     * @param basePath the base path
     * @param productID the product id
     * @return the product overlay path
     */
    public static String getProductOverlayPath(final String basePath, final String productID) {
        return basePath.replaceFirst("\\/$", "") + getProductPath(productID);
    }
    
    /**
     * Gets the product path.
     * @param productID the product ID
     * @return the product path
     */
    public static String getProductPath(String productID) {
        final StringBuilder path = new StringBuilder();
        for (int i = 0; i < productID.length(); i++) {
            if (i % 3 == 0) { // TODO use StringBuffer and assign constant to 3
                path.append("/");
            }
            path.append(productID.charAt(i));
        }
        path.append("/");
        return path.toString();
    }
    
}
