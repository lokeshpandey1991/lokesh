package com.roche.pharma.customerportal.core.services;

import java.util.Map;
import org.apache.sling.api.resource.ResourceResolver;
import com.day.cq.search.result.SearchResult;

/**
 * Search result service interface.
 */
@FunctionalInterface
public interface SearchService {

    /**
     * Gets the search results.
     *
     * @param resourceResolver the resource resolver
     * @param predicateParameterMap the predicate parameter map
     * @return the search results
     */
    public SearchResult getSearchResults(ResourceResolver resourceResolver, Map<String, String> predicateParameterMap);

}
