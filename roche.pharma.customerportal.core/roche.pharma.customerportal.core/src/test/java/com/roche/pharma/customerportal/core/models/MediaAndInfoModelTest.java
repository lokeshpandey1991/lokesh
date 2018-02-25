package com.roche.pharma.customerportal.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;
import java.io.IOException;
import javax.jcr.RepositoryException;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import com.roche.pharma.customerportal.core.beans.MediaAndInfoBean;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;

public class MediaAndInfoModelTest {
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    final static String mediaInfoPath = "/content/roche/us/en/home-page/products-page/jcr:content/parCatalog/mediainfo";
    
    final static String[] mediaLinksData = {
            "{\"imageText\":\"Sample Supply 1\",\"altText\":\"Product Image\",\"imageHeading\":\"Sample Supply Heading 1\",\"imagePath\":\"/content/dam/roche/customerportal/products/carouselmachine_01.png\"}",
            "{\"imageText\":\"Sample Supply 2\",\"altText\":\"Product Image2\",\"imageHeading\":\"Sample Supply Heading 2\",\"imagePath\":\"/content/dam/roche/customerportal/products/carouselmachine_02.png\"}",
            "{\"imageText\":\"Sample Supply  3\",\"altText\":\"Product Image3\",\"imageHeading\":\"Sample Supply Heading 3\",\"imagePath\":\"/content/dam/roche/customerportal/products/carouselmachine_03.png\"}",
            "{\"imageText\":\"Sample Supply  4\",\"altText\":\"Product Image4\",\"imageHeading\":\"Sample Supply Heading 4\",\"imagePath\":\"/content/dam/roche/customerportal/products/carouselmachine_04.png\"}"
    };
    
    final static String imageHeading0 = "Sample Supply Heading 1";
    final static String imageText0 = "Sample Supply 1";
    final static String imagePath0 = "/content/dam/roche/customerportal/products/carouselmachine_01.png";
    final static String altText0 = "Product Image";
    
    final static MediaAndInfoBean bean = new MediaAndInfoBean();
    
    @Test
    public void testMediaData() throws LoginException {
        MediaAndInfoModel modelObject = context.resourceResolver().getResource(mediaInfoPath)
                .adaptTo(MediaAndInfoModel.class);
        Assert.assertEquals("Cobas Machine", modelObject.getCtaDescription());
        Assert.assertEquals("Contact US", modelObject.getCtaText());
        Assert.assertArrayEquals(mediaLinksData, modelObject.getMediaLinks());
    }
    
    @Test
    public void testMediaListData() throws LoginException {
        MediaAndInfoModel modelObject = context.resourceResolver().getResource(mediaInfoPath)
                .adaptTo(MediaAndInfoModel.class);
        bean.setImageHeading(modelObject.getMediaList().get(0).getImageHeading());
        bean.setimagePath(modelObject.getMediaList().get(0).getImagePath());
        bean.setImageText(modelObject.getMediaList().get(0).getImageText());
        Assert.assertEquals(imageHeading0, modelObject.getMediaList().get(0).getImageHeading());
        Assert.assertEquals(imageText0, modelObject.getMediaList().get(0).getImageText());
        Assert.assertEquals(imagePath0, modelObject.getMediaList().get(0).getImagePath());
        Assert.assertEquals(altText0, modelObject.getMediaList().get(0).getAltText());
        Assert.assertEquals("mediainfo", modelObject.getComponentName());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.load(context);
        }
    };
}