package com.platformstory.modelhouse.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.platformstory.modelhouse.BaseActivity;
import com.platformstory.modelhouse.R;

public class FoundEmailActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_found_mail);
        actList.add(this);

        ((Button)findViewById(R.id.btn_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<actList.size(); i++){
                    actList.get(i).finish();
                }
            }
        });

        ((Button)findViewById(R.id.btn_find_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoundEmailActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        for(int i=0; i<actList.size(); i++){
            actList.get(i).finish();
        }
    }


}
