package com.fuyou.play.bean.discuss;

import com.fuyou.play.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018-07-26.
 */
public class DiscussBaseBean extends BaseBean{

    private List<DiscussEntity> data;

    public List<DiscussEntity> getData() {
        return data;
    }

    public void setData(List<DiscussEntity> data) {
        this.data = data;
    }
}
