package com.roche.pharma.customerportal.core.mock;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.Resource;

import com.day.cq.search.facets.Facet;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.ResultPage;
import com.day.cq.search.result.SearchResult;

public class MockSearchResult implements SearchResult {

    List<Hit> hits;

    public MockSearchResult(final List<Hit> hits) {
        this.hits = hits;
    }

    @Override
    public String getExecutionTime() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getExecutionTimeMillis() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Map<String, Facet> getFacets() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFilteringPredicates() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Hit> getHits() {
        // TODO Auto-generated method stub
        return hits;
    }

    @Override
    public long getHitsPerPage() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ResultPage getNextPage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<Node> getNodes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultPage getPreviousPage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getQueryStatement() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<Resource> getResources() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ResultPage> getResultPages() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getStartIndex() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getTotalMatches() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public boolean hasMore() {
        // TODO Auto-generated method stub
        return false;
    }

}
