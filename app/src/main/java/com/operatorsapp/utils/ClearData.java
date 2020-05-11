package com.operatorsapp.utils;

import com.example.common.Event;
import com.example.common.StandardResponse;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.PostDeleteTokenRequest;

import org.litepal.crud.DataSupport;

import retrofit2.Call;
import retrofit2.Callback;

import static android.text.format.DateUtils.DAY_IN_MILLIS;

public class ClearData {

    public static void clearData() {
        PostDeleteTokenRequest request = new PostDeleteTokenRequest(PersistenceManager.getInstance().getMachineId(), PersistenceManager.getInstance().getSessionId(), PersistenceManager.getInstance().getNotificationToken());
        NetworkManager.getInstance().postDeleteToken(request, new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, retrofit2.Response<StandardResponse> response) {

            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {

            }
        });

        clearMachineData();

        String tmpLanguage = PersistenceManager.getInstance().getCurrentLang();
        String tmpLanguageName = PersistenceManager.getInstance().getCurrentLanguageName();

        PersistenceManager.getInstance().clear();

        PersistenceManager.getInstance().items.clear();

        PersistenceManager.getInstance().setCurrentLang(tmpLanguage);
        PersistenceManager.getInstance().setCurrentLanguageName(tmpLanguageName);

        PersistenceManager.getInstance().setMachineId(-1);

    }

    public static void clearMachineData(){
        cleanEvents();
        PersistenceManager.getInstance().setOperatorId(null);
        PersistenceManager.getInstance().setOperatorName(null);
        PersistenceManager.getInstance().clearCalledTechnician();
        PersistenceManager.getInstance().setCalledTechnicianList(null);
        PersistenceManager.getInstance().setCalledTechnicianName(null);
        PersistenceManager.getInstance().setTechnicianCallTime(0);
        PersistenceManager.getInstance().setRecentTechCallId(0);
        PersistenceManager.getInstance().setNeedUpdateToken(true);

    }

    public static void cleanEvents() {
        PersistenceManager.getInstance().setShiftLogStartingFrom(TimeUtils.getDate(System.currentTimeMillis() - DAY_IN_MILLIS, "yyyy-MM-dd HH:mm:ss.SSS"));
        PersistenceManager.getInstance().setMachineDataStartingFrom(TimeUtils.getDate(System.currentTimeMillis() - DAY_IN_MILLIS, "yyyy-MM-dd HH:mm:ss.SSS"));
        DataSupport.deleteAll(Event.class);
    }
}
