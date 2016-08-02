package com.operators.reportfieldsformachinecore;

import com.operators.reportfieldsformachinecore.interfaces.ReportFieldsForMachineUICallback;
import com.operators.reportfieldsformachinecore.polling.EmeraldJobBase;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachineNetworkBridgeInterface;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachinePersistenceManagerInterface;

/**
 * Created by Sergey on 02/08/2016.
 */
public class ReportFieldsForMachineCore {
    private static final String LOG_TAG = ReportFieldsForMachineCore.class.getSimpleName();
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

    }

    public void stopPolling() {

    }

    public void getReportFieldForMachine() {

    }

}
