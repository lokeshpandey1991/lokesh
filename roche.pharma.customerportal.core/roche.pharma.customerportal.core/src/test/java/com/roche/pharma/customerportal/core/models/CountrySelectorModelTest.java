package com.roche.pharma.customerportal.core.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.WCMException;
import com.roche.pharma.customerportal.core.beans.ContinentBean;
import com.roche.pharma.customerportal.core.services.impl.CountrySelectorServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class CountrySelectorModelTest {
    
    final static String COUNTRY_SELECTOR_PATH = "/content/customerportal/us/en/country-selector/jcr:content/par/countryselector";
    final static String MAPPING_PATH = "/etc/tags";
    private static final String[] redirectUrlConfig = {
            "us:en||/content/customerportal/us/en/home-page", "ca:en||/content/customerportal/ca/en/home-page"
    };
    private static final Map<String, String> redirectUrlMap;
    static {
        redirectUrlMap = new HashMap<String, String>();
        redirectUrlMap.put("us:en", "/content/customerportal/us/en/home-page");
        redirectUrlMap.put("ca:en", "/content/customerportal/ca/en/home-page");
    }
    
    final static String[] language2 = {
            "{\"languageCode\":\"en\",\"languageName\":\"English\",\"redirectPath\":\"/content/customerportal/ca/en/home\"}",
            "{\"languageCode\":\"fr\",\"languageName\":\"French\",\"redirectPath\":\"/content/customerportal/ca/fr/home\"}"
    };
    
    final static String[] language1 = {
            "{\"languageCode\":\"en\",\"languageName\":\"English\",\"redirectPath\":\"/content/customerportal/us/en/home\"}"
    };
    
    final static String[] language3 = {
            "{\"languageCode\":\"fr\",\"languageName\":\"French\",\"redirectPath\":\"/content/customerportal/fr/fr/home\"}"
    };
    
    final static String[] country1 = {
            "{\"countryCode\":\"US\",\"countryName\":\"United States\",\"languageBean\":language1}",
            "{\"countryCode\":\"CA\",\"countryName\":\"Canada\",\"languageBean\":language2}"
    };
    
    final static String[] country2 = {
            "{\"countryCode\":\"FR\",\"countryName\":\"France\",\"languageBean\":language3}"
    };
    
    final static String[] countrySelectorList = {
            "{\"continentCode\":\"na\",\"continentName\":\"United States\",\"countryBean\":country1}",
            "{\"continentCode\":\"eu\",\"continentName\":\"Europe\",\"countryBean\":country2}"
    };
    
    final static ContinentBean continentBean = new ContinentBean();
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    private final CountrySelectorServiceImpl countrySelectorService = new CountrySelectorServiceImpl();
    Map<String, String[]> _config = new HashMap<String, String[]>();
    
    @Before
    public void setUp() throws Exception {
        _config.put("roche.country.selector.redirect.url", redirectUrlConfig);
        context.registerInjectActivateService(countrySelectorService, _config);
        
    }
    
    @Test
    public void testGetAuthoredContent() {
        final CountrySelectorModel modelObject = context.request().adaptTo(CountrySelectorModel.class);
        Assert.assertEquals("/content/customerportal/us/en/home.html", modelObject.getGlobalHomePagePath());
        Assert.assertEquals("us", modelObject.getGlobalCountryCode());
        Assert.assertEquals("en", modelObject.getGlobalLanguageCode());
        Assert.assertEquals("countryselector", modelObject.getComponentName());
    }
    
    @Test
    public void testGetCountrySelectorList() {
        final CountrySelectorModel modelObject = context.request().adaptTo(CountrySelectorModel.class);
        continentBean.setContinentCode(modelObject.getCountrySelectorList().get(0).getContinentCode());
        continentBean.setContinentCode(modelObject.getCountrySelectorList().get(0).getContinentName());
        modelObject.getCountrySelectorList().get(0).getCountryBean().get(0).getCountryCode();
        modelObject.getCountrySelectorList().get(0).getCountryBean().get(0).getCountryName();
        modelObject.getCountrySelectorList().get(0).getCountryBean().get(0).getLanguageBean().get(0).getLanguageCode();
        modelObject.getCountrySelectorList().get(0).getCountryBean().get(0).getLanguageBean().get(0).getLanguageName();
        modelObject.getCountrySelectorList().get(0).getCountryBean().get(0).getLanguageBean().get(0).getRedirectPath();
    }
    
    @Test
    public void testGetLocale() {
        final CountrySelectorModel modelObject = context.request().adaptTo(CountrySelectorModel.class);
        Assert.assertEquals("de", modelObject.getLocale());
    }
    
    /**
     * Custom set up rules required in all unit tests.
     */
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context)
                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException, WCMException {
            context.addModelsForPackage("com.roche.pharma.customerportal.core.models");
            context.load().json("/json/country-selector.json", "/content/customerportal/us");
            context.load().json("/json/roche/tags.json", MAPPING_PATH);
            final Page page = context.pageManager().create(COUNTRY_SELECTOR_PATH, "test1",
                    "/apps/roche/pharma/customerportal/components/page/blankPage", "Country Selector");
            context.currentPage(page);
            final Resource res = context.request().getResourceResolver().getResource(COUNTRY_SELECTOR_PATH);
            context.request().setResource(res);
            context.requestPathInfo().setSelectorString("de");
        }
    };
    
}
