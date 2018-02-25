package com.roche.pharma.customerportal.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * model class for Image Gallery component.
 * @author agu207
 */

@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = ImageGallery.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
/**
 * Sling model class to get the parameters
 */
public class ImageGallery {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/imagegallery";
    
    @ValueMapValue
    private String altText1;
    
    @ValueMapValue
    private String fileReference1;
    
    @ValueMapValue
    private String altText2;
    
    @ValueMapValue
    private String fileReference2;
    
    @ValueMapValue
    private String altText3;
    
    @ValueMapValue
    private String fileReference3;
    
    @ValueMapValue
    private String altText4;
    
    @ValueMapValue
    private String fileReference4;
    
    @ValueMapValue
    private String altText5;
    
    @ValueMapValue
    private String fileReference5;
    
    @ValueMapValue
    private String altText6;
    
    @ValueMapValue
    private String fileReference6;
    
    @ValueMapValue
    private String imageHeadline;
    
    /**
     * @return the altText1
     */
    public String getAltText1() {
        return altText1;
    }
    
    /**
     * @return the fileReference1
     */
    public String getFileReference1() {
        return fileReference1;
    }
    
    /**
     * @return the altText2
     */
    public String getAltText2() {
        return altText2;
    }
    
    /**
     * @return the fileReference2
     */
    public String getFileReference2() {
        return fileReference2;
    }
    
    /**
     * @return the altText3
     */
    public String getAltText3() {
        return altText3;
    }
    
    /**
     * @return the fileReference3
     */
    public String getFileReference3() {
        return fileReference3;
    }
    
    /**
     * @return the altText4
     */
    public String getAltText4() {
        return altText4;
    }
    
    /**
     * @return the fileReference4
     */
    public String getFileReference4() {
        return fileReference4;
    }
    
    /**
     * @return the altText5
     */
    public String getAltText5() {
        return altText5;
    }
    
    /**
     * @return the fileReference5
     */
    public String getFileReference5() {
        return fileReference5;
    }
    
    /**
     * @return the altText6
     */
    public String getAltText6() {
        return altText6;
    }
    
    /**
     * @return the fileReference6
     */
    public String getFileReference6() {
        return fileReference6;
    }
    
    /**
     * @return the imageHeadline
     */
    public String getImageHeadline() {
        return imageHeadline;
    }
    
}
