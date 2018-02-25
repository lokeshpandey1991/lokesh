package com.roche.pharma.customerportal.core.models;

import java.util.HashMap;
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

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This is Meta Tags model class.
 * @author mhuss3
 */
@Model(adaptables = {
		SlingHttpServletRequest.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = MetaTagModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class MetaTagModel {
	public static final String RESOURCE_TYPE = "roche/pharma/customerportal/components/metaTag";
    
    /** The sling request. */
    @Self
    private SlingHttpServletRequest slingRequest;
    
    /** The resource. */
    private Resource resource;
    
    @ValueMapValue
    private String[] listingTag;
    
    @ValueMapValue
    @Named("cq:tags")
    private String[] cqTags;
    
    /** The new product. */
    @ValueMapValue
    private String showFeaturedProduct;
    
    private Locale pageLocale;
    
    private String pimTagPath;
    
    private String ssiUrl;
    
    private Boolean enableSSI = true;
    
    private String[] metaTagMapping;
    
    private static final String LEVEL_1 = "L1";
    
    private static final String LEVEL_2 = "L2";
    
    private static final String DATA_CTD = "data-ctd";
    
    private static final String DATA_CTV = "data-ctv";
    
    private static final String DATA_CTD1 = "data-ctd1";
    
    private static final String DATA_CTV1 = "data-ctv1";
    
    private static final String PRODUCTS = "products";
    
    private static final String SNP_PROXY_URL = "/searchnpromote";
    
    /**
     * Post construct.
     */
    @PostConstruct
    protected void postConstruct() {
        resource = slingRequest.getResource();
        final Page currentPage = CommonUtils.getCurrentPage(resource);
        pageLocale = CommonUtils.getPageLocale(currentPage);
        final GlobalConfigurationsModel globalConfigurations = CommonUtils.getGlobalConfigurations(resource);
        final LanguageConfigurationsModel languageModel = CommonUtils.getlanguageConfigurations(resource);
        pimTagPath = RocheConstants.TAG_PREFIX + RocheConstants.SLASH;
        if (globalConfigurations != null) {
            metaTagMapping = globalConfigurations.getMetaTagMap();
        }
        if (languageModel != null && StringUtils.isNotBlank(languageModel.getSearchUrl())) {
            ssiUrl = languageModel.getSearchUrl().replaceAll("http(s?)://", "").split("\\.")[0];
            ssiUrl = SNP_PROXY_URL
                    + RocheConstants.SLASH
                    + ssiUrl
                    + RocheConstants.SLASH
                    + (languageModel.getSearchCollection() == null ? pageLocale.toString() : languageModel
                            .getSearchCollection()) + RocheConstants.SLASH;
        }
    }
    
    /**
     * Gets if featured products are shown on page load.
     * @return showFeaturedProduct
     */
    public String getShowFeaturedProduct() {
        return null == showFeaturedProduct ? "false" : showFeaturedProduct;
    }
    
    /**
     * This method returns if category facets are required to be fetched using ssi.
     * @return enableSSI
     */
    public Boolean isEnableSSI() {
        return enableSSI;
    }
    
    /**
     * This method returns the complete proxy url for fetching category facets on PLP page.
     * @return ssiUrl
     */
    public String getSnPSSIUrl() {
        getListingMeta();
        return "<!--#include virtual=\"" + ssiUrl + ".json\" -->";
    }
    
    /**
     * Method to get localized tag title.
     * @param tag
     * @param locale
     * @return localized title
     */
    private String getLocalizedTagTitle(final Tag tag, final Locale locale) {
        return StringUtils.isNotBlank(tag.getLocalizedTitle(locale)) ? tag.getLocalizedTitle(locale) : tag.getTitle();
    }
    
    /**
     * This method returns the tag from the given path.
     * @param path
     * @param tagManager
     * @return tag
     */
    private Tag getTagFromPath(final String path, final TagManager tagManager) {
        return tagManager.resolve(path);
    }
    
    /**
     * This method reads the cq tags from page and provide the map for meta tags.
     * @return map for meta tags
     */
    public Map<String, String> getMetaDataValues() {
        final Map<String, String> map = new HashMap<>();
        if (null == resource.getResourceResolver() || null == pimTagPath) {
            return null;
        } else {
            final TagManager tagManager = resource.getResourceResolver().adaptTo(TagManager.class);
            final Map<String, String> mapping = CommonUtils.getMetaTagMapping(metaTagMapping, tagManager);
            final List<String> tagList = CommonUtils.getCategoryTags(pimTagPath);
            if (null == cqTags || null == tagManager) {
                return null;
            } else {
                for (final String cqTag : cqTags) {
                    final Tag tag = tagManager.resolve(cqTag);
                    if (tag != null && tag.getPath().contains(pimTagPath)) {
                        if (containsTag(tag.getPath(), tagList)) {
                            getMultiLevelTags(tag, map, tagManager, mapping);
                        } else {
                            getSingleLevelTags(tag, map, tagManager, mapping);
                        }
                    }
                }
            }
            return map;
        }
    }
    
    /**
     * This method checks if the tagId is present in taglist.
     * @param tagId
     * @param tagList
     * @return if tag is present in taglist
     */
    private boolean containsTag(final String tagId, final List<String> tagList) {
        for (final String tag : tagList) {
            if (tagId.contains(tag)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * This method returns the meta name and content for multilevel tags.
     * @param tag
     * @param map
     * @param tagManager
     */
    private void getMultiLevelTags(final Tag tag, final Map<String, String> map, final TagManager tagManager,
            final Map<String, String> mapping) {
        final String path = tag.getPath().replaceAll(pimTagPath, StringUtils.EMPTY);
        final String[] tagIds = path.split(RocheConstants.SLASH);
        if (tagIds.length >= RocheConstants.THREE) {
            final Tag childTag = getTagFromPath(pimTagPath + tagIds[0] + RocheConstants.SLASH + tagIds[1]
                    + RocheConstants.SLASH + tagIds[2], tagManager);
            map.put(getMapKeyValue(mapping, tagIds[0]) + LEVEL_2, getLocalizedTagTitle(childTag, pageLocale));
            map.put(getMapKeyValue(mapping, tagIds[0]) + LEVEL_1,
                    getLocalizedTagTitle(childTag.getParent(), pageLocale));
        } else if (tagIds.length >= RocheConstants.TWO) {
            final Tag childTag = getTagFromPath(pimTagPath + tagIds[0] + RocheConstants.SLASH + tagIds[1], tagManager);
            map.put(getMapKeyValue(mapping, tagIds[0]) + LEVEL_1, getLocalizedTagTitle(childTag, pageLocale));
        }
        
    }
    
    /**
     * This method returns the meta name and content for single level tags.
     * @param tag
     * @param map
     * @param tagManager
     */
    private void getSingleLevelTags(final Tag tag, final Map<String, String> map, final TagManager tagManager,
            final Map<String, String> mapping) {
        final String path = tag.getPath().replaceAll(pimTagPath, StringUtils.EMPTY);
        final String[] tagIds = path.split(RocheConstants.SLASH);
        if (tagIds.length >= RocheConstants.TWO) {
            final Tag childTag = getTagFromPath(pimTagPath + tagIds[0] + RocheConstants.SLASH + tagIds[1], tagManager);
            map.put(getMapKeyValue(mapping, tagIds[0]), getLocalizedTagTitle(childTag, pageLocale));
        }
    }
    
    /**
     * This is a test method for S&P nested facets It will be deleted after design is finalized and complete.
     * @return map
     */
    public Map<String, String> getListingMeta() {
        final Map<String, String> map = new HashMap<>();
        if (null == resource.getResourceResolver() || null == pimTagPath) {
            return null;
        } else {
            final TagManager tagManager = resource.getResourceResolver().adaptTo(TagManager.class);
            if (null == listingTag || null == tagManager) {
                return null;
            } else {
                final String cqTag = listingTag[0];
                final Map<String, String> mapping = CommonUtils.getMetaTagMapping(metaTagMapping, tagManager);
                final Tag tag = tagManager.resolve(cqTag);
                if (tag != null && tag.getPath().contains(pimTagPath)) {
                    final String path = tag.getPath().replaceAll(pimTagPath, StringUtils.EMPTY);
                    final String[] tagIds = path.split(RocheConstants.SLASH);
                    if (tagIds.length >= RocheConstants.THREE) {
                        final Tag childTag = getTagFromPath(pimTagPath + tagIds[0] + RocheConstants.SLASH + tagIds[1]
                                + RocheConstants.SLASH + tagIds[2], tagManager);
                        map.put(DATA_CTD1, getMapKeyValue(mapping, tagIds[0]) + LEVEL_2);
                        map.put(DATA_CTV1, getLocalizedTagTitle(childTag, pageLocale));
                        map.put(DATA_CTD, getMapKeyValue(mapping, tagIds[0]) + LEVEL_1);
                        map.put(DATA_CTV, getLocalizedTagTitle(childTag.getParent(), pageLocale));
                        enableSSI = false;
                    } else if (tagIds.length >= RocheConstants.TWO) {
                        final Tag childTag = getTagFromPath(pimTagPath + tagIds[0] + RocheConstants.SLASH + tagIds[1],
                                tagManager);
                        map.put(DATA_CTD, getMapKeyValue(mapping, tagIds[0]) + LEVEL_1);
                        map.put(DATA_CTV, getLocalizedTagTitle(childTag, pageLocale));
                        if (StringUtils.isNotBlank(ssiUrl) && !ssiUrl.contains(PRODUCTS)) {
                            ssiUrl = ssiUrl + PRODUCTS + RocheConstants.SLASH + map.get(DATA_CTD)
                                    + RocheConstants.SLASH + map.get(DATA_CTV).replaceAll(" ", "+");
                        }
                        if (null == childTag.listChildren() || !childTag.listChildren().hasNext()) {
                            enableSSI = false;
                        }
                    }
                }
            }
            return map;
        }
    }
    
    /**
     * This method return mapping from the Meta tag mapping.
     * @return value
     */
    private String getMapKeyValue(final Map<String, String> map, final String key) {
        String value = StringUtils.EMPTY;
        if (null == map || map.isEmpty()) {
            return key;
        }
        if (map.containsKey(key)) {
            value = map.get(key);
        }
        return StringUtils.isBlank(value) ? key : value;
    }
    
}
