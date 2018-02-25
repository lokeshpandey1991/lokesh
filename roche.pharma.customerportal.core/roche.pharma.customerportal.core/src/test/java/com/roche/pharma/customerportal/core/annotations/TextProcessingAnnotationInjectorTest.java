package com.roche.pharma.customerportal.core.annotations;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.annotations.interfaces.TextProcessing;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

/**
 * The Class TextProcessingAnnotationInjectorTest.
 */
public class TextProcessingAnnotationInjectorTest {

    /** The Constant BASEPATH. */
    private final static String BASEPATH = "/content/customerportal/us";
    
    /** The Constant ADAPTPATH. */
    private final static String ADAPTPATH = "/content/customerportal/us/jcr:content/languageconfig";

    /** The text processing. */
    private final TextProcessingAnnotationInjector textProcessing = new TextProcessingAnnotationInjector();

    /** The context. */
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    /**
     * Sets the up.
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        
        context.registerInjectActivateService(textProcessing);
    }
    
    /**
     * Test text processing.
     */
    @Test
    public final void testTextProcessing() {

        TestModel testModel = context.resourceResolver().getResource(ADAPTPATH).adaptTo(TestModel.class);

        Assert.assertNotNull(testModel);

    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            
            context.addModelsForPackage("com.roche.pharma.customerportal.core.annotations");
            context.load().json("/json/roche/us/us.json", BASEPATH);
        }
    };

    /**
     * The Inner Class TestModel.
     */
    @Model(adaptables = {
            Resource.class
    })
    public static class TestModel {
        
        /** The search collection. */
        @TextProcessing(sup = {

        }, suppre = "<sup>", suppost = "</sup>")
        String searchCollection;

        /**
         * Gets the search collection.
         * @return the search collection
         */
        public String getSearchCollection() {
            return searchCollection;
        }

    }
}
