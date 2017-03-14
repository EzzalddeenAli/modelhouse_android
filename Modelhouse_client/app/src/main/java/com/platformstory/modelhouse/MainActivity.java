package com.platformstory.modelhouse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.platformstory.modelhouse.Common.Network;
import com.platformstory.modelhouse.Estate.EstateStoreActivity;
import com.platformstory.modelhouse.Search.EstateSearchListActivity;
import com.platformstory.modelhouse.Search.SearchFilterArea;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends FragmentActivity {
    public static final String PREFS_NAME = "ModelhouseLoc";

    GoogleMap gMap;
    double latitude;
    double longitude;
    int zoom;

    ClusterManager<MyItem> mClusterManager;

    String JSON;
    JSONArray ja;

    int estate_type;
    int deal_type;
    int price_type;

    int price_from;
    int price_to;
    int monthly_from;
    int monthly_to;
    int extent_from;
    int extent_to;

    int monthly_annual;

    ProgressDialog mProgress;

    TextView result_count;
    Button view_list;

    FrameLayout frameLayout;
    Boolean isPageOpen = false;
    TextView menu_btn;
    Animation translateLeftAnim;
    Animation translateRightAnim;
    ScrollView slidingPage;

    TextView estate_store;

    EstateSearchMapThread thread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view_list = (Button)findViewById(R.id.button);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        //앱이 종료되었을 때 지도의 좌표, 줌 값을 가져와 그대로 반영, 설정값이 없을 경우 서울을 중심으로 보여줌
        latitude = Double.parseDouble(settings.getString("latitude", "37.5668260055"));
        longitude = Double.parseDouble(settings.getString("longitude", "126.9786567859"));
        zoom = settings.getInt("zoom", 14);

        //앱이 종료되었을 때 검색 필터의 값을 가져온다. 검색 필터의 값이 설정되어 있는 경우는 설정된 필터대로 지도 및 리스트 검색 수행
        estate_type = settings.getInt("estate_type", 0);
        deal_type = settings.getInt("deal_type", 0);
        price_type = settings.getInt("price_type", 0);

        price_from = settings.getInt("price_from", 0);
        price_to = settings.getInt("price_to", 10000);
        monthly_from = settings.getInt("monthly_from", 0);
        monthly_to = settings.getInt("monthly_to", 10000);
        extent_from = settings.getInt("extent_from", 0);
        extent_to = settings.getInt("extent_to", 10000);

        monthly_annual = settings.getInt("monthly_annual", 0);

        // 지도 객체를 생성하고 지도를 표시한 후 좌표, 줌 값을 토대로 중심을 이동
        gMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));

        //매물 검색 필터 버튼 선언 및 클릭 리스너 등록
        Button search_filter = (Button)findViewById(R.id.search_filter);
        search_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchFilterArea.class);

                intent.putExtra("estate_type", estate_type);
                intent.putExtra("deal_type", deal_type);
                intent.putExtra("price_type", price_type);

                intent.putExtra("price_from", price_from);
                intent.putExtra("price_to", price_to);
                intent.putExtra("monthly_from", monthly_from);
                intent.putExtra("monthly_to", monthly_to);
                intent.putExtra("extent_from", extent_from);
                intent.putExtra("extent_to", extent_to);

                intent.putExtra("monthly_annual", monthly_annual);

                startActivityForResult(intent, 0);
            }
        });

        if(!Network.isNetworkAvailable(MainActivity.this)){
            Log.i("modelhouse","현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요");
            Toast.makeText(MainActivity.this, "현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요", Toast.LENGTH_LONG).show();
        }else {
            mProgress = ProgressDialog.show(MainActivity.this, "Wait", "Downloading...");

            if (!(estate_type == 0 && deal_type == 0 && price_type == 0 && price_from == 0 && price_to == 10000 && monthly_from == 0 && monthly_to == 10000 && extent_from == 0 && extent_to == 10000 && monthly_annual == 0)) {
                Toast.makeText(MainActivity.this, "검색 필터가 적용됩니다.", Toast.LENGTH_LONG).show();
                Log.i("modelhouse", "[초기 검색[검색 필터 적용]] http://52.79.106.71/api/search?latitude=" + latitude + "&longitude=" + longitude
                        + "&estate_type=" + estate_type + "&deal_type=" + deal_type + "&price_type=" + price_type
                        + "&price_from=" + price_from + "&price_to=" + price_to + "&monthly_from=" + monthly_from + "&monthly_to=" + monthly_to
                        + "&extent_from=" + extent_from + "&extent_to=" + extent_to + "&monthly_annual=" + monthly_annual);

                thread = new EstateSearchMapThread("http://52.79.106.71/api/search?latitude=" + latitude + "&longitude=" + longitude
                        + "&estate_type=" + estate_type + "&deal_type=" + deal_type + "&price_type=" + price_type
                        + "&price_from=" + price_from + "&price_to=" + price_to + "&monthly_from=" + monthly_from + "&monthly_to=" + monthly_to
                        + "&extent_from=" + extent_from + "&extent_to=" + extent_to + "&monthly_annual=" + monthly_annual, "POST");
                thread.start();
            } else {
                // 지도의 좌표, 줌 값을 토대로 지도 중심 반경 내의 매물을 검색하여 클러스터로 띄움
                Log.i("modelhouse", "[초기 검색[검색 필터 미적용]] http://52.79.106.71/api/search?latitude=" + latitude + "&longtitude=" + longitude + "&zoom=" + zoom);
                thread = new EstateSearchMapThread("http://52.79.106.71/api/search?latitude=" + latitude + "&longtitude=" + longitude + "&zoom=" + zoom, "GET");
                thread.start();
            }
        }

        // 슬라이딩 페이지
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        slidingPage = (ScrollView) findViewById(R.id.slidingPage);
        frameLayout.bringChildToFront(slidingPage);

        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_right);

        Animation.AnimationListener animListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(isPageOpen){
                    slidingPage.setVisibility(View.INVISIBLE);
                    isPageOpen=false;
                }else{
                    isPageOpen=true;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        translateRightAnim.setAnimationListener(animListener);
        translateLeftAnim.setAnimationListener(animListener);

        menu_btn = (TextView)findViewById(R.id.menu_btn);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPageOpen){
                    slidingPage.startAnimation(translateRightAnim);
                    Log.i("modelhouse", "버튼 클릭 2");
                }else{
                    slidingPage.setVisibility(View.VISIBLE);
                    slidingPage.startAnimation(translateLeftAnim);
                    Log.i("modelhouse", "버튼 클릭 1");
                }
            }
        });

        // 슬라이딩 페이지 메뉴 버튼 리스너 등록
        estate_store = (TextView)findViewById(R.id.estate_store);
        estate_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EstateStoreActivity.class);
                startActivity(intent);
            }
        });

        // 슬라이딩 페이지(끝)
    }

    //인텐트로 결과를 받아오면 위도, 경도를 토대로 지도를 이동하고 클러스터 리스너 등록하는 로직을 여기에 작성
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case 0 :
                if(resultCode == RESULT_OK){
                    try{
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(data.getStringExtra("latitude")), Double.parseDouble(data.getStringExtra("longitude"))), zoom));

                        JSON = data.getStringExtra("searchJSON");

                        ja = new JSONArray(JSON);

                        estate_type = data.getIntExtra("estate_type", 0);
                        deal_type = data.getIntExtra("deal_type", 0);
                        price_type = data.getIntExtra("price_type", 0);

                        price_from = data.getIntExtra("price_from", 0);
                        price_to = data.getIntExtra("price_to", 10000);
                        monthly_from = data.getIntExtra("monthly_from", 0);
                        monthly_to = data.getIntExtra("monthly_to", 10000);
                        extent_from = data.getIntExtra("extent_from", 0);
                        extent_to = data.getIntExtra("extent_to", 10000);

                        monthly_annual = data.getIntExtra("monthly_annual", 0);

                        if(!(estate_type==0 && deal_type==0 && price_type==0 && price_from==0 && price_to==10000 && monthly_from==0 && monthly_to==10000 && extent_from==0 && extent_to==10000 && monthly_annual==0)){
                            Toast.makeText(MainActivity.this, "검색 필터가 적용됩니다.", Toast.LENGTH_LONG).show();
                        }

                        //위도, 경도 값들을 토대로 지도에 클러스트를 만들고, 리스트 버튼 클릭 시 제이슨 배열을 인텐트로 넘겨주는 로직을 이곳에 작성
                        mClusterManager = new ClusterManager<MyItem>(MainActivity.this, gMap){
                            // 지도의 중심이 바뀌면 바뀐 지도의 좌표, 줌 값을 토대로 지도 중심 반경 내의 매물을 검색하여 클러스터로 띄움
                            @Override
                            public void onCameraChange(CameraPosition cameraPosition) {
                                gMap.clear();

                                super.onCameraChange(cameraPosition);

                                latitude = cameraPosition.target.latitude;
                                longitude = cameraPosition.target.longitude;

                                Log.i("modelhouse", "[검색 필터 적용 후 지도 이동] http://52.79.106.71/api/search?latitude="+latitude+"&longitude="+longitude
                                        +"&estate_type="+estate_type+"&deal_type="+deal_type+"&price_type="+price_type
                                        +"&price_from="+price_from+"&price_to="+price_to+"&monthly_from="+monthly_from+"&monthly_to="+monthly_to
                                        +"&extent_from="+extent_from+"&extent_to="+extent_to+"&monthly_annual="+monthly_annual);

                                thread = new EstateSearchMapThread("http://52.79.106.71/api/search?latitude="+latitude+"&longitude="+longitude
                                        +"&estate_type="+estate_type+"&deal_type="+deal_type+"&price_type="+price_type
                                        +"&price_from="+price_from+"&price_to="+price_to+"&monthly_from="+monthly_from+"&monthly_to="+monthly_to
                                        +"&extent_from="+extent_from+"&extent_to="+extent_to+"&monthly_annual="+monthly_annual, "POST");
                                thread.start();
                            }
                        };

                        gMap.setOnCameraChangeListener(mClusterManager);
                        gMap.setOnMarkerClickListener(mClusterManager);

                        addItems(ja);


                    }catch (JSONException e){

                    }

                    // 텍스트뷰에 검색 결과의 개수를 표시한다.
