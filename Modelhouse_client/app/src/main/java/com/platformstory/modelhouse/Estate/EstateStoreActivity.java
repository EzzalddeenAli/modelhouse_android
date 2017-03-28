package com.platformstory.modelhouse.Estate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.platformstory.modelhouse.Common.Estate;
import com.platformstory.modelhouse.Common.Network;
import com.platformstory.modelhouse.Common.UtilLibs;
import com.platformstory.modelhouse.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class EstateStoreActivity extends Activity implements MapView.MapViewEventListener, MapView.POIItemEventListener{
    // 매물 위치 정보를 등록하기 위한 변수 정의
    TextView si;
    TextView gu;
    TextView dong;

    EditText addr_detail;

    String[] si_list;
    String[] gu_list;
    String[] dong_list;

    int[] gu_ids;
    int[] dong_ids;

    Button btn_find_latlng;
    MapView mapView;
    RelativeLayout mapViewContainer;

    // 매물 사진을 등록하기 위한 버튼과 이미지 뷰 정의
    Button btn_photo_1, btn_photo_2, btn_photo_3, btn_photo_4, btn_photo_5, btn_photo_6, btn_photo_7, btn_photo_8, btn_photo_9, btn_photo_10;
    Button btn_photo_11, btn_photo_12, btn_photo_13, btn_photo_14, btn_photo_15;
    ImageView img_photo_1, img_photo_2, img_photo_3, img_photo_4, img_photo_5, img_photo_6, img_photo_7, img_photo_8, img_photo_9, img_photo_10;
    ImageView img_photo_11, img_photo_12, img_photo_13, img_photo_14, img_photo_15;

    private int button_id;

    String[] absolutePath;

    // 매물 등록 항목에 대한 입력 양식 변수
    RadioGroup estate_type;

    LinearLayout lv_private_extent;
    LinearLayout lv_support_extent;
    LinearLayout lv_height;
    LinearLayout lv_movein;
    LinearLayout lv_floors;
    LinearLayout lv_heater;
    LinearLayout lv_fuel;
    LinearLayout lv_complete;
    LinearLayout lv_parking;

    RadioGroup rg_price_type;
    TextView price_tv;

    LinearLayout lv_monthly;
    RadioGroup monthly_or_annual;
    LinearLayout manage_price_lv;

    TextView category;
    TextView usearea;

    EditText land_ratio_min;
    EditText land_ratio_max;
    EditText area_ratio_min;
    EditText area_ratio_max;

    Button submit;

    // 인텐트 관련 상수들
    final static int SELECT_PICTURE = 2;


    // 서버 요청 시 입력 파라미터로 보낼 값에 대한 변수/ 초기값은 0 또는 null 이며,
    // 서버로 요청하기 전 필수 입력 항목 확인 시에 이 값이 설정되어 있지 않으면 입력 하도록 유도하는 메시지를 띄움
    int addr_si_id=0;
    int addr_gu_id=0;
    int addr_dong_id=0;

    String addr1;
    String addr2;



    double latitude=0;
    double longitude=0;

    int type=0;
    int price_type=0;
    String price;
    String monthly_param;
    int monthly_or_annual_param;
    String manage_price_param;

    String public_price;
    String category_param;
    String usearea_param;
    String land_ratio_param;
    String area_ratio_param;
    String extent;
    String private_extent;
    String support_extent;
    String height;
    String movein;
    String loan;
    String total_floor;
    String floor;
    String heater;
    String fuel;
    String complete;
    String parking;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estate_store);

        //시-구-동 선택을 위한 텍스트 뷰 선언과 리스너 등록
        si = (TextView)findViewById(R.id.si);
        gu = (TextView)findViewById(R.id.gu);
        dong = (TextView)findViewById(R.id.dong);

        addr_detail = (EditText) findViewById(R.id.addr_detail);

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Network.isNetworkAvailable(EstateStoreActivity.this)) {
                    Log.i("modelhouse", "현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요");
                    Toast.makeText(EstateStoreActivity.this, "현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요", Toast.LENGTH_LONG).show();
                } else {
                    si_list = new String[]{"서울", "인천", "경기"};
                    AlertDialog.Builder dlg = new AlertDialog.Builder(EstateStoreActivity.this);
                    dlg.setTitle("도/시 선택");
                    dlg.setItems(si_list, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            addr_si_id = i + 1;
                            gu_list = null;
                            gu_ids = null;
                            dong_list = null;
                            dong_ids = null;
                            si.setText(si_list[i]);
                            gu.setText("시/군/구 선택");
                            dong.setText("읍/면/동 선택");
                            AddressThread addrThread = new AddressThread(Network.URL + "address?table=addr_gues&value=" + addr_si_id, "addr_gues", "POST");
                            addrThread.start();
                        }
                    });
                    dlg.setPositiveButton("닫기", null);
                    dlg.show();
                }
            }
        });

        gu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Network.isNetworkAvailable(EstateStoreActivity.this)){
                    Log.i("modelhouse","현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요");
                    Toast.makeText(EstateStoreActivity.this, "현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요", Toast.LENGTH_LONG).show();
                }else {
                    if (gu_list == null) {
                        Toast.makeText(EstateStoreActivity.this, "시/도 를 먼저 선택하세요", Toast.LENGTH_LONG).show();
                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(EstateStoreActivity.this);
                        dlg.setTitle("시/군/구 선택");
                        dlg.setItems(gu_list, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addr_gu_id = gu_ids[i];
                                dong_list = null;
                                dong_ids = null;
                                gu.setText(gu_list[i]);
                                dong.setText("읍/면/동 선택");
                                AddressThread addrThread = new AddressThread(Network.URL + "address?table=addr_dongs&value=" + gu_ids[i], "addr_dongs", "POST");
                                addrThread.start();
                            }
                        });
                        dlg.setPositiveButton("닫기", null);
                        dlg.show();
                    }
                }
            }
        });

        dong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Network.isNetworkAvailable(EstateStoreActivity.this)){
                    Log.i("modelhouse","현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요");
                    Toast.makeText(EstateStoreActivity.this, "현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요", Toast.LENGTH_LONG).show();
                }else {
                    if (dong_list == null) {
                        Toast.makeText(EstateStoreActivity.this, "시/군/구 를 먼저 선택하세요", Toast.LENGTH_LONG).show();
                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(EstateStoreActivity.this);
                        dlg.setTitle("읍/면/동 선택");
                        dlg.setItems(dong_list, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addr_dong_id = dong_ids[i];
                                dong.setText(dong_list[i]);

                                addr1 = si.getText() + " " + gu.getText() + " " + dong.getText();
//                                Log.i(UtilLibs.LOG_TAG, "si : "+addr_si_id + ", gu : " + addr_gu_id + ", dong : " + addr_dong_id);
                            }
                        });
                        dlg.setPositiveButton("닫기", null);
                        dlg.show();
                    }
                }
            }
        });

        // 입력된 주소를 바탕으로 위치 검색을 한다. 위치 검색이 끝나고 나면 파라미터 값인 위도,경도값이 설정되며 해당 위치가 다음(Daum) 지도로 출력.
        btn_find_latlng = (Button)findViewById(R.id.btn_find_latlng);
        btn_find_latlng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addr_dong_id==0){
                    Toast.makeText(EstateStoreActivity.this, "주소를 먼저 선택하세요", Toast.LENGTH_LONG).show();
                }else {
                    Geocoder geocoder = new Geocoder(EstateStoreActivity.this, Locale.KOREA);

                    String location_dong = si.getText() + " " + gu.getText() + " " + dong.getText();
                    String location_jibun = location_dong + addr_detail.getText();

                    Log.i(UtilLibs.LOG_TAG, location_jibun);

                    try {
                        List<Address> addrs = geocoder.getFromLocationName(location_dong, 1);

                        latitude = addrs.get(0).getLatitude();
                        longitude = addrs.get(0).getLongitude();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (mapView != null) {
                        mapViewContainer.removeView(mapView);
                    }

                    mapView = new MapView(EstateStoreActivity.this);
                    mapView.setDaumMapApiKey(UtilLibs.DAUM_API_KEY);
                    mapView.setMapViewEventListener(EstateStoreActivity.this);
                    mapView.setPOIItemEventListener(EstateStoreActivity.this);

                    mapViewContainer = (RelativeLayout) findViewById(R.id.map_view);
                    mapViewContainer.setVisibility(View.VISIBLE);
                    mapViewContainer.addView(mapView);
                }
            }
        });

        btn_photo_1 = (Button)findViewById(R.id.btn_photo_1);
        btn_photo_2 = (Button)findViewById(R.id.btn_photo_2);
        btn_photo_3 = (Button)findViewById(R.id.btn_photo_3);
        btn_photo_4 = (Button)findViewById(R.id.btn_photo_4);
        btn_photo_5 = (Button)findViewById(R.id.btn_photo_5);
        btn_photo_6 = (Button)findViewById(R.id.btn_photo_6);
        btn_photo_7 = (Button)findViewById(R.id.btn_photo_7);
        btn_photo_8 = (Button)findViewById(R.id.btn_photo_8);
        btn_photo_9 = (Button)findViewById(R.id.btn_photo_9);
        btn_photo_10 = (Button)findViewById(R.id.btn_photo_10);
        btn_photo_11 = (Button)findViewById(R.id.btn_photo_11);
        btn_photo_12 = (Button)findViewById(R.id.btn_photo_12);
        btn_photo_13 = (Button)findViewById(R.id.btn_photo_13);
        btn_photo_14 = (Button)findViewById(R.id.btn_photo_14);
        btn_photo_15 = (Button)findViewById(R.id.btn_photo_15);

        btn_photo_1.setOnClickListener(select_photo);
        btn_photo_2.setOnClickListener(select_photo);
        btn_photo_3.setOnClickListener(select_photo);
        btn_photo_4.setOnClickListener(select_photo);
        btn_photo_5.setOnClickListener(select_photo);
        btn_photo_6.setOnClickListener(select_photo);
        btn_photo_7.setOnClickListener(select_photo);
        btn_photo_8.setOnClickListener(select_photo);
        btn_photo_9.setOnClickListener(select_photo);
        btn_photo_10.setOnClickListener(select_photo);
        btn_photo_11.setOnClickListener(select_photo);
        btn_photo_12.setOnClickListener(select_photo);
        btn_photo_13.setOnClickListener(select_photo);
        btn_photo_14.setOnClickListener(select_photo);
        btn_photo_15.setOnClickListener(select_photo);

        img_photo_1 = (ImageView)findViewById(R.id.img_photo_1);
        img_photo_2 = (ImageView)findViewById(R.id.img_photo_2);
        img_photo_3 = (ImageView)findViewById(R.id.img_photo_3);
        img_photo_4 = (ImageView)findViewById(R.id.img_photo_4);
        img_photo_5 = (ImageView)findViewById(R.id.img_photo_5);
        img_photo_6 = (ImageView)findViewById(R.id.img_photo_6);
        img_photo_7 = (ImageView)findViewById(R.id.img_photo_7);
        img_photo_8 = (ImageView)findViewById(R.id.img_photo_8);
        img_photo_9 = (ImageView)findViewById(R.id.img_photo_9);
        img_photo_10 = (ImageView)findViewById(R.id.img_photo_10);
        img_photo_11 = (ImageView)findViewById(R.id.img_photo_11);
        img_photo_12 = (ImageView)findViewById(R.id.img_photo_12);
        img_photo_13 = (ImageView)findViewById(R.id.img_photo_13);
        img_photo_14 = (ImageView)findViewById(R.id.img_photo_14);
        img_photo_15 = (ImageView)findViewById(R.id.img_photo_15);

        absolutePath = new String[15];



        // 각종 뷰 및 입력 양식 정의
        estate_type = (RadioGroup) findViewById(R.id.estate_type);



        rg_price_type = (RadioGroup)findViewById(R.id.price_type);
        price_tv = (TextView)findViewById(R.id.price_tv);
        lv_monthly = (LinearLayout)findViewById(R.id.lv_monthly);
        monthly_or_annual = (RadioGroup)findViewById(R.id.monthly_or_annual);
        manage_price_lv = (LinearLayout)findViewById(R.id.manage_price);

        lv_private_extent = (LinearLayout)findViewById(R.id.lv_private_extent);
        lv_support_extent = (LinearLayout)findViewById(R.id.lv_support_extent);
        lv_height = (LinearLayout)findViewById(R.id.lv_height);
        lv_movein = (LinearLayout)findViewById(R.id.lv_movein);
        lv_floors = (LinearLayout)findViewById(R.id.lv_floors);
        lv_heater = (LinearLayout)findViewById(R.id.lv_heater);
        lv_fuel = (LinearLayout)findViewById(R.id.lv_fuel);
        lv_complete = (LinearLayout)findViewById(R.id.lv_complete);
        lv_parking = (LinearLayout)findViewById(R.id.lv_parking);


        // 토지, 건물 라디오 버튼 선택했을 때 파라미터 값 설정 및 뷰 상태 변경
        estate_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.land:
                        type = 1;
                        manage_price_lv.setVisibility(View.GONE);

                        lv_private_extent.setVisibility(View.GONE);
                        lv_support_extent.setVisibility(View.GONE);
                        lv_height.setVisibility(View.GONE);
                        lv_movein.setVisibility(View.GONE);
                        lv_floors.setVisibility(View.GONE);
                        lv_heater.setVisibility(View.GONE);
                        lv_fuel.setVisibility(View.GONE);
                        lv_complete.setVisibility(View.GONE);
                        lv_parking.setVisibility(View.GONE);

                        break;
                    case R.id.building:
                        type = 2;
                        if(price_type==3){
                            manage_price_lv.setVisibility(View.VISIBLE);
                        }

                        lv_private_extent.setVisibility(View.VISIBLE);
                        lv_support_extent.setVisibility(View.VISIBLE);
                        lv_height.setVisibility(View.VISIBLE);
                        lv_movein.setVisibility(View.VISIBLE);
                        lv_floors.setVisibility(View.VISIBLE);
                        lv_heater.setVisibility(View.VISIBLE);
                        lv_fuel.setVisibility(View.VISIBLE);
                        lv_complete.setVisibility(View.VISIBLE);
                        lv_parking.setVisibility(View.VISIBLE);

                        break;
                }
            }
        });

        // 매매, 전세, 임대 라디오 버튼 선택했을 때 파라미터 값 설정 및 뷰 상태 변경
        rg_price_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.trade:
                        price_type = 1;
                        price_tv.setText("매매가");
                        lv_monthly.setVisibility(View.GONE);
                        monthly_or_annual.setVisibility(View.GONE);
                        manage_price_lv.setVisibility(View.GONE);
                        break;
                    case R.id.lent:
                        price_type = 2;
                        price_tv.setText("전세가");
                        lv_monthly.setVisibility(View.GONE);
                        monthly_or_annual.setVisibility(View.GONE);
                        manage_price_lv.setVisibility(View.GONE);
                        break;
                    case R.id.monthly:
                        price_type = 3;
                        price_tv.setText("보증금");
                        lv_monthly.setVisibility(View.VISIBLE);
                        monthly_or_annual.setVisibility(View.VISIBLE);
                        if(type==2){
                            manage_price_lv.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
        });

        monthly_or_annual.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.month:
                        monthly_or_annual_param = 1;
                        break;
                    case  R.id.annual:
                        monthly_or_annual_param = 2;
                        break;
                }
            }
        });

        category = (TextView)findViewById(R.id.category);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(EstateStoreActivity.this);

                final String categories[] = {"전","답","과수원","목장용지","임야","광천지","염전","대","공장용지","학교용지","주차장","주유소용지","창고용지","도로","철도용지",
                        "제방","하천","구거","유지","양어장","수도용지","공원","체육용지","유원지","종교용지","사적지","요지","잡종지"};

                dlg.setTitle("지목구분 선택");
                dlg.setItems(categories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        category.setText(categories[i]);
                        category_param = categories[i];
                    }
                });
                dlg.setPositiveButton("닫기", null);
                dlg.show();
            }
        });

        land_ratio_min = (EditText)findViewById(R.id.land_ratio_min);
        land_ratio_max = (EditText)findViewById(R.id.land_ratio_max);
        area_ratio_min = (EditText)findViewById(R.id.area_ratio_min);
        area_ratio_max = (EditText)findViewById(R.id.area_ratio_max);

        usearea = (TextView)findViewById(R.id.usearea);
        usearea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(EstateStoreActivity.this);

                final String useareas[] = {"제1종전용주거지역","제2종전용주거지역","제1종일반주거지역","제2종일반주거지역","제3종일반주거지역","준주거지역","중심상업지역",
                        "일반상업지역","근린상업지역","유통상업지역","전용공업지역","일반공업지역","준공업지역","보전녹지지역","생산녹지지역","자연녹지지역","보전관리지역",
                        "생산관리지역","계획관리지역","농림지역","자연환경보전지역"};

                dlg.setTitle("용도구분 선택");
                dlg.setItems(useareas, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        usearea.setText(useareas[i]);
                        usearea_param = useareas[i];

                        if(type==1) {
                            int[] land_area_ratios = null;
                            switch (i) {
                                case 0:
                                    land_area_ratios = new int[]{0, 50, 50, 100};
                                    break;
                                case 1:
                                    land_area_ratios = new int[]{0, 50, 100, 150};
                                    break;
                                case 2:
                                    land_area_ratios = new int[]{0, 60, 100, 200};
                                    break;
                                case 3:
                                    land_area_ratios = new int[]{0, 50, 150, 250};
                                    break;
                                case 4:
                                    land_area_ratios = new int[]{0, 50, 200, 300};
                                    break;
                                case 5:
                                    land_area_ratios = new int[]{0, 70, 200, 500};
                                    break;
                                case 6:
                                    land_area_ratios = new int[]{0, 90, 400, 1500};
                                    break;
                                case 7:
                                    land_area_ratios = new int[]{0, 80, 300, 1300};
                                    break;
                                case 8:
                                    land_area_ratios = new int[]{0, 70, 200, 900};
                                    break;
                                case 9:
                                    land_area_ratios = new int[]{0, 80, 200, 1100};
                                    break;
                                case 10:
                                    land_area_ratios = new int[]{0, 70, 150, 300};
                                    break;
                                case 11:
                                    land_area_ratios = new int[]{0, 70, 200, 350};
                                    break;
                                case 12:
                                    land_area_ratios = new int[]{0, 70, 200, 400};
                                    break;
                                case 16:
                                case 17:
                                case 18:
                                case 19:
                                case 20:
                                case 13:
                                    land_area_ratios = new int[]{0, 20, 50, 80};
                                    break;
                                case 14:
                                case 15:
                                    land_area_ratios = new int[]{0, 20, 50, 100};
                                    break;
                            }

                            land_ratio_min.setText(land_area_ratios[0] + "");
                            land_ratio_max.setText(land_area_ratios[1] + "");
                            area_ratio_min.setText(land_area_ratios[2] + "");
                            area_ratio_max.setText(land_area_ratios[3] + "");
                        }
                    }
                });
                dlg.setPositiveButton("닫기", null);
                dlg.show();
            }
        });



        // 모든 입력 양식을 채우고 매물 등록 버튼을 누를 때 서버로 입력 내용 및 이미지를 전송
        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addr2 = ((EditText)findViewById(R.id.addr_detail)).getText().toString();

                price = ((EditText)findViewById(R.id.price)).getText().toString();
                monthly_param = ((EditText)findViewById(R.id.monthly_param)).getText().toString();
                manage_price_param = ((EditText)findViewById(R.id.manage_price_param)).getText().toString();

                public_price = ((EditText)findViewById(R.id.public_price)).getText().toString();

                land_ratio_param = ((EditText)findViewById(R.id.land_ratio_min)).getText().toString() + "~" + ((EditText)findViewById(R.id.land_ratio_max)).getText().toString();
                area_ratio_param= ((EditText)findViewById(R.id.area_ratio_min)).getText().toString() + "~" + ((EditText)findViewById(R.id.area_ratio_max)).getText().toString();

                extent = ((EditText)findViewById(R.id.extent)).getText().toString();
                private_extent = ((EditText)findViewById(R.id.private_extent)).getText().toString();
                support_extent = ((EditText)findViewById(R.id.support_extent)).getText().toString();
                height = ((EditText)findViewById(R.id.height)).getText().toString();
                movein = ((EditText)findViewById(R.id.movein)).getText().toString();
                loan = ((EditText)findViewById(R.id.loan)).getText().toString();
                total_floor = ((EditText)findViewById(R.id.total_floor)).getText().toString();
                floor = ((EditText)findViewById(R.id.floor)).getText().toString();
                heater = ((EditText)findViewById(R.id.heater)).getText().toString();
                fuel = ((EditText)findViewById(R.id.fuel)).getText().toString();
                complete = ((EditText)findViewById(R.id.complete)).getText().toString();
                parking = ((EditText)findViewById(R.id.parking)).getText().toString();

                Estate estate = new Estate(type+"", null, price_type+"", price, extent, category_param, usearea_param,
                        null, addr1, null, latitude, longitude, addr2, monthly_param, monthly_or_annual_param+"",
                        public_price, land_ratio_param, area_ratio_param, private_extent, support_extent, height, movein,
                        loan, total_floor, floor, heater, fuel, complete, parking, addr_si_id, addr_gu_id, addr_dong_id, manage_price_param);

