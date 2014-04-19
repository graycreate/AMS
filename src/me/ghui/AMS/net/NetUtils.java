package me.ghui.AMS.net;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
import me.ghui.AMS.UI.Activity.NetWorkUnavailableDialog;
import me.ghui.AMS.domain.User;
import me.ghui.AMS.utils.Constants;
import me.ghui.AMS.utils.MyApp;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ghui on 3/28/14.
 */
public class NetUtils {
    public static String SESSION_ID = "";

    public static Bitmap getValidateCode(Context context) {
        if (!isNetWorkAvailable(context)) {
            return null;
        }
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(Constants.VALIDATE_CODE_URL);
        try {
            httpGet.setHeader("Referer", "http://211.84.112.49/lyit/_data/index_LOGIN.aspx");
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                SESSION_ID = httpClient.getCookieStore().getCookies().get(0).getValue();
                Log.e("ghui", "sessionId: " + SESSION_ID);
                InputStream inputStream = response.getEntity().getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }
    //                                验证码错误！<br>登录失败！           帐号或密码不正确！请重新输入。
    public static String login(Context context,final User user) {
        if (!isNetWorkAvailable(context)) {
            return null;
        }
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Constants.LOGIN_URL);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("UserID", user.getID()));
        pairs.add(new BasicNameValuePair("PassWord", user.getPassword()));
        pairs.add(new BasicNameValuePair("cCode", user.getValidateCode()));
        pairs.add(new BasicNameValuePair("Sel_Type", "TEA"));
        pairs.add(new BasicNameValuePair("typeName", "教师教辅人员"));
//        pairs.add(new BasicNameValuePair("sbtState", ""));
//        pairs.add(new BasicNameValuePair("pcInfo", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36undefined5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36 SN:NULL"));
        httpPost.setHeader("Cookie", "ASP.NET_SessionId=" + SESSION_ID);
//                httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
//                httpPost.setHeader("Host","211.84.112.49");
//               httpPost.setHeader("Origin","211.84.112.49");
        httpPost.setHeader("Referer", "http://211.84.112.49/lyit/_data/index_LOGIN.aspx");
//                httpPost.setHeader("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36");
//                httpPost.setHeader("Cache-Control","max-age=0");
//                httpPost.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
            HttpResponse response = httpClient.execute(httpPost);
            if (response == null) {
                return "网络错误！";
            }
            String result_str = EntityUtils.toString(response.getEntity());
            if (result_str.contains("验证码错误")) {
                Log.e("ghui", "验证码错误");
                return "验证码错误!";
            }else if (result_str.contains("帐号或密码不正确")) {
                Log.e("ghui", "帐号或密码不正确");
                return "帐号或密码不正确!";
            } else {
                Log.e("ghui", "登录成功!");
                return "登录成功";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "未知错误";
    }

    public static Document getDataFromServer(Context context,String url) {
        if (!isNetWorkAvailable(context)) {
            return null;
        }
        Document doc;
        try {
            doc = getConnection(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ghui", "doc is null");
            return getDataFromServer(context,url);
        }
        if (isTimeOut(doc)) {
            MyApp.getMyApp().restart("登录超时，请重新登录！");
        }
        return doc;
    }

    public static Document getDataFromServer(Context context,String url,String refer) {
        if (!isNetWorkAvailable(context)) {
            return null;
        }
        Document doc;
        try {
            doc = getConnection(url).referrer(refer).get();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ghui", "doc is null");
            return getDataFromServer(context,url);
        }
        if (isTimeOut(doc)) {
            MyApp.getMyApp().restart("登录超时，请重新登录！");
        }
        return doc;
    }
    public static Document postDataToServer(Context context,String url, Map<String, String> requestData) {
        if (!isNetWorkAvailable(context)) {
            return null;
        }
        Document doc;
        try {
            doc = getConnection(url).data(requestData).post();
        } catch (IOException e) {
            e.printStackTrace();
            return postDataToServer(context,url, requestData);
        }
        if (isTimeOut(doc)) {
            MyApp.getMyApp().restart("登录超时，请重新登录！");
        }
        return doc;
    }

    public static Document postDataToServer(Context context,String url, Map<String, String> requestData, String refer) {
        //todo
        if (!isNetWorkAvailable(context)) {
            return null;
        }
        Document doc;
        try {
            doc = getConnection(url).data(requestData).referrer(refer).post();
        } catch (IOException e) {
            e.printStackTrace();
            return postDataToServer(context, url, requestData, refer);
        }
        if (isTimeOut(doc)) {
            MyApp.getMyApp().restart("登录超时，请重新登录！");
        }
        return doc;
    }

    public static boolean isTimeOut(Document doc) {
        return doc.text().contains("您无权访问此页");
    }

    private static Connection getConnection(String url) {
        return Jsoup.connect(url).cookie("ASP.NET_SessionId", SESSION_ID);
    }

    public static boolean isConnectioned(Document doc) {
        //todo
        return !(doc == null);
    }
    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        boolean flag = false;
        if(activeNetInfo!=null){
            if(activeNetInfo.isAvailable()){
                flag = true;
            }else{
                flag= false;
            }
        }
        if (!flag) {
            context.startActivity(new Intent(context, NetWorkUnavailableDialog.class));
        }
        return flag;
    }
}


