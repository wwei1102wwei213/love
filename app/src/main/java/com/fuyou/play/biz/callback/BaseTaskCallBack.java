package com.fuyou.play.biz.callback;

/**
 * #
 *
 * @author wwei
 */
public interface BaseTaskCallBack {

    /**
     * Task返回
     * @param object 返回数据,数据具体类型需强制转型
     * @param tag 接口标识
     * @param flag 返回标识
     */
    void resultTaskData(Object object, int tag, int flag);

}
