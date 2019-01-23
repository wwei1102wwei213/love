package com.yyspbfq.filmplay.widget.custom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyspbfq.filmplay.R;


/**
 * 所有异常界面统一处理，比如无网络，无数据等等
 */
public class CommonExceptionView {

    /**
     *数据为空
     */
    public static final int TYPE_DATA_EMTY = 1;
    /**
     * 加载错误，例如没有网络 白天模式
     */
    public static final int TYPE_LOAD_ERROR = 2;
    /**
     * 加载错误，例如没有网络 夜晚模式
     */
    public static final int TYPE_LOAD_ERROR_NIGHT = 3;

    private Context mContext;
    private LayoutInflater mInflater;
    private ViewGroup mParentView;//父类布局
    private ViewGroup mContentView;//异常界面下的View
    private ViewGroup mExceptionView;//异常界面，他会覆盖在mContentView上

    private ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private int mType = TYPE_DATA_EMTY;
    private TextView error_text;
    private Button error_btn_reload;
    private boolean isAdd;
    private ImageView error_image;

    private OnExceptionViewListener listener;
    private OnClickImageViewListener imageListner;

    private LinearLayout root;

    public interface OnExceptionViewListener {
        void reload();
    }

    public interface OnClickImageViewListener {
        void onClick();
    }

    public void setOnExceptionViewListner(OnExceptionViewListener listner) {
        this.listener = listner;
    }

    public void setOnClickImageViewListener(OnClickImageViewListener listner) {
        imageListner = listner;
    }

    /**
     * contentView的父布局必须是RelativeLayout或FrameLayout
     * @param context
     * @param contentView
     */
    public CommonExceptionView(Context context, ViewGroup contentView) {

        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mContentView = contentView;
        this.mParentView = (ViewGroup) contentView.getParent();
        initView();

    }

    public CommonExceptionView(Context context, ViewGroup contentView, boolean isSelf) {

        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mContentView = contentView;
        this.mParentView = contentView;
        initView();

    }

    private void initView() {

        mExceptionView = (ViewGroup) mInflater.inflate(R.layout.custom_error_layout, null);
        root = (LinearLayout) mExceptionView.findViewById(R.id.error_ll);
        error_image = (ImageView) mExceptionView.findViewById(R.id.error_image);
        error_text = (TextView) mExceptionView.findViewById(R.id.error_text);
        error_btn_reload = (Button) mExceptionView.findViewById(R.id.error_btn_reload);
        error_btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)listener.reload();
            }
        });

        error_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageListner != null)imageListner.onClick();
            }
        });

        mExceptionView.setVisibility(View.GONE);
        if (!isAdd) {
            isAdd = true;
            mParentView.addView(mExceptionView, mLayoutParams);
        }

    }

    public ViewGroup getExceptionView() {
        return mExceptionView;
    }

    public ViewGroup getRootView() {
        return mParentView;
    }

    public ViewGroup getContentView() {
        return mContentView;
    }

    /**
     * 显示不同类型异常界面
     * @param type
     */
    public void showByType(int type) {

        switch (type) {

            case TYPE_DATA_EMTY:
                if (mExceptionView != null) {
                    root.setBackgroundColor(Color.parseColor("#ffffff"));
                    error_text.setTextColor(Color.parseColor("#9b9b9b"));
                    error_text.setText("没有数据");
                    error_image.setVisibility(View.GONE);
                    error_btn_reload.setVisibility(View.GONE);
                    mExceptionView.setVisibility(View.VISIBLE);
                }
                break;

            case TYPE_LOAD_ERROR:
                if (mExceptionView != null) {
                    root.setBackgroundColor(Color.parseColor("#ffffff"));
                    error_text.setTextColor(Color.parseColor("#9b9b9b"));
                    error_image.setImageResource(R.mipmap.loading_error);
                    error_btn_reload.setTextColor(mContext.getResources().getColor(R.color.actionbar_default));
                    error_btn_reload.setBackgroundResource(R.drawable.btn_net_error_selector);

                    error_text.setText("网络连接异常，请检查网络设置");
                    error_btn_reload.setVisibility(View.VISIBLE);
                    error_image.setVisibility(View.VISIBLE);
                    mExceptionView.setVisibility(View.VISIBLE);
                }
                break;

            case TYPE_LOAD_ERROR_NIGHT:
                if (mExceptionView != null) {
                    root.setBackgroundColor(Color.parseColor("#121212"));
                    error_text.setTextColor(Color.parseColor("#3d3d3d"));
                    error_image.setImageResource(R.mipmap.loading_error);
                    error_btn_reload.setTextColor(Color.parseColor("#3d3d3d"));
                    error_btn_reload.setBackgroundResource(R.drawable.btn_net_error_selector);
                    error_text.setText("网络连接异常，请检查网络设置");
                    error_btn_reload.setVisibility(View.VISIBLE);
                    error_image.setVisibility(View.VISIBLE);
                    mExceptionView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void dismiss() {
        if(mExceptionView != null && mExceptionView.getVisibility() != View.GONE)
            mExceptionView.setVisibility(View.GONE);
    }

}
