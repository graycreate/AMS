package me.ghui.AMS.UI;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import me.ghui.AMS.R;
import me.ghui.AMS.net.NetUtils;
import me.ghui.AMS.utils.Constants;
import org.jsoup.Jsoup;
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
    private ArrayAdapter<String> adapter;
    private List<String> strings;
    private List<String> values;
    private Spinner spinner;
    private int currentPage = 0;
    private int currentSelection = 0;
    private Elements es;
    private TextView[] tvs;
    private int ids[] = {
            R.id.line1,
            R.id.line2,
            R.id.line3,
            R.id.line4,
            R.id.line5,
            R.id.line6,
            R.id.line7,
            R.id.line8,
            R.id.line9,
            R.id.line10,
            R.id.line11,
            R.id.line12,
            R.id.line13,
            R.id.line14,
            R.id.line15
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        findView();
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
            }
        }).start();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ghui", "current Selection: " + currentSelection + "; position: " + position);
                if (position != currentSelection) {
                    currentSelection = position;
                    search_onClick(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void findView() {
        tvs = new TextView[15];
        for (int i = 0; i < 15; i++) {
            tvs[i] = (TextView) findViewById(ids[i]);
        }
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


    public void search_onClick(View view) {
        showProgressBar();
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                spinner.post(new Runnable() {
                    @Override
                    public void run() {
                        fillData();
                    }
                });
                dismissProgressBar();
                Log.e("ghui", "doc::" + es.text());
            }
        }).start();
    }

    private void fillData() {
        Elements elements = es.get(currentPage).children();
        Log.e("ghui", "size:" + elements.size());
        for (int i = 1; i < elements.size(); i++) {
            tvs[i - 1].setText(elements.get(i).text());
        }
    }

}