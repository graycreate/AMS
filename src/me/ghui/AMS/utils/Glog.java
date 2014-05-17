package me.ghui.AMS.utils;

import android.util.Log;

/**
 * Created by vann on 5/17/14.
 */
public class Glog {
    public static void e(String msg) {
        Log.e("ghui", msg);
    }
    public static void d(String msg) {
        Log.d("ghui", msg);
    }
    public static void i(String msg) {
        Log.i("ghui", msg);
    }
}
