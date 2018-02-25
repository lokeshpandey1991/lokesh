package com.roche.pharma.customerportal.core.services.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.roche.pharma.customerportal.core.services.exception.JsonParsingException;

/**
 * The Class JsonConverter.
 *
 * @author Avinash kumar
 */
public final class JsonConverter {
    
    /** The gson. */
    private static Gson gson;
    static {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }
    
    /**
     * Instantiates a new json converter.
     */
    private JsonConverter() {}
    
    /**
     * Convert to json.
     *
     * @param <T> the generic type
     * @param object the object
     * @return the string
     */
    public static <T> String convertToJson(final T object) {
        return gson.toJson(object);
    }
    
    /**
     * Convert from json.
     *
     * @param <T> the generic type
     * @param json the json
     * @param type the type
     * @return the t
     * @throws JsonParsingException the json parsing exception
     */
    public static <T> T convertFromJson(final String json, final Type type) throws JsonParsingException {
        try {
            return gson.fromJson(json, type);
        } catch (final JsonSyntaxException e) {
            throw new JsonParsingException(e.getMessage(), e);
        }
    }
    
}
