package com.platformstory.modelhouse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends FragmentActivity {
    public static final String PREFS_NAME = "ModelhouseLoc";

    GoogleMap gMap;
    double latitude;
    double longitude;
    int zoom;

    String JSON;
    ProgressDialog mProgress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgress = ProgressDialog.show(MainActivity.this, "Wait", "Downloading...");

        //앱이 종료되었을 때 지도의 좌표, 줌 값을 가져와 그대로 반영, 설정값이 없을 경우 서울을 중심으로 보여줌
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        latitude = Double.parseDouble(settings.getString("latitude", "37.5668260055"));
        longitude = Double.parseDouble(settings.getString("longitude", "126.9786567859"));
        zoom = settings.getInt("zoom", 14);

        // 지도의 좌표, 줌 값을 토대로 지도 중심 반경 내의 매물을 검색하여 클러스터로 띄움
        EstateSearchMapThread thread = new EstateSearchMapThread("http://52.79.106.71/estates?latitude="+latitude+"&longtitude="+longitude+"&zoom="+zoom);
        thread.start();

        gMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EstateSearchListActivity.class);
                startActivity(intent);
            }
        });

        gMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                latitude = cameraPosition.target.latitude;
                longitude = cameraPosition.target.longitude;

                // 지도의 좌표, 줌 값을 토대로 지도 중심 반경 내의 매물을 검색하여 클러스터로 띄움
                EstateSearchMapThread thread = new EstateSearchMapThread("http://52.79.106.71/estates?latitude="+latitude+"&longtitude="+longitude+"&zoom="+zoom);
                thread.start();
            }
        });

    }

    class EstateSearchMapThread extends Thread {
        String mAddr;

        EstateSearchMapThread(String mAddr) {
            this.mAddr = mAddr;
        }

        public void run() {
            String Json = Network.DownloadHtml(mAddr);

            Message message = mAfterDown.obtainMessage();
            message.obj = Json;
            mAfterDown.sendMessage(message);
        }
    }

    Handler mAfterDown = new Handler() {
        public void handleMessage(Message msg) {
            mProgress.dismiss();

            try{
                JSON = (String)msg.obj;
                JSONArray ja = new JSONArray(JSON);

                for(int i=0; i<ja.length(); i++) {
                    JSONObject estateObj = ja.getJSONObject(i);

                    Log.i("modelhouse", estateObj.getDouble("latitude")+", "+estateObj.getDouble("longtitude"));
                    //estateObj.getDouble("latitude"); estateObj.getDouble("longtitude");
                }
                //위도, 경도 값들을 토대로 지도에 클러스트를 만들고, 리스트 버튼 클릭 시 제이슨 배열을 인텐트로 넘겨주는 로직을 이곳에 작성


//                Button button = (Button)findViewById(R.id.button);
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(MainActivity.this, EstateSearchListActivity.class);
//                        intent.putExtra("JSON", JSON);
//                        startActivity(intent);
//                    }
//                });
            }catch (JSONException e){

            }
        }
    };

    //앱이 종료되면 현재 지도의 좌표, 줌 값을 저장
    @Override
    public void onStop(){
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("latitude", latitude+"");
        editor.putString("longitude", longitude+"");
        editor.putInt("zoom", zoom);
        editor.commit();
    }
}