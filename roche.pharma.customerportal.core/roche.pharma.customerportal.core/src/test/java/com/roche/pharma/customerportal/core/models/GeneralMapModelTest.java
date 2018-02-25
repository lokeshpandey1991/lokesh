package com.roche.pharma.customerportal.core.models;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.beans.TimeDetailsListBean;
import com.roche.pharma.customerportal.core.mock.MockRocheContent;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class GeneralMapModelTest {

    final static String GENERALMAP_RESOURCE = "/content/roche/customerportal/us/en/home/jcr:content/par/generalmap";
    final static String[] TIMDEDETAILS_DATA = {
            "{\"timeDetails\":\"Monday-Friday:8:00 a.m.-5:00 p.m.\"}",
            "{\"timeDetails\":\"Staurday-Sunday:10:00 a.m.-6:00 p.m.\"}"
    };
    final static TimeDetailsListBean bean = new TimeDetailsListBean();

    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();

    @Test
    public void testjcrDataListing() throws LoginException {
        final GeneralMapModel modelObject = context.resourceResolver().getResource(GENERALMAP_RESOURCE)
                .adaptTo(GeneralMapModel.class);
        Assert.assertEquals("Monday-Friday:8:00 a.m.-5:00 p.m.", modelObject.getTimeDetailListing().get(0).getTimeDetails());
        Assert.assertEquals("Have any Question",modelObject.getHeading());
        Assert.assertEquals("<p>Biochemistry Building</p>", modelObject.getAddress());
        Assert.assertEquals("13213213213",modelObject.getPhoneNumber());
        Assert.assertEquals("sfsd@as.com",modelObject.getEmail());
        Assert.assertEquals("https://roche.com",modelObject.getCtaLink());
        Assert.assertEquals("View Website",modelObject.getCtaLabel());
        Assert.assertEquals("121.12112",modelObject.getLatitude());
        Assert.assertEquals("121.21212",modelObject.getLongitude());
        Assert.assertEquals("_self", modelObject.getLinkBehaviour());
        Assert.assertEquals("external", modelObject.getLinkType());
    }

   
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            MockRocheContent.loadfile(context, "/json/roche/roche.json", "/content");
            MockRocheContent.loadfile(context, "/json/roche/us/us.json", "/content/roche/customerportal/us");
            MockRocheContent.loadfile(context, "/json/roche/us/pages/home.json",
                    "/content/roche/customerportal/us/en/home");
        }
    };

}
