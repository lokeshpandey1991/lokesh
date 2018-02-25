package com.roche.pharma.customerportal.core.services.exception;

/**
 * The Class UnauthorizedException.
 *
 * @author Avinash kumar
 */
public class UnauthorizedException extends WebserviceException {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /**
     * Instantiates a new unauthorized exception.
     */
    public UnauthorizedException() {
        super();
    }
    
    /**
     * Instantiates a new unauthorized exception.
     *
     * @param message the message
     */
    public UnauthorizedException(final String message) {
        super(message);
    }
}
