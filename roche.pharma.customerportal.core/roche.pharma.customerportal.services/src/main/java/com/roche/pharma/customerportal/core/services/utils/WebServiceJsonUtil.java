package com.roche.pharma.customerportal.core.services.utils;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.roche.pharma.customerportal.core.services.constants.ApplicationConstants;

/**
 * The Class WebServiceJsonUtil.
 *
 * @author Avinash kumar
 */
public final class WebServiceJsonUtil {

    /**
     * Instantiates a new web service json util.
     */
    private WebServiceJsonUtil() {

    }

    /**
     * Gets the json string from object.
     *
     * @param <T> the generic type
     * @param object the object
     * @return the json string from object
     */
    public static <T> String getJsonStringFromObject(T object) {
        final Gson gson = new Gson();
        return gson.toJson(object);
    }

    /**
     * Filter input.
     *
     * @param input the input
     * @return the string
     */
    public static String filterInput(String input) {
        if (input == null) {
            return StringUtils.EMPTY;
        }
        return input.replaceAll(ApplicationConstants.XSS_FILTER, "");

    }

}
