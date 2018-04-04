package com.team10.trotot.view.supports;

import android.content.Context;
import android.content.SharedPreferences;

import com.team10.trotot.model.PREF_STRING;

import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by vinhkang on 05/12/2017.
 */

public class PrefUtil {

    private static String pack = "com.team10.trotot";

    public static int getInt(Context context, PREF_STRING pref) {
        return context.getSharedPreferences(pack, MODE_PRIVATE).getInt(pref.getName(), -1);
    }

    public static long getLong(Context context, PREF_STRING pref) {
        return context.getSharedPreferences(pack, MODE_PRIVATE).getLong(pref.getName(), -1);
    }

    public static float getFloat(Context context, PREF_STRING pref) {
        return context.getSharedPreferences(pack, MODE_PRIVATE).getFloat(pref.getName(), 0);
    }

    public static boolean getBoolean(Context context, PREF_STRING pref) {
        return context.getSharedPreferences(pack, MODE_PRIVATE).getBoolean(pref.getName(), false);
    }

    public static String getString(Context context, PREF_STRING pref) {
        return context.getSharedPreferences(pack, MODE_PRIVATE).getString(pref.getName(), "");
    }

    public static Set<String> getStringSet(Context context, PREF_STRING pref) {
        return context.getSharedPreferences(pack, MODE_PRIVATE).getStringSet(pref.getName(), new HashSet<String>());
    }

    public static void set(Context context, PREF_STRING pref, int data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(pack, MODE_PRIVATE).edit();
        editor.putInt(pref.getName(), data);
        editor.apply();
    }

    public static void set(Context context, PREF_STRING pref, long data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(pack, MODE_PRIVATE).edit();
        editor.putLong(pref.getName(), data);
        editor.apply();
    }

    public static void set(Context context, PREF_STRING pref, float data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(pack, MODE_PRIVATE).edit();
        editor.putFloat(pref.getName(), data);
        editor.apply();
    }

    public static void set(Context context, PREF_STRING pref, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(pack, MODE_PRIVATE).edit();
        editor.putBoolean(pref.getName(), data);
        editor.apply();
    }

    public static void set(Context context, PREF_STRING pref, String data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(pack, MODE_PRIVATE).edit();
        editor.putString(pref.getName(), data);
        editor.apply();
    }


    public static void set(Context context, PREF_STRING pref, Set<String> data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(pack, MODE_PRIVATE).edit();
        editor.putStringSet(pref.getName(), data);
        editor.apply();
    }


}
