package com.roche.pharma.customerportal.core.services.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roche.pharma.customerportal.core.services.RocheEmailService;

/**
 * job Consumer for sending emails
 * @author asi130
 */
@Component(immediate = true, metatype = true, label = "Global Email Service")
@Service(value = JobConsumer.class)
@Property(name = JobConsumer.PROPERTY_TOPICS, value = "customerportal/email/job")
public class EmailJobConsumer implements JobConsumer {
    
    private static final String EMAIL_RECIPIENTS = "recipients";
    /** Constant for TEMPLATE */
    public static final String EMAIL_TEMPLATE = "TEMPLATE";
    
    /** The class logger service */
    private static final Logger LOGGER = LoggerFactory.getLogger(RocheEmailService.class);
    
    @Reference
    private RocheEmailService emailService;
    
    @Override
    public JobResult process(Job job) {
        final String templatePath = readEmailTemplate(job);
        final Map<String, String> emailParams = new HashMap<String, String>();
        emailParams.put("body", (String) job.getProperty("body"));
        emailParams.put("subject", (String) job.getProperty("subject"));
        emailParams.put(EMAIL_TEMPLATE, templatePath);
        final String[] emailRecipients = readRecipients(job);
        final List<String> failureList = emailService.sendEmail(emailParams, emailRecipients);
        if (!failureList.isEmpty()) {
            for (final String email : failureList) {
                LOGGER.error("not able to send Email to:" + email);
            }
            return JobResult.CANCEL;
        }
        
        return JobResult.OK;
    }
    
    /**
     * the method is used to extract an email template
     * @param job - the current job
     * @return {@link String}
     */
    private String readEmailTemplate(Job job) {
        final Object result = job.getProperty(EMAIL_TEMPLATE);
        if (result instanceof String) {
            return (String) result;
        }
        return null;
    }
    
    /**
     * the method is used to extract {@link List} of {@link String} from job this list contains email receive addresses
     * @param job - the current job
     * @return the email addresses
     */
    @SuppressWarnings("unchecked")
    private String[] readRecipients(Job job) {
        final Object result = job.getProperty(EMAIL_RECIPIENTS);
        if (result instanceof Collection) {
            return ((Collection<String>) result).toArray(new String[0]);
        }
        return new String[0];
    }
    
}
