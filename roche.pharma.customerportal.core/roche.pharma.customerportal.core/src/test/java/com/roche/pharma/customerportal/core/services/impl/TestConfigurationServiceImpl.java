package com.roche.pharma.customerportal.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.services.ConfigurationService;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * JUnit test verifying the TestConfigurationServiceImpl
 */
public class TestConfigurationServiceImpl {
    
    private static final String VIDEO_SERVICE_URL = "https://gateway-eu.assetsadobe.com/DMGateway/";
    
    private static final String IMAGE_SERVICE_URL = "https://roche-staging-h.assetsadobe2.com/is/image";
    
    private static final String ROOTPATH = "/content/roche/customerportal";
    
    private static final String ASSAYPATH = "/content/customerportal/global/en/instrument";
    
    private static final String DISP_PATH = "http://10.100.4.28:80/dispatcher/invalidate.cache";
    
    private static final String PRODUCTLOGOPATH = "/content/customerportal/author-product-logos";
    
    private ConfigurationServiceImpl configurationServiceImpl = new ConfigurationServiceImpl();
    
    Map<String, Object> _config;
    
    @Rule
    public final AemContext aemContext = new AemContext();
    
    @Before
    public void setUp() throws Exception {
        _config = new HashMap<String, Object>();
        _config.put("image.service.url", IMAGE_SERVICE_URL);
        _config.put("video.service.url", VIDEO_SERVICE_URL);
        _config.put("service.rootPath", ROOTPATH);
        _config.put("assay.menu.pages", ASSAYPATH);
        _config.put("dispatcher.flush.url", DISP_PATH);
        _config.put("activate.pdp.on.dtl.event","false");
        _config.put("productlogo.service.pagePath", PRODUCTLOGOPATH);
        aemContext.registerService(ConfigurationService.class, configurationServiceImpl);
        configurationServiceImpl = aemContext.registerInjectActivateService(configurationServiceImpl, _config);
        configurationServiceImpl.activate(_config);
    }
    
    @Test
    public void testGetImageServiceUrl() throws Exception {
        assertEquals(IMAGE_SERVICE_URL, configurationServiceImpl.getImageServiceUrl());
        assertFalse(StringUtils.isBlank(configurationServiceImpl.getImageServiceUrl()));
    }
    
    @Test
    public void testGetVideoServiceUrl() throws Exception {
        assertEquals(VIDEO_SERVICE_URL, configurationServiceImpl.getVideoServiceUrl());
        assertFalse(StringUtils.isBlank(configurationServiceImpl.getVideoServiceUrl()));
    }
    
    @Test
    public void testGetRootPath() throws Exception {
        assertEquals(ROOTPATH, configurationServiceImpl.getRootPath());
        assertFalse(StringUtils.isBlank(configurationServiceImpl.getRootPath()));
    }
}
