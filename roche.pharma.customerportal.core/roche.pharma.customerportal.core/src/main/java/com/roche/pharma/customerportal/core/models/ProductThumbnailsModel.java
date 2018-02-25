/**
 *
 */
package com.roche.pharma.customerportal.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.beans.ProductThumbnails;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * model class for marketingTile component which return the data of product defined the component to display the
 * thumbnail on page
 * @author agu207
 * @version 1.0
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = ProductThumbnailsModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class ProductThumbnailsModel {
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/productThumbnails";

    @Self
    private Resource resource;
    
    @ValueMapValue
    @Named("productPath1")
    private String productPathOne;
    
    @ValueMapValue
    @Named("productTitle1")
    private String productTitleOne;
    
    @ValueMapValue
    @Named("altText1")
    private String altTextOne;
    
    @ValueMapValue
    @Named("fileReference1")
    private String fileReferenceOne;
    
    @ValueMapValue
    @Named("productPath2")
    private String productPathTwo;
    
    @ValueMapValue
    @Named("productTitle2")
    private String productTitleTwo;
    
    @ValueMapValue
    @Named("altText2")
    private String altTextTwo;
    
    @ValueMapValue
    @Named("fileReference2")
    private String fileReferenceTwo;
    
    @ValueMapValue
    @Named("tagsId1")
    private String[] tagsIdOne;
    
    @ValueMapValue
    @Named("tagsId2")
    private String[] tagsIdTwo;
    
    private List<ProductThumbnails> productThumbnailsList = new ArrayList<>();
    
    @PostConstruct
    protected void postConstruct() {
        
        if (!StringUtils.isBlank(productPathOne)
                || checkAuthoredFields(productTitleOne, fileReferenceOne, altTextOne, tagsIdOne)) {
            
            addToList(productPathCheck(productPathOne, fileReferenceOne, altTextOne, productTitleOne, tagsIdOne));
        }
        
        if (!StringUtils.isBlank(productPathTwo)
                || checkAuthoredFields(productTitleTwo, fileReferenceTwo, altTextTwo, tagsIdTwo)) {
            
            addToList(productPathCheck(productPathTwo, fileReferenceTwo, altTextTwo, productTitleTwo, tagsIdTwo));
        }
        
    }
    
    /**
     * This method check if author has configured the dialog of the components with all the required fields in case of
     * missing product path
     * @param productTitle is the value to be used
     * @param fileReference is the value to be used
     * @param altText is the value to be used
     * @param tagsId is the value to be used
     * @return the boolean value of the method
     */
    public boolean checkAuthoredFields(String productTitle, String fileReference, String altText, String[] tagsId) {
        
        return !StringUtils.isBlank(productTitle) && checkImageFields(fileReference, altText)
                && (tagsId != null && tagsId.length != 0);
    }
    
    /**
     * This method check if author has configured the image reference path and alternate text fields in case of missing
     * product path
     * @param fileReference is the value to be used
     * @param altText is the value to be used
     * @return the boolean value of the method
     */
    public boolean checkImageFields(String fileReference, String altText) {
        
        return !StringUtils.isBlank(fileReference) && !StringUtils.isBlank(altText);
    }
    
    /**
     * adding productThumbnails object to list product
     * @param productThumbnails
     */
    private void addToList(final ProductThumbnails productThumbnails) {
        if (productThumbnails != null) {
            productThumbnailsList.add(productThumbnails);
        }
    }
    
    /**
     * This is product path check method.
     * @param productPath is used parameter
     * @param fileReference is used parameter
     * @param altText is used parameter
     * @param productTitle is used parameter
     * @param tagsIdParam is used parameter
     * @return object containing product details
     */
    
    public ProductThumbnails productPathCheck(final String productPath, final String fileReference,
            final String altText, final String productTitle, String[] tagsIdParam) {
        final Page currentPage = CommonUtils.getCurrentPage(resource);
        final Locale pageLocale = CommonUtils.getPageLocale(currentPage);
        final Resource articleResource = resource.getResourceResolver()
                .getResource(productPath + RocheConstants.FORWARD_SLASH + JcrConstants.JCR_CONTENT
                        + RocheConstants.FORWARD_SLASH + RocheConstants.PRODUCT);
        
        final ProductThumbnails productThumbnails = new ProductThumbnails();
        
        productThumbnails.setProductPath(CommonUtils.getPagepathWithExtension(productPath));
        productThumbnails.setProductTitle(checkProductName(articleResource, productTitle));
        productThumbnails.setFileReference(checkFileReference(articleResource, fileReference));
        productThumbnails.setAltText(checkAltText(articleResource, altText));
        String[] tagsId = tagsIdParam;
        if (tagsId == null) {
            final Resource targetPageResource = resource.getResourceResolver().getResource(productPath);
            if (targetPageResource != null) {
                final Page targetPage = targetPageResource.adaptTo(Page.class);
                if (targetPage != null) {
                    final Map<String, Object> pageValueMap = targetPage.getProperties();
                    tagsId = pageValueMap != null && pageValueMap.containsKey(RocheConstants.CQTAGS)
                            ? (String[]) pageValueMap.get(RocheConstants.CQTAGS)
                            : null;
                }
            }
        }
        if (tagsId != null) {
            productThumbnails.setTagsId(checktags(resource, tagsId, pageLocale));
        }
        return productThumbnails;
    }
    
    /**
     * Checks the tags added by the author in component and return the tags. It check the product page in case if not
     * authored
     * @param resource is the value to be used
     * @param tagsId is the value to be used
     * @param pageLocale is the value to be used
     * @return the tags associated to page
     */
    public List<String> checktags(final Resource resource, final String[] tagsId, final Locale pageLocale) {
        if (tagsId != null) {
            return CommonUtils.filterCqTagsByArea(tagsId, pageLocale, resource);
        }
        return Collections.emptyList();
    }
    
    /**
     * Checks file reference is author over written then update the value, return the filereference path from th
     * configured path in configured by author
     * @param resource is the value to be used
     * @param fileReference is the value to be used
     * @return the path of asset associated with product
     */
    public String checkFileReference(final Resource resource, String fileReference) {
        String newFileReference = fileReference;
        if (resource != null && StringUtils.isBlank(newFileReference)
                && resource.getValueMap().containsKey(RocheConstants.HERO_MEDIA)) {
            newFileReference = resource.getValueMap().get(RocheConstants.HERO_MEDIA, String.class);
        }
        
        return newFileReference;
    }
    
    /**
     * Checks altText is author over written then update the value
     * @param resource is the value to be used
     * @param altText is the value to be used
     * @return the alternate text for the asset
     */
    public String checkAltText(final Resource resource, String altText) {
        
        String newAltText = altText;
        if (resource != null && StringUtils.isBlank(newAltText)
                && resource.getValueMap().containsKey(RocheConstants.HERO_MEDIA_ALT_TEXT)) {
            newAltText = resource.getValueMap().get(RocheConstants.HERO_MEDIA_ALT_TEXT, String.class);
        }
        
        return newAltText;
    }
    
    /**
     * Checks articleTitle is author over written then update the value, it return the product title configured by
     * author and return title in case if not authored from product page
     * @param resource is the value to be used
     * @param productTitle is the value to be used
     * @return the title for the product
     */
    public String checkProductName(final Resource resource, String productTitle) {
        
        String newProductTitle = productTitle;
        if (resource != null && StringUtils.isBlank(newProductTitle)
                && resource.getValueMap().containsKey(RocheConstants.PRODUCT_NAME)) {
            newProductTitle = resource.getValueMap().get(RocheConstants.PRODUCT_NAME, String.class);
        }
        
        return newProductTitle;
    }
    
    /**
     * getter for productThumbnailsList
     * @return productThumbnailsList
     */
    public List<ProductThumbnails> getProductThumbnailsList() {
        return new ArrayList<>(productThumbnailsList);
    }
    
    /**
     * setter for productThumbnailsList
     * @param productThumbnailsList the productThumbnailsList to set
     */
    public void setProductThumbnailsList(List<ProductThumbnails> productThumbnailsList) {
        this.productThumbnailsList = productThumbnailsList;
    }
    
}
