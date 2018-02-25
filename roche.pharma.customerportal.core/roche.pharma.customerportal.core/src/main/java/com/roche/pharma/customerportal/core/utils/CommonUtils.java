package com.roche.pharma.customerportal.core.utils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.roche.pharma.customerportal.core.beans.MetaDataBean;
import com.roche.pharma.customerportal.core.beans.TagBean;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.exceptions.BusinessExecutionException;
import com.roche.pharma.customerportal.core.exceptions.IntegrationExecutionException;
import com.roche.pharma.customerportal.core.models.GlobalConfigurationsModel;
import com.roche.pharma.customerportal.core.models.LanguageConfigurationsModel;
import com.roche.pharma.customerportal.core.models.LanguageHeaderFooterModel;
import com.roche.pharma.customerportal.core.services.ConfigurationService;

/**
 * This is a common utility class to provide various utility methods to other classes.
 */
public final class CommonUtils {

    /** The Constant EXTERNAL. */
    private static final String EXTERNAL = "external";

    /** The Constant INTERNAL. */
    private static final String INTERNAL = "internal";

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(CommonUtils.class);

    /** The gson. */
    private static Gson gson;

    /**
     * Instantiates a new common utils.
     */
    /*
     * Constructor
     */
    private CommonUtils() {}

    /**
     * This method returns gson insatnce.
     * @return gson or null
     */
    public static Gson getGsonInstance() {
        if (gson != null) {
            return gson;
        }
        return new Gson();
    }

    /**
     * This method returns the bean object for corresponding JSON using gson.
     * @param <T> the generic type
     * @param json this is json given as a parameter to this method
     * @param classOfT This parameter can be any class for which json is required
     * @return object for the class given as parameter
     */
    public static <T> T getMultifield(final String json, final Class<T> classOfT) {
        if (StringUtils.isNotBlank(json) && classOfT != null) {
            try {
                return getGsonInstance().fromJson(json, (Type) classOfT);
            } catch (final JsonSyntaxException e) {
                LOG.error("Unable to parse json {}", json, e);
            }
        }
        return null;

    }

    /**
     * This method returns current page for give resource.
     * @param resource sling resource
     * @return Page page returned by this method or null
     */
    public static Page getCurrentPage(final Resource resource) {
        if (resource != null) {
            final PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
            if (pageManager != null) {
                return pageManager.getContainingPage(resource);
            }
        }
        return null;
    }


    /**
     * This method returns language page for the corresponding current page.
     * @param currentPage current page
     * @return language page
     */

    public static Page getRegionalLanguagePage(final Page currentPage) {
        if (currentPage != null) {
            return currentPage.getAbsoluteParent(RocheConstants.REGIONL_LANGUAGE_PAGE_LEVEL);
        }
        return null;
    }

    /**
     * This method returns regional page for the corresponding current page.
     * @param currentPage current page
     * @return regional page
     */
    public static Page getRegionalPage(final Page currentPage) {
        if (currentPage != null) {
            return currentPage.getAbsoluteParent(RocheConstants.REGIONL_PAGE_LEVEL);
        }
        return null;
    }

    /**
     * This method returns global header footer configurations authored in language page.
     * @param languagePage language page
     * @return language header footer model
     */
    public static LanguageHeaderFooterModel getGlobalHeaderFooterConfigurations(final Page languagePage) {
        if (languagePage != null && languagePage.getContentResource() != null) {
            final Resource resource = languagePage.getContentResource().getChild(
                    RocheConstants.HEADER_FOOTER_CONFIGURATIONS_RESOURCE_NAME);
            if (resource != null) {
                return resource.adaptTo(LanguageHeaderFooterModel.class);
            }
        }
        return null;
    }

    /**
     * This method returns language configurations authored in language page.
     * @param languagePage language page
     * @return language configurations model
     */
    public static LanguageConfigurationsModel getlanguageConfigurations(final Page languagePage) {
        if (languagePage != null && languagePage.getContentResource() != null) {
            final Resource resource = languagePage.getContentResource().getChild(
                    RocheConstants.LANGUAGE_CONFIGURATIONS_RESOURCE_NAME);
            if (resource != null) {
                return resource.adaptTo(LanguageConfigurationsModel.class);
            }
        }
        return null;
    }

    /**
     * This method returns language configurations authored in language page.
     * @param resource sling Resource
     * @return language configurations model
     */
    public static LanguageConfigurationsModel getlanguageConfigurations(final Resource resource) {
        return getlanguageConfigurations(getRegionalLanguagePage(getCurrentPage(resource)));
    }

