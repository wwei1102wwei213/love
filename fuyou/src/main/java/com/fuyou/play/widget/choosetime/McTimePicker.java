package com.fuyou.play.widget.choosetime;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.fuyou.play.R;
import com.fuyou.play.util.ExceptionUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-09-16.
 */

public class McTimePicker extends LinearLayout {

    public McTimePicker(Context context) {
        this(context, null);
    }

    public McTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 获取选择的年
     *
     * @return
     */
    public int getMinute() {
        return mWheelMinute.getSelected();
    }

    /**
     * 获取选择的月
     *
     * @return
     */
    public int getMill() {
        return mWheelMill.getSelected();
    }

    public String getAfter() {
        return mWheelAfter.getSelectedText();
    }
    public int getAfterSelection() {
        return mWheelAfter.getSelected();
    }


    private WheelView mWheelMinute;
    private WheelView mWheelMill;
    private WheelView mWheelAfter;


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.mc_time_picker, this);

        mWheelMinute = (WheelView) findViewById(R.id.minute);
        mWheelMill = (WheelView) findViewById(R.id.mill);
        mWheelAfter = (WheelView) findViewById(R.id.after);


        // 设置默认年月日为当前日期
        mWheelMinute.setData(getMinuteList());
        mWheelMill.setData(getMillList());
        mWheelAfter.setData(getAfterList());
        mWheelMinute.setDefault(hh);
        mWheelMill.setDefault(mm);
        mWheelAfter.setDefault(after);

       /* mWheelMinute.setOnSelectListener(this);
        mWheelMill.setOnSelectListener(this);*/

    }

    private int hh,mm,after;
    public void setDefaultValue(int hh, int mm, int after){
        try {
            this.hh = hh;
            this.mm = mm;
            this.after = after;
            mWheelMinute.setDefault(hh);
            mWheelMill.setDefault(mm);
            mWheelAfter.setDefault(after);
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    private ArrayList<String> getMinuteList(){
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            if (i<10){
                list.add("0"+i);
            }else {
                list.add(String.valueOf(i));
            }
        }
        return list;
    }

    private ArrayList<String> getMillList(){
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i<10){
                list.add("0"+i);
            }else {
                list.add(String.valueOf(i));
            }
        }
        return list;
    }

    private ArrayList<String> getAfterList(){
        ArrayList<String> list = new ArrayList<>();
        list.add("AM");
        list.add("PM");
        return list;
    }


}
