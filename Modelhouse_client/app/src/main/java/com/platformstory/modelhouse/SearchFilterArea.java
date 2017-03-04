package com.platformstory.modelhouse;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.maps.android.clustering.ClusterManager;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchFilterArea extends Activity {
    TextView si;
    TextView gu;
    TextView dong;

    RadioGroup estate_type_rg;
    RadioGroup deal_type_rg;
    RadioGroup price_type_rg;

    RangeSeekBar price_range_rs;
    RangeSeekBar monthly_range_rs;
    RangeSeekBar extent_range_rs;

    RadioGroup monthly_annual_rg;

    String[] si_list;
    String[] gu_list;
    String[] dong_list;

    int[] gu_ids;
    int[] dong_ids;

    int addr_si_id=0;
    int addr_gu_id=0;
    int addr_dong_id=0;

    int estate_type=0;
    int deal_type=0;
    int price_type=0;

    int price_from=0;
    int price_to=10000;
    int monthly_from=0;
    int monthly_to=10000;
    int extent_from=0;
    int extent_to=10000;

    int monthly_annual=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_filter_area);

        //시-구-동 선택을 위한 텍스트 뷰 선언과 리스너 등록
        si = (TextView)findViewById(R.id.si);
        gu = (TextView)findViewById(R.id.gu);
        dong = (TextView)findViewById(R.id.dong);

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                si_list = new String[]{"서울", "인천", "경기"};
                AlertDialog.Builder dlg = new AlertDialog.Builder(SearchFilterArea.this);
                dlg.setTitle("도/시 선택");
                dlg.setItems(si_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addr_si_id = i+1;
                        gu_list=null;
                        gu_ids=null;
                        dong_list=null;
                        dong_ids=null;
                        si.setText(si_list[i]);
                        gu.setText("시/군/구 선택");
                        dong.setText("읍/면/동 선택");
                        AddressThread addrThread = new AddressThread("http://52.79.106.71/address?table=addr_gues&value="+addr_si_id, "addr_gues");
                        addrThread.start();
                    }
                });
                dlg.setPositiveButton("닫기", null);
                dlg.show();
            }
        });

        gu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gu_list==null){
                    Log.i("modelhouse", "시/도 를 먼저 선택하세요");
                }else{
                    AlertDialog.Builder dlg = new AlertDialog.Builder(SearchFilterArea.this);
                    dlg.setTitle("시/군/구 선택");
                    dlg.setItems(gu_list, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            addr_gu_id = gu_ids[i];
                            dong_list=null;
                            dong_ids=null;
                            gu.setText(gu_list[i]);
                            dong.setText("읍/면/동 선택");
                            AddressThread addrThread = new AddressThread("http://52.79.106.71/address?table=addr_dongs&value="+gu_ids[i], "addr_dongs");
                            addrThread.start();
                        }
                    });
                    dlg.setPositiveButton("닫기", null);
                    dlg.show();
                }
            }
        });

        dong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dong_list==null){
                    Log.i("modelhouse", "시/군/구 를 먼저 선택하세요");
                }else{
                    AlertDialog.Builder dlg = new AlertDialog.Builder(SearchFilterArea.this);
                    dlg.setTitle("읍/면/동 선택");
                    dlg.setItems(dong_list, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            addr_dong_id = dong_ids[i];
                            dong.setText(dong_list[i]);
                        }
                    });
                    dlg.setPositiveButton("닫기", null);
                    dlg.show();
                }
            }
        });

        // 토지-건물 라디오를 선택하였을 때
        estate_type_rg = (RadioGroup)findViewById(R.id.estate_type);
        estate_type_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.land:
                        estate_type = 1;
                        break;
                    case R.id.building:
                        estate_type = 2;
                        break;
                }
            }
        });

        // 중개사무소-직거래 라디오를 선택하였을 때
        deal_type_rg = (RadioGroup)findViewById(R.id.deal_type);
        deal_type_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.office:
                        deal_type = 1;
                        break;
                    case R.id.direct:
                        deal_type = 2;
                        break;
                }
            }
        });

        // 매매-전세-임대 라디오를 선택하였을 때
        price_type_rg = (RadioGroup)findViewById(R.id.price_type);
        price_type_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.trade:
                        price_type = 1;
                        ((TextView)findViewById(R.id.price_tv)).setText("매매가");
                        ((LinearLayout)findViewById(R.id.lent_price)).setVisibility(View.GONE);
                        break;
                    case R.id.lent:
                        price_type = 2;
                        ((TextView)findViewById(R.id.price_tv)).setText("전세금");
                        ((LinearLayout)findViewById(R.id.lent_price)).setVisibility(View.GONE);
                        break;
                    case R.id.monthly :
                        price_type = 3;
                        ((TextView)findViewById(R.id.price_tv)).setText("보증금");
                        ((LinearLayout)findViewById(R.id.lent_price)).setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        // 가격 설정
        price_range_rs = (RangeSeekBar) findViewById(R.id.price_range);
        price_range_rs.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener(){
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                price_from = (int)minValue;
                price_to = (int)maxValue;
                ((TextView)findViewById(R.id.price_range_tv)).setText(minValue + "~" + maxValue + "원");
            }
        });

        // 임대료 설정
        monthly_range_rs = (RangeSeekBar) findViewById(R.id.monthly_range);
        monthly_range_rs.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener(){
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                monthly_from = (int)minValue;
                monthly_to = (int)maxValue;
                ((TextView)findViewById(R.id.monthly_range_tv)).setText(minValue + "~" + maxValue + "원");
            }
        });

        // 면적 설정
        extent_range_rs = (RangeSeekBar) findViewById(R.id.extent_range);
        extent_range_rs.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener(){
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                extent_from = (int)minValue;
                extent_to = (int)maxValue;
                ((TextView)findViewById(R.id.extent_range_tv)).setText(minValue + "~" + maxValue + "㎡");
            }
        });

        // 월세-년세 설정
        monthly_annual_rg = (RadioGroup)findViewById(R.id.monthly_or_annual);
        monthly_annual_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.month:
                        monthly_annual = 1;
                        break;
                    case R.id.annual:
                        monthly_annual = 2;
                        break;
                }
            }
        });

        // 초기화 버튼을 누르면 모든 선택된 것들을 초기화시키고 필터 변수들 또한 초기화 시킴
        ((Button)findViewById(R.id.reset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                si.setText("도/시 선택");
                gu.setText("시/군/구 선택");
                dong.setText("읍/면/동 선택");

                estate_type_rg.clearCheck();
                deal_type_rg.clearCheck();
                price_type_rg.clearCheck();
                monthly_annual_rg.clearCheck();

                price_range_rs.setSelectedMinValue(0);
                price_range_rs.setSelectedMaxValue(10000);
                ((TextView)findViewById(R.id.price_range_tv)).setText("0~제한없음");

                monthly_range_rs.setSelectedMinValue(0);
                monthly_range_rs.setSelectedMaxValue(10000);
                ((TextView)findViewById(R.id.extent_range_tv)).setText("0~제한없음");

                extent_range_rs.setSelectedMinValue(0);
                extent_range_rs.setSelectedMaxValue(10000);
                ((TextView)findViewById(R.id.monthly_range_tv)).setText("0~제한없음");

                gu_list=null;
                dong_list=null;

                gu_ids=null;
                dong_ids=null;

                addr_si_id=0;
                addr_gu_id=0;
                addr_dong_id=0;

                estate_type=0;
                deal_type=0;
                price_type=0;

                price_from=0;
                price_to=10000;
                monthly_from=0;
                monthly_to=10000;
                extent_from=0;
                extent_to=10000;

                monthly_annual=0;
            }
        });

        // 확인 버튼을 누르면 필터 변수들을 서버로 보내어 검색 결과를 메인 화면으로 넘김
        ((Button)findViewById(R.id.submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("modelhouse", "http://52.79.106.71/search?addr_si_id="+addr_si_id+"&addr_gu_id="+addr_gu_id+"&addr_dong_id="+addr_dong_id
                                        +"&estate_type="+estate_type+"&deal_type="+deal_type+"&price_type="+price_type
                                        +"&price_from="+price_from+"&price_to="+price_to+"&monthly_from="+monthly_from+"&monthly_to="+monthly_to
                                        +"&extent_from="+extent_from+"&extent_to="+extent_to+"&monthly_annual="+monthly_annual);

                SearchThread searchThread
                        = new SearchThread("http://52.79.106.71/search?addr_si_id="+addr_si_id+"&addr_gu_id="+addr_gu_id+"&addr_dong_id="+addr_dong_id
                                            +"&estate_type="+estate_type+"&deal_type="+deal_type+"&price_type="+price_type
                                            +"&price_from="+price_from+"&price_to="+price_to+"&monthly_from="+monthly_from+"&monthly_to="+monthly_to
                                            +"&extent_from="+extent_from+"&extent_to="+extent_to+"&monthly_annual="+monthly_annual);
                searchThread.start();
            }
        });
    }

    class SearchThread extends Thread{
        String mAddr;

        SearchThread(String mAddr){
            this.mAddr = mAddr;
        }

        public void run(){
            String[] searchResult = Network.DownloadHtml(mAddr).split("`");

            String latitude = searchResult[0];
            String longitude = searchResult[1];
            String searchJSON = searchResult[2];

            Intent intent = new Intent();
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.putExtra("searchJSON", searchJSON);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    // 매물의 주소(시,구,동)를 선택을 위한 네트워크 접속을 위한 스레드
    class AddressThread extends Thread {
        String mAddr;
        String table;

        AddressThread(String mAddr, String table) {
            this.table = table;
            this.mAddr = mAddr;
        }

        public void run() {
            switch(table){
                case "addr_gues" :
                    String gues = Network.DownloadHtml(mAddr);

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
                    String dongs = Network.DownloadHtml(mAddr);

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
}




//
//        // Setup the new range seek bar
//        RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<>(this);
//        // Set the range
//        rangeSeekBar.setRangeValues(15, 90);
//        rangeSeekBar.setSelectedMinValue(20);
//        rangeSeekBar.setSelectedMaxValue(88);
//        rangeSeekBar.setTextAboveThumbsColorResource(android.R.color.black);
//
//        // Add to layout
//        FrameLayout layout = (FrameLayout) findViewById(R.id.seekbar_placeholder);
//        layout.addView(rangeSeekBar);
//
//        // Seek bar for which we will set text color in code