package com.roche.pharma.customerportal.core.filter;

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.servlethelpers.MockRequestDispatcherFactory;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockFilterChain;
import com.roche.pharma.customerportal.core.mock.MockRequestDispatcherFactoryImpl;

public class AssayMenuFilterTest {
	
	 private static AssayMenuFilter assayMenuFilter = new AssayMenuFilter();
	 
	 FilterChain filterChain = new MockFilterChain();
	 
	 @Test
	 public void testFilter() throws ServletException, IOException{
		 assayMenuFilter.init(null);
		 assayMenuFilter.doFilter(context.request(), context.response(), filterChain);
		 assayMenuFilter.destroy();
	 }
	
	 @Rule
	 public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
	            .build();
	 
	 private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
	        @Override
	        public void execute(AemContext context)
	                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException { 
	        	
	        	MockRequestDispatcherFactory dispFactory = new MockRequestDispatcherFactoryImpl();
	        	context.request().setRequestDispatcherFactory(dispFactory);
	        	context.requestPathInfo().setResourcePath("/content/customerportal/us/en/instrument/INS_123");

	        }
	 };

}
