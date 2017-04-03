package com.platformstory.modelhouse.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.platformstory.modelhouse.DTO.Estate;
import com.platformstory.modelhouse.Common.Network;
import com.platformstory.modelhouse.R;

import java.util.ArrayList;


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
        return estates.get(position).getId()+"";
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

        Glide.with(maincon).load(Network.IMAGE_URL + "files/"+estates.get(position).getPhoto()).into(img);

        TextView estate_price = (TextView)convertView.findViewById(R.id.estate_price);
        TextView estate_basic = (TextView)convertView.findViewById(R.id.estate_basic);
        TextView estate_info = (TextView)convertView.findViewById(R.id.estate_info);
        TextView estate_facility = (TextView)convertView.findViewById(R.id.estate_facility);
        TextView estate_addr1 = (TextView)convertView.findViewById(R.id.estate_addr1);

        estate_price.setText(estates.get(position).getPrice_type() + ", " + estates.get(position).getPrice());
        estate_basic.setText(estates.get(position).getType() + " | " + estates.get(position).getExtent() +"㎡ | "+ estates.get(position).getCategory() +" | "+ estates.get(position).getUsearea());
        estate_info.setText(estates.get(position).getInfo());
        estate_facility.setText(estates.get(position).getFacility());
        estate_addr1.setText(estates.get(position).getAddr1());

        return convertView;
    }
}




