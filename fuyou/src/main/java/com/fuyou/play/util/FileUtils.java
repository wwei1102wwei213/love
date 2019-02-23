package com.fuyou.play.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FileUtils {

    /**
     * 获取Assets文件数据
     * @param context
     * @param fileName
     * @return
     */
    public static String getStringFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            inputReader.close();
            bufReader.close();
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * uri 获取文件路径
     * @param context
     * @param uri
     * @return
     */
    public static String getPhotoPathFromContentUri(Context context, Uri uri) {
        String photoPath = "";
        if(context == null || uri == null) {
            return photoPath;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if(isExternalStorageDocument(uri)) {
                String[] split = docId.split(":");
                if(split.length >= 2) {
                    String type = split[0];
                    if("primary".equalsIgnoreCase(type)) {
                        photoPath = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
            }
            else if(isDownloadsDocument(uri)) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                photoPath = getDataColumn(context, contentUri, null, null);
            }
            else if(isMediaDocument(uri)) {
                String[] split = docId.split(":");
                if(split.length >= 2) {
                    String type = split[0];
                    Uri contentUris = null;
                    if("image".equals(type)) {
                        contentUris = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    }
                    else if("video".equals(type)) {
                        contentUris = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    }
                    else if("audio".equals(type)) {
                        contentUris = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    String selection = MediaStore.Images.Media._ID + "=?";
                    String[] selectionArgs = new String[] { split[1] };
                    photoPath = getDataColumn(context, contentUris, selection, selectionArgs);
                }
            }
        }
        else if("file".equalsIgnoreCase(uri.getScheme())) {
            photoPath = uri.getPath();
        }
        else {
            photoPath = getDataColumn(context, uri, null, null);
        }

        return photoPath;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return null;
    }

    /**
     * 根据星座获取星座属性
     * @param str
     * @return
     */
    public static String getElementForSign(String str){
        String result = "Fire";
        switch (str){
            case "Pis":
            case "Can":
            case "Sco":
                result = "Water";
                break;
            case "Vir":
            case "Cap":
            case "Tau":
                result = "Earth";
                break;
            case "Gem":
            case "Aqu":
            case "Lib":
                result = "Air";
                break;
        }
        return result;
    }

    /**
     * 根据星座获取星座模式
     * @param str
     * @return
     */
    public static String getQualityForSign(String str){
        String result = "Cardinal";
        switch (str){
            case "Sco":
            case "Leo":
            case "Tau":
            case "Aqu":
                result = "Fixed";
                break;
            case "Gem":
            case "Sag":
            case "Pis":
            case "Vir":
                result = "Mutable";
                break;
        }
        return result;
    }

    /**
     * 根据星座获取守护星座
     * @param str
     * @return
     */
    public static String getRulerForSign(String str){
        String result = "Mars";
        switch (str){
            case "Ari":
                result = "Mars";
                break;
            case "Tau":
                result = "Venus";
                break;
            case "Gem":
                result = "Mercury";
                break;
            case "Can":
                result = "Moon";
                break;
            case "Leo":
                result = "Sun";
                break;
            case "Vir":
                result = "Mercury";
                break;
            case "Lib":
                result = "Venus";
                break;
            case "Sco":
                result = "Pluto";
                break;
            case "Sag":
                result = "Jupiter";
                break;
            case "Cap":
                result = "Saturn";
                break;
            case "Aqu":
                result = "Uranus";
                break;
            case "Pis":
                result = "Neptune";
                break;
        }
        return result;
    }

    public static List<String> getYearData2018(){
        List<String> temp = new ArrayList<>();
        temp.add("You’ll continue to gain distinction between January 1st and May 14th, when original Uranus will be touring your Image House. Setting yourself apart from the crowd will cultivate success during this golden period. Starting on May 15th, it’s possible you’ll generate income on a freelance basis, thanks to independent Uranus moving into your Earned Income House. A romantic relationship should be a source of great joy for the first ten months of the year, when expansive Jupiter makes its way through your Intimacy House between January 1st and November 7th. If you’re in a relationship, you’ll fall in love with your partner all over again. Are you single? You could meet someone special at an antique shop, environmental group, or nightclub. Your professional success may soar this year, thanks to ambitious Saturn touring your Career House all year.  Bravo!");
        temp.add("After experiencing a surprising spiritual transformation during the first five months of 2018, you might undergo a dramatic makeover after May 15th, when original Uranus enters your Appearance House. It’s possible you’ll undergo cosmetic surgery or adopt a totally new fashion sense. Your love life shows tremendous promise for the first ten months of the year, when joyful Jupiter makes its way through your Partnership House. If you’re in a relationship, your other half may be extra loving and affectionate. Are you single? You may meet an intelligent, generous person who falls head over heels for you. On November 8th, our luck will shift to your Intimacy House, helping you to merge hearts and minds with your other half. Accomplished Saturn moves through your Higher Education House all year, making it a good year to earn an advanced degree.");
        temp.add("Some jolts in your social life could prompt you to undergo some spiritual changes. This transition may start in mid-May, when independent Uranus will move into your Seclusion House. Instead of looking to your friends for guidance, you’ll start trusting your intuition more. Work will continue to be a source of profound pleasure for the first ten months of the year, thanks to jolly Jupiter’s tour of your Sixth House of Employment. Starting on November 8th, your luck will flourish on the romantic front; that’s when expansive Jupiter moves into your Partnership House. If you’re single, you could meet your soulmate during the final months of the year. Do you have a partner? You may fall even deeper in love. Serious Saturn moves through your Eighth House of Intimacy, causing you to be more cautious about sharing secrets.");
        temp.add("Your professional life will continue to feel like a rollercoaster ride for the first five months of the year, thanks to disruptive Uranus moving through your Tenth House of Career. Work should stabilize starting on May 15th, when independent Uranus moves into your Friendship House. Be open to making exciting alliances with people who are highly unusual and extraordinarily talented. Upbeat Jupiter moves through your Romance House for the first ten months of the year, urging you to pursue your desire for love. If you’re single, you could meet someone special on an overseas trip or while taking an advanced class. If you have a partner, it will feel like you’re enjoying a prolonged honeymoon. Serious Saturn tours your Relationship House all year, possibly prompting you to make an exclusive commitment to a romantic or business partner.");
        temp.add("Your career will assume an exciting tone in 2018, when unpredictable Uranus moves into your Professional House on May 15th. Instead of traveling incessantly on business, you could be given a permanent assignment at a cutting-edge company. Lucky Jupiter continues its tour of your Domesticity House for the first ten months of the year. There’s never been a better time to find a big, beautiful home in a quiet neighborhood. On November 8th, your luck will shift to your love life, when expansive Jupiter enters your Fifth House of Romance. If you’re single, you may meet the love of your life at a holiday party. Do you have a partner? It could be impossible to contain your mutual passion. Dedicated Saturn moves through your Health House all year, suggesting you will benefit from a steady fitness regimen.");
        temp.add("A close relationship will be a continual source of surprises for the first five months of the year, when unpredictable Uranus moves through your Eighth House of Intimacy. By May 15th, this union should either break apart or stabilize. That’s when unique Uranus moves into your Higher Knowledge House. An opportunity to get an advanced degree in an unconventional field is a strong possibility. Generous Jupiter continues its tour of your Communication House, urging you to share your ideas with the world. Writing articles, making blog posts and recording podcasts could all yield fruit for the first ten months of 2018. On November 8th, expansive Jupiter enters your Domestic House, suggesting you might move to a big, beautiful place you can easily afford. Serious Saturn tours your Romance House all year, warning against casual flirtations.");
        temp.add("Creating a stable home life is critical in 2018. That’s because serious Saturn is touring your Fourth House of Domesticity. Putting a down payment on a home, moving closer to a thriving job center, or reducing your living expenses are all options. Although your love life keeps undergoing twists and turns, the drama could end in mid-May. That’s when disruptive Uranus leaves your Partnership House and moves into your Eighth House of Transformation. If you’re in a relationship, you may decide to either break things off altogether or make a serious commitment to your partner. Are you single? Instead of clinging to your independence, you could become more open to an exclusive relationship.  Generous Jupiter blesses you with a multitude of moneymaking opportunities for the first ten months of the year; use this golden period to increase your earned income.");
        temp.add("Lucky Jupiter continues its tour of your sign for the first ten months of the year, putting you squarely in the spotlight. This is an extraordinary opportunity to earn fame and acclaim in your desired field. While you’re busy making headlines, original Uranus keeps shaking things up in your Health House. Give your body the nourishing foods it needs to flourish; don’t forget to get enough sleep and take regular exercise, too. After May 15th, your health could stabilize; that’s when Uranus moves into your Partnership House. If you’re in a relationship, your other half may need to relocate for work. Are you single? You could be drawn to someone who is unlike anyone you’ve ever admired in the past. On November 8th, expansive Jupiter moves into your Earned Income House, attracting fabulous moneymaking opportunities.");
        temp.add("2018 will mark an exciting turning point for you. For the first ten months of the year, Jupiter, your ruling planet, moves through your Spirituality House. Taking up a sacred practice can keep you contented, centered, and calm. This energy will shift on November 8th, when expansive Jupiter moves into your sign. Instead of spending most of your time in seclusion, you’ll be ready to get out and mingle. Making a name in your desired field is a strong possibility during the final two months of the year. You will benefit from sticking to a strict budget this year, since restrictive Saturn is touring your Earned Income House. Making mindful purchases, putting a portion of each paycheck into savings, and cutting down wasteful spending will pave the way to long-term prosperity.  ");
        temp.add("2018 should be a year of tremendous personal accomplishment, thanks to Saturn, your ruler, moving through your Image House all year. You may achieve prominence for a special talent or set of skills. Launching your own business is possible; this endeavor is sure to be successful. A feeling of restlessness could subside in mid-May, when independent Uranus leaves your Domestic House for your Fifth House of Romance. After May 15th, you could establish a comfortable home, possibly with an unusual love interest. If you’re single, be open to dating people who are unlike anyone you’ve ever loved in the past. Jolly Jupiter moves through your Friendship House for the first ten months of the year, making you a highly popular figure. Starting on November 8th, your enjoyment shifts to your sacred life, when enlightened Jupiter moves into your Spiritual House.  ");
        temp.add("2018 could mark a big change in your home life, thanks to Uranus, your ruling planet, entering your Domesticity House. There’s a chance you could move to a place that is famous for its natural beauty or put a down payment on a distinctive property. Your career should be a source of tremendous joy for the first ten months of the year, when upbeat Jupiter moves through your Professional House. Starting November 8th, you have an excellent opportunity to realize a cherished goal; that’s when lucky Jupiter moves into your House of Cherished Dreams. Serious Saturn is touring your Twelfth House of Spirituality all year. Connecting with your higher power can provide tremendous comfort throughout 2018. Worries about money, status, and appearances will fade into the background, allowing you to focus on what’s truly important.");
        temp.add("Your income will continue to be a bit erratic during the first five months of the year, due to inconsistent Uranus moving through your Earned Income House. This will change after May 15th, when unique Uranus enters your Communication House. This could prompt you to express yourself in a radically different way than you have in the past. Don’t be surprised if you’re compelled to write an article, book, or screenplay that gets lots of attention. Lucky Jupiter continues its tour of your Adventure House for the first ten months of the year; enjoy at least one exotic trip during this golden period. On November 8th, joyous Jupiter moves into your Career House, suggesting you could land your dream job. Your social life will evolve throughout 2018, prompting you to shed fair-weather friends and grow closer to loyal people.");
        return temp;
    }
}
