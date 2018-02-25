package com.roche.pharma.customerportal.core.mock;

import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;

public class MockWorkflowData implements WorkflowData {
    
    String payload;
    
    MetaDataMap metaMap;
    
    public MockWorkflowData(String payload, MetaDataMap metaMap) {
        this.payload = payload;
        this.metaMap = metaMap;
    }
    
    @Override
    public MetaDataMap getMetaDataMap() {
        // TODO Auto-generated method stub
        return metaMap;
    }
    
    @Override
    public Object getPayload() {
        // TODO Auto-generated method stub
        return payload;
    }
    
    @Override
    public String getPayloadType() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