    /**
     * This method returns Global configurations authored on Global page.
     * @param page cq Page
     * @return global configurations model
     */
    public static GlobalConfigurationsModel getGlobalConfigurations(final Page page) {
        final Page globalPage = page == null ? null : page.getAbsoluteParent(1);
        if (globalPage != null && globalPage.getContentResource() != null) {
            final Resource resource = globalPage.getContentResource().getChild(
                    RocheConstants.PAR + "/" + RocheConstants.GLOBAL_CONFIGURATIONS_RESOURCE_NAME);
            if (resource != null) {
                return resource.adaptTo(GlobalConfigurationsModel.class);
            }
        }
        return null;
    }

    /**
     * Gets the global configurations.
     * @param resource the resource
     * @return the global configurations
     */
    public static GlobalConfigurationsModel getGlobalConfigurations(final Resource resource) {
        return getGlobalConfigurations(getCurrentPage(resource));
    }

    /**
     * This method returns page locale default is en-US.
     * @param currentPage current page
     * @return local current locale
     */
    public static Locale getPageLocale(final Page currentPage) {
        if (currentPage != null) {
            return currentPage.getLanguage(false);
        }

        return new Locale.Builder().setLanguage(RocheConstants.ENGLISH_LANGUAGE_ISO_CODE)
                .setRegion(RocheConstants.US_ISO_CODE).build();
    }

    /**
     * This method will convert date in inputFormat to outputFormat.
     * @param date java Util Date
     * @param outputFormat output format
     * @return date in inputFormat to outputFormat
     */
    public static String getFormattedDate(final Date date, final DateFormat outputFormat) {
        String newDateValue = "";
        if (date != null) {
            try {
                newDateValue = outputFormat.format(date.getTime());
            } catch (final BusinessExecutionException e) {
                LOG.error("BusinessExecutionException parsing date {}", date.getTime(), e);
            }
        }
        return newDateValue;
    }

    /**
     * Initialization tag's title by tag's id.
     * @param cqTagIDs cqTagIDs is the array with tag's ids Locale of the page
     * @param locale java Util Locale
     * @param resource sling resource
     * @return list of tags
     */
    public static List<String> initCqTagsByIds(final String[] cqTagIDs, final Locale locale, final Resource resource) {

        final List<String> localTitle = new ArrayList<>();
        try {
            final TagManager tagManager = resource.getResourceResolver().adaptTo(TagManager.class);
            if (tagManager != null) {
                for (final String cqTagID : cqTagIDs) {
                    final Tag tag = tagManager.resolve(cqTagID);
                    final String title = tag == null ? null
                            : StringUtils.isNotBlank(tag.getLocalizedTitle(locale)) ? tag.getLocalizedTitle(locale)
                                    : tag.getTitle();
                    localTitle.add(title);
                }
            }

        } catch (final IntegrationExecutionException e) {
            LOG.error("CommonUtils:initCqTagsByIds::Exception:", e.getMessage(), e);
        }
        return localTitle;
    }

    /**
     * This method display tag's title as Business area by filtering the tags on basis of parent path.
     * @param cqTagIDs cqTagIDs is the array with tag's Id
     * @param locale is the parameter to be used
     * @param resource is the parameter to be used
     * @return list of filtered tags
     */
    public static List<String> filterCqTagsByArea(final String[] cqTagIDs, final Locale locale, final Resource resource) {

        final List<String> localTitle = new ArrayList<>();
        try {
            final TagManager tagManager = resource.getResourceResolver().adaptTo(TagManager.class);
            if (tagManager != null) {
                for (final String cqTagID : cqTagIDs) {
                    final Tag tag = tagManager.resolve(cqTagID);
                    checkTags(locale, localTitle, cqTagID, tag);
                }
            }

        } catch (final IntegrationExecutionException e) {
            LOG.error("CommonUtils:initCqTagsByIds::Exception:", e.getMessage(), e);
        }
        return localTitle;
    }

    /**
     * This method check tag's title as Business area by filtering the tags on basis of parent path.
     * @param locale is the parameter to be used
     * @param localTitle is the parameter to be used
     * @param cqTagID cqTagID is the array with tag's Id
     * @param tag the tag
     */
    private static void checkTags(final Locale locale, final List<String> localTitle, final String cqTagID,
            final Tag tag) {
        if (cqTagID.contains(RocheConstants.BUSINESS_AREA_TAG_PREFIX)) {
            final String title = tag == null ? null : StringUtils.isNotBlank(tag.getLocalizedTitle(locale)) ? tag
                    .getLocalizedTitle(locale) : tag.getTitle();
            localTitle.add(title);
        }
    }

