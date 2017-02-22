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

        mProgress = ProgressDialog.show(MainActivity.this, "Wait", "Downloading...");

        DownThread  thread = new DownThread("http://52.79.106.71/estates");
        thread.start();
    }

    class DownThread extends Thread {
        String mAddr;

        DownThread(String addr) {
            mAddr = addr;
        }

        public void run() {
            String Json = Network.DownloadHtml(mAddr);

            ArrayList<Estate> estates = new ArrayList<Estate>();

            //Log.i("modelhouse", Json);

            try{
                JSONArray ja = new JSONArray(Json);

                //Log.i("modelhouse", ja.length()+"");

                for(int i=0; i<ja.length(); i++){
                    JSONObject estateObj = ja.getJSONObject(i);
                    Estate estate = new Estate(estateObj.getInt("id"), estateObj.getString("type"), estateObj.getString("photo"), estateObj.getString("price_type"),
                            estateObj.getString("price"),
                            estateObj.getString("extent"), estateObj.getString("category"), estateObj.getString("usearea"), estateObj.getString("facility"),
                            estateObj.getString("addr1"), estateObj.getString("info"));
                    estates.add(estate);
                }

                //Log.i("modelhouse", estates.size()+"");
            }catch (JSONException e){

            }

            Message message = mAfterDown.obtainMessage();
            message.obj = estates;
            mAfterDown.sendMessage(message);
        }
    }

    Handler mAfterDown = new Handler() {
        public void handleMessage(Message msg) {
            mProgress.dismiss();

            EstateList estateList = new EstateList(MainActivity.this, R.layout.estate_list, (ArrayList<Estate>)msg.obj);

            ListView MyList = (ListView)findViewById(R.id.list);
            MyList.setAdapter(estateList);

        }
    };
}