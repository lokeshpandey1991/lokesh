package com.roche.pharma.customerportal.core.annotations;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.spi.DisposalCallbackRegistry;
import org.apache.sling.models.spi.Injector;
import org.apache.sling.models.spi.injectorspecific.AbstractInjectAnnotationProcessor2;
import org.apache.sling.models.spi.injectorspecific.InjectAnnotationProcessor2;
import org.apache.sling.models.spi.injectorspecific.StaticInjectAnnotationProcessorFactory;
import org.osgi.framework.Constants;

import com.roche.pharma.customerportal.core.annotations.interfaces.TextProcessing;

/**
 * This Custom Sling Model Injector injects the string value containing character like ® as superscript.
 * Example:- @TextProcessing(sup={"®"})
 */
@Component
@Service
@Property(name = Constants.SERVICE_RANKING, intValue = 4070)
public class TextProcessingAnnotationInjector implements Injector, StaticInjectAnnotationProcessorFactory {

    /*
     * (non-Javadoc)
     * @see
     * org.apache.sling.models.spi.injectorspecific.StaticInjectAnnotationProcessorFactory#createAnnotationProcessor(
     * java.lang.reflect.AnnotatedElement)
     */
    @Override
    public InjectAnnotationProcessor2 createAnnotationProcessor(AnnotatedElement element) {

        TextProcessing annotation = element.getAnnotation(TextProcessing.class);
        if (null != annotation) {
            return new TextProcessingAnnotationProcessor(annotation);
        }
        
        return null;
    }
    
    /**
     * The Class TextProcessingAnnotationProcessor.
     */
    private static class TextProcessingAnnotationProcessor extends AbstractInjectAnnotationProcessor2 {
        
        /** The annotation. */
        private TextProcessing annotation;

        /**
         * Instantiates a new text processing annotation processor.
         * @param annotation the annotation
         */
        TextProcessingAnnotationProcessor(TextProcessing annotation) {
            this.annotation = annotation;
        }
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.sling.models.spi.Injector#getName()
     */
    @Override
    public String getName() {

        return "textProcesing";
    }

    /*
     * (non-Javadoc)
     * @see org.apache.sling.models.spi.Injector#getValue(java.lang.Object, java.lang.String, java.lang.reflect.Type,
     * java.lang.reflect.AnnotatedElement, org.apache.sling.models.spi.DisposalCallbackRegistry)
     */
    @Override
    public Object getValue(final Object adaptable, final String fieldName, final Type type,
            final AnnotatedElement annotatedElement, final DisposalCallbackRegistry disposalCallbackRegistry) {

        if (adaptable instanceof Resource && type.equals(String.class)) {

            TextProcessing annotation = annotatedElement.getAnnotation(TextProcessing.class);
            final Resource resource = (Resource) adaptable;
            ValueMap valueMap = resource.getValueMap();
            String value = null != valueMap.get(fieldName) ? valueMap.get(fieldName).toString() : null;
            if (StringUtils.isNotBlank(value)) {
                return processSupValue(value, annotation);
            }

        }

        return null;
    }

    /**
     * Process sup value.
     * @param value the value
     * @param annotation the annotation
     * @return the string
     */
    private String processSupValue(String value, TextProcessing annotation) {
        StringBuilder finalValue = new StringBuilder(value);
        for (final String subValue : annotation.sup()) {
            final StringBuilder processedValue = new StringBuilder();
            String[] splitedValues = StringUtils.split(finalValue.toString(), subValue);
            for (final String splitedValue : splitedValues) {
                if (processedValue.length() != 0) {
                    processedValue.append(annotation.suppre());
                    processedValue.append(subValue);
                    processedValue.append(annotation.suppost());
                    
                }
                processedValue.append(splitedValue);
            }
            finalValue = processedValue;
        }
        return finalValue.toString();
    }

}
