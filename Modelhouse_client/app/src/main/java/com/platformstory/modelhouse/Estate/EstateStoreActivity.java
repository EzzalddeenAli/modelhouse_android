package com.platformstory.modelhouse.Estate;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.platformstory.modelhouse.R;


public class EstateStoreActivity extends Activity {
    Button btn_photo_1, btn_photo_2, btn_photo_3, btn_photo_4, btn_photo_5, btn_photo_6, btn_photo_7, btn_photo_8, btn_photo_9, btn_photo_10;
    Button btn_photo_11, btn_photo_12, btn_photo_13, btn_photo_14, btn_photo_15;
    ImageView img_photo_1, img_photo_2, img_photo_3, img_photo_4, img_photo_5, img_photo_6, img_photo_7, img_photo_8, img_photo_9, img_photo_10;
    ImageView img_photo_11, img_photo_12, img_photo_13, img_photo_14, img_photo_15;

    final static int SELECT_PICTURE = 2;

    private String selectedImagePath;
    private int button_id;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estate_store);

        btn_photo_1 = (Button)findViewById(R.id.btn_photo_1);
        btn_photo_2 = (Button)findViewById(R.id.btn_photo_2);
        btn_photo_3 = (Button)findViewById(R.id.btn_photo_3);
        btn_photo_4 = (Button)findViewById(R.id.btn_photo_4);
        btn_photo_5 = (Button)findViewById(R.id.btn_photo_5);
        btn_photo_6 = (Button)findViewById(R.id.btn_photo_6);
        btn_photo_7 = (Button)findViewById(R.id.btn_photo_7);
        btn_photo_8 = (Button)findViewById(R.id.btn_photo_8);
        btn_photo_9 = (Button)findViewById(R.id.btn_photo_9);
        btn_photo_10 = (Button)findViewById(R.id.btn_photo_10);
        btn_photo_11 = (Button)findViewById(R.id.btn_photo_11);
        btn_photo_12 = (Button)findViewById(R.id.btn_photo_12);
        btn_photo_13 = (Button)findViewById(R.id.btn_photo_13);
        btn_photo_14 = (Button)findViewById(R.id.btn_photo_14);
        btn_photo_15 = (Button)findViewById(R.id.btn_photo_15);

        btn_photo_1.setOnClickListener(select_photo);
        btn_photo_2.setOnClickListener(select_photo);
        btn_photo_3.setOnClickListener(select_photo);
        btn_photo_4.setOnClickListener(select_photo);
        btn_photo_5.setOnClickListener(select_photo);
        btn_photo_6.setOnClickListener(select_photo);
        btn_photo_7.setOnClickListener(select_photo);
        btn_photo_8.setOnClickListener(select_photo);
        btn_photo_9.setOnClickListener(select_photo);
        btn_photo_10.setOnClickListener(select_photo);
        btn_photo_11.setOnClickListener(select_photo);
        btn_photo_12.setOnClickListener(select_photo);
        btn_photo_13.setOnClickListener(select_photo);
        btn_photo_14.setOnClickListener(select_photo);
        btn_photo_15.setOnClickListener(select_photo);

        img_photo_1 = (ImageView)findViewById(R.id.img_photo_1);
        img_photo_2 = (ImageView)findViewById(R.id.img_photo_2);
        img_photo_3 = (ImageView)findViewById(R.id.img_photo_3);
        img_photo_4 = (ImageView)findViewById(R.id.img_photo_4);
        img_photo_5 = (ImageView)findViewById(R.id.img_photo_5);
        img_photo_6 = (ImageView)findViewById(R.id.img_photo_6);
        img_photo_7 = (ImageView)findViewById(R.id.img_photo_7);
        img_photo_8 = (ImageView)findViewById(R.id.img_photo_8);
        img_photo_9 = (ImageView)findViewById(R.id.img_photo_9);
        img_photo_10 = (ImageView)findViewById(R.id.img_photo_10);
        img_photo_11 = (ImageView)findViewById(R.id.img_photo_11);
        img_photo_12 = (ImageView)findViewById(R.id.img_photo_12);
        img_photo_13 = (ImageView)findViewById(R.id.img_photo_13);
        img_photo_14 = (ImageView)findViewById(R.id.img_photo_14);
        img_photo_15 = (ImageView)findViewById(R.id.img_photo_15);

    }

    View.OnClickListener select_photo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            button_id = v.getId();

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);

                switch (button_id){
                    case R.id.btn_photo_1:
                        Glide.with(this).load(selectedImageUri).into(img_photo_1);
                        break;
                    case R.id.btn_photo_2:
                        Glide.with(this).load(selectedImageUri).into(img_photo_2);
                        break;
                    case R.id.btn_photo_3:
                        Glide.with(this).load(selectedImageUri).into(img_photo_3);
                        break;
                    case R.id.btn_photo_4:
                        Glide.with(this).load(selectedImageUri).into(img_photo_4);
                        break;
                    case R.id.btn_photo_5:
                        Glide.with(this).load(selectedImageUri).into(img_photo_5);
                        break;
                    case R.id.btn_photo_6:
                        Glide.with(this).load(selectedImageUri).into(img_photo_6);
                        break;
                    case R.id.btn_photo_7:
                        Glide.with(this).load(selectedImageUri).into(img_photo_7);
                        break;
                    case R.id.btn_photo_8:
                        Glide.with(this).load(selectedImageUri).into(img_photo_8);
                        break;
                    case R.id.btn_photo_9:
                        Glide.with(this).load(selectedImageUri).into(img_photo_9);
                        break;
                    case R.id.btn_photo_10:
                        Glide.with(this).load(selectedImageUri).into(img_photo_10);
                        break;
                    case R.id.btn_photo_11:
                        Glide.with(this).load(selectedImageUri).into(img_photo_11);
                        break;
                    case R.id.btn_photo_12:
                        Glide.with(this).load(selectedImageUri).into(img_photo_12);
                        break;
                    case R.id.btn_photo_13:
                        Glide.with(this).load(selectedImageUri).into(img_photo_13);
                        break;
                    case R.id.btn_photo_14:
                        Glide.with(this).load(selectedImageUri).into(img_photo_14);
                        break;
                    case R.id.btn_photo_15:
                        Glide.with(this).load(selectedImageUri).into(img_photo_15);
                        break;
                }
            }
        }
    }

    /**
     * 사진의 URI 경로를 받는 메소드
     */
    public String getPath(Uri uri) {
        // uri가 null일경우 null반환
        if( uri == null ) {
            return null;
        }
        // 미디어스토어에서 유저가 선택한 사진의 URI를 받아온다.
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // URI경로를 반환한다.
        return uri.getPath();
    }

//    여러개 사진 선택가능하게할때
//    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); 이런식으로 intent에 MULTIPLE을 허용해준다.
//
//    그리고 Result부분에 이 소스를 추가해준다.
//            if (Intent.ACTION_SEND_MULTIPLE.equals(data.getAction()))
//            && Intent.hasExtra(Intent.EXTRA_STREAM)) {
//        // retrieve a collection of selected images
//        ArrayList<Parcelable> list = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
//        // iterate over these images
//        if( list != null ) {
//            for (Parcelable parcel : list) {
//                Uri uri = (Uri) parcel;
//                // TODO handle the images one by one here
//            }
//        }
//    }

}
