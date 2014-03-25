package me.ghui.AMS.utils;

import android.content.Context;
import android.content.SharedPreferences;
import me.ghui.AMS.domain.User;

/**
 * Created by ghui on 3/25/14.
 */
public class LoginDataHelper {

    public static void saveLoginInfo(Context context, User user) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.LOGIN_DATA, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.ID, user.getID());
        editor.putString(Constants.PSW, user.getPassword());
        editor.commit();
    }

    public static void clearLoginInfo(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.LOGIN_DATA, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.ID, Constants.UNLOGIN);
        editor.putString(Constants.PSW, Constants.UNLOGIN);
        editor.commit();
    }

    public static boolean hasLogin(Context context) {
        return ! context.getSharedPreferences(Constants.LOGIN_DATA, Context.MODE_PRIVATE).getString(Constants.ID,"").isEmpty();
    }
}
