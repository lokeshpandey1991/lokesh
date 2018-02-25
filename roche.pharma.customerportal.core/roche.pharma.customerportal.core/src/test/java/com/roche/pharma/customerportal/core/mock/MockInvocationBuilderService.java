package com.roche.pharma.customerportal.core.mock;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.mockito.Mockito;

import com.roche.pharma.customerportal.core.dtl.services.InvocationBuilderService;

public class MockInvocationBuilderService implements InvocationBuilderService {
    
    public MockInvocationBuilderService(Integer status, String responseString) {
        this.responseString = responseString;
        this.status = status;
    }
    
    private Integer status;
    
    private String responseString;
    
    public void setResponseEntity(String responseEntity) {
        responseString = responseEntity;
    }
    
    public void setStatus(Integer responseCode) {
        status = responseCode;
    }
    
    @Override
    public Response getBuilder(Builder invocationBuilder) {
        final Response response = Mockito.mock(Response.class);
        Mockito.when(response.getStatus()).thenReturn(status);
        Mockito.when(response.readEntity(String.class)).thenReturn(responseString);
        return response;
    }
    
}
