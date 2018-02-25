package com.roche.pharma.customerportal.core.schedulers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyOption;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.search.Predicate;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.exceptions.BusinessExecutionException;
import com.roche.pharma.customerportal.core.services.ConfigurationService;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is a sceduler class which is responsible to create single or multiple index files having list of urls which are
 * being activated or deactivated in a configured time buffer. This class also acts as service and provide a list of
 * pages available on publish instances for full indexing.
 * @author Nitin Kumar
 */
@Service(SearchIndexScheduler.class)
@Component(metatype = true, immediate = true, label = "Roche Search Index Scheduler",
        description = "Roche Search Index Scheduler")
public class SearchIndexScheduler {
    /**
     * This property is configured from console and default value is false
     */
    @Property(name = "singleIndexNode", label = "Enable single index file", boolValue = false,
            description = "Check this to enable indexing file at the root path")
    private boolean singleIndexNode;
    
    /**
     * This property is configured from console and default value is 'scheduled'
     */
    @Property(name = "executionMode", label = "Execution Mode", options = {
            @PropertyOption(name = "scheduled", value = "scheduled"),
            @PropertyOption(name = "immediate", value = "immediate"),
            @PropertyOption(name = "disabled", value = "disabled")
    }, description = "Select the option here to decide when will this service run" + "Disabled - will not run ."
            + "Immediate - will run immediately" + "Scheduled - will run as per the cron expression (Default)")
    private String executionMode = "scheduled";
    
    /**
     * This property is configured from console and default value is "0 0/15 * 1/1 * ? *"
     */
    @Property(
            name = "cronJobExpression",
            label = "Cron Job Expression",
            value = "0 0 0/1 1/1 * ? *",
            description = "[every 15 minutes = 0 0/15 * 1/1 * ? *],[every hour = 0 0 0/1 1/1 * ? *], [12:01am daily = 0 1 0 ? * *]")
    private static final String CRON_EXPR = "0 0/15 * 1/1 * ? *";
    
    /**
     * This property is configured from console and default value is 2h, which indicates 2 Hours
     */
    @Property(name = "timeRange", label = "Time Range for Query to search results", value = "2h",
            description = "m=minute, h=hour, d=day, w=week, M=month, y=year")
    private String timeRange = "2h";
    
    /**
     * This property is configured from console and default value is product description page and product landing page
     */
    @Property(name = "templateList", label = "Template list to search", value = {
            RocheConstants.PRODUCT_RESOURCE, RocheConstants.PRODUCT_LANDING_RESOURCE,
            RocheConstants.EVENT_DETAIL_RESOURCE, RocheConstants.LANDING_PAGE_RESOURCE,
            RocheConstants.SMALL_PRODUCT_RESOURCE
    }, description = "sepcify the template resource type", unbounded = PropertyUnbounded.ARRAY, cardinality = 10)
    private String[] templateList = {
            RocheConstants.PRODUCT_RESOURCE, RocheConstants.PRODUCT_LANDING_RESOURCE,
            RocheConstants.EVENT_DETAIL_RESOURCE, RocheConstants.LANDING_PAGE_RESOURCE,
            RocheConstants.SMALL_PRODUCT_RESOURCE
    };
    
    /**
     * This property is configured from console and default value is false
     */
    @Property(name = "scheduler.concurrent", boolValue = false,
            description = "Check this if the jobs can be concurrently executed")
    private boolean isConcurrent;
    
    /**
     * This property is configured from console and default value is SINGLE
     */
    @Property(
            name = "scheduler.runOn",
            value = "SINGLE",
            description = "[job can either be bound to the leader of the topology = LEADER], [or a single instance = SINGLE]")
    private String scheduledTaskName;
    
    /**
     * constant for index node name
     */
    private static final String INDEXNODE = "publishedIndex";
    /**
     * Replcation action constant
     */
    private static final String REPLICATIONACTION = "cq:lastReplicationAction";
    
    /**
     * constant for roche system user
     */
    private static final String ROCHEUSER = "rocheUser";
    
