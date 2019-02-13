package com.yyspbfq.filmplay.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.wei.wlib.elasticity.ElasticityScrollView;
import com.wei.wlib.http.WLibHttpListener;
import com.wei.wlib.pullrefresh.PullElasticityScrollView;
import com.wei.wlib.pullrefresh.PullToRefreshBase;
import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.adapter.CollectionRvAdapter;
import com.yyspbfq.filmplay.adapter.DownloadListRvAdapter;
import com.yyspbfq.filmplay.adapter.WatchRecordAdapter;
import com.yyspbfq.filmplay.bean.AdvertBean;
import com.yyspbfq.filmplay.bean.DiscoverDataBean;
import com.yyspbfq.filmplay.bean.InfoMessageBean;
import com.yyspbfq.filmplay.bean.InfoMessageEntity;
import com.yyspbfq.filmplay.bean.UserInfo;
import com.yyspbfq.filmplay.bean.UserInfoBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.db.DBHelper;
import com.yyspbfq.filmplay.db.VideoDownloadBean;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.db.VideoRecordBean;
import com.yyspbfq.filmplay.ui.BaseFragment;
import com.yyspbfq.filmplay.ui.activity.DownloadListActivity;
import com.yyspbfq.filmplay.ui.activity.InfoCollationActivity;
import com.yyspbfq.filmplay.ui.activity.InfoInviteActivity;
import com.yyspbfq.filmplay.ui.activity.InfoMessageActivity;
import com.yyspbfq.filmplay.ui.activity.LevelExchangeActivity;
import com.yyspbfq.filmplay.ui.activity.SettingActivity;
import com.yyspbfq.filmplay.ui.activity.VideoHelpActivity;
import com.yyspbfq.filmplay.ui.activity.VideoRecordActivity;
import com.yyspbfq.filmplay.ui.dialog.LoginDialog;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.UiUtils;
import com.yyspbfq.filmplay.utils.sp.UserDataUtil;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;
import com.yyspbfq.filmplay.utils.tools.TimeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 我的
 */
public class MyFragment extends BaseFragment implements View.OnClickListener, WLibHttpListener{

    public static final String TAG = MyFragment.class.getSimpleName();
    private View content;
    private PullElasticityScrollView mPullScrollView;
    private ElasticityScrollView mScrollView;
    private ImageView myHeadImg;

    private TextView myNameTv;
    private TextView myCoinTv;
    private TextView tv_level_or_login;
    private TextView tv_level_next;
    private ImageView iv_icon_level,iv_level_info;
    private View statusBar;
    private View v_red_dot;
    private View tv_no_read_record;

    private TextView tv_download_num;

    private float Height_Top;

