package com.roche.pharma.customerportal.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.sling.SlingFilter;
import org.apache.felix.scr.annotations.sling.SlingFilterScope;
import org.apache.sling.api.SlingHttpServletRequest;

@SlingFilter(label = "Assay Menu Filter", description = "Assay Menu Filter", metatype = false, generateService = true,
        order = 0, scope = SlingFilterScope.REQUEST)
public class AssayMenuFilter implements Filter {
    
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final String path = ((SlingHttpServletRequest) request).getRequestURI();
        if (StringUtils.isNotBlank(path) && path.startsWith("/content/customerportal")
                && path.contains("/instrument/INS_")) {
            final String pathURL = path.substring(0, path.lastIndexOf("/"));
            final String selector = path.substring(path.lastIndexOf("/") + 1);
            final RequestDispatcher rd = request.getRequestDispatcher(pathURL + "." + selector);
            rd.forward(request, response);
        }
        chain.doFilter(request, response);
        
    }
    
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }
}
