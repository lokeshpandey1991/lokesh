package com.roche.pharma.customerportal.core.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.roche.pharma.customerportal.core.services.CaptchaService;

/**
 * The Class CaptchaServiceImpl. This purpose of this service is to validate google recaptcha value whether it's a
 * valid captcha or not. This can be achieve by calling verify() method.
 */
@Service(value = CaptchaService.class)
@Component(immediate = true, metatype = true, label = "Captcha Service")
@Properties({
        @Property(name = "service.url", value = "https://www.google.com/recaptcha/api/siteverify",
                description = "Url of captcha service"),
        @Property(name = "service.description", value = "Service for captcha"),
        @Property(name = "captcha.service.clientsidekey", label = "Client Side Integration Key",
                description = "configure the Client Side Integration Key", value = ""),
        @Property(name = "captcha.service.serversidekey", label = "Server Side Integration Key",
                description = "configure the Server Side Integration Key", value = "")
})
public class CaptchaServiceImpl implements CaptchaService {
    
    /** The Constant QUERY_SYMBOL. */
    private static final String QUERY_SYMBOL = "?";
    
    /** The Constant SECRET_S_RESPONSE_S. */
    private static final String SECRET_S_RESPONSE_S = "secret=%s&response=%s";
    
    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(CaptchaServiceImpl.class);
    
    /** The client side key. */
    private String clientSideKey;
    
    /** The server side key. */
    private String serverSideKey;
    
    /** The service URL. */
    private String serviceURL;
        
    /**
     * Activate Method is called when bundle get initialize.
     * @param properties the properties
     */
    @Activate
    public void activate(final Map<String, Object> properties) {
        readProperties(properties);
    }
    
    /**
     * Read properties.
     * @param properties the properties this method set the properties
     */
    protected void readProperties(final Map<String, Object> properties) {
        this.serviceURL = properties.get("service.url").toString();
        this.clientSideKey = properties.get("captcha.service.clientsidekey").toString();
        this.serverSideKey = properties.get("captcha.service.serversidekey").toString();
    }
    
    /**
     * verify captcha
     * @param gRecaptchaResponse - Captcha Token Value
     * @return true , if valid captcha token
     */
    public boolean verify(String gRecaptchaResponse) {
        if (StringUtils.isBlank(gRecaptchaResponse)) {
            return false;
        }
        return validateCaptcha(gRecaptchaResponse);
        
    }
    
    /**
     * Validate captcha.
     * @param gRecaptchaResponse the g recaptcha response
     * @return true, if successful
     */
    private boolean validateCaptcha(String gRecaptchaResponse) {
        URLConnection connection;
        try {
            connection = createConnection(gRecaptchaResponse);
            final StringBuilder response = getServerResponse(connection);
            return getVerificationResponse(response);
        } catch (IOException e) {
            LOG.error("IOException in CaptchaServiceImpl::validateCaptcha", e);
            return false;
        }
    }
    
    /**
     * Gets the verification response.
     * @param response the response
     * @return the verification response
     */
    private boolean getVerificationResponse(StringBuilder response) {
        final Gson gson = new Gson();
        final JsonElement jsonElement = gson.fromJson(response.toString(), JsonElement.class);
        final JsonObject jobj = jsonElement.getAsJsonObject();
        return jobj.get("success").getAsBoolean();
    }
    
    /**
     * Gets the server response.
     * @param connection the connection
     * @return the server response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private StringBuilder getServerResponse(URLConnection connection) throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        final StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response;
    }
    
    /**
     * Creates the connection.
     * @param gRecaptchaResponse the g recaptcha response
     * @return the URL connection
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private URLConnection createConnection(String gRecaptchaResponse) throws IOException {
        final String charset = java.nio.charset.StandardCharsets.UTF_8.name();
        final String query = String.format(SECRET_S_RESPONSE_S, URLEncoder.encode(serverSideKey, charset),
                URLEncoder.encode(gRecaptchaResponse, charset));
        return new URL(serviceURL + QUERY_SYMBOL + query).openConnection();
    }
    
    /**
     * Provide captcha Client side key
     * This method is used to get the client side key 
     * from osgi console configuration
     */
    public String getClientSideKey() {
        return clientSideKey;
    }
    
}
