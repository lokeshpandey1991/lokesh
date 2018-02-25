package com.roche.pharma.customerportal.core.utils;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;
import javax.ws.rs.ProcessingException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.dtl.services.InvocationBuilderService;
import com.roche.pharma.customerportal.core.dtl.services.RestClientEndPointService;
import com.roche.pharma.customerportal.core.dtl.services.impl.InvocationBuilderimpl;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;
import com.roche.pharma.customerportal.core.services.models.Header;
import com.roche.pharma.customerportal.core.services.utils.RestClientUtil;

public class RestClientUtilTest {

    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();

    @Test
    public void testHandleResponseServerError() throws ServletException, IOException {
        try {
              final RestClientEndPointService restService = new RestClientEndPointService();
            context.registerInjectActivateService(restService);
            final Header header = new Header();
            header.setPasswordResetToken("tocken");
            header.setTokenHeader("tockenheader");
            header.setUniqueIdentifier("uniqueIdentifier");
            final InvocationBuilderService service = new InvocationBuilderimpl();
            RestClientUtil.executeGetRequest("products/116068", header, service);
            Assert.assertTrue(false);
        } catch (final ProcessingException | WebserviceException e) {
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
