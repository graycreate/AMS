package me.ghui.AMS.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import me.ghui.AMS.domain.User;
import me.ghui.AMS.utils.Constants;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghui on 3/28/14.
 */
public class NetUtils {
    public static String SESSION_ID = "";
    private static HttpClient httpClient = new DefaultHttpClient();

//    public static HttpClient getHttpClient() {
//        if (null == httpClient) {
//            return httpClient = new DefaultHttpClient();
//        }
//        return httpClient;
//    }

    public static Bitmap getValidateCode() {
        HttpGet httpGet = new HttpGet(Constants.VALIDATE_CODE_URL);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
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

    public static void login(final User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpPost httpPost = new HttpPost(Constants.LOGIN_URL);
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("UserID", user.getID()));
                pairs.add(new BasicNameValuePair("PassWord", user.getPassword()));
                pairs.add(new BasicNameValuePair("cCode", user.getValidateCode()));
                pairs.add(new BasicNameValuePair("Sel_Type", "TEA"));
                httpPost.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(pairs));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                HttpResponse response = null;
                try {
                    response = httpClient.execute(httpPost);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    Log.e("ghui", "login successfully!");
                    try {
                        Log.e("ghui", "content: " + EntityUtils.toString(response.getEntity()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
