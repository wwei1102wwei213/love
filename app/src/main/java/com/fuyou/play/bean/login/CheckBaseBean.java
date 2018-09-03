package com.fuyou.play.bean.login;

import com.fuyou.play.bean.BaseBean;

/**
 * Created by Administrator on 2018-07-25.
 *
 * @author wwei
 */
public class CheckBaseBean extends BaseBean{

    private int check;
    private VersionConfigBean data;

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public VersionConfigBean getData() {
        return data;
    }

    public void setData(VersionConfigBean data) {
        this.data = data;
    }
}
