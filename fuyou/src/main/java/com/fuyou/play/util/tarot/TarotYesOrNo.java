package com.fuyou.play.util.tarot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Creacc on 2017/10/18.
 */

public class TarotYesOrNo {

    public static final String TAROT_YES = "YES";
    public static final String TAROT_NO = "NO";

    private List<String> mUprightContents = new ArrayList<>();

    private List<String> mReversedContents = new ArrayList<>();

    void addUprightContent(String content) {
        mUprightContents.add(content);
    }

    void addReversedContent(String content) {
        mReversedContents.add(content);
    }

    public String[] getContent() {
        String[] content = new String[2];
        if (Math.abs(new Random().nextInt()) % 2 == 0) {
            content[0] = TAROT_YES;
            content[1] = getContent(mUprightContents);
        } else {
            content[0] = TAROT_NO;
            content[1] = getContent(mReversedContents);
        }
        return content;
    }

    private String getContent(List<String> contents) {
        if (contents.isEmpty()) {
            return "";
        }
        if (contents.size() > 1) {
            return contents.get(Math.abs(new Random().nextInt()) % contents.size());
        }
        return contents.get(0);
    }
}
