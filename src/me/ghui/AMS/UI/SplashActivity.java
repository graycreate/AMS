package me.ghui.AMS.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import me.ghui.AMS.R;
import me.ghui.AMS.utils.LoginDataHelper;

/**
 * Created by ghui on 3/25/14.
 */
public class SplashActivity extends Activity {
    LinearLayout layout;
    ImageView logo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        initUI();
        initLogic();
    }

    private void initUI() {
        layout = (LinearLayout) findViewById(R.id.layout_login);
        logo= (ImageView) findViewById(R.id.img_logo);

    }

    private void initLogic() {
        if (!LoginDataHelper.hasLogin(this)) {
            layout.setVisibility(View.VISIBLE);
        }
    }

    public void login(View view) {
        //start animation...
        showAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 6500);
    }

    private void showAnimation() {
        layout.setVisibility(View.GONE);
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);

      Animation rotateAnimation = new RotateAnimation(0f, 360f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                rotateAnimation.setDuration(5000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animation);
        set.addAnimation(rotateAnimation);
        set.setFillAfter(true);
        logo.startAnimation(set);
    }
}