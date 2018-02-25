package com.roche.pharma.customerportal.core.events;

import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextBuilder;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChange.ChangeType;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Rule;
import org.junit.Test;

import com.roche.pharma.customerportal.core.mock.MockJobManager;

public class RocheProductResourceChangeListnerTest {
	 
   private static JobManager jobmanager;
   
   private static RocheProductResourceChangeListner  rocheProductListner = new RocheProductResourceChangeListner();
	 
	 @Test
	 public void testFilter() throws ServletException, IOException{
		 ResourceChange change1 = new ResourceChange(ChangeType.ADDED, "/etc/commerce/products/customerportal", false, null, null, null);
		 ResourceChange change2 = new ResourceChange(ChangeType.CHANGED, "/etc/commerce/products/customerportal", false, null, null, null);
		 ResourceChange change3 = new ResourceChange(ChangeType.REMOVED, "/etc/commerce/products/customerportal", false, null, null, null);
		 List<ResourceChange> changes = new ArrayList<ResourceChange>();
		 changes.add(change1);
		 changes.add(change2);
		 changes.add(change3);
		 context.registerInjectActivateService(rocheProductListner);
		 rocheProductListner.onChange(changes);
	 }
	
	 @Rule
	 public final AemContext context = new AemContextBuilder(ResourceResolverType.JCR_MOCK).afterSetUp(SETUP_CALLBACK)
	            .build();
	 
	 private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
	        @Override
	        public void execute(AemContext context)
	                throws PersistenceException, IOException, javax.jcr.LoginException, RepositoryException { 	        		        	
	        	jobmanager = new MockJobManager();
	            context.registerService(JobManager.class, jobmanager);
	        	context.requestPathInfo().setResourcePath("/content/customerportal/us/en/instrument/INS_123");

	        }
	 };
}
