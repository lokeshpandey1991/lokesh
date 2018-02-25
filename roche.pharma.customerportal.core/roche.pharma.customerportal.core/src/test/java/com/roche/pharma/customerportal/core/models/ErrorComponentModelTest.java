package com.roche.pharma.customerportal.core.models;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.beans.LinksListBean;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class ErrorComponentModelTest {
    
    final static String ERROR_COMPONENT_PATH = "/content/roche/customerportal/us/errors/404/jcr:content/par/errorpage";
    final static LinksListBean bean = new LinksListBean();
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    @Test
    public void TestErrorPageTitle() {
        ErrorComponentModel model = context.resourceResolver().getResource(ERROR_COMPONENT_PATH)
                .adaptTo(ErrorComponentModel.class);
        Assert.assertEquals("404 page not found", model.getNotificationTitle());
        
    }
    
    @Test
    public void TestErrorPageDescription(){
        ErrorComponentModel model = context.resourceResolver().getResource(ERROR_COMPONENT_PATH)
                .adaptTo(ErrorComponentModel.class);
        Assert.assertEquals("<p>We are sorry we could not find the page you were looking for.</p>\r\n", model.getNotificationDescription());
    }
    
    @Test
    public void TestErrorPageLinkText(){
        ErrorComponentModel model = context.resourceResolver().getResource(ERROR_COMPONENT_PATH)
                .adaptTo(ErrorComponentModel.class);
        bean.setLinkText("Home");
        Assert.assertEquals("Home", model.getLinks().get(0).getLinkText());
    }
    
    @Test
    public void TestErrorPageLink(){
        ErrorComponentModel model = context.resourceResolver().getResource(ERROR_COMPONENT_PATH)
                .adaptTo(ErrorComponentModel.class);
        bean.setLinkURL("/content/roche/customerportal/us/en/home");
        Assert.assertEquals("/content/roche/customerportal/us/en/home.html", model.getLinks().get(0).getLinkURL());
    }
    
    @Test
    public void TestErrorComponentName(){
        ErrorComponentModel model = context.resourceResolver().getResource(ERROR_COMPONENT_PATH)
                .adaptTo(ErrorComponentModel.class);
        Assert.assertEquals("errorPage", model.getComponentName());
    }
    
    @Test
    public void TestErrorLinkPageType(){
        ErrorComponentModel model = context.resourceResolver().getResource(ERROR_COMPONENT_PATH)
                .adaptTo(ErrorComponentModel.class);
        bean.setLinkURL("News");
        Assert.assertEquals("News", model.getLinks().get(0).getPageType());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.load(context);
            MockRocheContent.loadfile(context, "/json/roche/us/pages/home.json", "/content/roche/customerportal/us/en/home");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/errors.json", "/content/roche/customerportal/us/errors");
        }
    };
    
}
