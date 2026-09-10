package moe.div.mobase.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageUtils {

    private static final String SP_NAME = "mobase_language_settings";
    private static final String KEY_LANGUAGE = "selected_language";

    public static final String LANG_DEFAULT = "default";
    public static final String LANG_ZH = "zh";
    public static final String LANG_EN = "en";

    public static void applyLanguage(Context context) {
        String lang = getLanguage(context);
        updateLocale(context, getLocaleByValue(lang));
    }

    public static Context attachBaseContext(Context context) {
        String lang = getLanguage(context);
        Locale locale = getLocaleByValue(lang);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, locale);
        }
        return context;
    }

    private static Context updateResources(Context context, Locale locale) {
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        return context.createConfigurationContext(config);
    }

    public static void setLanguage(Context context, String lang) {
        saveLanguage(context, lang);
        updateLocale(context, getLocaleByValue(lang));
    }

    public static String getLanguage(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(KEY_LANGUAGE, LANG_DEFAULT);
    }

    private static void saveLanguage(Context context, String lang) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_LANGUAGE, lang).apply();
    }

    private static Locale getLocaleByValue(String lang) {
        if (LANG_ZH.equals(lang)) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (LANG_EN.equals(lang)) {
            return Locale.ENGLISH;
        }
        // 返回系统默认语言
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            return Resources.getSystem().getConfiguration().locale;
        }
    }

    private static void updateLocale(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            context.createConfigurationContext(config);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, dm);
    }
}
