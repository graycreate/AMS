package me.ghui.AMS.UI;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import me.ghui.AMS.R;
import me.ghui.AMS.net.NetUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainActivity extends Activity {
    TextView tv_teacher_info ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }

    private void init() {

        tv_teacher_info = ((TextView) findViewById(R.id.tv_teacher_info));
    }

    public void onClick_getSelfInfo(View view) {
        Log.e("ghui", "get teacher Info...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetUtils.getTeaInfo();
                tv_teacher_info.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_teacher_info.setText("数据待解析................................................../n "+NetUtils.TEA_INFO_HTML);
                    }
                });
                Log.e("ghui","jjjjjjjjjjj: "+NetUtils.TEA_INFO_HTML);
                Document document = Jsoup.parse(NetUtils.TEA_INFO_HTML);
                Log.e("ghui", "数据待解析...");
                Log.e("ghui", "document:" + document);
            }
        }).start();
    }
}
