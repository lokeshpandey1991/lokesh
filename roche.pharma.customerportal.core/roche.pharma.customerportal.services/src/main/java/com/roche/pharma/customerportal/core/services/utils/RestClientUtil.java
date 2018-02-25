package com.roche.pharma.customerportal.core.services.utils;

import java.net.ConnectException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roche.pharma.customerportal.core.dtl.services.InvocationBuilderService;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;
import com.roche.pharma.customerportal.core.services.exception.WebserviceTimeoutException;
import com.roche.pharma.customerportal.core.services.models.Header;

/**
 * The Class RestClientUtil.
 *
 * @author Avinash kumar
 */
public final class RestClientUtil {
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RestClientUtil.class);
    
    /** The Constant CONNECTION_TIME_OUT. */
    private static final String CONNECTION_TIME_OUT = "SN: Connection Timeout Exception";
    
    /**
     * Instantiates a new rest client util.
     */
    private RestClientUtil() {}
    
    /**
     * Execute get request.
     *
     * @param servicesName the services name
     * @param header the header
     * @param service the service
     * @return the response
     * @throws WebserviceException the webservice exception
     */
    public static Response executeGetRequest(final String servicesName, final Header header,
            final InvocationBuilderService service) throws WebserviceException {
        try {
            return RestServiceInvoker.executeGet(servicesName, header, service);
        } catch (final ProcessingException e) {
            LOGGER.debug("SN: Connection Exception while executing get with params. Retrying... {}", e);
            try {
                return RestServiceInvoker.executeGet(servicesName, header, service);
            } catch (final ProcessingException e1) {
                LOGGER.error("SN: Connection Exception while retrying get. {}", e1);
                
                if (e1.getCause().getClass().isInstance(ConnectException.class)) {
                    LOGGER.error("Error while conneting to Jboss end point :::" + e1);
                    throw new WebserviceTimeoutException(CONNECTION_TIME_OUT);
                }
                // TOBeDone: Refactor for specific exceptions
                throw new WebserviceException(e1.getMessage());
            }
        }
    }
    
}
