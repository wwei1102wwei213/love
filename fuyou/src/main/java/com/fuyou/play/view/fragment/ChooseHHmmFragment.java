package com.fuyou.play.view.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fuyou.play.R;
import com.fuyou.play.biz.callback.ChooseTimeInterface;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.LogCustom;
import com.fuyou.play.util.TimeUtils;
import com.fuyou.play.widget.choosetime.McTimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017-09-17.
 */

public class ChooseHHmmFragment extends DialogFragment {

    public static final String type = "ChooseHHmmFragment";

    public static ChooseHHmmFragment instance;
    private ChooseTimeInterface callback;

    public static ChooseHHmmFragment getInstance(ChooseTimeInterface callback){
        Bundle bundle = new Bundle();
        bundle.putSerializable("ChooseHHmmFragment",callback);
        instance = new ChooseHHmmFragment();
        instance.setArguments(bundle);
        return instance;
    }

    private int mHH,mMM,mAfter;
    public void setDefault(String regStr){
        try {
            int hhmm = TimeUtils.getMillForHHmm(regStr);
            LogCustom.show(hhmm+"");
            mHH = hhmm/3600;
            if (mHH==0){
                mMM = hhmm/60;
            } else {
                mMM = (hhmm-mHH*3600)/60;
            }
            if (mHH>11){
                mAfter = 1;
                mHH = mHH - 12;
            } else {
                mAfter = 0;
            }
            LogCustom.show(mHH+","+mMM+","+mAfter);
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            callback = (ChooseTimeInterface)bundle.getSerializable("ChooseHHmmFragment");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog_base_style);

        dialog.setCancelable(false);
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_common_hhmm_time, null);
        initView(rootView);
//        initData();
        dialog.setContentView(rootView);
        Window window = dialog.getWindow();
        try {
            window.getDecorView().setPadding(0, 0, 0, 0);
        }catch (Exception e){

        }
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
//        window.setWindowAnimations(R.style.mc_dialog_anim_style);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    private McTimePicker dp;
    private void initView(View v){
        dp = (McTimePicker)v.findViewById(R.id.mc_time_picker);
        dp.setDefaultValue(mHH,mMM,mAfter);
        v.findViewById(R.id.btn_time_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minute = dp.getMinute();
                int mill = dp.getMill();
                int after = dp.getAfterSelection();
                LogCustom.show(minute+","+mill+","+after);
                try {
                    int mTime = minute*60*60 + mill*60 + after*12*60*60;
                    mTime = mTime - TimeZone.getDefault().getRawOffset()/1000;
                    SimpleDateFormat sdf = new SimpleDateFormat("KK:mm a");
                    LogCustom.show(mTime+"");
                    String back = sdf.format(new Date(Long.parseLong((mTime+"").concat("000"))));
                    callback.timeCallBack(back, 2);
                    LogCustom.show(back);
                    dismiss();
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"Choose HHmm Error");
                }
            }
        });
    }

    @Override
    public void onPause() {
        dismiss();
        super.onPause();
    }
}
