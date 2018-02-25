package com.roche.pharma.customerportal.core.mock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.adobe.granite.workflow.metadata.MetaDataMap;

public class MockMetaDataMap implements MetaDataMap {
    
    Map<String, Object> map = new HashMap<String, Object>();
    
    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean containsKey(Object key) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean containsValue(Object value) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public Object get(Object key) {
        // TODO Auto-generated method stub
        return map.get(key);
    }
    
    @Override
    public Object put(String key, Object value) {
        // TODO Auto-generated method stub
        map.put(key, value);
        return null;
    }
    
    @Override
    public Object remove(Object key) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void clear() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public Set<String> keySet() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Collection<Object> values() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public <T> T get(String arg0, Class<T> arg1) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public <T> T get(String arg0, T arg1) {
        if (map.get(arg0) != null) {
            return (T) map.get(arg0);
        }
        return arg1;
    }
    
}
