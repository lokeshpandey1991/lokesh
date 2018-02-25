package com.roche.pharma.customerportal.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.roche.pharma.customerportal.core.beans.CarouselBean;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is model class for carousel Component.
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = CarouselModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")

public class CarouselModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/carousel";
    
    @Self
    private Resource resource;
    
    @ValueMapValue
    private String secTitle;
    
    @ValueMapValue
    private String buttonTitle;
    
    @ValueMapValue
    private String[] carouselData;
    
    private List<CarouselBean> assetList = new ArrayList<>();
    
    private String componentName;
    
    @PostConstruct
    protected void postConstruct() {
        assetList = getMultifieldValue(carouselData, assetList);
    }
    
    /**
     * This method return the list of data added into multi-field
     * @param jsonArr
     * @param assetList
     * @return
     */
    private List<CarouselBean> getMultifieldValue(final String[] jsonArr, final List<CarouselBean> assetList) {
        if (jsonArr != null) {
            for (final String json : jsonArr) {
                final CarouselBean carouselBean = CommonUtils.getMultifield(json, CarouselBean.class);
                setPageContentType(carouselBean);
                carouselBean.setLinkPath(CommonUtils.getPagepathWithExtension(carouselBean.getLinkPath()));
                assetList.add(carouselBean);
                componentName = CommonUtils.getComponentName(resource);
            }
        }
        
        return assetList;
    }
    
    /**
     * This method set page content type if page is internal page
     * @param pageLinkBean
     */
    private void setPageContentType(final CarouselBean bean) {
        final String link = bean.getLinkPath();
        if (CommonUtils.isInternalLink(link) && !CommonUtils.isAssetPath(link)) {
            bean.setContentType(CommonUtils.getPageTypeProperty(link, resource.getResourceResolver()));
        }
    }
    
    public String getComponentName() {
        return componentName;
    }
    
    /**
     * @return secTitle
     */
    public String getSecTitle() {
        return secTitle;
    }
    
    /**
     * @return carouselData
     */
    public String[] getCarouselData() {
        return null == carouselData ? null : carouselData.clone();
    }
    
    /**
     * @return assetList
     */
    public List<CarouselBean> getAssetList() {
        return new ArrayList<CarouselBean>(assetList);
    }
    
    /**
     * @return buttonTitle
     */
    public String getButtonTitle() {
        return buttonTitle;
    }
    
}
