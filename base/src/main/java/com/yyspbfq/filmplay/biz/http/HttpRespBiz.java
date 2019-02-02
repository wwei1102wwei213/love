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

        }
        return result;
    }

    @Override
    protected String postUrl() {
        String result = null;
        switch (flag) {
            case HttpFlag.FLAG_UPLOAD_IMG:
                result = HttpFlag.URL_UPLOAD_IMG;
                break;
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
            case HttpFlag.FLAG_LEVEL_EXCHANGE:
                result = HttpFlag.URL_LEVEL_EXCHANGE;
                break;
            case HttpFlag.FLAG_FEEDBACK_SEND:
                result = HttpFlag.URL_FEEDBACK_SEND;
                break;
            case HttpFlag.FLAG_USER_EXIT:
                result = HttpFlag.URL_USER_EXIT;
                break;
            case HttpFlag.FLAG_ADVERT_FIRST_SCREEN:
                result = HttpFlag.URL_ADVERT_FIRST_SCREEN;
                break;
            case HttpFlag.FLAG_ADVERT_INDEX:
                result = HttpFlag.URL_ADVERT_INDEX;
                break;
            case HttpFlag.FLAG_ADVERT_USER_CENTER:
                result = HttpFlag.URL_ADVERT_USER_CENTER;
                break;
            case HttpFlag.FLAG_ADVERT_VIDEO_ABOVE:
                result = HttpFlag.URL_ADVERT_VIDEO_ABOVE;
                break;
            case HttpFlag.FLAG_ADVERT_VIDEO_DETAIL:
                result = HttpFlag.URL_ADVERT_VIDEO_DETAIL;
                break;
            case HttpFlag.FLAG_UPDATE_INFO:
                result = HttpFlag.URL_UPDATE_INFO;
                break;
            case HttpFlag.FLAG_MESSAGE_SHOW:
                result = HttpFlag.URL_MESSAGE_SHOW;
                break;
            case HttpFlag.FLAG_INFO_COLLATION:
                result = HttpFlag.URL_INFO_COLLATION;
                break;
            case HttpFlag.FLAG_DELETE_COLLATION:
                result = HttpFlag.URL_DELETE_COLLATION;
                break;
            case HttpFlag.FLAG_DELETE_VIDEO_RECORD:
                result = HttpFlag.URL_DELETE_VIDEO_RECORD;
                break;
            case HttpFlag.FLAG_SET_VIDEO_RECORD:
                result = HttpFlag.URL_SET_VIDEO_RECORD;
                break;
            case HttpFlag.FLAG_SYNC_VIDEO_RECORD:
                result = HttpFlag.URL_SYNC_VIDEO_RECORD;
                break;
            case HttpFlag.FLAG_DEDUCTION_COIN:
                result = HttpFlag.URL_DEDUCTION_COIN;
                break;
            case HttpFlag.FLAG_COMMENT_LIST:
                result = HttpFlag.URL_COMMENT_LIST;
                break;
            case HttpFlag.FLAG_COMMENT_SENT:
                result = HttpFlag.URL_COMMENT_SENT;
                break;
            case HttpFlag.FLAG_COMMENT_LIKE:
                result = HttpFlag.URL_COMMENT_LIKE;
                break;
            case HttpFlag.FLAG_INFO_HELP:
                result = HttpFlag.URL_INFO_HELP;
                break;
        }
        return result;
    }

}
