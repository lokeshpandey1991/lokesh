package com.roche.pharma.customerportal.core.services.exception;

/**
 * The Class WebserviceTimeoutException.
 *
 * @author Avinash kumar
 */
public class WebserviceTimeoutException extends WebserviceException {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /**
     * Instantiates a new webservice timeout exception.
     */
    public WebserviceTimeoutException() {
        super();
    }
    
    /**
     * Instantiates a new webservice timeout exception.
     *
     * @param message the message
     */
    public WebserviceTimeoutException(final String message) {
        super(message);
    }
    
}
