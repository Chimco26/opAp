package com.operatorsapp.interfaces;

import com.operators.infra.MachineStatus;
import com.operators.machinestatuscore.interfaces.OnTimeToEndChangedListener;

/**
 * Created by User on 14/07/2016.
 */
public interface DashboardUICallbackListener
{
  void onDeviceStatusChanged(MachineStatus machineStatus);
  void onTimerChanged(String timeToEndInHours);
}
