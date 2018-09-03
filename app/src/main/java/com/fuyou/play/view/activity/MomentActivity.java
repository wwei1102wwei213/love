package com.fuyou.play.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuyou.play.R;
import com.fuyou.play.adapter.MomentListAdapter;
import com.fuyou.play.bean.moment.MomentItem;
import com.fuyou.play.bean.moment.MomentUser;
import com.fuyou.play.biz.Factory;
import com.fuyou.play.biz.http.HttpFlag;
import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.custom.FriendRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-08-16.
 *
 * @author wwei
 */
public class MomentActivity extends BaseActivity implements HttpRepListener{

    private RecyclerView rv;
    private MomentListAdapter adapter;
    private View hv;
    private List<MomentItem> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acotivity_moment);
//        initViews();
        initViews2();
        list = new ArrayList<>();
        Factory.getHttpRespBiz(this, HttpFlag.MOMENT_USER, null).get();
        Factory.getHttpRespBiz(this, HttpFlag.MOMENT_LIST, null).get();
    }

    private void initViews(){
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        adapter = new MomentListAdapter(this, null);
        hv = adapter.setHeaderView(R.layout.layout_moment_head, rv);
        initHeadView();
        rv.setAdapter(adapter);
    }

    private FriendRefreshView frv;
    private void initViews2() {
        frv = findViewById(R.id.frv);
        frv.setOnRefreshListener(new FriendRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*if (list!=null) {
                            frv.update(list, true);
                        }*/
                        frv.stopRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frv.setLoading(false);
                    }
                }, 2000);
            }
        });
    }

    private ImageView iv_background, iv_head;
    private TextView tv_name;
    private void initHeadView() {
        iv_background = hv.findViewById(R.id.iv_background);
        iv_head = hv.findViewById(R.id.iv_head);
        tv_name = hv.findViewById(R.id.tv_name);
    }

    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    @Override
    public byte[] getPostParams(int flag, Object obj) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag, Object obj) {
        if (flag == HttpFlag.MOMENT_USER) {
            try {
                MomentUser user = (MomentUser) response;
                user.setProfile_image("https://fuyouoss.oss-cn-shenzhen.aliyuncs.com/image/small.png");
                /*Glide.with(this).load(user.getProfile_image()).placeholder(R.mipmap.default_error_img).into(iv_background);
                Glide.with(this).load(user.getAvatar()).placeholder(R.mipmap.default_error_img).into(iv_head);
                tv_name.setText(TextUtils.isEmpty(user.getNick())?"":user.getNick());*/
                frv.updateHead(user, this);
                frv.startRefresh();
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e);
            }
        } else if (flag == HttpFlag.MOMENT_LIST) {
            try {
                List<MomentItem> temp = (List<MomentItem>) response;
                if (temp!=null&&temp.size()>0) {
                    list = new ArrayList<>();
                    for (MomentItem item:temp) {
                        if (item.getError()==null&&item.getSender()!=null) {
                            list.add(item);
                        }
                    }
                }
                temp = new ArrayList<>();
                for (int i=0;i<5&&i<list.size();i++) {
                    temp.add(list.get(i));
                }
                frv.update(list);
            } catch (Exception e){
                ExceptionUtils.ExceptionSend(e);
            }
        }
    }

    @Override
    public void showLoading(int flag, Object obj) {

    }

    @Override
    public void hideLoading(int flag, Object obj) {

    }

    @Override
    public void showError(int flag, Object obj, int errorType) {

    }
}
