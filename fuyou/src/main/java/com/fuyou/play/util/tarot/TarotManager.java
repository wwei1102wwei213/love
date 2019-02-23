package com.fuyou.play.util.tarot;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Creacc on 2017/10/18.
 */

public class TarotManager {

    private static final String PATH_TAROT_CARDS_INFO = "tarot/TarotCardsInfo.plist";
    private static final String PATH_TAROT_LOVER_INFO = "tarot/TarotLoverInfo.plist";
    private static final String PATH_TAROT_YES_OR_NO = "tarot/TarotYesOrNo.plist";

    public static final int TAROT_CARD_GROUP_INDEX_PAST = 0;
    public static final int TAROT_CARD_GROUP_INDEX_CURRENT = 1;
    public static final int TAROT_CARD_GROUP_INDEX_FUTURE = 2;

    public static final int TAROT_LOVER_GROUP_INDEX_FIRST_LOVER = 0;
    public static final int TAROT_LOVER_GROUP_INDEX_SECOND_LOVER = 1;
    public static final int TAROT_LOVER_GROUP_INDEX_YOUR = 2;

    private AtomicBoolean mIsContentLoaded = new AtomicBoolean(false);
    private ArrayList<TarotCard>[] mTarotCardArray = new ArrayList[3];
    private ArrayList<String>[] mTarotLoverArray = new ArrayList[3];
    private ArrayList<TarotYesOrNo> mTarotYesOrNo = new ArrayList<>();

    public static TarotManager getInstance() {
        return InstanceContext.instanceObject;
    }

    private TarotManager() {

    }

    public void loadContent(final Context context) {
        if (mIsContentLoaded.compareAndSet(false, true)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadCardsInfo(context);
                        loadLoverInfo(context);
                        loadYesOrNo(context);
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public int getCardCount() {
        return mTarotYesOrNo.size();
    }

    public TarotYesOrNo getTarotYesOrNo(int index) {
        return mTarotYesOrNo.get(index);
    }

    public String getTarotLover(int groupIndex, int cardIndex) {
        return mTarotLoverArray[groupIndex].get(cardIndex);
    }

    public TarotCard getTarotCard(int groupIndex, int cardIndex) {
        return mTarotCardArray[groupIndex].get(cardIndex);
    }

    private void loadCardsInfo(Context context) throws XmlPullParserException, IOException {
        int groupIndex = 0;
        XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser pullParser = parserFactory.newPullParser();
        pullParser.setInput(context.getAssets().open(PATH_TAROT_CARDS_INFO), "UTF-8");
        for (int eventType = pullParser.next(); eventType != XmlPullParser.END_DOCUMENT; eventType = pullParser.next()) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    String name = pullParser.getName();
                    if ("key".equals(name)) {
                        String text = pullParser.nextText();
                        switch (text) {
                            case "past":
                                groupIndex = TAROT_CARD_GROUP_INDEX_PAST;
                                mTarotCardArray[groupIndex] = new ArrayList<>();
                                break;
                            case "current":
                                groupIndex = TAROT_CARD_GROUP_INDEX_CURRENT;
                                mTarotCardArray[groupIndex] = new ArrayList<>();
                                break;
                            case "future":
                                groupIndex = TAROT_CARD_GROUP_INDEX_FUTURE;
                                mTarotCardArray[groupIndex] = new ArrayList<>();
                                break;
                            case "name":
                                TarotCard card = new TarotCard();
                                card.setName(getTextBySkip(pullParser, 2));
                                card.setUprightContent(getTextBySkip(pullParser, 6));
                                card.setReversedContent(getTextBySkip(pullParser, 6));
                                mTarotCardArray[groupIndex].add(card);
                                break;
                        }
                    }
                    break;
            }
        }
    }

    private void loadLoverInfo(Context context) throws XmlPullParserException, IOException {
        int groupIndex = 0;
        XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser pullParser = parserFactory.newPullParser();
        pullParser.setInput(context.getAssets().open(PATH_TAROT_LOVER_INFO), "UTF-8");
        for (int eventType = pullParser.next(); eventType != XmlPullParser.END_DOCUMENT; eventType = pullParser.next()) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    String name = pullParser.getName();
                    if ("key".equals(name)) {
                        String text = pullParser.nextText();
                        switch (text) {
                            case "firstlover":
                                groupIndex = TAROT_LOVER_GROUP_INDEX_FIRST_LOVER;
                                mTarotLoverArray[groupIndex] = new ArrayList<>();
                                break;
                            case "secondlover":
                                groupIndex = TAROT_LOVER_GROUP_INDEX_SECOND_LOVER;
                                mTarotLoverArray[groupIndex] = new ArrayList<>();
                                break;
                            case "your":
                                groupIndex = TAROT_LOVER_GROUP_INDEX_YOUR;
                                mTarotLoverArray[groupIndex] = new ArrayList<>();
                                break;
                            case "Content":
                                mTarotLoverArray[groupIndex].add(getTextBySkip(pullParser, 2));
                                break;
                        }
                    }
                    break;
            }
        }
    }

    private void loadYesOrNo(Context context) throws XmlPullParserException, IOException {
        TarotYesOrNo current = null;
        XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser pullParser = parserFactory.newPullParser();
        pullParser.setInput(context.getAssets().open(PATH_TAROT_YES_OR_NO), "UTF-8");
        for (int eventType = pullParser.next(); eventType != XmlPullParser.END_DOCUMENT; eventType = pullParser.next()) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    String name = pullParser.getName();
                    if ("key".equals(name)) {
                        String text = pullParser.nextText();
                        switch (text) {
                            case "result":
                                if (current != null) {
                                    mTarotYesOrNo.add(current);
                                }
                                current = new TarotYesOrNo();
                                break;
                            case "Upright":
                                current.addUprightContent(getTextBySkip(pullParser, 2));
                                break;
                            case "Reversed":
                                current.addReversedContent(getTextBySkip(pullParser, 2));
                                break;
                        }
                    }
                    break;
            }
        }
        mTarotYesOrNo.add(current);
    }

    private String getTextBySkip(XmlPullParser parser, int count) throws IOException, XmlPullParserException {
        skipParser(parser, count);
        return parser.nextText();
    }

    private void skipParser(XmlPullParser pullParser, int count) throws IOException, XmlPullParserException {
        while (count-- > 0) {
            pullParser.next();
        }
    }

    private static class InstanceContext {

        private static final TarotManager instanceObject = new TarotManager();

    }
}
