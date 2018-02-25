package com.roche.pharma.customerportal.core.annotations.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.spi.injectorspecific.InjectAnnotation;

@Target({
        ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
@InjectAnnotation
@Source("textProcesing")
public @interface TextProcessing {

    String[] sup();
    
    String suppre() default "";
    
    String suppost() default "";
    
    String[] sub() default "[]";
    
    String subpre() default "";
    
    String subpost() default "";
    
}
