package com.roche.pharma.customerportal.core.models;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.roche.pharma.customerportal.core.beans.LinksListBean;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This class is the model class for Error page component, errorPage.html calls this model class. This class returns the
 * author configured properties in form of Json/Java object
 * @version 1.0
 * @author rsha66
 */
@Model(adaptables = {
        Resource.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = ErrorComponentModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")

public class ErrorComponentModel {
    
    public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/errorcomponent";
    
    /* below variable are inject when model is called. */
    @Self
    private Resource resource;
    
    @ValueMapValue
    private String notificationTitle;
    
    @ValueMapValue
    private String notificationDescription;
    
    @ValueMapValue
    private String[] linksList;
    
    /* Component name variable */
    private String componentName;
    
    /* list for bean */
    private List<LinksListBean> links = new LinkedList<>();
    
    /**
     * This method is called once all the variable are injected one model is called.
     */
    @PostConstruct
    protected void postConstruct() {
        links = getMultifieldValue(linksList, links);
        componentName = CommonUtils.getComponentName(resource);
    }
    
    /**
     * return multifield in list object from JSON
     * @param jsonArr
     * @param linkList
     * @return
     */
    private List<LinksListBean> getMultifieldValue(String[] jsonArr, List<LinksListBean> linkList) {
        if (jsonArr != null) {
            for (final String json : jsonArr) {
                final LinksListBean linksListBean = CommonUtils.getMultifield(json, LinksListBean.class);
                linkList.add(linksListBean);
            }
        }
        return linkList;
    }
    
    /**
     * return the the list of Links with HTML extension
     * @param linkList
     * @return
     */
    private List<LinksListBean> getLinksWithHtml(List<LinksListBean> linkList) {
        final List<LinksListBean> newBean = new LinkedList<>();
        if (!linkList.isEmpty()) {
            for (final LinksListBean list : linkList) {
                final LinksListBean bean = new LinksListBean();
                bean.setPageType(CommonUtils.getPageTypeProperty(list.getLinkURL(), resource.getResourceResolver()));
                final String url = CommonUtils.isInternalLink(list.getLinkURL())
                        ? list.getLinkURL().concat(RocheConstants.DOT).concat(RocheConstants.HTML)
                        : list.getLinkURL();
                bean.setLinkURL(url);
                bean.setLinkText(list.getLinkText());
                newBean.add(bean);
            }
        }
        return newBean;
    }
    
    /**
     * this method called from AEM component
     * @return error page title
     */
    public String getNotificationTitle() {
        return notificationTitle;
    }
    
    /**
     * This method is called from AEM component
     * @return error page description
     */
    public String getNotificationDescription() {
        return notificationDescription;
    }
    
    /**
     * this method is called from AEM component
     * @return list of links on error page
     */
    public List<LinksListBean> getLinks() {
        return getLinksWithHtml(links);
    }
    
    /**
     * tihs method called from component for analytics
     * @return name of current component
     */
    public String getComponentName() {
        return componentName;
    }
    
}
