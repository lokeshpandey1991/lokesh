package com.roche.pharma.customerportal.core.beans;

import java.util.List;

/**
 * This is bean class for Country.
 * @author vrawat
 */
public class CountryBean {
    
    private String countryCode;
    
    private String countryName;
    
    private List<LanguageBean> languageBean;
    
    public String getCountryCode() {
        return countryCode;
    }
    
    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }
    
    public String getCountryName() {
        return countryName;
    }
    
    public void setCountryName(final String countryName) {
        this.countryName = countryName;
    }
    
    public List<LanguageBean> getLanguageBean() {
        return languageBean;
    }
    
    public void setLanguageBean(final List<LanguageBean> languageBean) {
        this.languageBean = languageBean;
    }
    
}
