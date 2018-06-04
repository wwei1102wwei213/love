package com.fuyou.play.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created by codeest on 2016/8/2.
 */
public class ImageLoader {
    public static void load(Activity activity, String url, ImageView iv, int placeholder) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!activity.isDestroyed()) {
                Glide.with(activity)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(placeholder)
                        .error(placeholder)
                        .into(iv);
            }
        } else {
            Glide.with(activity)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(placeholder)
                    .error(placeholder)
                    .into(iv);
        }
    }

    public static void load(Activity activity, String url, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!activity.isDestroyed()) {
                Glide.with(activity).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
            }
        } else {
            Glide.with(activity).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
        }
    }

    public static void load(Context context, String url, ImageView iv) {
//        if (context == null || ((Activity) context).isFinishing()) {
//            return;
//        }
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
    }

    public static void loadALL(Context context, String url, ImageView iv, int resourceId, int erroResouceId) {//http://blog.csdn.net/nzw31/article/details/54092889
        try {
            if (context == null || ((Activity) context).isFinishing()) {
                return;
            }
            Glide.with(context)
                    .load(url)
                    .dontAnimate()
                    .placeholder(resourceId) // 占位符，就是图片从开始请求到最后完全加载，这段时间显示的默认图片。
                    .error(erroResouceId) //请求图片发生异常的错误图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadAll(Context context, String url, ImageView iv) {
        if (context == null || ((Activity) context).isFinishing()) {
            return;
        }
        Glide.with(context).load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
    }

    public static void loadAll(Context context, String url, ImageView iv, int placeholder) {
        if (context == null || ((Activity) context).isFinishing()) {
            return;
        }
        Glide.with(context).load(url).placeholder(placeholder).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
    }

    public static void loadAll(Activity activity, String url, ImageView iv) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!activity.isDestroyed()) {
                Glide.with(activity).load(url).crossFade().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
            }
        } else {
            Glide.with(activity).load(url).crossFade().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
        }
    }

    public static Bitmap getBitmap(Activity activity, String url, ImageView iv) {    //不缓存，全部从网络加载
        final Bitmap[] bitmap = {null};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!activity.isDestroyed()) {
                Glide.with(activity).load(url).asBitmap().into(new BitmapImageViewTarget(iv) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        bitmap[0] = resource;
                    }
                });
            }
        } else {
            Glide.with(activity).load(url).asBitmap().into(new BitmapImageViewTarget(iv) {
                @Override
                protected void setResource(Bitmap resource) {
                    super.setResource(resource);
                    bitmap[0] = resource;
                }
            });
        }
        return bitmap[0];
    }
}
