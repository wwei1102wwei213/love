package com.yyspbfq.filmplay.bean;

import java.util.List;

public class InviteListBean {

    private List<UserInvitee> data;
    private int size;

    public List<UserInvitee> getData() {
        return data;
    }

    public void setData(List<UserInvitee> data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
