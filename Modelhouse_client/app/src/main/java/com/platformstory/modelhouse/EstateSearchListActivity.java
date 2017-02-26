package com.platformstory.modelhouse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EstateSearchListActivity  extends Activity {
    ProgressDialog mProgress;
    ArrayList<Estate> estates;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estate_search_list);

        Intent intent = getIntent();
        String JSON = intent.getStringExtra("JSON");

        estates = new ArrayList<Estate>();

        try {
            JSONArray ja = new JSONArray(JSON);

            //Log.i("modelhouse", ja.length()+"");

            for (int i = 0; i < ja.length(); i++) {
                JSONObject estateObj = ja.getJSONObject(i);
                Estate estate = new Estate(estateObj.getInt("id"), estateObj.getString("type"), estateObj.getString("photo"), estateObj.getString("price_type"),
                        estateObj.getString("price"),
                        estateObj.getString("extent"), estateObj.getString("category"), estateObj.getString("usearea"), estateObj.getString("facility"),
                        estateObj.getString("addr1"), estateObj.getString("info"), estateObj.getDouble("latitude"), estateObj.getDouble("longtitude"));
                estates.add(estate);
            }
        }catch (JSONException e){

        }

        EstateList estateList = new EstateList(EstateSearchListActivity.this, R.layout.estate_list, estates);

        ListView MyList = (ListView)findViewById(R.id.list);
        MyList.setAdapter(estateList);

        MyList.setOnItemClickListener(estateDetail);
    }

    AdapterView.OnItemClickListener estateDetail = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(EstateSearchListActivity.this, EstateDetailActivity.class);
            intent.putExtra("estate_id", estates.get(position).id+"");
            intent.putExtra("latitude", estates.get(position).latitude);
            intent.putExtra("longtitude", estates.get(position).longtitude);

            startActivity(intent);

            //Toast.makeText(MainActivity.this, estates.get(position).id+"", Toast.LENGTH_LONG).show();
        }
    };
}
