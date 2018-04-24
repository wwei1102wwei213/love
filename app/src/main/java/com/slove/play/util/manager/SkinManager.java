package com.slove.play.util.manager;

import android.content.Context;

import com.slove.play.R;
import com.slove.play.service.BroadcastManager;
import com.slove.play.util.sp.UserDataUtil;

/**
 * Created by Administrator on 2018/4/16 0016.
 *
 * 颜色管理器
 *
 * @author wwei
 */
public class SkinManager {

    private static SkinManager instance;

    public static final int BASE_COLOR = 0;
    public static final int TEXT_COLOR = 1;
    public static final int BUTTON_COLOR = 2;
    private int mBaseColor;
    private int mTextColor;
    private int mButtonColor;

    private int mSkinType;

    private int[] baseColors = {
        R.color.base_color_0,
        R.color.base_color_1,
        R.color.base_color_2,
        R.color.base_color_3,
        R.color.base_color_4,
        R.color.base_color_5,
        R.color.base_color_6,
        R.color.base_color_7,
        R.color.base_color_8,
        R.color.base_color_9
    };

    private SkinManager(Context context) {
        mSkinType = UserDataUtil.getSkinType(context);
        mBaseColor = baseColors[mSkinType];
    }

    //单例
    public static SkinManager getInstance(Context context) {
        if (instance == null) {
            synchronized (BroadcastManager.class) {
                if (instance == null) {
                    instance = new SkinManager(context);
                }
            }
        }
        return instance;
    }

    public int getSkinType(){
        return mSkinType;
    }

    public int getBaseColor(int type){
        int res = mBaseColor;
        switch (type) {
            case TEXT_COLOR:
                res = mTextColor;
                break;
            case BUTTON_COLOR:
                res = mButtonColor;
                break;
        }
        return res;
    }

    public void changeBaseColor(Context context, int type) {
        mSkinType = type;
    }
}
