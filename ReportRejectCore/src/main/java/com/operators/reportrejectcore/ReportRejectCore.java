package com.operators.reportrejectcore;

import android.util.Log;

import com.operators.reportrejectinfra.ErrorObjectInterface;
import com.operators.reportrejectinfra.ReportRejectNetworkBridgeInterface;
import com.operators.reportrejectinfra.ReportRejectPersistenceManagerInterface;
import com.operators.reportrejectinfra.SendReportRejectCallback;

/**
 * Created by Sergey on 08/08/2016.
 */
public class ReportRejectCore {
    private static final String LOG_TAG = ReportRejectCore.class.getSimpleName();
    private ReportRejectNetworkBridgeInterface mReportRejectNetworkBridgeInterface;
    private ReportRejectCallbackListener mReportRejectCallbackListener;
    private ReportRejectPersistenceManagerInterface mReportRejectPersistenceManagerInterface;

    public ReportRejectCore(ReportRejectNetworkBridgeInterface operatorNetworkBridgeInterface , ReportRejectPersistenceManagerInterface reportRejectPersistenceManagerInterface) {
        mReportRejectNetworkBridgeInterface = operatorNetworkBridgeInterface;
        mReportRejectPersistenceManagerInterface = reportRejectPersistenceManagerInterface;

    }

    public void registerListener(ReportRejectCallbackListener reportRejectCallbackListener) {
        mReportRejectCallbackListener = reportRejectCallbackListener;
    }

    public void unregisterListener() {
        mReportRejectCallbackListener = null;
    }

    public void sendReportReject(int rejectReasonId, int rejectReasonCause, double units, Double weight) {
        if (mReportRejectPersistenceManagerInterface != null) {
            mReportRejectNetworkBridgeInterface.sendReportReject(mReportRejectPersistenceManagerInterface.getSiteUrl(), mReportRejectPersistenceManagerInterface.getSessionId(),
                    String.valueOf(mReportRejectPersistenceManagerInterface.getMachineId())
                    , mReportRejectPersistenceManagerInterface.getOperatorId(), rejectReasonId, rejectReasonCause, units, weight, new SendReportRejectCallback() {
                        @Override
                        public void onSendReportSuccess() {
                            if (mReportRejectCallbackListener != null) {
                                mReportRejectCallbackListener.sendReportRejectSuccess();
                            }
                            else {
                                Log.w(LOG_TAG, "onSendReportSuccess() mReportRejectCallbackListener is null ");
                            }
                        }

                        @Override
                        public void onSendReportFailed(ErrorObjectInterface reason) {
                            if (mReportRejectCallbackListener != null) {
                                mReportRejectCallbackListener.sendReportRejectFailure(reason);
                            }
                            else {
                                Log.w(LOG_TAG, "onSendReportSuccess() mReportRejectCallbackListener is null ");
                            }
                        }
                    }, mReportRejectPersistenceManagerInterface.getTotalRetries(), mReportRejectPersistenceManagerInterface.getRequestTimeout());
        }
    }
}
