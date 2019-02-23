package com.fuyou.play.bean.login;

import com.fuyou.play.bean.BaseBean;
import com.fuyou.play.bean.UserInfo;

/**
 * Created by Administrator on 2018-07-11.
 */

public class LoginPhoneBean extends BaseBean{

    private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
