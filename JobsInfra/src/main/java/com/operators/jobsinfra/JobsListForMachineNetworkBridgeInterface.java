package com.operators.jobsinfra;


public interface JobsListForMachineNetworkBridgeInterface {
  void  getJobsForMachine(String siteUrl, String sessionId, int machineId, GetJobsListForMachineCallback callback, int totalRetries, int specificRequestTimeout);
  void  startJobsForMachine(String siteUrl, String sessionId, int machineId,int JobId, StartJobForMachineCallback callback, int totalRetries, int specificRequestTimeout);

}
