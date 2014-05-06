package me.ghui.AMS.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import me.ghui.AMS.R;

public class MainActivity extends BaseActivity {
    @Override
    public int getLayoutResourceId() {
        return R.layout.main;
    }

    @Override
    public void init() {
    }
//onClickLisener........................................................................................................
    public void gotoSelfInfoActivity(View v) {
        Log.e("ghui", ",,,goSelfInfoActivity!!!");
        Intent intent = new Intent(this, SelfInfoActivity.class);
        startActivity(intent);
    }

    public void gotoCourseActivity(View view) {
        Intent intent = new Intent(this,CourseActivity.class);
        startActivity(intent);
    }

    public void gotoTeachingPlan(View view) {
        Intent intent = new Intent(this,TeachingPlanActivity.class);
        startActivity(intent);
    }

    public void gotoGradesActivity(View view) {
        startActivity(new Intent(this,GradesActivity.class));
    }
    public void gotoInputGradesActivity(View view) {
        startActivity(new Intent(this,InputGradesActivity.class));
    }
    public void gotoOthersActivity(View view) {
        startActivity(new Intent(this,ModPswActivity.class));
    }
}

