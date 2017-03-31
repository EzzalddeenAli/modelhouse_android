package com.platformstory.modelhouse.User;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.platformstory.modelhouse.BaseActivity;
import com.platformstory.modelhouse.R;


public class FindPasswordActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_find_password);
        actList.add(this);

        ((Button)findViewById(R.id.btn_find_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
