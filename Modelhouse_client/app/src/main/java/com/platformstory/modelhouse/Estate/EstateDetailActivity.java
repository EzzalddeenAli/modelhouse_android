package com.platformstory.modelhouse.Estate;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
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
import com.platformstory.modelhouse.DTO.Estate;
import com.platformstory.modelhouse.DTO.NetworkService;
import com.platformstory.modelhouse.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class EstateDetailActivity extends Activity implements MapView.MapViewEventListener, MapView.POIItemEventListener{
    ProgressDialog mProgress;
    ImageView img;
    Double latitude;
    Double longtitude;
    AdapterViewFlipper avf;
    ArrayList<Bitmap> bitmaps;
    ArrayList<String> imageUrls;

    String estate_id;

    TextView title_addr1;
    TextView user_name;
    TextView phone;
    Button phone_call;

    Estate estate_info;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estate_detail);

        // 매물 결과 리스트에서 해당 항목을 클릭했을 때 매물 아이디와 위도, 경도 정보를 넘겨 받는다
        Intent intent = getIntent();
        estate_id = intent.getStringExtra("estate_id");
        latitude = intent.getDoubleExtra("latitude", 37.3219085636);
        longtitude = intent.getDoubleExtra("longtitude", 126.8308434601);

        // 넘겨 받은 위도 경도 정보를 토대로 다음 지도를 띄우는 작업을 수
        MapView mapView = new MapView(EstateDetailActivity.this);
        mapView.setDaumMapApiKey(UtilLibs.DAUM_API_KEY);
        mapView.setMapViewEventListener(EstateDetailActivity.this);
        mapView.setPOIItemEventListener(EstateDetailActivity.this);

        RelativeLayout mapViewContainer = (RelativeLayout) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        // 하트를 클릭하면 해당 매물을 찜한다 (데이터베이스에 저장하는 로직은 추후 작성해야 함
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

//        mProgress = ProgressDialog.show(EstateDetailActivity.this, "Wait", "Downloading...");

        // 넘겨 받은 매물 아이디 정보를 토대로 매물 상세 정보를 서버에서 불러와 화면에 띄운다
        new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                NetworkService networkService = NetworkService.retrofit.create(NetworkService.class);
                Call<List<Estate>> estate = networkService.estateDetail(Integer.parseInt(estate_id));

                try {
                    estate_info = estate.execute().body().get(0);
                    return 1;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            protected void onPostExecute(Integer result) {
                if(result==1){
                    // 이미지를 슬라이더로 띄우는 로직
                    imageUrls = new ArrayList<String>();

                    String[] photos = estate_info.getPhoto().split(",");
                    for (int i = 0; i < photos.length; i++) {
                        imageUrls.add(photos[i]);
                    }

                    avf = (AdapterViewFlipper) findViewById(R.id.detailed_photo);
                    avf.setAdapter(new galleryAdapter(EstateDetailActivity.this));
                    avf.startFlipping();

                    // 이미지 슬라이더에서 이전/다음 버튼을 누르면 이미지를 넘길 수 있다.
                    ((Button) findViewById(R.id.showPrev)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            avf.showPrevious();
                        }
                    });

                    ((Button) findViewById(R.id.showNext)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            avf.showNext();
                        }
                    });

                    title_addr1 = (TextView) findViewById(R.id.title_addr1);
                    title_addr1.setText(estate_info.getAddr1());

                    user_name = (TextView) findViewById(R.id.user_name);
                    user_name.setText(estate_info.getName());

                    phone = (TextView) findViewById(R.id.phone);
                    phone.setText(estate_info.getMobile());

                    phone_call = (Button) findViewById(R.id.phone_call);
                    phone_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + phone.getText().toString()));
                            startActivity(intent);
                        }
                    });

                    TextView estate_id = (TextView) findViewById(R.id.estate_id);
                    estate_id.setText(
                        "id : " + estate_info.getId() + "\n"
                        + "type : " + estate_info.getType() + "\n"
                                + "deal_type : " + estate_info.getDeal_type() + "\n"
                                + "facility : " + estate_info.getFacility() + "\n"
                                + "extent : " + estate_info.getExtent() + "\n"
                                + "info : " + estate_info.getInfo() + "\n"
                                + "price_type : " + estate_info.getPrice_type() + "\n"
                                + "price : " + estate_info.getPrice() + "\n"
                                + "annual_price : " + estate_info.getAnnual_price() + "\n"
                                + "monthly_price : " + estate_info.getMonthly_price() + "\n"
                                + "category : " + estate_info.getCategory() + "\n"
                                + "usearea : " + estate_info.getUsearea() + "\n"
                                + "land_ratio : " + estate_info.getLand_ratio() + "\n"
                                + "area_ratio : " + estate_info.getArea_ratio() + "\n"
                                + "\n" + "name : " + estate_info.getName() + "\n"
                                + "mobile : " + estate_info.getMobile() + "\n"
                                + "user_type : " + estate_info.getUser_type() + "\n"

                    );

//@if($estate_detail->type=='2')
//                        "public_price" : "{{$estate_detail->public_price}}",
//                            "private_extent" : "{{$estate_detail->private_extent}}",
//                            "support_extent" : "{{$estate_detail->support_extent}}",
//                            "height" : "{{$estate_detail->height}}",
//                            "movein" : "{{$estate_detail->movein}}",
//                            "loan" : "{{$estate_detail->loan}}",
//                            "total_floor" : "{{$estate_detail->total_floor}}",
//                            "floor" : "{{$estate_detail->floor}}",
//                            "heater" : "{{$estate_detail->heater}}",
//                            "fuel" : "{{$estate_detail->fuel}}",
//                            "complete" : "{{$estate_detail->complete}}",
//                            "parking" : "{{$estate_detail->parking}}",
//                            "maintenance_price" : "{{$estate_detail->maintenance_price}}",
//@else
//                    "public_price" : "{{$estate_detail->public_price}}",
//                            "loan" : "{{$estate_detail->loan}}",
//                            "land_type" : "{{$estate_detail->type}}",
//                    @endif

                }
            }
        }.execute();
    }

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


