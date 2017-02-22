package com.platformstory.modelhouse;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class EstateDetailActivity extends Activity{
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estate_detail);

        TextView estate_id_text = (TextView)findViewById(R.id.estate_id);

        Intent intent = getIntent();
        String estate_id = intent.getStringExtra("estate_id");

        estate_id_text.setText(estate_id);
    }
}
