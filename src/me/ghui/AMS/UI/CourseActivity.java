package me.ghui.AMS.UI;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import me.ghui.AMS.R;
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

    private void initFragments() {
        mPager = (ViewPager) findViewById(R.id.vPager);

        pagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int i) {
                CourseItemFragment fragment = new CourseItemFragment();
                Bundle args = new Bundle();
                args.putInt(CourseItemFragment.POS, i);
                fragment.setArguments(args);
                return fragment;
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                super.destroyItem(container, position, object);
                //do nothing
            }


            @Override
            public int getCount() {
                if (es == null) {
                    return 1;
                }
                return es.size();
            }

        };
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
                if (doc == null) {
                    Log.e("ghui", "doc is null");
                    return;
                }
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
                            pagerAdapter.notifyDataSetChanged();
                            for (int i=0;i<pagerAdapter.getCount();i++) {
                                ((onElementsChangedListener)pagerAdapter.getItem(i)).onElementsChanged(es);
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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

    public void search() {
        showProgressBar();
        String referer = "http://211.84.112.49/lyit/znpk/Pri_TeacKCJXRW.aspx";
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("Sel_XNXQ", values.get(currentSelection));
        data.put("Submit01", "检索");
                /*
               讲授/上机 序号 承担单位 课程 学分 总学时 讲授 学时 实验 学时 上机 学时 其他 学时 周学时 周次 单双周 授课 方式 上课班级 上课班号 上课班级名称 合班人数
               1 计算机与信息工程系 [050086]单片机原理与应用 4.0 64.0 54.0 10.0 0.0 0.0 4.0 1-19 讲授 002 56
               2 教务处 [007218]PPT从入门到精通 1.5 24.0 24.0 0.0 0.0 0.0 4.0 1-19 讲授 001 219
               3 24.0 24.0 0.0 0.0 0.0 4.0 1-19 讲授 002 169
                 */
        Document doc = NetUtils.postDataToServer(Constants.COURSE_INFO_URL, data, referer);
        es = doc.select("tbody").get(1).select("tr[class!=T]");
        dismissProgressBar();
        Log.e("ghui", "doc::" + es.text());
    }

    public interface onElementsChangedListener {
        public void onElementsChanged(Elements es);
    }
}