    /**
     * Returns true if the path is external, otherwise false.
     * @param path - the path to be checked
     * @return true if the path is not an internal link and contains http, https or is a protocol relative url
     */
    public static boolean isExternalLink(final String path) {
        return StringUtils.isNotBlank(path) && !isInternalLink(path) && path.matches("^(http(s)?:)?//.*")
                || StringUtils.startsWith(path, "www.");
    }

    /**
     * Gets the link type.
     * @param link the link
     * @return the link type
     */
    public static String getLinkType(final String link) {
        return isExternalLink(link) ? EXTERNAL : INTERNAL;
    }

    /**
     * Returns true if the path is internal, otherwise false.
     * @param path - the path to be checked
     * @return true if the path starts with slash but not with double slash
     */

    public static boolean isInternalLink(final String path) {
        return StringUtils.isNotBlank(path) && path.matches("^/[^/].*");
    }

    /**
     * checks if the path is a assets' path or not.
     * @param path the resource path to check
     * @return true or false
     */
    public static boolean isAssetPath(final String path) {
        return isInternalLink(path) && path.startsWith("/content/dam");
    }

    /**
     * get localized tag data from Cq tags object.
     * @param tag cq tag
     * @param locale Java Util locale
     * @return tagBean
     */

    private static TagBean getLocalizedTagBean(final Tag tag, final Locale locale) {
        final TagBean tagBean = new TagBean();
        if (tag != null) {
            tagBean.setLocalTitle(StringUtils.isNotBlank(tag.getLocalizedTitle(locale)) ? tag.getLocalizedTitle(locale)
                    : tag.getTitle());
            tagBean.setTagName(tag.getName());
        }
        return tagBean;
    }

    /**
     * Initialization tag's title by tag's id.
     * @param cqTagID the tag id
     * @param locale Java Util locale
     * @param resource sling resource
     * @return roche tag bean
     */
    public static TagBean getTagBean(final String cqTagID, final Locale locale, final Resource resource) {

        TagBean tagBean = new TagBean();
        final TagManager tagManager = resource.getResourceResolver().adaptTo(TagManager.class);
        if (tagManager != null) {
            final Tag tag = tagManager.resolve(cqTagID);
            tagBean = getLocalizedTagBean(tag, locale);
        }
        return tagBean;
    }

    /**
     * This method returns service resolver based on parameter map.
     * @param resourceFactory ResourceResolverFactory
     * @param paramMap Java Util Map
     * @return sling resource resolver
     */
    public static ResourceResolver getResourceResolverFromSubService(final ResourceResolverFactory resourceFactory,
            final Map<String, Object> paramMap) {
        ResourceResolver resourceResolver = null;
        if (!paramMap.isEmpty()) {
            try {
                resourceResolver = resourceFactory.getServiceResourceResolver(paramMap);
                LOG.info("resourceResolver for user {}", resourceResolver.getUserID());
            } catch (final LoginException e) {
                LOG.error("Unable to fetch resourceResolver for subservice {} exception {}",
                        paramMap.get(ResourceResolverFactory.SUBSERVICE), e);
            }
        }
        return resourceResolver;
    }

    /**
     * This method checks if hideInNavigation is set for current page.
     * @param currentPage current page
     * @return true if page is having hide in nav property set to true
     */
    public static boolean isHideInNav(final Page currentPage) {
        return !(currentPage != null && !currentPage.getProperties().containsKey(NameConstants.PN_HIDE_IN_NAV));
    }

    /**
     * This method checks if hide in navigation is set for pagePath.
     * @param pagePath page path
     * @param resourceResolver sling ResourceResolver
     * @return true if page is having hide in nav property set to true
     */
    public static boolean isHideInNav(final String pagePath, final ResourceResolver resourceResolver) {
        boolean isHideInNav = true;
        if (StringUtils.isNotBlank(pagePath) && resourceResolver != null) {
            isHideInNav = isHideInNav(getCurrentPage(resourceResolver.getResource(pagePath)));
        }
        return isHideInNav;
    }

    /**
     * This method checks if link is internal/external is sets .html for internal link
     * @param internalPath imnternal path
     * @return path with extension html
     */
    public static String getPagepathWithExtension(final String internalPath) {
        if (isInternalLink(internalPath) && !isAssetPath(internalPath)) {
            return internalPath + RocheConstants.DOT + RocheConstants.HTML;
        }
        return internalPath;
    }

