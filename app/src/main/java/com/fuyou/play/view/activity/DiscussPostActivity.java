package com.fuyou.play.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.fuyou.play.R;
import com.fuyou.play.adapter.GridAdapter;
import com.fuyou.play.bean.BaseBean;
import com.fuyou.play.biz.Factory;
import com.fuyou.play.biz.RequestPermissionsBiz;
import com.fuyou.play.biz.http.HttpFlag;
import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.biz.oss.CDNHelper;
import com.fuyou.play.util.Const;
import com.fuyou.play.util.DecryptUtils;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.LogCustom;
import com.fuyou.play.util.PhotoUtils;
import com.fuyou.play.util.sp.UserDataUtil;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.MyLayout;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2018-07-25.
 *
 * @author wwei
 */
public class DiscussPostActivity extends BaseActivity implements HttpRepListener{

    //监听输入法控件
    private MyLayout mLayout;
    //标题输入框
    private EditText et_title;
    //内容输入框
    private EditText et_content;
    //图片选择区
    private GridView gv;
    //发布按钮
    private TextView tv_right_base;
    //进度框
    private ProgressDialog mProgressDialog;
    private List<String> selectedPicture;//containts  photo which is choosed path
    private GridAdapter adapter;
    private RequestPermissionsBiz permissionsBiz;
    private static final int REQUEST_PICK = 0;//选取图片的请求码
    private int selectedIndex = 0;
    private final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private final int PERMISSION_SELECT_PICTURE = 11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss_post);
        initStatusBar(findViewById(R.id.status_bar));
        initViews();
    }

    private void initViews(){
        mLayout = findViewById(R.id.my_layout);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        gv = findViewById(R.id.gv);
        tv_right_base = findViewById(R.id.tv_right_base);
        ((TextView) findViewById(R.id.tv_title_base)).setText(R.string.discuss_post_title);
        setBackViews(R.id.iv_back_base);
        tv_right_base.setText(R.string.discuss_post_txt);
        tv_right_base.setVisibility(View.VISIBLE);
        tv_right_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPost();
            }
        });

        mLayout.setOnSoftKeyboardListener(new MyLayout.OnSoftKeyboardListener() {
            @Override
            public void onShown() {

            }

            @Override
            public void onHidden() {

            }
        });
        selectedPicture = new ArrayList<>();
        adapter = new GridAdapter(this, selectedPicture);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position==adapter.getCount()-1) {
                    checkPermissions();
                } else {
                    startActivity(new Intent(DiscussPostActivity.this, ShowPhotoActivity.class)
                            .putExtra("list_show_photo", (Serializable) (adapter.getList())).putExtra("position", position));
                }
            }
        });

        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
    }

    //检查发布内容
    private void checkPost(){
        if (TextUtils.isEmpty(et_title.getText().toString().trim())){
            showToast(getString(R.string.hint_discuss_title));
            return;
        }
        if (TextUtils.isEmpty(et_content.getText().toString().trim())){
            showToast(getString(R.string.hint_discuss_content));
            return;
        }
        toPost();
    }

    //发布内容
    private void toPost(){
        tv_right_base.setClickable(false);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.discuss_post_loading));
        mProgressDialog.show();
        if (selectedPicture != null && selectedPicture.size() > 0) {//说明是有图片的
            uploadPhoto();
        } else {
            postData();
        }
    }

    private void postData(){
        Factory.getHttpRespBiz(this, HttpFlag.DISCUSS_ADD, null).post();
    }

    //图片url集合
    private List<String> urls;
    private void uploadPhoto() {
        try {
            urls = new ArrayList<>();
            final int index = selectedPicture.size();
            for (int i = 0; i < selectedPicture.size(); i++) {
                final CDNHelper get = new CDNHelper(this);
                try {
                    String imageName = getImgName(this, false);
                    final File big = PhotoUtils.scal(selectedPicture.get(i), PhotoUtils.SCAL_IMAGE_100);
                    get.uploadFile(big.getPath(),imageName, new SaveCallback() {
                        @Override
                        public void onProgress(String s, int i, int i1) {}
                        @Override
                        public void onFailure(String s, OSSException e) {}
                        @Override
                        public void onSuccess(String s) {
                            urls.add(get.getResourseURL());
                            try {
                                big.delete();
                            }catch (Exception e){
                                ExceptionUtils.ExceptionSend(e,"删除临时图片（大）失败");
                            }
                            if (urls.size() == index) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        postData();
                                    }
                                });
                            }
                        }
                    });

                    final CDNHelper gets = new CDNHelper(this);
                    final File small = PhotoUtils.scal(selectedPicture.get(i), PhotoUtils.SCAL_IMAGE_30);
                    gets.uploadFile(small.getPath(),insertThumb(imageName), new SaveCallback() {
                        @Override
                        public void onSuccess(String s) {
                            try {
                                small.delete();
                            }catch (Exception e){
                                ExceptionUtils.ExceptionSend(e,"删除临时图片（小）失败");
                            }
                        }
                        @Override
                        public void onProgress(String s, int i, int i1) {}
                        @Override
                        public void onFailure(String s, OSSException e) {}
                    });

                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "ReleaseForumActivity");
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "ReleaseForumActivity");
        }
    }

    private int index = 0;

    /**
     * 上传图片的命名规则
     *
     * @param context
     * @param isAvatar true 为头像上传 false 为其他类型图片上传
     * @return
     */
    protected String getImgName(Context context, boolean isAvatar) {
        StringBuilder builder = new StringBuilder();
        builder.append(UserDataUtil.getUserID(context));//infoid
        builder.append("post");
        builder.append(System.currentTimeMillis());//当前时间戳 微秒
        String random = getRandom();
        builder.append(random);//随机码（0~999）
        builder.append(index++);
        builder.append("2");//标记码  android 为2 ios为1
        if (isAvatar) {
            return builder.toString() + ".png";
        } else {
            return builder.toString() + ".png";
        }
    }

    /**
     * 小图的url
     */
    private static final String THUMB = "thumb";

    protected String insertThumb(String imageName) {
        StringBuilder sb = new StringBuilder(imageName);
        int index = sb.indexOf(".");
        return sb.insert(index, THUMB).toString();
    }

    /**
     * 生成一个1到10^6的随机数
     *
     * @return
     */
    protected String getRandom() {
        Random random = new Random();
        int i = random.nextInt(1000000);
        if (i / 100 > 0) {
            return i + "";
        } else if (i / 10 > 0) {
            return "0" + i;
        } else {
            return "00" + i;
        }
    }

    private void checkPermissions() {
        if (permissionsBiz==null) {
            permissionsBiz = new RequestPermissionsBiz(this, permissions, new RequestPermissionsBiz.RequestPermissionsListener() {
                @Override
                public void RequestComplete(boolean isOk) {
                    if (isOk) {
                        selectPicture();
                    }
                }
            }, PERMISSION_SELECT_PICTURE);
        }
        permissionsBiz.toCheckPermission();
    }

    private void selectPicture() {
        int index = adapter.getCount() - 1;
        if (index >= 10) {
            showToast("最多上传9张图片");
            return;
        }
        selectedIndex = index;
        Intent intent = new Intent(this, SelectPictureActivity.class);
        index = 9 - index;
        intent.putExtra(Const.INTENT_MAX_NUM, index);
        startActivityForResult(
                intent, REQUEST_PICK);
    }

    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, String> map = new HashMap<>();
        if (flag == HttpFlag.DISCUSS_ADD) {
            map.put("InfoID", UserDataUtil.getUserID(this));
            map.put("Token", UserDataUtil.getAccessToken(this));
            map.put("title", DecryptUtils.encode(et_title.getText().toString().trim()));
            map.put("content", DecryptUtils.encode(et_content.getText().toString().trim()));
            map.put("type", "0");
            map.put("label", "0");
            map.put("labelName", "");
            if (urls!=null&&urls.size()>0) {
                String images = urls.toString();
                images = images.substring(1, images.length()-1);
                map.put("images", images.replaceAll(" ", ""));
            }
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag, Object obj) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag, Object obj) {
        if (flag == HttpFlag.DISCUSS_ADD) {
            BaseBean bean = (BaseBean) response;
            if (bean.getStatus()==0) {
                showToast(getString(R.string.discuss_post_success));
                finish();
            } else {
                showToast(getString(R.string.discuss_post_fail));
            }
        }
    }

    @Override
    public void showLoading(int flag, Object obj) {

    }

    @Override
    public void hideLoading(int flag, Object obj) {
        if (flag == HttpFlag.DISCUSS_ADD ){
            if (mProgressDialog!=null) mProgressDialog.dismiss();
            tv_right_base.setClickable(true);
        }
    }

    @Override
    public void showError(int flag, Object obj, int errorType) {
        if (flag == HttpFlag.DISCUSS_ADD) {
            showToast(getString(R.string.discuss_post_fail));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_PICK://选择图片的结果
                    if (resultCode == RESULT_OK) {
                        List<String> list = (ArrayList<String>) data
                                .getSerializableExtra(Const.INTENT_SELECTED_PICTURE);
                        LogCustom.show("选择的图片uri是：" + list.toString());
                        selectedPicture.addAll(list);
                        adapter.updateDate(selectedPicture);
                    }
                    break;
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsBiz!=null)
        permissionsBiz.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
