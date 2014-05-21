package me.ghui.AMS.UI.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import me.ghui.AMS.R;
import me.ghui.AMS.net.NetUtils;
import me.ghui.AMS.utils.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MainActivity extends BaseActivity {
    Intent keepaliveIntent;

    @Override
    public int getLayoutResourceId() {
        return R.layout.main;
    }

    @Override
    public void init() {
        keepMeAlive();
        getUserName();
    }

    private void keepMeAlive() {
        if (PrefsUtils.isKeepAlive(this)) {
            //start a service to interact with server
            keepaliveIntent = new Intent(this, KeepAliveService.class);
            startService(keepaliveIntent);
        }
    }

    @Override
    public void onBackPressed() {
        if (!PrefsUtils.isKeepAlive(this)) {
            MyApp.getMyApp().exit();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("保持登录吗?");
        builder.setMessage("你开启了保持登录状态,需要后台保持吗?");
        builder.setPositiveButton("完全退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyApp.getMyApp().exit();
            }
        });
        builder.setNegativeButton("后台保持", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                moveTaskToBack(true);
            }
        });
        builder.create().show();
    }

    private void getUserName() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                showProgressBar();
                String refer = "http://211.84.112.48/lyit/sys/menu.aspx";
                Document document = NetUtils.getDataFromServer(MainActivity.this, Constants.PSW_MOD_URL, refer);
                if (document == null) {
                    return;
                }
                Elements elements = document.select("input");
                if (elements.size() < 2) {
                    Glog.e("error..");
                    return;
                }
                String userName = elements.get(1).attr("value");
                Glog.e("userName: " + userName);
                PrefsUtils.saveUserNameToSharedPrefs(MainActivity.this, userName);
                dismissProgressBar();
            }
        }).start();
    }

    //onClickLisener........................................................................................................
    public void gotoSelfInfoActivity(View v) {
        Log.e("ghui", ",,,goSelfInfoActivity!!!");
        Intent intent = new Intent(this, SelfInfoActivity.class);
        startActivity(intent);
    }

    public void gotoCourseActivity(View view) {
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ghui", "MainActivity onDestroy...");
    }

    public void gotoTeachingPlan(View view) {
        Intent intent = new Intent(this, TeachingPlanActivity.class);
        startActivity(intent);
    }

    public void gotoGradesActivity(View view) {
        startActivity(new Intent(this, GradesActivity.class));
    }

    public void gotoInputGradesActivity(View view) {
        startActivity(new Intent(this, InputGradesActivity.class));
    }

    public void gotoOthersActivity(View view) {
        startActivity(new Intent(this, ModPswActivity.class));
    }


    public void gotoSettingsActivity_OnClick(View view) {

        startActivity(new Intent(this, SettingsActivity.class));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            //go to SettingsActivity
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

