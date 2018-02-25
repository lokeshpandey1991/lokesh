package com.roche.pharma.customerportal.core.jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.exceptions.BusinessExecutionException;
import com.roche.pharma.customerportal.core.services.ConfigurationService;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

@Component(immediate = true, metatype = true, label = "Roche Product Importer")
@Service(value = JobConsumer.class)
@Property(name = JobConsumer.PROPERTY_TOPICS, value = "com/roche/customerportal/productImport")
public class RocheProductImporter implements JobConsumer {
    
    private static final String PRODUCT_TAP = "/etc/commerce/products/customerportal/TAP";
    
    private static final String PRODUCT_TAP_PREFIX = "TAP_";
    
    private static final String PRODUCT_INS_PREFIX = "INS_";
    
    private static final String PRODUCT_INS = "/etc/commerce/products/customerportal/INS";
    
    private static final Logger LOG = LoggerFactory.getLogger(RocheProductImporter.class);
    
    @Reference
    private QueryBuilder builder;
    
    @Reference
    Replicator replicator;
    
    @Reference
    ConfigurationService configurationService;
    
    /**
     * Resource resolver factory
     */
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    /*
     * (non-Javadoc)
     * @see org.apache.sling.event.jobs.consumer.JobConsumer#process(org.apache.sling.event.jobs.Job)
     */
    @Override
    public JobResult process(final Job job) {
        final String path = (String) job.getProperty("path");
        final String eventType = (String) job.getProperty("event");
        if (StringUtils.isNotBlank(path)) {
            final Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put(ResourceResolverFactory.SUBSERVICE, "rocheUser");
            ResourceResolver resourceResolver = null;
            Session jcrSession = null;
            try {
                resourceResolver = CommonUtils.getResourceResolverFromSubService(resolverFactory, paramMap);
                jcrSession = resourceResolver.adaptTo(Session.class);
                if (path.startsWith(PRODUCT_INS)) {
                    handleInstrumentEvent(resourceResolver, jcrSession, path, eventType);
                }
                if (path.startsWith(PRODUCT_TAP)) {
                    handleTestApplicationEvent(resourceResolver, jcrSession, path, eventType);
                }
            } catch (final BusinessExecutionException e) {
                LOG.error("BusinessExecutionException in updateIndex method {}", e);
            } finally {
                if (jcrSession != null && jcrSession.isLive()) {
                    jcrSession.logout();
                }
                if (resourceResolver != null && resourceResolver.isLive()) {
                    resourceResolver.close();
                }
            }
        } else {
            return JobResult.CANCEL;
        }
        return JobResult.OK;
    }
    
    /**
     * @param resourceResolver
     * @param session
     * @param path
     * @param eventType
     */
    public void handleInstrumentEvent(final ResourceResolver resourceResolver, final Session session,
            final String path, final String eventType) {
        
        final String productId = path.substring(path.lastIndexOf("/") + 1);
        try {
            if (!productId.startsWith(PRODUCT_INS_PREFIX)) {
                return;
            }
            if ("add".equalsIgnoreCase(eventType)) {
                replicator.replicate(session, ReplicationActionType.ACTIVATE, path);
            }
            if ("update".equalsIgnoreCase(eventType) && configurationService.isActivatePDPOnDTLUpdate()) {
                replicator.replicate(session, ReplicationActionType.ACTIVATE, path);
                activatePDPPages(resourceResolver, session, productId, path, false);
            }
            if ("removed".equalsIgnoreCase(eventType)) {
                replicator.replicate(session, ReplicationActionType.DEACTIVATE, path);
                deactivatePDPPages(session, productId, false);
            }
        } catch (final ReplicationException e) {
            LOG.error("ReplicationException in deactivate PDP::" + path, e.getMessage(), e);
        }
        dispatcherFlushAssayMenu(productId);
    }
    
    /**
     * @param resourceResolver
     * @param session
     * @param path
     * @param eventType
     */
    public void handleTestApplicationEvent(final ResourceResolver resourceResolver, final Session session,
            final String path, final String eventType) {
        final String productId = path.substring(path.lastIndexOf("/") + 1);
        if (!productId.startsWith(PRODUCT_TAP_PREFIX)) {
            return;
        }
        try {
            if ("add".equalsIgnoreCase(eventType)) {
                replicator.replicate(session, ReplicationActionType.ACTIVATE, path);
            }
            if ("update".equalsIgnoreCase(eventType) && configurationService.isActivatePDPOnDTLUpdate()) {
                replicator.replicate(session, ReplicationActionType.ACTIVATE, path);
                activatePDPPages(resourceResolver, session, productId, path, true);
            }
            if ("removed".equalsIgnoreCase(eventType)) {
                replicator.replicate(session, ReplicationActionType.DEACTIVATE, path);
                deactivatePDPPages(session, productId, true);
            }
        } catch (final ReplicationException e) {
            LOG.error("ReplicationException in add Product::" + path, e.getMessage(), e);
        }
    }
    
