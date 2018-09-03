package com.fuyou.play.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fuyou.play.bean.BaseBean;
import com.fuyou.play.bean.ErrorBean;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.manager.SkinManager;


/**
 * Created by Administrator on yd
 *
 * App Fragment父类
 *
 * @author wwei
 */
public class BaseFragment extends Fragment {

    protected Context context;
    protected View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    public View getRootView() {
        return rootView;
    }

    protected View findViewById(int id) {
        return rootView.findViewById(id);
    }

    protected void showToast(String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
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
            ExceptionUtils.ExceptionSend(e, "initStatusBar BaseZYFragment");
        }
    }

    //获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        try {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "获取状态栏高度失败");
        }
        return result;
    }

    protected void startActivity(Class<? extends Activity> activityClass) {
        startActivity(new Intent(getActivity(), activityClass));
    }

    //错误提示方法
    protected void showBaseError(BaseBean bean){
        if (bean==null) return;
        try {
            String msg=bean.getMsg();
            ErrorBean error= bean.getError();
            if (msg!=null&&!TextUtils.isEmpty(msg)){
                showToast(msg);
            }else if (error!=null&&!TextUtils.isEmpty(error.getMessage())){
                showToast(error.getMessage());
            }
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private int mSkinType = -1;
    protected void skinSetting(){
        if (mSkinType==-1 || mSkinType!= SkinManager.getInstance(context).getSkinType()) {
            mSkinType = SkinManager.getInstance(context).getSkinType();
            setSkinView();
        }
    }

    //设置皮肤相关UI
    protected void setSkinView(){}
}
