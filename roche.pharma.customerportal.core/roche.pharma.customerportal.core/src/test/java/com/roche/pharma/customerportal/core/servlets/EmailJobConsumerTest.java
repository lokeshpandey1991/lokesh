package com.roche.pharma.customerportal.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.adobe.acs.commons.email.EmailService;
import com.roche.pharma.customerportal.core.mock.MockEmailService;
import com.roche.pharma.customerportal.core.mock.MockJobImpl;
import com.roche.pharma.customerportal.core.mock.MockJobManager;
import com.roche.pharma.customerportal.core.services.impl.EmailJobConsumer;
import com.roche.pharma.customerportal.core.services.impl.RocheEmailServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class EmailJobConsumerTest {
    private static JobManager jobmanager;
    private static EmailJobConsumer consumer = new EmailJobConsumer();
    private static MockJobImpl job;
    private static MockJobImpl jobBlank;
    static Map<String, String> jobParams = new HashMap<String, String>();
    static Map<String, String> jobParamsEmpty = new HashMap<String, String>();
    String recipients = "admin@roche.com";
    private final static RocheEmailServiceImpl emailService = new RocheEmailServiceImpl();
    private static EmailService sendEmailService;
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Before
    public void setUp() throws Exception {
        jobParams.put("subject", "this subject");
        jobParams.put("body", "this is email body");
        jobParams.put("recepients", recipients);
        jobParams.put("TEMPLATE", "/etc/notofocation");
        jobParams.put("recipients", recipients);
        context.registerInjectActivateService(consumer);
        job = new MockJobImpl(jobParams);
        jobBlank = new MockJobImpl(jobParamsEmpty);
        
    }
    
    @Test
    public void testProcess() throws LoginException {
        consumer.process(job);
    }
    
    @Test
    public void testProcessBlank() throws LoginException {
        consumer.process(jobBlank);
    }
    
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            sendEmailService = new MockEmailService(jobParams);
            context.registerService(EmailService.class, sendEmailService);
            jobmanager = new MockJobManager();
            context.registerService(JobManager.class, jobmanager);
            // context.registerService(RocheEmailServiceImpl.class, emailService);
            context.registerInjectActivateService(emailService);
        }
    };
}
