package com.platformstory.modelhouse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {
    ProgressDialog mProgress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String Json = DownloadHtml("http://52.79.106.71/estates");
//
//        Log.i("modelhouse", Json);
//
//        try{
//            ArrayList<Estate> estates = new ArrayList<Estate>();
//            JSONArray ja = new JSONArray(Json);
//
//            for(int i=0; i<3; i++){
//                JSONObject estateObj = ja.getJSONObject(i);
//                Estate estate = new Estate(estateObj.getInt("id"), estateObj.getInt("type"), estateObj.getString("photo"), estateObj.getInt("price_type"),
//                        estateObj.getString("price"), estateObj.getString("monthly_price"), estateObj.getString("annual_price"),
//                        estateObj.getString("extent"), estateObj.getString("category"), estateObj.getString("usearea"), estateObj.getString("facility"),
//                        estateObj.getString("addr1"), estateObj.getDouble("latitude"), estateObj.getDouble("longtitude"), estateObj.getString("info"));
//                estates.add(estate);
//            }
//
//            EstateList estateList = new EstateList(MainActivity.this, R.layout.estate_list, estates);
//
//            ListView MyList = (ListView)findViewById(R.id.list);
//            MyList.setAdapter(estateList);
//
//        }catch(JSONException e){
//
//        }

        Button btn = (Button)findViewById(R.id.down);
        btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                mProgress = ProgressDialog.show(MainActivity.this,
                        "Wait", "Downloading...");


                DownThread  thread = new DownThread("http://52.79.106.71/estates");
                thread.start();
            }
        });
    }

    String DownloadHtml(String addr) {
        StringBuilder html = new StringBuilder();
        try {
            URL url = new URL(addr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);
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


    class DownThread extends Thread {
        String mAddr;

        DownThread(String addr) {
            mAddr = addr;
        }

        public void run() {
            String Json = DownloadHtml(mAddr);

            String result="";
//            try{
//                ArrayList<Estate> estates = new ArrayList<Estate>();
//                JSONArray ja = new JSONArray(Json);
//
//                for(int i=0; i<3; i++){
//                    JSONObject estateObj = ja.getJSONObject(i);
//                    Estate estate = new Estate(estateObj.getInt("id"), estateObj.getInt("type"), estateObj.getString("photo"), estateObj.getInt("price_type"),
//                                        estateObj.getString("price"), estateObj.getString("monthly_price"), estateObj.getString("annual_price"),
//                                        estateObj.getString("extent"), estateObj.getString("category"), estateObj.getString("usearea"), estateObj.getString("facility"),
//                                        estateObj.getString("addr1"), estateObj.getDouble("latitude"), estateObj.getDouble("longtitude"), estateObj.getString("info"));
//                    estates.add(estate);
//                }
//
//                EstateList estateList = new EstateList(MainActivity.this, R.layout.estate_list, estates);
//
//                ListView MyList = (ListView)findViewById(R.id.list);
//                MyList.setAdapter(estateList);
//
//            }catch(JSONException e){
//
//            }

            Message message = mAfterDown.obtainMessage();
            message.obj = Json;
            mAfterDown.sendMessage(message);
        }

        String DownloadHtml(String addr) {
            StringBuilder html = new StringBuilder();
            try {
                URL url = new URL(addr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
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
    }

    Handler mAfterDown =
            new Handler() {
        public void handleMessage(Message msg) {
            mProgress.dismiss();
            TextView result = (TextView)findViewById(R.id.result);
            result.setText((String)msg.obj);
        }
    };
}