    /**
     * Resource resolver factory
     */
    @Reference
    private ResourceResolverFactory resolverFactory;
    /**
     * Apache sling commons Scheduler
     */
    @Reference
    private Scheduler scheduler;
    /**
     * Apache sling settings
     */
    @Reference
    private SlingSettingsService settings;
    /**
     * CQ search query builder
     */
    @Reference
    private QueryBuilder builder;
    /**
     * Day CQ replicator
     */
    @Reference
    private Replicator replicator;
    /**
     * Roche Pharma Customerportal configuration service
     */
    @Reference
    private ConfigurationService configurationService;
    
    /**
     * Set for containing non duplicate list of actiavted pages
     */
    private final Set<String> nodeSet = new HashSet<>();
    
    private static final Logger LOG = LoggerFactory.getLogger(SearchIndexScheduler.class);
    
    /**
     * This is activate method and is called when this component is configured
     * @param componentContext
     */
    @Activate
    protected void activate(final ComponentContext componentContext) {
        init(componentContext);
    }
    
    /**
     * This method is called from activate method and it configure if the job is disabled or scheduled
     * @param componentContext component context for this configuration to read properties
     */
    private void init(final ComponentContext ctx) {
        @SuppressWarnings("unchecked")
        final Map<String, String> properties = (Map<String, String>) ctx.getProperties();
        executionMode = properties.get("executionMode");
        scheduledTaskName = properties.get("service.pid");
        timeRange = properties.get("timeRange");
        templateList = PropertiesUtil.toStringArray(properties.get("templateList"));
        singleIndexNode = PropertiesUtil.toBoolean(properties.get("singleIndexNode"), false);
        if (StringUtils.isEmpty(scheduledTaskName)) {
            throw new BusinessExecutionException("service.pid is unavailable.");
        }
        
        LOG.info(executionMode);
        
        final SearchIndexTask task = new SearchIndexTask();
        if (StringUtils.equals(executionMode, "disabled")) {
            LOG.info("under disabled execution mode");
            scheduler.unschedule(scheduledTaskName);
        } else {
            if (StringUtils.equals(executionMode, "scheduled")) {
                LOG.info("under scheduled execution mode");
                final ScheduleOptions schedulerOptions = scheduler.EXPR(CRON_EXPR);
                setSchedulerOptions(schedulerOptions, task);
            }
            if (StringUtils.equals(executionMode, "immediate")) {
                LOG.info("under immediate execution mode");
                final ScheduleOptions schedulerOptions = scheduler.NOW();
                setSchedulerOptions(schedulerOptions, task);
                LOG.info(schedulerOptions.toString());
            }
        }
        
    }
    
    /**
     * This method searches for the activated/deactivated urls and invokes the appropriate method for updating different
     * index files
     */
    private void updateIndex() {
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, ROCHEUSER);
        ResourceResolver resourceResolver = null;
        Session jcrSession = null;
        try {
            
            resourceResolver = CommonUtils.getResourceResolverFromSubService(resolverFactory, paramMap);
            jcrSession = resourceResolver.adaptTo(Session.class);
            final StopWatch timer = new StopWatch();
            timer.start();
            final Map<String, String> map = new HashMap<>();
            map.put("path", configurationService.getRootPath());
            map.put("type", "cq:Page");
            map.put("p" + RocheConstants.DOT + Predicate.PARAM_LIMIT, "-1");
            map.put("1_property", "@" + JcrConstants.JCR_CONTENT + "/cq:lastReplicationAction");
            map.put("1_property.1_value", "Activate");
            map.put("1_property.2_value", "Deactivate");
            map.put("2_relativedaterange.property", "@" + JcrConstants.JCR_CONTENT + "/cq:lastReplicated");
            map.put("2_relativedaterange.lowerBound", "-" + timeRange);
            map.put("3_property", "@" + JcrConstants.JCR_CONTENT + "/" + RocheConstants.SLING_RESOURCE);
            int i = 1;
            for (final String template : templateList) {
                map.put("3_property." + i + "_value", template);
                i++;
            }
            
            final Query query = builder.createQuery(PredicateGroup.create(map), jcrSession);
            final SearchResult result = query.getResult();
            
            if (singleIndexNode) {
                updateSingleIndex(resourceResolver, jcrSession, result);
            } else {
                updateMultipleIndexes(resourceResolver, jcrSession, result);
            }
            
            timer.stop();
            LOG.info("updateIndex method taken time : {}", timer.toString());
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
    }
    
