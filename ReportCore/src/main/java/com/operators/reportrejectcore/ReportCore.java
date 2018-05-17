package com.operators.reportrejectcore;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportrejectinfra.ReportRejectNetworkBridgeInterface;
import com.operators.reportrejectinfra.ReportPersistenceManagerInterface;
import com.operators.reportrejectinfra.SendReportCallback;
import com.operators.reportrejectinfra.SendReportRejectCallback;
import com.operators.reportrejectinfra.SendReportStopCallback;
import com.zemingo.logrecorder.ZLogger;

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

    public void sendReportReject(int rejectReasonId, int rejectReasonCause, Double units, Double weight, Integer jobId) {
        if (mReportPersistenceManagerInterface != null) {
            mReportRejectNetworkBridgeInterface.sendReportReject(mReportPersistenceManagerInterface.getSiteUrl(), mReportPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportPersistenceManagerInterface.getMachineId())
                    , mReportPersistenceManagerInterface.getOperatorId(), rejectReasonId, rejectReasonCause, units, weight, jobId, new SendReportRejectCallback() {
                        @Override
                        public void onSendReportSuccess() {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess();
                            } else {
                                ZLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendReportFailed(ErrorObjectInterface reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            } else {
                                ZLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
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
                        public void onSendReportSuccess() {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess();
                            } else {
                                ZLogger.w(LOG_TAG, "onApproveFirstItemSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendReportFailed(ErrorObjectInterface reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            } else {
                                ZLogger.w(LOG_TAG, "onApproveFirstItemSuccess() mReportCallbackListener is null ");
                            }
                        }
                    }, mReportPersistenceManagerInterface.getTotalRetries(), mReportPersistenceManagerInterface.getRequestTimeout());
        }
    }

    public void sendStopReport(int stopReasonId, int stopSubReasonId, int eventId, int jobId) {
        if (mReportPersistenceManagerInterface != null) {
            mReportRejectNetworkBridgeInterface.sendReportStop(mReportPersistenceManagerInterface.getSiteUrl(), mReportPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportPersistenceManagerInterface.getMachineId())
                    , mReportPersistenceManagerInterface.getOperatorId(), stopReasonId, stopSubReasonId, eventId, jobId, new SendReportStopCallback() {
                        @Override
                        public void onSendStopReportSuccess() {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess();
                            } else {
                                ZLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendStopReportFailed(ErrorObjectInterface reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            } else {
                                ZLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }
                    }, mReportPersistenceManagerInterface.getTotalRetries(), mReportPersistenceManagerInterface.getRequestTimeout());
        }

    }

    public void sendMultipleStopReport(int stopReasonId, int stopSubReasonId, long[] eventId, int jobId) {
        if (mReportPersistenceManagerInterface != null) {
            mReportRejectNetworkBridgeInterface.sendMultipleReportStop(mReportPersistenceManagerInterface.getSiteUrl(), mReportPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportPersistenceManagerInterface.getMachineId())
                    , mReportPersistenceManagerInterface.getOperatorId(), stopReasonId, stopSubReasonId, eventId, jobId, new SendReportStopCallback() {
                        @Override
                        public void onSendStopReportSuccess() {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess();
                            } else {
                                ZLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendStopReportFailed(ErrorObjectInterface reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            } else {
                                ZLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }
                    }, mReportPersistenceManagerInterface.getTotalRetries(), mReportPersistenceManagerInterface.getRequestTimeout());
        }

    }

    public void sendCycleUnitsReport(double unitsPerCycle, Integer jobId) {
        if (mReportPersistenceManagerInterface != null) {
            mReportRejectNetworkBridgeInterface.sendReportCycleUnits(mReportPersistenceManagerInterface.getSiteUrl(), mReportPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportPersistenceManagerInterface.getMachineId())
                    , mReportPersistenceManagerInterface.getOperatorId(), unitsPerCycle, jobId, new SendReportCallback() {
                        @Override
                        public void onSendReportSuccess() {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess();
                            } else {
                                ZLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendReportFailed(ErrorObjectInterface reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            } else {
                                ZLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }
                    }, mReportPersistenceManagerInterface.getTotalRetries(), mReportPersistenceManagerInterface.getRequestTimeout());
        }
    }

    public void sendInventoryReport(int packageTypeId, int units, Integer jobId) {
        if (mReportPersistenceManagerInterface != null) {
            mReportRejectNetworkBridgeInterface.sendReportInventory(mReportPersistenceManagerInterface.getSiteUrl(), mReportPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportPersistenceManagerInterface.getMachineId())
                    , mReportPersistenceManagerInterface.getOperatorId(), packageTypeId, units, jobId, new SendReportCallback() {
                        @Override
                        public void onSendReportSuccess() {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess();
                            } else {
                                ZLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendReportFailed(ErrorObjectInterface reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            } else {
                                ZLogger.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }
                    }, mReportPersistenceManagerInterface.getTotalRetries(), mReportPersistenceManagerInterface.getRequestTimeout());
        }
    }
}