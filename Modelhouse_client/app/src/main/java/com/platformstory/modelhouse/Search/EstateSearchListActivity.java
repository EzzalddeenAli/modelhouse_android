package com.platformstory.modelhouse.Search;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.platformstory.modelhouse.Common.UtilLibs;
import com.platformstory.modelhouse.DTO.Estate;
import com.platformstory.modelhouse.Common.Network;
import com.platformstory.modelhouse.DTO.NetworkService;
import com.platformstory.modelhouse.Estate.EstateDetailActivity;
import com.platformstory.modelhouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;

public class EstateSearchListActivity extends Activity {
    ArrayList<Estate> estates;

    String latitude;
    String longitude;
    String estate_type;
    String deal_type;
    String price_type;
    String price_from;
    String price_to;
    String monthly_from;
    String monthly_to;
    String extent_from;
    String extent_to;
    String monthly_annual;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estate_search_list);

        Intent intent = getIntent();

        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        estate_type = intent.getStringExtra("estate_type");
        deal_type = intent.getStringExtra("deal_type");
        price_type = intent.getStringExtra("price_type");
        price_from = intent.getStringExtra("price_from");
        price_to = intent.getStringExtra("price_to");
        monthly_from = intent.getStringExtra("monthly_from");
        monthly_to = intent.getStringExtra("monthly_to");
        extent_from = intent.getStringExtra("extent_from");
        extent_to = intent.getStringExtra("extent_to");
        monthly_annual = intent.getStringExtra("monthly_annual");

        Log.i("modelhouse", "latitude="+latitude+"&longitude="+longitude
                +"&estate_type="+estate_type+"&deal_type="+deal_type+"&price_type="+price_type
                +"&price_from="+price_from+"&price_to="+price_to+"&monthly_from="+monthly_from+"&monthly_to="+monthly_to
                +"&extent_from="+extent_from+"&extent_to="+extent_to+"&monthly_annual="+monthly_annual);


        new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                NetworkService networkService = NetworkService.retrofit.create(NetworkService.class);
                Call<ArrayList<Estate>> estateResult = networkService.estateSearchResult(
                        latitude, longitude, estate_type, deal_type, price_type, price_from, price_to,
                        monthly_from, monthly_to, extent_from, extent_to, monthly_annual
                );

                try {
                    Log.i("modelhouse", "zzz");

                    estates = estateResult.execute().body();

                    Log.i("modelhouse", estates.size()+"");

                    return 1;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return 0;
            }

            protected void onPostExecute(Integer result) {
                if(result==1){
                    EstateList estateList = new EstateList(EstateSearchListActivity.this, R.layout.estate_list, estates);

                    ListView MyList = (ListView)findViewById(R.id.list);
                    MyList.setAdapter(estateList);

                    MyList.setOnItemClickListener(estateDetail);
                }
            }
        }.execute();

//        estates = new ArrayList<Estate>();


    }

    AdapterView.OnItemClickListener estateDetail = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(!Network.isNetworkAvailable(EstateSearchListActivity.this)){
                Log.i("modelhouse","현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요");
                Toast.makeText(EstateSearchListActivity.this, "현재 네트워크에 연결되어 있지 않습니다.\n네트워크 연결 상태를 확인해 주세요", Toast.LENGTH_LONG).show();
            }else {
                Intent intent = new Intent(EstateSearchListActivity.this, EstateDetailActivity.class);
                intent.putExtra("estate_id", estates.get(position).getId() + "");
                intent.putExtra("latitude", estates.get(position).getLatitude());
                intent.putExtra("longtitude", estates.get(position).getLongtitude());

                startActivity(intent);
            }
        }
    };
}
