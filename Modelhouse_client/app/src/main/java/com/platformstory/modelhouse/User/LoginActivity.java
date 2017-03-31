package com.platformstory.modelhouse.User;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.platformstory.modelhouse.BaseActivity;
import com.platformstory.modelhouse.Common.UtilLibs;
import com.platformstory.modelhouse.DTO.Estate;
import com.platformstory.modelhouse.DTO.NetworkService;
import com.platformstory.modelhouse.DTO.User;
import com.platformstory.modelhouse.Estate.EstateDetailActivity;
import com.platformstory.modelhouse.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class LoginActivity extends Activity {
    EditText et_email;
    EditText et_password;
    Button btn_login;
    Button btn_find_mail;
    Button btn_find_password;

    User user_info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        et_email = (EditText)findViewById(R.id.email);
        et_email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        et_password = (EditText)findViewById(R.id.password);

        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = et_email.getText().toString();
                final String password = et_password.getText().toString();

                new AsyncTask<Integer, Integer, Integer>() {
                    @Override
                    protected Integer doInBackground(Integer... params) {
                        NetworkService networkService = NetworkService.retrofit.create(NetworkService.class);
                        Call<List<User>> user = networkService.login(email, password);

                        try {
                            user_info = user.execute().body().get(0);
                            return 1;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return 0;
                    }

                    protected void onPostExecute(Integer result) {
                        if(result==1){
                            Intent intent = new Intent();

                            intent.putExtra("id", user_info.getId()+"");
                            intent.putExtra("name", user_info.getName());
                            intent.putExtra("email", user_info.getEmail());

                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }.execute();
            }
        });

        btn_find_mail = (Button)findViewById(R.id.btn_find_mail);
        btn_find_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindEmailActivity.class);
                startActivity(intent);
            }
        });

        btn_find_password = (Button)findViewById(R.id.btn_find_password);
        btn_find_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
