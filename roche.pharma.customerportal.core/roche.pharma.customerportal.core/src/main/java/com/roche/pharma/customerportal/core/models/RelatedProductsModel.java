package com.roche.pharma.customerportal.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.eval.JcrPropertyPredicateEvaluator;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.TagConstants;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.beans.RelatedProductBean;
import com.roche.pharma.customerportal.core.beans.TagBean;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.services.ConfigurationService;
import com.roche.pharma.customerportal.core.services.SearchService;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is a model class for the related products component to read authored content from the component dialog which is
 * a list of pages and a list of tags. This class uses this list of authored related product pages and tags to search
 * products related to the current product and gives a list of product detail pages. This list id combined, ordered and
 * with no duplicate page.
 * @author Nitin Kumar
 */

@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = RelatedProductsModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class RelatedProductsModel {
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/relatedProducts";
    
    /**
     * Sling Resource used as current resource
     */
    @Self
    private Resource resource;
    
    /**
     * Current page
     */
    private Page currentPage;
    
    /**
     * Configuration service to get root path
     */
    @OSGiService
    private static ConfigurationService configurationService;
    
    /**
     * AEM query builder
     */
    @OSGiService
    private static QueryBuilder queryBuilder;
    
    /**
     * Search Service reference
     */
    @OSGiService
    private static SearchService searchService;
    /**
     * Logger for this class to print logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(RelatedProductsModel.class);
    
    /**
     * This field is from dialog and contains list of product detail pages
     */
    @ValueMapValue
    private String[] relatedProducts;
    
    /**
     * This field is from dialog and contains list of tags
     */
    @ValueMapValue
    private String[] tags;
    
    /**
     * This field is from dialog and contains section heading for related products section
     */
    @ValueMapValue
    private String sectionHeading;
    
    /**
     * List of pdp beans returned by post construct method
     */
    private final List<RelatedProductBean> pdpBeanList = new ArrayList<>();
    
    /** The component name. */
    private String componentName;
    
    /**
     * Post construct method. This is root method for this model class which checks for the tags from the dialog. If
     * tags are missing from the related component the it uses tags from page properties. And finally this method calls
     * another method so that list of product detail pages can be returned to the sighlty component.
     */
    @PostConstruct
    protected void postConstruct() {
        currentPage = CommonUtils.getCurrentPage(resource);
        if (tags == null && currentPage.getProperties().containsKey(TagConstants.PN_TAGS)) {
            tags = currentPage.getProperties().get(TagConstants.PN_TAGS, String[].class);
        }
        getDynamicResults();
        componentName = CommonUtils.getComponentName(resource);
    }
    
    /**
     * This will fetch related product beans based on authored tags and product detail pages in related products
     * component.
     */
    private void getDynamicResults() {
        if (null == tags) {
            getResultsFromHits(getPdpUrls());
        } else {
            final SearchResult result = searchService.getSearchResults(resource.getResourceResolver(),
                    createPredicateParamMap());
            if (result != null) {
                final List<String> nonDuplicateList = getNonDuplicateListOfPages(getPdpUrls(), result.getHits());
                getResultsFromHits(nonDuplicateList);
            }
        }
    }
    
    /**
     * This is a test method for Search and promote nested facets It will be deleted after design is finalized and
     * complete.
     * @return list of product detail page URLs
     */
    public List<String> getPdpUrls() {
        final List<String> pageList = new ArrayList<>();
        RelatedProductBean bean;
        if (null != relatedProducts) {
            for (final String pageLink : relatedProducts) {
                bean = CommonUtils.getGsonInstance().fromJson(pageLink, RelatedProductBean.class);
                pageList.add(bean.getPagePath());
            }
        }
        return pageList;
    }
    
    /**
     * This method is used to remove duplicate values from the list of pages.
     * @param list list of related products
     * @param hits hits from tag based search results
     * @return list of non duplicate pages
     */
    private List<String> getNonDuplicateListOfPages(List<String> pdpList, List<Hit> hits) {
        final Set<String> nonDuplicateListSet = new LinkedHashSet<>(pdpList);
        for (final Hit hit : hits) {
            try {
                final Page page = hit.getResource().adaptTo(Page.class);
                if (null != page) {
                    nonDuplicateListSet.add(page.getPath());
                }
            } catch (final RepositoryException e) {
                LOG.error("Error fetching results for related links{}", e);
            }
        }
        return new ArrayList<>(nonDuplicateListSet);
    }
    
    /**
     * This will fetch pages from search results These will be converted to page link bean. In this method related
     * product bean is used and only page path property is required.
     * @param nonDuplicateList list of non duplicate pages
     */
    private void getResultsFromHits(final List<String> nonDuplicateList) {
        final ResourceResolver resourceResolver = resource.getResourceResolver();
        for (final String pagePath : nonDuplicateList) {
            final Resource res = resourceResolver.getResource(pagePath);
            if (null != res) {
                final Page page = res.adaptTo(Page.class);
                if (null != page) {
                    getProductDetails(page);
                }
            }
        }
    }
    
    /**
     * This method is to get product detail page's product infromation like product name and image and to add these
     * information in the bean list which is used to return the listof related products
     * @param page cq page
     */
    private void getProductDetails(Page page) {
        final Resource productResource = page.getContentResource().getChild("product");
        final Resource parentResource = page.getContentResource();
        if (null != productResource) {
            final ValueMap map = productResource.getValueMap();
            final RelatedProductBean relatedProductBean = new RelatedProductBean();
            if (map.containsKey("productName")) {
                relatedProductBean.setProductTitle(map.get("productName").toString());
            }
            if (map.containsKey("fileReference")) {
                relatedProductBean.setProductImage(map.get("fileReference").toString());
            }
            if (map.containsKey("altText")) {
                relatedProductBean.setAltText(map.get("altText").toString());
            }
            relatedProductBean.setPagePath(
                    CommonUtils.getPagepathWithExtension(productResource.getResourceResolver().map(page.getPath())));
            relatedProductBean.setBusinessArea(getBusinessArea(page));
            final ValueMap parentMap = parentResource.getValueMap();
            if (parentMap.containsKey("productId")) {
                relatedProductBean.setProductId(parentMap.get("productId").toString());
            }
            addToList(relatedProductBean);
        }
    }
    
    /**
     * This method returns business area value for the tags used for product detail page form page properties. This
     * trims the tags starting from last slash and gives that substring as a business area
     * @param page cq page
     * @return
     */
    private String getBusinessArea(Page page) {
        String businessArea = "";
        final ValueMap map = page.getContentResource().getValueMap();
        final Resource res = page.adaptTo(Resource.class);
        if (null != res && map.containsKey(RocheConstants.CQTAGS)) {
            final String[] businessTags = (String[]) map.get(RocheConstants.CQTAGS);
            for (final String businessTag : businessTags) {
                if (businessTag.contains(RocheConstants.BUSINESS_AREA_TAG_PREFIX)) {
                    final TagBean tagBean = CommonUtils.getTagBean(businessTag, CommonUtils.getPageLocale(page), res);
                    businessArea = tagBean.getLocalTitle();
                }
            }
        }
        return businessArea;
    }
    
    /**
     * This method is the last method called from this model class and this method assigns benas passed to this method
     * as parameter to the pdpBeanList which is returned to component sightly component.
     * @param relatedProductBean related product bean
     */
    private void addToList(final RelatedProductBean relatedProductBean) {
        if (currentPage != null && !currentPage.getPath().equals(relatedProductBean.getPagePath())) {
            pdpBeanList.add(relatedProductBean);
        }
    }
    
    /**
     * This will create predicate map for searching related products on root path and type is cq:Page, search is done on
     * page type property search limit is 10 for related links and sorting is done pulished date and default is
     * cq:lastModified
     * @return map returns map of query parameters
     */
    private Map<String, String> createPredicateParamMap() {
        String path = null;
        final LanguageConfigurationsModel languageConfigurationsModel = CommonUtils.getlanguageConfigurations(resource);
        if (null != languageConfigurationsModel) {
            path = languageConfigurationsModel.getRelatedProductsSearchPagePath();
        }
        if (null == path) {
            path = configurationService.getRootPath();
        }
        final Map<String, String> predicateParamMap = new HashMap<>();
        predicateParamMap.put(RocheConstants.SEARCH_PATH, path);
        predicateParamMap.put(RocheConstants.SEARCH_TYPE, NameConstants.NT_PAGE);
        predicateParamMap.put("p.limit", "-1");
        predicateParamMap.put("1" + "_" + JcrPropertyPredicateEvaluator.PROPERTY,
                JcrConstants.JCR_CONTENT + "/" + TagConstants.PN_TAGS);
        int i = 1;
        for (final String tag : tags) {
            predicateParamMap.put("1_" + JcrPropertyPredicateEvaluator.PROPERTY + RocheConstants.DOT + (i) + "_"
                    + JcrPropertyPredicateEvaluator.VALUE, tag);
            i++;
        }
        return predicateParamMap;
    }
    
    /**
     * This is a getter method for pdpBeanList
     * @return the pdpBeanList
     */
    public List<RelatedProductBean> getPdpBeanList() {
        return new ArrayList<>(pdpBeanList);
    }
    
    /**
     * This is a getter method for sectionHeading
     * @return the sectionHeading
     */
    public String getSectionHeading() {
        return sectionHeading;
    }
    
    /**
     * This is a getter method for component name
     * @return
     */
    public String getComponentName() {
        return componentName;
    }
}
