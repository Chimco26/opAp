package com.operatorsapp.interfaces;

import com.operators.machinestatusinfra.MachineStatus;

public interface DashboardUICallbackListener
{
  void onDeviceStatusChanged(MachineStatus machineStatus);
  void onTimerChanged(String timeToEndInHours);
}
