package com.ikould.frame.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * View操作帮助工具
 * <p>
 * Created by liudong on 2017/8/7.
 */

public class ViewUtil {

    /**
     * 密码显隐状态管理
     *
     * @param editText
     * @param checkBox
     */
    public static void bindPasswordToEye(final EditText editText, CheckBox checkBox) {
        boolean checked = checkBox.isChecked();
        editText.setTransformationMethod(checked ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d("PasswordUtil", "bindPasswordToEye: checked2 = " + isChecked);
            editText.setTransformationMethod(isChecked ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
            editText.setSelection(editText.length());
        });
    }

    /**
     * 禁止EditText输入空格
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Log.d("PasswordUtil", "filter: source = " + source);
                if (source.equals(" ")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }


    /**
     * 限制TextView的长度，超过N数量，第N-1位为"..."
     */
    public static void addLimitTextToTextView(TextView textView, String text, int limit) {
        int length = text.length();
        if (length > limit) {
            int nowLength = 0;
            for (int i = 0; i < length; i++) {
                char c = text.charAt(i);
                boolean isEnglishOrNum = CharUtil.isEnglish(c) || CharUtil.isNumber(c) || CharUtil.isSpace(c);
                /*if (isEnglish()){

                }*/
            }
        }
    }

}
