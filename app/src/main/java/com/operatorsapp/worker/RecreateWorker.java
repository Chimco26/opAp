package com.operatorsapp.worker;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.jakewharton.processphoenix.ProcessPhoenix;
import com.operatorsapp.activities.MainActivity;

import org.jetbrains.annotations.NotNull;

import static com.operatorsapp.utils.ClearData.cleanEvents;

public class RecreateWorker extends Worker {
    private static final String TAG = RecreateWorker.class.getSimpleName();
    public RecreateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NotNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: recreate app from code");
        // Do the work here
        refreshApp();

        // Indicate success or failure with your return value:
        return Result.success();

        // (Returning RETRY tells WorkManager to try this task again
        // later; FAILURE says not to try again.)
    }

    private void refreshApp() {
        cleanEvents();
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplicationContext().startActivity(myIntent);
    }
}