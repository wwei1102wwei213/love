package com.fuyou.play.bean.quizzes;

import java.util.List;

/**
 * Created by Ma on 2017/10/16.
 */

public class QuizzesQuestionBean {

    /**
     * subTopicImage : Are you a true Aquarius.jpg
     * allQuestion : [{"questionResult_name":"Are you a true Aquarius?","explanation":"Does your personality match up to your given Sun Sign? If not, you might be more in tune with your Rising or Moon Sign!","answers":[{"text":"In a bustling city.","indentifer":"1"},{"text":"By the water.","indentifer":"2"},{"text":"Deep in the woods where it's very quiet.","indentifer":"3"},{"text":"In the suburbs.","indentifer":"4"}],"image":"","text":"You'd rather live:"},{"questionResult_name":"Are you a true Aquarius?","explanation":"Does your personality match up to your given Sun Sign? If not, you might be more in tune with your Rising or Moon Sign!","answers":[{"text":"Logic.","indentifer":"5"},{"text":"Emotions and experiences.","indentifer":"6"},{"text":"Intuition.","indentifer":"7"},{"text":"A little of everything.","indentifer":"8"}],"image":"","text":"You make decisions based on:"},{"questionResult_name":"Are you a true Aquarius?","explanation":"Does your personality match up to your given Sun Sign? If not, you might be more in tune with your Rising or Moon Sign!","answers":[{"text":"Be in charge of everything.","indentifer":"9"},{"text":"Lead a team that works together.","indentifer":"10"},{"text":"Work alone, and be left alone.","indentifer":"11"},{"text":"Be involved in a lot of things, but not as the leader.","indentifer":"12"}],"image":"","text":"At work, you'd rather:"}]
     */

    private String subTopicImage;
    private List<AllQuestionBean> allQuestion;

    public String getSubTopicImage() {
        return subTopicImage;
    }

    public void setSubTopicImage(String subTopicImage) {
        this.subTopicImage = subTopicImage;
    }

    public List<AllQuestionBean> getAllQuestion() {
        return allQuestion;
    }

    public void setAllQuestion(List<AllQuestionBean> allQuestion) {
        this.allQuestion = allQuestion;
    }

    public static class AllQuestionBean {
        /**
         * questionResult_name : Are you a true Aquarius?
         * explanation : Does your personality match up to your given Sun Sign? If not, you might be more in tune with your Rising or Moon Sign!
         * answers : [{"text":"In a bustling city.","indentifer":"1"},{"text":"By the water.","indentifer":"2"},{"text":"Deep in the woods where it's very quiet.","indentifer":"3"},{"text":"In the suburbs.","indentifer":"4"}]
         * image :
         * text : You'd rather live:
         */

        private String questionResult_name;
        private String explanation;
        private String image;
        private String text;
        private List<AnswersBean> answers;

        public String getQuestionResult_name() {
            return questionResult_name;
        }

        public void setQuestionResult_name(String questionResult_name) {
            this.questionResult_name = questionResult_name;
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<AnswersBean> getAnswers() {
            return answers;
        }

        public void setAnswers(List<AnswersBean> answers) {
            this.answers = answers;
        }

        public static class AnswersBean {
            /**
             * text : In a bustling city.
             * indentifer : 1
             */

            private String text;
            private String indentifer;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getIndentifer() {
                return indentifer;
            }

            public void setIndentifer(String indentifer) {
                this.indentifer = indentifer;
            }
        }
    }
}
