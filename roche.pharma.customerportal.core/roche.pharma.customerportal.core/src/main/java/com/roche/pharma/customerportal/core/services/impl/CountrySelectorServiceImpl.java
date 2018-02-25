package com.roche.pharma.customerportal.core.services.impl;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;

import com.roche.pharma.customerportal.core.services.CountrySelectorService;

/**
 * The Class ConfigurationServiceImpl provides redirectURL's and Mapping Path
 * @version 1.0
 * @author vrawa6
 */
@Service(value = CountrySelectorService.class)
@Component(immediate = true, metatype = true, label = "Country Selector Configuration Service")
@Properties({
        @Property(name = "service.description", value = "Country Selector Configuration Service"),
        @Property(name = "roche.country.selector.mapping.path", label = "Tag path", description = "Set the Tag Path",
                value = "/etc/tags/customerportal/continent"),
        @Property(name = "roche.country.selector.redirect.url", label = "Country Redirect URL  via : and || ",
                description = "e.g. us:en||/content/customerportal/us/en/home", cardinality = Integer.MAX_VALUE)
})
public class CountrySelectorServiceImpl implements CountrySelectorService {
    
    /** The redirect URL */
    private Map<String, String> redirectURL;
    
    private static final String SEPARATOR = "\\|\\|";
    
    private String mappingpath;
    
    /**
     * Activate
     * @param ctx component context
     */
    @Activate
    public void activate(final ComponentContext ctx) {
        @SuppressWarnings("unchecked")
        final Dictionary<String, Object> props = ctx.getProperties();
        redirectURL = getFilteredList(props, "roche.country.selector.redirect.url");
        mappingpath = props.get("roche.country.selector.mapping.path").toString();
    }
    
    /**
     * Gets the Redirect URL
     */
    @Override
    public Map<String, String> getRedirectURL() {
        return redirectURL;
    }
    
    /**
     * Gets the Redirect URL
     */
    @Override
    public String getMappingPath() {
        return mappingpath;
    }
    
    private Map<String, String> getFilteredList(final Dictionary<String, Object> props, final String propName) {
        final Object prop = props.get(propName);
        final Map<String, String> map = new HashMap<>();
        if (prop != null) {
            final String[] list = prop instanceof String[] ? (String[]) prop : prop.toString().split("\\s+");
            for (final String item : list) {
                final String[] parts = item.split(SEPARATOR);
                final String item1 = StringUtils.isNotBlank(parts[0]) ? parts[0] : "";
                final String item2 = StringUtils.isNotBlank(parts[1]) ? parts[1] : "";
                map.put(item1, item2);
            }
        }
        return map;
    }
    
}