    /**
     * This method is called to update index file ona root level
     * @param resourceResolver resource resolver
     * @param jcrSession session from resource resolver
     * @param result search results
     */
    private void updateSingleIndex(final ResourceResolver resourceResolver, final Session jcrSession,
            final SearchResult result) {
        try {
            for (final Hit hit : result.getHits()) {
                final String path = hit.getPath();
                final ValueMap properties = hit.getProperties();
                final Object replicationAction = properties.get(REPLICATIONACTION);
                final Object hideInSearch = properties.get(RocheConstants.HIDE_IN_SEARCH);
                if (null != replicationAction) {
                    updateSingleIndexNode(resourceResolver, jcrSession, path, replicationAction.toString(),
                            null == hideInSearch ? false : Boolean.getBoolean(hideInSearch.toString()));
                }
            }
            final String rootPath = configurationService.getRootPath();
            final Resource rootResource = resourceResolver.getResource(rootPath);
            if (null != rootResource) {
                final Node rootNode = rootResource.adaptTo(Node.class);
                if (null != rootNode && rootNode.hasNode(INDEXNODE)) {
                    final Node fileNode = rootNode.getNode(INDEXNODE);
                    replicator.replicate(jcrSession, ReplicationActionType.ACTIVATE, fileNode.getPath());
                }
            }
        } catch (final ReplicationException e) {
            LOG.error("ReplicationException in updateSingleIndex method {}", e);
        } catch (final RepositoryException e) {
            LOG.error("RepositoryException in updateSingleIndex {}", e);
        }
        
    }
    
    /**
     * This method is called to update index file If index file is not present it creates and update its properties
     * @param resourceResolver resource resolver
     * @param jcrSession jcr session from resource resolver
     * @param path path for the page
     * @param replicationAction replication action
     */
    private void updateSingleIndexNode(final ResourceResolver resourceResolver, final Session jcrSession,
            final String path, final String replicationAction, final boolean hideInSearch) {
        
        final String rootPath = configurationService.getRootPath();
        try {
            final Resource rootResource = resourceResolver.getResource(rootPath);
            if (null != rootResource) {
                final Node rootNode = rootResource.adaptTo(Node.class);
                Node fileNode;
                if (null != rootNode && rootNode.hasNode(INDEXNODE)) {
                    fileNode = rootNode.getNode(INDEXNODE);
                    LOG.info("Node already there {}", fileNode.getPath());
                    updateProperty(resourceResolver.map(path), fileNode, replicationAction, hideInSearch);
                    
                    jcrSession.refresh(true);
                    jcrSession.save();
                } else if (null != rootNode) {
                    fileNode = rootNode.addNode(INDEXNODE, JcrConstants.NT_UNSTRUCTURED);
                    LOG.info("Node created {}", fileNode.getPath());
                    updateProperty(resourceResolver.map(path), fileNode, replicationAction, hideInSearch);
                    
                    jcrSession.refresh(true);
                    jcrSession.save();
                }
                
            }
            
        } catch (final RepositoryException e) {
            LOG.error("Exception in updateSingleIndexNode {}", e);
        }
    }
    
    /**
     * This method is called to update different index files on country/language level
     * @param resourceResolver resource resolver
     * @param jcrSession jcr session from resource resolver
     * @param result Search results
     */
    private void updateMultipleIndexes(final ResourceResolver resourceResolver, final Session jcrSession,
            final SearchResult result) {
        try {
            for (final Hit hit : result.getHits()) {
                final String PATH = hit.getPath();
                final Page languagePage = getLanguagePage(resourceResolver, PATH);
                final ValueMap properties = hit.getProperties();
                final Object replicationAction = properties.get(REPLICATIONACTION);
                final Object hideInSearch = properties.get(RocheConstants.HIDE_IN_SEARCH);
                if (null != replicationAction && null != languagePage) {
                    updateMultipleIndexNodes(jcrSession, languagePage, resourceResolver.map(PATH),
                            replicationAction.toString(),
                            null == hideInSearch ? false : Boolean.getBoolean(hideInSearch.toString()));
                }
            }
            
            if (null != nodeSet) {
                for (final String nodePath : nodeSet) {
                    replicator.replicate(jcrSession, ReplicationActionType.ACTIVATE, nodePath);
                }
            }
        } catch (final RepositoryException e) {
            LOG.error("RepositoryException in updateMultipleIndexNodes {}", e);
        } catch (final ReplicationException e) {
            LOG.error("ReplicationException in updateMultipleIndexNodes {}", e);
        }
        
    }
    
