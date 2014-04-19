package me.ghui.AMS.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import me.ghui.AMS.R;
import me.ghui.AMS.utils.MyApp;

/**
 * Created by ghui on 4/19/14.
 */
public class NetWorkUnavailableDialog extends BaseActivity {
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.network_unavailable_dialog;
    }

    @Override
    public void init() {
        setFinishOnTouchOutside(false);
    }

    public void cancleSetOnNetWork_OnClick(View view) {
        MyApp.getMyApp().exit();
    }

    public void SetOnNetWork_OnClick(View view) {
        Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
        startActivity(intent);
        finish();
    }
}