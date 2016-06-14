package com.operatorsapp.managers;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Omri Bager on 2/16/2016.
 * Falcore LTD
 */
public class CriticalMachinesManager
{
    private static final String LOG_TAG = CriticalMachinesManager.class.getSimpleName();
    private static final int NUMBER_OF_CRITICAL_MACHINES_ALLOWED = 30;
    private static CriticalMachinesManager msInstance;
    private HashMap<String, ArrayList<Integer>> mCriticalMachinesIdsBySite;

    private CriticalMachinesManager()
    {
        loadCriticalMachinesFromPreferences();
    }

    public static void initInstance()
    {
        msInstance = new CriticalMachinesManager();
    }

    public static CriticalMachinesManager getInstance()
    {
        return msInstance;
    }

    public ArrayList<Integer> getCriticalMachinesIdsBySite(String siteId)
    {
        ArrayList<Integer> criticalMachinesIdsBySite = mCriticalMachinesIdsBySite.get(siteId);

        if (criticalMachinesIdsBySite == null)
        {
            criticalMachinesIdsBySite = new ArrayList<>();
        }

        return criticalMachinesIdsBySite;
    }

    public boolean addCriticalMachine(String siteId, int criticalMachineId)
    {
        ArrayList<Integer> criticalMachinesIdsBySite = mCriticalMachinesIdsBySite.get(siteId);

        if (criticalMachinesIdsBySite == null)
        {
            criticalMachinesIdsBySite = new ArrayList<>();
            mCriticalMachinesIdsBySite.put(siteId, criticalMachinesIdsBySite);
        }

        if (criticalMachinesIdsBySite.size() == NUMBER_OF_CRITICAL_MACHINES_ALLOWED)
        {
            return false;
        }
        else
        {
            if (!criticalMachinesIdsBySite.contains(criticalMachineId))
            {
                criticalMachinesIdsBySite.add(criticalMachineId);
                saveCriticalMachinesToPreferences();
            }

            return true;
        }
    }

    public void removeCriticalMachine(String siteId, int machineId)
    {
        ArrayList<Integer> criticalMachinesIdsBySite = mCriticalMachinesIdsBySite.get(siteId);

        int indexToRemove = -1;

        if (criticalMachinesIdsBySite != null)
        {
            for (Integer criticalMachineId : criticalMachinesIdsBySite)
            {
                if (criticalMachineId.equals(machineId))
                {
                    indexToRemove = criticalMachinesIdsBySite.indexOf(criticalMachineId);
                }
            }

            if (indexToRemove != -1)
            {
                criticalMachinesIdsBySite.remove(indexToRemove);
                saveCriticalMachinesToPreferences();
            }
        }
        else
        {
            Log.w(LOG_TAG, "removeCriticalMachine() the machine is not in the critical machine manager");
        }
    }

    private void saveCriticalMachinesToPreferences()
    {
        PersistenceManager.getInstance().saveCriticalMachines(mCriticalMachinesIdsBySite);
    }

    private void loadCriticalMachinesFromPreferences()
    {
        mCriticalMachinesIdsBySite = PersistenceManager.getInstance().loadCriticalMachines();

        if (mCriticalMachinesIdsBySite == null)
        {
            mCriticalMachinesIdsBySite = new HashMap<>();
        }
    }

    public boolean isMachineCritical(String siteId, int machineId)
    {
        ArrayList<Integer> criticalMachinesIdsBySite = mCriticalMachinesIdsBySite.get(siteId);

        if (criticalMachinesIdsBySite != null)
        {
            for (Integer criticalMachineId : criticalMachinesIdsBySite)
            {
                if (criticalMachineId.equals(machineId))
                {
                    return true;
                }
            }

        }

        return false;
    }

    public void setCriticalMachines(String siteId, ArrayList<Integer> criticalMachines)
    {
        mCriticalMachinesIdsBySite.put(siteId, criticalMachines);
        saveCriticalMachinesToPreferences();
    }
}
