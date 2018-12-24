package com.fuyou.play.view.fragment;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.fuyou.play.R;
import com.fuyou.play.biz.callback.ChooseTimeInterface;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.widget.choosetime.McDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/9/14 0014.
 *
 * 选择日期控件
 *
 * @author wwei
 */
public class ChooseTimeFragment extends DialogFragment {

    public static final String type = "ChooseTimeFragment";

    public static ChooseTimeFragment instance;
    private ChooseTimeInterface callback;

    public static ChooseTimeFragment getInstance(ChooseTimeInterface callback){
        Bundle bundle = new Bundle();
        bundle.putSerializable("ChooseTimeFragment",callback);
        instance = new ChooseTimeFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            callback = (ChooseTimeInterface)bundle.getSerializable("ChooseTimeFragment");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog_base_style);

        dialog.setCancelable(false);
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_common_datetime, null);
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

    private int MMM;
    private int dd,yyyy;
    public void setDefault(String regStr){
        try {

            if (!TextUtils.isEmpty(regStr)){
                SimpleDateFormat format = new SimpleDateFormat("MMM,dd,yyyy");
                long loadingTime = 0;
                try {
                    Date time = format.parse(regStr);
                    loadingTime = time.getTime() - TimeZone.getDefault().getRawOffset();
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new RuntimeException("输入的时间格式不对");
                }
                format = new SimpleDateFormat("MM,dd,yyyy");
                String temp = format.format(new Date(loadingTime + TimeZone.getDefault().getRawOffset()));
                String[] args = temp.split(",");
                if (args.length==3){
                    MMM = Integer.valueOf(args[0]);
                    if (MMM>0) MMM--;
                    dd = Integer.valueOf(args[1]);
                    yyyy = Integer.valueOf(args[2]);
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }

    }

    private void initView(View v){
        final McDatePicker dp = (McDatePicker)v.findViewById(R.id.mc_date_picker);
        dp.setDefault(MMM, dd, yyyy);
        v.findViewById(R.id.btn_time_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempmonth = dp.getMonth();
                int monthId = dp.getMonthID()+1;
                String monthStr = monthId < 10 ? "0" + monthId : monthId+"";
                String tempday = dp.getDay();
                tempday = Integer.valueOf(tempday) < 10 ? "0" + tempday : tempday;
//                String choosetime = dp.getYear() + "-" + tempmonth + "-" + tempday;
                String choosetime = monthStr+","+tempday+","+dp.getYear();

                try {
//                    long temp = TimeUtils.showTimeToLoadTime(choosetime);
                    SimpleDateFormat format = new SimpleDateFormat("MM,dd,yyyy");
                    long loadingTime = 0;
                    try {
                        Date time = format.parse(choosetime);
                        loadingTime = time.getTime() - TimeZone.getDefault().getRawOffset();
                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new RuntimeException("输入的时间格式不对");
                    }
                    if(loadingTime> System.currentTimeMillis()){
                        Toast.makeText(getActivity(),"Can't choose this time", Toast.LENGTH_SHORT).show();
                    }else {
                        format = new SimpleDateFormat("MMM,dd,yyyy");
                        if(!TextUtils.isEmpty(choosetime)){
                            callback.timeCallBack(format.format(new Date(loadingTime + TimeZone.getDefault().getRawOffset())),1);
                        }
                        dismiss();
                    }
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"Choose Time Error");
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