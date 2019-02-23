package com.yyspbfq.filmplay.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wei.wlib.http.WLibHttpListener;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.HelpDataBean;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.ui.BaseActivity;
import com.yyspbfq.filmplay.utils.BLog;

import net.nightwhistler.htmlspanner.HtmlSpanner;

import java.util.List;

public class VideoHelpActivity extends BaseActivity implements WLibHttpListener{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_help);
        initStatusBar();
        setBackViews(R.id.iv_base_back);
        ((TextView) findViewById(R.id.tv_base_title)).setText("帮助与反馈");
        initViews();
        initData();
    }

    private LinearLayout body;
    private void initViews() {
        body = findViewById(R.id.v_body);
        findViewById(R.id.tv_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFeedback();
            }
        });
    }

    private void toFeedback(){
        startActivity(FeedbackActivity.class);
    }

    private void initData() {
        Factory.resp(this, HttpFlag.FLAG_INFO_HELP, null, null).post(null);
    }

    @Override
    public void handleResp(Object formatData, int flag, Object tag, String response, String hint) {
        if (flag == HttpFlag.FLAG_INFO_HELP) {
            try {
                List<HelpDataBean> list = new Gson().fromJson(formatData.toString(), new TypeToken<List<HelpDataBean>>(){}.getType());
                if (list!=null&&list.size()>0) {
                    HtmlSpanner spanner = new HtmlSpanner();
                    body.removeAllViews();
                    for (int i=0;i<list.size();i++) {
                        String name = list.get(i).getName();
                        TextView tv_name = (TextView) LayoutInflater.from(this).inflate(R.layout.item_info_help_title, body, false);
                        tv_name.setText(name==null?"": name);
                        body.addView(tv_name);
                        String content = list.get(i).getContent();
                        TextView tv_content = (TextView) LayoutInflater.from(this).inflate(R.layout.item_info_help_content, body, false);
                        tv_content.setText(content==null?"": spanner.fromHtml(content));
                        tv_content.setMovementMethod(LinkMovementMethod.getInstance());
                        body.addView(tv_content);
                        if (i<list.size()-1) {
                            View line = LayoutInflater.from(this).inflate(R.layout.item_info_help_line, body, false);
                            body.addView(line);
                        }
                    }
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
}
