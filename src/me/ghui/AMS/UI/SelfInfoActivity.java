package me.ghui.AMS.UI;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import me.ghui.AMS.R;
import me.ghui.AMS.net.NetUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by ghui on 4/7/14.
 */
public class SelfInfoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_info);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetUtils.getTeaInfo();
                Log.e("ghui", "jjjjjjjjjjj: " + NetUtils.TEA_INFO_HTML);
                Document document = Jsoup.parse(NetUtils.TEA_INFO_HTML);
                Log.e("ghui", "数据待解析...");
                Log.e("ghui", "document:" + document);
            }
        }).start();
    }
}
