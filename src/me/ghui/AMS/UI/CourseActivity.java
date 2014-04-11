package me.ghui.AMS.UI;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import me.ghui.AMS.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghui on 4/7/14.
 */
public class CourseActivity extends Activity {
    private ArrayAdapter<String> adapter;
    private List<String> strings;
    private Spinner spinner;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course);
        init();
    }

    private void init() {
        strings = new ArrayList<String>();
        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, strings);
        spinner.setAdapter(adapter);
    }
}