package com.roche.pharma.customerportal.core.framework;

import com.google.gson.annotations.SerializedName;

/**
 * The Class ErrorDTO.
 *
 * @author Avinash kumar
 */
public class ErrorDTO {
    
    /** The code. */
    private String code;
    
    /** The message. */
    @SerializedName("error_message")
    private String message;
    
    /**
     * Gets the code.
     *
     * @return the code
     */
    public final String getCode() {
        return code;
    }
    
    /**
     * Sets the code.
     *
     * @param code the new code
     */
    public final void setCode(final String code) {
        this.code = code;
    }
    
    /**
     * Gets the message.
     *
     * @return the message
     */
    public final String getMessage() {
        return message;
    }
    
    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public final void setMessage(final String message) {
        this.message = message;
    }
}
