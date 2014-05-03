package me.ghui.AMS.UI.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import me.ghui.AMS.R;
import me.ghui.AMS.net.NetUtils;
import me.ghui.AMS.utils.Constants;
import me.ghui.AMS.utils.MyApp;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ghui on 4/29/14.
 */
public class InputScoreActivity extends BaseActivity {
    private ListView listView;
    public static String[] datas = new String[3];
    public static Elements es;

    @Override
    public int getLayoutResourceId() {
        return R.layout.input_score_activity;
    }

    @Override
    public void init() {
        String requestData = getIntent().getStringExtra("data");
        Log.e("ghui", "request data: " + requestData);
        requestData = requestData.replace("?xnxq=", "");
        requestData = requestData.replace("&kcdm=", ",");
        requestData = requestData.replace("&skbj=", ",");
        Log.e("ghui", "new data: " + requestData);
        datas = requestData.split(",");
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new MyListViewOnItemClickListener());
        getData();
    }

    class MyListViewOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("ghui", "OnItemClickListener: " + position);
            Elements es_temp = es.get(position).children();
            Intent intent = new Intent(InputScoreActivity.this, InputScoreDialog.class);
            intent.putExtra("studentId",es_temp.get(1).text() );
            startActivityForResult(intent, 1);
        }
    }

    private void getData() {
        showProgressBar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("hid_xnxq", datas[0]);
                data.put("sel_kc", datas[1]);
                data.put("sel_skbj", datas[2]);
                data.put("hid_user", MyApp.userid);
                Document doc = NetUtils.postDataToServer(InputScoreActivity.this, Constants.INPUT_SCORE_URL, data);
                es = doc.select("table").get(2).select("tbody").select("tr[onclick=changeRowBgColor(this)]");
                Log.e("ghui", "es.size: " + es.size());
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(new ScoreAdapter(InputScoreActivity.this));
                    }
                });
                dismissProgressBar();
            }
        }).start();
    }

    class ScoreAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        class ViewHolder {
            TextView name;
            TextView sex;
            TextView id;
            TextView style;
            TextView input_status;
        }

        public ScoreAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return es == null ? 0 : es.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.score_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name_tv);
                viewHolder.sex = (TextView) convertView.findViewById(R.id.sex_tv);
                viewHolder.id = (TextView) convertView.findViewById(R.id.id_tv);
                viewHolder.style = (TextView) convertView.findViewById(R.id.style_tv);
                viewHolder.input_status = (TextView) convertView.findViewById(R.id.input_status);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Elements es_temp = es.get(position).children();
            viewHolder.id.setText(es_temp.get(1).text());
            viewHolder.name.setText(es_temp.get(2).text());
            viewHolder.sex.setText(es_temp.get(3).text());
            viewHolder.style.setText(es_temp.get(4).text());
            String readonly = es_temp.get(9).select("input").attr("readonly");
            String finalScore = es_temp.get(9).select("input").attr("value");
            Log.e("ghui", "flag = " + readonly);
            Log.e("ghui", "final score: " + finalScore);
            if (readonly.equals("true")) {
                viewHolder.input_status.setText("已录入");
            } else {
                if (finalScore.isEmpty()) {
                    viewHolder.input_status.setText("未录入");
                } else {
                    viewHolder.input_status.setText("已暂存");
                }
            }
            return convertView;
        }
    }
}
