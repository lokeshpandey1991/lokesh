package com.roche.pharma.customerportal.core.mock;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.commons.Externalizer;

public class MockExteranlizer implements Externalizer {
    
    @Override
    public String absoluteLink(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String absoluteLink(final SlingHttpServletRequest arg0, final String arg1, final String arg2) {
        // TODO Auto-generated method stub
        return arg2;
    }
    
    @Override
    public String absoluteLink(final ResourceResolver arg0, final String arg1, final String arg2) {
        // TODO Auto-generated method stub
        return arg2;
    }
    
    @Override
    public String authorLink(final ResourceResolver arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String authorLink(final ResourceResolver arg0, final String arg1, final String arg2) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String externalLink(final ResourceResolver arg0, final String arg1, final String arg2) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String externalLink(final ResourceResolver arg0, final String arg1, final String arg2, final String arg3) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String publishLink(final ResourceResolver arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String publishLink(final ResourceResolver arg0, final String arg1, final String arg2) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String relativeLink(final SlingHttpServletRequest arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

}
