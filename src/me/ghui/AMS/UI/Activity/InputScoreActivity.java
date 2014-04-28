package me.ghui.AMS.UI.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import me.ghui.AMS.R;
import me.ghui.AMS.utils.Constants;

/**
 * Created by ghui on 4/29/14.
 */
public class InputScoreActivity extends BaseActivity {
    private static String url;
    @Override
    public int getLayoutResourceId() {
        return R.layout.input_score_activity;
    }

    @Override
    public void init() {
        url = Constants.INPUT_SCORE_URL + getIntent().getStringExtra("data");
        Log.e("ghui", "url:" + url);
    }
}