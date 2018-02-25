package com.roche.pharma.customerportal.core.services.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.roche.pharma.customerportal.core.dtl.services.InvocationBuilderService;
import com.roche.pharma.customerportal.core.dtl.services.impl.InvocationBuilderimpl;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;

public class InvocationBuilderImplTest {

    @Test
    public void testResponse() throws ServletException, IOException, WebserviceException {
        final InvocationBuilderService invoker = new InvocationBuilderimpl();
        final Invocation.Builder builder = Mockito.mock(Invocation.Builder.class);
        final Response responseMock = Mockito.mock(Response.class);
        Mockito.when(invoker.getBuilder(builder)).thenReturn(responseMock);
        final Response response = invoker.getBuilder(builder);
        Assert.assertNotNull(response);

    }

}
