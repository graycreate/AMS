package me.ghui.AMS.UI.View;

import android.content.Context;
import android.os.Build;
import android.preference.ListPreference;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by vann on 5/18/14.
 */
public class ListPreferenceCompat extends ListPreference {
    public ListPreferenceCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListPreferenceCompat(Context context) {
        super(context);
    }

    //to do this ,since a bug before android kitkat
    @Override
    public void setValue(String value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            super.setValue(value);
        } else {
            String oldValue = getValue();
            super.setValue(value);
            if (!TextUtils.equals(value, oldValue)) {
                notifyChanged();
            }
        }
    }
}
