package com.roche.pharma.customerportal.core.dtl.services;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

/**
 * The Interface InvocationBuilderService.
 * @author Avinash kumar
 */
@FunctionalInterface
public interface InvocationBuilderService {
    
    /**
     * Gets the builder.
     * @param invocationBuilder the invocation builder
     * @return the builder
     */
    Response getBuilder(Builder invocationBuilder);
    
}
