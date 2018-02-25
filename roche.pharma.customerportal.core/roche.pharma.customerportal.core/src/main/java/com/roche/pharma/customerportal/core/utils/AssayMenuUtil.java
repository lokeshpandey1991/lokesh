package com.roche.pharma.customerportal.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.dto.Assay;
import com.roche.pharma.customerportal.core.dto.AssayMenuResponse;
import com.roche.pharma.customerportal.core.dto.Indication;
import com.roche.pharma.customerportal.core.dto.RelatedAssay;
import com.roche.pharma.customerportal.core.dto.RelatedAssaysMap;
import com.roche.pharma.customerportal.core.framework.ServiceResponse;
import com.roche.pharma.customerportal.core.models.AssayMenuModel;

/**
 * The Class AssayMenuUtil.
 * @author Avinash kumar
 */
public final class AssayMenuUtil {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AssayMenuModel.class);

    /** The session. */
    private static volatile Session session;

    private AssayMenuUtil() {

    }

    /** The Constant PRODUCT_LIST. */
    public static final Object PRODUCT_LIST = "products";

    /** The Constant PRODUCT_IMAGE. */
    public static final String PRODUCT_IMAGE = "productImage";

    /** The Constant PRODUCT_NAME. */
    public static final String PRODUCT_NAME = "productName";

    /** The Constant PRODUCT_ALTTEXT. */
    public static final String PRODUCT_ALTTEXT = "altText";

    /** The product path. */
    public static final String PRODUCT_PATH = "productPath";

    /** The page title. */
    public static final String PAGE_TITLE = "assayTitle";

    /** The product type. */
    public static final String PRODUCT_TYPE = "productType";

    /** The page url. */
    public static final String PAGE_URL = "assayUrl";

    /** The product id. */
    public static final String PRODUCT_ID = "productId";

    public static final String COMPONENT_TITLE = "title";

    private static final String PRODUCT_NODEID = "nodeId";

    private static final String PRODUCT_NODE_NAME = "nodeName";

    private static final String PRODUCT_NODE_ASSAY_LIST = "assayList";

    /**
     * Gets the product pages.
     * @param slingRequest the sling request
     * @param builder the builder
     * @return the product pages
     * @throws RepositoryException
     */
    public static Map<String, Map<String, String>> getProductPages(SlingHttpServletRequest slingRequest,
            QueryBuilder builder) throws RepositoryException {
        LOGGER.info("Query started====" + System.currentTimeMillis());
        session = slingRequest.getResourceResolver().adaptTo(Session.class);
        final Page currentPage = CommonUtils.getCurrentPage(slingRequest.getResource());
        if (null != currentPage) {
            final Page languagePage = CommonUtils.getRegionalLanguagePage(currentPage);
            if (null != languagePage) {
                return getProductRootPath(slingRequest, builder, languagePage);
            }
        }
        LOGGER.info("Query ends ====" + System.currentTimeMillis());
        return new HashMap<String, Map<String, String>>();
    }

    private static Map<String, Map<String, String>> getProductRootPath(SlingHttpServletRequest slingRequest,
            QueryBuilder builder, final Page languagePage) throws RepositoryException {
        final Resource resource = slingRequest.getResourceResolver().getResource(
                languagePage.getPath() + "/jcr:content/languageconfig");
        if (null != resource) {
            final String productPath = (String) resource.getValueMap().get("relatedProductsSearchPagePath");
            if (null != productPath) {
                return getproductResult(productPath, builder);
            }
        }
        return new HashMap<String, Map<String, String>>();
    }

    /**
     * Gets the product result.
     * @param productPath the product path
     * @param builder the builder
     * @return the product result
     * @throws RepositoryException
     */
    public static Map<String, Map<String, String>> getproductResult(String productPath, QueryBuilder builder) throws RepositoryException {

        final Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("path", productPath);
        queryMap.put("type", "cq:Page");
        queryMap.put("p.limit", "-1");
        queryMap.put("0_property", "jcr:content/pageType");
        queryMap.put("0_property.value", "Products");
        queryMap.put("1_property", "jcr:content/productType");
        queryMap.put("1_property.0_value", "newProduct");
        queryMap.put("1_property.1_value", "featureProduct");
        queryMap.put("2_property", "jcr:content/productId");
        queryMap.put("2_property.operation", "exists");

        final Query query = builder.createQuery(PredicateGroup.create(queryMap), session);
        final SearchResult result = query.getResult();
        final Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();

        for (final Hit hit : result.getHits()) {
            getPageDataMap(dataMap, hit);
        }
        return dataMap;
    }

    /**
     * Gets the page data map.
     * @param dataMap the data map
     * @param hit the hit
     * @throws RepositoryException
     */
    private static void getPageDataMap(Map<String, Map<String, String>> dataMap, Hit hit) throws RepositoryException {
        final Page page = hit.getResource().adaptTo(Page.class);
        if (null != page) {
            final String productId = (String) page.getProperties().get(PRODUCT_ID);
            final Map<String, String> data = new HashMap<String, String>();
            data.put(PAGE_TITLE, page.getTitle());
            data.put(PAGE_URL, page.getPath());
            data.put(PRODUCT_TYPE, (String) page.getProperties().get(PRODUCT_TYPE));
            dataMap.put(productId, data);
        }

    }

    /**
     * Gets the current page locale.
     * @param slingRequest the sling request
     * @return the current page locale
     */
    public static Locale getCurrentPageLocale(final SlingHttpServletRequest slingRequest) {
        final Page currentPage = CommonUtils.getCurrentPage(slingRequest.getResource());
        if (currentPage == null) {
            return slingRequest.getLocale();
        } else {
            return currentPage.getLanguage(false);
        }
    }

    /**
     * Gets the assaymenu list.
     * @param watchlist the watchlist
     * @param dataMap the data map
     * @param legendsList the legends list
     * @param relatedAssaysMap the related assays map
     * @return the assaymenu list
     */
    public static void getAssaymenuList(ServiceResponse<AssayMenuResponse> watchlist,
            Map<String, Map<String, String>> dataMap, Set<String> legendsList, List<RelatedAssaysMap> relatedAssaysMap) {

        final AssayMenuResponse assayMenu = watchlist.getData();
        if (null != assayMenu && !assayMenu.getRelatedAssays().isEmpty()) {
            final List<RelatedAssay> relatedAssay = assayMenu.getRelatedAssays();
            getAssayMenuMap(dataMap, relatedAssay, relatedAssaysMap, legendsList);
        }
    }

    /**
     * Gets the assay menu map.
     * @param dataMap the data map
     * @param relatedAssayMap the related assay map
     * @param validRelatedAssayMap the valid related assay map
     * @param legendsList
     */
    @SuppressWarnings("unchecked")
    private static void getAssayMenuMap(Map<String, Map<String, String>> dataMap,
            final List<RelatedAssay> relatedAssay, final List<RelatedAssaysMap> relatedResponseAssays,
            Set<String> legendsList) {
        final Map<String, Object> validAssay = new HashMap<String, Object>();

        for (final RelatedAssay assayMap : relatedAssay) {
            final List<Indication> indicationlist = assayMap.getIndications();
            final String productId = assayMap.getProductId();
            if (dataMap.containsKey(assayMap.getProductId())) {
                final Assay assay = new Assay();
                final Map<String, String> pageData = dataMap.get(productId);
                assay.setProductId(productId);
                assay.setAssayTitle(pageData.get(AssayMenuUtil.PAGE_TITLE));
                assay.setAssayUrl(pageData.get(AssayMenuUtil.PAGE_URL));
                assay.setLegend(pageData.get(AssayMenuUtil.PRODUCT_TYPE));
                legendsList.add(pageData.get(AssayMenuUtil.PRODUCT_TYPE));
                if (null != indicationlist && !indicationlist.isEmpty()) {
                    getIndicationMap(validAssay, indicationlist, assay);
                }
            }
        }
        for (final Entry<String, Object> assayCategory : validAssay.entrySet()) {
            final Map<String, Object> assayNodeMap = (Map<String, Object>) assayCategory.getValue();
            if (null != assayNodeMap) {
                final RelatedAssaysMap assayMap = new RelatedAssaysMap();
                assayMap.setCategoryName((String) assayNodeMap.get(AssayMenuUtil.PRODUCT_NODE_NAME));
                assayMap.setAssays((List<Assay>) assayNodeMap.get(AssayMenuUtil.PRODUCT_NODE_ASSAY_LIST));
                relatedResponseAssays.add(assayMap);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void getIndicationMap(final Map<String, Object> validAssay, final List<Indication> indicationlist,
            final Assay assay) {
        for (final Indication indication : indicationlist) {
            final String nodeId = indication.getNodeId();
            final String nodeName = indication.getNodeName();
            if (validAssay.containsKey(nodeId)) {
                final Map<String, Object> assayIdMap = (Map<String, Object>) validAssay.get(nodeId);
                final List<Assay> assayList = (List<Assay>) assayIdMap.get(AssayMenuUtil.PRODUCT_NODE_ASSAY_LIST);
                assayList.add(assay);
            } else {
                final List<Assay> assayList = new ArrayList<Assay>();
                assayList.add(assay);
                final Map<String, Object> assayIdMap = new HashMap<String, Object>();
                assayIdMap.put(AssayMenuUtil.PRODUCT_NODE_NAME, nodeName);
                assayIdMap.put(AssayMenuUtil.PRODUCT_NODE_ASSAY_LIST, assayList);
                validAssay.put(nodeId, assayIdMap);
            }

        }
    }
}
