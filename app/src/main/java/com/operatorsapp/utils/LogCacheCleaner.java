package com.operatorsapp.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class LogCacheCleaner {

    private Context mContext;

    public LogCacheCleaner(Context context) {

        mContext = context;
    }

    public void removeExistingFiles() {
        File root = this.getRootCacheDirectory();
        File logsDir = new File(root, "Logs");
        if (logsDir.exists()) {
            String[] children = logsDir.list();

            for(int i = 0; i < children.length; ++i) {
                (new File(logsDir, children[i])).delete();
            }

//            logsDir.delete();
        }

    }

    private File getRootCacheDirectory() {
        File appCacheDir = null;

        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException var4) {
            externalStorageState = "";
        }

        if ("mounted".equals(externalStorageState) && this.hasExternalStoragePermission(this.mContext)) {
            appCacheDir = this.getExternalCacheDir(this.mContext);
        }

        if (appCacheDir == null) {
            appCacheDir = this.mContext.getCacheDir();
        }

        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + this.mContext.getPackageName() + "/cache/";
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
