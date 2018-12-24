package com.doojaa.base;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * Activity进出栈管理
 */

public class AppManager {

    /**
     * activity栈
     */
    private static Stack<Activity> activityStack;

    private static AppManager mAppManager;

    public static AppManager getInstance() {
        if (mAppManager == null)
            mAppManager = new AppManager();
        return mAppManager;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除Activity到堆栈
     */
    public void removeActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.remove(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null&&activityStack!=null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack==null) return;
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack==null) return;
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {

        try {
            finishAllActivity();
            //如果开发者调用Process.kill或者System.exit之类的方法杀死进程
            //请务必在此之前调用MobclickAgent.onKillProcess(Context context)方法，用来保存统计数据。
//            MobclickAgent.onKillProcess(BaseApplication.getInstance());
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
        } catch (Exception e) {

        }
    }

}
