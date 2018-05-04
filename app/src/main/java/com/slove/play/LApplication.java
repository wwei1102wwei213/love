package com.slove.play;

import android.app.Application;

import com.slove.play.util.sp.UserDataUtil;

/**
 * Created by Administrator on 2018/3/30 0030.
 *
 * @author wwei
 */
public class LApplication extends Application{


    private static LApplication instance;
    private String INFO_TOKEN;
    @Override
    public void onCreate() {
        super.onCreate();
        //Application对象
        instance = this;
        /*UMConfigure.setLogEnabled(true);
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        UMConfigure.init(this, "59892f08310c9307b60023d0", "Umeng", UMConfigure.DEVICE_TYPE_PHONE,
                "669c30a9584623e70e8cd01b0381dcb4");*/
    }

    public static synchronized LApplication getInstance() {
        return instance;
    }

    public String getInfoToken(){
        if (INFO_TOKEN==null){
            INFO_TOKEN = UserDataUtil.getAccessToken(this);
        }
        return INFO_TOKEN;
    }

    public void setInfoToken(String token){
        INFO_TOKEN = token;
    }


}
