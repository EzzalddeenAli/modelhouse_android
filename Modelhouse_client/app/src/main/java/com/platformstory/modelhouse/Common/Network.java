package com.platformstory.modelhouse.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.NetworkOnMainThreadException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Network {
    public static final String URL = "http://52.79.106.71/api/";

    public static boolean isNetworkAvailable(Context context){
        boolean available = false;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        if(info != null && info.isAvailable()){
            available = true;
        }

        return available;
    }

    public static String DownloadHtml(String addr, String method) {
        StringBuilder html = new StringBuilder();
        try {
            URL url = new URL(addr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);
                conn.setRequestMethod(method);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    for (;;) {
                        String line = br.readLine();
                        if (line == null) break;
                        html.append(line + '\n');
                    }
                    br.close();
                }
                conn.disconnect();
            }
        } catch (NetworkOnMainThreadException e) {
            return "Error : 메인 스레드 네트워크 작업 에러 - " + e.getMessage();
        } catch (Exception e) {
            return "Error : " + e.getMessage();
        }
        return html.toString();
    }

    public static Bitmap DownloadImage(String addr){
        Bitmap bit;
        try {
            InputStream is = new URL(addr).openStream();
            bit = BitmapFactory.decodeStream(is);
            is.close();
        }catch (Exception e){
            bit = null;
        }
        return bit;
    }

}
