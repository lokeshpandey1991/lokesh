package com.roche.pharma.customerportal.core.mock;

import com.roche.pharma.customerportal.core.services.CaptchaService;

public class MockCaptchaService implements CaptchaService {
    
    @Override
    public boolean verify(String gRecaptchaResponse) {
        return true;
    }
    
    @Override
    public String getClientSideKey() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
