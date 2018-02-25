package com.roche.pharma.customerportal.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * The Class PersonaSelectorModelTest.
 * @author asagga
 */
public class PersonaSelectorModelTest {
    
    /** The Constant personaResourcePath. */
    final static String personaResourcePath = "/content/customerportal/us/en/personaselector/jcr:content/par/personaselector";
    
    /** The Constant personaListObject. */
    final static String personaListObject = "{\"personaSelectorType\":\"wcm:geo\",\"personaSelectorName\":\"fty\",\"personaSelectorDescription\":\"description\"}";
    
    /** The product path. */
    private static String productPath;
    
    /** The persona selector. */
    private PersonaSelectorModel personaSelector;
    
    /** The context. */
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    /**
     * Test get persona selector name.
     * @throws LoginException the login exception
     */
    @Test
    public void testGetPersonaSelectorName() throws LoginException {
        
        Resource personaResource = context.resourceResolver().getResource(personaResourcePath);
        
        personaSelector = personaResource.adaptTo(PersonaSelectorModel.class);
        
        Assert.assertEquals("fty", personaSelector.getPersonaList().get(0).getPersonaSelectorName());
        
    }
    
    /**
     * Test get persona selector type.
     * @throws LoginException the login exception
     */
    @Test
    public void testGetPersonaSelectorType() throws LoginException {
        
        Resource personaResource = context.resourceResolver().getResource(personaResourcePath);
        
        personaSelector = personaResource.adaptTo(PersonaSelectorModel.class);
        
        Assert.assertEquals("healthcare", personaSelector.getPersonaList().get(0).getPersonaSelectorType());
        
    }
    
    /**
     * Test get persona selector description.
     * @throws LoginException the login exception
     */
    @Test
    public void testGetPersonaSelectorDescription() throws LoginException {
        
        Resource personaResource = context.resourceResolver().getResource(personaResourcePath);
        
        personaSelector = personaResource.adaptTo(PersonaSelectorModel.class);
        
        Assert.assertEquals("description", personaSelector.getPersonaList().get(0).getPersonaSelectorDescription());
        Assert.assertEquals("personapicker", personaSelector.getComponentName());
    }
    
    /**
     * Test get persona header text.
     * @throws LoginException the login exception
     */
    @Test
    public void testGetPersonaHeaderText() throws LoginException {
        
        Resource personaResource = context.resourceResolver().getResource(personaResourcePath);
        
        personaSelector = personaResource.adaptTo(PersonaSelectorModel.class);
        
        Assert.assertEquals("hello this is the header of selector.", personaSelector.getPersonaHeaderText());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            
            context.load().json("/json/persona.json", personaResourcePath);
            context.load().json("/json/roche/tags.json", "/etc/tags/roche");
            
        }
    };
    
}
