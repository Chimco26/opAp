package com.operators.reportrejectcore;

import android.util.Log;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportrejectinfra.ReportRejectNetworkBridgeInterface;
import com.operators.reportrejectinfra.ReportPersistenceManagerInterface;
import com.operators.reportrejectinfra.SendReportCallback;
import com.operators.reportrejectinfra.SendReportRejectCallback;
import com.operators.reportrejectinfra.SendReportStopCallback;

/**
 * Created by Sergey on 08/08/2016.
 */
public class ReportRejectCore {
    private static final String LOG_TAG = ReportRejectCore.class.getSimpleName();
    private ReportRejectNetworkBridgeInterface mReportRejectNetworkBridgeInterface;
    private ReportCallbackListener mReportCallbackListener;
    private ReportPersistenceManagerInterface mReportPersistenceManagerInterface;

    public ReportRejectCore(ReportRejectNetworkBridgeInterface operatorNetworkBridgeInterface, ReportPersistenceManagerInterface reportPersistenceManagerInterface) {
        mReportRejectNetworkBridgeInterface = operatorNetworkBridgeInterface;
        mReportPersistenceManagerInterface = reportPersistenceManagerInterface;

    }

    public void registerListener(ReportCallbackListener reportCallbackListener) {
        mReportCallbackListener = reportCallbackListener;
    }

    public void unregisterListener() {
        mReportCallbackListener = null;
    }

    public void sendReportReject(int rejectReasonId, int rejectReasonCause, double units, Double weight, Integer jobId) {
        if (mReportPersistenceManagerInterface != null) {
            mReportRejectNetworkBridgeInterface.sendReportReject(mReportPersistenceManagerInterface.getSiteUrl(), mReportPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportPersistenceManagerInterface.getMachineId())
                    , mReportPersistenceManagerInterface.getOperatorId(), rejectReasonId, rejectReasonCause, units, weight, jobId, new SendReportRejectCallback() {
                        @Override
                        public void onSendReportSuccess() {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess();
                            }
                            else {
                                Log.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendReportFailed(ErrorObjectInterface reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            }
                            else {
                                Log.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }
                    }, mReportPersistenceManagerInterface.getTotalRetries(), mReportPersistenceManagerInterface.getRequestTimeout());
        }
    }

    public void sendStopReport(int stopReasonId, int stopSubReasonId, Integer jobId) {
        if (mReportPersistenceManagerInterface != null) {
            mReportRejectNetworkBridgeInterface.sendReportStop(mReportPersistenceManagerInterface.getSiteUrl(), mReportPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportPersistenceManagerInterface.getMachineId())
                    , mReportPersistenceManagerInterface.getOperatorId(), stopReasonId, stopSubReasonId, jobId, new SendReportStopCallback() {
                        @Override
                        public void onSendStopReportSuccess() {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportSuccess();
                            }
                            else {
                                Log.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendStopReportFailed(ErrorObjectInterface reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            }
                            else {
                                Log.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
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
                            }
                            else {
                                Log.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendReportFailed(ErrorObjectInterface reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            }
                            else {
                                Log.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
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
                            }
                            else {
                                Log.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendReportFailed(ErrorObjectInterface reason) {
                            if (mReportCallbackListener != null) {
                                mReportCallbackListener.sendReportFailure(reason);
                            }
                            else {
                                Log.w(LOG_TAG, "onSendReportSuccess() mReportCallbackListener is null ");
                            }
                        }
                    }, mReportPersistenceManagerInterface.getTotalRetries(), mReportPersistenceManagerInterface.getRequestTimeout());
        }
    }
}