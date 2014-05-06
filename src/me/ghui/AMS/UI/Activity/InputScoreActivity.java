package me.ghui.AMS.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import me.ghui.AMS.R;
import me.ghui.AMS.net.NetUtils;
import me.ghui.AMS.utils.Constants;
import me.ghui.AMS.utils.MyApp;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ghui on 4/29/14.
 */
public class InputScoreActivity extends BaseActivity {
    private ListView listView;
    public static String[] datas = new String[3];
    private List<HashMap<String, String>> datum = new ArrayList<HashMap<String, String>>();
    private ScoreAdapter scoreAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("ghui", "onActivityResult");
        String text = null;
        int pos = requestCode;
        if (data == null) {
            return;
        }
        String input_status = data.getStringExtra("input_status"); //0:haven't insert, 1: insert, 2: only save
        datum.get(pos).put("status", input_status);
        scoreAdapter.notifyDataSetChanged();
    }

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
            Intent intent = new Intent(InputScoreActivity.this, InputScoreDialog.class);
            intent.putExtra("studentId", datum.get(position).get("id"));
            intent.putExtra("input_status", datum.get(position).get("status"));
            intent.putExtra("name", datum.get(position).get("name"));
            startActivityForResult(intent, position);
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
                Elements es = doc.select("table").get(2).select("tbody").select("tr[onclick=changeRowBgColor(this)]");
                Elements es_temp;
                String status = null;
                for (int i=0;i<es.size();i++) {
                    es_temp = es.get(i).children();
                    HashMap<String, String> maps = new HashMap<String, String>();
                    maps.put("id", es_temp.get(1).text());
                    maps.put("name", es_temp.get(2).text());
                    maps.put("sex", es_temp.get(3).text());
                    maps.put("style", es_temp.get(4).text());
                    String readonly = es_temp.get(9).select("input").attr("readonly");
                    String finalScore = es_temp.get(9).select("input").attr("value");
                    if (readonly.equals("true")) {
                        status = "已录入";
                    } else {
                        if (finalScore.isEmpty()) {
                            status = "未录入";
                        } else {
                            status = "已暂存";
                        }
                    }
                    maps.put("status", status);
                    datum.add(i, maps);
                }

                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        scoreAdapter = new ScoreAdapter(InputScoreActivity.this);
                        listView.setAdapter(scoreAdapter);
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
            return datum.size();
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
            if (position % 2 == 0) {
                convertView.setBackgroundColor(getResources().getColor(R.color.grey));
            } else {
                convertView.setBackgroundColor(Color.WHITE);
            }
            HashMap<String, String> maps = datum.get(position);
            viewHolder.id.setText(maps.get("id"));
            viewHolder.name.setText(maps.get("name"));
            viewHolder.sex.setText(maps.get("sex"));
            viewHolder.style.setText(maps.get("style"));
            String status = maps.get("status");
            int color;
            if (status.contains("已录入")) {
                color = Color.BLACK;
            } else if (status.contains("已暂存")) {
                color = Color.GREEN;
            } else {
                color = Color.RED;
            }
            viewHolder.input_status.setTextColor(color);
            viewHolder.input_status.setText(status);
            return convertView;
        }
    }
}
