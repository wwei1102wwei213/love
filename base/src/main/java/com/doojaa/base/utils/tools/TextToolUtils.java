package com.doojaa.base.utils.tools;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 文字处理工具类
 */
public class TextToolUtils {

    private static final String COLOR_DEFAULT = "ff890b";

    /**
     * 关键字高亮显示
     *
     * @param text   需要显示的文字
     * @param target 需要高亮的关键字
     * @param color  高亮颜色值 例如#ffffff
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     */
    public static SpannableStringBuilder highlight(String text, String target, String color) {

        if (TextUtils.isEmpty(color))
            color = COLOR_DEFAULT;

        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;
        try {
            Pattern p = Pattern.compile(target);
            Matcher m = p.matcher(text);
            while (m.find()) {
                span = new ForegroundColorSpan(Color.parseColor(color));// 需要重复！
                spannable.setSpan(span, m.start(), m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
        return spannable;
    }

    public static String limitLength(String str, int maxLength) {
        if (str.length() < maxLength)
            return str;
        String s = str.substring(0, maxLength);
        if (s.endsWith("..")) {
            return s;
        } else {
            if (s.endsWith("，") || s.endsWith("。") || s.endsWith("！"))
                s = s.substring(0, s.length() - 1);
            return s + "...";
        }
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("，", "").replaceAll("。", "");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
