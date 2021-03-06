package com.operators.reportfieldsformachinecore;


import com.example.common.StandardResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.oppapplog.OppAppLogger;
import com.operators.reportfieldsformachinecore.interfaces.ReportFieldsForMachineUICallback;
import com.operators.reportfieldsformachinecore.polling.EmeraldJobBase;
import com.operators.reportfieldsformachineinfra.GetReportFieldsForMachineCallback;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachineNetworkBridgeInterface;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachinePersistenceManagerInterface;

import ravtech.co.il.publicutils.JobBase;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sergey on 02/08/2016
 */
public class ReportFieldsForMachineCore {
    private static final String LOG_TAG = ReportFieldsForMachineCore.class.getSimpleName();
    private static final int INTERVAL_DELAY = 24;
    private static final int START_DELAY = 0;
    private ReportFieldsForMachineNetworkBridgeInterface mReportFieldsForMachineNetworkBridgeInterface;
    private ReportFieldsForMachinePersistenceManagerInterface mReportFieldsForMachinePersistenceManagerInterface;
    private ReportFieldsForMachineUICallback mReportFieldsForMachineUICallback;
    private EmeraldJobBase mJob;

    public ReportFieldsForMachineCore(ReportFieldsForMachineNetworkBridgeInterface reportFieldsForMachineNetworkBridgeInterface, ReportFieldsForMachinePersistenceManagerInterface reportFieldsForMachinePersistenceManagerInterface) {
        mReportFieldsForMachineNetworkBridgeInterface = reportFieldsForMachineNetworkBridgeInterface;
        mReportFieldsForMachinePersistenceManagerInterface = reportFieldsForMachinePersistenceManagerInterface;
    }

    public void registerListener(ReportFieldsForMachineUICallback reportFieldsForMachineUICallback) {
        mReportFieldsForMachineUICallback = reportFieldsForMachineUICallback;
    }

    public void unregisterListener() {
        mReportFieldsForMachineUICallback = null;
    }

    public void startPolling() {
        mJob = null;
        mJob = new EmeraldJobBase() {
            @Override
            protected void executeJob(final JobBase.OnJobFinishedListener onJobFinishedListener) {
                getReportFieldForMachine(onJobFinishedListener);
            }
        };

        mJob.startJob(START_DELAY, INTERVAL_DELAY, TimeUnit.HOURS);
    }

    public void stopPolling() {
        if (mJob != null) {
            mJob.stopJob();
        }
    }

    private void getReportFieldForMachine(final JobBase.OnJobFinishedListener onJobFinishedListener) {
        if (mReportFieldsForMachinePersistenceManagerInterface != null) {
            mReportFieldsForMachineNetworkBridgeInterface.getReportFieldsForMachine(mReportFieldsForMachinePersistenceManagerInterface.getSiteUrl(), mReportFieldsForMachinePersistenceManagerInterface.getSessionId(),
                    mReportFieldsForMachinePersistenceManagerInterface.getMachineId(), new GetReportFieldsForMachineCallback() {
                        @Override
                        public void onGetReportFieldsForMachineSuccess(ReportFieldsForMachine reportFieldsForMachine) {
                            if (reportFieldsForMachine != null) {
                                if (mReportFieldsForMachineUICallback != null) {
                                    mReportFieldsForMachineUICallback.onReportFieldsReceivedSuccessfully(reportFieldsForMachine);
                                }
                                else {
                                    OppAppLogger.w(LOG_TAG, "mReportFieldsForMachineUICallback is null");
                                }
                            }
                            else {
                                OppAppLogger.e(LOG_TAG, "reportFieldsForMachine is null");
                            }
                            if (onJobFinishedListener != null) {
                                onJobFinishedListener.onJobFinished();
                            }
                            else {
                                OppAppLogger.w(LOG_TAG, "onGetReportFieldsForMachineSuccess() onJobFinishedListener is null");
                            }
                        }

                        @Override
                        public void onGetReportFieldsForMachineFailed(StandardResponse reason) {
                            if (mReportFieldsForMachineUICallback != null) {
                                if (reason != null) {
                                    mReportFieldsForMachineUICallback.onReportFieldsReceivedSFailure(reason);
                                }
                            }
                            else {
                                OppAppLogger.w(LOG_TAG, "getReportFieldForMachine() mReportFieldsForMachineUICallback is null");
                            }
                            if (onJobFinishedListener != null) {
                                onJobFinishedListener.onJobFinished();
                            }
                        }
                    }, mReportFieldsForMachinePersistenceManagerInterface.getTotalRetries(), mReportFieldsForMachinePersistenceManagerInterface.getRequestTimeout());
        }
    }

}
