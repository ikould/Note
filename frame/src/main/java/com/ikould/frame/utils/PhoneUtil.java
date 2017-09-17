package com.ikould.frame.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

/**
 * 手机相关工具类
 * <p>
 * Created by ikould on 2017/6/5.
 */

public class PhoneUtil {

    /**
     * 判断系统是否安装了浏览器
     *
     * @param context
     * @return
     */
    public static boolean hasBrowser(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://"));
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY); // TAG 待定
        final int size = (list == null) ? 0 : list.size();
        return size > 0;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String areaCode, String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3、4、5、7、8，其他位置的可以为0-9
        */
        if ("+86".equals(areaCode)) {
            String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
            if (TextUtils.isEmpty(mobiles)) return false;
            else return mobiles.matches(telRegex);
        } else {
            return true;
        }
    }
}
