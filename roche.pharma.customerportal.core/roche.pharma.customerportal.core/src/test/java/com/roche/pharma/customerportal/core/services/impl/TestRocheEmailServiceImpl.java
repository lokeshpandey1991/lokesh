package com.roche.pharma.customerportal.core.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.adobe.acs.commons.email.EmailService;
import com.roche.pharma.customerportal.core.mock.MockEmailService;
import com.roche.pharma.customerportal.core.mock.MockJobManager;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class TestRocheEmailServiceImpl {
    
    private final static RocheEmailServiceImpl emailService = new RocheEmailServiceImpl();
    private static JobManager jobmanager;
    private static EmailService sendEmailService;
    final static String templatePath = "/content/rocheus/en/emailtemplate";
    final List<String> list = new ArrayList<String>();
    
    static Map<String, String> emailParams = new HashMap<String, String>();
    Map<String, Object> jobParams = new HashMap<String, Object>();
    String[] recipients = {
            "admin@roche.com"
    };
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Before
    public void setUp() throws Exception {
        emailParams.put("TEMPLATE", templatePath);
        emailParams.put("body", "this is email body");
        emailParams.put("subject", "this subject");
        jobParams.put("TEMPLATE", templatePath);
        jobParams.put("subject", "this subject");
        jobParams.put("body", "this is email body");
        jobParams.put("recepients", Arrays.asList(recipients));
        context.registerInjectActivateService(emailService);
    }
    
    @Test
    public void testreadEmailTemplate() throws LoginException {
        Assert.assertEquals(templatePath, emailService.readEmailTemplate(emailParams));
    }
    
    @Test
    public void testSendEmail() throws LoginException {
        Assert.assertEquals(list, emailService.sendEmail(emailParams, recipients));
        emailService.sendEmail(jobParams);
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            sendEmailService = new MockEmailService(emailParams);
            jobmanager = new MockJobManager();
            context.registerService(EmailService.class, sendEmailService);
            context.registerService(JobManager.class, jobmanager);
            // context.registerService(jobmanager);
        }
    };
    
}
