package com.fuyou.play.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.fuyou.play.R;
import com.fuyou.play.bean.chat.ChatMessageBean;
import com.fuyou.play.biz.FYThreadPoolManager;
import com.fuyou.play.biz.db.DBHelper;
import com.fuyou.play.biz.http.HttpFlag;
import com.fuyou.play.biz.http.HttpRepListener;
import com.fuyou.play.biz.xmpp.Watcher;
import com.fuyou.play.biz.xmpp.XMChatMessageListener;
import com.fuyou.play.biz.xmpp.XmppConnection;
import com.fuyou.play.util.Const;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.LogCustom;
import com.fuyou.play.util.sp.UserDataUtil;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.view.BaseFragment;
import com.fuyou.play.view.fragment.AstrolableFragment;
import com.fuyou.play.view.fragment.DiscussFragment;
import com.fuyou.play.view.fragment.HomeFragment;
import com.fuyou.play.view.fragment.MyselfFragment;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.packet.Message;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import static com.fuyou.play.util.Const.TAG_LOG;

public class MainActivity extends BaseActivity implements HttpRepListener, View.OnClickListener, Watcher{

    private RadioButton[] btns;
    private Fragment[] fragments;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_main);
        dealWithLogin(UserDataUtil.getUserID(this), Const.CHAT_ACCOUNT_PASSWORD);
        initViews();
    }


    private void initViews(){
        try {
            btns = new RadioButton[4];
            btns[0] = (RadioButton)findViewById(R.id.btn1);
            btns[1] = (RadioButton)findViewById(R.id.btn2);
            btns[2] = (RadioButton)findViewById(R.id.btn3);
            btns[3] = (RadioButton)findViewById(R.id.btn4);

            for (int i=0;i<4;i++){
                btns[i].setOnClickListener(new MyBtnClickListener());
            }
            BaseFragment fragment1,fragment2,fragment3,fragment4;
            fragmentManager = getSupportFragmentManager();
            fragment1 = new HomeFragment();
            fragment2 = new AstrolableFragment();
            fragment3 = new DiscussFragment();
            fragment4 = new MyselfFragment();
            fragments = new Fragment[]{fragment1,fragment2,fragment3,fragment4};

            for (int i=0;i<4;i++){
                Fragment fragment = fragmentManager.findFragmentByTag(i+"");
                if (fragment!=null){
                    fragmentManager.beginTransaction().remove(fragment).commit();
                }
            }

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fl,fragments[currentIndex],currentIndex+"");
            transaction.show(fragments[currentIndex]);
            transaction.commit();
            btns[currentIndex].setChecked(true);
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, String> map = new HashMap<>();
        if (HttpFlag.LOGIN == flag) {
            map.put("PhoneNum", "13723701704");
            map.put("Code", "123456");
        } else if (HttpFlag.REGISTER == flag) {
            map.put("username", "goodluck");
            map.put("password", "123456");
            map.put("gender", "0");
        } else if (HttpFlag.USER_DETAIL == flag) {
            map.put("InfoID", "100003");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag, Object obj) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag, Object obj) {

    }

    @Override
    public void showLoading(int flag, Object obj) {

    }

    @Override
    public void hideLoading(int flag, Object obj) {

    }

    @Override
    public void showError(int flag, Object obj, int errorType) {
        Log.d(TAG_LOG, "showError");
    }

    private long exitSpaceTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitSpaceTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitSpaceTime = System.currentTimeMillis();
            } else {
                System.gc();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private int selectIndex;
    private int currentIndex;
    private class MyBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn1:
                    selectIndex = 0;
                    break;
                case R.id.btn2:
                    selectIndex = 1;
                    break;
                case R.id.btn3:
                    selectIndex = 2;
                    break;
                case R.id.btn4:
                    selectIndex = 3;
                    break;
            }
            if (selectIndex!=currentIndex){
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.hide(fragments[currentIndex]);
                if (!fragments[selectIndex].isAdded()){
                    transaction.add(R.id.fl,fragments[selectIndex], selectIndex+"");
                }
                transaction.show(fragments[selectIndex]);
                transaction.commit();
                btns[selectIndex].setChecked(true);
                currentIndex = selectIndex;
            }
        }
    }

    private void dealWithLogin(final String account, final String password) {
        FYThreadPoolManager.execute(new XmppRunnable(this, account, password));
    }

    @Override
    public void update(Message message) {
        try {
            LogCustom.show("update:"+message.getBody());
            ChatMessageBean bean = null;
            try {
                bean = new Gson().fromJson(message.getBody(), ChatMessageBean.class);
            } catch (JsonSyntaxException e){
                ExceptionUtils.ExceptionSend(e);
            } catch (Exception e){
                ExceptionUtils.ExceptionSend(e);
            }
            if (bean!=null) {
                EventBus.getDefault().post(bean);
                DBHelper.getInstance().insertObject(bean, ChatMessageBean.class);
            }
            if (message.getType()== Message.Type.chat) {
                LogCustom.show("type:chat");
            } else if (message.getType()== Message.Type.groupchat) {
                LogCustom.show("type:groupchat");
            } else if (message.getType()== Message.Type.normal) {
                LogCustom.show("type:normal");
            } else if (message.getType()== Message.Type.headline) {
                LogCustom.show("type:headline");
            } else {
                LogCustom.show("type:error");
            }

        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    @Override
    public void finish() {
        try {
            // 更改用户状态为离线
            XmppConnection.getInstance().setPresence(5);
            // 1.退出程序应该移除监听
            // 2.退出程序应该移除连接监听
            // 3.退出程序应该关闭连接
            XmppConnection.getInstance().closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.finish();
    }

    private static class XmppRunnable implements Runnable {

        private String account, password;
        private WeakReference<Watcher> weak;

        private XmppRunnable(Watcher watcher, String account, String password) {
            weak = new WeakReference<>(watcher);
            this.account = account;
            this.password = password;
        }

        @Override
        public void run() {
            try {
                if (weak!=null&&weak.get()!=null&&XmppConnection.getInstance().login(account, password)) {
                    LogCustom.show("登录成功");
                    XmppConnection.getInstance().getHistoryMessages();
                    XmppConnection.getInstance().initChatManager();
                    XmppConnection.getInstance().initGroupChatManager();
                    XMChatMessageListener.removeAll();
                    XMChatMessageListener.addWatcher(weak.get());// 增加XMPP消息观察者
                } else {
                    LogCustom.show("登录失败");
                }
            } catch (Exception e){
                LogCustom.show("登录失败");
                ExceptionUtils.ExceptionSend(e);
            }
        }
    }
}
