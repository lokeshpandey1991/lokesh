package com.roche.pharma.customerportal.core.services.utils;

import java.lang.reflect.Type;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roche.pharma.customerportal.core.framework.ServiceResponse;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;

/**
 * The Class JsonUtil.
 *
 * @author Avinash kumar
 */
public final class JsonUtil {

    /** The Constant JSON_STRING. */
    private static final String JSON_STRING = "jsonString : ";

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * Instantiates a new json util.
     */
    private JsonUtil() {}
    
    /**
     * Unmarshal response.
     *
     * @param <T> the generic type
     * @param response the response
     * @param type the type
     * @return the service response
     * @throws WebserviceException the webservice exception
     */
    public static <T> ServiceResponse<T> unmarshalResponse(final Response response, final Type type)
            throws WebserviceException {

        final String jsonString = response.readEntity(String.class);
        LOGGER.debug(JSON_STRING + jsonString);
        return JsonConverter.convertFromJson(jsonString, type);

    }

}
