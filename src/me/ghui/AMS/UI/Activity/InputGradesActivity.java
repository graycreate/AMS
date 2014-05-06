package me.ghui.AMS.UI.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import me.ghui.AMS.R;
import me.ghui.AMS.UI.Animation.ZoomOutPageTransformer;
import me.ghui.AMS.UI.Fragment.GradesFragment;
import me.ghui.AMS.net.NetUtils;
import me.ghui.AMS.utils.Constants;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ghui on 4/7/14.
 */
public class InputGradesActivity extends BaseActivity {
    private ViewPager mPager;
    private ArrayAdapter<String> adapter;
    private List<String> values;
    private Spinner spinner;
    private int currentSelection = 0;
    public Elements es;
    public FragmentStatePagerAdapter pagerAdapter;
    private ArrayList<String> strings;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ghui", "onDestroy");
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.input_grades;
    }

    @Override
    public void init() {
//        initFragments();
        mPager = (ViewPager) findViewById(R.id.vPager);
        strings = new ArrayList<String>();
        values = new ArrayList<String>();
        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, strings);
        showProgressBar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = NetUtils.getDataFromServer(InputGradesActivity.this, Constants.INPUT_GRADE_INFO_URL);
                Elements elements = doc.select("select[name=sel_xnxq]").select("option");
                Log.e("ghui", "elements: " + elements);
                for (Element e : elements) {
                    strings.add(e.text());
                    Log.e("ghui", "value: " + e.attr("value"));
                    values.add(e.attr("value"));
                }
                spinner.post(new Runnable() {
                    @Override
                    public void run() {
                        spinner.setAdapter(adapter);
                    }
                });
                dismissProgressBar();
                search();
                mPager.post(new Runnable() {
                    @Override
                    public void run() {
                        initFragments();
                        findViewById(R.id.pager_title_strip).setVisibility(View.VISIBLE);
                        mPager.setAdapter(pagerAdapter);
                    }
                });
            }
        }).start();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ghui", "current Selection: " + currentSelection + "; position: " + position);
                if (position != currentSelection) {
                    currentSelection = position;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            search();
                            mPager.post(new Runnable() {
                                @Override
                                public void run() {
                                    pagerAdapter.notifyDataSetChanged();
                                    if (mPager.getCurrentItem() != 0) {
                                        mPager.setCurrentItem(0);
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void initFragments() {
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
//        mPager.setPageTransformer(true, new DepthPageTransformer());
        pagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            private String getProTitle(int pos) {
                if (es == null) {
                    Log.e("ghui", "es is null");
                    return "";
                }
                String title = es.get(pos).children().get(0).text();
                if (title.isEmpty()) {
                    return getProTitle(pos - 1);
                }
                return title;
            }

            @Override
            public android.support.v4.app.Fragment getItem(int i) {
                GradesFragment fragment = new GradesFragment();
                Bundle args = new Bundle();
                args.putInt(GradesFragment.POS, i);
                fragment.setArguments(args);
                return fragment;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getProTitle(position);
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return es == null ? 0 : es.size();
            }

        };
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void search() {
        showProgressBar();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("sel_xnxq", values.get(currentSelection));
        data.put("button", "检索");
        Document doc = NetUtils.postDataToServer(this,Constants.INPUT_GRADE_INFO_INDEX_URL, data);
        if (doc == null) {
            return;
        }
        //todo parse doc           id="ID_Table"   <table
        es = doc.select("table[bgcolor=#89bfa7]").select("tbody").select("tr");
        Log.e("ghui", "es.size: " + es.size());
        dismissProgressBar();
    }
}