    /**
     * This method is called to get language level page for a path in a site
     * @param resourceResolver resource resolver
     * @param path path for the page
     * @return language page for this page
     */
    private Page getLanguagePage(final ResourceResolver resourceResolver, final String path) {
        Page languagePage = null;
        Resource resource = resourceResolver.getResource(path);
        final String PARENT = ResourceUtil.getParent(path);
        if (null == resource && null != PARENT) {
            resource = resourceResolver.getResource(PARENT);
        }
        if (null != resource) {
            languagePage = CommonUtils.getRegionalLanguagePage(CommonUtils.getCurrentPage(resource));
        }
        
        return languagePage;
    }
    
    /**
     * This method is called to update different index nodes on country/language level If index file is not present it
     * creates and update its properties
     * @param jcrSession session from resource resolver
     * @param languagePage langauge page for current page
     * @param path path for the page
     * @param replicationAction replication action
     */
    private void updateMultipleIndexNodes(final Session jcrSession, final Page languagePage, final String path,
            final String replicationAction, final boolean hideInSearch) {
        
        try {
            final Node node = languagePage.adaptTo(Node.class);
            Node fileNode = null;
            if (null != node) {
                if (node.hasNode(INDEXNODE)) {
                    fileNode = node.getNode(INDEXNODE);
                    LOG.info("Node already there {}", fileNode.getPath());
                } else {
                    fileNode = node.addNode(INDEXNODE, JcrConstants.NT_UNSTRUCTURED);
                    LOG.info("Node created {}", fileNode.getPath());
                }
                updateProperty(path, fileNode, replicationAction, hideInSearch);
            }
            
            if (null != fileNode) {
                nodeSet.add(fileNode.getPath());
            }
            
            jcrSession.refresh(true);
            jcrSession.save();
            
        } catch (final RepositoryException e) {
            LOG.error("Exception in updateActivatedUrls {}", e);
        }
        
    }
    
    /**
     * This method is called from method updateMultipleIndexNodes and is to update properties in index nodes
     * @param path path of the page
     * @param fileNode cq node for file
     * @param replicationAction replication action
     */
    private void updateProperty(final String path, final Node fileNode, final String replicationAction,
            final boolean hideInSearch) {
        final String ACTIVATED_URLS = "activatedUrls";
        final String DEACTIVATED_URL = "deactivatedUrls";
        try {
            if ("Activate".equals(replicationAction) && !hideInSearch) {
                if (fileNode.hasProperty(ACTIVATED_URLS) && null != fileNode.getProperty(ACTIVATED_URLS).getValue()) {
                    final String finalList = fileNode.getProperty(ACTIVATED_URLS).getValue().getString();
                    setProperty(ACTIVATED_URLS, finalList, path, fileNode);
                } else {
                    fileNode.setProperty(ACTIVATED_URLS, path + "\n");
                }
            } else {
                if (fileNode.hasProperty(DEACTIVATED_URL) && null != fileNode.getProperty(DEACTIVATED_URL).getValue()) {
                    final String finalList = fileNode.getProperty(DEACTIVATED_URL).getValue().getString();
                    setProperty(DEACTIVATED_URL, finalList, path, fileNode);
                } else {
                    fileNode.setProperty(DEACTIVATED_URL, path + "\n");
                }
            }
        } catch (final RepositoryException e) {
            LOG.error("Exception in updateActivatedUrls {}", e);
        }
        
    }
    
    /**
     * This method will set property in a node and also checks whether that value in property already exists.
     * @param urls list of urls
     * @param finalList final list to be updated
     * @param path path of the node
     * @param fileNode file node to be updated
     */
    public void setProperty(final String urls, final String finalList, final String path, final Node fileNode) {
        try {
            if (!StringUtils.contains(finalList, path)) {
                fileNode.setProperty(urls, finalList + path + "\n");
            }
        } catch (final RepositoryException e) {
            LOG.error("Exception in setProperty {}", e);
        }
    }
    
