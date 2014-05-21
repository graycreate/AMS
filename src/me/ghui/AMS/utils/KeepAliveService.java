package me.ghui.AMS.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import me.ghui.AMS.net.NetUtils;
import org.jsoup.nodes.Document;


/**
 * Created by vann on 5/18/14.
 */
public class KeepAliveService extends Service {
    private static final long TIME = 5 * 60 * 1000;
//    private static final long TIME = 5 * 1000;

    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //todo
                if (msg.what == 0) {
                    keepAlive();
                    handler.sendEmptyMessageDelayed(0, TIME);
                }
                return false;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Glog.e("KeepAliveService startted...");
        handler.sendEmptyMessage(0);
        return super.onStartCommand(intent, flags, startId);
    }

    private void keepAlive() {
        new Thread(new Runnable() {
            @Override
            public void run() {
               //todo
                String refer = Constants.BASE_URL + "/lyit/sys/menu.aspx";
                NetUtils.getDataFromServer(KeepAliveService.this,Constants.PSW_MOD_URL, refer);
                Glog.e("keepAlive...");
            }
        }).start();
    }



    @Override
    public void onDestroy() {
        Glog.e("KeepAliveService killed...");
        super.onDestroy();
    }
}

