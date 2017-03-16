package com.platformstory.modelhouse.Estate;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.platformstory.modelhouse.Common.Network;
import com.platformstory.modelhouse.Common.UtilLibs;
import com.platformstory.modelhouse.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EstateDetailActivity extends Activity implements MapView.MapViewEventListener, MapView.POIItemEventListener{
    ProgressDialog mProgress;
    ImageView img;
    Double latitude;
    Double longtitude;
    AdapterViewFlipper avf;
    ArrayList<Bitmap> bitmaps;
    ArrayList<String> imageUrls;

    TextView title_addr1;
    TextView user_name;
    TextView phone;
    Button phone_call;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estate_detail);

        Intent intent = getIntent();
        String estate_id = intent.getStringExtra("estate_id");
        latitude = intent.getDoubleExtra("latitude", 37.3219085636);
        longtitude = intent.getDoubleExtra("longtitude", 126.8308434601);

        mProgress = ProgressDialog.show(EstateDetailActivity.this, "Wait", "Downloading...");

        EstateDetailThread  thread = new EstateDetailThread(Network.URL + "estates/"+estate_id, "GET");
        thread.start();

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
        mapView.setDaumMapApiKey(UtilLibs.DAUM_API_KEY);
        mapView.setMapViewEventListener(EstateDetailActivity.this);
        mapView.setPOIItemEventListener(EstateDetailActivity.this);

        RelativeLayout mapViewContainer = (RelativeLayout) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
    }

    class EstateDetailThread extends Thread {
        String mAddr;
        String method;

        EstateDetailThread(String addr, String method) {
            mAddr = addr;
            this.method = method;
        }

        public void run() {
            String Json = Network.DownloadHtml(mAddr, method);

            Message message = mAfterDown.obtainMessage();
            message.obj = Json;
            mAfterDown.sendMessage(message);
        }
    }

//    class DownImageThread extends Thread{
//        ArrayList<String> imageUrls;
//
//        DownImageThread(ArrayList<String> imageUrls) {
//            this.imageUrls = imageUrls;
//        }
//
//        public void run() {
//            bitmaps = new ArrayList<Bitmap>();
//
//            for(int i=0; i<imageUrls.size(); i++){
//                Bitmap bit = Network.DownloadImage(imageUrls.get(i));
//                bitmaps.add(bit);
//            }
//
//            Message message = mAfterDownImage.obtainMessage();
//            message.obj = bitmaps;
//            mAfterDownImage.sendMessage(message);
//        }
//    }

    Handler mAfterDown = new Handler() {
        public void handleMessage(Message msg) {
            mProgress.dismiss();

            try{
                JSONArray ja = new JSONArray((String)msg.obj);

                JSONObject estate = ja.getJSONObject(0);

                // 추후 estate.getString("photo")를 콤마(,)를 기준으로 배열로 쪼개어 반복문을 돌릴 예정
                imageUrls = new ArrayList<String>();

                String[] photos = estate.getString("photo").split(",");
                for(int i=0; i<photos.length; i++){
                    imageUrls.add(photos[i]);
                }

//                bitmaps = (ArrayList<Bitmap>)msg.obj;

                avf = (AdapterViewFlipper) findViewById(R.id.detailed_photo);
                avf.setAdapter(new galleryAdapter(EstateDetailActivity.this));
                avf.startFlipping();

                ((Button)findViewById(R.id.showPrev)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        avf.showPrevious();
                    }
                });

                ((Button)findViewById(R.id.showNext)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        avf.showNext();
                    }
                });

//                imageUrls.add("http://blogfiles7.naver.net/data44/2009/1/18/198/21_goback2u.jpg");
//                imageUrls.add("http://cfile6.uf.tistory.com/image/213A6A4E588A9304335C66");
//                imageUrls.add("http://blogfiles3.naver.net/data41/2008/11/23/146/jinhae_62_goback2u_goback2u.jpg");
//                imageUrls.add("http://blogfiles9.naver.net/data41/2009/1/18/24/08_goback2u.jpg");

//                DownImageThread thread1 = new DownImageThread(imageUrls);
//                thread1.start();

//                Log.i("modelhouse", estate.getInt("id")+ estate.getString("type")+estate.getString("photo")+ estate.getString("price_type")+
//                        estate.getString("price")+
//                        estate.getString("extent")+ estate.getString("category")+ estate.getString("usearea")+ estate.getString("facility")+
//                        estate.getString("addr1")+ estate.getString("info"));

                title_addr1 = (TextView)findViewById(R.id.title_addr1);
                title_addr1.setText(estate.getString("addr1"));

                user_name = (TextView)findViewById(R.id.user_name);
                user_name.setText(estate.getString("name"));

                phone = (TextView)findViewById(R.id.phone);
                phone.setText(estate.getString("mobile"));

                phone_call = (Button)findViewById(R.id.phone_call);
                phone_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + phone.getText().toString()));
                        startActivity(intent);
                    }
                });


            }catch (JSONException e){

            }

            TextView estate_id = (TextView)findViewById(R.id.estate_id);
            estate_id.setText((String)msg.obj);
        }

    };

//    Handler mAfterDownImage = new Handler(){
//        public void handleMessage(Message msg){
//            bitmaps = (ArrayList<Bitmap>)msg.obj;
//
//            avf = (AdapterViewFlipper) findViewById(R.id.detailed_photo);
//            avf.setAdapter(new galleryAdapter(EstateDetailActivity.this));
//            avf.startFlipping();
//
//            ((Button)findViewById(R.id.showPrev)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    avf.showPrevious();
//                }
//            });
//
//            ((Button)findViewById(R.id.showNext)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    avf.showNext();
//                }
//            });
//        }
//    };

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

    public class galleryAdapter extends BaseAdapter{
        private final Context mContext;
        LayoutInflater inflater;

        public galleryAdapter(Context c){
            mContext = c;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = inflater.inflate(R.layout.estate_detail_photo, parent, false);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
//            imageView.setImageBitmap(bitmaps.get(position));
            Glide.with(mContext).load(Network.IMAGE_URL + "files/"+imageUrls.get(position)).into(imageView);
//            출처: http://ogoons.com/119 [오군의 기술 블로그]

            return convertView;
        }
    }
}


