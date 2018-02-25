package com.roche.pharma.customerportal.core.services.impl;

import java.util.List;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.event.jobs.JobManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.email.EmailService;
import com.roche.pharma.customerportal.core.services.RocheEmailService;

/**
 * implement RocheEmailservice. Uses EmailService. Used for sending emails
 * @author asi130
 */
@Component(immediate = true, label = "Global Email Service")
@Service(value = RocheEmailService.class)
public class RocheEmailServiceImpl implements RocheEmailService {
    /** The class logger service */
    private static final Logger LOGGER = LoggerFactory.getLogger(RocheEmailService.class);
    /** Constant for JOB topic */
    public static final String EMAIL_BODY = "body";
    /** Constant for TEMPLATE */
    public static final String EMAIL_TEMPLATE = "TEMPLATE";
    /** Constant for SUBJECT */
    public static final String EMAIL_SUBJECT = "subject";
    
    @Reference
    private EmailService emailService;
    
    @Reference
    private JobManager jobManager;
    
    /*
     * (non-Javadoc)
     * @see com.roche.pharma.customerportal.core.services.RocheEmailService#sendEmail(java.util.Map, java.lang.String[])
     */
    @Override
    public List<String> sendEmail(Map<String, String> emailParams, String[] recipients) {
        /** read the templatePath from email params */
        final String templatePath = readEmailTemplate(emailParams);
        final List<String> failureList = emailService.sendEmail(templatePath, emailParams, recipients);
        /** failure list provides list of email addresses for which email failed **/
        if (!failureList.isEmpty()) {
            for (final String email : failureList) {
                LOGGER.error("not able to send Email to:" + email);
            }
        }
        return failureList;
    }
    
    /* (non-Javadoc)
     * @see com.roche.pharma.customerportal.core.services.RocheEmailService#sendEmail(java.util.Map)
     */
    @Override
    public void sendEmail(Map<String, Object> emailParams) {
        jobManager.addJob("customerportal/email/job", emailParams);
        
    }
    
    /**
     * @param emailParams
     * @return String templatePath
     */
    protected String readEmailTemplate(Map<String, String> emailParams) {
        return emailParams.get(EMAIL_TEMPLATE);
    }
    
}
