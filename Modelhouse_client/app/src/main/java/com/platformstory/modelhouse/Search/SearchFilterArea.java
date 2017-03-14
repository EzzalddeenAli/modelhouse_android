package com.platformstory.modelhouse.Search;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.platformstory.modelhouse.Common.Network;
import com.platformstory.modelhouse.R;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_filter_area);

////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 만일 검색 필터를 적용 후 다시 검색 필터 화면이 로딩되었을 시 이전 필터 값을 불러와서 설정해 둠
        Intent intent = getIntent();
        estate_type = intent.getIntExtra("estate_type", 0);
        deal_type = intent.getIntExtra("deal_type", 0);
        price_type = intent.getIntExtra("price_type", 0);

        price_from = intent.getIntExtra("price_from", 0);
        price_to = intent.getIntExtra("price_to", 10000);
        monthly_from = intent.getIntExtra("monthly_from", 0);
        monthly_to = intent.getIntExtra("monthly_to", 10000);
        extent_from = intent.getIntExtra("extent_from", 0);
        extent_to = intent.getIntExtra("extent_to", 10000);

        monthly_annual = intent.getIntExtra("monthly_annual", 0);

        // 검색 항목 선택을 위한 라디오 그룹과 RangeSeekBar 변수 선언
        estate_type_rg = (RadioGroup)findViewById(R.id.estate_type);
        deal_type_rg = (RadioGroup)findViewById(R.id.deal_type);
        price_type_rg = (RadioGroup)findViewById(R.id.price_type);

        price_range_rs = (RangeSeekBar) findViewById(R.id.price_range);
        monthly_range_rs = (RangeSeekBar) findViewById(R.id.monthly_range);
        extent_range_rs = (RangeSeekBar) findViewById(R.id.extent_range);

        monthly_annual_rg = (RadioGroup)findViewById(R.id.monthly_or_annual);

        // 이전 필터 값을 가져와서 라디오 그룹과 RangeSeekBar에 값 설정
        switch(estate_type){
            case 1 :
                estate_type_rg.check(R.id.land);
                break;
            case 2 :
                estate_type_rg.check(R.id.building);
                break;
        }

        switch(deal_type){
            case 1 :
                deal_type_rg.check(R.id.office);
                break;
            case 2 :
                deal_type_rg.check(R.id.direct);
                break;
        }

        switch(price_type){
            case 1 :
                price_type_rg.check(R.id.trade);
                break;
            case 2 :
                price_type_rg.check(R.id.lent);
                break;
            case 3 :
                price_type_rg.check(R.id.monthly);
                break;
        }

        switch(monthly_annual){
            case 1 :
                monthly_annual_rg.check(R.id.month);
                break;
            case 2 :
                monthly_annual_rg.check(R.id.annual);
                break;
        }

        price_range_rs.setSelectedMinValue(price_from);
        price_range_rs.setSelectedMaxValue(price_to);

        monthly_range_rs.setSelectedMinValue(monthly_from);
        monthly_range_rs.setSelectedMaxValue(monthly_to);

        extent_range_rs.setSelectedMinValue(extent_from);
        extent_range_rs.setSelectedMaxValue(extent_to);

