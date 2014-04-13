package me.ghui.AMS.UI.Activity;

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
import me.ghui.AMS.UI.Fragment.CourseItemFragment;
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
public class CourseActivity extends BaseActivity {
    private ViewPager mPager;
    private ArrayAdapter<String> adapter;
    private List<String> strings;
    private List<String> values;
    private Spinner spinner;
    private int currentSelection = 0;
    public Elements es;
    public FragmentStatePagerAdapter pagerAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragments();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.course;
    }

    @Override
    public void init() {
        strings = new ArrayList<String>();
        values = new ArrayList<String>();
        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, strings);
        showProgressBar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = NetUtils.getDataFromServer(Constants.TERM_INFO_URL);
                Elements elements = doc.select("option[value]");
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
                            //todo
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
        mPager = (ViewPager) findViewById(R.id.vPager);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
//        mPager.setPageTransformer(true, new DepthPageTransformer());
        pagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            private String getProTitle(int pos) {
                String title = es.get(pos).children().get(2).text();
                if (title.isEmpty()) {
                    return getProTitle(pos - 1);
                }
                return title;
            }

            @Override
            public android.support.v4.app.Fragment getItem(int i) {
                CourseItemFragment fragment = new CourseItemFragment();
                Bundle args = new Bundle();
                args.putInt(CourseItemFragment.POS, i);
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
                return es == null ? 1 : es.size();
            }

        };
    }

    /*
        承担单位 1
        课程  2
        学分
        总学时
        讲授学时
        实验学时
        上机学时
        其他学时
        周学时
        周次
        单双周
        授课方式
        上课班号 null
        合班人数
     */

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
//        String referer = "http://211.84.112.49/lyit/znpk/Pri_TeacKCJXRW.aspx";
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("Sel_XNXQ", values.get(currentSelection));
        data.put("Submit01", "检索");
        Document doc = NetUtils.postDataToServer(Constants.COURSE_INFO_URL, data);
        es = doc.select("tbody").get(1).select("tr[class!=T]");
        dismissProgressBar();
        Log.e("ghui", "doc::" + es.text());
    }
}