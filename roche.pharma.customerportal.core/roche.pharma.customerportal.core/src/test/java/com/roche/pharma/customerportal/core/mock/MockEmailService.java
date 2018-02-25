package com.roche.pharma.customerportal.core.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;
import javax.mail.internet.InternetAddress;

import org.apache.felix.scr.annotations.Service;

import com.adobe.acs.commons.email.EmailService;

import aQute.bnd.annotation.component.Component;

@Service(value = EmailService.class)
@Component(immediate = true)
public class MockEmailService implements EmailService {
    Map<String, String> map = new HashMap<String, String>();
    List<String> list = new ArrayList<String>();
    
    public MockEmailService(Map<String, String> arg1) {
        this.map = arg1;
    }
    
    @Override
    public List<InternetAddress> sendEmail(String arg0, Map<String, String> arg1, InternetAddress... arg2) {
        final List<InternetAddress> list = new ArrayList<InternetAddress>();
        return list;
    }
    
    @Override
    public List<String> sendEmail(String arg0, Map<String, String> arg1, String... arg2) {
        
        return list;
    }
    
    @Override
    public List<InternetAddress> sendEmail(String arg0, Map<String, String> arg1, Map<String, DataSource> arg2,
            InternetAddress... arg3) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<String> sendEmail(String arg0, Map<String, String> arg1, Map<String, DataSource> arg2, String... arg3) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
