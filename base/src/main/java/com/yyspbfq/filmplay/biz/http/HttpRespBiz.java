package com.yyspbfq.filmplay.biz.http;

import com.wei.wlib.http.WLibDefaultHttpBiz;
import com.wei.wlib.http.WLibHttpListener;

public class HttpRespBiz extends WLibDefaultHttpBiz{

    public HttpRespBiz(int flag, Object tag, WLibHttpListener callback, Class<?> mClass) {
        super(flag, tag, callback, mClass);
    }

    public HttpRespBiz(int flag, Object tag, WLibHttpListener callback, Class<?> mClass, int codeType) {
        super(flag, tag, callback, mClass, codeType);
    }

    @Override
    protected String getUrl() {
        String result = null;
        switch (flag) {
            /*case HttpFlag.FLAG_LOGIN_MOBILE:
                result = HttpFlag.URL_LOGIN_MOBILE;
                break;
            case HttpFlag.FLAG_VIDEO_DETAIL:
                result = HttpFlag.URL_VIDEO_DETAIL;
                break;
            case HttpFlag.FLAG_VIDEO_DETAIL_LIKE:
                result = HttpFlag.URL_VIDEO_DETAIL_LIKE;
                break;
            case HttpFlag.FLAG_VIDEO_COLLECTION:
                result = HttpFlag.URL_VIDEO_COLLECTION;
                break;
            case HttpFlag.FLAG_VIDEO_SET_LIKE:
                result = HttpFlag.URL_VIDEO_SET_LIKE;
                break;*/
        }
        return result;
    }

    @Override
    protected String postUrl() {
        String result = null;
        switch (flag) {
            case HttpFlag.FLAG_LOGIN_MOBILE:
                result = HttpFlag.URL_LOGIN_MOBILE;
                break;
            case HttpFlag.FLAG_VIDEO_DETAIL:
                result = HttpFlag.URL_VIDEO_DETAIL;
                break;
            case HttpFlag.FLAG_VIDEO_DETAIL_LIKE:
                result = HttpFlag.URL_VIDEO_DETAIL_LIKE;
                break;
            case HttpFlag.FLAG_VIDEO_COLLECTION:
                result = HttpFlag.URL_VIDEO_COLLECTION;
                break;
            case HttpFlag.FLAG_VIDEO_SET_LIKE:
                result = HttpFlag.URL_VIDEO_SET_LIKE;
                break;
            case HttpFlag.FLAG_HOME_MENU_COLUMN:
                result = HttpFlag.URL_HOME_MENU_COLUMN;
                break;
            case HttpFlag.FLAG_HOME_LIKE_VIDEO:
                result = HttpFlag.URL_HOME_LIKE_VIDEO;
                break;
            case HttpFlag.FLAG_HOME_NEWEST_VIDEO:
                result = HttpFlag.URL_HOME_NEWEST_VIDEO;
                break;
            case HttpFlag.FLAG_HOME_HOT_VIDEO:
                result = HttpFlag.URL_HOME_HOT_VIDEO;
                break;
            case HttpFlag.FLAG_HOME_CLASSIFY_LIST:
                result = HttpFlag.URL_HOME_CLASSIFY_LIST;
                break;
            case HttpFlag.FLAG_HOME_SLIDE:
                result = HttpFlag.URL_HOME_SLIDE;
                break;
            case HttpFlag.FLAG_MAIN_DISCOVER:
                result = HttpFlag.URL_MAIN_DISCOVER;
                break;
            case HttpFlag.FLAG_CHANNEL_RECOMMEND:
                result = HttpFlag.URL_CHANNEL_RECOMMEND;
                break;
            case HttpFlag.FLAG_CHANNEL_HOT:
                result = HttpFlag.URL_CHANNEL_HOT;
                break;
            case HttpFlag.FLAG_CHANNEL_HUMAN:
                result = HttpFlag.URL_CHANNEL_HUMAN;
                break;
            case HttpFlag.FLAG_CHANNEL_LIST:
                result = HttpFlag.URL_CHANNEL_LIST;
                break;
            case HttpFlag.FLAG_CHANNEL_DETAIL_LIST:
                result = HttpFlag.URL_CHANNEL_DETAIL_LIST;
                break;
            case HttpFlag.FLAG_CHANNEL_DETAIL:
                result = HttpFlag.URL_CHANNEL_DETAIL;
                break;
            case HttpFlag.FLAG_ALL_CLASSIFY:
                result = HttpFlag.URL_ALL_CLASSIFY;
                break;
            case HttpFlag.FLAG_CLASSIFY_LIST:
                result = HttpFlag.URL_CLASSIFY_LIST;
                break;
            case HttpFlag.FLAG_SEARCH_HOT:
                result = HttpFlag.URL_SEARCH_HOT;
                break;
            case HttpFlag.FLAG_SEARCH_KEYWORD:
                result = HttpFlag.URL_SEARCH_KEYWORD;
                break;
            case HttpFlag.FLAG_USER_INFO:
                result = HttpFlag.URL_USER_INFO;
                break;
            case HttpFlag.FLAG_INVITE_TASK:
                result = HttpFlag.URL_INVITE_TASK;
                break;
            case HttpFlag.FLAG_INVITE_LIST:
                result = HttpFlag.URL_INVITE_LIST;
                break;
            case HttpFlag.FLAG_INVITE_CLICK_ADS:
                result = HttpFlag.URL_INVITE_CLICK_ADS;
                break;
            case HttpFlag.FLAG_INVITE_SAVE_CODE:
                result = HttpFlag.URL_INVITE_SAVE_CODE;
                break;
            case HttpFlag.FLAG_INVITE_CODE_MSG:
                result = HttpFlag.URL_INVITE_CODE_MSG;
                break;
        }
        return result;
    }

}
