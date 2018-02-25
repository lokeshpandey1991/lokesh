package com.roche.pharma.customerportal.core.mock;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.sling.event.jobs.Job;

public class MockJobImpl implements Job {
    
    private Map<String, String> jobMap = new HashMap<String, String>();
    
    public MockJobImpl(Map<String, String> props) {
        jobMap = props;
    }
    
    @Override
    public String getTopic() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Object getProperty(String name) {
        // TODO Auto-generated method stub
        return jobMap.get(name);
    }
    
    @Override
    public Set<String> getPropertyNames() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public <T> T getProperty(String name, Class<T> type) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public <T> T getProperty(String name, T defaultValue) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public int getRetryCount() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public int getNumberOfRetries() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public String getQueueName() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getTargetInstance() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Calendar getProcessingStarted() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Calendar getCreated() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getCreatedInstance() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public JobState getJobState() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Calendar getFinishedDate() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getResultMessage() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String[] getProgressLog() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public int getProgressStepCount() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public int getFinishedProgressStep() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public Calendar getProgressETA() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
