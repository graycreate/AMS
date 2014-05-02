package me.ghui.AMS.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;
import me.ghui.AMS.UI.Activity.SplashActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ghui on 4/18/14.
 */
public class MyApp extends Application {
    private static List<Activity> list = new LinkedList<Activity>();
    private static MyApp myApp;
    public static String userid;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
    }

    public static MyApp getMyApp() {
        return myApp;
    }

    public void addActivity(Activity activity) {
        list.add(activity);
    }

    public void removeActivity(Activity activity) {
        list.remove(activity);
    }

    public void exit() {
        for (Activity activity : list) {
            if (activity != null) {
                activity.finish();
            }
        }
    }

    public void restart(String toast) {
        exit();
        Intent intent = new Intent(myApp, SplashActivity.class);
        intent.putExtra("toast", toast);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myApp.startActivity(intent);
    }
}
