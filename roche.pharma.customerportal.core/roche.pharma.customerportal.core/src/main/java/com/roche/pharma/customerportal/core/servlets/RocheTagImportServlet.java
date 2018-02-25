package com.roche.pharma.customerportal.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roche.pharma.customerportal.core.beans.DTLTag;
import com.roche.pharma.customerportal.core.beans.Data;
import com.roche.pharma.customerportal.core.beans.Title;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.framework.ServiceResponse;
import com.roche.pharma.customerportal.core.services.RocheTagsImportService;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * @author Nitin Kumar
 */
@SlingServlet(paths = "/bin/Customerportal/importTags")
public class RocheTagImportServlet extends SlingAllMethodsServlet {
    
    /**
     * Serail Version UID
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Roche Tag import service
     */
    @Reference
    private transient RocheTagsImportService rocheTagsImportService;
    
    /**
     * constant for roche system user
     */
    
    private static final String ROCHEUSER = "rocheUser";
    
    /**
     * Resource resolver factory
     */
    @Reference
    private transient ResourceResolverFactory resolverFactory;
    
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(RocheTagImportServlet.class);
    
    /**
     * Number of cycles constant
     */
    public static final int NUMBER_OF_CYCLES = 500;
    
    /*
     * (non-Javadoc)
     * @see org.apache.sling.api.servlets.SlingSafeMethodsServlet#doGet(org.apache.sling.api.SlingHttpServletRequest,
     * org.apache.sling.api.SlingHttpServletResponse)
     */
    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, ROCHEUSER);
        ResourceResolver resourceResolver = null;
        Session jcrSession = null;
        final PrintWriter out = response.getWriter();
        try {
            resourceResolver = CommonUtils.getResourceResolverFromSubService(resolverFactory, paramMap);
            jcrSession = resourceResolver.adaptTo(Session.class);
            final ServiceResponse<Data> dtlData = rocheTagsImportService.getAllTags();
            final List<DTLTag> dtlTags = dtlData.getData().getDtlTags();
            final Comparator<DTLTag> sortTagsByNodelevel = (DTLTag tag1,
                    DTLTag tag2) -> Integer.parseInt(tag1.getNodeLevel()) - Integer.parseInt(tag2.getNodeLevel());
            Collections.sort(dtlTags, sortTagsByNodelevel);
            createRocheTags(resourceResolver, jcrSession, dtlTags, out);
            
            if (jcrSession != null && jcrSession.isLive()) {
                jcrSession.refresh(true);
                jcrSession.save();
            }
            
            out.println("\n\r Tag creation task is completed !");
        } catch (WebserviceException | RepositoryException e) {
            LOG.error("Exception in doGet of RocheTagImportServlet {}", e);
            out.println("\n\r WebserviceException in creating tags!");
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
            if (jcrSession != null && jcrSession.isLive()) {
                jcrSession.logout();
            }
        }
    }
    
    /**
     * This method calls method which is used to create tags for roche
     * @param resourceResolver sling resource resolver
     * @param jcrSession jcr session
     * @param dtlTags roche dtl tag list
     * @param out print writer
     */
    private void createRocheTags(ResourceResolver resourceResolver, Session jcrSession, List<DTLTag> dtlTags,
            PrintWriter out) {
        final ListIterator<DTLTag> itr = dtlTags.listIterator();
        final Resource resource = resourceResolver.getResource(RocheConstants.TAG_PREFIX);
        out.println("Tag import started /n");
        if (null == resource) {
            createBasicStructureForTags(resourceResolver, out);
        }
        int counter = 0;
        while (itr.hasNext()) {
            final DTLTag dtlTag = itr.next();
            createTag(resourceResolver, dtlTag, out);
            counter++;
            if (counter > NUMBER_OF_CYCLES && jcrSession != null && jcrSession.isLive()) {
                try {
                    jcrSession.refresh(true);
                    jcrSession.save();
                } catch (RepositoryException e) {
                    LOG.error("Exception in createRocheTags of RocheTagImportServlet {}", e);
                    out.println("\n\r WebserviceException in creating tags!");
                }
                
            }
        }
        
    }
    
    /**
     * This method create a basic structure id not present
     * @param resourceResolver sling resource resolver
     * @param out print writer
     */
    private void createBasicStructureForTags(ResourceResolver resourceResolver, PrintWriter out) {
        final Resource parentResource = resourceResolver.getResource("/etc/tags");
        if (null != parentResource) {
            final Node parentNode = parentResource.adaptTo(Node.class);
            if (null != parentNode) {
                try {
                    parentNode.addNode("customerportal", "cq:Tag");
                    final Node node = parentNode.getNode("customerportal");
                    node.setProperty(RocheConstants.SLING_RESOURCE, RocheConstants.TAG_RESOURCE_TYPE);
                    node.setProperty(RocheConstants.TITLE, "customerportal");
                    out.println("\n\r Tag created : /etc/tags/customerportal");
                    
                    node.addNode("pim");
                    final Node pimNode = node.getNode("pim");
                    pimNode.setProperty(RocheConstants.SLING_RESOURCE, RocheConstants.TAG_RESOURCE_TYPE);
                    pimNode.setProperty(RocheConstants.TITLE, "PIM Tags");
                    out.println("\n\r Tag created : /etc/tags/customerportal/pim");
                    
                    pimNode.addNode("product");
                    final Node productNode = pimNode.getNode("product");
                    productNode.setProperty(RocheConstants.SLING_RESOURCE, RocheConstants.TAG_RESOURCE_TYPE);
                    productNode.setProperty(RocheConstants.TITLE, "Product");
                    out.println("\n\r Tag created : /etc/tags/customerportal/pim/product");
                    
                } catch (ValueFormatException e) {
                    LOG.error("ValueFormatException in creating basic tag structure", e);
                } catch (VersionException e) {
                    LOG.error("VersionException in creating basic tag structure", e);
                } catch (LockException e) {
                    LOG.error("LockException in creating basic tag structure", e);
                } catch (ConstraintViolationException e) {
                    LOG.error("ConstraintViolationException in creating basic tag structure", e);
                } catch (RepositoryException e) {
                    LOG.error("RepositoryException in creating basic tag structure", e);
                }
                LOG.info("Parent structure for tags created.");
            }
        }
        
    }
    
    /**
     * This method is used to create roche tag under path /etc/tags/customerportal/pim/product
     * @param resourceResolver sling resource resolver
     * @param dtlTag tag DTO
     * @param out print writer
     */
    private void createTag(ResourceResolver resourceResolver, DTLTag dtlTag, PrintWriter out) {
        final String parentPath = RocheConstants.TAG_PREFIX + StringUtils.substringBeforeLast(dtlTag.getPath(), "/");
        final Resource parentResource = resourceResolver.getResource(parentPath);
        try {
            if (null == parentResource) {
                LOG.info("Tag not created for path {} parent tag was not found", parentPath);
                out.println("\n\r Tag not created for the path " + parentPath + " parent tag was not found");
            } else {
                final Node node = parentResource.adaptTo(Node.class);
                if (null != node) {
                    node.addNode(dtlTag.getNodeId());
                    final Node newNode = node.getNode(dtlTag.getNodeId());
                    newNode.setProperty(RocheConstants.SLING_RESOURCE, RocheConstants.TAG_RESOURCE_TYPE);
                    setTitles(newNode, dtlTag.getTitles());
                }
                LOG.info("Tag created {}", RocheConstants.TAG_PREFIX + dtlTag.getPath());
                out.println("\n\r Tag created : " + RocheConstants.TAG_PREFIX + dtlTag.getPath());
            }
            
        } catch (RepositoryException e) {
            LOG.error("RepositoryException in creating tag {} {}", RocheConstants.TAG_PREFIX + dtlTag.getPath(), e);
        }
    }
    
    /**
     * This method sets title in a tag for different languages
     * @param node jcr node
     * @param titles list of title
     */
    private void setTitles(Node node, List<Title> titles) {
        try {
            for (final Title title : titles) {
                if (("en").equalsIgnoreCase(title.getLanguage())) {
                    node.setProperty(RocheConstants.TITLE, title.getValue());
                } else {
                    node.setProperty(RocheConstants.TITLE + RocheConstants.DOT + title.getLanguage(), title.getValue());
                }
            }
        } catch (RepositoryException e) {
            LOG.error("RepositoryException in setTitles {}", e);
        }
    }
}
