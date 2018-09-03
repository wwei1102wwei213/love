package com.fuyou.play.biz.xmpp;

import org.jivesoftware.smack.packet.Message;

/**
 * 被观察者接口
 */
public interface Watched {

    /**
     * 添加观察者
     *
     * @param watcher
     */
    void addWatcher(Watcher watcher);

    /**
     * 删除观察者
     *
     * @param watcher
     */
    void removeWatcher(Watcher watcher);

    /**
     * 提醒所有的观察者
     *
     * @param message message
     */
    void notifyWatchers(Message message);

    /**
     * 删除所有观察者
     *
     */
    void removeAll();
}
