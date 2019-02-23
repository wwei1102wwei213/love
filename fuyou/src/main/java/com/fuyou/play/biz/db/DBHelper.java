package com.fuyou.play.biz.db;

import android.text.TextUtils;

import com.fuyou.play.LApplication;
import com.fuyou.play.bean.chat.ChatMessageBean;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.LogCustom;
import com.ta.util.db.TASQLiteDatabase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2016/3/30.
 * <p>
 * 数据表单帮助类
 *
 * @author wwei
 */
public class DBHelper {

    private DBHelper() {
    }

    private static DBHelper instance = null;

    /***
     * 单例模式，注意要加同步锁
     *
     * @return DBHelper
     */
    public static DBHelper getInstance() {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 创建表
     * @param mClass 表标识
     */
    public void createTable(Class<?> mClass){
        try {
            TASQLiteDatabase tasqLiteDatabase = LApplication.getInstance().getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(mClass)) {
                tasqLiteDatabase.creatTable(mClass);
                LogCustom.show("DBHelper", mClass.getSimpleName()+"表已创建");
            }
            LApplication.getInstance().getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    /**
     * 删除表
     * @param mClass 表标识
     */
    public void dropTable(Class<?> mClass){
        try {
            TASQLiteDatabase tasqLiteDatabase = LApplication.getInstance().getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(mClass)) {
                tasqLiteDatabase.dropTable(mClass);
                LogCustom.show("DBHelper", mClass.getSimpleName()+"表已删除");
            }
            LApplication.getInstance().getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    /**
     * 插入数据
     * @param object 数据
     * @param mClass 表标识
     */
    public void insertObject(Object object, Class<?> mClass) {
        try {
            TASQLiteDatabase tasqLiteDatabase = LApplication.getInstance().getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(mClass)) {
                tasqLiteDatabase.creatTable(mClass);
                LogCustom.show("DBHelper", mClass.getSimpleName()+"表已创建");
            }
            tasqLiteDatabase.insert(object);
            LApplication.getInstance().getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    /**
     * 查询数据
     * @param mClass  表标识
     * @param where   查询条件
     * @param orderBy 排序
     * @param limit   分页
     * @param <T>     返回类型
     * @return
     */
    public <T> List<T> queryObject (Class<?> mClass, String where, String orderBy, String limit){
        try {
            TASQLiteDatabase tasqLiteDatabase = LApplication.getInstance().getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(mClass)) {
                return null;
            }
            List<T> list = tasqLiteDatabase.query(mClass, false, where, null, null, orderBy, limit);
            LApplication.getInstance().getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
            return list;
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
        return null;
    }

    /**
     * 查询聊天记录
     * @param lastTime
     * @param ChatID
     * @return
     */
    public List<ChatMessageBean> queryChatObject (String lastTime, String ChatID){
        try {
            TASQLiteDatabase tasqLiteDatabase = LApplication.getInstance().getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(ChatMessageBean.class)) {
                return null;
            }
            String where = "ChatID="+ChatID;
            if (!TextUtils.isEmpty(lastTime)) {
                where += " AND SendTime<" + lastTime;
            }
            List<ChatMessageBean> list = tasqLiteDatabase.query(ChatMessageBean.class, false, where,
                    null, null, "SendTime desc", "10");
            Collections.sort(list, new Comparator<ChatMessageBean>() {
                @Override
                public int compare(ChatMessageBean bean, ChatMessageBean t1) {
                    try {
                        long time1 = Long.parseLong(bean.getSendTime());
                        long time2 = Long.parseLong(t1.getSendTime());
                        int result = 0;
                        if (time1>time2) {
                            result = 1;
                        } else if (time1<time2) {
                            result = -1;
                        }
                        return result;
                    } catch (Exception e){

                    }
                    return 0;
                }
            });
            LApplication.getInstance().getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
            return list;
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
        return null;
    }

    public void insertChatHistory(List<ChatMessageBean> list) {
        try {
            TASQLiteDatabase tasqLiteDatabase = LApplication.getInstance().getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(ChatMessageBean.class)) {
                tasqLiteDatabase.creatTable(ChatMessageBean.class);
                LogCustom.show("DBHelper", ChatMessageBean.class.getSimpleName()+"表已创建");
            }
            List<ChatMessageBean> find;
            String where;
            for (ChatMessageBean bean:list) {
                where = "ChatID="+bean.getChatID() + " AND SendTime=" + bean.getSendTime() + " AND ToID=" + bean.getToID();
                find = tasqLiteDatabase.query(ChatMessageBean.class, false, where, null, null, null, null);
                if (find==null||find.size()==0) {
                    tasqLiteDatabase.insert(bean);
                }
            }
            LApplication.getInstance().getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

}