    /**
     * This method will return the list of activated pages from the site
     * @param domain this is domain name for class
     * @return string list of urls
     */
    public String getActivatedUrls(final String domain) {
        final StringBuilder sb = new StringBuilder(100);
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, ROCHEUSER);
        ResourceResolver resourceResolver = null;
        Session session = null;
        try {
            resourceResolver = CommonUtils.getResourceResolverFromSubService(resolverFactory, paramMap);
            session = resourceResolver.adaptTo(Session.class);
            final StopWatch timer = new StopWatch();
            timer.start();
            final Map<String, String> queryMap = new HashMap<>();
            queryMap.put("path", configurationService.getRootPath());
            queryMap.put("type", "cq:Page");
            queryMap.put("p" + RocheConstants.DOT + Predicate.PARAM_LIMIT, "-1");
            queryMap.put("property", "@" + JcrConstants.JCR_CONTENT + "/" + RocheConstants.SLING_RESOURCE);
            int i = 1;
            for (final String template : templateList) {
                queryMap.put("property." + i + "_value", template);
                i++;
            }
            
            final Query query = builder.createQuery(PredicateGroup.create(queryMap), session);
            final SearchResult result = query.getResult();
            
            for (final Hit hit : result.getHits()) {
                final String url = domain + resourceResolver.map(hit.getPath()) + ".html";
                sb.append("<a href=\"").append(url).append("\">").append(url).append("</a><br /> \n");
            }
            timer.stop();
            LOG.info("Full Index query taken time : {}", timer.toString());
        } catch (final BusinessExecutionException e) {
            LOG.error("BusinessExecutionException in getActivatedUrls method {}", e);
        } catch (final RepositoryException e) {
            LOG.error("RepositoryException in getActivatedUrls method {}", e);
        } finally {
            if (session != null && session.isLive()) {
                session.logout();
            }
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
        return sb.toString();
        
    }
    
    /**
     * This method sets scheduler options.
     * @param schedulerOptions
     * @param task
     */
    private void setSchedulerOptions(final ScheduleOptions schedulerOptions, final SearchIndexTask task) {
        schedulerOptions.canRunConcurrently(false);
        schedulerOptions.config(null);
        schedulerOptions.name(scheduledTaskName);
        scheduler.schedule(task, schedulerOptions);
    }
    
    /**
     * This method is a clean up task to remove index nodes from previously created
     */
    public void cleanIndex() {
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, ROCHEUSER);
        ResourceResolver resourceResolver = null;
        Session session = null;
        try {
            resourceResolver = CommonUtils.getResourceResolverFromSubService(resolverFactory, paramMap);
            session = resourceResolver.adaptTo(Session.class);
            final Map<String, String> queryMap = new HashMap<>();
            queryMap.put("path", configurationService.getRootPath());
            queryMap.put(RocheConstants.SEARCH_TYPE, "nt:unstructured");
            queryMap.put("p" + RocheConstants.DOT + Predicate.PARAM_LIMIT, "-1");
            queryMap.put("nodename.nodename", INDEXNODE);
            queryMap.put("nodename.operation", "equals");
            
            final Query query = builder.createQuery(PredicateGroup.create(queryMap), session);
            final SearchResult result = query.getResult();
            
            for (final Hit hit : result.getHits()) {
                final Node node = hit.getNode();
                node.remove();
            }
            if (null != session) {
                session.save();
            }
        } catch (final BusinessExecutionException e) {
            LOG.error("BusinessExecutionException in cleanIndex method of SearchIndexScheduler class {}", e);
        } catch (final RepositoryException e) {
            LOG.error("RepositoryException in cleanIndex method of SearchIndexScheduler class {}", e);
        } finally {
            if (null != session && session.isLive()) {
                session.logout();
            }
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
    }
    
    /**
     * This class is to provide a task to run the schedular
     */
    public class SearchIndexTask implements Runnable {
        
        @Override
        public void run() {
            LOG.info("run method started");
            try {
                if (settings.getRunModes().contains("author")) {
                    cleanIndex();
                    updateIndex();
                }
            } catch (final BusinessExecutionException e) {
                LOG.error("BusinessExecutionException in SearchIndexTask runnable method", e);
            }
            LOG.info("run method end");
        }
    }
}
