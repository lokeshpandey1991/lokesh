package com.roche.pharma.customerportal.core.beans;

import java.util.List;

/**
 * This is bean class for Continent.
 * @author vrawat
 */
public class ContinentBean {
    
    private String continentCode;
    
    private String continentName;
    
    private List<CountryBean> countryBean;
    
    public String getContinentCode() {
        return continentCode;
    }
    
    public void setContinentCode(final String continentCode) {
        this.continentCode = continentCode;
    }
    
    public String getContinentName() {
        return continentName;
    }
    
    public void setContinentName(final String continentName) {
        this.continentName = continentName;
    }
    
    public List<CountryBean> getCountryBean() {
        return countryBean;
    }
    
    public void setCountryBean(final List<CountryBean> countryBean) {
        this.countryBean = countryBean;
    }
    
}