//                    result_count = (TextView)findViewById(R.id.result_count);
                    result_count.setText(ja.length() + "개");

                    // 이 버튼을 클릭하면 해당 지역(지도 중심 반경)의 검색 결과 개수만큼 리스트로 보여주는 페이지로 이동
//                    Button button = (Button)findViewById(R.id.button);
                    view_list.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, EstateSearchListActivity.class);
                            intent.putExtra("JSON", JSON);
                            startActivity(intent);
                        }
                    });
                }
                break;
        }
    }

    class EstateSearchMapThread extends Thread {
        String mAddr;
        String method;

        EstateSearchMapThread(String mAddr, String method) {
            this.mAddr = mAddr;
            this.method = method;
        }

        public void run() {
            String Json = Network.DownloadHtml(mAddr, method);

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
                ja = new JSONArray(JSON);

                //위도, 경도 값들을 토대로 지도에 클러스트를 만들고, 리스트 버튼 클릭 시 제이슨 배열을 인텐트로 넘겨주는 로직을 이곳에 작성
                mClusterManager = new ClusterManager<MyItem>(MainActivity.this, gMap){
                    // 지도의 중심이 바뀌면 바뀐 지도의 좌표, 줌 값을 토대로 지도 중심 반경 내의 매물을 검색하여 클러스터로 띄움
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        gMap.clear();

                        super.onCameraChange(cameraPosition);

                        latitude = cameraPosition.target.latitude;
                        longitude = cameraPosition.target.longitude;

                        if(estate_type==0 && deal_type==0 && price_type==0 && price_from==0 && price_to==10000 && monthly_from==0 && monthly_to==10000 && extent_from==0 && extent_to==10000 && monthly_annual==0){
                            Log.i("modelhouse", "[초기 화면 지도 이동] http://52.79.106.71/api/search?latitude="+latitude+"&longtitude="+longitude+"&zoom="+zoom);

                            thread = new EstateSearchMapThread("http://52.79.106.71/api/search?latitude="+latitude+"&longtitude="+longitude+"&zoom="+zoom, "GET");
                            thread.start();
                        }else{
                            Log.i("modelhouse", "[초기 화면 지도 이동] http://52.79.106.71/api/search?latitude="+latitude+"&longitude="+longitude
                                    +"&estate_type="+estate_type+"&deal_type="+deal_type+"&price_type="+price_type
                                    +"&price_from="+price_from+"&price_to="+price_to+"&monthly_from="+monthly_from+"&monthly_to="+monthly_to
                                    +"&extent_from="+extent_from+"&extent_to="+extent_to+"&monthly_annual="+monthly_annual);

                            thread = new EstateSearchMapThread("http://52.79.106.71/api/search?latitude="+latitude+"&longitude="+longitude
                                    +"&estate_type="+estate_type+"&deal_type="+deal_type+"&price_type="+price_type
                                    +"&price_from="+price_from+"&price_to="+price_to+"&monthly_from="+monthly_from+"&monthly_to="+monthly_to
                                    +"&extent_from="+extent_from+"&extent_to="+extent_to+"&monthly_annual="+monthly_annual, "POST");
                            thread.start();
                        }
                    }
                };

//                DefaultClusterRenderer<MyItem> dClustererRenderer = new DefaultClusterRenderer<>(MainActivity.this, gMap, mClusterManager);
//                dClustererRenderer.

                mClusterManager.setRenderer(new CustomRenderer<MyItem>(MainActivity.this, gMap, mClusterManager));

                gMap.setOnCameraChangeListener(mClusterManager);
                gMap.setOnMarkerClickListener(mClusterManager);

                addItems(ja);
            }catch (JSONException e){

            }

            // 텍스트뷰에 검색 결과의 개수를 표시한다.
            result_count = (TextView)findViewById(R.id.result_count);
            result_count.setText(ja.length() + "개");

            // 이 버튼을 클릭하면 해당 지역(지도 중심 반경)의 검색 결과 개수만큼 리스트로 보여주는 페이지로 이동
            view_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, EstateSearchListActivity.class);
                    intent.putExtra("JSON", JSON);
                    startActivity(intent);
                }
            });
        }
    };

    // 클러스터를 띄우는데 사용되는 위도,경도 값을 저장하기 위한 객체 정의
    public class MyItem implements ClusterItem {
        private final LatLng mPosition;

        public MyItem(double lat, double lng) {
            mPosition = new LatLng(lat, lng);
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }
    }

    // 서버에서 받아온 제이슨 객체로부터 위도, 경도 값을 가져와서 클러스터를 만들기 위한 작업 실시
    private void addItems(JSONArray ja) {
        try {
            for (int i = 0; i < ja.length(); i++) {
                JSONObject estateObj = ja.getJSONObject(i);

                MyItem offsetItem = new MyItem(estateObj.getDouble("latitude"), estateObj.getDouble("longtitude"));
                //Log.i("modelhouse", estateObj.getDouble("latitude") + ", " + estateObj.getDouble("longtitude"));

                mClusterManager.addItem(offsetItem);
            }
        }catch (JSONException e){

        }
    }

    // 클러스터를 형성할 최소의 마커 개수를 1로 설정
    class CustomRenderer<T extends ClusterItem> extends DefaultClusterRenderer<T>
    {
        public CustomRenderer(Context context, GoogleMap map, ClusterManager<T> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<T> cluster) {
            //start clustering if at least 1 items overlap
            return cluster.getSize() >= 1;
        }
    }

    //앱이 종료되면 현재 지도의 좌표, 줌, 검색 필터 값을 저장
    @Override
    public void onStop(){
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("latitude", latitude+"");
        editor.putString("longitude", longitude+"");
        editor.putInt("zoom", zoom);

        editor.putInt("estate_type", estate_type);
        editor.putInt("deal_type", deal_type);
        editor.putInt("price_type", price_type);

        editor.putInt("price_from", price_from);
        editor.putInt("price_to", price_to);
        editor.putInt("monthly_from", monthly_from);
        editor.putInt("monthly_to", monthly_to);
        editor.putInt("extent_from", extent_from);
        editor.putInt("extent_to", extent_to);

        editor.putInt("monthly_annual", monthly_annual);


        editor.commit();
    }
}



