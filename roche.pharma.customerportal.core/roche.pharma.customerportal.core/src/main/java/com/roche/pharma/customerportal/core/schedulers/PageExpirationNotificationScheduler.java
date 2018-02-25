package com.roche.pharma.customerportal.core.schedulers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.ComponentContext;
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
import com.roche.pharma.customerportal.core.services.RocheEmailService;

/**
 * The Class PageExpirationNotificationScheduler.
 * @author Avinash kumar
 */
@Service
@Component(description = "PageExpirationNotificationScheduler", immediate = true, metatype = true,
        label = "PageExpirationNotificationScheduler")
@Properties({
        @Property(name = org.osgi.framework.Constants.SERVICE_VENDOR, value = "Sapient Razorfish"),
        @Property(name = org.osgi.framework.Constants.SERVICE_DESCRIPTION,
                value = "Page Expiration Notification Scheduler for website"),
        @Property(name = "senderEmailAddress", value = "roche@example.com"),
        @Property(name = "senderName", value = "Roche Admin"),
        @Property(name = "subject", value = "Page Deactivation alert"),
        @Property(name = "scheduler.expression", label = "Schedule",
                description = "Set the running schedule in cron-format. " + "See http://en.wikipedia.org/wiki/Cron "
                        + "(note: here, the first number represents seconds).",
                value = "0 * * * * ?"),
        @Property(name = "template", value = "/etc/notification/email/roche/pageExpiration/emailTemplate.html"),
        @Property(name = "recipients", value = {
                "abcd@example.com", "abcd@example.com"
        })
        
})
public class PageExpirationNotificationScheduler implements Runnable {
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PageExpirationNotificationScheduler.class);
    
    /** The Constant _10. */
    private static final int TEN = 10;
    
    /** The Constant _30. */
    private static final int THIRTY = 30;
    
    /** The Constant _60. */
    private static final int SIXTY = 60;
    
    /** The Constant COLUMN_START. */
    private static final String COLUMN_START = "<tr class='test-result-step-row test-result-step-row-altone'>";
    
    /** The Constant COLUMN_END. */
    private static final String COLUMN_END = "</tr>";
    
    /** The Constant CELL_START. */
    private static final String CELL_START = "<td class='test-result-step-command-cell'>";
    
    /** The Constant CELL_END. */
    private static final String CELL_END = "</td>";
    
    /** The constant Expirey Time **/
    private static final String EXPIREY_TIME = "expirationTime";
    
    /** The resource resolver factory. */
    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    
    /** The resource resolver. */
    private ResourceResolver resourceResolver;
    
    /** The email service. */
    @Reference
    private RocheEmailService emailService;
    
    /** The replicator Service **/
    @Reference
    private Replicator replicator;
    
    /** The session. */
    private Session session;
    
    /** The builder. */
    @Reference
    private QueryBuilder builder;
    
    /** The template. */
    private String template;
    
    private String subject;
    
    private String senderEmailAddress;
    
    private String senderName;
    
    /** The recipients. */
    private String[] recipients;
    
    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        
        LOGGER.info("Scheduler job called");
        
        try {
            processFurther();
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
        
    }
    
    /**
     * Process further.
     */
    private void processFurther() {
        try {
            final Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put(ResourceResolverFactory.SUBSERVICE, "rocheUser");
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
            session = resourceResolver.adaptTo(Session.class);
            
        } catch (final LoginException e) {
            LOGGER.error("Exception while getting resource resolver in processFurther method", e);
        }
        
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd", Locale.ENGLISH);
        final String startDate = format.format(cal.getTime()) + StringUtils.EMPTY;
        cal.add(Calendar.DATE, SIXTY);
        final String endDate = format.format(cal.getTime()) + StringUtils.EMPTY;
        final List<Map<String, Object>> map = getSearchQuery(startDate, endDate);
        if (!map.isEmpty()) {
            sendMail(map);
        }
        
    }
    
    /**
     * Gets the search query.
     * @param startDate the start date
     * @param endDate the end date
     * @return the search query
     */
    private List<Map<String, Object>> getSearchQuery(final String startDate, final String endDate) {
        final Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("path", "/content/customerportal");
        queryMap.put("p.limit", "-1");
        queryMap.put("1_property", EXPIREY_TIME);
        queryMap.put("1_property.operation", "exists");
        queryMap.put("2_daterange.property", EXPIREY_TIME);
        queryMap.put("2_daterange.lowerBound", startDate);
        queryMap.put("2_daterange.upperBound", endDate);
        queryMap.put("2_daterange.lowerOperation", ">=");
        queryMap.put("2_daterange.upperOperation", "<");
        queryMap.put("3_property", "cq:lastReplicationAction");
        queryMap.put("3_property.value", "Activate");
        
        final Query query = builder.createQuery(PredicateGroup.create(queryMap), session);
        final SearchResult result = query.getResult();
        final List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        
        for (final Hit hit : result.getHits()) {
            try {
                getPageData(dataList, hit.getResource());
                session.save();
            } catch (final RepositoryException | PersistenceException e) {
                LOGGER.error("Exception while reading resource", e);
            }
            
        }
        return dataList;
    }
    
    /**
     * @param dataList
     * @param resource
     * @throws RepositoryException
     * @throws PersistenceException
     */
    private void getPageData(List<Map<String, Object>> dataList, Resource resource)
            throws RepositoryException, PersistenceException {
        if (null != resource) {
            String payloadPath = resource.getPath();
            final Resource pageResource = resource.getParent();
            if (pageResource != null && pageResource.getPath() != null) {
                payloadPath = pageResource.getPath();
            }
            final String expireyTime = getTimeInMilliSec(resource);
            if (null != payloadPath && StringUtils.isNotBlank(expireyTime)) {
                getPageExpireyData(resource, expireyTime, payloadPath, dataList);
            }
        }
    }
    
    /**
     * Get a difference between two dates
     * @param expirationDate the De activation date of page
     * @param timeUnit the unit in which you want the diff
     * @return the difference value, in the provided unit
     */
    public static long getDateDiff(String expirationDate, TimeUnit timeUnit) {
        
        final long diffInMillies = Long.parseLong(expirationDate) - new Date().getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Activate.
     * @param context the context
     */
    protected final void activate(final ComponentContext context) {
        
        final Dictionary<?, ?> properties = context.getProperties();
        final String schedulerExpression = properties.get("scheduler.expression").toString();
        template = (String) properties.get("template");
        senderName = (String) properties.get("senderName");
        senderEmailAddress = (String) properties.get("senderEmailAddress");
        subject = (String) properties.get("subject");
        recipients = (String[]) properties.get("recipients");
        LOGGER.info("Scheduler Expression" + schedulerExpression);
        
    }
    
    /**
     * Send mail.
     * @param map the map
     */
    private void sendMail(List<Map<String, Object>> map) {
        final Map<String, String> emailProperties = new HashMap<String, String>();
        emailProperties.put("senderEmailAddress", senderEmailAddress);
        emailProperties.put("senderName", senderName);
        emailProperties.put("subject", subject);
        final StringBuilder buffer = new StringBuilder();
        emailProperties.put("TEMPLATE", template);
        
        for (final Map<String, Object> page : map) {
            buffer.append(COLUMN_START).append(CELL_START).append(page.get("path")).append(CELL_END).append(CELL_START)
                    .append(page.get("days")).append(CELL_END).append(COLUMN_END);
            
        }
        emailProperties.put("pageList", buffer.toString());
        emailService.sendEmail(emailProperties, recipients);
        
    }
    
    /**
     * This method returns expiring time set on the page in millisecond
     * @param resource
     * @return expireyDate
     */
    private String getTimeInMilliSec(Resource resource) {
        if (resource != null && resource.adaptTo(ValueMap.class) != null) {
            final ValueMap map = resource.adaptTo(ValueMap.class);
            if (map != null && map.get(EXPIREY_TIME) != null) {
                final GregorianCalendar expieryDate = (GregorianCalendar) map.get(EXPIREY_TIME);
                return String.valueOf(expieryDate.getTimeInMillis());
            }
        }
        return StringUtils.EMPTY;
    }
    
    /**
     * This method deactivated the page with exipery date same as current date
     * @param pagePath
     */
    private void deactivatePage(String pagePath) {
        final Session jcrSession = resourceResolver.adaptTo(Session.class);
        if (replicator != null) {
            try {
                replicator.replicate(jcrSession, ReplicationActionType.DEACTIVATE, pagePath);
            } catch (ReplicationException e) {
                LOGGER.error("Unable to replicate the scheduled pages", e);
            }
        }
        
    }
    
    /**
     * this class calculates the date of and check if email is already send if not adds the resource to sending email
     * list i.e dataList
     * @param resource
     * @param expireyTime
     * @param payloadPath
     * @param dataList
     * @throws PersistenceException
     */
    private void getPageExpireyData(Resource resource, String expireyTime, String payloadPath,
            List<Map<String, Object>> dataList) throws PersistenceException {
        
        final ModifiableValueMap map = resource.adaptTo(ModifiableValueMap.class);
        final Long days = getDateDiff(expireyTime, TimeUnit.DAYS);
        final Map<String, Object> data = new HashMap<String, Object>();
        final Long mailDays = resource.getValueMap().get("days") == null ? -1
                : (Long) resource.getValueMap().get("days");
        if (!mailDays.equals(days) && (days == SIXTY || (days == THIRTY) || (days == TEN))) {
            data.put("path", payloadPath);
            data.put("days", days);
            dataList.add(data);
            if (map != null) {
                map.put("days", days);
            }
            resourceResolver.commit();
        } else if (days == 0) {
            deactivatePage(payloadPath);
        }
    }
    
}
