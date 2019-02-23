package com.fuyou.play.util.HORUtils;

import android.content.Context;

import com.fuyou.play.bean.UserInfo;


/**
 * Created by Administrator on 2017/11/9 0009.
 */

public class SubHelper {

    private SubHelper() {
    }

    private static SubHelper instance = null;

    /***
     * 单例模式，注意要加同步锁
     *
     * @return DBHelper
     */
    public static SubHelper getInstance() {
        if (instance == null) {
            synchronized (SubHelper.class) {
                if (instance == null) {
                    instance = new SubHelper();
                }
            }
        }
        return instance;
    }

    public void clear(){
        mMainData = null;
        mMainUser = null;
        mSubData = null;
        mSubUser = null;
    }

    private  HORChartResult mMainData;
    private  HORChartResult mSubData;
    private UserInfo mMainUser;
    private  UserInfo mSubUser;
    public HORChartResult getSubMainData(Context context){
        if (mMainUser==null){
            if (mMainData==null){
                mMainData = HORDataManger.getInstance().getUserHORData(context);
            }
        } else {
            if (mMainData==null){
                mMainData = HORFunction.getHORChartResult(mMainUser);
            }
        }
        return mMainData;
    }

    public void setSubMainData( HORChartResult result){
        mMainData = result;
    }

    public HORChartResult getSubSubData(Context context){
        return mSubData;
    }

    public void setSubSubData( HORChartResult result){
        mSubData = result;
    }

    public UserInfo getMainUser() {
        return mMainUser;
    }

    public UserInfo getMainUserNoNull(Context context) {
        if (mMainUser==null){
            return HORFunction.copySubForUser(context);
        }
        return mMainUser;
    }

    public void setMainUser(UserInfo mMainUser) {
        this.mMainUser = mMainUser;
        if (mMainUser==null){
            mMainData = null;
        } else {
            mMainData = HORFunction.getHORChartResult(mMainUser);
        }
    }

    public UserInfo getSubUser() {
        return mSubUser;
    }

    public void setSubUser(UserInfo mSubUser) {
        this.mSubUser = mSubUser;
        if (mSubUser==null){
            mSubData = null;
        } else {
            mSubData = HORFunction.getHORChartResult(mSubUser);
        }
    }
}
