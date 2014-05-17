package me.ghui.AMS.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import me.ghui.AMS.domain.User;

/**
 * Created by vann on 5/17/14.
 */
public class PrefsUtils {

    public static String getScreenOritention(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_screen_oritentation", "0");
    }

    public static boolean isAutoSavePsw(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_auto_psw", true);
    }

    public static void saveUserInfoToSharedPrefs(Context context, String id, String psw) {
        //first,check if user select savepsw option
        SharedPreferences.Editor prefsEditor = context.getSharedPreferences(Constants.PREFS_USER_INFO, Context.MODE_PRIVATE).edit();
//        if (isAutoSavePsw(context)) {
            //save psw to sharedPrefs
            prefsEditor.putString(Constants.PREF_USER_PSW, psw);
//        } else {
//            prefsEditor.putString(Constants.PREF_USER_PSW, "");
//        }
        prefsEditor.putString(Constants.PREF_USER_ID, id);
        prefsEditor.commit();
    }

    public static void saveUserNameToSharedPrefs(Context context,String name) {
        SharedPreferences.Editor prefsEditor = context.getSharedPreferences(Constants.PREFS_USER_INFO, Context.MODE_PRIVATE).edit();
        prefsEditor.putString(Constants.PREF_USER_NAME, name);
        prefsEditor.commit();
    }

    public static void clearUserPrefs(Context context) {
        saveUserInfoToSharedPrefs(context,"","");
        saveUserNameToSharedPrefs(context,"");
    }

    public static User getUserInfoSharedPrefs(Context context) {
        User user = new User();
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFS_USER_INFO, Context.MODE_PRIVATE);
        user.setID(preferences.getString(Constants.PREF_USER_ID, ""));
        user.setName(preferences.getString(Constants.PREF_USER_NAME,""));
        if (isAutoSavePsw(context)) {
            user.setPassword(preferences.getString(Constants.PREF_USER_PSW, ""));
        } else {
            user.setPassword("");
        }
        return user;
    }


}
