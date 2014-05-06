package me.ghui.AMS.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import me.ghui.AMS.utils.MyApp;
import me.ghui.AMS.utils.StringHelper;

/**
 * Created by ghui on 3/25/14.
 */
public class SplashActivity extends BaseActivity {
    LinearLayout layout;
    ImageView logo;
    ImageView img_validatecode;
    EditText et_work_id;
    EditText et_password;
    EditText et_validate_code;
    ProgressBar progressBar;

    @Override
    public int getLayoutResourceId() {
        return R.layout.splash_layout;
    }

    @Override
    public void init() {
        toast();
        initUI();
        initLogic();
    }

    private void toast() {
        String toast = getIntent().getStringExtra("toast");
        if (toast != null && toast.length() > 0) {
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        }
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

    private boolean checkForm() {
        boolean flag = false;
        if (et_work_id.length() == 0) {
            showToast("请输入工号!");
        } else if (et_password.length() == 0) {
            showToast("请输入密码!");
        } else if (et_validate_code.length() == 0) {
            showToast("请输入验证码！");
        } else {
            flag = true;
        }
        return flag;
    }

    public void login(View view) {
        //start animation...
        if (!checkForm()) {
            return;
        }
        showAnimation();
        User user = new User();
        user.setID(et_work_id.getText().toString())
                .setPassword(et_password.getText().toString()).setValidateCode(et_validate_code.getText().toString());
        MyApp.userid = user.getID();
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
                final String result = NetUtils.login(SplashActivity.this,user);
                if (result == null) {
                    return;
                }
                img_validatecode.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashActivity.this, result, Toast.LENGTH_SHORT).show();
                        logo.clearAnimation();
                        if (!result.contains("登录成功")) {
                            if (result.contains("验证码错误")) {
                                //todo clear validate code edittext then refresh the picture
                                et_validate_code.setText("");
                                et_validate_code.setHint("请重新输入验证码");
                                showInvalidateCode();
                            } else if (result.contains("帐号或密码不正确")) {
                                //todo clear psw edittext
                                et_work_id.setText("");
                                et_work_id.setHint("请重新输入工号");
                                et_password.setText("");
                                et_password.setHint("请重新输入密码");
                            }
                            layout.setVisibility(View.VISIBLE);
                            return;
                        }
                        logo.setVisibility(View.GONE);
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();
                    }
                });
            }
        }).start();
    }

    long time0;
    public void refreshValidateCode_OnClick(View view) {
        if (System.currentTimeMillis() - time0 > 2000) {
            time0 = System.currentTimeMillis();
            showInvalidateCode();
        } else {
            showToast("正在刷新,请稍后...");
        }
    }

    private void showInvalidateCode() {
        img_validatecode.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = NetUtils.getValidateCode(SplashActivity.this);
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