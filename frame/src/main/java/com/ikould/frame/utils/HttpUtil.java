package com.ikould.frame.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * describe
 * Created by ikould on 2017/6/26.
 */

public class HttpUtil {

    private static Pattern pattern;

    /**
     * Url是否有效
     *
     * @param url
     * @return
     */
    public static boolean isUrlValid(String url) {
        if (pattern == null)
            pattern = Pattern.compile("((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }
}
