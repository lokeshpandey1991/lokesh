package com.roche.pharma.customerportal.core.mock;

import java.util.Collection;
import java.util.Map;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobBuilder;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.Queue;
import org.apache.sling.event.jobs.ScheduledJobInfo;
import org.apache.sling.event.jobs.Statistics;
import org.apache.sling.event.jobs.TopicStatistics;

public class MockJobManager implements JobManager {
    
    @Override
    public Job addJob(String arg0, Map<String, Object> arg1) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Collection<Job> findJobs(QueryType arg0, String arg1, long arg2, Map<String, Object>... arg3) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Job getJob(String arg0, Map<String, Object> arg1) {
        final Job job = null;
        return job;
    }
    
    @Override
    public Job getJobById(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Queue getQueue(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Iterable<Queue> getQueues() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Statistics getStatistics() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Iterable<TopicStatistics> getTopicStatistics() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean removeJobById(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public JobBuilder createJob(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Collection<ScheduledJobInfo> getScheduledJobs() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Collection<ScheduledJobInfo> getScheduledJobs(String arg0, long arg1, Map<String, Object>... arg2) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Job retryJobById(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void stopJobById(String arg0) {
        // TODO Auto-generated method stub
        
    }
    
}
