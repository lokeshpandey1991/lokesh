package com.roche.pharma.customerportal.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import com.roche.pharma.customerportal.core.dtl.services.InvocationBuilderService;
import com.roche.pharma.customerportal.core.dtl.services.impl.InvocationBuilderimpl;
import com.roche.pharma.customerportal.core.mock.MockHelper;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;
import com.roche.pharma.customerportal.core.services.AssayMenuService;
import com.roche.pharma.customerportal.core.services.CacheManagerService;
import com.roche.pharma.customerportal.core.services.exception.WebserviceException;
import com.roche.pharma.customerportal.core.services.impl.AssayMenuServiceImpl;
import com.roche.pharma.customerportal.core.services.impl.EhCacheManagerServiceImpl;

/**
 * The Class AssayMenuModelTest.
 * @author Avinash kumar
 */
public class AssayMenuModelTest {

    /** The context. */
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();

    /** The Constant productNodePath. */
    final static String productNodePath = "/content/customerportal/us/en";

    /** The output. */
    static String OUTPUT = "{\"id\": \"INS_2202\",\"data\": {\"productId\": \"INS_2202\",\"relatedAssaysMap\": [{\"categoryName\": \"Women health\",\"assays\": [{\"productId\":\"20737941322\"},{\"productId\": \"11820176316\"}]},{\"categoryName\": \"Beauty\",\"assays\": [{\"productId\": \"20737836324\"},{\"productId\": \"04810716190\"}]}]}}";

    /** The model. */
    private static AssayMenuModel model;

    /** The Constant PDP_PATH. */
    final static String PDP_PATH = "/content/customerportal/us/en/jcr:content/assaymenu";

    /** The assay menu service. */
    private static AssayMenuService assayMenuService;

    /** Initialize.
     */
    @Before
    public void initialize() {

        model = context.request().adaptTo(AssayMenuModel.class);
    }


    /** Test get assay service response.
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws WebserviceException the webservice exception
     */
    @Test
    public void testGetAssayMenuTabs() throws ServletException, IOException, WebserviceException {
        Assert.assertEquals(3, model.getProducts().size() );
    }

    /** The Constant SETUP_CALLBACK. */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context) throws PersistenceException, IOException,
                javax.jcr.LoginException, RepositoryException, WebserviceException {
            context.addModelsForClasses(AssayMenuModel.class);
            MockRocheContent.loadfile(context, "/json/roche/products.json", productNodePath);
            final List<String> pdpPaths = new ArrayList<String>();
            pdpPaths.add(productNodePath + "/smallpdptest");
            pdpPaths.add(productNodePath + "/largepdptest");
            MockHelper.loadQuery(context, pdpPaths);
            final Resource res = context.request().getResourceResolver().getResource(productNodePath+"/jcr:content/assaymenu");
            context.request().setResource(res);

            final InvocationBuilderService invoker = new InvocationBuilderimpl();
            context.registerService(InvocationBuilderService.class, invoker);
            final CacheManagerService ehcache = Mockito.mock(EhCacheManagerServiceImpl.class);
            context.registerService(CacheManagerService.class, ehcache);
            assayMenuService = Mockito.mock(AssayMenuServiceImpl.class);
            context.registerService(assayMenuService);


        }
    };
}
