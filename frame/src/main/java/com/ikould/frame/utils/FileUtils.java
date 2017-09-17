package com.ikould.frame.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;


/**
 * SD卡操作类
 *
 * @author Administrator
 */
public class FileUtils {
    public static final String TAG = FileUtils.class.getSimpleName();
    private static String SDPath;

    public FileUtils() {
        super();
    }

    /**
     * 在SD卡上创建文件
     *
     * @param filePath
     * @return
     */
    public File createSDFile(String filePath) {
        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
        }
        return file;
    }

    /**
     * 返回apk的data文件夹里面的文件列表
     *
     * @param context
     * @return
     */
    public static String[] findAllFileList(Context context) {
        String[] files = null;
        if (context != null) {
            files = context.fileList();
        }
        return files;
    }

    /**
     * 将SD卡里面的完整路径的文件filePath复制到apk的data文件夹里面
     *
     * @param context
     * @param filePath
     * @param fileName
     * @return
     */
    public static boolean copySDFile2DataCustom(Context context, String filePath, String dirName, String fileName) {
        boolean flag = false;
        if (fileName != null && filePath != null) {
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                in = new FileInputStream(filePath);
//				out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                File cacheDir = context.getDir(dirName, Context.MODE_PRIVATE);
                String strCacheDir = cacheDir.getAbsolutePath();
                String strName = strCacheDir + File.separator + fileName;
                out = new FileOutputStream(strName);
                byte[] buf = new byte[1024 * 4];
                int len = 0;
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                out.flush();
                flag = true;
            } catch (FileNotFoundException e) {
                Log.e("AssertEx", "FileNotFoundException" + e);
            } catch (IOException e) {
                Log.e("AssertEx", "IOException" + e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                        in = null;
                    } catch (IOException e) {
                        Log.e("AssertEx", "IOException" + e);
                    }

                }
                if (out != null) {
                    try {
                        out.close();
                        out = null;
                    } catch (IOException e) {
                        Log.e("AssertEx", "IOException" + e);
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 将SD卡里面的完整路径的文件filePath复制到apk的data文件夹里面
     *
     * @param context
     * @param filePath
     * @param fileName
     * @return
     */
    public static boolean copySDFile2Data(Context context, String filePath, String fileName) {
        boolean flag = false;
        if (fileName != null && filePath != null) {
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                in = new FileInputStream(filePath);
                out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                if (in != null && out != null) {
                    byte[] buf = new byte[1024 * 4];
                    int len = 0;
                    while ((len = in.read(buf)) != -1) {
                        out.write(buf, 0, len);
                    }
                    out.flush();
                    flag = true;
                }
            } catch (FileNotFoundException e) {
                Log.e("AssertEx", "FileNotFoundException" + e);
            } catch (IOException e) {
                Log.e("AssertEx", "IOException" + e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                        in = null;
                    } catch (IOException e) {
                        Log.e("AssertEx", "IOException" + e);
                    }

                }
                if (out != null) {
                    try {
                        out.close();
                        out = null;
                    } catch (IOException e) {
                        Log.e("AssertEx", "IOException" + e);
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 将data里面的文件名为fileName的文件复制到SD卡filePath的完整文件路径
     *
     * @param context
     * @param dirName
     * @param fileName
     * @param filePath
     * @return
     */
    public static boolean copyCustomDataFile2SD(Context context, String dirName, String fileName, String filePath) {
        if (fileName != null && filePath != null) {
            try {
//				FileInputStream in = context.openFileInput(fileName);
                File cacheDir = context.getDir(dirName, Context.MODE_PRIVATE);
                String strCacheDir = cacheDir.getAbsolutePath();
                String name = strCacheDir + File.separator + fileName;
                FileInputStream in = new FileInputStream(name);
                File file = new File(filePath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                FileOutputStream out = new FileOutputStream(filePath);
                int size = in.available();
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && size < FileUtils.getSDAvailableSize()) {
                    byte[] buf = new byte[1024 * 2];
                    int len = 0;
                    while ((len = in.read(buf)) != -1) {
                        out.write(buf, 0, len);
                    }
                    out.flush();
                    if (in != null) {
                        in.close();
                        in = null;
                    }
                    if (out != null) {
                        out.close();
                        out = null;
                    }
                    return true;
                } else {
                    if (in != null) {
                        in.close();
                        in = null;
                    }
                    if (out != null) {
                        out.close();
                        out = null;
                    }
                }
            } catch (FileNotFoundException e) {
                Log.e("AssertEx", "FileNotFoundException" + e);
            } catch (IOException e) {
                Log.e("AssertEx", "IOException" + e);
            }
        }
        return false;
    }

    /**
     * 将data里面的文件名为fileName的文件复制到SD卡filePath的完整文件路径
     *
     * @param context
     * @param fileName
     * @param filePath
     * @return
     */
    public static boolean copyDataFile2SD(Context context, String fileName, String filePath) {
        if (fileName != null && filePath != null) {
            try {
                FileInputStream in = context.openFileInput(fileName);
                File file = new File(filePath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                FileOutputStream out = new FileOutputStream(filePath);
                int size = in.available();
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && size < FileUtils.getSDAvailableSize()) {
                    byte[] buf = new byte[1024 * 2];
                    int len = 0;
                    while ((len = in.read(buf)) != -1) {
                        out.write(buf, 0, len);
                    }
                    out.flush();
                    if (in != null) {
                        in.close();
                        in = null;
                    }
                    if (out != null) {
                        out.close();
                        out = null;
                    }
                    return true;
                } else {
                    if (in != null) {
                        in.close();
                        in = null;
                    }
                    if (out != null) {
                        out.close();
                        out = null;
                    }
                }
            } catch (IOException e) {
                Log.e("FileUtils", "copyDataFile2SD: e = " + e);
            }
        }
        return false;
    }

    /**
     * 在SD卡上创建文件
     *
     * @param pathName
     * @param fileName
     * @return
     */
    public File createSDFile(String pathName, String fileName) {
        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory()
                    .toString()
                    + File.separator
                    + pathName
                    + File.separator
                    + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            Log.e("FileUtils", "createSDFile: e = " + e);
        }
        return file;
    }

    public static boolean isExistDefaultDir(String filePath) {
        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            file = new File(filePath);
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 查找文件的data文件夹里面是否存在文件名为fileName的文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static boolean isExitInData(Context context, String fileName) {
        String[] files = null;
        if (context != null) {
            files = context.fileList();
        }
        if (files != null) {
            for (String str : files) {
                if (fileName.equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isExitInDataCustom(Context context, String dirName, String fileName) {
        if (context == null || fileName == null) {
            return false;
        }
        String[] files = null;
        File cacheDir = context.getDir(dirName, Context.MODE_PRIVATE);
        files = cacheDir.list();
        if (files != null) {
            for (String str : files) {
                if (fileName.equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否存在文件或者存在的文件夹是否为空
     *
     * @param filePath
     * @return
     */
    public static boolean isExistFiles(String filePath) {
        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            file = new File(filePath);
            if (file.exists()) {
                if (file.isDirectory()) {
                    if (file.list().length != 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     * @return
     */
    public File createSDDir(String dirName) {
        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory()
                    .toString() + File.separator + dirName);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return file;
    }

    /**
     * 判断SD卡上的文件夹是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory()
                    .toString() + File.separator + filePath);
            return file.exists();
        }
        return false;
    }

    /**
     * 获取当前设备的SD卡根存储目录
     *
     * @return
     */
    public static String getSDPath() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            SDPath = Environment.getExternalStorageDirectory().toString()
                    + File.separator;
            return SDPath;
        }
        return null;
    }

    /**
     * 获取文件路径String
     *
     * @param dirName
     * @param fileName
     * @return
     */
    public String getFilePath2String(String dirName, String fileName) {
        return SDPath + dirName + File.separator + fileName;
    }

    /**
     * 获取对应文件目录下的文件的File
     *
     * @param dirName
     * @param fileName
     * @return
     */
    public static File getFilePath(String dirName, String fileName) {
        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String dirPath;
            if (dirName != null) {
                dirPath = Environment.getExternalStorageDirectory().toString()
                        + File.separator + dirName + File.separator;
            } else {
                dirPath = SDPath;
            }
            file = new File(dirPath + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
        }
        return file;
    }

    /**
     * 获取SD卡的剩余容量，单位是Byte
     *
     * @return
     */
    public static long getSDAvailableSize() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File pathFile = Environment.getExternalStorageDirectory();
            // Retrieve overall information about the space on a filesystem.
            // This is a Wrapper for Unix statfs().
            StatFs statfs = new StatFs(pathFile.getPath());
            // 获取SDCard上每一个block的SIZE
            long nBlockSize = statfs.getBlockSize();
            // 获取可供程序使用的Block的数量
            // long nAvailBlock = statfs.getAvailableBlocksLong();
            long nAvailBlock = statfs.getAvailableBlocks();
            // 计算SDCard剩余大小Byte
            long nSDFreeSize = nAvailBlock * nBlockSize;
            return nSDFreeSize;
        }
        return 0;
    }

    /**
     * 获取SD卡的总容量，单位是Byte
     *
     * @return
     */
    public long getSDCountSize() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File pathFile = Environment.getExternalStorageDirectory();
            // Retrieve overall information about the space on a filesystem.
            // This is a Wrapper for Unix statfs().
            StatFs statfs = new StatFs(pathFile.getPath());
            // 获取SDCard上每一个block的SIZE
            long nBlockSize = statfs.getBlockSize();
            // 获取可供程序使用的Block的数量
            // long nCountBlock = statfs.getBlockCountLong();
            long nCountBlock = statfs.getBlockCount();
            // 计算SDCard剩余大小Byte
            long nSDFreeSize = nCountBlock * nBlockSize;
            return nSDFreeSize;
        }
        return 0;
    }

    /**
     * 将输入流写入到指定目录的SD卡中
     *
     * @param path
     * @param fileName
     * @param input
     * @return
     */
    public static boolean write2SDFromInput(String path, String fileName,
                                            InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            int size = input.available();
            // 拥有可读可写权限，并且有足够的容量
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)
                    && size < getSDAvailableSize()) {
                file = getFilePath(path, fileName);
                output = new FileOutputStream(file);
                byte buffer[] = new byte[1024 * 2];
                int len = 0;
                while ((len = input.read(buffer)) != -1) {
                    output.write(buffer, 0, len);
                }
                output.flush();
            }
        } catch (FileNotFoundException e) {
            Log.e("FileEro", "FileNotFoundException write2SDFromInput" + e);
            return false;
        } catch (IOException e) {
            Log.e("FileEro", "IOException write2SDFromInput" + e);
            return false;
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    Log.e("FileEro", "IOException write2SDFromInput" + e);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 将输入流写入到指定的File路径中
     *
     * @param filePath
     * @param input
     * @return
     */
    public static boolean write2SDFromInput(String filePath, InputStream input) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            int size = input.available();
            // 拥有可读可写权限，并且有足够的容量
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)
                    && size < getSDAvailableSize()) {
                bis = new BufferedInputStream(input);
                File outFile = new File(filePath);
                if (!outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs();
                }
                bos = new BufferedOutputStream(new FileOutputStream(outFile));
                byte buffer[] = new byte[1024 * 2];
                int len = 0;
                while ((len = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
            }
        } catch (FileNotFoundException e) {
            Log.e("FileEro", "FileNotFoundException write2SDFromInput" + e);
            return false;
        } catch (IOException e) {
            Log.e("FileEro", "IOException write2SDFromInput" + e);
            return false;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    Log.e("FileEro", "IOException write2SDFromInput" + e);
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    Log.e("FileEro", "IOException write2SDFromInput" + e);
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 将输入流转换成字符串
     *
     * @param input
     * @return
     */
    public static String inputStream2String(InputStream input) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            Log.e("FileEro", "IOException inputStream2String" + e);
        }
        return builder.toString();
    }

    /**
     * 将data里面的文件输入流转换成字符串
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String dataInputStream2String(Context context, String fileName) {
        if (fileName == null) {
            return null;
        }
        String str = null;
        if (isExitInData(context, fileName)) {
            try {
                FileInputStream in = context.openFileInput(fileName);
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                if (reader != null) {
                    reader.close();
                }
                str = builder.toString();
            } catch (FileNotFoundException e) {
                Log.e("FileEro", "FileNotFoundException" + e);
            } catch (IOException e) {
                Log.e("FileEro", "IOException" + e);
            }
        }
        return str;
    }

    /**
     * 读取本地文本文件
     *
     * @param fileName 指  文件所在路径  + 文件名
     * @return 字符串
     */
    public static String readFile2String(String fileName) {
        File file = null;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        file = new File(fileName);
        if (file.exists()) {
            Log.d("Share", "file.exists() ");
            try {
                reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file)));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (FileNotFoundException e) {
                Log.e("FileEro", "FileNotFoundException readFile2String" + e);
            } catch (IOException e) {
                Log.e("FileEro", "IOException readFile2String" + e);
            }
            return builder.toString();
        } else {
            Log.d("Share", "file. not exists() ");
            return null;
        }
    }

    /**
     * 读取本地图片
     *
     * @param fileName 文件的全路径
     * @return
     */
    public static Bitmap readPicture(String fileName) {
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
        return bitmap;
    }

    /**
     * 删除指定路径下的文件，包括文件和文件目录
     *
     * @param filePath
     * @return
     */
    public static void deleteFile(String filePath) {
        //定义文件路径
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File subFile : files) {
                    if (subFile.isDirectory()) {
                        deleteFile(subFile.getPath());
                    } else {
                        subFile.delete();
                    }
                }
            }
            file.delete();
        }
    }

    /**
     * 更改文件和文件夹名字
     *
     * @param src  源文件名
     * @param dest 更改后的文件名
     * @return
     */
    public static boolean renameToNewFile(String src, String dest) {
        File srcDir = new File(src);
        boolean isOk = srcDir.renameTo(new File(dest));
        return isOk;
    }


    /**
     * 将assets文件夹下的文件复制到sd卡，以便于后续解压
     *
     * @param oldPath
     * @param newPath
     * @return
     */
    public static boolean copyFile(String oldPath, String newPath) {
        File file = new File(oldPath);
        // 创建解压目标目录
        File saveFile = new File(newPath);
        // 如果目标目录不存在，则创建
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        int count = 0;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buf = new byte[1024 * 4];
            File file1 = new File(newPath + File.separator + file.getName());
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file1);
            while ((count = fileInputStream.read(buf)) != -1) {
                fos.write(buf, 0, count);
            }
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "copyFile: " + e.getMessage());
        }
        return false;
    }

    /**
     * 将assets文件夹下的文件复制到sd卡，以便于后续解压
     *
     * @param fileInputStream
     * @param fileName
     * @param newPath
     * @return
     */
    public static boolean copyFile(InputStream fileInputStream, String fileName, String newPath) {
        // 创建解压目标目录
        File saveFile = new File(newPath);
        // 如果目标目录不存在，则创建
        if (!saveFile.exists()) {
            Log.i(TAG, "copyFile: " + saveFile.getAbsolutePath());
            boolean isSuccess = saveFile.mkdirs();
            if (!isSuccess) {
                Log.e("FileUtils", "文件夹创建失败 dirPath = " + newPath);
            }
        }
        int count = 0;
        try {
            byte[] buf = new byte[1024 * 4];
            File file1 = new File(newPath + File.separator + fileName);
            Log.d("FileUtils", "copyFile: file1.exist = " + file1.exists());
            file1.createNewFile();
            FileOutputStream fos = new FileOutputStream(file1);
            while ((count = fileInputStream.read(buf)) != -1) {
                fos.write(buf, 0, count);
            }
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "copyFile: FileNotFoundException" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "copyFile: IOException " + e.getMessage());
        }
        return false;
    }

    /**
     * 复制文件夹以及其下文件
     *
     * @param oldPath
     * @param newPath
     * @return
     */
    public static boolean copyDirectorys(String oldPath, String newPath) {
        //创建新文件夹
        File newFile = new File(newPath);
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
//        File oldFile = new File(oldPath);
        File[] file = (new File(oldPath)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 复制文件
                copyFile(file[i], new File(newPath + File.separator + file[i].getName()));
            }
            if (file[i].isDirectory()) {
                // 复制目录
                String sourceDir = oldPath + File.separator + file[i].getName();
                String targetDir = newPath + File.separator + file[i].getName();
                copyDirectiory(sourceDir, targetDir);
            }
        }

        return true;
    }


    // 复制文件夹
    public static void copyDirectiory(String sourceDir, String targetDir) {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new
                        File(new File(targetDir).getAbsolutePath()
                        + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }


    /**
     * 复制文件
     *
     * @param sourceFile
     * @param targetFile
     */
    public static void copyFile(File sourceFile, File targetFile) {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = null;
        try {
            input = new FileInputStream(sourceFile);
            BufferedInputStream inBuff = new BufferedInputStream(input);
            // 新建文件输出流并对它进行缓冲
            FileOutputStream output = new FileOutputStream(targetFile);
            BufferedOutputStream outBuff = new BufferedOutputStream(output);

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
            //关闭流
            inBuff.close();
            outBuff.close();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    Bitmap对象保存图片文件
    public static void saveBitmapFile(Bitmap bitmap, String path, String endName) {
        File file = new File(path);//将要保存图片的路径
        Log.i(TAG, "saveBitmapFile: " + path);
        if (!file.exists()) {
            boolean isSuccess = file.mkdirs();
            Log.d("FileUtils", "saveBitmapFile: isSuccess = " + isSuccess);
        }
        File newFile = new File(path + File.separator + endName);
        try {
            if (newFile.exists()) {
                newFile.delete();
            }
            newFile.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile));
            if (endName.endsWith("jpg")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
            } else if (endName.endsWith("png")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 30, bos);
            }
            bos.flush();
            bos.close();
            Log.i(TAG, "saveBitmapFile: " + "缓存成功" + path);
        } catch (IOException e) {
            Log.e(TAG, "saveBitmapFile:  " + "\t" + "缓存失败" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void writeStrToFile(String xml, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            Writer os = new OutputStreamWriter(fos, "UTF-8");
            os.write(xml);
            os.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
