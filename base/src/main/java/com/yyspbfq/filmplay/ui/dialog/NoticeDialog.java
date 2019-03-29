package com.yyspbfq.filmplay.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.bean.NoticeBean;
import com.yyspbfq.filmplay.utils.BLog;

import net.nightwhistler.htmlspanner.HtmlSpanner;

public class NoticeDialog extends Dialog{

    private Context context;

    public NoticeDialog(@NonNull Context context) {
        this(context, R.style.dialog_base_style);
    }

    public NoticeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initViews();
    }

    private TextView tv_title, tv_sub_title, tv_content, tv_sure;

    private void initViews() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null);
        setContentView(view);
        tv_title = view.findViewById(R.id.tv_title);
        tv_sub_title = view.findViewById(R.id.tv_sub_title);
        tv_content = view.findViewById(R.id.tv_content);
        tv_content.setMovementMethod(new ScrollingMovementMethod());
        tv_sure = view.findViewById(R.id.tv_sure);

        setCancelable(false);
    }

    public void setData(NoticeBean bean) {
        try {
            tv_title.setText(TextUtils.isEmpty(bean.getTitle())?"公告":bean.getTitle());
            if (!TextUtils.isEmpty(bean.getSubheading())) {
                tv_sub_title.setText(bean.getSubheading());
                tv_sub_title.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(bean.getContent())){
                tv_content.setText("暂无公告");
            } else {
                String temp = bean.getContent();
                temp = temp.replaceAll("<p>", "");
                temp = temp.replaceAll("</p>", "<br/>");
                if (temp.endsWith("<br/>")) {
                    temp = temp.substring(0, temp.length()-5);
                }
                tv_content.setText(new HtmlSpanner().fromHtml(temp));
                tv_content.setMovementMethod(LinkMovementMethod.getInstance());
            }
            tv_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } catch (Exception e){
            BLog.e(e);
        }
    }

    public void setUpdateHint(String website) {
        tv_title.setText("提示");
        tv_content.setText("部分配置已失效，亲们两行泪!!!\n请至官网重新下载最新版App。");
        tv_sure.setText("去下载");
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                    context.startActivity(intent);
                } catch (Exception e) {
                    BLog.e(e);
                }
                dismiss();
            }
        });
    }

}
