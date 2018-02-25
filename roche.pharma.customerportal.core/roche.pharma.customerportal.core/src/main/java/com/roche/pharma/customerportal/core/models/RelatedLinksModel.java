package com.roche.pharma.customerportal.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.Predicate;
import com.day.cq.search.eval.JcrPropertyPredicateEvaluator;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.TagConstants;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.beans.PageLinkBean;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.services.SearchService;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * Related links model to get related links for corresponding page type and this will be sorted based on published date
 * or cq:lastModified date
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = RelatedLinksModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class RelatedLinksModel {
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/relatedLinks";

    /**
     * Resource for related link component
     */
    @Self
    private Resource resource;

    /**
     * search path for related links
     */
    @ValueMapValue
    private String rootPath;

    /**
     * Type of data static or dynamic
     */
    @ValueMapValue
    private String relatedLinksType;
    
    @ValueMapValue
    private String[] tags;
    
    @ValueMapValue
    private String[] relatedLinks;

    @ValueMapValue
    private String headline;

    /**
     * limit for related links
     */
    @ValueMapValue
    private String limit;
    
    /**
     * link behaviour same window or new window
     */
    @ValueMapValue
    private String linkBehaviour;

    /**
     * PageType to filter related links
     */
    private String pageType;
    
    /**
     * Current page of resource
     */
    private Page currentPage;
    
    @OSGiService
    private SearchService searchService;

    /**
     * Constant field static
     */
    private static final String RELATED_LINK_TYPE_STATIC = "static";
    
    private static final String PAGE_TYPE_PROPERTY = "pageType";
    
    private List<PageLinkBean> relatedList = new ArrayList<>();

    private static final Logger LOG = LoggerFactory.getLogger(RelatedLinksModel.class);

    /**
     * Constant for deafault limit if not authored
     */
    private static final String DEFAULT_LIMIT = "6";

    /**
     * Constant to create sublist
     */
    private static final int DEFAULT_SUBLIST_LIMIT = 1;

    /**
     * @return relatedList
     */
    public List<PageLinkBean> getRelatedList() {
        return new ArrayList<PageLinkBean>(relatedList);
    }

    /**
     * @return headline
     */
    public String getHeadline() {
        return headline;
    }

    @PostConstruct
    protected void postConstruct() {
        currentPage = CommonUtils.getCurrentPage(resource);
        final Page regionalLanguagePage = CommonUtils.getRegionalLanguagePage(currentPage);
        rootPath = rootPath == null ? regionalLanguagePage.getPath() : rootPath;
        if (tags == null && currentPage.getProperties().containsKey(TagConstants.PN_TAGS)) {
            tags = currentPage.getProperties().get(TagConstants.PN_TAGS, String[].class);
        }
        if (RELATED_LINK_TYPE_STATIC.equals(relatedLinksType)) {
            getMultifieldValue(relatedLinks);
        } else {
            pageType = CommonUtils.getPageTypeProperty(currentPage.getPath(), resource.getResourceResolver());
            getDynamicResults();
        }
    }

    /**
     * This will return the sort field based on page Default is last modified For news sorting will be done based on
     * published date if no published date is authored default last modified will be used
     * @param currentPage
     * @return sort field
     */
    private String getSortField(final Page currentPage) {
        if (currentPage != null && currentPage.getProperties().containsKey(RocheConstants.PUBLISHED_DATE)) {
            return RocheConstants.AT_THE_RATE + JcrConstants.JCR_CONTENT + RocheConstants.SLASH
                    + RocheConstants.PUBLISHED_DATE;
        } else {
            return RocheConstants.AT_THE_RATE + JcrConstants.JCR_CONTENT + RocheConstants.SLASH
                    + NameConstants.PN_PAGE_LAST_MOD;
        }
    }

    /**
     * This will fetch related links based on tags and page type current page if found in related links will be removed
     * if not found sublist will be returned
     */
    private void getDynamicResults() {
        if (tags != null) {
            final SearchResult result = searchService.getSearchResults(resource.getResourceResolver(),
                    createPredicateParamMap());
            final int lim = Integer.parseInt(limit);
            if (result != null) {
                getResultsFromHits(result.getHits());
            }
            if (relatedList.size() > (lim - 1)) {
                relatedList = relatedList.subList(0, (lim - DEFAULT_SUBLIST_LIMIT));
            }
        }
    }
    
    /**
     * This will fetch pages from search results each hit returned will be converted to page link bean and added to list
     * related list
     * @param hits
     */
    private void getResultsFromHits(final List<Hit> hits) {
        if (hits != null && !hits.isEmpty()) {
            for (final Hit hit : hits) {
                getPageFromHit(hit);
            }
        }
    }

    /**
     * This will fetch the corresponding page from search hit and populate the page link bean and add it to the related
     * list current page is removed from the list and list size is adjusted according to the limit authored in dialog if
     * not found default limit used is 6
     * @param hit
     */
    private void getPageFromHit(final Hit hit) {
        try {
            if (hit.getResource() != null && hit.getResource().adaptTo(Page.class) != null) {
                final Page page = hit.getResource().adaptTo(Page.class);
                if (page != null) {
                    final PageLinkBean pageLinkBean = new PageLinkBean();
                    pageLinkBean.setPageName(page.getTitle());
                    pageLinkBean.setPagePath(page.getPath());
                    pageLinkBean.setLinkBehaviour(linkBehaviour);
                    addToList(pageLinkBean);
                }
            }
        } catch (final RepositoryException e) {
            LOG.error("Error fetching results for related links{}", e);
        }
    }
    
    /**
     * This will remove current page from related links
     * @param pageLinkBean
     */
    private void addToList(final PageLinkBean pageLinkBean) {
        if (currentPage != null && !currentPage.getPath().equals(pageLinkBean.getPagePath())) {
            relatedList.add(pageLinkBean);
        }
    }
    
    /**
     * This will create predicate map for searching related links search is on root path and type is cq:Page search is
     * done on tags with OR condition page with hide in search property set will not be searched search is done on page
     * type property example news,events search limit is 6 for related links sorting is done published date and default
     * is cq:lastModified
     * @return map
     */
    private Map<String, String> createPredicateParamMap() {
        if (limit == null) {
            limit = DEFAULT_LIMIT;

        } else {
            try {
                limit = Integer.toString((Integer.parseInt(limit) + 1));
            } catch (final NumberFormatException e) {
                LOG.error("Error parsing limit in relatedLinks default 6 will be used {}", e);
                limit = DEFAULT_LIMIT;
            }
        }
        final Map<String, String> predicateParamMap = new HashMap<>();
        predicateParamMap.put(RocheConstants.SEARCH_PATH, rootPath);
        predicateParamMap.put(RocheConstants.SEARCH_TYPE, NameConstants.NT_PAGE);
        predicateParamMap.put("1" + RocheConstants.UNDERSCORE + JcrPropertyPredicateEvaluator.PROPERTY,
                JcrConstants.JCR_CONTENT + RocheConstants.SLASH + TagConstants.PN_TAGS);
        int i = 1;
        for (final String tag : tags) {
            predicateParamMap.put("1_" + JcrPropertyPredicateEvaluator.PROPERTY + RocheConstants.DOT + (i)
                    + RocheConstants.UNDERSCORE + JcrPropertyPredicateEvaluator.VALUE, tag);
            i = i + 1;
        }
        predicateParamMap.put("2" + RocheConstants.UNDERSCORE + JcrPropertyPredicateEvaluator.PROPERTY,
                JcrConstants.JCR_CONTENT + RocheConstants.SLASH + RocheConstants.HIDE_IN_SEARCH);
        predicateParamMap.put("2" + RocheConstants.UNDERSCORE + JcrPropertyPredicateEvaluator.PROPERTY
                + RocheConstants.DOT + JcrPropertyPredicateEvaluator.OPERATION, JcrPropertyPredicateEvaluator.OP_NOT);
        if (pageType != null) {
            predicateParamMap.put("3" + RocheConstants.UNDERSCORE + JcrPropertyPredicateEvaluator.PROPERTY,
                    JcrConstants.JCR_CONTENT + RocheConstants.SLASH + PAGE_TYPE_PROPERTY);
            predicateParamMap.put("3" + RocheConstants.UNDERSCORE + JcrPropertyPredicateEvaluator.PROPERTY
                    + RocheConstants.DOT + JcrPropertyPredicateEvaluator.VALUE, pageType);
        }
        predicateParamMap.put(Predicate.ORDER_BY, getSortField(currentPage));
        predicateParamMap.put(Predicate.ORDER_BY + RocheConstants.DOT + Predicate.PARAM_SORT,
                Predicate.SORT_DESCENDING);
        predicateParamMap.put("p" + RocheConstants.DOT + Predicate.PARAM_LIMIT, limit);
        return predicateParamMap;
    }
    
    /**
     * This will create related links when author provides related links in dialog authorable fields are link name and
     * link path
     * @param jsonArr
     */
    private void getMultifieldValue(final String[] jsonArr) {
        if (jsonArr != null) {
            for (final String json : jsonArr) {
                final PageLinkBean pageLinkBean = CommonUtils.getMultifield(json, PageLinkBean.class);
                pageLinkBean.setLinkBehaviour(linkBehaviour);
                relatedList.add(pageLinkBean);
            }
        }
    }
    
}
