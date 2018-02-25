package com.roche.pharma.customerportal.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.roche.pharma.customerportal.core.exceptions.BusinessExecutionException;
import com.roche.pharma.customerportal.core.services.CaptchaService;
import com.roche.pharma.customerportal.core.services.RocheEmailService;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * @author asi130 Servlet called for contact us form submission
 */
@SlingServlet(paths = "/bin/Customerportal/ContactUs", extensions = {
        "html"
})

public class ContactUsServlet extends SlingAllMethodsServlet {
    /**
     * Roche email service for sending email to the user
     */
    
    @Reference
    private RocheEmailService emailService;
    
    @Reference
    private CaptchaService captchaService;
    
    /**
     * Serail Version UID
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(ContactUsServlet.class);
    
    private String date;
    
    private String referrer;
    
    private String interestEmail;
    
    /**
     * The static EMAILTEMPLATE
     */
    private static final String EMAILTEMPLATE = "emailTemplate";
    /**
     * The static EMAILBODY
     */
    private static final String EMAILBODY = "emailBody";
    /**
     * The static EMAILSUBJECT
     */
    private static final String EMAILSUBJECT = "emailSubject";
    /**
     * The static interestArea
     */
    private static final String INTERESTAREA = "interestArea";
    
    /**
     * The static action
     */
    private static final String ACTION = "action";
    
    /**
     * The static FIRSTNAME
     */
    private static final String DATE = "date";
    
    /**
     * The static REFERRER
     */
    private static final String REFERRER = "referrer";
    
    /**
     * The static I18NCONTACTUSTITLE
     */
    private static final String I18NCONTACTUSERRORTITLE = "rdoe_ContactUs.ErrorTitle";
    /**
     * The static I18NCONTACTUSTITLE
     */
    private static final String I18NCONTACTUSERRORMESSAGE = "rdoe_ContactUs.ErrorMessage";
    
    /**
     * The static I18NCONTACTUSTITLE
     */
    private static final String I18NCONTACTUSSUCCESSTITLE = "rdoe_ContactUs.SuccessTitle";
    /**
     * The static I18NCONTACTUSTITLE
     */
    private static final String I18NCONTACTSUCCESSMESSAGE = "rdoe_ContactUs.SuccessMessage";
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        JSONObject json = new JSONObject();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        final PrintWriter out = response.getWriter();
        try {
            
            final ResourceResolver resolver = request.getResourceResolver();
            final String reCaptcha = request.getParameter("recaptcha");
            final String pagePath = request.getParameter("currentPagepath");
            final Map<String, Object> emailParams = new HashMap<String, Object>();
            final String templatePath = getEmaiTemplate(resolver, pagePath);
            final Resource emailTemplate = resolver.getResource(templatePath + "/jcr:content/emailContent");
            final boolean validCaptcha = captchaService.verify(reCaptcha);
            if ((null == emailTemplate) || (!StringUtils.isEmpty(reCaptcha) && !validCaptcha)) {
                json = CommonUtils.getErrorNotification(I18NCONTACTUSERRORTITLE, I18NCONTACTUSERRORMESSAGE, null, null,
                        "error");
            } else {
                final Map<String, Object> emailvalueMap = emailTemplate.getValueMap();
                final String body = (String) emailvalueMap.get(EMAILBODY);
                emailParams.put("TEMPLATE", emailvalueMap.get(EMAILTEMPLATE));
                emailParams.put("subject", emailvalueMap.get(EMAILSUBJECT));
                if (request.getParameter(DATE) != null) {
                    date = request.getParameter(DATE);
                }
                if (request.getParameter(REFERRER) != null) {
                    referrer = request.getParameter(REFERRER);
                }
                List<String> recipientList = new ArrayList<String>();
                recipientList.add(interestEmail);
                final String xml = request.getParameter("dataXML");
                final Map<String, Object> xmlParam = getXmlParams(xml);
                xmlParam.put("Date", date);
                xmlParam.put("Referrer", referrer);
                recipientList = getRecipientList(xmlParam);
                xmlParam.remove(INTERESTAREA);
                xmlParam.remove(ACTION);
                final String EmailBody = getEmailParams(xmlParam, body);
                emailParams.put("recipients", recipientList);
                emailParams.put("body", EmailBody);
                emailService.sendEmail(emailParams);
                
                json = CommonUtils.getErrorNotification(I18NCONTACTUSSUCCESSTITLE, I18NCONTACTSUCCESSMESSAGE, null,
                        null, "confirmation");
            }
            
        } catch (final BusinessExecutionException exception) {
            LOG.error("eception caught during contact us servlet ", exception);
            json = CommonUtils.getErrorNotification(I18NCONTACTUSERRORTITLE, I18NCONTACTUSERRORMESSAGE, null, null,
                    "error");
        }
        finally {
            out.print(json);
        }
    }
    
    /**
     * Gets the recipient list.
     * @param xmlParam the xml param
     * @return the recipient list
     */
    private List<String> getRecipientList(Map<String, Object> xmlParam) {
        List<String> recipientList = new ArrayList<String>();
        interestEmail = (String) xmlParam.get(INTERESTAREA);
        String[] recipients = {
                interestEmail,
        };
        if (null != interestEmail && interestEmail.indexOf(',') != -1) {
            recipients = interestEmail.split(",");
        }
        recipientList = Arrays.asList(recipients);
        return recipientList;
    }
    
    private String getEmailParams(Map<String, Object> xmlParam, String body) {
        final StringBuilder emailbuffer = new StringBuilder(body.trim());
        for (final Entry<String, Object> entry : xmlParam.entrySet()) {
            emailbuffer.append('\n');
            emailbuffer.append(entry.getKey() + ':' + entry.getValue());
        }
        return emailbuffer.toString();
    }
    
    private Map<String, Object> getXmlParams(String formXml) {
        final Map<String, Object> xmlParams = new LinkedHashMap<String, Object>();
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(formXml));
            final Document xmlDoc = builder.parse(is);
            final Element root = xmlDoc.getDocumentElement();
            final Node data = root.getFirstChild().getNextSibling().getFirstChild().getNextSibling();
            final NodeList dataList = data.getChildNodes();
            for (int i = 0; i < dataList.getLength(); i++) {
                if (dataList.item(i).getNextSibling() != null && dataList.item(i).getNextSibling().hasChildNodes()) {
                    final String nodeName = dataList.item(i).getNextSibling().getNodeName();
                    final String nodeValue = dataList.item(i).getNextSibling().getFirstChild().getNodeValue();
                    xmlParams.put(nodeName, nodeValue);
                }
            }
            
        } catch (final SAXException exception) {
            LOG.error("SAXException caught while parsing contact us xml", exception);
            
        } catch (final ParserConfigurationException exception) {
            LOG.error("ParserConfigurationException  caught while parsing contact us xml", exception);
        } catch (final IOException exception) {
            LOG.error("IOException caught while parsing contact us xml", exception);
        }
        return xmlParams;
    }
    
    /**
     * @param resolver ResourceResolver
     * @param pagePath Path of the page on which form is submitted
     * @return
     */
    private String getEmaiTemplate(ResourceResolver resolver, String pagePath) {
        String emailPath = null;
        final String resolvedPath = resolver.resolve(pagePath).getPath();
        final Resource contactPage = resolver.getResource(resolvedPath + "/jcr:content");
        if (contactPage != null) {
            final Map<String, Object> emailvalueMap = contactPage.getValueMap();
            emailPath = (String) emailvalueMap.get("emailPath");
        }
        return emailPath;
    }
}
