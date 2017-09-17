package com.ikould.frame.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by admin on 2017/2/13.
 * 修改图片的工具类
 */

public class ImageUtils {

    /**
     * 压缩图片
     *
     * @param bitmap   源图片
     * @param width    想要的宽度
     * @param height   想要的高度
     * @param isAdjust 是否自动调整尺寸, true图片就不会拉伸，false严格按照你的尺寸压缩
     * @return Bitmap
     */
    public static Bitmap reduce(Bitmap bitmap, int width, int height, boolean isAdjust) {
        // 如果想要的宽度和高度都比源图片小，就不压缩了，直接返回原图
        if (bitmap.getWidth() < width && bitmap.getHeight() < height) {
            return bitmap;
        }
        // 根据想要的尺寸精确计算压缩比例, 方法详解：public BigDecimal divide(BigDecimal divisor, int scale, int roundingMode);
        // scale表示要保留的小数位, roundingMode表示如何处理多余的小数位，BigDecimal.ROUND_DOWN表示自动舍弃
        float sx = new BigDecimal(width).divide(new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN).floatValue();
        float sy = new BigDecimal(height).divide(new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN).floatValue();
        if (isAdjust) {// 如果想自动调整比例，不至于图片会拉伸
            sx = (sx < sy ? sx : sy);
            sy = sx;// 哪个比例小一点，就用哪个比例
        }
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);// 调用api中的方法进行压缩，就大功告成了
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }


    /**
     * 在bitmap上铺上一层0x59000000的bitmap
     *
     * @param background
     * @return
     */
    public static Bitmap toConformBitmap(Bitmap background) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
//        int[] pix = new int[bgWidth * bgHeight];
//        for (int y = 0; y < bgHeight; y++)
//            for (int x = 0; x < bgWidth; x++) {
//                int index = y * bgWidth + x;
//                int r = ((pix[index] >> 16) & 0xff)|0xff;
//                int g = ((pix[index] >> 8) & 0xff)|0xff;
//                int b =( pix[index] & 0xff)|0xff;
//                pix[index] = 0xb3000000 | (r << 16) | (g << 8) | b;
//            }
//        Bitmap foreground =Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
//        foreground.setPixels(pix, 0, bgWidth, 0, 0, bgWidth, bgHeight);
        Bitmap foreground = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        foreground.eraseColor(Color.parseColor("#59000000"));//填充颜色
        // 创建一个新的和SRC长度宽度一样的位图
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        //draw bg into
        cv.drawBitmap(background, 0, 0, null);
        // 在 0，0坐标开始画入bg
        // draw fg into
        cv.drawBitmap(foreground, 0, 0, null);
        // 在 0，0坐标开始画入fg ，可以从任意位置画入
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);
        // 保存store
        cv.restore();
        // 存储
        return newbmp;
    }

    /**
     * @param bmp     必须为ARGB8888
     * @param quality
     * @param path    保存路径
     * @return IMAGELIB_SUCCESS 0x0000
     * IMAGELIB_FILE_OPEN_ERROR 0x0001
     * IMAGELIB_TYPE_ERROR 0x0002
     * IMAGELIB_PARAM_ERROR 0x0004
     * IMAGELIB_ORTHER_ERROR 0x0008
     */
    public static int WriteJpgAndCount(Bitmap bmp, int quality, String path) {
        if (bmp == null || path == null || path.equals("") || path.endsWith(File.separator)) {
            return -1;
        }
        {
            File file = new File(path).getParentFile();
            if (file != null) {
                file.mkdirs();
            }
        }

        if (bmp.getConfig() != Bitmap.Config.ARGB_8888) {
            bmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
        }

        //Statistics.add_using_count("jpg保存开始");
        int reCode = WriteJpg(bmp, quality, path);
        //Statistics.add_using_count("jpg保存结束");

        return reCode;
    }

    private static int WriteJpg(Bitmap bmp, int quality, String path) {


        return 0;
    }

    public static Bitmap ReadJpgAndCount(String path, int inSampleSize) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        if (opts.outMimeType != null && !opts.outMimeType.equals("")) {
            if (opts.outMimeType.equals("image/jpeg")) {
                if (inSampleSize < 1) {
                    inSampleSize = 1;
                }
                Bitmap bmp = ReadJpg(path, inSampleSize);
                return bmp;
            }
        }

        return null;
    }

    private static Bitmap ReadJpg(String path, int inSampleSize) {


        return null;
    }
}
