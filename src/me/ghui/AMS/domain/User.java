package me.ghui.AMS.domain;

import android.util.Log;
import me.ghui.AMS.net.NetUtils;
import me.ghui.AMS.utils.Constants;
import org.jsoup.nodes.Document;

import java.util.HashMap;

/**
 * Created by ghui on 3/25/14.
 */
public class User {
    private String ID;

    public String getID() {
        return ID;
    }

    public User setID(String ID) {
        this.ID = ID;
        return this;
    }

    public String getPassword() {
        return Password;
    }

    public User setPassword(String password) {
        Password = password;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID='" + ID + '\'' +
                ", Password='" + Password + '\'' +
                ", ValidateCode='" + ValidateCode + '\'' +
                '}';
    }

    private String Password;

    private String ValidateCode;

    public User setValidateCode(String validateCode) {
        ValidateCode = validateCode;
        return this;
    }

    public String getValidateCode() {
        return ValidateCode;
    }
//    pairs.add(new BasicNameValuePair("UserID", user.getID()));
//    pairs.add(new BasicNameValuePair("PassWord", user.getPassword()));
//    pairs.add(new BasicNameValuePair("cCode", user.getValidateCode()));
//    pairs.add(new BasicNameValuePair("Sel_Type", "TEA"));
//    pairs.add(new BasicNameValuePair("typeName", "教师教辅人员"));

    public boolean login() {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("UserID", getID());
        data.put("PassWord", getPassword());
        data.put("cCode", getValidateCode());
        data.put("Sel_Type","TEA");
        data.put("typeName","教师教辅人员");
        Log.e("ghui", "id: " + getID());
        Log.e("ghui", "psw: " + getPassword());
        Log.e("ghui", "cCode: " + getValidateCode());
        Log.e("ghui", "Sel_Type: " + data.get("typeName"));
        Document doc = NetUtils.postDataToServer(Constants.LOGIN_URL, data, "http://211.84.112.49/lyit/_data/index_LOGIN.aspx");
//        Log.e("ghui", "doc: " + doc.text());
        return NetUtils.isConnectioned(doc);
    }
}
