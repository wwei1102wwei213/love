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
import com.yyspbfq.filmplay.adapter.WatchRecordAdapter;
import com.yyspbfq.filmplay.bean.UserInfo;
import com.yyspbfq.filmplay.bean.UserInfoBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.db.DBHelper;
import com.yyspbfq.filmplay.db.VideoRecordBean;
import com.yyspbfq.filmplay.ui.BaseFragment;
import com.yyspbfq.filmplay.ui.activity.InfoInviteActivity;
import com.yyspbfq.filmplay.ui.dialog.LoginDialog;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.sp.UserDataUtil;
import com.yyspbfq.filmplay.utils.tools.DensityUtils;
import com.yyspbfq.filmplay.utils.tools.TimeUtils;

import java.util.List;


/**
 * 我的
 */
public class MyFragment extends BaseFragment implements View.OnClickListener, WLibHttpListener{

    public static final String TAG = MyFragment.class.getSimpleName();
    private boolean isPull = false;
    private View content;
    private PullElasticityScrollView mPullScrollView;
    private ElasticityScrollView mScrollView;
    private ImageView myHeadImg;

    private TextView myNameTv;
    //    private TextView myIdTv;
    private TextView myCoinTv;
    //    private TextView mNotUserTv;
    private TextView tv_level_or_login;
    private TextView tv_level_next;
    private ImageView iv_icon_level,iv_level_info;
    private View statusBar;
//    private View v_history_read;
    private View iv_invite;
    private View v_red_dot;
    private View tv_no_read_record;

    private TextView tv_download_num;

    private float Height_Top;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.base_fragment_my, container, false);
        initView(rootView);
        initData();
        return rootView;
    }


    private void initView(View view) {

        statusBar = findViewById(R.id.status_bar);
        Height_Top = DensityUtils.dp2px(context, 100);
        initStatusBar(findViewById(R.id.status_bar));

        content = LayoutInflater.from(context).inflate(R.layout.fragment_my_content, null);
        content.setVisibility(View.VISIBLE);

        mPullScrollView = (PullElasticityScrollView) view.findViewById(R.id.pull_refresh_scroll);
        mScrollView = mPullScrollView.getRefreshableView();
        mScrollView.setVerticalScrollBarEnabled(false);
        mScrollView.addView(content);

        mPullScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ElasticityScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ElasticityScrollView> refreshView) {
                isPull = true;
//                UserHelper.getInstance().getUserInfo(context, false, false, isPull);//会自带刷新我的页面
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
//        v_history_read = content.findViewById(R.id.v_history_read);
//        v_red_dot = content.findViewById(R.id.my_ll_message_img_dot);
//        boolean hasRed = PreferencesUtils.getBoolean(mContext, Constant.MY_MESSAGE_HAS_RED, false);
//        v_red_dot.setVisibility(hasRed?View.VISIBLE:View.GONE);
        tv_level_or_login = (TextView) content.findViewById(R.id.tv_level_or_login);
        tv_level_or_login.setOnClickListener(this);
        content.findViewById(R.id.tv_level_exchange).setOnClickListener(this);

        content.findViewById(R.id.v_invite_level).setOnClickListener(this);
        content.findViewById(R.id.v_user_detail).setOnClickListener(this);
        content.findViewById(R.id.v_history_menu).setOnClickListener(this);
        content.findViewById(R.id.v_menu_1).setOnClickListener(this);
        content.findViewById(R.id.v_menu_2).setOnClickListener(this);
        content.findViewById(R.id.v_menu_3).setOnClickListener(this);
        content.findViewById(R.id.v_menu_4).setOnClickListener(this);

        tv_download_num = content.findViewById(R.id.tv_download_num);

        tv_no_read_record = content.findViewById(R.id.tv_no_read_record);
        rv_history = (RecyclerView) content.findViewById(R.id.rv_history);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_history.setLayoutManager(manager);
        mineAdapter = new WatchRecordAdapter(context, null);
        rv_history.setAdapter(mineAdapter);

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
                        .error(R.mipmap.default_error_img).into(myHeadImg);
            } else {
                myNameTv.setText("请登录");
                Glide.with(context).load(R.mipmap.default_error_img).into(myHeadImg);
                tv_level_or_login.setText("请登录");
                tv_level_next.setText("去推广升级吧");
            }
            mPullScrollView.onPullDownRefreshComplete();
            mPullScrollView.setLastUpdatedLabel(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_DATE_2));
            setHistoryView();
        } catch (Exception e){
            BLog.e(e);
        }
    }

    private RecyclerView rv_history;
    private WatchRecordAdapter mineAdapter;
    public void setHistoryView(){
        try {
            List<VideoRecordBean> list = DBHelper.getInstance().getVideoRecord(BaseApplication.getInstance(), 10);
            if (list!=null&&list.size()>0) {
                if (mineAdapter!=null) mineAdapter.update(list);
                if (tv_no_read_record!=null) tv_no_read_record.setVisibility(View.GONE);
            } else {
                if (tv_no_read_record!=null) tv_no_read_record.setVisibility(View.VISIBLE);
            }
        } catch (Exception e){

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
//                UserInfoActivity.actionStart(mContext);
                break;
            case R.id.v_menu_3:
                if ("2".equals(UserDataUtil.getLoginType(context))) {
                    /*boolean hasRed = PreferencesUtils.getBoolean(mContext, Constant.MY_MESSAGE_HAS_RED, false);
                    if (hasRed) {
                        UserMessageActivity.actionStart(mContext, true);
                    } else {
                        UserMessageActivity.actionStart(mContext);
                    }*/
//                    v_red_dot.setVisibility(View.VISIBLE);
                    //点击后隐藏小红点
//                    PreferencesUtils.putBoolean(mContext, Constant.MY_MESSAGE_HAS_RED, false);
                } else {
                    new LoginDialog(context).show();
                }
                break;
            case R.id.tv_level_or_login:
            case R.id.v_invite_level:
            case R.id.v_menu_1:
                if (!"2".equals(UserDataUtil.getLoginType(context))) {
                    new LoginDialog(context).show();
                    return;
                }
                context.startActivity(new Intent(context, InfoInviteActivity.class));
                break;
            case R.id.v_menu_2:
//                FeedBackActivity.actionStart(mContext);
                break;
            case R.id.v_menu_4:
//                SettingActivity.actionStart(mContext);
                break;
            case R.id.tv_level_exchange:
                if ("2".equals(UserDataUtil.getLoginType(context))) {
//                    RedeemCodeActivity.actionStart(mContext);
                } else {
                    new LoginDialog(context).show();
                }
                break;
            case R.id.v_history_menu:
                if ("2".equals(UserDataUtil.getLoginType(context))) {
//                    RedeemCodeActivity.actionStart(mContext);
//                    startActivity(new Intent(getActivity(), HistoryReadActivity.class));
                } else {
                    new LoginDialog(context).show();
                }
                break;
        }
    }

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
        setHistoryView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
