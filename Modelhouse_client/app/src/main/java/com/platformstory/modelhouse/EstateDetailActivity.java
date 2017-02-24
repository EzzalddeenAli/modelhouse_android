package com.platformstory.modelhouse;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EstateDetailActivity extends Activity implements MapView.MapViewEventListener, MapView.POIItemEventListener{
    ProgressDialog mProgress;
    ImageView img;
    Double latitude;
    Double longtitude;

    final String API_KEY = "c28ce2a178185b5577a869519a3ac74b";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estate_detail);

        Intent intent = getIntent();
        String estate_id = intent.getStringExtra("estate_id");
        latitude = intent.getDoubleExtra("latitude", 37.3219085636);
        longtitude = intent.getDoubleExtra("longtitude", 126.8308434601);

        mProgress = ProgressDialog.show(EstateDetailActivity.this, "Wait", "Downloading...");

        EstateDetailThread  thread = new EstateDetailThread("http://52.79.106.71/estates/"+estate_id);
        thread.start();

        DownImageThread thread1 = new DownImageThread("http://cfile6.uf.tistory.com/image/213A6A4E588A9304335C66");
        thread1.start();

        final TextView like_it = (TextView)findViewById(R.id.like_it);
        like_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(like_it.getText().toString().equals("♡")){
                    like_it.setText("♥");
                }else{
                    like_it.setText("♡");
                }
            }
        });

        MapView mapView = new MapView(EstateDetailActivity.this);
        mapView.setDaumMapApiKey(API_KEY);
        mapView.setMapViewEventListener(EstateDetailActivity.this);
        mapView.setPOIItemEventListener(EstateDetailActivity.this);

        RelativeLayout mapViewContainer = (RelativeLayout) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
    }

    class EstateDetailThread extends Thread {
        String mAddr;

        EstateDetailThread(String addr) {
            mAddr = addr;
        }

        public void run() {
            String Json = Network.DownloadHtml(mAddr);

            Message message = mAfterDown.obtainMessage();
            message.obj = Json;
            mAfterDown.sendMessage(message);
        }
    }

    class DownImageThread extends Thread{
        String mAddr;

        DownImageThread(String addr) {
            mAddr = addr;
        }

        public void run() {
            Bitmap bit = Network.DownloadImage(mAddr);

            Message message = mAfterDownImage.obtainMessage();
            message.obj = bit;
            mAfterDownImage.sendMessage(message);
        }
    }

    Handler mAfterDown = new Handler() {
        public void handleMessage(Message msg) {
            mProgress.dismiss();

            try{
                JSONArray ja = new JSONArray((String)msg.obj);

                JSONObject estate = ja.getJSONObject(0);

//                Log.i("modelhouse", estate.getInt("id")+ estate.getString("type")+estate.getString("photo")+ estate.getString("price_type")+
//                        estate.getString("price")+
//                        estate.getString("extent")+ estate.getString("category")+ estate.getString("usearea")+ estate.getString("facility")+
//                        estate.getString("addr1")+ estate.getString("info"));

                TextView title_addr1 = (TextView)findViewById(R.id.title_addr1);
                title_addr1.setText(estate.getString("addr1"));


            }catch (JSONException e){

            }

            TextView estate_id = (TextView)findViewById(R.id.estate_id);
            estate_id.setText((String)msg.obj);
        }

    };

    Handler mAfterDownImage = new Handler(){
        public void handleMessage(Message msg){
            img = (ImageView)findViewById(R.id.detailed_photo);
            Bitmap bit = (Bitmap)msg.obj;
            if (bit == null) {
                Log.i("modelhouse", "그림을 불러올 수 없음");
            } else {
                img.setImageBitmap(bit);
            }
        }
    };

    @Override
    public void onMapViewInitialized(MapView mapView) {
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longtitude), true);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longtitude));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);
        mapView.selectPOIItem(marker, true);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longtitude), false);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }


    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
