package me.ghui.AMS.UI.Activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import me.ghui.AMS.R;
import me.ghui.AMS.net.NetUtils;
import me.ghui.AMS.utils.Constants;
import me.ghui.AMS.utils.MyApp;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ghui on 4/7/14.
 */
public class ModPswActivity extends BaseActivity {
    String userName;
    String Id;
    EditText usrName_et;
    EditText Id_et;
    EditText psw_old_et;
    EditText psw_et;
    EditText psw_confirm_et;

    @Override
    public int getLayoutResourceId() {
        return R.layout.mod_psw;
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
                String refer = "http://211.84.112.48/lyit/sys/menu.aspx";
                Document document = NetUtils.getDataFromServer(ModPswActivity.this,Constants.PSW_MOD_URL, refer);
                if (document == null) {
                    return;
                }
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

/*
oldPWD:65928088
NewPWD:1
CNewPWD:1
   Referer:http://211.84.112.49/lyit/MyWeb/User_ModPWD.aspx
                    Request URL:http://211.84.112.49/lyit/MyWeb/User_ModPWD.aspx
 */
    public void ModifyPSW_onClick(View view) {
        if (!check()) {
            return;
        }
        final Map<String, String> data = new HashMap<String, String>();
        data.put("NewPWD", psw_et.getText().toString());
        data.put("CNewPWD", psw_confirm_et.getText().toString());
        data.put("oldPWD", psw_old_et.getText().toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                //todo post the request...
                showProgressBar();
                Document doc = NetUtils.postDataToServer(ModPswActivity.this,Constants.PSW_MOD_URL, data,Constants.PSW_MOD_URL);
                if (doc == null) {
                    return;
                }
                final boolean result = doc.text().toString().contains("您的密码已经修改成功");
                final String result_str = result?"您的密码已经修改成功!请重新登录":"密码修改遇到问题！";
                    psw_et.post(new Runnable() {
                        @Override
                        public void run() {
                            if (result) {
                                MyApp.getMyApp().restart(result_str);
                            }
                        }
                    });
                dismissProgressBar();
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