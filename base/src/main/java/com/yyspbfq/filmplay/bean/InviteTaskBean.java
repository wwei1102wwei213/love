package com.yyspbfq.filmplay.bean;

import java.util.List;

public class InviteTaskBean {

    private List<LevelEntity> grabe;
    private List<LevelEntity> welfare;
    private LevelEntity day;

    public List<LevelEntity> getGrabe() {
        return grabe;
    }

    public List<LevelEntity> getWelfare() {
        return welfare;
    }

    public LevelEntity getDay() {
        return day;
    }


    public static class LevelEntity {
        private String name, level, remark, type, thumb;

        public String getName() {
            return name;
        }

        public String getLevel() {
            return level;
        }

        public String getRemark() {
            return remark;
        }

        public String getType() {
            return type;
        }

        public String getThumb() {
            return thumb;
        }
    }

}
