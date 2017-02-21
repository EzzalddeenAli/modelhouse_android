package com.platformstory.modelhouse;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Midas Yoon on 2017-02-21.
 */
public class EstateList extends BaseAdapter{
    Context maincon;
    LayoutInflater Inflator;
    ArrayList<Estate> estates;
    int layout;

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
        ImageView img = (ImageView)convertView.findViewById(R.id.estate_photo);
        //img.setImageResource(estates.get(position).photo);

        TextView estate_price = (TextView)convertView.findViewById(R.id.estate_price);
        TextView estate_basic = (TextView)convertView.findViewById(R.id.estate_basic);
        TextView estate_info = (TextView)convertView.findViewById(R.id.estate_info);
        TextView estate_facility = (TextView)convertView.findViewById(R.id.estate_facility);
        TextView estate_addr1 = (TextView)convertView.findViewById(R.id.estate_addr1);

        estate_price.setText(estates.get(position).price_type + ", " + estates.get(position).price);
        estate_basic.setText(estates.get(position).type + "|" + estates.get(position).extent +"|"+ estates.get(position).category +"|"+ estates.get(position).usearea);
        estate_info.setText(estates.get(position).info);
        estate_facility.setText(estates.get(position).facility);
        estate_addr1.setText(estates.get(position).addr1);

        return convertView;
    }
}

class Estate{
    int id; int type; String photo; int price_type; String price; String monthly_price; String annual_price; String extent; String category; String usearea;
    String facility; String addr1; Double latitude; Double longtitude; String info;

    Estate(int id, int type, String photo, int price_type, String price, String monthly_price, String annual_price, String extent, String category, String usearea,
           String facility, String addr1, Double latitude, Double longtitude, String info){
        this.id = id;
        this.type = type;
        this.photo = photo;
        this.price_type = price_type;
        this.price = price;
        this.monthly_price = monthly_price;
        this.annual_price = annual_price;
        this.extent = extent;
        this.category = category;
        this.usearea = usearea;
        this.facility = facility;
        this.addr1 = addr1;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.info = info;
    }
}
