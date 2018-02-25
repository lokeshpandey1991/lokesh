package com.roche.pharma.customerportal.core.models;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

/**
 * The Class TextHighlightRailModelTest.
 */
public class TextHighlightRailModelTest {
    
    /** The pagepath. */
    private static String PAGEPATH = "/content/customerportal/global-master-blueprint/en/testcarrer/jcr:content/par/columncontrol/par_1/texthighlightrail";
    
    /** The context. */
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    /**
     * Test text highlight rail model.
     */
    @Test
    public void testTextHighlightRailModel() {
        
        Resource resource = context.resourceResolver().getResource(PAGEPATH);
        TextHighlightRailModel textHighlightRailModel = resource.adaptTo(TextHighlightRailModel.class);
        
        Assert.assertEquals("hello", textHighlightRailModel.getHeadline());
        Assert.assertEquals("<p>jhsadufhausdfusafd</p>\r\n", textHighlightRailModel.getBodyText());
        Assert.assertEquals("/content/customerportal/global-master-blueprint/en/home",
                textHighlightRailModel.getCtaLink());
        Assert.assertEquals("jusdhufushduf", textHighlightRailModel.getCtaName());
    }
    
    /** The Constant SETUP_CALLBACK. */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            context.load().json("/json/roche/us/pages/text.json", PAGEPATH);
            
        }
    };
    
}
