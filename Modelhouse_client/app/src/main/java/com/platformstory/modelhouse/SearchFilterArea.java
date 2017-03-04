package com.platformstory.modelhouse;

import android.app.Activity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class SearchFilterArea extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_filter_area);

        ((RadioGroup)findViewById(R.id.price_type)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.monthly :
                        ((TextView)findViewById(R.id.price_tv)).setText("보증금");
                        ((LinearLayout)findViewById(R.id.lent_price)).setVisibility(View.VISIBLE);
                        break;
                    case R.id.trade:
                        ((TextView)findViewById(R.id.price_tv)).setText("매매가");
                        ((LinearLayout)findViewById(R.id.lent_price)).setVisibility(View.GONE);
                        break;
                    case R.id.lent:
                        ((TextView)findViewById(R.id.price_tv)).setText("전세금");
                        ((LinearLayout)findViewById(R.id.lent_price)).setVisibility(View.GONE);
                        break;
                }
            }
        });

        ((RangeSeekBar) findViewById(R.id.price_range)).setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener(){
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                ((TextView)findViewById(R.id.price_range_tv)).setText(minValue + "~" + maxValue + "원");
            }
        });

        ((RangeSeekBar) findViewById(R.id.extent_range)).setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener(){
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                ((TextView)findViewById(R.id.extent_range_tv)).setText(minValue + "~" + maxValue + "㎡");
            }
        });

        ((RangeSeekBar) findViewById(R.id.monthly_range)).setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener(){
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                ((TextView)findViewById(R.id.monthly_range_tv)).setText(minValue + "~" + maxValue + "원");
            }
        });

        ((Button)findViewById(R.id.reset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RadioGroup)findViewById(R.id.estate_type)).clearCheck();
                ((RadioGroup)findViewById(R.id.deal_type)).clearCheck();
                ((RadioGroup)findViewById(R.id.price_type)).clearCheck();
                ((RadioGroup)findViewById(R.id.monthly_or_annual)).clearCheck();

                ((RangeSeekBar) findViewById(R.id.price_range)).setSelectedMinValue(0);
                ((RangeSeekBar) findViewById(R.id.price_range)).setSelectedMaxValue(1000);
                ((TextView)findViewById(R.id.price_range_tv)).setText("0~제한없음");

                ((RangeSeekBar) findViewById(R.id.extent_range)).setSelectedMinValue(0);
                ((RangeSeekBar) findViewById(R.id.extent_range)).setSelectedMaxValue(1000);
                ((TextView)findViewById(R.id.extent_range_tv)).setText("0~제한없음");

                ((RangeSeekBar) findViewById(R.id.monthly_range)).setSelectedMinValue(0);
                ((RangeSeekBar) findViewById(R.id.monthly_range)).setSelectedMaxValue(1000);
                ((TextView)findViewById(R.id.monthly_range_tv)).setText("0~제한없음");
            }
        });
    }
}


//
//        // Setup the new range seek bar
//        RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<>(this);
//        // Set the range
//        rangeSeekBar.setRangeValues(15, 90);
//        rangeSeekBar.setSelectedMinValue(20);
//        rangeSeekBar.setSelectedMaxValue(88);
//        rangeSeekBar.setTextAboveThumbsColorResource(android.R.color.black);
//
//        // Add to layout
//        FrameLayout layout = (FrameLayout) findViewById(R.id.seekbar_placeholder);
//        layout.addView(rangeSeekBar);
//
//        // Seek bar for which we will set text color in code