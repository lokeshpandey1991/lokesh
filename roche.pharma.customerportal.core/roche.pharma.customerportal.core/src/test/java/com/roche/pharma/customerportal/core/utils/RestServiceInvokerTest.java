package com.roche.pharma.customerportal.core.utils;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.dtl.services.InvocationBuilderService;
import com.roche.pharma.customerportal.core.dtl.services.RestClientEndPointService;
import com.roche.pharma.customerportal.core.mock.MockInvocationBuilderService;
import com.roche.pharma.customerportal.core.services.exception.ResourceNotFoundException;
import com.roche.pharma.customerportal.core.services.exception.UnauthorizedException;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;
import com.roche.pharma.customerportal.core.services.models.Header;
import com.roche.pharma.customerportal.core.services.utils.RestServiceInvoker;

public class RestServiceInvokerTest {

    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
    .build();

    @Test
    public void testExecuteGet() throws ServletException, IOException, WebserviceException {
        final InvocationBuilderService invoker = new MockInvocationBuilderService(200, "{}");
        context.registerService(InvocationBuilderService.class, invoker);
        final RestClientEndPointService restService = new RestClientEndPointService();
        context.registerInjectActivateService(restService);
        RestServiceInvoker.executeGet("products/116068", new Header(), invoker);
        Assert.assertEquals(200, context.response().getStatus());

    }

    @Test
    public void testHeaderResponse() throws ServletException, IOException, WebserviceException {
        final InvocationBuilderService invoker = new MockInvocationBuilderService(200, "{}");
        context.registerService(InvocationBuilderService.class, invoker);
        final RestClientEndPointService restService = new RestClientEndPointService();
        context.registerInjectActivateService(restService);
        final Header header = new Header();
        header.setTokenHeader("token_header");
        header.setPasswordResetToken("token_passwordResetToken");
        header.setUniqueIdentifier("id");
        RestServiceInvoker.executeGet("products/116068", header, invoker);
        Assert.assertEquals(200, context.response().getStatus());

    }

    @Test
    public void testBuilder() throws ServletException, IOException, WebserviceException {
        final Map<String, Object> map = new HashMap<String, Object>();
        final InvocationBuilderService invoker = new MockInvocationBuilderService(200, "{}");
        context.registerService(InvocationBuilderService.class, invoker);
        final RestClientEndPointService restService = new RestClientEndPointService();
        context.registerInjectActivateService(restService);
        map.put("language", "en");
        RestServiceInvoker.executeGet("products/116068", new Header(), invoker, map);
        Assert.assertEquals(200, context.response().getStatus());

    }

    @Test
    public void testHandleResponse() throws ServletException, IOException {
        final InvocationBuilderService invoker = new MockInvocationBuilderService(401, "{}");
        context.registerService(InvocationBuilderService.class, invoker);
        final RestClientEndPointService restService = new RestClientEndPointService();
        context.registerInjectActivateService(restService);
        try {

            RestServiceInvoker.executeGet("products/116068", new Header(), invoker);
            Assert.assertTrue(false);
        } catch (final UnauthorizedException e) {
            Assert.assertTrue(true);
        } catch (final WebserviceException e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testHandlePageNotFound() throws ServletException, IOException {
        final InvocationBuilderService invoker = new MockInvocationBuilderService(404, "{}");
        context.registerService(InvocationBuilderService.class, invoker);
        final RestClientEndPointService restService = new RestClientEndPointService();
        context.registerInjectActivateService(restService);
        try {

            RestServiceInvoker.executeGet("products/116068", new Header(), invoker);
            Assert.assertTrue(false);
        } catch (final ResourceNotFoundException e) {
            Assert.assertTrue(true);
        } catch (final WebserviceException e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testHandleResponseServerError() throws ServletException, IOException, WebserviceException {
        final InvocationBuilderService invoker = new MockInvocationBuilderService(500, "{}");
        context.registerService(InvocationBuilderService.class, invoker);
        final RestClientEndPointService restService = new RestClientEndPointService();
        context.registerInjectActivateService(restService);
        try {
            RestServiceInvoker.executeGet("products/116068", new Header(), invoker);
            Assert.assertTrue(false);
        } catch (final ResourceNotFoundException e) {
            Assert.assertTrue(false);

        } catch (final WebserviceException e) {
            Assert.assertTrue(true);
        }
    }

    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context) throws PersistenceException, IOException,
        javax.jcr.LoginException, RepositoryException, WebserviceException {

        }
    };
}
