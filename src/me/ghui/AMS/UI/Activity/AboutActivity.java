package me.ghui.AMS.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import me.ghui.AMS.R;

/**
 * Created by vann on 5/18/14.
 */
public class AboutActivity extends BaseActivity {

    @Override
    public int getLayoutResourceId() {
        return R.layout.about;
    }

    @Override
    public void init() {

    }

    public void FAQ_OnClick(View view) {
        showToast("help");
    }
    public void getSource_OnClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ghuiii/AMS"));
        startActivity(intent);
    }

    public void feedback_OnClick(View view) {
        String body = "Mode:" + android.os.Build.MODEL+"\n----------------------------------------\n";
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"hi@ghui.me"});
        i.putExtra(Intent.EXTRA_SUBJECT, "feedback");
        i.putExtra(Intent.EXTRA_TEXT, body);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            showToast("There are no email clients installed");
        }
    }
}