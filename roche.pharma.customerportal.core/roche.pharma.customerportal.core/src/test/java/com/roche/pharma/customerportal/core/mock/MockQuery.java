package com.roche.pharma.customerportal.core.mock;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.eval.PredicateEvaluator;
import com.day.cq.search.facets.Bucket;
import com.day.cq.search.result.SearchResult;

/**
 * @author sku154
 */
public class MockQuery implements Query {
    
    SearchResult searchResult;
    
    public MockQuery(SearchResult searchResult) {
        this.searchResult = searchResult;
    }
    
    public boolean getExcerpt() {
        // TODO Auto-generated method stub
        return false;
    }
    
    public long getHitsPerPage() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public PredicateGroup getPredicates() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public SearchResult getResult() {
        // TODO Auto-generated method stub
        return searchResult;
    }
    
    public long getStart() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public Query refine(Bucket arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void registerPredicateEvaluator(String arg0, PredicateEvaluator arg1) {
        // TODO Auto-generated method stub
        
    }
    
    public void setExcerpt(boolean arg0) {
        // TODO Auto-generated method stub
        
    }
    
    public void setHitsPerPage(long arg0) {
        // TODO Auto-generated method stub
        
    }
    
    public void setStart(long arg0) {
        // TODO Auto-generated method stub
        
    }
}
