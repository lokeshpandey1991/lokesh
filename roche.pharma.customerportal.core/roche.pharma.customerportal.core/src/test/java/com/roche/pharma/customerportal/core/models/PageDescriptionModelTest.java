package com.roche.pharma.customerportal.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;

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
public class PageDescriptionModelTest {

    /** The Constant PDP_PATH. */
    final static String PDP_PATH = "/content/customerportal/us/en/home/jcr:content/pagedetails";

    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context) throws PersistenceException, IOException,
        javax.jcr.LoginException, RepositoryException {
            context.addModelsForClasses(PageDescriptionModel.class);
            MockRocheContent.loadfile(context, "/json/roche/us/pages/pagedescription.json",
                    "/content/customerportal/us/en/home");
        }
    };

    /** The model object. */
    private static PageDescriptionModel modelObject;

    /** The context. */
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
    .build();

    /**
     * Initialize.
     */
    @Before
    public void initialize() {

        modelObject = context.resourceResolver().getResource(PDP_PATH).adaptTo(PageDescriptionModel.class);
    }

    /**
     * Test cta label.
     */
    @Test
    public void testCtaLabel() {
        Assert.assertEquals("Page description label is not valid", "Label", modelObject.getLabel());
    }

    /**
     * Test cta target.
     */
    @Test
    public void testCtaTarget() {
        Assert.assertEquals("Page description  target is not valid", "_self", modelObject.getTarget());
    }

    /**
     * Test cta url.
     */
    @Test
    public void testCtaUrl() {
        Assert.assertEquals("Page description url  is not valid", "/content/roche/customerportal/us/en/home.html",
                modelObject.getUrl());
    }

    @Test
    public void testViewType() {
        Assert.assertEquals("Page description viewType  is not valid", "event", modelObject.getViewType());
    }

    @Test
    public void testImagePath() {
        Assert.assertEquals("Page description Image  is not valid",
                "/content/dam/roche/customerportal/products/product-7.png", modelObject.getFileReference());
    }

    @Test
    public void testAltText() {
        Assert.assertEquals("Page description url  is not valid", "Alt", modelObject.getAltText());
    }

    /**
     * Test first description.
     */
    @Test
    public void testFirstDescription() {
        Assert.assertEquals("Page Description is not valid", "Page description one",
                modelObject.getPageDescriptionOne());
    }

    /**
     * Test first heading.
     */
    @Test
    public void testFirstHeading() {
        Assert.assertEquals("Page Description heading is not valid", "Heading", modelObject.getTitleOne());
    }

    /**
     * Test Third heading.
     */
    @Test
    public void testThirdHeading() {
        Assert.assertEquals("Page Description heading is not valid", "heading", modelObject.getTitleThree());
    }

    /**
     * Test Location.
     */
    @Test
    public void testLocation() {
        Assert.assertEquals("Page Description heading is not valid", "Delhi India", modelObject.getLocation());
    }

    /**
     * Test Location.
     */
    @Test
    public void testPublishDate() {
        Assert.assertTrue(modelObject.getPublishDate() != null);
    }

    /**
     * Test second description.
     */
    @Test
    public void testSecondDescription() {
        Assert.assertEquals("Page Description is not valid", "Page description two",
                modelObject.getPageDescriptionTwo());
    }

    /**
     * Test second heading.
     */
    @Test
    public void testSecondHeading() {
        Assert.assertEquals("Page heading is not valid", "Sub heading", modelObject.getTitleTwo());
    }

    /**
     * Test two column view.
     */
    @Test
    public void testTwoColumnView() {
        Assert.assertEquals("Page Description is not two column", true, modelObject.isTwoColumnView());
    }

}
