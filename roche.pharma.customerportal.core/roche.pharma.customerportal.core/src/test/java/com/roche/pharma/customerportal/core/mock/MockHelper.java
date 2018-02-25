package com.roche.pharma.customerportal.core.mock;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;

import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

import io.wcm.testing.mock.aem.junit.AemContext;

public class MockHelper {
    
    public static void loadQuery(AemContext context, List<String> hitPaths) {
        
        List<Hit> hits = new ArrayList<Hit>();
        for (String hitPath : hitPaths) {
            hits.add(new MockHits(context.resourceResolver().getResource(hitPath), hitPath));
        }
        
        SearchResult searchResult = new MockSearchResult(hits);
        Query query = new MockQuery(searchResult);
        context.registerService(QueryBuilder.class, new MockQueryBuilder(query));
    }
    
    public static MockWorkItem getWorkFlowItem(String payload, MetaDataMap map) {
        WorkflowData workflowData = new MockWorkflowData(payload, map);
        return new MockWorkItem(workflowData);
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends SlingAllMethodsServlet> T getServlet(AemContext context, Class<T> servletName) {
        Servlet servlet;
        try {
            servlet = (Servlet) Class.forName(servletName.getName()).newInstance();
            context.registerInjectActivateService(servlet);
            for (Servlet servletService : context.getServices(Servlet.class, null)) {
                if (servletName.getName().equals(servletService.getClass().getName())) {
                    return (T) servletService;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
