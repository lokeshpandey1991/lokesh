package com.roche.pharma.customerportal.core.exceptions;

/**
 * BusinessExecutionException class for the exceptions
 */
public class BusinessExecutionException extends RuntimeException {
    
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * BusinessExecutionException constructor
     * @param message value is be used.
     */
    public BusinessExecutionException(final String message) {
        super(message);
        
    }
    
    /**
     * BusinessExecutionException constructor
     * @param message value is be used.
     * @param cause throwable cause.
     */
    public BusinessExecutionException(final String message, final Throwable cause) {
        super(message, cause);
        
    }
    
}
