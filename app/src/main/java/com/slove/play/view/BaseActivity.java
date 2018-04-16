package com.slove.play.view;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.slove.play.util.ExceptionUtils;

/**
 * Created by Administrator on 2017/8/29 0029.
 *
 * App Activity父类
 *
 * @author wwei
 */
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.broadcast.kick");

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void showToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics());
        return res;
    }

    /*protected void setTitleAndBack(String title){
        initStatusBar(findViewById(R.id.status_bar));
        ((TextView) findViewById(R.id.title)).setText(title);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }*/

    /**
     * 设置状态栏为透明
     */
    public void setStatusBarTranslucent(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置状态栏为透明
     */
    public void setFullScreen(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void setBackViews(int... ids) {
        if (ids != null) {
            for (int id : ids) {
                View view = findViewById(id);
                if (view != null) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
            }
        }
    }



    /**
     * 获取状态栏高度
     * @return 状态栏高度
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * @公用方法抽取
     * 设置状态栏
     */
    public void initStatusBar(View v){
        try {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
                ViewGroup.LayoutParams params=v.getLayoutParams();
                params.height=getStatusBarHeight();
                v.setLayoutParams(params);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initStatusBar");
        }
    }

    /**
     * 退出动画设置
     */
    protected void finishForAnim(){
        finish();
//        overridePendingTransition(R.anim.activity_in_from_right,R.anim.activity_out_to_left);
    }

    protected boolean exit_mBase = false;



    protected void showTJLoading(){

    }

    protected void cancelTJLoading(){

    }

    protected void WatchVideoComplete(){

    }

    @Override
    protected void onDestroy() {
        exit_mBase = true;
        //解除广播
        super.onDestroy();
    }

    /**
     * 退出动画设置
     * @param enterAnim 下个页面的开始动画
     * @param exitAnim 这个页面的退出动画
     */
    protected void finishForAnim(int enterAnim, int exitAnim){
        finish();
        overridePendingTransition(enterAnim,exitAnim);
    }

    protected void addActivityInList(){

    }

    protected void startActivity(Class<? extends Activity> activityClass) {
        startActivity(new Intent(this, activityClass));
    }


}
