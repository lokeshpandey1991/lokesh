package com.roche.pharma.customerportal.core.services.utils;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roche.pharma.customerportal.core.dtl.services.InvocationBuilderService;
import com.roche.pharma.customerportal.core.framework.EndPointSingleton;
import com.roche.pharma.customerportal.core.services.constants.ApplicationConstants;
import com.roche.pharma.customerportal.core.services.exception.ResourceNotFoundException;
import com.roche.pharma.customerportal.core.services.exception.UnauthorizedException;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;
import com.roche.pharma.customerportal.core.services.models.Header;

/**
 * The Class RestServiceInvoker.
 *
 * @author Avinash kumar
 */
public final class RestServiceInvoker {

    /** The Constant SERVICE_NAME. */
    private static final String SERVICE_NAME = "Service Name = ";

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceInvoker.class);

    /**
     * Instantiates a new rest service invoker.
     */
    private RestServiceInvoker() {}

    /**
     * Execute get.
     *
     * @param servicesName the services name
     * @param header the header
     * @param service the service
     * @return the response
     * @throws WebserviceException the webservice exception
     */
    public static Response executeGet(final String servicesName, final Header header,
            final InvocationBuilderService service) throws WebserviceException {
        final EndPointSingleton eps = EndPointSingleton.getInstance();
        final Invocation.Builder invocationBuilder = getBuilder(eps.getActiveEndPoint(), servicesName, header);
        Response response;
        LOGGER.debug(SERVICE_NAME + servicesName + " :: " + "URL with query string = " + invocationBuilder.toString());
        LOGGER.info("Before REST API Call for get : " + eps.getActiveEndPoint().concat(servicesName));
        response = service.getBuilder(invocationBuilder);
        LOGGER.info("After REST API Call for get : " + eps.getActiveEndPoint().concat(servicesName));
        final Integer responseCode = response.getStatus();
        handleResponse(responseCode);
        return response;
    }

    /**
     * Execute get.
     *
     * @param servicesName the services name
     * @param header the header
     * @param service the service
     * @param map the map
     * @return the response
     * @throws WebserviceException the webservice exception
     */
    public static Response executeGet(final String servicesName, final Header header,
            final InvocationBuilderService service, final Map<String, Object> map) throws WebserviceException {
        final EndPointSingleton eps = EndPointSingleton.getInstance();
        final Invocation.Builder invocationBuilder = getBuilder(eps.getActiveEndPoint(), servicesName, header, map);
        Response response;
        LOGGER.debug(SERVICE_NAME + servicesName + " :: " + "URL with query string = " + invocationBuilder.toString());
        LOGGER.info("Before REST API Call for get : " + eps.getActiveEndPoint().concat(servicesName));
        response = service.getBuilder(invocationBuilder);
        LOGGER.info("After REST API Call for get : " + eps.getActiveEndPoint().concat(servicesName));
        final Integer responseCode = response.getStatus();
        handleResponse(responseCode);
        return response;
    }

    /**
     * Gets the builder.
     *
     * @param baseUrl the base url
     * @param path the path
     * @param header the header
     * @return the builder
     */
    private static Invocation.Builder getBuilder(final String baseUrl, final String path, final Header header) {
        return getBuilder(baseUrl, path, header, null);

    }

    /**
     * Gets the builder.
     *
     * @param baseUrl the base url
     * @param path the path
     * @param header the header
     * @param map the map
     * @return the builder
     */
    private static Invocation.Builder getBuilder(final String baseUrl, final String path, final Header header,
            final Map<String, Object> map) {
        final Client client = ClientBuilder.newClient();
        WebTarget target = client.target(baseUrl).path(path);
        if (map != null) {
            target = MapIterator.fillQueryParams(target, map);
        }
        LOGGER.info("Get request URL with params : " + target.getUri());
        final Invocation.Builder builder = target.request().accept(MediaType.APPLICATION_JSON_TYPE);

        if (null != header.getToken()) {
            builder.header(ApplicationConstants.TOKEN, header.getToken());
        }
        if (null != header.getPasswordResetToken()) {
            builder.header(ApplicationConstants.PASSWORD_RESET_TOKEN, header.getPasswordResetToken());
        }
        if (null != header.getUniqueIdentifier()) {
            builder.header(ApplicationConstants.UNIQUE_IDENTIFIER, header.getUniqueIdentifier());
        }
        builder.header(ApplicationConstants.CONTENT_TYPE, "application/json");

        return builder;
    }

    /**
     * Handle response.
     *
     * @param responseCode the response code
     * @throws WebserviceException the webservice exception
     */
    private static void handleResponse(final Integer responseCode) throws WebserviceException {
        if (responseCode < Status.OK.getStatusCode() || responseCode > Status.PARTIAL_CONTENT.getStatusCode()) {
            if (responseCode.equals(Status.UNAUTHORIZED.getStatusCode())) {
                throw new UnauthorizedException(Status.UNAUTHORIZED.getReasonPhrase());
            } else if (responseCode.equals(Status.NOT_FOUND.getStatusCode())) {
                throw new ResourceNotFoundException(Status.NOT_FOUND.getReasonPhrase());
            } else {
                throw new WebserviceException(Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }
    }
}
