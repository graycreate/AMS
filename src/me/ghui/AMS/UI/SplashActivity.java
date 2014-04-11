package me.ghui.AMS.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.*;
import me.ghui.AMS.R;
import me.ghui.AMS.domain.User;
import me.ghui.AMS.net.NetUtils;
import me.ghui.AMS.utils.LoginDataHelper;
import me.ghui.AMS.utils.StringHelper;

import java.io.IOException;

/**
 * Created by ghui on 3/25/14.
 */
public class SplashActivity extends Activity {
    LinearLayout layout;
    ImageView logo;
    ImageView img_validatecode;
    EditText et_work_id;
    EditText et_password;
    EditText et_validate_code;
    ProgressBar progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        initUI();
        initLogic();
    }

    private void initUI() {
        layout = (LinearLayout) findViewById(R.id.layout_login);
        logo = (ImageView) findViewById(R.id.img_logo);
        img_validatecode = (ImageView) findViewById(R.id.img_validatecode);
        et_work_id = (EditText) findViewById(R.id.et_work_id);
        et_work_id.setSelection(et_work_id.length());
        et_password = (EditText) findViewById(R.id.et_password);
        et_validate_code = (EditText) findViewById(R.id.et_validate_code);
        progressBar = (ProgressBar) findViewById(R.id.pb_validate);
        StringHelper.toUpcase(et_validate_code);
        showInvalidateCode();
    }



    private void initLogic() {
        if (!LoginDataHelper.hasLogin(this)) {
            layout.setVisibility(View.VISIBLE);
        }
    }

    public void login(View view) {
        //start animation...
        showAnimation();
        User user = new User();
        user.setID(et_work_id.getText().toString())
                .setPassword(et_password.getText().toString()).setValidateCode(et_validate_code.getText().toString());
        Log.e("ghui", user.toString());
        executeLogin(user);
    }

    private void showAnimation() {
        layout.setVisibility(View.GONE);
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);

        Animation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(5000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animation);
        set.addAnimation(rotateAnimation);
        set.setFillAfter(true);
        logo.startAnimation(set);
    }


    private void executeLogin(final User user) {
       new Thread(new Runnable() {
           @Override
           public void run() {
              NetUtils.login(user);
               img_validatecode.post(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(SplashActivity.this,"login successfully!",Toast.LENGTH_SHORT).show();
                       logo.clearAnimation();
                       logo.setVisibility(View.GONE);
                       finish();
                       startActivity(new Intent(SplashActivity.this, MainActivity.class));
                      overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                   }
               });
           }
       }).start();
    }


    private void showInvalidateCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = NetUtils.getValidateCode();
                img_validatecode.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        img_validatecode.setVisibility(View.VISIBLE);
                        img_validatecode.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
    }
}