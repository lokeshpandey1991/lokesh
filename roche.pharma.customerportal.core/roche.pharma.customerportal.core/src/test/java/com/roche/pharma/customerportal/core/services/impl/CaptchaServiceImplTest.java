package com.roche.pharma.customerportal.core.services.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.services.CaptchaService;

import io.wcm.testing.mock.aem.junit.AemContext;

public class CaptchaServiceImplTest {
    
private static final Object SERVICE_URL = "https://www.google.com/recaptcha/api/siteverify";

private static final Object CAPTCHA_SERVER_SIDE_KEY = "6LeINTAUAAAAAN9ifWbJi4OwKe8U06w89fSw0Pke";

private static final Object CAPTCHA_CLIENT_SIDE_KEY = "6LeINTAUAAAAAFSHsgFzbeEYhqopZpC5xYFKAVE0";

private CaptchaServiceImpl captchaServiceImpl = new CaptchaServiceImpl();
    
    Map<String, Object> _config;
    
    @Rule
    public final AemContext aemContext = new AemContext();
    
    @Before
    public void setUp() throws Exception {
        _config = new HashMap<String, Object>();
        _config.put("service.url", SERVICE_URL);
        _config.put("captcha.service.serversidekey", CAPTCHA_SERVER_SIDE_KEY);
        _config.put("captcha.service.clientsidekey", CAPTCHA_CLIENT_SIDE_KEY);
        aemContext.registerService(CaptchaService.class, captchaServiceImpl);
        captchaServiceImpl = aemContext.registerInjectActivateService(captchaServiceImpl, _config);
        captchaServiceImpl.activate(_config);
    }
    
    @Test
    public void testverify() throws Exception {
        assertEquals(false, captchaServiceImpl.verify("sjgsadjsavdnsadvn12vjavdsjsad"));
        assertEquals("6LeINTAUAAAAAFSHsgFzbeEYhqopZpC5xYFKAVE0", captchaServiceImpl.getClientSideKey());
    }
    
}
