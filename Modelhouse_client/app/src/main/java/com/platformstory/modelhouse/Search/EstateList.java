package com.platformstory.modelhouse.Search;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.platformstory.modelhouse.Common.Network;
import com.platformstory.modelhouse.R;

import java.util.ArrayList;

/**
 * Created by Midas Yoon on 2017-02-21.
 */
public class EstateList extends BaseAdapter{
    Context maincon;
    LayoutInflater Inflator;
    ArrayList<Estate> estates;
    int layout;
    ImageView img;

    public EstateList(Context context, int alayout, ArrayList<Estate> estates){
        maincon = context;
        Inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.estates = estates;
        layout = alayout;
    }

    public int getCount(){
        return estates.size();
    }

    public String getItem(int position){
        return estates.get(position).id+"";
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        if(convertView==null){
            convertView = Inflator.inflate(layout, parent, false);
        }
        img = (ImageView)convertView.findViewById(R.id.estate_photo);

        Glide.with(maincon).load(Network.IMAGE_URL + "files/"+estates.get(position).photo).into(img);


//        (new DownImageThread("http://tourplatform.net:2500/storage/files/"+estates.get(position).photo)).start();
//        (new DownImageThread("http://postfiles14.naver.net/20091021_93/studiopj_1256099112708tbtqU_jpg/45625_studiopj.jpg")).start();

        //Log.i("modelhouse", "http://tourplatform.net:2500/storage/files/"+estates.get(position).photo);

        //img.setImageResource(estates.get(position).photo);

        TextView estate_price = (TextView)convertView.findViewById(R.id.estate_price);
        TextView estate_basic = (TextView)convertView.findViewById(R.id.estate_basic);
        TextView estate_info = (TextView)convertView.findViewById(R.id.estate_info);
        TextView estate_facility = (TextView)convertView.findViewById(R.id.estate_facility);
        TextView estate_addr1 = (TextView)convertView.findViewById(R.id.estate_addr1);

        estate_price.setText(estates.get(position).price_type + ", " + estates.get(position).price);
        estate_basic.setText(estates.get(position).type + " | " + estates.get(position).extent +"㎡ | "+ estates.get(position).category +" | "+ estates.get(position).usearea);
        estate_info.setText(estates.get(position).info);
        estate_facility.setText(estates.get(position).facility);
        estate_addr1.setText(estates.get(position).addr1);

        return convertView;
    }

//    class DownImageThread extends Thread{
//        String mAddr;
//
//        DownImageThread(String addr) {
//            mAddr = addr;
//        }
//
//        public void run() {
//            Bitmap bit = Network.DownloadImage(mAddr);
//
//            Message message = mAfterDown.obtainMessage();
//            message.obj = bit;
//            mAfterDown.sendMessage(message);
//        }
//    }
//
//    Handler mAfterDown = new Handler() {
//        public void handleMessage(Message msg) {
//            Bitmap bit = (Bitmap)msg.obj;
//            if (bit == null) {
//                Log.i("modelhouse", "그림을 불러올 수 없음");
//            } else {
//                Glide.with(maincon).load(bit).into(img);
//
//
////                img.setImageBitmap(bit);
//            }
//        }
//    };
}

class Estate{
    int id; String type; String photo; String price_type; String price; String extent; String category; String usearea;
    String facility; String addr1; String info; Double latitude; Double longtitude;

    Estate(int id, String type, String photo, String price_type, String price, String extent, String category, String usearea,
           String facility, String addr1, String info, Double latitude, Double longtitude){
        this.id = id;
        this.type = type;
        this.photo = photo;
        this.price_type = price_type;
        this.price = price;
        this.extent = extent;
        this.category = category;
        this.usearea = usearea;
        this.facility = facility;
        this.addr1 = addr1;
        this.info = info;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }
}