    /**
     * @param session
     * @param productId
     * @param isAssay
     * @throws ReplicationException
     */
    public void activatePDPPages(final ResourceResolver resolver, final Session session, final String productId,
            final String productPath, final Boolean isAssay) throws ReplicationException {
        List<String> pdpActivateList = null;
        if (isAssay) {
            pdpActivateList = pdpSearchQuery(session, productId, true);
        } else {
            pdpActivateList = pdpSearchQuery(session, productId, false);
        }
        if (!pdpActivateList.isEmpty()) {
            for (final String pdpPath : pdpActivateList) {
                updatePDP(resolver, productPath, pdpPath);
                replicator.replicate(session, ReplicationActionType.ACTIVATE, pdpPath);
            }
        }
    }
    
    /**
     * @param resolver
     * @param productPath
     * @param pdpPath
     */
    public void updatePDP(final ResourceResolver resolver, final String productPath, final String pdpPath) {
        final Resource productResource = resolver.getResource(productPath);
        final Resource pdpResource = resolver.getResource(pdpPath + "/jcr:content");
        final Locale languagePage = CommonUtils.getPageLocale(CommonUtils.getCurrentPage(pdpResource));
        final String language = languagePage.getLanguage();
        if (pdpResource != null && productResource != null) {
            final ModifiableValueMap properties = pdpResource.adaptTo(ModifiableValueMap.class);
            if (properties != null) {
                if (productResource.getValueMap().get("cq:tags") != null) {
                    properties.put("cq:tags", productResource.getValueMap().get("cq:tags"));
                }
                if (StringUtils.isBlank(language)
                        || language.equalsIgnoreCase(RocheConstants.ENGLISH_LANGUAGE_ISO_CODE)) {
                    if (productResource.getValueMap().get("jcr:title") != null) {
                        properties.put("jcr:title", productResource.getValueMap().get("jcr:title"));
                    }
                } else {
                    if (productResource.getValueMap().get("jcr:title" + language) != null) {
                        properties.put("jcr:title", productResource.getValueMap().get("jcr:title." + language));
                    }
                }
            }
            try {
                resolver.commit();
            } catch (final PersistenceException e) {
                LOG.error("PersistenceException in updatePDP method {}", e.getMessage(), e);
            }
        }
        
    }
    
    /**
     * @param session
     * @param productId
     * @param isAssay
     * @throws ReplicationException
     */
    public void deactivatePDPPages(final Session session, final String productId, final Boolean isAssay)
            throws ReplicationException {
        List<String> pdpDeactivateList = pdpSearchQuery(session, productId, false);
        if (isAssay) {
            pdpDeactivateList = pdpSearchQuery(session, productId, true);
        } else {
            pdpDeactivateList = pdpSearchQuery(session, productId, false);
        }
        if (!pdpDeactivateList.isEmpty()) {
            for (final String pdpPath : pdpDeactivateList) {
                replicator.replicate(session, ReplicationActionType.DEACTIVATE, pdpPath);
            }
        }
    }
    
    /**
     * @param productId
     */
    public void dispatcherFlushAssayMenu(final String productId) {
        final List<String> instrumentPages = configurationService.getInstrumentPages();
        final List<String> flushUrls = configurationService.getDispatcherFlushURL();
        for (final String flushUrl : flushUrls) {
            for (final String instrumentPage : instrumentPages) {
                CommonUtils.dispatchCacheFlush(instrumentPage + "/" + productId + ".html", flushUrl);
            }
        }
    }
    
    /**
     * @param session
     * @param productId
     * @param isAssay
     * @return pdp list
     */
    public List<String> pdpSearchQuery(final Session session, final String productId, final Boolean isAssay) {
        
        final List<String> assayActivateList = new ArrayList<>();
        final Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("path", "/content/customerportal");
        queryMap.put("type", "cq:Page");
        queryMap.put("p.limit", "-1");
        
        queryMap.put("1_property", "jcr:content/sling:resourceType");
        if (isAssay) {
            queryMap.put("1_property.1_value", "/apps/roche/pharma/customerportal/components/page/smallproductdetailpage");
            queryMap.put("1_property.2_value", "roche/customerportal/components/page/smallproductdetailpage");
        } else {
            queryMap.put("1_property.1_value", "/apps/roche/pharma/customerportal/components/page/productdetailpage");
            queryMap.put("1_property.2_value", "roche/customerportal/components/page/productdetailpage");
        }
        queryMap.put("2_property", "jcr:content/productId");
        queryMap.put("2_property.value", productId);
        
        final Query query = builder.createQuery(PredicateGroup.create(queryMap), session);
        final SearchResult result = query.getResult();
        for (final Hit hit : result.getHits()) {
            try {
                if (!hit.getPath().startsWith("/content/customerportal/global-master-blueprint")) {
                    assayActivateList.add(hit.getPath());
                }
            } catch (final RepositoryException e) {
                LOG.error("RepositoryException in pdpSearchQuery method {}", e.getMessage(), e);
            }
        }
        return assayActivateList;
    }
}
