package com.ikould.frame.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符判断
 * <p>
 * Created by liudong on 2017/8/31.
 */

public class CharUtil {

    /**
     * 是否是英文
     *
     * @return
     */
    public static boolean isEnglish(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    /**
     * 是否是数字
     *
     * @return
     */
    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * 是否是空格
     *
     * @return
     */
    public static boolean isSpace(char c) {
        return c == ' ';
    }

    /**
     * Emoji表情过滤器
     */
    public static class EmoJiFilter implements InputFilter {

        private OnEmoJiListener onEmoJiListener;

        private Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            // emoji  过滤
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                if (onEmoJiListener != null)
                    onEmoJiListener.onCheckEmoji(true);
                return "";
            }
            if (onEmoJiListener != null)
                onEmoJiListener.onCheckEmoji(false);
            return null;
        }

        public void setOnSpaceListener(OnEmoJiListener onEmoJiListener) {
            this.onEmoJiListener = onEmoJiListener;
        }

        public interface OnEmoJiListener {
            void onCheckEmoji(boolean isHaveEmoJi);
        }
    }

    /**
     * 空格过滤
     */
    public static class SpaceFilter implements InputFilter {

        private OnSpaceListener onSpaceListener;

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.toString().contains(" ")) {
                if (onSpaceListener != null)
                    onSpaceListener.onCheckSpace(true);
                return source.toString().replace(" ", "");
            }
            if (onSpaceListener != null)
                onSpaceListener.onCheckSpace(false);
            return null;
        }

        public void setOnSpaceListener(OnSpaceListener onSpaceListener) {
            this.onSpaceListener = onSpaceListener;
        }

        public interface OnSpaceListener {
            void onCheckSpace(boolean isHaveSpace);
        }
    }

    /**
     * 长度过滤器
     */
    public static class LengthFilter implements InputFilter {

        private final int                  mMax;
        private       OnLengthOverListener onLengthOverListener;

        public LengthFilter(int max) {
            mMax = max;
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            Log.d("LengthFilter", "filter: source = " + source + " start = " + start + " end = " + end + " dest = " + dest.toString() + " dstart = " + dstart + " dend = " + dend);
            int desLength = getLengthByString(dest.toString());
            int keep = mMax - desLength;  // 剩余可用字符
            Log.d("LengthFilter", "filter: keep = " + keep);
            if (keep <= 0) {
                if (onLengthOverListener != null) {
                    onLengthOverListener.onOverListener(source.toString().length() > 0);
                }
                return "";
            } else {
                keep += start;
                String sourceStr = source.toString();
                String text = limitToString(sourceStr, keep);
                Log.d("LengthFilter", "filter: sourceStr = " + sourceStr);
                Log.d("LengthFilter", "filter: text = " + text);
                if (onLengthOverListener != null) {
                    onLengthOverListener.onOverListener(!text.equals(sourceStr));
                }
                return text;
            }
        }

        public void setOnLengthOverListener(OnLengthOverListener onLengthOverListener) {
            this.onLengthOverListener = onLengthOverListener;
        }

        public interface OnLengthOverListener {
            void onOverListener(boolean isOver);
        }
    }

    /**
     * 限定字符长度
     * 英文，数字，空格长度算1，其余算2
     *
     * @param text
     * @param limit
     * @return
     */
    public static String limitToString(String text, int limit) {
        if (TextUtils.isEmpty(text))
            return text;
        int length = text.length();
        if (limit > 0) {
            int nowLength = 0;
            for (int i = 0; i < length; i++) {
                char c = text.charAt(i);
                boolean isLength_1 = CharUtil.isEnglish(c) || CharUtil.isSpace(c) || CharUtil.isNumber(c);
                if (isLength_1) {
                    nowLength++;
                } else {
                    nowLength += 2;
                }
                if (nowLength > limit) {
                    return text.substring(0, i);
                }
            }
        }
        return text;
    }

    /**
     * 获取字符串的长度
     * 数字，英文，空格算1，其余算2
     *
     * @return
     */
    public static int getLengthByString(String string) {
        if (TextUtils.isEmpty(string))
            return 0;
        int length = 0;
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            boolean isLength_1 = CharUtil.isEnglish(c) || CharUtil.isSpace(c) || CharUtil.isNumber(c);
            if (isLength_1) {
                length++;
            } else {
                length += 2;
            }
        }
        return length;
    }
}
