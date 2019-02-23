package com.yyspbfq.filmplay.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.wei.wlib.http.WLibHttpListener;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.InviteCodeBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.QRCodeUtil;
import com.yyspbfq.filmplay.utils.SystemUtils;
import com.yyspbfq.filmplay.utils.sp.UserDataUtil;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;
import com.yyspbfq.filmplay.utils.tools.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class InviteCodeActivity extends BaseActivity implements WLibHttpListener{

    private String url = "";

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, InviteCodeActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        initStatusBar();
        ((TextView) findViewById(R.id.tv_base_title)).setText("推广");
        setBackViews(R.id.iv_base_back);
        initViews();
        initData();
    }

    private TextView tv_index;
    private ImageView iv_bg, iv_code;
    private String textCopy;
    private void initViews() {
        try {
            TextView tv_code = (TextView) findViewById(R.id.tv_code);
            tv_code.setText(UserDataUtil.getUserInfo(this).getShareCodeID().toUpperCase());
            tv_index = (TextView) findViewById(R.id.tv_index);
            iv_bg = (ImageView) findViewById(R.id.iv_bg);
            iv_code = findViewById(R.id.iv_qcode);
            findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toSave();
                }
            });
            findViewById(R.id.tv_copy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toCopy();
                }
            });
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void initData() {
        Factory.resp(this, HttpFlag.FLAG_INVITE_CODE_MSG, null, InviteCodeBean.class).post(null);
    }

    private void toSave() {
        Factory.resp(this, HttpFlag.FLAG_INVITE_SAVE_CODE, null, null).post(null);
    }

    private void toCopy() {
        if (TextUtils.isEmpty(textCopy)) return;
        SystemUtils.copyTextToClip(this, textCopy);
        ToastUtils.showToast("你的邀请码复制成功,去分享吧！");
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag == HttpFlag.FLAG_INVITE_CODE_MSG) {
            try {
                InviteCodeBean bean = (InviteCodeBean) formatData;
                tv_index.setText("官网：" + (bean.getOfficialWebsite()==null?"":bean.getOfficialWebsite()));
                Glide.with(this).load(bean.getExtendBackGroup()).crossFade().into(iv_bg);
                url = bean.getUrl();
                int width = DensityUtils.dp2px(this, 200);
                //生成用户邀请二维码
                iv_code.setImageBitmap(QRCodeUtil.createQRCode(url, width));
                textCopy = bean.getExtension()==null?url:bean.getExtension();
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_INVITE_SAVE_CODE) {
            try {
                viewSaveToImage(findViewById(R.id.v_body));
                ToastUtils.showToast("保存成功");
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }

    @Override
    public void handleLoading(int flag, Object tag, boolean isShow) {
        if (isShow&&flag==HttpFlag.FLAG_INVITE_SAVE_CODE) {
            saveHandle(false);
        }
    }

    @Override
    public void handleError(int flag, Object tag, int errorType, String response, String hint) {

    }

    @Override
    public void handleAfter(int flag, Object tag) {
        if (flag==HttpFlag.FLAG_INVITE_SAVE_CODE) {
            saveHandle(true);
        }
    }

    private String tempSave = "";
    private void saveHandle(boolean isAfter) {
        if (isAfter) {
            findViewById(R.id.tv_summary).setVisibility(View.GONE);
            findViewById(R.id.v_btns).setVisibility(View.VISIBLE);
            tv_index.setText(tempSave);
            findViewById(R.id.iv_base_back).setVisibility(View.VISIBLE);
        } else {
            tempSave = tv_index.getText().toString();
            tv_index.setText("扫描上面二维码, 下载"+getString(R.string.app_name)+"\n"+tempSave);
            findViewById(R.id.v_btns).setVisibility(View.GONE);
            findViewById(R.id.tv_summary).setVisibility(View.VISIBLE);
            findViewById(R.id.iv_base_back).setVisibility(View.INVISIBLE);
        }
    }

    private void viewSaveToImage(View view) {
        try {
            view.setDrawingCacheEnabled(true);
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            view.setDrawingCacheBackgroundColor(Color.WHITE);
            // 把一个View转换成图片
            Bitmap cachebmp = loadBitmapFromView(view);
            FileOutputStream fos;
            String imagePath = "";
            try {
                // 判断手机设备是否有SD卡
                boolean isHasSDCard = Environment.getExternalStorageState().equals(
                        android.os.Environment.MEDIA_MOUNTED);
                if (isHasSDCard) {
                    // SD卡根目录
                    File sdRoot = Environment.getExternalStorageDirectory();
                    File file = new File(sdRoot, Calendar.getInstance().getTimeInMillis()+".png");
                    fos = new FileOutputStream(file);
                    imagePath = file.getAbsolutePath();
                } else
                    throw new Exception("创建文件失败!");
                cachebmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Logger.e("imagePath="+imagePath);
            view.destroyDrawingCache();
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色,则生成透明 */
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }
}
