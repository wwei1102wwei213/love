package com.fuyou.play.biz.callback;

        import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public interface ChooseTimeInterface extends Serializable {

    /**
     *
     * @param time 返回的串
     * @param flag 1:日期选择 2：分秒选择
     */
    void timeCallBack(String time, int flag);

}
