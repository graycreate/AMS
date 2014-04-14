package me.ghui.AMS.UI.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import me.ghui.AMS.R;
import me.ghui.AMS.net.NetUtils;
import me.ghui.AMS.utils.Constants;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by ghui on 4/7/14.
 */
public class OthersActivity extends BaseActivity {
    String userName;
    String Id;
    EditText usrName_et;
    EditText Id_et;
    EditText psw_old_et;
    EditText psw_et;
    EditText psw_confirm_et;

    @Override
    public int getLayoutResourceId() {
        return R.layout.others;
    }

    @Override
    public void init() {
        showProgressBar();
        usrName_et = (EditText) findViewById(R.id.user_name);
        Id_et = (EditText) findViewById(R.id.id);
        psw_old_et = (EditText) findViewById(R.id.psw_old);
        psw_et = (EditText) findViewById(R.id.psw);
        psw_confirm_et = (EditText) findViewById(R.id.psw_confirm);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String refer = "http://211.84.112.49/lyit/sys/menu.aspx";
                Document document = NetUtils.getDataFromServer(Constants.PSW_MOD_URL, refer);
                Elements elements = document.select("input");
                userName = elements.get(1).attr("value");
                Id = elements.get(2).attr("value");
                Log.e("ghui", "userName:" + userName + " ;Id: " + Id);
                Id_et.post(new Runnable() {
                    @Override
                    public void run() {
                        usrName_et.setText(userName);
                        Id_et.setText(Id);
                        dismissProgressBar();
                    }
                });
            }
        }).start();
    }


    public void ModifyPSW_onClick(View view) {
        if (!check()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                //todo post the request...
            }
        }).start();
    }

    private boolean check() {
        if (psw_old_et.getText().toString().isEmpty()) {
            showToast("请输入原密码！");
            return false;
        }
        //1. check new psw isEmpty
        if (psw_et.getText().toString().isEmpty()) {
            showToast("新密码不能为空!");
            return false;
        }
        //2. check confirm newPsw == newPsw_confirm
        if (!psw_confirm_et.getText().toString().equals(psw_et.getText().toString())) {
            showToast("密码两次输入不一致!");
            return false;
        }
        //3. check old psw isRight
        //4. return true;
        return true;
    }
}