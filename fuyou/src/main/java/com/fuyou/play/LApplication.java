package com.fuyou.play;

import com.fuyou.play.biz.xmpp.XMChatMessageListener;
import com.fuyou.play.biz.xmpp.XmppConnection;
import com.orhanobut.logger.AndroidLogAdapter;
import com.ta.TAApplication;

import org.jivesoftware.smack.chat2.ChatManager;

/**
 * Created by Administrator on 2018/3/30 0030.
 *
 * @author wwei
 */
public class LApplication extends TAApplication {


    private static LApplication instance;
    public static int SHARE_TAG = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        //Application对象
        instance = this;
        //LOG输出设置
        com.orhanobut.logger.Logger.addLogAdapter(new AndroidLogAdapter());
        /*UMConfigure.setLogEnabled(true);
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        UMConfigure.init(this, "59892f08310c9307b60023d0", "Umeng", UMConfigure.DEVICE_TYPE_PHONE,
                "669c30a9584623e70e8cd01b0381dcb4");*/

    }

    // 聊天监听类
    private void initChatManager() {
        // 单聊
        ChatManager cm = ChatManager.getInstanceFor(XmppConnection.getInstance().getConnection());
        cm.addIncomingListener(new XMChatMessageListener());
        cm.addOutgoingListener(new XMChatMessageListener());
    }

    public static synchronized LApplication getInstance() {
        return instance;
    }

    /*public String getInfoToken(){
        if (TextUtils.isEmpty(INFO_TOKEN)){
            INFO_TOKEN = UserDataUtil.getAccessToken(this);
        }
        return INFO_TOKEN;
    }

    public void setInfoToken(String token){
        INFO_TOKEN = token;
    }*/


}
