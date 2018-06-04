package com.fuyou.play.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.fuyou.play.R;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.manager.SkinManager;


/**
 * Created by Administrator on 2017/8/29 0029.
 *
 * App Activity父类
 *
 * @author wwei
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void showToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    protected void startActivity(Class<? extends Activity> activityClass) {
        startActivity(new Intent(this, activityClass));
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

    //设置状态栏为透明
    public void setStatusBarTranslucent(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    //设置全屏样式
    public void setFullScreen(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //设置状态栏
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

    //退出动画设置
    protected void finishForAnim(){
        finish();
        overridePendingTransition(R.anim.activity_translate_up,R.anim.activity_out_to_left);
    }

    //退出动画设置 @enterAnim 下个页面的开始动画 @exitAnim 这个页面的退出动画
    protected void finishForAnim(int enterAnim, int exitAnim){
        finish();
        overridePendingTransition(enterAnim,exitAnim);
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this); // 基础指标统计，不能遗漏
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this); // 基础指标统计，不能遗漏
    }

    private int mSkinType = -1;
    protected void skinSetting(){
        if (mSkinType==-1 || mSkinType!= SkinManager.getInstance(this).getSkinType()) {
            mSkinType = SkinManager.getInstance(this).getSkinType();
            setSkinView();
        }
    }

    //设置皮肤相关UI
    protected void setSkinView(){}




    protected boolean mBaseExit = false;
    @Override
    protected void onDestroy() {
        mBaseExit = true;
        super.onDestroy();
    }

}
