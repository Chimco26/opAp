package com.operatorsapp.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.pulling.interfaces.MachineDataUICallback;

import java.util.Locale;

public class ChangeLang {


    //    public static final String[] languages = new String[]{"English", "עברית", "中文", "Deutsch", "русский", "Українська", "español", "Français", "Nederlands", "العربية"};
    public static final String[] languagesCode = new String[]{"en", "iw", "zh", "de", "ru", "uk", "es", "fr", "nl", "ar", "it", "hu", "pl", "el"}; // when adding a new language, add below as well
    public static final String[] languagesCode3Letters = new String[]{"eng", "heb", "chn", "ger", "rus", "ukr", "spn", "fre", "ned", "arb", "ita", "hun", "pol", "gre"}; // add here new language as well when adding above
    private static final String TAG = ChangeLang.class.getSimpleName();


    public static void initLanguage(Context baseContext) {

        setLanguage(baseContext, getDefaultLanguage());

    }

    private static String getDefaultLanguage() {

        String selectedLanguage = PersistenceManager.getInstance().getCurrentLang();

        if (selectedLanguage != null) {
            return selectedLanguage;
        } else {
            String localeLanguage = Locale.getDefault().getLanguage();
            for (String language : languagesCode) {

                if (language.equals(localeLanguage)) {
                    PersistenceManager.getInstance().setCurrentLang(localeLanguage);
                    return localeLanguage;
                }
            }
            PersistenceManager.getInstance().setCurrentLang("en");
            return "en";
        }
    }

    public static boolean defaultIsEng() {

        return PersistenceManager.getInstance().getCurrentLang().equals("en");
    }

    public static boolean defaultIsHebrew() {

        return PersistenceManager.getInstance().getCurrentLang().equals("iw");
    }

    public static boolean defaultIsRtl() {

        return defaultIsHebrew() || PersistenceManager.getInstance().getCurrentLang().equals("ar");
    }

    public static int getPositionByLanguageCode() {

        String language = getDefaultLanguage();
        for (int i = 0; i < languagesCode.length; i++) {
            if (languagesCode[i].equals(language)) {
                return i;
            }
        }
        return 0;
    }

    private static void setLanguage(Context baseContext, String languageCode) {
        Configuration config = baseContext.getResources().getConfiguration();
        Locale locale = new Locale(languageCode, config.locale.getCountry());
        Locale.setDefault(locale);
        config.setLocale(locale);
        baseContext.getResources().updateConfiguration(config, baseContext.getResources().getDisplayMetrics());
        Log.d(TAG, "Set Language: " + languageCode);
    }

    public static boolean isLanguageSetOk(Context context) {
        if (Locale.getDefault().getLanguage().equals(PersistenceManager.getInstance().getCurrentLang())){
            return true;
        }else {
            setLanguage(context, PersistenceManager.getInstance().getCurrentLang());
            return false;
        }
    }


    /*private static final String TAG = ChangeLang.class.getSimpleName();

    @SuppressWarnings("unused")
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
//Async cause bug because the screens layout direction is created before the language is set.
//        it's not a long task so i set it in main thread
//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... params) {
                if (PersistenceManager.getInstance() != null) {
                    String app_locale = PersistenceManager.getInstance().getCurrentLang();
                    Log.d(TAG, "changeLanguage: " + app_locale);
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

                }
//                return null;
//            }
//
//        }.execute();
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
                String tmp_locale;
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

    }*/
}
