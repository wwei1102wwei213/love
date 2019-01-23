package com.yyspbfq.filmplay.biz.download;

import android.util.Log;

import com.yyspbfq.filmplay.utils.tools.ToastUtils;
import com.wei.wlib.thread.WLibThreadPoolManager;

import java.util.HashMap;
import java.util.LinkedList;

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

    public void addTask(String key, String url, String fileName, Object tag) {
        if ( key == null || url == null ) return;
        if ( map == null ){
            map = new HashMap<>();
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
        DownloadTask task = new DownloadTask(url, fileName, tag);
        Log.e(TAG,"DownloadTaskManager====> map.put( key, task ):"+key);
        map.put( key, task );
        if ( count >= MAX_TASK ) {
            if ( waitList == null ) {
                waitList = new LinkedList<>();
            }
            Log.e(TAG,"DownloadTaskManager====> waitList.add(key):"+key);
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
            if (map.get(key).isWork()) {
                map.get(key).cancelDownload();
            }
            map.remove(key);
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
