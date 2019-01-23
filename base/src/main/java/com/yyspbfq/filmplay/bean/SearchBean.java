package com.yyspbfq.filmplay.bean;

import android.text.TextUtils;

import com.yyspbfq.filmplay.db.VideoEntity;
import com.yyspbfq.filmplay.utils.BLog;

import java.util.List;

public class SearchBean {

    private List<VideoEntity> searchData;
    private String searchCount;
    private int size;

    public List<VideoEntity> getSearchData() {
        return searchData;
    }

    public void setSearchData(List<VideoEntity> searchData) {
        this.searchData = searchData;
    }

    public String getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(String searchCount) {
        this.searchCount = searchCount;
    }

    public int getCount() {
        if (TextUtils.isEmpty(searchCount)) return 0;
        int result = 0;
        try {
            result = Integer.parseInt(searchCount);
        } catch (Exception e){
            BLog.e(e);
        }
        return result;
    }

    public int getSize() {
        return size;
    }
}