    /**
     * This method returns the error notification JSON object.
     * @param notificationTitle notification title
     * @param notificationDescription notification description
     * @param notificationCTAText notification CTA text
     * @param notificationCTALink notification CTA link
     * @param notificationType notification type
     * @return json object for error notification
     */
    public static JSONObject getErrorNotification(final String notificationTitle, final String notificationDescription,
            final String notificationCTAText, final String notificationCTALink, final String notificationType) {
        final JSONObject errorNotification = new JSONObject();
        try {
            errorNotification.put("notificationTitle", notificationTitle);
            errorNotification.put("notificationDescription", notificationDescription);
            errorNotification.put("notificationCTAText", notificationCTAText);
            errorNotification.put("notificationCTALink", notificationCTALink);
            errorNotification.put("notificationType", notificationType);
        } catch (final JSONException e) {
            LOG.error("Exception caught in getErrorNotification-" + e);
        }
        return errorNotification;
    }

    /**
     * Gets the component name.
     * @param resource the resource
     * @return the component name
     */
    public static String getComponentName(final Resource resource) {
        return StringUtils.substringAfterLast(resource.getResourceType(), "/");
    }

    /**
     * This method returns the pageType property of given page path.
     * @param pagePath page path
     * @param resourceResolver sling ResourceResolver
     * @return pageType page type
     */
    public static String getPageTypeProperty(final String pagePath, final ResourceResolver resourceResolver) {
        String pageType = StringUtils.EMPTY;
        if (resourceResolver != null && StringUtils.isNotBlank(pagePath)) {
            final Resource jcrResoure = resourceResolver.getResource(pagePath + "/jcr:content");
            if (null != jcrResoure) {
                pageType = CommonUtils.getStringProperty(jcrResoure, "pageType");
            }
        }
        return pageType;
    }

    /**
     * This method returns the value of String property.
     * @param resource sling resource
     * @param propertyName property name
     * @return property value
     */
    public static String getStringProperty(final Resource resource, final String propertyName) {

        final ValueMap properties = resource.adaptTo(ValueMap.class);
        if (properties != null && !properties.isEmpty() && properties.get(propertyName) != null) {
            return properties.get(propertyName).toString();
        }
        return StringUtils.EMPTY;
    }

    /**
     * Gets the service reference.
     * @param <T> the generic type
     * @param serviceClass the service class
     * @return the service reference
     */
    @SuppressWarnings("unchecked")
    public static <T> T getServiceReference(final Class<T> serviceClass) {
        T serviceRef;
        final BundleContext bundleContext = FrameworkUtil.getBundle(serviceClass).getBundleContext();
        final ServiceReference osgiRef = bundleContext.getServiceReference(serviceClass.getName());
        serviceRef = (T) bundleContext.getService(osgiRef);
        return serviceRef;
    }

    /**
     * Gets the dynamic media video url.
     * @param asset the asset
     * @param configurationServiceRef Configuration Service
     * @return the dynamic media video url
     */
    public static String getDynamicMediaVideoUrl(final Asset asset, final ConfigurationService configurationServiceRef) {
        String videoUrl = "";
        final String dynamicMediaVideoUrl = configurationServiceRef == null ? "" : configurationServiceRef
                .getVideoServiceUrl();
        if (StringUtils.isNotBlank(dynamicMediaVideoUrl)) {
            final Rendition rendition = asset.getRendition("cqdam.video.dm.avs.1260x720.2600k.mp4");
            final Resource resource = rendition == null ? getVideoRendition(asset) : rendition.getChild("jcr:content");
            final ValueMap valueMap = resource == null ? null : resource.getValueMap();
            final String renditionUrl = valueMap != null && valueMap.containsKey("dam:proxyUrl") ? valueMap.get(
                    "dam:proxyUrl").toString() : "";
            videoUrl = StringUtils.isNotBlank(renditionUrl) ? dynamicMediaVideoUrl + renditionUrl : asset.getPath();
        }
        return videoUrl;
    }

    /**
     * Gets the video rendition.
     * @param asset the asset
     * @return the video rendition
     */
    private static Resource getVideoRendition(final Asset asset) {
        Resource resource = null;
        final List<Rendition> renditionList = asset.getRenditions();
        if (!renditionList.isEmpty()) {
            final Rendition rendition = renditionList.get(0);
            resource = rendition == null ? null : rendition.getChild("jcr:content");
        }
        return resource;
    }

