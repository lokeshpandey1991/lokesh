package com.roche.pharma.customerportal.core.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

import com.roche.pharma.customerportal.core.beans.ContinentBean;
import com.roche.pharma.customerportal.core.beans.CountryBean;
import com.roche.pharma.customerportal.core.beans.LanguageBean;
import com.roche.pharma.customerportal.core.constants.RocheConstants;
import com.roche.pharma.customerportal.core.utils.CommonUtils;

/**
 * This CountrySelectorModelHelper class is called from CountrySelectorModel and returns the list of Continent with
 * country, language and its associated data.
 * @version 1.2
 */
public final class CountrySelectorModelHelper {
    
    /**
     * I18NBASE Constant for Country Selector Model Helper
     */
    private static final String I18NBASE = "rdoe_countrySelector";
    /**
     * I18NCOUNTRY Constant for Country Selector Model Helper
     */
    private static final String I18NCOUNTRY = "country";
    /**
     * I18NCONTINENT Constant for Country Selector Model Helper
     */
    private static final String I18NCONTINENT = "continent";
    /**
     * I18NLANGUAGE Constant for Country Selector Model Helper
     */
    private static final String I18NLANGUAGE = "language";
    /**
     * Resource mappingResource for Country Selector Model Helper
     */
    private static volatile Resource mappingResource;
    /**
     * List continentList for Country Selector Model Helper
     */
    private static volatile List<ContinentBean> continentList;
    
    /**
     * Constructor for Country Selector Model Helper
     */
    private CountrySelectorModelHelper() {}
    
    /**
     * This method provide List of Continent and its country and language data
     * @param slingRequest final SlingHttpServletRequest
     * @param redirects final Map
     * @param mappingpath final String
     * @return continentList List for ContinentBean
     */
    public static List<ContinentBean> getCountryContinentList(final SlingHttpServletRequest slingRequest,
            final Map<String, String> redirects, final String mappingpath) {
        final Resource resource = slingRequest.getResource();
        mappingResource = slingRequest.getResourceResolver().getResource(mappingpath);
        if (null != resource.getResourceResolver().getResource(mappingpath)) {
            continentList = getList(redirects);
        }
        return continentList;
    }
    
    /**
     * This method provide Continent List having associated country data and language data. It also, add only those
     * languages which are having not blank redirect URL.
     * @param redirects final Map
     * @return continentList List for ContinentBean
     */
    public static List<ContinentBean> getList(final Map<String, String> redirects) {
        ContinentBean continentElement;
        continentList = new ArrayList<>();
        int continentIndex = 0;
        for (final Resource continentResource : mappingResource.getChildren()) {
            continentElement = new ContinentBean();
            final String continentName = continentResource.getName();
            continentElement.setContinentCode(continentName);
            continentElement.setContinentName(
                    I18NBASE + RocheConstants.DOT + I18NCONTINENT + RocheConstants.DOT + continentName);
            int countryIndex = 0;
            final List<CountryBean> countryList = new ArrayList<>();
            CountryBean countryElement;
            for (final Resource countryResource : continentResource.getChildren()) {
                countryElement = new CountryBean();
                final String countryName = countryResource.getName();
                countryElement.setCountryCode(countryName);
                countryElement
                        .setCountryName(I18NBASE + RocheConstants.DOT + I18NCOUNTRY + RocheConstants.DOT + countryName);
                final List<LanguageBean> languageList = new ArrayList<>();
                setLanguageList(redirects, countryResource, languageList, countryName);
                countryElement.setLanguageBean(languageList);
                if (!countryElement.getLanguageBean().isEmpty()) {
                    countryList.add(countryIndex, countryElement);
                    countryIndex++;
                }
            }
            continentElement.setCountryBean(countryList);
            if (!continentElement.getCountryBean().isEmpty()) {
                continentList.add(continentIndex, continentElement);
                continentIndex++;
            }
        }
        return continentList;
    }
    
    /**
     * This method provide locale which is taken from selector.If there is no selector then en as selector is returned
     * to the front end.
     * @param slingRequest final SlingHttpServletRequest
     * @return localeAvailable String locale
     */
    public static String getLocaleAvailable(final SlingHttpServletRequest slingRequest) {
        if (StringUtils.isNotBlank(slingRequest.getRequestPathInfo().getSelectors()[0])) {
            return slingRequest.getRequestPathInfo().getSelectors()[0];
        }
        return RocheConstants.ENGLISH_LANGUAGE_ISO_CODE;
    }
    
    /**
     * This method set Language List. If the redirect URL is blank then that language is not added in the list.Also, it
     * returns the i18n key for language name. Redirect URL is appended with html as extension, if it is internal URL
     * and does not add html extension in case of external URL.
     * @param redirects final Map
     * @param countryResource final Resource countryResource
     * @param countryName String countryName
     * @param languageList final List
     */
    public static void setLanguageList(final Map<String, String> redirects, final Resource countryResource,
            final List<LanguageBean> languageList, final String countryName) {
        int languageIndex = 0;
        LanguageBean languageElement;
        for (final Resource languageResource : countryResource.getChildren()) {
            languageElement = new LanguageBean();
            final String languageName = languageResource.getName();
            languageElement.setLanguageCode(languageName);
            languageElement
                    .setLanguageName(I18NBASE + RocheConstants.DOT + I18NLANGUAGE + RocheConstants.DOT + languageName);
            final String tempRedirectPath = redirects.get(countryName + RocheConstants.COLON + languageName);
            final String redirectPath = CommonUtils.getResolvedPathWithHtml(tempRedirectPath, languageResource);
            languageElement.setChannel(countryName + RocheConstants.SLASH + languageName);
            languageElement.setRedirectPath(redirectPath);
            if (StringUtils.isNotBlank(redirectPath)) {
                languageList.add(languageIndex, languageElement);
                languageIndex++;
            }
        }
    }
    
}
