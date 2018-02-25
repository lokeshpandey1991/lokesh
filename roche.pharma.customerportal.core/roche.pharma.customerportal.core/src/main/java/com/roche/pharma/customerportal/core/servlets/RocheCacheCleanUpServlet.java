package com.roche.pharma.customerportal.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.google.gson.Gson;
import com.roche.pharma.customerportal.core.exceptions.BusinessExecutionException;
import com.roche.pharma.customerportal.core.services.ConfigurationService;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * @author asi130 Servlet called for contact us form submission
 */
@SlingServlet(resourceTypes = "roche/customerportal/components/ehCacheClean", selectors = {
        "relatedAssay", "assayMenuDispCache"
}, methods = {
    "GET"
}, extensions = {
    "html"
})
public class RocheCacheCleanUpServlet extends SlingAllMethodsServlet {
    
    /**
     * Serail Version UID
     */
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = LoggerFactory.getLogger(RocheCacheCleanUpServlet.class);
    
    private static final String CACHE_NODE_PATH = "/content/customerportal/cacheClean";
    
    /**
     * Resource resolver factory
     */
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    @Reference
    private Replicator replicator;
    
    @Reference
    ConfigurationService configurationService;
    
    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        final PrintWriter out = response.getWriter();
        final Gson gson = new Gson();
        final List<String> list = new ArrayList<String>();
        final String selector = request.getRequestPathInfo().getSelectorString();
        if ("relatedAssay".equalsIgnoreCase(selector)) {
            relatedCacheClean(selector, list);
        }
        if ("assayMenuDispCache".equalsIgnoreCase(selector)) {
            assayMenuDispCache(selector, list);
        }
        out.print(gson.toJson(list));
    }
    
    /**
     * @param selector
     * @return resposne
     * @throws PersistenceException
     */
    private List<String> relatedCacheClean(final String selector, final List<String> list) throws PersistenceException {
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "rocheUser");
        ResourceResolver resourceResolver = null;
        Session session = null;
        try {
            resourceResolver = CommonUtils.getResourceResolverFromSubService(resolverFactory, paramMap);
            session = resourceResolver.adaptTo(Session.class);
            final Resource res = resourceResolver.getResource(CACHE_NODE_PATH);
            final ModifiableValueMap props = res.adaptTo(ModifiableValueMap.class);
            props.put(selector, java.util.Calendar.getInstance());
            resourceResolver.commit();
            replicator.replicate(session, ReplicationActionType.ACTIVATE, CACHE_NODE_PATH);
            list.add("{{{EhCache Clean Request Started}}} for " + selector);
        } catch (final ReplicationException e) {
            LOG.error("ReplicationException in RocheCacheCleanUpServlet {}", e);
            list.add("{{{EhCache Clean Request Failed}}} for " + selector);
        } catch (final BusinessExecutionException e) {
            LOG.error("BusinessExecutionException in RocheCacheCleanUpServlet {}", e);
            list.add("{{{EhCache Clean Request Failed}}} for " + selector);
        } finally {
            if (session != null && session.isLive()) {
                session.logout();
            }
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
        return list;
    }
    
    private List<String> assayMenuDispCache(final String selector, final List<String> list) {
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "rocheUser");
        ResourceResolver resourceResolver = null;
        Session session = null;
        try {
            final List<String> instrumentPages = configurationService.getInstrumentPages();
            resourceResolver = CommonUtils.getResourceResolverFromSubService(resolverFactory, paramMap);
            session = resourceResolver.adaptTo(Session.class);
            if (instrumentPages != null && !instrumentPages.isEmpty()) {
                for (final String instrumentPage : instrumentPages) {
                    replicator.replicate(session, ReplicationActionType.ACTIVATE, instrumentPage);
                }
            }
            list.add("{{{Dispatcher Cache Clean Request Started}}} for " + selector);
        } catch (final ReplicationException e) {
            LOG.error("ReplicationException in RocheCacheCleanUpServlet {}", e);
            list.add("{{{Dispatcher Cache Clean Request Failed}}} for " + selector);
        } catch (final BusinessExecutionException e) {
            LOG.error("BusinessExecutionException in RocheCacheCleanUpServlet {}", e);
            list.add("{{{Dispatcher Cache Clean Request Failed}}} for " + selector);
        } finally {
            if (session != null && session.isLive()) {
                session.logout();
            }
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
        return list;
    }
    
}
