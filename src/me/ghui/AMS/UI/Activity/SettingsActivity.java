package me.ghui.AMS.UI.Activity;

import android.app.Activity;
import android.os.Bundle;
import me.ghui.AMS.R;
import me.ghui.AMS.UI.Fragment.SettingsFragment;

/**
 * Created by vann on 5/17/14.
 */
public class SettingsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}