package com.operatorsapp.model.event;

public class ActivateJobEvent {
    String erpJobId;

    public ActivateJobEvent(String erpJobId) {
        this.erpJobId = erpJobId;
    }

    public String getErpJobId() {
        return erpJobId;
    }
}
