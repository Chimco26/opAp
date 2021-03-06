package com.operators.reportrejectcore;

import com.example.common.StandardResponse;
import com.example.oppapplog.OppAppLogger;
import com.operators.reportrejectinfra.ReportPersistenceManagerInterface;
import com.operators.reportrejectinfra.ReportRejectNetworkBridgeInterface;
import com.operators.reportrejectinfra.SendReportCallback;
import com.operators.reportrejectinfra.SendReportRejectCallback;
import com.operators.reportrejectinfra.SendReportStopCallback;

/**
 * Created by Sergey on 08/08/2016.
 */
public class ReportCore {
    private static final String LOG_TAG = ReportCore.class.getSimpleName();
    private ReportRejectNetworkBridgeInterface mReportRejectNetworkBridgeInterface;
    private ReportCallbackListener mReportCallbackListener;
    private ReportPersistenceManagerInterface mReportPersistenceManagerInterface;

    public ReportCore(ReportRejectNetworkBridgeInterface operatorNetworkBridgeInterface, ReportPersistenceManagerInterface reportPersistenceManagerInterface) {
        mReportRejectNetworkBridgeInterface = operatorNetworkBridgeInterface;
        mReportPersistenceManagerInterface = reportPersistenceManagerInterface;

    }

    public void registerListener(ReportCallbackListener reportCallbackListener) {
        mReportCallbackListener = reportCallbackListener;
    }

    public void unregisterListener() {
        mReportCallbackListener = null;
    }

