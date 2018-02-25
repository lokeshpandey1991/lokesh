package com.roche.pharma.customerportal.core.framework;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class ServiceResponse.
 *
 * @author Avinash kumar
 * @param <T> the generic type
 */
@XmlRootElement
public class ServiceResponse<T> extends Response {
    
    /** The data. */
    private T data;
    
    /**
     * Gets the data.
     *
     * @return the data
     */
    public final T getData() {
        return data;
    }
    
    /**
     * Sets the data.
     *
     * @param data the new data
     */
    public final void setData(final T data) {
        this.data = data;
    }
    
}
