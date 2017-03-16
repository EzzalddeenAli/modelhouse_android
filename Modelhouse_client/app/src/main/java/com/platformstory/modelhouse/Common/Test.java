package com.platformstory.modelhouse.Common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.platformstory.modelhouse.Common.Network;
import com.platformstory.modelhouse.Common.UtilLibs;
import com.platformstory.modelhouse.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

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
import java.util.HashMap;


public class Test extends Activity {
    Button btn_photo_1;
    ImageView img_photo_1;

    final static int SELECT_PICTURE = 2;

    String absolutePath;
    String urlString;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estate_store);

        img_photo_1 = (ImageView)findViewById(R.id.img_photo_1);

        btn_photo_1 = (Button)findViewById(R.id.btn_photo_1);
        btn_photo_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImageUri = data.getData();

                //절대경로를 획득한다!!! 중요~
                Cursor c = getContentResolver().query(Uri.parse(selectedImageUri.toString()), null,null,null,null);
                c.moveToNext();
                absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));


                ImageUploadTask imageUploadTask = new ImageUploadTask();
                imageUploadTask.execute();

                Glide.with(this).load(selectedImageUri).into(img_photo_1);
            }
        }
    }

    class ImageUploadTask extends AsyncTask<Integer, Integer, Integer>{

        @Override
        protected Integer doInBackground(Integer... params) {
            // Create MultipartEntityBuilder
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            // Set String Params
            builder.addTextBody("Key 값", "Value 값", ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("Key 값", "Value 값", ContentType.create("Multipart/related", "UTF-8"));

            // Set File Params
            builder.addPart("Key 값", new FileBody(new File(absolutePath)));
            // File 이 여러개 인 경우 아래와 같이 adpart 를 하나 더 추가해 주면 된다.
            builder.addPart("Key 값", new FileBody(new File("File 경로")));

            // Send Request
            try {
                InputStream inputStream = null;
                HttpClient httpClient = AndroidHttpClient.newInstance("Android");
                HttpPost httpPost = new HttpPost("이미지를 업로드할 서버 url");
                httpPost.setEntity(builder.build());
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();

                // Response
                BufferedReader bufferdReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;

                while ((line = bufferdReader.readLine()) != null) {
                    Log.i(UtilLibs.LOG_TAG, line);
                    stringBuilder.append(line + "\n");
                }
                inputStream.close();
            }catch(IOException e){
                e.printStackTrace();
            }

            return 0;
        }
    }
}


//package com.platformstory.modelhouse;
//
//import android.os.AsyncTask;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Toast;
//
//import com.google.ads.AdRequest;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Created by Midas Yoon on 2017-03-06.
// */
//public class Test {
//    private void getUserCastingListData() {
//        String url = CommonValues.URL_SERVER + CONNECT_URL;
//        String paramStr = "session_id=" + SharedPreference.getUserSessionID(getActivity());
//
//
//        Http.ParamModel params = new Http.ParamModel();
//        params.setUrl(url);
//        params.setParamStr(paramStr);
//
//        //AsyncTask
//        getUserCastingListDataTask = new GetUserCastingListDataTask();
//        getUserCastingListDataTask.execute(params);
//    }
//
//    private class GetUserCastingListDataTask extends AsyncTask<Http.ParamModel, Void, Integer> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Integer doInBackground(Http.ParamModel... params) {
//
//            Http http = new Http();
//            String result = http.send(params[0]);
//
//            if (TextUtils.isEmpty(result)) {
//                return AdRequest.ErrorCode.INIT;
//            }
//
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//
//                int code = jsonObject.getInt("code");
//
//                switch (code) {
//
//                    case AdRequest.ErrorCode.SUCCESS:
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                        if (jsonArray.length() == 0) {
//                            return AdRequest.ErrorCode.ERR_DB_NODATA;
//                        }
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObj = jsonArray.getJSONObject(i);
//
//                            CastingModel model = new CastingModel();
//                            model.setId(jsonObj.getInt("id"));
//                            model.setEvent_name(jsonObj.getString("event_name"));
//                            model.setCasting_start_date(jsonObj.getString("start_date"));
//                            model.setCasting_end_date(jsonObj.getString("end_date"));
//                            model.setEvent_date(jsonObj.getString("event_date"));
//                            model.setDayover(jsonObj.getString("dayover"));
//                            model.setD_day(jsonObj.getString("days"));
//                            model.setRemain_time(jsonObj.getString("days") + "일 " + jsonObj.getString("hours") + "시간 " + jsonObj.getString("minutes") + "분");
//                            model.setFlag(jsonObj.getString("flag"));
//                            model.setEvent_address(jsonObj.getString("address"));
//                            model.setEvent_start_time(jsonObj.getString("start_time"));
//                            model.setEvent_end_time(jsonObj.getString("end_time"));
//                            model.setEvent_target_people(jsonObj.getString("target"));
//                            model.setEvent_people_num(jsonObj.getString("target_num"));
//                            model.setBid_id(jsonObj.getString("engchal_id"));
//                            model.setBid_flag(jsonObj.getString("engchal"));
//                            model.setDuration(jsonObj.getInt("duration"));
//
//                            if (jsonObj.getString("flag").equals("M")) {
//                                model.setCasting_large(jsonObj.getString("job"));
//                                model.setCasting_small(jsonObj.getString("category"));
//                                model.setCasting_count(jsonObj.getInt("count"));
//                                model.setBid_finish(jsonObj.getString("finish"));
//                            } else if (jsonObj.getString("flag").equals("S")) {
//                                model.setEntertainer_name(jsonObj.getString("name"));
//                            }
//
//                            models.add(model);
//                        }
//                        break;
//
//                    case AdRequest.ErrorCode.ERR_DB_NODATA:
//                        break;
//                }
//
//                return code;
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return AdRequest.ErrorCode.INIT;
//        }
//
//        @Override
//        protected void onPostExecute(Integer code) {
//
//            switch (code) {
//                case AdRequest.ErrorCode.SUCCESS:
//                    adapter.notifyDataSetChanged();
//                    txt_no_data.setVisibility(View.GONE);
//                    break;
//                case AdRequest.ErrorCode.ERR_DB_NODATA:
//                    adapter.notifyDataSetChanged();
//                    txt_no_data.setVisibility(View.VISIBLE);
//                    break;
//                case AdRequest.ErrorCode.INIT:
//                    Toast.makeText(getActivity(), getString(R.string.init), Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    }
//
//}
