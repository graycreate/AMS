package me.ghui.AMS.UI.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import me.ghui.AMS.R;
import me.ghui.AMS.UI.Activity.AboutActivity;
import me.ghui.AMS.domain.User;
import me.ghui.AMS.utils.Glog;
import me.ghui.AMS.utils.MyApp;
import me.ghui.AMS.utils.PrefsUtils;
import me.ghui.AMS.utils.Utils;

/**
 * Created by vann on 5/17/14.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        final User user = PrefsUtils.getUserInfoSharedPrefs(getActivity());
        Glog.e(user.toString());
        //1,user pref
        Preference pref_user = findPreference("pref_user");
        Glog.e(user.toString()+",,,,,,,");
        pref_user.setTitle(user.getName());
        pref_user.setSummary(user.getID());
        pref_user.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Glog.e("OnPreferenceClicked...");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("确定退出吗?");
                builder.setMessage("当前登录的账号是: " + user.getID());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PrefsUtils.clearUserPrefs(getActivity());
                        MyApp.getMyApp().exit();
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return false;
            }
        });
        //2,screen oratation
//       findPreference("pref_screen_oritentation").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//           @Override
//           public boolean onPreferenceChange(Preference preference, Object newValue) {
//               Glog.e("preference changed...");
////               Utils.setScreenOritention(getActivity());
//               return false;
//           }
//       });

        //3,about activity
        findPreference("pref_about").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getActivity().startActivity(new Intent(getActivity(), AboutActivity.class));
                return true;
            }
        });
        //4, startservice to keep alive
//        findPreference("pref_keep_alive").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            publicrboolean onPreferenceChange(Preference preference, Object newValue) {
//                Glog.e("preference changed...newValue: " + newValue);
//                return false;
//            }
//        });

    }
}