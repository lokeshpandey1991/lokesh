package com.roche.pharma.customerportal.core.services.impl;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.dtl.services.InvocationBuilderService;
import com.roche.pharma.customerportal.core.dtl.services.RestClientEndPointService;
import com.roche.pharma.customerportal.core.dto.AssayMenuResponse;
import com.roche.pharma.customerportal.core.framework.ServiceResponse;
import com.roche.pharma.customerportal.core.mock.MockInvocationBuilderService;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;
import com.roche.pharma.customerportal.core.services.AssayMenuService;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;

public class AssayMenuServiceTest {

    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
    .build();

    final static String productNodePath = "/content/customerportal/us/en/home/roche";

    @Test
    public void testGetAssayServiceResponse() throws ServletException, IOException, WebserviceException {

        final InvocationBuilderService invoker = new MockInvocationBuilderService(200, "{\"responseCode\":200}");
        context.registerService(InvocationBuilderService.class, invoker);
        final RestClientEndPointService restService = new RestClientEndPointService();
        context.registerInjectActivateService(restService);
        final AssayMenuService assayMenuService = new AssayMenuServiceImpl();
        context.registerInjectActivateService(assayMenuService);
        final ServiceResponse<AssayMenuResponse> result = assayMenuService.viewAssayMenu(context.request(), "116068","en");
        Assert.assertEquals(200, result.getResponseCode().intValue());

    }

    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context) throws PersistenceException, IOException,
        javax.jcr.LoginException, RepositoryException, WebserviceException {
            MockRocheContent.loadfile(context, "/json/roche/workflow.json", productNodePath);
            final Resource res = context.request().getResourceResolver().getResource(productNodePath);
            context.request().setResource(res);

        }
    };
}
