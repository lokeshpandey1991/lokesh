package com.roche.pharma.customerportal.core.mock;

import io.wcm.testing.mock.aem.junit.AemContext;

public class MockRocheContent {
    
    final static String rocheBasePath = "/content/roche";
    
    public static void load(AemContext context) {
        
        context.load().json("/json/roche.json", rocheBasePath);
    }
    
    /**
     * Static method to load the context for productCategory Page and components
     * @param context
     */
    public static void loadProductCategory(AemContext context) {
        context.load().json("/json/productCategory.json", rocheBasePath);
        context.load().json("/json/tags.json", "/etc/tags");
    }
    
    public static void loadfile(AemContext context, String filePath, String contentPath) {
        context.load().json(filePath, contentPath);
    }
    
}
