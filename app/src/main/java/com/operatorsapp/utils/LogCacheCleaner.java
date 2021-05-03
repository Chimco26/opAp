package com.operatorsapp.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class LogCacheCleaner {


    public LogCacheCleaner() {
    }

    public void removeExistingFiles(Context context) {
        File root = this.getRootCacheDirectory(context);
        File logsDir = new File(root, "Logs");
        if (logsDir.exists()) {
            String[] children = logsDir.list();

            for (String aChildren : children) {
                (new File(logsDir, aChildren)).delete();
            }

//            logsDir.delete();
        }

    }

    private File getRootCacheDirectory(Context context) {
        File appCacheDir = null;

        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException var4) {
            externalStorageState = "";
        }

        if ("mounted".equals(externalStorageState) && this.hasExternalStoragePermission(context)) {
            appCacheDir = this.getExternalCacheDir(context);
        }

        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }

        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }

        return appCacheDir;
    }

    private File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }

            try {
                (new File(appCacheDir, ".nomedia")).createNewFile();
            } catch (IOException var5) {

            }
        }

        return appCacheDir;
    }

    private boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == 0;
    }
}
