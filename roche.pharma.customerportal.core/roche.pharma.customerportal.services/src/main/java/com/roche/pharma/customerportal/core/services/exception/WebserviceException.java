package com.roche.pharma.customerportal.core.services.exception;

/**
 * The Class WebserviceException.
 *
 * @author Avinash kumar
 */
public class WebserviceException extends Exception {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new webservice exception.
     */
    public WebserviceException() {
        super();
    }
    
    /**
     * Instantiates a new webservice exception.
     *
     * @param message the message
     */
    public WebserviceException(final String message) {
        super(message);
    }
    
    /**
     * Instantiates a new webservice exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public WebserviceException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
}
