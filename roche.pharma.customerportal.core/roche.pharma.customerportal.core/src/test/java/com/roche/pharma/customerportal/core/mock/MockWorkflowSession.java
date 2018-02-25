package com.roche.pharma.customerportal.core.mock;

import java.security.AccessControlException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.collection.util.ResultSet;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.InboxItem;
import com.adobe.granite.workflow.exec.Participant;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.filter.InboxItemFilter;
import com.adobe.granite.workflow.exec.filter.WorkItemFilter;
import com.adobe.granite.workflow.model.VersionException;
import com.adobe.granite.workflow.model.WorkflowModel;
import com.adobe.granite.workflow.model.WorkflowModelFilter;

public class MockWorkflowSession implements WorkflowSession {
    
    private ResourceResolver resourceResolver;
    
    public MockWorkflowSession(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }
    
    @Override
    public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
        AdapterType adapted = null;
        if (adapted == null && ResourceResolver.class.equals(type)) {
            adapted = (AdapterType) resourceResolver;
        }
        return adapted;
    }
    
    @Override
    public void complete(WorkItem arg0, Route arg1) throws WorkflowException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public WorkflowModel createNewModel(String arg0) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public WorkflowModel createNewModel(String arg0, String arg1) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void delegateWorkItem(WorkItem arg0, Participant arg1) throws WorkflowException, AccessControlException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void deleteModel(String arg0) throws WorkflowException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void deployModel(WorkflowModel arg0) throws WorkflowException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public ResultSet<InboxItem> getActiveInboxItems(long arg0, long arg1, InboxItemFilter arg2)
            throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ResultSet<InboxItem> getActiveInboxItems(long arg0, long arg1, String arg2, InboxItemFilter arg3)
            throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public WorkItem[] getActiveWorkItems() throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ResultSet<WorkItem> getActiveWorkItems(long arg0, long arg1) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ResultSet<WorkItem> getActiveWorkItems(long arg0, long arg1, WorkItemFilter arg2) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public WorkItem[] getAllWorkItems() throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ResultSet<WorkItem> getAllWorkItems(long arg0, long arg1) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Workflow[] getAllWorkflows() throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<Route> getBackRoutes(WorkItem arg0, boolean arg1) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Iterator<Participant> getDelegates(WorkItem arg0) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<HistoryItem> getHistory(Workflow arg0) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public WorkflowModel getModel(String arg0) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public WorkflowModel getModel(String arg0, String arg1) throws WorkflowException, VersionException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public WorkflowModel[] getModels() throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public WorkflowModel[] getModels(WorkflowModelFilter arg0) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ResultSet<WorkflowModel> getModels(long arg0, long arg1) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ResultSet<WorkflowModel> getModels(long arg0, long arg1, WorkflowModelFilter arg2) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<Route> getRoutes(WorkItem arg0, boolean arg1) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public WorkItem getWorkItem(String arg0) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Workflow getWorkflow(String arg0) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Workflow[] getWorkflows(String[] arg0) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ResultSet<Workflow> getWorkflows(String[] arg0, long arg1, long arg2) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean isSuperuser() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void logout() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public WorkflowData newWorkflowData(String arg0, Object arg1) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void restartWorkflow(Workflow arg0) throws WorkflowException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void resumeWorkflow(Workflow arg0) throws WorkflowException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public Workflow startWorkflow(WorkflowModel arg0, WorkflowData arg1) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Workflow startWorkflow(WorkflowModel arg0, WorkflowData arg1, Map<String, Object> arg2)
            throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void suspendWorkflow(Workflow arg0) throws WorkflowException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void terminateWorkflow(Workflow arg0) throws WorkflowException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void updateWorkflowData(Workflow arg0, WorkflowData arg1) {
        // TODO Auto-generated method stub
        
    }
    
}
