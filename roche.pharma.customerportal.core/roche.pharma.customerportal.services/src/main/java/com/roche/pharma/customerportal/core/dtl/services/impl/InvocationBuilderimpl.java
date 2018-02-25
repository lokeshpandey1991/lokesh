package com.roche.pharma.customerportal.core.dtl.services.impl;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.roche.pharma.customerportal.core.dtl.services.InvocationBuilderService;

/**
 * The Class InvocationBuilderimpl.
 * @author Avinash kumar
 */
@Component(metatype = false, immediate = true,
        name = "com.roche.customerportal.core.dtl.services.impl.InvocationBuilderService")
@Service(InvocationBuilderService.class)
public class InvocationBuilderimpl implements InvocationBuilderService {
    
    /*
     * (non-Javadoc)
     * @see
     * com.roche.customerportal.core.dtl.services.InvocationBuilderService#getBuilder(javax.ws.rs.client.Invocation.
     * Builder)
     */
    @Override
    public Response getBuilder(Builder invocationBuilder) {
        return invocationBuilder.get();
    }
}
