package com.yyspbfq.filmplay.bean;

import android.text.TextUtils;

import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.utils.BLog;

import java.util.List;

public class SearchBean {

    private List<VideoEntity> data;
    private String count;
    private int size;

    public List<VideoEntity> getSearchData() {
        return data;
    }

    public void setSearchData(List<VideoEntity> searchData) {
        this.data = searchData;
    }

    public String getSearchCount() {
        return count;
    }

    public void setSearchCount(String searchCount) {
        this.count = searchCount;
    }

    public int getCount() {
        if (TextUtils.isEmpty(count)) return 0;
        int result = 0;
        try {
            result = Integer.parseInt(count);
        } catch (Exception e){
            BLog.e(e);
        }
        return result;
    }

    public int getSize() {
        return size;
    }
}
