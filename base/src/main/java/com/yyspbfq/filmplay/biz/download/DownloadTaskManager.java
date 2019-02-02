package com.yyspbfq.filmplay.biz.download;

import android.util.Log;

import com.wei.wlib.thread.WLibThreadPoolManager;
import com.yyspbfq.filmplay.BaseApplication;
import com.yyspbfq.filmplay.bean.MessageEvent;
import com.yyspbfq.filmplay.db.DBHelper;
import com.yyspbfq.filmplay.db.VideoDownloadBean;
import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.utils.tools.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DownloadTaskManager {

    private static final String TAG = "DownloadTaskManager";

    private HashMap<String, DownloadTask> map;
    private LinkedList<String> waitList;

    private static final int MAX_TASK = 3;

    private int count = 0;

    private static DownloadTaskManager instance;

    public static DownloadTaskManager getInstance() {
        if (instance==null) {
            synchronized (DownloadTaskManager.class) {
                if (instance==null) {
                    instance = new DownloadTaskManager();
                }
            }
        }
        return instance;
    }

    public int getCurrentCount() {
        return count;
    }

    public void addTask(String key, String url, String fileName, VideoEntity entity) {
        if ( key == null || url == null ) return;
        if ( map == null ){
            map = new HashMap<>();
        }
        VideoDownloadBean temp = DBHelper.getInstance().getDownloadRecordByKey(BaseApplication.getInstance(), key);
        if (temp!=null&&temp.getState()==99) {
            ToastUtils.showToast("已缓存过该视频");
            return;
        }
        if ( map.get(key) != null ) {
            if ( map.get(key).isWork() ) {
                ToastUtils.showToast("下载任务已经存在");
                return;
            } else {
                Log.e(TAG,"DownloadTaskManager====> map.remove(key):"+key);
                map.remove(key);
            }
        }
        VideoDownloadBean bean = new VideoDownloadBean();
        bean.setVid(entity.getId());
        bean.setCreate_time(System.currentTimeMillis());
        bean.setVideo_time(entity.getVideo_time());
        bean.setVideo_thumb(entity.getVideo_thump());
        bean.setName(entity.getName());
        bean.setDownload_url(url);
        DBHelper.getInstance().insertDownloadRecord(BaseApplication.getInstance(), bean);
        DownloadTask task = new DownloadTask(url, fileName, bean);
        Log.e(TAG,"DownloadTaskManager====> map.put( key, task ):"+key);
        map.put( key, task );
        if ( count >= MAX_TASK ) {
            if ( waitList == null ) {
                waitList = new LinkedList<>();
            }
            Log.e(TAG,"DownloadTaskManager====> waitList.add(key):"+key);
            ToastUtils.showToast("下载任务数量过多,进入等待列表");
            waitList.add(key);
        } else {
            Log.e(TAG,"DownloadTaskManager====> WLibThreadPoolManager.execute(task):"+key);
            ToastUtils.showToast("下载任务添加成功");
            WLibThreadPoolManager.execute(task);
        }
    }

    public void addTask(String key, String url, String fileName, VideoDownloadBean entity) {
        if ( key == null || url == null ) return;
        if ( map == null ){
            map = new HashMap<>();
        }
        VideoDownloadBean temp = DBHelper.getInstance().getDownloadRecordByKey(BaseApplication.getInstance(), key);
        if (temp!=null&&temp.getState()==99) {
            ToastUtils.showToast("已缓存过该视频");
            return;
        }
        if ( map.get(key) != null ) {
            if ( map.get(key).isWork() ) {
                ToastUtils.showToast("下载任务已经存在");
                return;
            } else {
                Log.e(TAG,"DownloadTaskManager====> map.remove(key):"+key);
                map.remove(key);
            }
        }
        DBHelper.getInstance().insertDownloadRecord(BaseApplication.getInstance(), entity);
        DownloadTask task = new DownloadTask(url, fileName, entity);
        Log.e(TAG,"DownloadTaskManager====> map.put( key, task ):"+key);
        map.put( key, task );
        if ( count >= MAX_TASK ) {
            if ( waitList == null ) {
                waitList = new LinkedList<>();
            }
            Log.e(TAG,"DownloadTaskManager====> waitList.add(key):"+key);
            ToastUtils.showToast("下载任务数量过多,进入等待列表");
            waitList.add(key);
        } else {
            Log.e(TAG,"DownloadTaskManager====> WLibThreadPoolManager.execute(task):"+key);
            WLibThreadPoolManager.execute(task);
        }
    }

    public void addTask(String key, DownloadTask task) {
        if ( key == null || task == null ) return;
        if ( map == null ){
            map = new HashMap<>();
        }
        if ( map.get(key) != null ) {
            if ( map.get(key).isWork() ) {
                ToastUtils.showToast("下载任务已经存在");
                return;
            } else {
                map.remove(key);
            }
        }
        map.put( key, task );
        if ( count >= MAX_TASK ) {
            if ( waitList == null ) {
                waitList = new LinkedList<>();
            }
            waitList.add(key);
        } else {
            WLibThreadPoolManager.execute(task);
        }
    }

    public void pauseTask(String key) {
        if (map==null) return;
        if (map.get(key)!=null&&map.get(key).isWork()) {
            Log.e(TAG,"DownloadTaskManager====> pauseTask:"+key);
            map.get(key).pauseDownload();
        }
    }

    public void cancelTask(String key) {
        if (map==null) return;
        if (map.get(key)!=null) {
            Log.e(TAG,"DownloadTaskManager====> cancelTask:"+key);
            DBHelper.getInstance().deleteDownloadRecord(BaseApplication.getInstance(), key);
            MessageEvent event = new MessageEvent();
            event.setMessage(MessageEvent.MSG_DOWNLOAD_VIDEO);
            event.setFlag(DownloadTask.DOWNLOAD_HANDLE_CANCEL);
            EventBus.getDefault().post(event);
            if (map.get(key).isWork()) {
                map.get(key).cancelDownload();
            }
            map.remove(key);

        }
    }

    public void cancelTask(List<String> keys) {
        DBHelper.getInstance().deleteDownloadRecord(BaseApplication.getInstance(), keys);
        MessageEvent event = new MessageEvent();
        event.setMessage(MessageEvent.MSG_DOWNLOAD_VIDEO);
        event.setFlag(DownloadTask.DOWNLOAD_HANDLE_CANCEL);
        EventBus.getDefault().post(event);
        if (map==null) return;
        for (String key:keys) {
            if (map.get(key)!=null) {
                if (map.get(key).isWork()) {
                    map.get(key).cancelDownload();
                }
                map.remove(key);
            }
        }
    }


    public int checkState(String key) {
        if (map==null||map.get(key)==null) {
            return -1;//等待开始
        } else {
            return map.get(key).getStatus();
        }
    }

    public void changeTaskNum(boolean isAdd) {
        if (isAdd) {
            count++;
        } else {
            if (count>0) count--;
            if (count<3 && waitList!=null && waitList.size()>0) {
                WLibThreadPoolManager.execute(
                        map.get(waitList.removeFirst()));
            }
        }
        MessageEvent event = new MessageEvent();
        event.setMessage(MessageEvent.MSG_DOWNLOAD_VIDEO);
        event.setFlag(DownloadTask.DOWNLOAD_HANDLE_CHANGE_COUNT);
        EventBus.getDefault().post(event);
    }

    public VideoDownloadBean getTagByKey(String key) {
        if (key==null || map == null || map.get(key) == null) return null;
        return map.get(key).getTag();
    }

    public void removeTask(String key) {
        if ( map==null || !map.containsKey(key)) return;
        WLibThreadPoolManager.cancel(map.remove(key));
    }

    public void clear() {
        if (map!=null) {
            map.clear();
        }
    }

}
