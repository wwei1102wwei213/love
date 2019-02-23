package com.fuyou.play.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fuyou.play.R;
import com.fuyou.play.adapter.ChatAdapter;
import com.fuyou.play.bean.UserInfo;
import com.fuyou.play.bean.chat.ChatMessageBean;
import com.fuyou.play.biz.db.DBHelper;
import com.fuyou.play.biz.xmpp.XmppConnection;
import com.fuyou.play.util.Const;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.LogCustom;
import com.fuyou.play.util.sp.UserDataUtil;
import com.fuyou.play.view.BaseActivity;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-07-21.
 *
 * 聊天页面
 *
 * @author wwei
 */
public class ChatActivity extends BaseActivity{

    private Chat chat;// 单聊
    private MultiUserChat muc;// 群组
    //聊天类型 0为单聊，1为群聊
    private int CHAT_TYPE = 0;
    //聊天ID
    private int ChatID;
    //单聊用户信息
    private UserInfo mUserInfo;
    private EditText et;
    private RecyclerView rv;
    private LinearLayoutManager manager;
    private List<ChatMessageBean> list;
    private ChatAdapter adapter;
    private SwipeRefreshLayout swipe;
    private String LastTime = "";
    private int IntUserID = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setBackViews(R.id.iv_back_base);
        CHAT_TYPE = getIntent().getIntExtra(Const.INTENT_CHAT_TYPE, 0);
        try {
            IntUserID = Integer.parseInt(UserDataUtil.getUserID(this));
        } catch (Exception e){

        }
        initViews();
    }

    private void initViews() {
        try {
            if (CHAT_TYPE==0) {
                mUserInfo = (UserInfo) getIntent().getSerializableExtra(Const.INTENT_CHAT_USER);
                if (mUserInfo==null||mUserInfo.getId()==0) return;
                ((TextView) findViewById(R.id.tv_title_base)).setText(TextUtils.isEmpty(mUserInfo.getNickName())?"":mUserInfo.getNickName());
                chat = XmppConnection.getInstance().getFriendChat(mUserInfo.getId()+"@119.23.247.126");
                ChatID = mUserInfo.getId()+IntUserID;
            } else {

            }
            et = findViewById(R.id.et_text);
            findViewById(R.id.tv_send).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(et.getText().toString().trim())) {
                        sendMsg(et.getText().toString().trim(), 0);
                    } else {
                        showToast("消息不能为空");
                    }
                }
            });

            rv = findViewById(R.id.rv);
            manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(manager);
            list = DBHelper.getInstance().queryChatObject("", ChatID+"");
            if (list==null||list.size()==0) {
                list = new ArrayList<>();
            } else {
                LastTime = list.get(0).getSendTime();
            }
            adapter = new ChatAdapter(this, list);
            rv.setAdapter(adapter);
            if (list.size()>0) {
                rv.scrollToPosition(list.size()-1);
            }
            swipe = findViewById(R.id.swipe);
            swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    try {
                        List<ChatMessageBean> temp = DBHelper.getInstance().queryChatObject(LastTime, ChatID+"");
                        if (temp!=null&&temp.size()>0) {
                            int size = temp.size();
                            if (list!=null&&list.size()>0) {
                                temp.addAll(list);
                            }
                            list = temp;
                            LastTime = list.get(0).getSendTime();
                            adapter.update(list);
                            manager.scrollToPositionWithOffset(size-1, 0);
//                            rv.scrollToPosition(list.size()-1);
                        } else {
                            LogCustom.show("查询结果为空");
                        }
                        swipe.setRefreshing(false);
                    } catch (Exception e){

                    }
                }
            });
            EventBus.getDefault().register(this);
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    private void sendMsg(String body, int type) {
        final ChatMessageBean bean = new ChatMessageBean();

        bean.setFromID(IntUserID);
        bean.setFromName(UserDataUtil.getUserName(this));
        bean.setFromIcon(UserDataUtil.getAvatar(this));
        bean.setText(body);
        bean.setSendTime(System.currentTimeMillis()+"");
        bean.setToID(mUserInfo.getId());
        bean.setIsRead(0);
        bean.setType(0);
        bean.setChatID(ChatID);
        new Thread(new Runnable() {
            @Override
            public void run() {
                XmppConnection.getInstance().sendMessage(chat, muc, new Gson().toJson(bean));
            }
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(ChatMessageBean bean){
        try {
            LogCustom.show("Event ChatMessageBean");
            if (bean!=null) {
                list.add(bean);
                adapter.update(list);
                rv.scrollToPosition(list.size()-1);
                et.setText("");
//                manager.scrollToPositionWithOffset(list.size()-1, 0);
            }
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

}
