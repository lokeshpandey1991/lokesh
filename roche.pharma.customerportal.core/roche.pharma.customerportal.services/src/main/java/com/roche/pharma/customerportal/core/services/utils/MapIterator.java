package com.roche.pharma.customerportal.core.services.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.WebTarget;

/**
 * The Class MapIterator.
 *
 * @author Avinash kumar
 */
public final class MapIterator {
    
    /**
     * Instantiates a new map iterator.
     */
    private MapIterator() {
        
    }
    
    /**
     * Fill query params.
     *
     * @param target the target
     * @param map the map
     * @return the web target
     */
    public static WebTarget fillQueryParams(final WebTarget target, final Map<String, Object> map) {
        WebTarget targetLocal = target;
        final Iterator<Entry<String, Object>> keyItr = map.entrySet().iterator();
        while (keyItr.hasNext()) {
            final Entry entry = keyItr.next();
            targetLocal = targetLocal.queryParam(entry.getKey().toString(), entry.getValue());
        }
        return targetLocal;
    }
    
}
