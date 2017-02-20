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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    ProgressDialog mProgress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button)findViewById(R.id.down);
        btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                mProgress = ProgressDialog.show(MainActivity.this,
                        "Wait", "Downloading...");
                DownThread  thread = new DownThread("http://192.168.0.56:8000/estates/");
                thread.start();
            }
        });
    }

    class DownThread extends Thread {
        String mAddr;

        DownThread(String addr) {
            mAddr = addr;
        }

        public void run() {
            String result = DownloadHtml(mAddr);
            Message message = mAfterDown.obtainMessage();
            message.obj = result;
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

    Handler mAfterDown = new Handler() {
        public void handleMessage(Message msg) {
            mProgress.dismiss();
            TextView result = (TextView)findViewById(R.id.result);
            result.setText((String)msg.obj);
        }
    };
}