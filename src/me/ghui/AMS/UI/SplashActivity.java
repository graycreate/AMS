package me.ghui.AMS.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import me.ghui.AMS.R;
import me.ghui.AMS.utils.LoginDataHelper;

/**
 * Created by ghui on 3/25/14.
 */
public class SplashActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        initUI();
        initLogic();
    }

    private void initUI() {

    }

    private void initLogic() {
        if (!LoginDataHelper.hasLogin(this)) {
            findViewById(R.id.layout_login).setVisibility(View.VISIBLE);
        }
    }

    public void login(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}