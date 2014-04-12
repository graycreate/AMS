package me.ghui.AMS.UI;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by ghui on 4/9/14.
 */
public abstract class BaseActivity extends FragmentActivity {
    private RelativeLayout layout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        initUI();
        init();
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
}