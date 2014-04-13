package me.ghui.AMS.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import me.ghui.AMS.R;

public class MainActivity extends Activity {
    View extend_menu1;
    View extend_menu2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }

    private void init() {
        extend_menu1 = findViewById(R.id.extend_menu_1);
        extend_menu2 = findViewById(R.id.extend_menu_2);
    }
//onClickLisener........................................................................................................
    public void gotoSelfInfoActivity(View v) {
        Log.e("ghui", ",,,goSelfInfoActivity!!!");
        Intent intent = new Intent(this, SelfInfoActivity.class);
        startActivity(intent);
    }

    public void toggleExtendMenu1(View view) {
        if (extend_menu1.getVisibility() == View.GONE) {
            extend_menu1.setVisibility(View.VISIBLE);
        } else {
            extend_menu1.setVisibility(View.GONE);
        }
    }

    public void gotoCourseActivity(View view) {
        Intent intent = new Intent(this,CourseActivity.class);
        startActivity(intent);
    }

    public void gotoTeachingPlan(View view) {
        Intent intent = new Intent(this,TeachingPlanActivity.class);
        startActivity(intent);
    }

    public void toggleExtendMenu2(View view) {
        if (extend_menu2.getVisibility() == View.GONE) {
            extend_menu2.setVisibility(View.VISIBLE);
        } else {
            extend_menu2.setVisibility(View.GONE);
        }
    }

    public void gotoGradesActivity(View view) {
        startActivity(new Intent(this,GradesActivity.class));
    }
    public void gotoInputGradesActivity(View view) {
        startActivity(new Intent(this,InputGradesActivity.class));
    }
    public void gotoOthersActivity(View view) {
        startActivity(new Intent(this,OthersActivity.class));
    }
}

