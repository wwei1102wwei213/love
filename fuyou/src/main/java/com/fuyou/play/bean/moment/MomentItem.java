package com.fuyou.play.bean.moment;

import java.util.List;

/**
 * Created by Administrator on 2018-08-17.
 */

public class MomentItem extends MomentComment{

    private List<MomentUrl> images;
    private List<MomentComment> comments;
    private String error;

    public List<MomentUrl> getImages() {
        return images;
    }

    public List<MomentComment> getComments() {
        return comments;
    }

    public String getError() {
        return error;
    }
}
