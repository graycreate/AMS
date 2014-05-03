package me.ghui.AMS.UI.Activity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import me.ghui.AMS.R;
import me.ghui.AMS.net.NetUtils;
import me.ghui.AMS.utils.Constants;
import me.ghui.AMS.utils.MyApp;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

/**
 * Created by vann on 5/3/14.
 */
public class InputScoreDialog extends BaseActivity {
    EditText score1_et;
    EditText score2_et;
    EditText score3_et;
    EditText score4_et;
    EditText score5_et;
    String refer = "http://211.84.112.49/lyit/XSCJ/KCCJ_ADD_rpt_T.aspx";
    String student_id;
    Elements es;
    Elements element_input;
    private Spinner remark_spinner;
    private String[] strings = {"", "舞弊", "缺考", "缓考", "取消考试资格", "免修"};
    private String[] values = {"","01|0.00|","02|0.00|","03||","04|0.00|","05|65.02|1"};
    HashMap<String, String> datas = new HashMap<String, String>();

    @Override
    public int getLayoutResourceId() {
        return R.layout.input_score_dialog;
    }

    @Override
    public void init() {
        findView();
        student_id = getIntent().getStringExtra("studentId");
        Log.e("ghui", "studentId: " + student_id);
        getData();
    }

    private void initView() {
        Elements et_temp = element_input.select("td[class=CJTD]").select("input");
        String remark = element_input.get(11).text();
        Log.e("ghui", "remark: " + remark);
        if (remark.length() > 6) {
            remark_spinner.setEnabled(true);
            remark = element_input.get(11).child(0).select("option[selected]").text();
            Log.e("ghui", "remark2: " + remark);
        } else {
            remark_spinner.setEnabled(false);
        }
        if (remark.isEmpty()) {
            remark_spinner.setSelection(0);
        } else if (remark.contains("舞弊")) {
            remark_spinner.setSelection(1);
        } else if (remark.contains("缺考")) {
            remark_spinner.setSelection(2);
        } else if (remark.contains("缓考")) {
            remark_spinner.setSelection(3);
        } else if (remark.contains("免修")) {
            remark_spinner.setSelection(5);
        } else {
            remark_spinner.setSelection(4);
        }

        Log.e("ghui", "et_temp.size: " + et_temp.size());
        String readonly1 = et_temp.get(0).attr("readonly");
        String value1 = et_temp.get(0).attr("value");
        String readonly2 = et_temp.get(1).attr("readonly");
        String value2 = et_temp.get(1).attr("value");
        String readonly3 = et_temp.get(2).attr("readonly");
        String value3 = et_temp.get(2).attr("value");
        String readonly4 = et_temp.get(3).attr("readonly");
        String value4 = et_temp.get(3).attr("value");
        String readonly5 = et_temp.get(4).attr("readonly");
        String value5 = et_temp.get(4).attr("value");
        if (readonly1.equals("true")) {
            score1_et.setEnabled(false);
        } else {
            score1_et.setEnabled(true);
        }
        if (readonly2.equals("true")) {
            score2_et.setEnabled(false);
        } else {
            score2_et.setEnabled(true);
        }
        if (readonly3.equals("true")) {
            score3_et.setEnabled(false);
        } else {
            score3_et.setEnabled(true);
        }
        if (readonly4.equals("true")) {
            score4_et.setEnabled(false);
        } else {
            score4_et.setEnabled(true);
        }
        if (readonly5.equals("true")) {
            score5_et.setEnabled(false);
        } else {
            score5_et.setEnabled(true);
        }
        score1_et.setText(value1);
        score2_et.setText(value2);
        score3_et.setText(value3);
        score4_et.setText(value4);
        score5_et.setText(value5);
    }


    private void findView() {
        score1_et = (EditText) findViewById(R.id.score1_et);
        score2_et = (EditText) findViewById(R.id.score2_et);
        score3_et = (EditText) findViewById(R.id.score3_et);
        score4_et = (EditText) findViewById(R.id.score4_et);
        score5_et = (EditText) findViewById(R.id.score5_et);
        remark_spinner = (Spinner) findViewById(R.id.remark_spinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, strings);
        remark_spinner.setAdapter(arrayAdapter);
    }


    private void getData() {
        showProgressBar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("txt_xh", student_id);
                data.put("hid_user", MyApp.userid);
                data.put("hid_xnxq", InputScoreActivity.datas[0]);
                data.put("sel_kc", InputScoreActivity.datas[1]);
                data.put("sel_skbj", InputScoreActivity.datas[2]);
                Document doc = NetUtils.postDataToServer(InputScoreDialog.this, Constants.INPUT_SCORE_URL, data);
                es = doc.select("form").select("table").get(2).select("tbody");
                Elements es_temp = es.select("input[type=hidden]");
                for (Element e: es_temp) {
                    datas.put(e.attr("name"), e.attr("value"));
                }
                element_input = es.select("tr").select("td");
                Log.e("ghui", "es.text: " + es.text());
                score1_et.post(new Runnable() {
                    @Override
                    public void run() {
                        initView();
                    }
                });
                dismissProgressBar();
            }
        }).start();

    }

    public void cancle_OnClick(View view) {
        finish();
    }

    public void insertScore_OnClick(View view) {
        showProgressBar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("ghui", "insert !!!");
                datas.put("CHKPSCJ1", score1_et.getText().toString());
                datas.put("CHKQZCJ1", score2_et.getText().toString());
                datas.put("CHKQMCJ1", score3_et.getText().toString());
                datas.put("CHKJNCJ1", score4_et.getText().toString());
                datas.put("CHKZHCJ1", score5_et.getText().toString());
                datas.put("sel_QMTSQK1", values[remark_spinner.getSelectedItemPosition()]);
                Log.e("ghui", "datas: " +datas);
                Document doc = NetUtils.postDataToServer(InputScoreDialog.this, "http://211.84.112.49/lyit/XSCJ/KCCJ_ADD_rpt_T.aspx?f=ok",datas, refer);
                Log.e("ghui", "insert doc: " + doc.text());
                dismissProgressBar();
            }
        }).start();
    }
}