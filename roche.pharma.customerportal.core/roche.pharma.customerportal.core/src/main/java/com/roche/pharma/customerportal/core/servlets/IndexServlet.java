package com.roche.pharma.customerportal.core.servlets;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.schedulers.SearchIndexScheduler;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is a servlet class to get the urls of activated and deactivated pages wtih respected selectors as crawl for full
 * indexing crawl_inr for incremental indexing crawl_del for deleted pages for incremental indexing.
 * @author Nitin Kumar
 */
@SlingServlet(resourceTypes = "sling/servlet/default", selectors = {
        "crawl", "crawl_inr", "crawl_del"
}, extensions = {
        "html"
})
public class IndexServlet extends SlingAllMethodsServlet {
    
    /**
     * This is a reference to service Search index scheduler to get url of pages as configured in configurations
     * template list
     */
    @Reference
    private SearchIndexScheduler searchIndexScheduler;
    
    /**
     * Serail Version UID
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(IndexServlet.class);
    /**
     * constant for index file name
     */
    private static final String FILE_NAME = "publishedIndex";
    
    /*
     * (non-Javadoc)
     * @see org.apache.sling.api.servlets.SlingSafeMethodsServlet#doGet(org.apache.sling.api.SlingHttpServletRequest,
     * org.apache.sling.api.SlingHttpServletResponse)
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        /*
         * This constant provides domain name for urls
         */
        final String DOMAIN = request.getRequestURL().substring(0,
                request.getRequestURL().indexOf(request.getRequestURI()));
        final String[] selectors = request.getRequestPathInfo().getSelectors();
        final String selector = selectors[0];
        if ("crawl".equals(selector)) {
            final String FINAL_URLS = searchIndexScheduler.getActivatedUrls(DOMAIN);
            LOG.debug("activated urls {}", FINAL_URLS);
            write(response, FINAL_URLS);
        } else {
            printPublishedUrl(request, response, DOMAIN, selector);
        }
    }
    
    /**
     * This method tries to get incrematal file containing activate and deactivated urls and prints them to the page so
     * that pages are crawled by search and promote
     * @param request sling request
     * @param response sling response
     * @param domain domain name for the website
     * @param selector selector from request
     * @throws IOException IO exception
     */
    private void printPublishedUrl(SlingHttpServletRequest request, SlingHttpServletResponse response, String domain,
            String selector) throws IOException {
        final Resource resource = request.getResource();
        final Page currentPage = CommonUtils.getCurrentPage(resource);
        if (null != currentPage) {
            final ValueMap map = getValueMap(resource, currentPage);
            if (null != map) {
                writeResponse(resource.getResourceResolver(), response, domain, selector, map);
            }
        }
    }
    
    /**
     * This is a recursive method which gives publishedIndex node from the current page and if it is not present with
     * current page then it goes to find with parent page recursively
     * @param resource sling resource
     * @param currentPage current page
     * @return ValueMap map for page properties
     */
    private ValueMap getValueMap(Resource resource, Page currentPage) {
        ValueMap map = null;
        if (currentPage.hasChild(FILE_NAME)) {
            final ResourceResolver resourceResolver = resource.getResourceResolver();
            final Resource contentResource = resourceResolver.getResource(currentPage.getPath() + "/" + FILE_NAME);
            if (null != contentResource) {
                map = contentResource.adaptTo(ValueMap.class);
            }
        } else {
            map = getValueMap(resource, currentPage.getParent());
        }
        return map;
    }
    
    /**
     * This method is used to call appropriate write method on the basis of selector. Selector crawl_inr will print
     * activated urls while selector crawl_del will print deactivated urls
     * @param resourceResolver sling resource resolver
     * @param response sling response
     * @param selector selector from request
     * @param map value map
     * @throws IOException IO exception
     */
    private void writeResponse(ResourceResolver resourceResolver, SlingHttpServletResponse response, String domain,
            String selector, ValueMap map) throws IOException {
        if ("crawl_inr".equals(selector)) {
            final Object activatedUrls = map.get("activatedUrls");
            if (null != activatedUrls) {
                final String FINAL_URLS = getFormattedUrls(resourceResolver, activatedUrls.toString(), domain);
                LOG.debug("activated urls {}", FINAL_URLS);
                write(response, FINAL_URLS);
            }
        } else if ("crawl_del".equals(selector)) {
            final Object deactivatedUrls = map.get("deactivatedUrls");
            if (null != deactivatedUrls) {
                final String FINAL_URLS = getFormattedUrls(resourceResolver, deactivatedUrls.toString(), domain);
                LOG.debug("deactivated urls {}", FINAL_URLS);
                write(response, FINAL_URLS);
            }
            
        }
    }
    
    /**
     * This method is used to return formatted urls It adds html extension
     * @param resourceResolver sling resource resolver
     * @param request sling request
     * @param activatedUrls list of activated urls
     * @return urls formatted with domain name and extension
     */
    private String getFormattedUrls(ResourceResolver resourceResolver, String activatedUrls, String domain) {
        
        final StringBuilder sb = new StringBuilder(100);
        final String[] urlsSpilitted = activatedUrls.split("\n");
        for (final String url : urlsSpilitted) {
            final String finalUrl = domain + resourceResolver.map(url) + ".html";
            sb.append("<a href=\"").append(finalUrl).append("\">").append(finalUrl).append("</a><br /> \n");
        }
        return sb.toString();
    }
    
    /**
     * This method is used to print urls and is called from various methods. It adds html markup to the urls and prints
     * them.
     * @param response sling response
     * @param urls list of urls
     * @throws IOException IO exception
     */
    private void write(SlingHttpServletResponse response, String urls) throws IOException {
        response.setContentType("text/html;charset=" + "UTF-8");
        response.setCharacterEncoding("UTF-8");
        final StringBuilder finalString = new StringBuilder(100);
        finalString.append("<!DOCTYPE html><HTML>\n<HEAD></HEAD>\n<BODY>\n").append(urls).append("\n</BODY></HTML>");
        response.getWriter().print(finalString);
    }
    
}