//                Log.i(UtilLibs.LOG_TAG, "주소 : " + addr1 + " " + addr2);
//                Log.i(UtilLibs.LOG_TAG, "좌표 : " + latitude + " " + longitude);
//                Log.i(UtilLibs.LOG_TAG, "매물 종류(토지/건물) : " + type);
//                Log.i(UtilLibs.LOG_TAG, "판매 유형(매매/전세/임대) : " + price_type);
//                Log.i(UtilLibs.LOG_TAG, "거래가격 : " + price);
//                Log.i(UtilLibs.LOG_TAG, "임대료(임대를 선택한 경우 : " + monthly_param + ", 년/월 : " + monthly_or_annual_param);
//                Log.i(UtilLibs.LOG_TAG, "개별공시지가 : " + public_price);
//                Log.i(UtilLibs.LOG_TAG, "지목 : " + category_param + ", 용도 : " + usearea_param);
//                Log.i(UtilLibs.LOG_TAG, "건폐율 : " + land_ratio_param + ", 용적률 : " + area_ratio_param);
//
//                Log.i(UtilLibs.LOG_TAG, "대지면적 : " + extent + ", 전용면적 : " + private_extent + ", 공급면적 : " + support_extent + ", 층고 : " + height);
//                Log.i(UtilLibs.LOG_TAG, "입주일 : " + movein);
//                Log.i(UtilLibs.LOG_TAG, "융자금 : " + loan);
//                Log.i(UtilLibs.LOG_TAG, "전체층 : " + total_floor + ", 해당층 : " + floor);
//                Log.i(UtilLibs.LOG_TAG, "난방방식 : " + heater + ", 난방연료 : " + fuel);
//                Log.i(UtilLibs.LOG_TAG, "준공년월 : " + complete);
//                Log.i(UtilLibs.LOG_TAG, "주차대수 : " + parking);


                EstateUploadTask estateUploadTask = new EstateUploadTask(Network.URL + "estates", estate, absolutePath);
                estateUploadTask.execute();
            }
        });
    }

    View.OnClickListener select_photo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            button_id = v.getId();

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImageUri = data.getData();
//                selectedImagePath = getPath(selectedImageUri);

                //절대경로를 획득한다!!! 중요~
                Cursor c = getContentResolver().query(Uri.parse(selectedImageUri.toString()), null,null,null,null);
                c.moveToNext();


                switch (button_id){
                    case R.id.btn_photo_1:
                        Glide.with(this).load(selectedImageUri).into(img_photo_1);
                        absolutePath[0] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                    case R.id.btn_photo_2:
                        Glide.with(this).load(selectedImageUri).into(img_photo_2);
                        absolutePath[1] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                    case R.id.btn_photo_3:
                        Glide.with(this).load(selectedImageUri).into(img_photo_3);
                        absolutePath[2] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                    case R.id.btn_photo_4:
                        Glide.with(this).load(selectedImageUri).into(img_photo_4);
                        absolutePath[3] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                    case R.id.btn_photo_5:
                        Glide.with(this).load(selectedImageUri).into(img_photo_5);
                        absolutePath[4] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                    case R.id.btn_photo_6:
                        Glide.with(this).load(selectedImageUri).into(img_photo_6);
                        absolutePath[5] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                    case R.id.btn_photo_7:
                        Glide.with(this).load(selectedImageUri).into(img_photo_7);
                        absolutePath[6] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                    case R.id.btn_photo_8:
                        Glide.with(this).load(selectedImageUri).into(img_photo_8);
                        absolutePath[7] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                    case R.id.btn_photo_9:
                        Glide.with(this).load(selectedImageUri).into(img_photo_9);
                        absolutePath[8] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                    case R.id.btn_photo_10:
                        Glide.with(this).load(selectedImageUri).into(img_photo_10);
                        absolutePath[9] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                    case R.id.btn_photo_11:
                        Glide.with(this).load(selectedImageUri).into(img_photo_11);
                        absolutePath[10] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                    case R.id.btn_photo_12:
                        Glide.with(this).load(selectedImageUri).into(img_photo_12);
                        absolutePath[11] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                    case R.id.btn_photo_13:
                        Glide.with(this).load(selectedImageUri).into(img_photo_13);
                        absolutePath[12] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                    case R.id.btn_photo_14:
                        Glide.with(this).load(selectedImageUri).into(img_photo_14);
                        absolutePath[13] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                    case R.id.btn_photo_15:
                        Glide.with(this).load(selectedImageUri).into(img_photo_15);
                        absolutePath[14] = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        break;
                }
            }
        }
    }

    /**
     * 사진의 URI 경로를 받는 메소드
     */
    public String getPath(Uri uri) {
        // uri가 null일경우 null반환
        if( uri == null ) {
            return null;
        }
        // 미디어스토어에서 유저가 선택한 사진의 URI를 받아온다.
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // URI경로를 반환한다.
        return uri.getPath();
    }




    class EstateUploadTask extends AsyncTask<Integer, Integer, Integer>{
        String url;
        String[] path;
        Estate estate;

        EstateUploadTask(String url, Estate estate, String[] path){
            this.url = url;
            this.path = path;
            this.estate = estate;
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            // Create MultipartEntityBuilder
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            // Set String Params
            builder.addTextBody("type", estate.type, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("price_type", estate.price_type, ContentType.create("Multipart/related", "UTF-8"));

            builder.addTextBody("price", estate.price, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("extent", estate.extent, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("category", estate.category, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("usearea", estate.usearea, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("addr1", estate.addr1, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("addr2", estate.addr2, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("latitude", estate.latitude+"", ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("longitude", estate.longtitude+"", ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("monthly", estate.monthly, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("monthly_or_annual", estate.monthly_or_annual, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("public_price", estate.public_price, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("land_ratio", estate.land_ratio, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("area_ratio", estate.area_ratio, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("private_extent", estate.private_extent, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("support_extent", estate.support_extent, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("height", estate.height, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("movein", estate.movein, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("loan", estate.loan, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("total_floor", estate.total_floor, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("floor", estate.floor, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("heater", estate.heater, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("fuel", estate.fuel, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("complete", estate.complete, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("parking", estate.parking, ContentType.create("Multipart/related", "UTF-8"));

            builder.addTextBody("addr_si_id", estate.addr_si_id+"", ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("addr_gu_id", estate.addr_gu_id+"", ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("addr_dong_id", estate.addr_dong_id+"", ContentType.create("Multipart/related", "UTF-8"));

            builder.addTextBody("manage_price", estate.manage_price, ContentType.create("Multipart/related", "UTF-8"));

            Log.i(UtilLibs.LOG_TAG, "사진 리스트 개수 : "+path.length+"개");

            // Set File Params
            for(int i=0; i<path.length; i++){
                if(path[i]!=null) {
                    Log.i(UtilLibs.LOG_TAG, path[i]);
                    builder.addPart("uploaded_file_" + i, new FileBody(new File(path[i])));
                }
            }

            // File 이 여러개 인 경우 아래와 같이 adpart 를 하나 더 추가해 주면 된다.
//                builder.addPart("Key 값", new FileBody(new File("File 경로")));

            // Send Request
            try {
                InputStream inputStream = null;
                HttpClient httpClient = AndroidHttpClient.newInstance("Android");
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(builder.build());
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();

                // Response
                BufferedReader bufferdReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;

                while ((line = bufferdReader.readLine()) != null) {
                    Log.i(UtilLibs.LOG_TAG, "*"+line+"*");

                    if(line.equals("1")){
                        inputStream.close();
                        return 1;
                    }

                    stringBuilder.append(line + "\n");
                }
                inputStream.close();


            }catch(IOException e){
                e.printStackTrace();
            }
//            Log.i(UtilLibs.LOG_TAG, selectedImagePath + ",\n" + absolutePath);
//            Log.i(UtilLibs.LOG_TAG, urlString);
            return 0;
        }

        protected void onPostExecute(Integer result){
            if(result==1){
                Toast.makeText(EstateStoreActivity.this, "매물이 등록되었습니다. 등록한 매물은\n'[나의 관심] > [내가 등록한 매물]'에서 확인 가능합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
//    여러개 사진 선택가능하게할때
//    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); 이런식으로 intent에 MULTIPLE을 허용해준다.
//
//    그리고 Result부분에 이 소스를 추가해준다.
//            if (Intent.ACTION_SEND_MULTIPLE.equals(data.getAction()))
//            && Intent.hasExtra(Intent.EXTRA_STREAM)) {
//        // retrieve a collection of selected images
//        ArrayList<Parcelable> list = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
//        // iterate over these images
//        if( list != null ) {
//            for (Parcelable parcel : list) {
//                Uri uri = (Uri) parcel;
//                // TODO handle the images one by one here
//            }
//        }
//    }

    // 매물의 주소(시,구,동)를 선택을 위한 네트워크 접속을 위한 스레드
    class AddressThread extends Thread {
        String mAddr;
        String table;
        String method;

        AddressThread(String mAddr, String table, String method) {
            this.table = table;
            this.mAddr = mAddr;
            this.method = method;
        }

        public void run() {
            switch(table){
                case "addr_gues" :
                    String gues = Network.DownloadHtml(mAddr, method);

                    try{
                        JSONArray ja = new JSONArray(gues);
                        gu_ids = new int[ja.length()-1];
                        gu_list = new String[ja.length()-1];
                        for(int i=0; i<ja.length()-1; i++){
                            JSONObject guObj = ja.getJSONObject(i);
                            gu_ids[i]=guObj.getInt("id");
                            gu_list[i]=guObj.getString("name");
                        }
                    }catch(JSONException e){                    }
                    break;
                case "addr_dongs" :
                    String dongs = Network.DownloadHtml(mAddr, method);

                    try{
                        JSONArray ja = new JSONArray(dongs);
                        dong_ids = new int[ja.length()-1];
                        dong_list = new String[ja.length()-1];
                        for(int i=0; i<ja.length()-1; i++){
                            JSONObject guObj = ja.getJSONObject(i);
                            dong_ids[i]=guObj.getInt("id");
                            dong_list[i]=guObj.getString("name");
                        }
                    }catch(JSONException e){                    }
                    break;
            }
        }
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);
        mapView.selectPOIItem(marker, true);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), false);
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
