package com.operatorsapp.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.operatorsapp.managers.PersistenceManager;

import java.text.Bidi;
import java.util.Locale;

public class ChangeLang {

    public static void changeHebrew(final Context context) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                String app_locale = "iw";
                Locale locale = new Locale(app_locale);
                Locale.setDefault(locale);

                //Configuration to query the current layout direction.
                Configuration config = new Configuration();
                config.locale = locale;
                context.getResources().updateConfiguration(config,
                        context.getResources().getDisplayMetrics());
                Bidi bidi = new Bidi(app_locale,
                        Bidi.DIRECTION_DEFAULT_RIGHT_TO_LEFT);
                bidi.isRightToLeft();
                YourGlobalClass.updateLanguage(context, "iw");

                return null;
            }

        }.execute();
    }

    public static void changeLanguage(final Context context) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                String app_locale = PersistenceManager.getInstance().getCurrentLang();
                Locale locale = new Locale(app_locale);
                Locale.setDefault(locale);

                //Configuration to query the current layout direction.
                Configuration config = new Configuration();
                config.locale = locale;
                context.getResources().updateConfiguration(config,
                        context.getResources().getDisplayMetrics());
                Bidi bidi = new Bidi(app_locale,
                        Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);
                bidi.isLeftToRight();
                YourGlobalClass.updateLanguage(context, app_locale);

                return null;
            }

        }.execute();
    }

    private static class YourGlobalClass extends Application {

        @Override
        public void onCreate() {
            updateLanguage(this, null);
            super.onCreate();
        }

        public static void updateLanguage(Context ctx, String lang) {

            Configuration cfg = new Configuration();
            PersistenceManager manager = PersistenceManager.getInstance();
            String language = manager.getCurrentLang();

            if (TextUtils.isEmpty(language) && lang == null) {
                cfg.locale = Locale.getDefault();
                String tmp_locale = "";
                tmp_locale = Locale.getDefault().toString().substring(0, 2);
                manager.setCurrentLang(tmp_locale);

            }
            else if (lang != null) {
                cfg.locale = new Locale(lang);
                manager.setCurrentLang(lang);

            }
            else if (!TextUtils.isEmpty(language)) {
                cfg.locale = new Locale(language);
            }
            ctx.getResources().updateConfiguration(cfg, null);
        }

    }
}
