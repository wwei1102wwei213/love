package com.fuyou.play.bean.quizzes;

import java.util.List;

/**
 * Created by Ma on 2017/10/16.
 */

public class QuizzesAnswerBean {

    private String questionResult_name;
    private List<AnswersResultListBean> answersResultList;

    public String getQuestionResult_name() {
        return questionResult_name;
    }

    public void setQuestionResult_name(String questionResult_name) {
        this.questionResult_name = questionResult_name;
    }

    public List<AnswersResultListBean> getAnswersResultList() {
        return answersResultList;
    }

    public void setAnswersResultList(List<AnswersResultListBean> answersResultList) {
        this.answersResultList = answersResultList;
    }

    public static class AnswersResultListBean {
        /**
         * title :  Somewhat Aquarius.
         * answerCode : 1-5-9
         * resultContent : A true Aquarius would visit the suburbs, laugh, and go away. If you're laughing at the burbs while living there, that makes you somewhat Aquarius. Your best ideas come through emotions and experiences, although the drive to road-test your beliefs is certainly an Aquarian trait. You like keeping busy, but with so many irons in the fire, you have no interest in leadership. And if you did find yourself in that role, you'd ask for input from your favorite team players and go with that. Who cares if this creates discord? You know who your friends are.
         */

        private String title;
        private String answerCode;
        private String resultContent;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAnswerCode() {
            return answerCode;
        }

        public void setAnswerCode(String answerCode) {
            this.answerCode = answerCode;
        }

        public String getResultContent() {
            return resultContent;
        }

        public void setResultContent(String resultContent) {
            this.resultContent = resultContent;
        }
    }
}
