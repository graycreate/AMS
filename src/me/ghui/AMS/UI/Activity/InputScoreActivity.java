package me.ghui.AMS.UI.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import me.ghui.AMS.R;
import me.ghui.AMS.net.NetUtils;
import me.ghui.AMS.utils.Constants;
import me.ghui.AMS.utils.MyApp;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ghui on 4/29/14.
 */
public class InputScoreActivity extends BaseActivity {
    private ListView listView;
    String[] datas = new String[3];

    @Override
    public int getLayoutResourceId() {
        return R.layout.input_score_activity;
    }
    //   http://211.84.112.49/lyit/XSCJ/KCCJ_ADD_rpt_T.aspx

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
        listView.setAdapter(new ScoreAdapter(this));
        getData();
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
                Document doc = NetUtils.postDataToServer(InputScoreActivity.this, "http://211.84.112.49/lyit/XSCJ/KCCJ_ADD_rpt_T.aspx", data);
                Log.e("ghui", "doc : " + doc.text());
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
            TextView score1;
            TextView score2;
            TextView score3;
            TextView score4;
            TextView score5;
            TextView remark;
        }

        public ScoreAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 3;
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
                viewHolder.score1 = (TextView) convertView.findViewById(R.id.score1);
                viewHolder.score2 = (TextView) convertView.findViewById(R.id.score2);
                viewHolder.score3 = (TextView) convertView.findViewById(R.id.score3);
                viewHolder.score4 = (TextView) convertView.findViewById(R.id.score4);
                viewHolder.score5 = (TextView) convertView.findViewById(R.id.score5);
                viewHolder.remark = (TextView) convertView.findViewById(R.id.remark);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.name.setText("张三");
            return convertView;
        }
    }
}
