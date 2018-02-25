package com.roche.pharma.customerportal.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockRocheContent;

/**
 * The Class PageDescriptionModelTest.
 * @author Avinash kumar
 */
public class FaqModelTest {

    /** The Constant PDP_PATH. */
    final static String PDP_PATH = "/content/customerportal/us/en/home/jcr:content/faq";

    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context) throws PersistenceException, IOException, javax.jcr.LoginException,
        RepositoryException {
            context.addModelsForClasses(FaqModel.class);
            MockRocheContent.loadfile(context, "/json/roche/us/pages/pdp.json", "/content/customerportal/us/en/home");
        }
    };

    /** The model object. */
    private static FaqModel modelObject;

    /** The context. */
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
    .build();

    /**
     * Initialize.
     */
    @Before
    public void initialize() {

        modelObject = context.resourceResolver().getResource(PDP_PATH).adaptTo(FaqModel.class);
    }

    /**
     * Test cta label.
     */
    @Test
    public void testCtaLabel() {
        Assert.assertEquals("Faq headline is not valid", "Headline", modelObject.getHeadline());
    }

    /**
     * Test cta target.
     */
    @Test
    public void testCtaTarget() {

        final List<Map<String, String>> accordionArray = modelObject.getFaqQuestionAnswerArray();
        if (null != accordionArray && accordionArray.size() > 0) {
            for (final Map<String, String> key : accordionArray) {
                Assert.assertEquals("Faq Question answer is not valid", "Question", key.get("question"));
                Assert.assertEquals("Faq Question answer is not valid", "Answer", key.get("answer"));
            }
        } else {
            Assert.assertFalse("Faq Question answer is not valid", true);
        }
        
    }
    
}
