package com.roche.pharma.customerportal.core.events;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChange.ChangeType;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.services.impl.EhCacheManagerServiceImpl;

public class RocheCacheCleanEventTest {
    
    private static RocheEhCacheCleanEvent rocheEhCacheCleanEvent = new RocheEhCacheCleanEvent();
    
    final static List<ResourceChange> changes = new ArrayList<ResourceChange>();
    
    @Test
    public void testFilter() throws ServletException, IOException {
        rocheEhCacheCleanEvent.onChange(changes);
    }
    
    @Rule
    public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
            .build();
    
    private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
        @Override
        public void execute(final AemContext context) throws PersistenceException, IOException,
                javax.jcr.LoginException, RepositoryException {
            
            final EhCacheManagerServiceImpl configurationServiceImpl = new EhCacheManagerServiceImpl();
            context.registerInjectActivateService(configurationServiceImpl);
            final Set<String> changedProps = new HashSet<String>();
            changedProps.add("relatedAssay");
            final ResourceChange change = new ResourceChange(ChangeType.CHANGED, "/content/customerportal/cacheClean",
                    false, null, changedProps, null);
            changes.add(change);
            context.registerInjectActivateService(rocheEhCacheCleanEvent);
            context.requestPathInfo().setResourcePath("/content/customerportal/us/en/instrument/INS_123");
        }
    };
}
