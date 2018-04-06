package com.slexsys.biz.main.option.comp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by slexsys on 12/16/17.
 */
public class LocalOptions {
    //region fields
    private static final String OPTION_NAME = "MyPref";

    private static Context context;
    private static LocalOptions ourInstance;
    private static SharedPreferences pref;
    //endregion

    //region constructors
    static {
        ourInstance = new LocalOptions();
        pref = context.getSharedPreferences(OPTION_NAME, 0);
    }

    private LocalOptions() { }
    //endregion

    //region public methods
    public static LocalOptions getInstance(Context context) {
        LocalOptions.context = context;
        return ourInstance;
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key) {
        return getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return pref.getInt(key, defaultValue);
    }

    public static void putFloat(String key, float value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static float getFloat(String key) {
        return getFloat(key, 0);
    }

    public static float getFloat(String key, float defaultValue) {
        return pref.getFloat(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return pref.getBoolean(key, defaultValue);
    }
    //endregion
}