    private ImageView iv_adv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.base_fragment_my, container, false);
        initView(rootView);
        initData();
        Factory.resp(this, HttpFlag.FLAG_ADVERT_USER_CENTER, null, AdvertBean.class).post(null);

        return rootView;
    }

    private void initView(View view) {

        statusBar = findViewById(R.id.status_bar);
        Height_Top = DensityUtils.dp2px(context, 100);
        initStatusBar(statusBar);

        content = LayoutInflater.from(context).inflate(R.layout.fragment_my_content, null);
        content.setVisibility(View.VISIBLE);

        mPullScrollView = (PullElasticityScrollView) view.findViewById(R.id.pull_refresh_scroll);
        mScrollView = mPullScrollView.getRefreshableView();
        mScrollView.setVerticalScrollBarEnabled(false);
        mScrollView.addView(content);

        mPullScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ElasticityScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ElasticityScrollView> refreshView) {
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ElasticityScrollView> refreshView) {

            }
        });

        content.findViewById(R.id.my_head_rl).setOnClickListener(this);
        myHeadImg = (ImageView) content.findViewById(R.id.my_head_img);
        iv_icon_level = (ImageView) content.findViewById(R.id.iv_icon_level);
        iv_level_info = (ImageView) content.findViewById(R.id.iv_level_info);
        myNameTv = (TextView) content.findViewById(R.id.my_name_tv);
        tv_level_next = (TextView) content.findViewById(R.id.tv_level_next);
        myCoinTv = (TextView) content.findViewById(R.id.my_coin_tv);
        v_red_dot = content.findViewById(R.id.my_ll_message_img_dot);
        tv_level_or_login = (TextView) content.findViewById(R.id.tv_level_or_login);
        iv_adv = content.findViewById(R.id.iv_adv);

        tv_level_or_login.setOnClickListener(this);
        content.findViewById(R.id.tv_level_exchange).setOnClickListener(this);

        content.findViewById(R.id.v_invite_level).setOnClickListener(this);
        content.findViewById(R.id.v_user_detail).setOnClickListener(this);
        content.findViewById(R.id.v_history_menu).setOnClickListener(this);
        content.findViewById(R.id.v_menu_1).setOnClickListener(this);
        content.findViewById(R.id.v_menu_2).setOnClickListener(this);
        content.findViewById(R.id.v_menu_3).setOnClickListener(this);
        content.findViewById(R.id.v_menu_4).setOnClickListener(this);
        content.findViewById(R.id.v_collect_menu).setOnClickListener(this);
        content.findViewById(R.id.v_download_menu).setOnClickListener(this);
        iv_adv.setOnClickListener(this);
        tv_download_num = content.findViewById(R.id.tv_download_num);

        tv_no_read_record = content.findViewById(R.id.tv_no_read_record);
        rv_history = (RecyclerView) content.findViewById(R.id.rv_history);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_history.setLayoutManager(manager);
        mineAdapter = new WatchRecordAdapter(context, null);
        rv_history.setAdapter(mineAdapter);

        rv_collection = (RecyclerView) content.findViewById(R.id.rv_collection);
        LinearLayoutManager manager1 = new LinearLayoutManager(context);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_collection.setLayoutManager(manager1);
        collectionRAdapter = new CollectionRvAdapter(context, null);
        rv_collection.setAdapter(collectionRAdapter);

        rv_download = (RecyclerView) content.findViewById(R.id.rv_download);
        LinearLayoutManager manager2 = new LinearLayoutManager(context);
        manager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_download.setLayoutManager(manager2);
        downloadAdapter = new DownloadListRvAdapter(context, null);
        rv_download.setAdapter(downloadAdapter);

        mScrollView.setElasticityScrollListener(new ElasticityScrollView.OnScrollListener() {
            @Override
            public void onElasticityScrollChanged(int l, int t, int oldl, int oldt) {
                if (mPullScrollView!=null) {
                    statusBarChangerAlpha(t);
                }
            }
        });
        RotateAnimation a = new RotateAnimation(
                0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        a.setDuration(2400);
        a.setRepeatCount(Animation.INFINITE);
        a.setRepeatMode(Animation.RESTART);
        a.setInterpolator(new LinearInterpolator());
        content.findViewById(R.id.iv_invite).setAnimation(a);
        a.start();

    }

    private void statusBarChangerAlpha(int top){
        try {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
                if (top>0) {
                    float alpha = top/Height_Top;
                    alpha = alpha>1?1:alpha;
                    alpha *= 0.7;
                    statusBar.setAlpha(alpha>1?1:alpha);
                } else {
                    statusBar.setAlpha(0);
                }
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private void initData() {
        Factory.resp(this, HttpFlag.FLAG_USER_INFO, null, UserInfoBean.class).post(null);
        Map<String, String> map = new HashMap<>();
        map.put("page", "0");
        map.put("size", "1");
        Factory.resp(this, HttpFlag.FLAG_MESSAGE_SHOW, null, InfoMessageBean.class).post(map);
    }

    public void getCollections() {
        if (UserDataUtil.isLogin(context)) {
            Map<String, String> map = new HashMap<>();
            map.put("page", "0");
            map.put("size", "10");
            Factory.resp(this, HttpFlag.FLAG_INFO_COLLATION, null, DiscoverDataBean.class).post(map);
        } else {
            if (rv_collection!=null) rv_collection.setVisibility(View.GONE);
        }
    }

    public void setUserView() {
        Logger.e("setUserView");
        try {
            UserInfo userInfo = UserDataUtil.getUserInfo(context);
            myCoinTv.setText(userInfo.canWatch + "/" + userInfo.maxWatch);
            tv_download_num.setText("今日缓存次数："+userInfo.canDown+"/"+userInfo.maxDown+"次");
            String levelName = "";
            String levelSpace = "";
            try {
                UserInfo.UserGrade.GradeItem now = userInfo.grade.now;
                UserInfo.UserGrade.GradeItem next = userInfo.grade.next;
                levelName = "L" + now.level + now.name;
                int nowNum = Integer.parseInt(now.num);
                int nextNum = Integer.parseInt(next.num);
                if (nextNum!=0&&(nextNum==nowNum)) {
                    levelSpace = "您已是最高等级";
                } else {
                    levelSpace = String.format(getString(R.string.mine_invite_level), (nextNum-nowNum)+"");
                }
                if (TextUtils.isEmpty(now.thumb)) {
                    Glide.with(context).load(R.mipmap.ic_level0).into(iv_level_info);
                } else {
                    Glide.with(context).load(now.thumb).into(iv_level_info);
                }
                if (TextUtils.isEmpty(next.thumb)) {
                    Glide.with(context).load(R.mipmap.ic_level0).into(iv_icon_level);
                } else {
                    Glide.with(context).load(next.thumb).into(iv_icon_level);
                }
            } catch (Exception e){
                BLog.e(e);
            }
            if (UserDataUtil.isLogin(context)) {
                myNameTv.setText(userInfo.name);
                tv_level_or_login.setText(levelName);
                tv_level_next.setText(levelSpace);
                Glide.with(context).load(userInfo.avatar).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.default_user_icon).into(myHeadImg);
            } else {
                myNameTv.setText("请登录");
                Glide.with(context).load(R.mipmap.default_user_icon).into(myHeadImg);
                tv_level_or_login.setText("请登录");
                tv_level_next.setText("去推广升级吧");
            }
            mPullScrollView.onPullDownRefreshComplete();
            mPullScrollView.setLastUpdatedLabel(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_DATE_2));

        } catch (Exception e){
            BLog.e(e);
        }
    }

    private RecyclerView rv_history;
    private WatchRecordAdapter mineAdapter;
    private RecyclerView rv_collection;
    private CollectionRvAdapter collectionRAdapter;
    private RecyclerView rv_download;
    private DownloadListRvAdapter downloadAdapter;
    public void setHistoryView(){
        try {
            List<VideoRecordBean> list = DBHelper.getInstance().getVideoRecord(BaseApplication.getInstance(), 10);
            if (list!=null&&list.size()>0) {
                if (mineAdapter!=null) mineAdapter.update(list);
                rv_history.setVisibility(View.VISIBLE);
                if (tv_no_read_record!=null) tv_no_read_record.setVisibility(View.GONE);
            } else {
                rv_history.setVisibility(View.GONE);
                if (tv_no_read_record!=null) tv_no_read_record.setVisibility(View.VISIBLE);
            }
        } catch (Exception e){
            BLog.e(e);
        }
        try {
            List<VideoDownloadBean> list = DBHelper.getInstance().getDownloadRecordCompleted(BaseApplication.getInstance(), 10);
            if (list!=null&&list.size()>0) {
                if (downloadAdapter!=null) downloadAdapter.update(list);
                rv_download.setVisibility(View.VISIBLE);
            } else {
                rv_download.setVisibility(View.GONE);
            }
        } catch (Exception e){
            BLog.e(e);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.v_user_detail:
            case R.id.my_head_rl://编辑个人信息
                if (!"2".equals(UserDataUtil.getLoginType(context))) {
                    new LoginDialog(context).show();
                    return;
                }
                context.startActivity(new Intent(context, SettingActivity.class));
                break;
            case R.id.v_menu_3:
                if (!UserDataUtil.isLogin(context)) {
                    new LoginDialog(context).show();
                    return;
                } else {
                    InfoMessageActivity.actionStart(context);
                    v_red_dot.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_level_or_login:
                if (!UserDataUtil.isLogin(context)) {
                    new LoginDialog(context).show();
                    return;
                } else {
                    context.startActivity(new Intent(context, InfoInviteActivity.class));
                }
                break;
            case R.id.v_invite_level:
            case R.id.v_menu_1:
                context.startActivity(new Intent(context, InfoInviteActivity.class));
                break;
            case R.id.v_menu_2:
                context.startActivity(new Intent(context, VideoHelpActivity.class));
                break;
            case R.id.v_menu_4:
                if (!UserDataUtil.isLogin(context)) {
                    new LoginDialog(context).show();
                    return;
                } else {
                    context.startActivity(new Intent(context, SettingActivity.class));
                }
                break;
            case R.id.tv_level_exchange:
                if (!UserDataUtil.isLogin(context)) {
                    new LoginDialog(context).show();
                    return;
                } else {
                    LevelExchangeActivity.actionStart(context);
                }
                break;
            case R.id.v_history_menu:
                if (!UserDataUtil.isLogin(context)) {
                    new LoginDialog(context).show();
                    return;
                } else {
                    startActivity(new Intent(getActivity(), VideoRecordActivity.class));
                }
                break;
            case R.id.iv_adv:
                UiUtils.handleAdvert(context, mAdvertBean);
                break;
            case R.id.v_collect_menu:
                if (!UserDataUtil.isLogin(context)) {
                    new LoginDialog(context).show();
                    return;
                } else {
                    context.startActivity(new Intent(context, InfoCollationActivity.class));
                }
                break;
            case R.id.v_download_menu:
                context.startActivity(new Intent(context, DownloadListActivity.class));
                break;
        }
    }


    private AdvertBean mAdvertBean;
    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag == HttpFlag.FLAG_USER_INFO) {
            try {
                UserInfoBean bean = (UserInfoBean) formatData;
                UserDataUtil.saveLoginType(context, bean.getType()+"");
                UserDataUtil.saveUserData(context, bean.getData());
                setUserView();
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_ADVERT_USER_CENTER) {
            try {
                mAdvertBean = (AdvertBean) formatData;
                if (mAdvertBean.getThumb()!=null) {
                    iv_adv.setVisibility(View.VISIBLE);
                    Glide.with(context).load(mAdvertBean.getThumb()+"").crossFade().into(iv_adv);
                } else {
                    iv_adv.setVisibility(View.GONE);
                }
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_MESSAGE_SHOW) {
            try {
                InfoMessageBean bean = (InfoMessageBean) formatData;
                List<InfoMessageEntity> list = bean.getData();
                if (list!=null&&list.size()>0) {
                    String newLastTime = list.get(0).getCtime();
                    boolean isRed = UiUtils.handleMessageRedPoint(newLastTime, UserDataUtil.getMessageLastTime(context));
                    if (isRed) {
                        v_red_dot.setVisibility(View.VISIBLE);
                    }
                }
            } catch (Exception e){
                BLog.e(e);
            }
        } else if (flag == HttpFlag.FLAG_INFO_COLLATION) {
            try {
                DiscoverDataBean bean = (DiscoverDataBean) formatData;
                List<VideoEntity> list = bean.getData();
                if (list!=null&&list.size()>0) {
                    collectionRAdapter.update(list);
                    rv_collection.setVisibility(View.VISIBLE);
                } else {
                    rv_collection.setVisibility(View.GONE);
                }
            } catch (Exception e){
                BLog.e(e);
            }
        }
    }

    @Override
    public void handleLoading(int flag, Object tag, boolean isShow) {

    }

    @Override
    public void handleError(int flag, Object tag, int errorType, String response, String hint) {

    }

    @Override
    public void handleAfter(int flag, Object tag) {

    }

    @Override
    public void onResume() {
        super.onResume();
//        Logger.e("MyFragment onResume");
        setHistoryView();
        getCollections();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        Logger.e("MyFragment onHiddenChanged:"+hidden);
        if (!hidden) {
            getCollections();
            setHistoryView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
