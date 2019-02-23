package com.fuyou.play.biz.db;

import com.fuyou.play.bean.chat.ChatMessageBean;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2018-07-31.
 *
 * @author wwei
 */
public class ChatMessageUtil {

    //保存聊天内容
    //ConcurrentHashMap 是线程安全的
    //CopyOnWriteArrayList 是线程安全的
    public static ConcurrentHashMap<String, CopyOnWriteArrayList<ChatMessageBean>> map=new ConcurrentHashMap<String, CopyOnWriteArrayList<ChatMessageBean>>();

    public static void addMessage(String room,ChatMessageBean msg){
        CopyOnWriteArrayList<ChatMessageBean> copyOnWriteArrayList=map.get(room);
        //第一次
        if (copyOnWriteArrayList==null){
            copyOnWriteArrayList=new CopyOnWriteArrayList<ChatMessageBean>();
        }
        copyOnWriteArrayList.add(msg);
        map.put(room, copyOnWriteArrayList);
    }

}
