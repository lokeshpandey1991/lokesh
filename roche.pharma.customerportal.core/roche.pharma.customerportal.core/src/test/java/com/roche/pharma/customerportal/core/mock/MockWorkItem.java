package com.roche.pharma.customerportal.core.mock;

import java.util.Date;

import com.adobe.granite.workflow.exec.Status;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowNode;

public class MockWorkItem implements WorkItem {

    private final WorkflowData workflowData;

    public MockWorkItem(final WorkflowData workflowData) {
        this.workflowData = workflowData;
    }

    @Override
    public WorkflowData getWorkflowData() {
        // TODO Auto-generated method stub
        return workflowData;
    }

    @Override
    public String getItemSubType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getItemType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Status getStatus() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MetaDataMap getMetaDataMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCurrentAssignee() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WorkflowNode getNode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Date getTimeEnded() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Date getTimeStarted() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Workflow getWorkflow() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getContentPath() {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public Date getDueTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Priority getPriority() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getProgressBeginTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDueTime(Date arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPriority(Priority arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProgressBeginTime(Date arg0) {
		// TODO Auto-generated method stub
		
	}

}
