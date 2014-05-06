package me.ghui.AMS.UI.Activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import me.ghui.AMS.utils.MyApp;

/**
 * Created by ghui on 4/9/14.
 */
public abstract class BaseActivity extends FragmentActivity {
    private RelativeLayout layout;
    public Handler handler;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.getMyApp().removeActivity(this);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutResourceId());
        initUI();
        init();
        MyApp.getMyApp().addActivity(this);
        handler = new Handler();
    }

    public abstract int getLayoutResourceId();

    public abstract void init();

    private void initUI() {
        ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        layout = new RelativeLayout(this);
        layout.setVisibility(View.GONE);
        layout.setGravity(Gravity.CENTER);
        layout.addView(progressBar);
        ViewGroup.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.addContentView(layout, params);
    }

    public void showProgressBar() {
        Log.e("ghui", "showProgressBar!");
        layout.post(new Runnable() {
            @Override
            public void run() {
                layout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void dismissProgressBar() {
        layout.post(new Runnable() {
            @Override
            public void run() {
                layout.setVisibility(View.GONE);
            }
        });
    }

    public void showToast(final String toast) {
        layout.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}