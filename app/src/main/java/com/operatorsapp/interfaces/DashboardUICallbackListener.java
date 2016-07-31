package com.operatorsapp.interfaces;

import com.operators.machinestatusinfra.ErrorObjectInterface;
import com.operators.machinestatusinfra.MachineStatus;

public interface DashboardUICallbackListener
{
  void onDeviceStatusChanged(MachineStatus machineStatus);
  void onTimerChanged(String timeToEndInHours);
  void onDataFailure(ErrorObjectInterface reason);
}
