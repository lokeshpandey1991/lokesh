package com.roche.pharma.customerportal.core.mock;

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Service;

import aQute.bnd.annotation.component.Component;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;

/**
 * @author sku154
 */
@Service(value = MockQueryBuilder.class)
@Component(immediate = true)
public class MockQueryBuilder implements QueryBuilder {
    
    Query query;
    
    public MockQueryBuilder(Query query) {
        this.query = query;
    }
    
    public void clearFacetCache() {
        // TODO Auto-generated method stub
        
    }
    
    public Query createQuery(Session arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Query createQuery(PredicateGroup arg0, Session arg1) {
        // TODO Auto-generated method stub
        return query;
    }
    
    public Query loadQuery(String arg0, Session arg1) throws RepositoryException, IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void storeQuery(Query arg0, String arg1, boolean arg2, Session arg3)
            throws RepositoryException, IOException {
        // TODO Auto-generated method stub
        
    }
    
}
