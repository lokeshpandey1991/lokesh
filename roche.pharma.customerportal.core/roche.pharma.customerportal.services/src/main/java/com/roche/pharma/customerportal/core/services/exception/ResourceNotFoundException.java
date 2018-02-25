package com.roche.pharma.customerportal.core.services.exception;

/**
 * The Class ResourceNotFoundException.
 *
 * @author Avinash kumar
 */
public class ResourceNotFoundException extends WebserviceException {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /**
     * Instantiates a new resource not found exception.
     */
    public ResourceNotFoundException() {
        super();
    }
    
    /**
     * Instantiates a new resource not found exception.
     *
     * @param message the message
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
