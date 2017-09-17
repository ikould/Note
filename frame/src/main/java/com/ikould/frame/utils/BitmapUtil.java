package com.ikould.frame.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.graphics.BitmapFactory.decodeStream;

/**
 * Bitmap工具类
 * <p>
 * Created by ALiu on 2017/3/17.
 */

public class BitmapUtil {

    /**
     * 回收Bitmap
     *
     * @param bmp
     */
    public static void recycle(Bitmap bmp) {
        if (bmp != null && !bmp.isRecycled()) {
            bmp.recycle();
            bmp = null;
        }
    }

    /**
     * 判断Bitmap可用
     *
     * @param bmp
     * @return
     */
    public static boolean canUse(Bitmap bmp) {
        return bmp != null && !bmp.isRecycled();
    }

    /**
     * 获取纯透明区域
     *
     * @param sourceImg
     * @param number
     * @return
     */
    public static Bitmap getTransparentBitmap(Bitmap sourceImg, int number) {
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg
                .getWidth(), sourceImg.getHeight());// 获得图片的ARGB值
        number = number * 255 / 100;
        for (int i = 0; i < argb.length; i++) {
            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
        }
        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg
                .getHeight(), Bitmap.Config.ARGB_8888);
        return sourceImg;
    }

    /**
     * 保存图片
     *
     * @param context
     * @param bmp
     * @return
     */
    public static String saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "ArtCamera");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
        return file.toString();
    }

    /**
     * 获取宽高
     *
     * @param imgPath
     * @return
     */
    public static int[] getImageWH(String imgPath) {
        int[] wh = {-1, -1};
        if (imgPath == null) {
            return wh;
        }
        File file = new File(imgPath);
        if (file.exists() && !file.isDirectory()) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                InputStream is = new FileInputStream(imgPath);
                decodeStream(is, null, options);
                wh[0] = options.outWidth;
                wh[1] = options.outHeight;
            } catch (Exception e) {
                Log.w("fuck", "getImageWH Exception.", e);
            }
        }
        return wh;
    }

    /**
     * 加载指定大小图片 - 硬压缩
     *
     * @param path
     * @param scale
     * @return
     */
    public static Bitmap scaleBitmapHard(String path, int scale) {
        Bitmap bm = null;
        try {
            //读取图片
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = scale;
            InputStream is = new FileInputStream(path);
            bm = decodeStream(is, null, options);
        } catch (Exception e) {
            Log.w("fuck", "scaleBitmapHard Exception.", e);
        } catch (OutOfMemoryError e) {
            Log.e("fuck", "scaleBitmapHard OutOfMemoryError.", e);
            bm = scaleBitmapHard(path, scale + 1);
            //TODO: out of memory deal..
        }
        return bm;
    }

    /**
     * 加载指定大小图片 - 软压缩
     *
     * @param temp      传入图片
     * @param scaleSize 缩放比例
     * @return
     */
    public static Bitmap scaleBitmapSoft(Bitmap temp, float scaleSize) {
        if (!canUse(temp)) {
            return null;
        }
        Matrix m = new Matrix();
        m.postScale(scaleSize, scaleSize, 0, 0);
        return Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), m, true);
    }

    /**
     * 加载到1024
     *
     * @param path
     * @param defaultSize
     * @return
     */
    public static Bitmap createBitmapDefaultsize(String path, int defaultSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap result = null;
        //读取照片是否旋转
        int degree = readPictureDegree(path);
        int[] imageWH = getImageWH(path);
        if (imageWH[0] < defaultSize * 2 || imageWH[1] < defaultSize * 2) {
            //小于指定分辨率2倍 缩放后加载
            Bitmap temp = BitmapFactory.decodeFile(path, options);
            result = scaleBitmapSoft(temp, defaultSize);
        } else {
            //大于两倍 先压缩再缩放
            int min = Math.min(imageWH[0], imageWH[1]);
            int scale = min / defaultSize;
            result = scaleBitmapHard(path, scale);
            if (canUse(result)) {
                result = scaleBitmapSoft(result, defaultSize);
            }
        }
        if (degree != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            // 创建新的图片
            Bitmap resizedBitmap = Bitmap.createBitmap(result, 0, 0,
                    result.getWidth(), result.getHeight(), matrix, true);
            recycle(result);
            result = resizedBitmap;
        }
        if (result == null) {
            result = Bitmap.createBitmap(defaultSize, defaultSize, Bitmap.Config.ARGB_8888);
        }
        return result;
    }

    /**
     * 根据屏幕大小放大图片
     *
     * @param path
     * @param height
     * @return
     */
    public static Bitmap createBitmapScreen(String path, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        int[] imageWH = getImageWH(path);
        float f = height * 1.0f / imageWH[1];
        Bitmap b = Bitmap.createBitmap(height, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Matrix m = new Matrix();
        m.postScale(f, f, 0, 0);
        c.drawBitmap(BitmapFactory.decodeFile(path, options), m, null);
        return b;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 带40背景蒙版的 用于加载选择框
     *
     * @param path
     * @return
     */
    public static Bitmap getWithBlack(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        result.eraseColor(Color.parseColor("#66000000"));
        Canvas c = new Canvas(result);
        c.drawBitmap(bitmap, 0, 0, null);
        recycle(bitmap);
        return result;
    }

    /**
     * 放大两倍，反转，居中，裁切掉两边，
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    public static Bitmap cropBitmap(Bitmap bitmap) {
        // 放大两倍
        Matrix matrix = new Matrix();
        matrix.setRotate(180);
        matrix.postScale(2f, 2f);
        Bitmap bigBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        bitmap.recycle();
        // 裁切两边
        Bitmap resultBitmap = Bitmap.createBitmap(bigBitmap, bigBitmap.getWidth() / 4, 0, bigBitmap.getWidth() / 2, bigBitmap.getHeight(), null, false);
        bigBitmap.recycle();
        return resultBitmap;
    }

    /**
     * 转换为圆形图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap circleToBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int min = width < height ? width : height;
        Bitmap output = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return output;
    }
}