///////////////////////////////////////////////////////////////////////////////////////
        //시-구-동 선택을 위한 텍스트 뷰 선언과 리스너 등록
        si = (TextView)findViewById(R.id.si);
        gu = (TextView)findViewById(R.id.gu);
        dong = (TextView)findViewById(R.id.dong);

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Network.isNetworkAvailable(SearchFilterArea.this)) {
                    Log.i("modelhouse", "현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요");
                    Toast.makeText(SearchFilterArea.this, "현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요", Toast.LENGTH_LONG).show();
                } else {
                    si_list = new String[]{"서울", "인천", "경기"};
                    AlertDialog.Builder dlg = new AlertDialog.Builder(SearchFilterArea.this);
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
                            AddressThread addrThread = new AddressThread("http://52.79.106.71/api/address?table=addr_gues&value=" + addr_si_id, "addr_gues", "POST");
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
                if(!Network.isNetworkAvailable(SearchFilterArea.this)){
                    Log.i("modelhouse","현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요");
                    Toast.makeText(SearchFilterArea.this, "현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요", Toast.LENGTH_LONG).show();
                }else {
                    if (gu_list == null) {
                        Toast.makeText(SearchFilterArea.this, "시/도 를 먼저 선택하세요", Toast.LENGTH_LONG).show();
                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(SearchFilterArea.this);
                        dlg.setTitle("시/군/구 선택");
                        dlg.setItems(gu_list, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addr_gu_id = gu_ids[i];
                                dong_list = null;
                                dong_ids = null;
                                gu.setText(gu_list[i]);
                                dong.setText("읍/면/동 선택");
                                AddressThread addrThread = new AddressThread("http://52.79.106.71/api/address?table=addr_dongs&value=" + gu_ids[i], "addr_dongs", "POST");
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
                if(!Network.isNetworkAvailable(SearchFilterArea.this)){
                    Log.i("modelhouse","현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요");
                    Toast.makeText(SearchFilterArea.this, "현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요", Toast.LENGTH_LONG).show();
                }else {
                    if (dong_list == null) {
                        Toast.makeText(SearchFilterArea.this, "시/군/구 를 먼저 선택하세요", Toast.LENGTH_LONG).show();
                    } else {
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
            }
        });

/////////////////////////////////////////////////////////////////////////////////

        // 토지-건물 라디오를 선택하였을 때
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
        price_range_rs.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener(){
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                price_from = (int)minValue;
                price_to = (int)maxValue;
                ((TextView)findViewById(R.id.price_range_tv)).setText(minValue + "~" + maxValue + "원");
            }
        });

        // 임대료 설정
        monthly_range_rs.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener(){
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                monthly_from = (int)minValue;
                monthly_to = (int)maxValue;
                ((TextView)findViewById(R.id.monthly_range_tv)).setText(minValue + "~" + maxValue + "원");
            }
        });

        // 면적 설정
        extent_range_rs.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener(){
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                extent_from = (int)minValue;
                extent_to = (int)maxValue;
                ((TextView)findViewById(R.id.extent_range_tv)).setText(minValue + "~" + maxValue + "㎡");
            }
        });

        // 월세-년세 설정
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

//////////////////////////////////////////////////////////////////////////////////////////////

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
                //반드시 지역을 선택해야 검색 로직이 수행됨
                if(addr_si_id==0 && addr_gu_id==0 && addr_dong_id==0){
                    Toast.makeText(SearchFilterArea.this, "지역을 선택해 주세요", Toast.LENGTH_LONG).show();
                }else{
                    if(!Network.isNetworkAvailable(SearchFilterArea.this)){
                        Log.i("modelhouse","현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요");
                        Toast.makeText(SearchFilterArea.this, "현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요", Toast.LENGTH_LONG).show();
                    }else{
                        Log.i("modelhouse", "[검색 화면에서 검색] http://52.79.106.71/api/search?addr_si_id="+addr_si_id+"&addr_gu_id="+addr_gu_id+"&addr_dong_id="+addr_dong_id
                                +"&estate_type="+estate_type+"&deal_type="+deal_type+"&price_type="+price_type
                                +"&price_from="+price_from+"&price_to="+price_to+"&monthly_from="+monthly_from+"&monthly_to="+monthly_to
                                +"&extent_from="+extent_from+"&extent_to="+extent_to+"&monthly_annual="+monthly_annual);

                        SearchThread searchThread
                                = new SearchThread("http://52.79.106.71/api/search?addr_si_id="+addr_si_id+"&addr_gu_id="+addr_gu_id+"&addr_dong_id="+addr_dong_id
                                +"&estate_type="+estate_type+"&deal_type="+deal_type+"&price_type="+price_type
                                +"&price_from="+price_from+"&price_to="+price_to+"&monthly_from="+monthly_from+"&monthly_to="+monthly_to
                                +"&extent_from="+extent_from+"&extent_to="+extent_to+"&monthly_annual="+monthly_annual, "POST");
                        searchThread.start();
                    }
                }
            }
        });
    }

//////////////////////////////////////////////////////////////////////////////////////////////

    class SearchThread extends Thread{
        String mAddr;
        String method;

        SearchThread(String mAddr, String method){
            this.mAddr = mAddr;
            this.method = method;
        }

        public void run(){
            String[] searchResult = Network.DownloadHtml(mAddr, method).split("`");

            String latitude = searchResult[0];
            String longitude = searchResult[1];
            String searchJSON = searchResult[2];

            Intent intent = new Intent();
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.putExtra("searchJSON", searchJSON);

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

            setResult(RESULT_OK, intent);
            finish();
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////

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