package com.doojaa.base.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doojaa.base.R;
import com.doojaa.base.ui.BaseFragment;
import com.doojaa.base.utils.BLog;
import com.doojaa.base.utils.tools.DensityUtils;
import com.doojaa.base.widget.pullwidget.elasticity.ElasticityHelper;
import com.doojaa.base.widget.pullwidget.pullrefresh.ElasticityScrollView;
import com.doojaa.base.widget.pullwidget.pullrefresh.PullElasticityScrollView;
import com.doojaa.base.widget.pullwidget.pullrefresh.PullToRefreshBase;


/**
 * 我的
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = MyFragment.class.getSimpleName();
    private boolean isPull = false;
    private View content;
    private PullElasticityScrollView mPullScrollView;
    private ElasticityScrollView mScrollView;
    private ImageView myHeadImg;
//    private ImageView myHeadSexImg;
    private TextView myNameTv;
    //    private TextView myIdTv;
    private TextView myCoinTv;
    //    private TextView mNotUserTv;
    private TextView tv_level_or_login;
    private TextView tv_level_next;
    private ImageView iv_icon_level,iv_level_info;
    private View statusBar;
    private View v_history_read;

    private float Height_Top;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.base_fragment_my, container, false);
        initView();
        return rootView;
    }

    private void initView() {

        statusBar = findViewById(R.id.status_bar);
        initStatusBar(statusBar);
        Height_Top = DensityUtils.dp2px(context, 80);

        content = LayoutInflater.from(context).inflate(R.layout.fragment_my_content, null);
        content.setVisibility(View.VISIBLE);

        mPullScrollView = (PullElasticityScrollView) findViewById(R.id.pull_refresh_scroll);
        mScrollView = mPullScrollView.getRefreshableView();
        mScrollView.setVerticalScrollBarEnabled(false);
        mScrollView.addView(content);
//        mPullScrollView.setPullRefreshEnabled(false);
        mPullScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ElasticityScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ElasticityScrollView> refreshView) {
                isPull = true;
                mPullScrollView.onPullDownRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ElasticityScrollView> refreshView) {

            }
        });

        content.findViewById(R.id.my_head_rl).setOnClickListener(this);
        myHeadImg = (ImageView) content.findViewById(R.id.my_head_img);
//        myHeadSexImg = (ImageView) content.findViewById(R.id.my_head_sex_img);
        iv_icon_level = (ImageView) content.findViewById(R.id.iv_icon_level);
        iv_level_info = (ImageView) content.findViewById(R.id.iv_level_info);
        myNameTv = (TextView) content.findViewById(R.id.my_name_tv);
        tv_level_next = (TextView) content.findViewById(R.id.tv_level_next);
        myCoinTv = (TextView) content.findViewById(R.id.my_coin_tv);
        v_history_read = content.findViewById(R.id.v_history_read);
        tv_level_or_login = (TextView) content.findViewById(R.id.tv_level_or_login);
        tv_level_or_login.setOnClickListener(this);
        content.findViewById(R.id.tv_level_exchange).setOnClickListener(this);
        content.findViewById(R.id.my_ll_message).setOnClickListener(this);
        content.findViewById(R.id.my_ll_invite).setOnClickListener(this);
        content.findViewById(R.id.my_ll_feedback).setOnClickListener(this);
        content.findViewById(R.id.my_ll_setting).setOnClickListener(this);
        content.findViewById(R.id.v_invite_level).setOnClickListener(this);
        content.findViewById(R.id.v_user_detail).setOnClickListener(this);
        content.findViewById(R.id.v_history_menu).setOnClickListener(this);

        /*rv_history = (RecyclerView) content.findViewById(R.id.rv_history);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_history.setLayoutManager(manager);
        mineAdapter = new RecyHistoryMineAdapter(mContext, null);
        rv_history.setAdapter(mineAdapter);*/

        ElasticityHelper.setUpOverScroll(mScrollView);

        mScrollView.setElasticityScrollListener(new ElasticityScrollView.OnScrollListener() {
            @Override
            public void onElasticityScrollChanged(int l, int t, int oldl, int oldt) {
                if (mPullScrollView!=null) {
                    statusBarChangerAlpha(t);
                }
            }
        });


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

    public void loadData() {
        /*try {
            if (userInfo.isLogin()) {
                Glide.with(mContext).load(userInfo.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.xxxx_no_login_head).into(myHeadImg);
                myNameTv.setText(userInfo.getNickname());
                myCoinTv.setText(userInfo.getV2_bookcoin() + "/" + userInfo.getMax_coin());
                String levelName = "";
                String levelSpace = "";
                try {
                    UserInfo.GradeEntity now = userInfo.getGrade().getNow();
                    UserInfo.GradeEntity next = userInfo.getGrade().getNext();
                    levelName = "L" + now.getLevel() + now.getName();
                    int nowNum = Integer.parseInt(now.getNum());
                    int nextNum = Integer.parseInt(next.getNum());
                    if (nextNum!=0&&(nextNum==nowNum)) {
                        levelSpace = "您已是最高等级";
                    } else {
                        levelSpace = String.format(getString(R.string.mine_invite_level), (nextNum-nowNum)+"");
                    }
                    if (TextUtils.isEmpty(now.getThumb())) {
                        Glide.with(mContext).load(R.mipmap.ic_level0).into(iv_level_info);
                    } else {
                        Glide.with(mContext).load(now.getThumb()).into(iv_level_info);
                    }
                    if (TextUtils.isEmpty(next.getThumb())) {
                        Glide.with(mContext).load(R.mipmap.ic_level0).into(iv_icon_level);
                    } else {
                        Glide.with(mContext).load(next.getThumb()).into(iv_icon_level);
                    }

                } catch (Exception e){
                    BLog.e(e);
                }
                tv_level_or_login.setText(levelName);
                tv_level_next.setText(levelSpace);

                myHeadSexImg.setVisibility(View.VISIBLE);
                myHeadSexImg.setImageResource(UserInfoHelper.getSex(userInfo.getSex()).getRes());

                setHistoryView();
            } else {
                myNameTv.setText("请登录");
                myHeadSexImg.setVisibility(View.GONE);
                Glide.with(mContext).load(R.mipmap.xxxx_no_login_head).into(myHeadImg);
                myCoinTv.setText("0");
                tv_level_or_login.setText("请登录");
                tv_level_next.setText("去推广升级吧");
                v_history_read.setVisibility(View.GONE);
            }
            mPullScrollView.onPullDownRefreshComplete();
            mPullScrollView.setLastUpdatedLabel(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_DATE_2));
        } catch (Exception e){
            BLog.e(e);
        }*/
    }

    /*private RecyclerView rv_history;
    private RecyHistoryMineAdapter mineAdapter;
    public void setHistoryView(){
        List<Book> list = BookHelper.getInstance().getBookRecordList(10);
        if (list!=null&&list.size()>0) {
            if (mineAdapter!=null) mineAdapter.update(list);
            if (v_history_read!=null) v_history_read.setVisibility(View.VISIBLE);
        } else {
            if (v_history_read!=null) v_history_read.setVisibility(View.GONE);
        }
    }*/

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.v_user_detail:
            case R.id.my_head_rl://编辑个人信息

                break;
            case R.id.my_ll_message:

                break;
            case R.id.tv_level_or_login:
            case R.id.v_invite_level:
            case R.id.my_ll_invite:
//                context.startActivity(new Intent(context, InfoAdsActivity.class));
                break;
            case R.id.my_ll_feedback:

                break;
            case R.id.my_ll_setting:

                break;
            case R.id.tv_level_exchange:

                break;
            case R.id.v_history_menu:

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
