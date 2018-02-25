package com.roche.pharma.customerportal.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.adobe.acs.commons.email.EmailService;
import com.roche.pharma.customerportal.core.mock.MockCaptchaService;
import com.roche.pharma.customerportal.core.mock.MockEmailService;
import com.roche.pharma.customerportal.core.mock.MockHelper;
import com.roche.pharma.customerportal.core.mock.MockJobManager;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;
import com.roche.pharma.customerportal.core.services.CaptchaService;
import com.roche.pharma.customerportal.core.services.impl.RocheEmailServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class ContactUsServletTest {
    private static ContactUsServlet servlet;
    private final static RocheEmailServiceImpl emailService = new RocheEmailServiceImpl();
    private static CaptchaService captchaService;
    private static JobManager jobmanager;
    private static EmailService sendEmailService;
    final static String templatePath = "/content/rocheus/en/emailtemplate";
    final static String INDEXNODEPATH = "/content/customerportal/us/en/home/contactus";
    final static String EMAILPATH = "/content/customerportal/us/en/emailtemplate";
    final static String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<afData>\r\n"
            + "  <afUnboundData>\r\n" + "    <data>\r\n"
            + "      <interestEmail>asingh131@sapient.com</interestEmail>\r\n"
            + "      <firstName>amarjot</firstName>\r\n" + "      <phonenumber>9990852917</phonenumber>\r\n"
            + "      <lastname>jnjnj</lastname>\r\n" + "      <email>amarjotsingh8@gmail.com</email>\r\n"
            + "      <accountNumber>ddsds</accountNumber>\r\n" + "      <comments>ddddsds</comments>\r\n"
            + "      <action>/bin/contactus.html</action>\r\n" + "      <interestArea>test@gmail.com</interestArea>\r\n"
            + "      \r\n" + "    </data>\r\n" + "  </afUnboundData>\r\n" + "  <afBoundData>\r\n"
            + "    <data xmlns:xfa=\"http://www.xfa.org/schema/xfa-data/1.0/\"/>\r\n" + "  </afBoundData>\r\n"
            + "  <afSubmissionInfo>\r\n"
            + "    <lastFocusItem>guide[0].guide1[0].guideRootPanel[0].contactuspanel[0]</lastFocusItem>\r\n"
            + "    <computedMetaInfo/>\r\n" + "  </afSubmissionInfo>\r\n" + "</afData>";
    final static String OUTPUT = "{\"notificationTitle\":\"rdoe_ContactUs.SuccessTitle\",\"notificationDescription\":\"rdoe_ContactUs.SuccessMessage\",\"notificationType\":\"confirmation\"}";
    static Map<String, String> emailParams = new HashMap<String, String>();
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
        context.registerInjectActivateService(emailService);
    }
    
    @Test
    public void testContactUsServlet() throws ServletException, IOException {
        servlet.doPost(context.request(), context.response());
        Assert.assertEquals(OUTPUT, context.response().getOutputAsString());
    }
    
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            
            context.request().setMethod(HttpConstants.METHOD_POST);
            final Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("firstName", "firstName");
            parameterMap.put("secondName", "secondName");
            parameterMap.put("subject", "this subject");
            parameterMap.put("date", "10th september");
            parameterMap.put("currentPagepath", "/content/customerportal/us/en/home/contactus");
            parameterMap.put("dataXML", XML);
            context.request().setParameterMap(parameterMap);
            context.requestPathInfo().setExtension("html");
            context.registerService(RocheEmailServiceImpl.class, emailService);
            captchaService = new MockCaptchaService();
            context.registerService(CaptchaService.class, captchaService);
            sendEmailService = new MockEmailService(emailParams);
            context.registerService(EmailService.class, sendEmailService);
            jobmanager = new MockJobManager();
            context.registerService(JobManager.class, jobmanager);
            servlet = MockHelper.getServlet(context, ContactUsServlet.class);
            MockRocheContent.loadfile(context, "/json/roche/us/pages/contactUs.json", INDEXNODEPATH);
            context.request().setResource(context.currentResource(INDEXNODEPATH));
            MockRocheContent.loadfile(context, "/json/roche/us/pages/email.json", EMAILPATH);
            // context.request().setResource(context.currentResource(INDEXNODEPATH));
            
        }
    };
}
