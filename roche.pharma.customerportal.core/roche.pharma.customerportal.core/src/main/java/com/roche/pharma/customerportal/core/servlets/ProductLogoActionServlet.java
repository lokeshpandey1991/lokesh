package com.roche.pharma.customerportal.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.roche.pharma.customerportal.core.exceptions.BusinessExecutionException;

/**
 * The Class ProductLogoActionServlet.
 * @author asagga
 */
@SlingServlet(metatype = true, description = "customerportal Product Logo Servlet", paths = {
        "/bin/Customerportal/productlogoupdateservlet"
}, extensions = {
        "html"
}, methods = {
        "GET"
})

@Properties({
        @org.apache.felix.scr.annotations.Property(name = "service.description", value = "Product Logo Action Servlet"),
        @org.apache.felix.scr.annotations.Property(name = "customerportal.productLogo.query.path", label = "Query path",
                description = "Set the Product Logo Query Path", value = "/content/customerportal")

})

public class ProductLogoActionServlet extends SlingAllMethodsServlet {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant ACTIVATED. */
    private static final String ACTIVATED = "PUBLISHED";
    
    /** The Constant DEACTIVATED. */
    private static final String DEACTIVATED = "UNPUBLISHED";
    
    /** The query path. */
    private String queryPath = "";

    /** The logo name. */
    private String logoName = "";
    
    /** The logo description. */
    private String logoDescription = "";
    
    /** The logo reference. */
    private String logoReference = "";
    
    /** The logo status. */
    private String logoStatus = "";

    /** The action. */
    private String action = "";
    
    /** The property value. */
    private String propertyValue = "";
    
    /** The query builder. */
    @Reference
    private QueryBuilder queryBuilder;

    /**
     * The LOG variable is used to log the info and error logs fro vanity url servlet class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ProductLogoActionServlet.class);

    /**
     * This method is called on servlet component activation . Assigns Value to the vanityQueryPath variable.
     * @param ctx the ctx
     */
    @Activate
    public void activate(ComponentContext ctx) {
        @SuppressWarnings("unchecked")
        final Dictionary<String, Object> props = ctx.getProperties();
        queryPath = props.get("customerportal.productLogo.query.path").toString();
    }

    /*
     * (non-Javadoc)
     * @see org.apache.sling.api.servlets.SlingSafeMethodsServlet#doGet(org.apache.sling.api.SlingHttpServletRequest,
     * org.apache.sling.api.SlingHttpServletResponse)
     */

    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        getRequestParameters(request);
        setPropertyValue();

        final ResourceResolver resourceResolver = request.getResourceResolver();
        
        final Map<String, String> predicateMap = new HashMap<>();
        predicateMap.put("path", queryPath);
        predicateMap.put("property", "productLogoSelect");
        predicateMap.put("property.value", logoName + "%");
        predicateMap.put("property.operation", "like");
        
        try {
            final Session session = resourceResolver.adaptTo(Session.class);
            String htmlResponse = "";
            if (null != session) {
                final Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);
                final SearchResult searchResult = query.getResult();
                
                final List<Hit> hits = searchResult.getHits();
                
                if (StringUtils.equalsIgnoreCase(action, "update")) {
                    htmlResponse = updateProductLogo(hits, session);
                }
                
                if (StringUtils.equalsIgnoreCase(action, "page")) {
                    htmlResponse = getProductLogoPages(hits);
                }
                
            }
            response.setContentType("text/html");
            final PrintWriter out = response.getWriter();

            out.println(htmlResponse);

            out.close();

        } catch (BusinessExecutionException businessExecutionException) {
            LOG.error("Servlet ProductLogo BusinessExecutionException ", businessExecutionException);
        } catch (RepositoryException repositoryException) {
            LOG.error("Servlet ProductLogo RepositoryException ", repositoryException);
        }

    }
    
    /**
     * Gets the request parameters.
     * @param request the request
     * @return the request parameters
     */
    private void getRequestParameters(SlingHttpServletRequest request) {

        logoName = request.getParameter("logoName");
        logoDescription = request.getParameter("logoDescription");
        logoReference = request.getParameter("logoReference");
        logoStatus = request.getParameter("logoStatus");
        action = request.getParameter("action");

    }
    
    /**
     * Update product logo.
     * @param iter the iter
     * @param session the session
     * @return the string
     * @throws RepositoryException the repository exception
     */
    private String updateProductLogo(List<Hit> hits, Session session) throws RepositoryException {
        /** The deactivateproductlogo. */
        final String deactivateProductLogo = "deactivateProductLogo";
        for (final Hit hit : hits) {
            final Node node = hit.getNode();
            if (StringUtils.equalsIgnoreCase(logoStatus, "true")) {
                final Property property = node.getProperty("productLogoSelect");
                property.setValue(propertyValue);

                if (node.hasProperty(deactivateProductLogo)) {
                    node.getProperty(deactivateProductLogo).remove();
                }
            } else {
                if (!node.hasProperty(deactivateProductLogo)) {
                    node.setProperty(deactivateProductLogo, "true");
                }
            }

        }

        session.save();

        return "Successfull Updated!!!!";

    }
    
    /**
     * Gets the product logo pages.
     * @param iter the iter
     * @return the product logo pages
     * @throws RepositoryException the repository exception
     */
    private String getProductLogoPages(List<Hit> hits) throws RepositoryException {
        final StringBuilder response = new StringBuilder();
        for (final Hit hit : hits) {
            final Node node = hit.getNode();
            final String nodeStatus = getNodeStatus(node);
            final String nodePath = node.getParent().getPath();
            response.append("<li class='list-group-item'>").append("<span class='badge'>").append(nodeStatus)
                    .append("</span><br/>").append(nodePath).append("</li>");
            
        }
        
        return generateHtmlResponse(response.toString());
    }
    
    /**
     * Generate html response.
     * @param response the response
     * @return the string
     */
    private String generateHtmlResponse(String response) {

        return "<div><ul class='list-group'>" + response + "</ul></div>";
        
    }
    
    /**
     * Sets the property value.
     */
    private void setPropertyValue() {
        final String SEPARATOR = "'''";
        propertyValue = logoName + SEPARATOR + logoDescription + SEPARATOR + logoReference;
    }

    /**
     * Gets the node status.
     * @param node the node
     * @return the node status
     * @throws RepositoryException the repository exception
     */
    private String getNodeStatus(Node node) throws RepositoryException {
        if (node.hasProperty("cq:lastReplicationAction")
                && StringUtils.equalsIgnoreCase(node.getProperty("cq:lastReplicationAction").getString(), "Activate")) {
            return ACTIVATED;
        }
        return DEACTIVATED;
    }
}
