package com.yyspbfq.filmplay.bean;

import com.yyspbfq.filmplay.utils.ShareCodeUtils;

public class UserInfo {

    public String id;
    public String name;
    public String mobile;
    public String sex;
    public String avatar;
    public String status;
    public String device;
    public String ip;
    public String vip_level;
    public String balance;
    public String max;
    public String tourist_max;
    public String invite_num;
    public String is_copy;
    public String cdate;
    public String logindate;
    public String pid;
    public String down_max;
    public String tourist_down_max;
    public String downed;
    /*"canWatch": 0,  //可用观看次数
            "maxWatch": "0", //最大观看次数
            "canDown": 0, //可用缓存次数
            "maxDown": "0", //最大缓存次数*/
    public String canWatch, maxWatch, canDown, maxDown;
    public UserGrade grade;
    public String type;

    public String getShareCodeID() {
        try {
            long temp = Long.parseLong(id);
            String shareCodeID = ShareCodeUtils.toSerialCode(temp);
            return shareCodeID==null?"":shareCodeID.toUpperCase();
        } catch (Exception e){

        }
        return "";
    }

    public String getNowLevelName() {
        if (grade!=null&&grade.now!=null&&grade.now.name!=null) {
            String temp = grade.now.name;
            temp = temp.replace("徽章", "");
            return "L" + grade.now.level + temp;
        }
        return "";
    }

    public String getNextLevelName() {
        if (grade!=null&&grade.next!=null&&grade.next.name!=null) {
            String temp = grade.next.name;
            temp = temp.replace("徽章", "");
            return "L" + grade.next.level + temp;
        }
        return "";
    }

    public int getNowNum() {
        try {
            if (grade!=null&&grade.now!=null&&grade.now.num!=null) {
                return Integer.parseInt(grade.now.num);
            }
        } catch (Exception e){

        }
        return 0;
    }

    public int getNextNum() {
        try {
            if (grade!=null&&grade.next!=null&&grade.next.num!=null) {
                return Integer.parseInt(grade.next.num);
            }
        } catch (Exception e){

        }
        return 0;
    }

    /*"now": {
                    "name": "小白",
                    "level": 0,
                    "num": 0,
                    "thumb": "http://p.xxxxxxxx.com/static/api/images/ic_level0.png"
                },
                "next": {
                    "name": "vip1",
                    "level": "1",
                    "num": "1",
                    "thumb": "http://p.xxxxxxxx.com/uploads/default/201811/15/default201811151151302082.png"
                }*/
    public static class UserGrade {
        public GradeItem now, next;
        public static class GradeItem {
            public String name, thumb, level, num;
        }
    }
}
