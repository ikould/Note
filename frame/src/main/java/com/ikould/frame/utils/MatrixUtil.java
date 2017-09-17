package com.ikould.frame.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;

/**
 * Created by ALiu on 2017/3/22.
 */

public class MatrixUtil {

    /**
     * 判断返回左上角坐标
     *
     * @return
     */
    public static Point getLT(float[] dst) {
//        Point a = dst[]

        return null;
    }

    //冒泡排序
    public static float[] getBubbleSort(float[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {//外层循环控制排序趟数
            for (int j = 0; j < arr.length - 1 - i; j++) {//内层循环控制每一趟排序多少次
                if (arr[j] > arr[j + 1]) {
                    float temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return arr;
    }

    public static Matrix getCropMatrix(Bitmap bmp, int frameWidth, int frameHeight) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        //原始矩阵大小
        Matrix originalMatrix = new Matrix();
        //缩放比例
        float f;
        if (width > height) {
            f = frameHeight * 1.0f / height;
            //缩放后宽度
            int afterWidth = (int) (width * f);
            //左侧超出宽度
            int leftWidth1 = (afterWidth - frameWidth) / 2;
            originalMatrix.postScale(f, f);
            originalMatrix.postTranslate(-leftWidth1, 0);
        } else {
            f = frameWidth * 1.0f / width;
            //缩放后高度
            int afterHeight = (int) (height * f);
            //顶部超出宽度
            int topHeight1 = (afterHeight - frameHeight) / 2;
            originalMatrix.postScale(f, f);
            originalMatrix.postTranslate(0, -topHeight1);
        }
        return originalMatrix;
    }

    public static Matrix getInsideMatrix(Bitmap bmp, int frameWidth, int frameHeight) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        //原始矩阵大小
        Matrix originalMatrix = new Matrix();
        //缩放比例
        float f;
        if (width < height) {
            f = frameHeight * 1.0f / height;
            //缩放后宽度
            int afterWidth = (int) (width * f);
            //左侧超出宽度
            int leftWidth1 = (frameWidth - afterWidth) / 2;
            originalMatrix.postScale(f, f);
            originalMatrix.postTranslate(leftWidth1, 0);
        } else {
            f = frameWidth * 1.0f / width;
            //缩放后高度
            int afterHeight = (int) (height * f);
            //顶部超出宽度
            int topHeight1 = (frameHeight - afterHeight) / 2;
            originalMatrix.postScale(f, f);
            originalMatrix.postTranslate(0, topHeight1);
        }
        return originalMatrix;
    }

    public static Matrix getCenterMatrix(Bitmap bmp, float scale, int frameWidth, int frameHeight) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        //原始矩阵大小
        Matrix originalMatrix = new Matrix();
        //缩放比例
        float afterWidth = width * scale;
        float afterHeight = height * scale;

        originalMatrix.postScale(scale, scale);
        originalMatrix.postTranslate((frameWidth - afterWidth) / 2, (frameHeight - afterHeight) / 2);

        return originalMatrix;
    }
}
