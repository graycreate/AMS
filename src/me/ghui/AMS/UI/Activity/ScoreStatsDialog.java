package me.ghui.AMS.UI.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import me.ghui.AMS.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghui on 4/28/14.
 */
public class ScoreStatsDialog extends BaseActivity {
    int[] ids = new int[]{
            R.id.line1, R.id.line2, R.id.line3,
            R.id.line4, R.id.line5, R.id.line6,
            R.id.line7, R.id.line8
    };

    @Override
    public int getLayoutResourceId() {
        return R.layout.score_stats_dialog;
    }

    @Override
    public void init() {
        String summary = getIntent().getStringExtra("summary");
        Log.e("ghui", "summary: " + summary);
        List<String> strings = new ArrayList<String>();
        for (String s : summary.split(" ")) {
            if (!(s.contains("人数") || s.contains("百分比"))) {
                strings.add(s);
            }
        }
        for (int i = 0; i < ids.length; i++) {
            ((TextView) findViewById(ids[i])).setText(strings.get(i) + "人(" + strings.get(8 + i) + "%)");
        }
    }
}