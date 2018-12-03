package com.fuyou.play.view.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fuyou.play.R;
import com.fuyou.play.adapter.QuizzesQuestionLvAdapter;
import com.fuyou.play.bean.quizzes.QuizzesAnswerBean;
import com.fuyou.play.bean.quizzes.QuizzesQuestionBean;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.FileUtils;
import com.fuyou.play.util.LogCustom;
import com.fuyou.play.util.sp.UserDataUtil;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.CircleImageView;
import com.fuyou.play.widget.custom.CustomScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 问答对话页面
 * Created by Ma on 2017/10/11.
 */

public class QuizzesDetailActivity extends BaseActivity {
    private String jsonFileName;
    private int type;
    private String baseUrl;

    private LinearLayout root_ll;
    private LayoutInflater inflater;
    private View system_view, user_view;
    private CustomScrollView sv;
    private List<RelativeLayout> rl_list = new ArrayList<>();
    private QuizzesQuestionBean questionBean;
    private QuizzesAnswerBean answerBean;
    private int[] idenfiters;
    private String[] answers;
    int flag = 0;
    List<QuizzesQuestionBean.AllQuestionBean> question_list = new ArrayList<>();

    private MyHandler mHandler;

    private List<String> list;
    private int mPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes_detail);
        initStatusBar(findViewById(R.id.status_bar));
        mHandler = new MyHandler(this);
        try {
            list = (List<String>) getIntent().getSerializableExtra("quizzes_list_data");
            mPosition = getIntent().getIntExtra("quizzes_position", 0);
            type = getIntent().getIntExtra("quizzes_type", 1);
            baseUrl = getIntent().getStringExtra("imageUrlHead");
            jsonFileName = list.get(mPosition);
            String s = getAssets("quizzes/RandomTransitContents.json");
            answers = new Gson().fromJson(s, new TypeToken<String[]>(){}.getType());
            initView();
            initAnim();
            initData(false);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"QuizzesDetailActivity onCreate");
        }
    }

    private String url = "";

    private ListView lv;
    private QuizzesQuestionLvAdapter adapter;
    private TextView tv_result,tv_next,tv_again;
    private View v_bottom,v_lv;
    private String answersResult;
    private void initView() {
        try {
            setBackViews(R.id.iv_back_base);
            setTitle(jsonFileName.substring(0, jsonFileName.length() - 5));

            sv = (CustomScrollView) findViewById(R.id.sv);
            root_ll = (LinearLayout) findViewById(R.id.quizzess_detail_acitvity_root_ll);
            v_bottom = findViewById(R.id.v_bottom);
            v_lv = findViewById(R.id.v_lv);
            tv_result = (TextView) findViewById(R.id.tv_result);
            tv_next = (TextView) findViewById(R.id.tv_next);
            tv_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_next.setClickable(false);
                    nextGame();
                }
            });
            tv_again = (TextView) findViewById(R.id.tv_again);

            tv_again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_again.setClickable(false);
                    restartGame();
                }
            });
            tv_result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        tv_result.setClickable(false);
                        Arrays.sort(idenfiters);
                        String temp = "";
                        for (int i=0;i<idenfiters.length;i++){
                            if (i!=0){
                                temp = temp.concat("-"+idenfiters[i]);
                            } else {
                                temp = temp.concat("" + idenfiters[0]);
                            }
                        }
                        LogCustom.show(temp);
                        List<QuizzesAnswerBean.AnswersResultListBean> list = answerBean.getAnswersResultList();
                        for (QuizzesAnswerBean.AnswersResultListBean bean : list ) {
                            if (bean.getAnswerCode().equals(temp)) {
                                last_flag = true;
                                answersResult = bean.getResultContent();
                                add_system_view(bean.getTitle());
                                break;
                            }
                        }
                    }catch (Exception e){
                        ExceptionUtils.ExceptionSend(e,"QuizzesDetailActivity add_result_view onClick");
                    }
                }
            });

            lv = (ListView) findViewById(R.id.lv);
            adapter = new QuizzesQuestionLvAdapter(this, null);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (!Flag_run){
                            QuizzesQuestionBean.AllQuestionBean.AnswersBean bean = adapter.getItem(position);
                            add_user_view(bean.getText());
                            idenfiters[flag] = Integer.parseInt(bean.getIndentifer());
                            add_system_answer_view(answers[getRandom(0, answers.length, 1)[0]]);
                            flag++;
                            refersh();
                        }
                    }catch (Exception e){
                        ExceptionUtils.ExceptionSend(e,"initVPList onClick");
                    }
                }
            });
            lv.setAdapter(adapter);

            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"QuizzesDetailActivity initView");
        }
    }

    private Animation down,up,in,out;
    //初始化动画
    private void initAnim(){
        down = AnimationUtils.loadAnimation(this, R.anim.activity_translate_down);
        up = AnimationUtils.loadAnimation(this, R.anim.activity_translate_up);
        in = AnimationUtils.loadAnimation(this, R.anim.quizzes_alpha_in);
        out = AnimationUtils.loadAnimation(this, R.anim.activity_alpha_out);
        down.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LogCustom.show("down onAnimationEnd");
                v_bottom.setVisibility(View.VISIBLE);
                v_bottom.startAnimation(in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    LogCustom.show("out onAnimationEnd");
                    tv_next.setVisibility(View.VISIBLE);
                    tv_again.setVisibility(View.VISIBLE);
                    tv_next.setClickable(true);
                    tv_again.setClickable(true);
                    tv_next.startAnimation(in);
                    tv_again.startAnimation(in);
                } catch (Exception e){
                    ExceptionUtils.ExceptionSend(e);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    /**
     * 设置数据
     * @param isReplay 是否replay
     */
    private void initData(boolean isReplay) {
        try {
            if (isReplay){
                mPosition++;
                if (mPosition> list.size()-1) mPosition = 0;
            }
            jsonFileName = list.get(mPosition);
            url = baseUrl + jsonFileName.replace(" ", "+").substring(0, jsonFileName.length() - 4) + "jpg";
            LogCustom.e(url);
            new parseDataTask(this, 0, type, jsonFileName).execute();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"QuizzesDetailActivity initData");
        }
    }

    private void parseDataComplete(Map<String, Object> map, int tag){
        if (mBaseExit) return;
        try {
            questionBean = (QuizzesQuestionBean) map.get("questionBean");
            answerBean = (QuizzesAnswerBean) map.get("answerBean");
            question_list = questionBean.getAllQuestion();
            idenfiters = new int[question_list.size()];
            refersh();
        } catch (Exception e){
            LogCustom.e(e, "parseDataComplete");
        }
    }

    //重启游戏
    private void restartGame(){
        try {
            root_ll.removeAllViews();
            flag = 0;
            last_flag = false;
            v_bottom.setVisibility(View.GONE);
            tv_next.setVisibility(View.GONE);
            tv_again.setVisibility(View.GONE);
            refersh();
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    //下一次游戏
    private void nextGame(){
        try {
            root_ll.removeAllViews();
            flag = 0;
            last_flag = false;
            v_bottom.setVisibility(View.GONE);
            tv_next.setVisibility(View.GONE);
            tv_again.setVisibility(View.GONE);
            initData(true);
            ((TextView)findViewById(R.id.title)).setText(jsonFileName.substring(0, jsonFileName.length() - 5));
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    //刷新viewpager
    void refersh() {
        try {
            if (flag < question_list.size()) {
                add_system_view(question_list.get(flag).getText());
            } else {
                v_lv.setVisibility(View.GONE);
                v_lv.startAnimation(down);
                v_bottom.setVisibility(View.INVISIBLE);
                tv_result.setVisibility(View.VISIBLE);
                tv_result.setClickable(true);
                tv_next.setVisibility(View.GONE);
                tv_again.setVisibility(View.GONE);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"QuizzesDetailActivity refersh");
        }
    }

    private boolean Flag_run = false;
    private String progress;
    //系统对话框
    void add_system_view(final String content) {
        try {
            system_view = inflater.inflate(R.layout.view_quizzes_detail_system_role, root_ll, false);
            Glide.with(this)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.default_user_icon)
                    .crossFade()
                    .into((ImageView) system_view.findViewById(R.id.circleImageView));
            final TextView tv = (TextView) system_view.findViewById(R.id.view_quizzes_detail_system_role_content_tv);
            tv.setText(".");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Flag_run = true;
                        progress = ".";
                        for (int i = 0; i <= question_list.size(); i++) {
                            Thread.sleep(500);
                            if (i<question_list.size()){
                                progress = progress.concat(".");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!mBaseExit){
                                            tv.setText(progress);
                                        }
                                    }
                                });
                            }
                            if (i==question_list.size()){
                                LogCustom.show("question_list ok");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!mBaseExit){
                                            LogCustom.show("question_list ok");
                                            tv.setText(content);
                                            mHandler.sendEmptyMessage(SV_BOTTOM);
                                            if (flag < question_list.size()){
                                                adapter.update(question_list.get(flag).getAnswers());
                                                if (flag==0&&v_lv.getVisibility()== View.GONE){
                                                    LogCustom.show("getVisibility v_lv");
                                                    v_lv.setVisibility(View.VISIBLE);
                                                    v_lv.startAnimation(up);
                                                }
                                            }
                                            if (last_flag){
                                                mHandler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (!mBaseExit){
                                                            LogCustom.show("out add_system_answer_view");
                                                            add_system_answer_view(answersResult);
                                                            tv_result.setVisibility(View.GONE);
                                                            tv_result.startAnimation(out);
                                                        }
                                                    }
                                                }, 500);
                                            }
                                            Flag_run = false;
                                        }
                                    }
                                });

                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            root_ll.addView(system_view);
            mHandler.sendEmptyMessage(SV_BOTTOM);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"QuizzesDetailActivity refersh");
        }
    }

    //系统回答框
    void add_system_answer_view(String content) {
        if (TextUtils.isEmpty(content)) return;
        try {
            system_view = inflater.inflate(R.layout.view_quizzes_detail_system_role, root_ll, false);
            Glide.with(this)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.default_user_icon)
                    .crossFade()
                    .into((ImageView) system_view.findViewById(R.id.circleImageView));
            TextView tv = (TextView) system_view.findViewById(R.id.view_quizzes_detail_system_role_content_tv);
            tv.setText(content);
            root_ll.addView(system_view);
            mHandler.sendEmptyMessage(SV_BOTTOM);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"QuizzesDetailActivity add_system_answer_view");
        }

    }

    //用户对话框
    void add_user_view(String content) {
        try {
            user_view = inflater.inflate(R.layout.view_quizzes_detail_user_role, root_ll, false);
            TextView tv = (TextView) user_view.findViewById(R.id.view_quizzes_detail_user_role_content_tv);
            CircleImageView civ = (CircleImageView) user_view.findViewById(R.id.view_quizzes_detail_user_role_head_iv);
            Glide.with(this)
                    .load(UserDataUtil.getAvatar(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.default_user_icon)
                    .crossFade()
                    .into(civ);
            tv.setText(content);
            root_ll.addView(user_view);
            mHandler.sendEmptyMessage(SV_BOTTOM);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"QuizzesDetailActivity add_user_view");
        }

    }

    private boolean last_flag = false;




    //获取不重复随机数
    private int[] getRandom(int min, int max, int n) {
        int len = max - min + 1;

        if (max < min || n > len) {
            return null;
        }

        //初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min + len; i++) {
            source[i - min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;

    }

    /**
     * 读取资源
     *
     * @param fileName
     * @return
     */
    private String getAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            inputReader.close();
            bufReader.close();
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void srcollToDowm(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void scrollToDown(){
        if (mBaseExit) return;
        sv.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public static final int SV_BOTTOM = 1;
    private static class MyHandler extends Handler {
        private WeakReference<QuizzesDetailActivity> weak;
        private QuizzesDetailActivity activity;
        public MyHandler(QuizzesDetailActivity activity){
            weak = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            activity = weak.get();
            if (msg.what==SV_BOTTOM){
                activity.scrollToDown();
            }
        }
    }

    private static class parseDataTask extends AsyncTask<Object, Void, Map<String, Object>> {

        private WeakReference<QuizzesDetailActivity> weak;
        private QuizzesDetailActivity activity;
        private int taskTag;
        private int type;
        private String jsonFileName;

        public parseDataTask(QuizzesDetailActivity activity, int taskTag, int dirName, String jsonFileName){
            weak = new WeakReference<>(activity);
            this.taskTag = taskTag;
            this.type = dirName;
            this.jsonFileName = jsonFileName;
        }

        @Override
        protected Map<String, Object> doInBackground(Object[] objects) {
            try {
                activity = weak.get();
                QuizzesQuestionBean questionBean = new Gson().fromJson(FileUtils.getStringFromAssets(
                        activity,"quizzes/" + (type+1) + "/" + jsonFileName), QuizzesQuestionBean.class);
                String json = FileUtils.getStringFromAssets(
                        activity,"quizzes/" +(type+4) + "/" + jsonFileName);
                json = json.substring(1, json.length()-1);
                QuizzesAnswerBean answerBean = new Gson().fromJson(json, QuizzesAnswerBean.class);
                Map<String, Object> map = new HashMap<>();
                map.put("questionBean", questionBean);
                map.put("answerBean", answerBean);
                return map;
            } catch (Exception e){
                LogCustom.e(e, "parseDataTask");
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Map<String, Object> o) {
            activity = weak.get();
            if (activity!=null&&o!=null&&o.size()!=0){
                activity.parseDataComplete(o, taskTag);
            }
        }

    }

}
