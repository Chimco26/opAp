package com.operatorsapp.model;

public class SendRejectObject {
    private final String value;
    private final boolean isUnit;
    private final int selectedCauseId;
    private final int selectedReasonId;

    public SendRejectObject(String value, boolean isUnit, int selectedCauseId, int selectedReasonId) {
        this.value = value;
        this.isUnit = isUnit;
        this.selectedCauseId = selectedCauseId;
        this.selectedReasonId = selectedReasonId;
    }

    public String getValue() {
        return value;
    }

    public boolean isUnit() {
        return isUnit;
    }

    public int getSelectedCauseId() {
        return selectedCauseId;
    }

    public int getSelectedReasonId() {
        return selectedReasonId;
    }
}
