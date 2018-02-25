package com.roche.pharma.customerportal.core.beans;

/**
 * This is language bean class.
 */
public class LanguageBean {
    
    private String languageCode;
    
    private String languageName;
    
    private String redirectPath;
    
    private String channel;
    
    public String getLanguageCode() {
        return languageCode;
    }
    
    public void setLanguageCode(final String languageCode) {
        this.languageCode = languageCode;
    }
    
    public String getLanguageName() {
        return languageName;
    }
    
    public void setLanguageName(final String languageName) {
        this.languageName = languageName;
    }
    
    public String getRedirectPath() {
        return redirectPath;
    }
    
    public void setRedirectPath(final String redirectPath) {
        this.redirectPath = redirectPath;
    }
    
    public String getChannel() {
        return channel;
    }
    
    public void setChannel(final String channel) {
        this.channel = channel;
    }
    
}
