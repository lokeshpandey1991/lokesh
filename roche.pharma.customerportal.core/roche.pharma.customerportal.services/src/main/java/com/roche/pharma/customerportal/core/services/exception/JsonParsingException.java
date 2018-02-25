package com.roche.pharma.customerportal.core.services.exception;

/**
 * The Class JsonParsingException.
 *
 * @author Avinash kumar
 */
public class JsonParsingException extends WebserviceException {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /**
     * Instantiates a new json parsing exception.
     */
    public JsonParsingException() {
        super();
    }
    
    /**
     * Instantiates a new json parsing exception.
     *
     * @param message the message
     */
    public JsonParsingException(final String message) {
        super(message);
    }
    
    /**
     * Instantiates a new json parsing exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public JsonParsingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
