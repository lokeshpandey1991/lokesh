package com.roche.pharma.customerportal.core.mock;

import javax.servlet.RequestDispatcher;

import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.servlethelpers.MockRequestDispatcherFactory;

public class MockRequestDispatcherFactoryImpl implements MockRequestDispatcherFactory{

	@Override
	public RequestDispatcher getRequestDispatcher(String path,
			RequestDispatcherOptions options) {
		// TODO Auto-generated method stub
		return new MockRequestDispatcher();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(Resource resource,
			RequestDispatcherOptions options) {
		// TODO Auto-generated method stub
		return new MockRequestDispatcher();
	}

}