    /**
     * This method checks if hideInNavigation is set for current page.
     * @param currentPage current page
     * @return true if hide in search property is set to true
     */
    public static boolean isHideInSearch(final Page currentPage) {
        return !(currentPage != null && !currentPage.getProperties().containsKey(RocheConstants.HIDE_IN_SEARCH) && !currentPage
                .getProperties().containsKey(NameConstants.PN_HIDE_IN_NAV));
    }

    /**
     * This method returns Resolved Path with HTML extension.
     * @param path internal path
     * @param resource Resource object
     * @return true if hide in search property is set to true
     */
    public static String getResolvedPathWithHtml(final String path, final Resource resource) {
        if (CommonUtils.isInternalLink(path) && !isAssetPath(path)) {
            final String resolvedPath = resource.getResourceResolver().map(path);
            return resolvedPath + RocheConstants.DOT + RocheConstants.HTML;
        }
        return path;
    }

    /**
     * This process returns the country code.
     * @param resource the resource
     * @return the country code
     */
    public static String getCountryCode(final Resource resource) {
        final Page page = CommonUtils.getCurrentPage(resource);
        String countryCode = StringUtils.EMPTY;
        Page countryPage;
        if (page != null) {
            countryPage = page.getAbsoluteParent(RocheConstants.REGIONL_PAGE_LEVEL);
            countryCode = countryPage.getProperties().get(RocheConstants.COUNTRY_CODE) == null ? StringUtils.EMPTY
                    : countryPage.getProperties().get(RocheConstants.COUNTRY_CODE).toString().toLowerCase();
        }
        return countryCode;
    }

    /**
     * Checks if is captcha disabled.
     * @param resource the resource
     * @return true, if is captcha disabled
     */
    public static boolean isCaptchaDisabled(final Resource resource) {
        boolean captchaDisabled = getlanguageConfigurations(resource) == null ? false : getlanguageConfigurations(
                resource).isCaptchaDisabled();
        if (!captchaDisabled) {
            captchaDisabled = getGlobalConfigurations(resource) == null ? false : getGlobalConfigurations(resource)
                    .isCaptchaDisabled();
        }
        return captchaDisabled;
    }

    /**
     * This method returns the list of tags for which level1 and level2 meta data will be created.
     * @param basePath the base path of product tag
     * @return List of tags
     */
    public static List<String> getCategoryTags(final String basePath) {
        return Arrays.asList(basePath + RocheConstants.HEALTH_TOPIC_LAB, basePath
                + RocheConstants.HEALTH_TOPIC_CLINICIAN, basePath + RocheConstants.HEALTH_TOPIC_PATIENT, basePath
                + RocheConstants.PRODUCT_SOLUTION);
    }

    /**
     * This method returns the map of tags with meta data at SnP.
     * @param metaValues authored values
     * @param tagManger the tag manager
     * @return map of mapped values
     */
    public static Map<String, String> getMetaTagMapping(final String[] metaValues, final TagManager tagManger) {
        final Map<String, String> map = new HashMap<>();
        MetaDataBean metaBean = null;
        if (null != metaValues) {
            for (final String json : metaValues) {
                metaBean = getGsonInstance().fromJson(json, MetaDataBean.class);
                final Tag tag = tagManger.resolve(metaBean.getTagPath());
                if (null != tag) {
                    map.put(tag.getName(), metaBean.getMetaMap());
                }
            }
        }
        return map;
    }

    public static void dispatchCacheFlush(final String path, final String dispatcherFlushUrl) {

        LOG.info("{Path}::" + path + "Dispatcher Flush request on {URL}::" + dispatcherFlushUrl);
        final org.apache.http.impl.conn.PoolingClientConnectionManager cm = new org.apache.http.impl.conn.PoolingClientConnectionManager();
        final org.apache.http.impl.client.DefaultHttpClient proxyClient = new org.apache.http.impl.client.DefaultHttpClient(
                cm);

        final org.apache.http.client.methods.HttpPost post = new org.apache.http.client.methods.HttpPost(
                dispatcherFlushUrl);
        post.setHeader("CQ-Action", "Activate");
        post.setHeader("CQ-Handle", path);
        try {
            proxyClient.execute(post);
        } catch (final java.io.IOException e) {
            LOG.error("IOException in cleanDispatchCache()", e.getMessage(), e);
        }
    }
}
