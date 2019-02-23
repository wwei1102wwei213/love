package com.fuyou.play.bean.chat;

import android.text.TextUtils;

import com.fuyou.play.util.DecryptUtils;

/**
 * Created by Administrator on 2018-07-29.
 */

public class ChatMessageBean {

    private int Type, IsRead, ChatID, FromID, ToID;
    private String SendTime, Text, FromName, FromIcon;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getIsRead() {
        return IsRead;
    }

    public void setIsRead(int isRead) {
        IsRead = isRead;
    }

    public int getFromID() {
        return FromID;
    }

    public void setFromID(int fromID) {
        FromID = fromID;
    }

    public int getToID() {
        return ToID;
    }

    public void setToID(int toID) {
        ToID = toID;
    }

    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String sendTime) {
        SendTime = sendTime;
    }

    public String getText() {
        if (TextUtils.isEmpty(Text)) return "";
        return DecryptUtils.decodeBase64(Text);
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            Text = "";
        } else {
            Text = DecryptUtils.encode(text);
        }
    }

    public String getFromName() {
        if (TextUtils.isEmpty(FromName)) return "";
        return DecryptUtils.decodeBase64(FromName);
    }

    public void setFromName(String fromName) {
        if (TextUtils.isEmpty(fromName)) {
            FromName = "";
        } else {
            FromName = DecryptUtils.encode(fromName);
        }
    }

    public String getFromIcon() {
        return FromIcon;
    }

    public void setFromIcon(String fromIcon) {
        FromIcon = fromIcon;
    }

    public void setChatID(int chatID) {
        ChatID = chatID;
    }

    public int getChatID() {
        return ChatID;
    }
}
