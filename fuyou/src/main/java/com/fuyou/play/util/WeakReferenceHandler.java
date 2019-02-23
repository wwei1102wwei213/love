package com.fuyou.play.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 弱引用工具类
 * <p>
 * 使用匿名内部类Handler有可能会导致内存泄漏， 凡是使用匿名内部类Handler的地方， 都尽量使用WeakReferenceHandler
 * 不适用于高强度请求环境
 */
public class WeakReferenceHandler extends Handler {
    private WeakReference<Callback> mWeakReferenceCallBack;

    public WeakReferenceHandler(Callback cb) {
        super();
        mWeakReferenceCallBack = new WeakReference<Callback>(cb);
    }

    public WeakReferenceHandler(Looper looper, Callback cb) {
        super(looper);
        mWeakReferenceCallBack = new WeakReference<Callback>(cb);
    }

    @Override
    public void handleMessage(Message msg) {
        Callback cb = mWeakReferenceCallBack.get();
        if (null != cb) {
            cb.handleMessage(msg);
        }
    }

    @Override
    public String toString() {
        Callback cb = mWeakReferenceCallBack.get();
        return super.toString() + " " + cb;
    }
}
