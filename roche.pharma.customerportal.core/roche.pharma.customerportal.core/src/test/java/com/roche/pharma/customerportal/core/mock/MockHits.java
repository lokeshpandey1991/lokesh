package com.roche.pharma.customerportal.core.mock;

import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.day.cq.search.result.Hit;

public class MockHits implements Hit {

    String path;

    Resource resource;

    public MockHits(Resource resource, String path) {
        this.resource = resource;
        this.path = path;
    }

    @Override
    public String getExcerpt() throws RepositoryException {
        return null;
    }

    @Override
    public Map<String, String> getExcerpts() throws RepositoryException {
        return null;
    }

    @Override
    public long getIndex() {
        return 0;
    }

    @Override
    public Node getNode() throws RepositoryException {
        return resource.adaptTo(Node.class);
    }

    @Override
    public String getPath() throws RepositoryException {
        return path;
    }

    @Override
    public ValueMap getProperties() throws RepositoryException {
        return resource.getValueMap();
    }

    @Override
    public Resource getResource() throws RepositoryException {
        return resource;
    }

    @Override
    public double getScore() throws RepositoryException {
        return 0;
    }

    @Override
    public String getTitle() throws RepositoryException {
        return null;
    }

}
