package com.platformstory.modelhouse.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.platformstory.modelhouse.BaseActivity;
import com.platformstory.modelhouse.R;

public class FindEmailActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_find_mail);
        actList.add(this);

        EditText mobile = (EditText)findViewById(R.id.mobile);
        Button btn_ok = (Button) findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(FindEmailActivity.this, FoundEmailActivity.class);
                startActivity(intent);
            }
        });
    }
}
