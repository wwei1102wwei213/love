package com.yyspbfq.filmplay.db;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.ta.TAApplication;
import com.ta.util.db.TASQLiteDatabase;
import com.yyspbfq.filmplay.biz.Factory;
import com.yyspbfq.filmplay.biz.http.HttpFlag;
import com.yyspbfq.filmplay.utils.BLog;
import com.yyspbfq.filmplay.utils.tools.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void createTable(TAApplication application, Class<?> mClass){
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(mClass)) {
                tasqLiteDatabase.creatTable(mClass);
                Log.e("DBHelper", mClass.getSimpleName()+"表已创建");
            }
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            Log.e("DBHelper", e.getMessage());
        }
    }

    /**
     * 删除表
     * @param mClass 表标识
     */
    public void dropTable(TAApplication application, Class<?> mClass){
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(mClass)) {
                tasqLiteDatabase.dropTable(mClass);
                Log.e("DBHelper", mClass.getSimpleName()+"表已删除");
            }
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            Log.e("DBHelper", e.getMessage());
        }
    }

    /**
     * 插入数据
     * @param object 数据
     * @param mClass 表标识
     */
    public void insertObject(TAApplication application, Object object, Class<?> mClass) {
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(mClass)) {
                tasqLiteDatabase.creatTable(mClass);
                Log.e("DBHelper", mClass.getSimpleName()+"表已创建");
            }
            tasqLiteDatabase.insert(object);
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            Log.e("DBHelper", e.getMessage());
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
    public <T> List<T> queryObject (TAApplication application, Class<?> mClass, String where, String orderBy, String limit){
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(mClass)) {
                return null;
            }
            List<T> list = tasqLiteDatabase.query(mClass, false, where, null, null, orderBy, limit);
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
            return list;
        } catch (Exception e){
            Log.e("DBHelper", e.getMessage());
        }
        return null;
    }

    /**
     * 同步播放记录
     * @param application
     * @param list
     */
    public void syncVideoRecord(TAApplication application, List<VideoEntity> list) {
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (tasqLiteDatabase.hasTable(VideoRecordBean.class)) {
                tasqLiteDatabase.dropTable(VideoRecordBean.class);
                Log.e("DBHelper", VideoRecordBean.class.getSimpleName()+"表已创建");
            }
            tasqLiteDatabase.creatTable(VideoRecordBean.class);
            for (VideoEntity entity:list) {
                VideoRecordBean bean = new VideoRecordBean();
                bean.setVid(entity.getId());
                bean.setLast_progress(Long.parseLong(entity.getWatch_time()));
                bean.setUpdate_time(Long.parseLong(entity.getC_date()));
                bean.setDetail(new Gson().toJson(entity));
                tasqLiteDatabase.insert(bean);
            }
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    /**
     * 删除播放记录
     * @param application
     * @param list
     */
    public void deleteVideoRecord(TAApplication application, List<VideoRecordBean> list) {
        if (list==null||list.size()==0) return;
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(VideoRecordBean.class)) {
                tasqLiteDatabase.creatTable(VideoRecordBean.class);
                Log.e("DBHelper", VideoRecordBean.class.getSimpleName()+"表已创建");
            }
            for (VideoRecordBean entity:list) {
                String where = "vid="+entity.getVid();
                try {
                    tasqLiteDatabase.delete(VideoRecordBean.class, where);
                } catch (Exception e){

                }
            }
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    /**
     * 更新视频播放进度
     * @param application
     * @param object
     */
    public void updateVideoRecord(TAApplication application, VideoRecordBean object)  {
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(VideoRecordBean.class)) {
                tasqLiteDatabase.creatTable(VideoRecordBean.class);
            }
            String where = "vid="+object.getVid();
            try {
                tasqLiteDatabase.delete(VideoRecordBean.class, where);
            } catch (Exception e){

            }
            tasqLiteDatabase.insert(object);
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
            Map<String, String> map = new HashMap<>();
            map.put("vid", object.getVid());
            map.put("watchTime", object.getLast_progress()+"");
            Factory.resp(null, HttpFlag.FLAG_SET_VIDEO_RECORD, null, null).post(map);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    /**
     * 获取视频播放记录
     * @param application
     * @param
     */
    public List<VideoRecordBean> getVideoRecord(TAApplication application, int limit)  {
        List<VideoRecordBean> result = new ArrayList<>();
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(VideoRecordBean.class)) {
                tasqLiteDatabase.creatTable(VideoRecordBean.class);
                BLog.e("DBHelper", VideoRecordBean.class.getSimpleName()+"表已创建");
            }
            List<VideoRecordBean> find = tasqLiteDatabase.query(
                    VideoRecordBean.class, false, null, null, null, "update_time desc", limit+"");
            if (find!=null&&find.size()>0) {
                result.addAll(find);
            }
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            BLog.e(e);
        }
        return result;
    }

    public VideoRecordBean getProgressById(Context context, String id) {
        VideoRecordBean result = null;
        try {
            TAApplication application = (TAApplication) context.getApplicationContext();
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(VideoRecordBean.class)) {
                tasqLiteDatabase.creatTable(VideoRecordBean.class);
            }
            String where = "vid="+id;
            List<VideoRecordBean> find = tasqLiteDatabase.query(
                    VideoRecordBean.class, false, where, null, null, null, null);
            if (find!=null&&find.size()>0) {
                result = find.get(0);
            }
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            BLog.e(e);
        }
        return result;
    }

    /**
     * 获取缓存列表
     * @param application
     * @param
     */
    public List<VideoDownloadBean> getDownloadRecord(TAApplication application)  {
        List<VideoDownloadBean> result = new ArrayList<>();
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(VideoDownloadBean.class)) {
                tasqLiteDatabase.creatTable(VideoDownloadBean.class);
                Log.e("DBHelper", VideoDownloadBean.class.getSimpleName()+"表已创建");
            }
            List<VideoDownloadBean> find = tasqLiteDatabase.query(
                    VideoDownloadBean.class, false, null, null, null, "create_time desc", null);
            if (find!=null&&find.size()>0) {
                result.addAll(find);
            }
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            BLog.e(e);
        }
        return result;
    }

    /**
     * 获取缓存列表
     * @param application
     * @param
     */
    public List<VideoDownloadBean> getDownloadRecordCompleted(TAApplication application, int limit)  {
        List<VideoDownloadBean> result = new ArrayList<>();
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(VideoDownloadBean.class)) {
                tasqLiteDatabase.creatTable(VideoDownloadBean.class);
                Log.e("DBHelper", VideoDownloadBean.class.getSimpleName()+"表已创建");
            }
            List<VideoDownloadBean> find = tasqLiteDatabase.query(
                    VideoDownloadBean.class, false, "state=99", null, null, "create_time desc", limit+"");
            if (find!=null&&find.size()>0) {
                result.addAll(find);
            }
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            BLog.e(e);
        }
        return result;
    }

    /**
     * 获取key获取下载记录
     * @param application
     * @param
     */
    public VideoDownloadBean getDownloadRecordByKey(TAApplication application, String key)  {
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(VideoDownloadBean.class)) {
                tasqLiteDatabase.creatTable(VideoDownloadBean.class);
                Log.e("DBHelper", VideoDownloadBean.class.getSimpleName()+"表已创建");
            }
            List<VideoDownloadBean> find = tasqLiteDatabase.query(
                    VideoDownloadBean.class, false, "vid="+key, null, null, "create_time desc", null);
            if (find!=null&&find.size()>0) {
                return find.get(0);
            }
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            BLog.e(e);
        }
        return null;
    }

    /**
     * 插入下载记录
     * @param application
     * @param object
     */
    public void insertDownloadRecord(TAApplication application, VideoDownloadBean object)  {
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(VideoDownloadBean.class)) {
                tasqLiteDatabase.creatTable(VideoDownloadBean.class);
                Log.e("DBHelper", VideoDownloadBean.class.getSimpleName()+"表已创建");
            }
            String where = "vid="+object.getVid();
            Log.e("DBHelper", "VideoDownloadBean:"+new Gson().toJson(object) +"\nwhere:"+where);
            List<VideoDownloadBean> find = tasqLiteDatabase.query(
                    VideoDownloadBean.class, false, where, null, null, null, null);
            if (find==null||find.size()==0) {
                tasqLiteDatabase.insert(object);
            }
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    /**
     * 更新下载视频总大小
     * @param application
     * @param object
     */
    public void updateDownloadDataSize(TAApplication application, VideoDownloadBean object)  {
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(VideoDownloadBean.class)) {
                tasqLiteDatabase.creatTable(VideoDownloadBean.class);
                Log.e("DBHelper", VideoDownloadBean.class.getSimpleName()+"表已创建");
            }
            String where = "vid="+object.getVid();
            Log.e("DBHelper", "VideoDownloadBean:"+new Gson().toJson(object) +"\nwhere:"+where);
            try {
                tasqLiteDatabase.delete(VideoDownloadBean.class, where);
            } catch (Exception e){

            }
            tasqLiteDatabase.insert(object);
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);

        } catch (Exception e){
            BLog.e(e);
        }
    }

    /**
     * 更新下载视频总大小
     * @param application
     * @param object
     */
    public void updateDownloadState(TAApplication application, VideoDownloadBean object)  {
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(VideoDownloadBean.class)) {
                tasqLiteDatabase.creatTable(VideoDownloadBean.class);
                Log.e("DBHelper", VideoDownloadBean.class.getSimpleName()+"表已创建");
            }
            String where = "vid="+object.getVid();
            Log.e("DBHelper", "VideoDownloadBean:"+new Gson().toJson(object) +"\nwhere:"+where);
            try {
                tasqLiteDatabase.delete(VideoDownloadBean.class, where);
            } catch (Exception e){

            }
            tasqLiteDatabase.insert(object);
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);

        } catch (Exception e){
            BLog.e(e);
        }
    }

    /**
     *
     * @param application
     * @param list
     */
    public void deleteDownloadRecord(TAApplication application, List<String> list) {
        if (list==null||list.size()==0) return;
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(VideoDownloadBean.class)) {
                tasqLiteDatabase.creatTable(VideoDownloadBean.class);
                Log.e("DBHelper", VideoDownloadBean.class.getSimpleName()+"表已创建");
            }
            for (String key:list) {
                String where = "vid="+key;
                try {
                    tasqLiteDatabase.delete(VideoDownloadBean.class, where);
                } catch (Exception e){

                }
                FileUtils.deleteFileById(key);
            }
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    /**
     *
     * @param application
     */
    public void deleteDownloadRecord(TAApplication application, String key) {
        if (key == null) return;
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(VideoDownloadBean.class)) {
                tasqLiteDatabase.creatTable(VideoDownloadBean.class);
                Log.e("DBHelper", VideoDownloadBean.class.getSimpleName()+"表已创建");
            }
            try {
                tasqLiteDatabase.delete(VideoDownloadBean.class, "vid="+key);
            } catch (Exception e){

            }
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    /**
     *
     * @param application
     */
    public void clearVideoRecord(TAApplication application) {
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(VideoRecordBean.class)) {

            } else {
                tasqLiteDatabase.dropTable(VideoRecordBean.class);
            }
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            BLog.e(e);
        }
    }

    /**
     * 查询聊天记录
     * @param lastTime
     * @param ChatID
     * @return
     */
    /*public List<ChatMessageBean> queryChatObject (TAApplication applicationString lastTime, String ChatID){
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
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
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
            return list;
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
        return null;
    }

    public void insertChatHistory(List<ChatMessageBean> list) {
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            if (!tasqLiteDatabase.hasTable(ChatMessageBean.class)) {
                tasqLiteDatabase.creatTable(ChatMessageBean.class);
                Log.e("DBHelper", ChatMessageBean.class.getSimpleName()+"表已创建");
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
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }*/

}