    public void sendReportReject(int rejectReasonId, int rejectReasonCause, Double units, Double weight, Integer joshId) {
        if (mReportPersistenceManagerInterface != null) {
            String workerID = mReportPersistenceManagerInterface.getOperatorId();
            if (workerID != null && workerID.equals("0")) {
                workerID = "";
            }
            mReportRejectNetworkBridgeInterface.sendReportReject(mReportPersistenceManagerInterface.getSiteUrl(), mReportPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportPersistenceManagerInterface.getMachineId())
                    , workerID, rejectReasonId, rejectReasonCause, units, weight, joshId, new SendReportRejectCallback() {
                        @Override
                        public void onSendReportSuccess(StandardResponse o) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess(o);
                            } else {
                                OppAppLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendReportFailed(StandardResponse reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            } else {
                                OppAppLogger.w(LOG_TAG, "onSendReportFailed() mReportCallbackListener is null ");
                            }
                        }
                    }, mReportPersistenceManagerInterface.getTotalRetries(), mReportPersistenceManagerInterface.getRequestTimeout());
        }
    }

    public void sendApproveFirstItem(int rejectReasonId, int approvingTechnicianID, Integer jobId) {
        if (mReportPersistenceManagerInterface != null) {
            mReportRejectNetworkBridgeInterface.sendApproveFirstItem(mReportPersistenceManagerInterface.getSiteUrl(), mReportPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportPersistenceManagerInterface.getMachineId())
                    , mReportPersistenceManagerInterface.getOperatorId(), rejectReasonId, approvingTechnicianID, jobId, new SendReportCallback() {
                        @Override
                        public void onSendReportSuccess(StandardResponse o) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess(o);
                            } else {
                                OppAppLogger.w(LOG_TAG, "onApproveFirstItemSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendReportFailed(StandardResponse reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            } else {
                                OppAppLogger.w(LOG_TAG, "onApproveFirstItemSuccess() mReportCallbackListener is null ");
                            }
                        }
                    }, mReportPersistenceManagerInterface.getTotalRetries(), mReportPersistenceManagerInterface.getRequestTimeout());
        }
    }

    public void sendStopReport(int stopReasonId, int stopSubReasonId, long eventId, int joshId) {
        if (mReportPersistenceManagerInterface != null) {
            mReportRejectNetworkBridgeInterface.sendReportStop(mReportPersistenceManagerInterface.getSiteUrl(), mReportPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportPersistenceManagerInterface.getMachineId())
                    , mReportPersistenceManagerInterface.getOperatorId(), stopReasonId, stopSubReasonId, eventId, joshId, new SendReportStopCallback() {
                        @Override
                        public void onSendStopReportSuccess(StandardResponse o) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess(o);
                            } else {
                                OppAppLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendStopReportFailed(StandardResponse reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            } else {
                                OppAppLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }
                    }, mReportPersistenceManagerInterface.getTotalRetries(), mReportPersistenceManagerInterface.getRequestTimeout());
        }

    }

    public void sendMultipleStopReport(int stopReasonId, int stopSubReasonId, long[] eventId, int joshId, boolean byRootEvent, String notes) {
        if (mReportPersistenceManagerInterface != null) {
            mReportRejectNetworkBridgeInterface.sendMultipleReportStop(mReportPersistenceManagerInterface.getSiteUrl(), mReportPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportPersistenceManagerInterface.getMachineId())
                    , mReportPersistenceManagerInterface.getOperatorId(), stopReasonId, stopSubReasonId, eventId, joshId, new SendReportStopCallback() {
                        @Override
                        public void onSendStopReportSuccess(StandardResponse o) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess(o);
                            } else {
                                OppAppLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendStopReportFailed(StandardResponse reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            } else {
                                OppAppLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }
                    }, mReportPersistenceManagerInterface.getTotalRetries(), mReportPersistenceManagerInterface.getRequestTimeout(), byRootEvent, notes);
        }
    }

    public void sendCycleUnitsReport(double unitsPerCycle, Integer jobId) {
        if (mReportPersistenceManagerInterface != null) {
            mReportRejectNetworkBridgeInterface.sendReportCycleUnits(mReportPersistenceManagerInterface.getSiteUrl(), mReportPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportPersistenceManagerInterface.getMachineId())
                    , mReportPersistenceManagerInterface.getOperatorId(), unitsPerCycle, jobId, new SendReportCallback() {
                        @Override
                        public void onSendReportSuccess(StandardResponse o) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess(o);
                            } else {
                                OppAppLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendReportFailed(StandardResponse reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            } else {
                                OppAppLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }
                    }, mReportPersistenceManagerInterface.getTotalRetries(), mReportPersistenceManagerInterface.getRequestTimeout());
        }
    }

    public void sendReportFixUnits(double amount, Integer joshId) {
        if (mReportPersistenceManagerInterface != null) {
            mReportRejectNetworkBridgeInterface.sendReportFixUnits(mReportPersistenceManagerInterface.getSiteUrl(), mReportPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportPersistenceManagerInterface.getMachineId())
                    , mReportPersistenceManagerInterface.getOperatorId(), amount, joshId, new SendReportCallback() {
                        @Override
                        public void onSendReportSuccess(StandardResponse o) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess(o);
                            } else {
                                OppAppLogger.w(LOG_TAG, "sendReportFixUnitsSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendReportFailed(StandardResponse reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            } else {
                                OppAppLogger.w(LOG_TAG, "sendReportFixUnitsSuccess() mReportCallbackListener is null ");
                            }
                        }
                    }, mReportPersistenceManagerInterface.getTotalRetries(), mReportPersistenceManagerInterface.getRequestTimeout());
        }
    }

    public void sendInventoryReport(int packageTypeId, int units, Integer joshId, int numberOfBatch) {
        if (mReportPersistenceManagerInterface != null) {
            mReportRejectNetworkBridgeInterface.sendReportInventory(mReportPersistenceManagerInterface.getSiteUrl(), mReportPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportPersistenceManagerInterface.getMachineId())
                    , mReportPersistenceManagerInterface.getOperatorId(), packageTypeId, units, joshId, numberOfBatch, new SendReportCallback() {
                        @Override
                        public void onSendReportSuccess(StandardResponse o) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess(o);
                            } else {
                                OppAppLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendReportFailed(StandardResponse reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            } else {
                                OppAppLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }
                    }, mReportPersistenceManagerInterface.getTotalRetries(), mReportPersistenceManagerInterface.getRequestTimeout());
        }
    }
}