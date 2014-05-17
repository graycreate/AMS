package me.ghui.AMS.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import me.ghui.AMS.UI.Activity.SplashActivity;

/**
 * Created by vann on 5/18/14.
 */
public class Utils {
    public static void setScreenOritention(Activity activity) {
        if (PrefsUtils.getScreenOritention(activity).equals("1")) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (PrefsUtils.getScreenOritention(activity).equals("2")) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        if (activity instanceof SplashActivity) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
