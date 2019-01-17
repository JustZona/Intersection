package com.rent.zona.baselib.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.rent.zona.baselib.log.LibLogger;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class FileHelper {
    private static final String TAG = "FileHelper";

    public static final String FILE_BASE = "file://";

    private static final int IO_BUF_SIZE = 1024 * 32; // 32KB
    private static final Random sTempRandom = new Random(System.currentTimeMillis());

    /**
     * Close a {@link java.io.Closeable} object and ignore the exception.
     *
     * @param target The target to close. Can be null.
     */
    public static void close(Closeable target) {
        try {
            if (target != null) {
                target.close();
            }
        } catch (IOException e) {

        }
    }

    public static void close(Cursor cursor) {
        try {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {

        }
    }

    public static boolean fileExist(String fileName) {
        return new File(fileName).exists();
    }

    public static void deleteFile(String filePath) {
        deleteFile(new File(filePath));
    }

    /**
     * Delete a file or a directory
     */
    public static void deleteFile(String dir, String filename) {
        deleteFile(new File(dir, filename));
    }


    /**
     * Delete a file or a directory
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        deleteFile(files[i]);
                    }
                }
                file.delete();
            }
        }
    }

    public static void ensureFileDeleted(File file) {
        if (file == null || !file.exists()) {
            return;
        }

        File renamedFile = file;
        // TODO 临近发版 先不大改了
//        File renamedFile = getRenamedTempFile(file);
//        if (!file.renameTo(renamedFile)) {
//            renamedFile = file;
//        }
        if (!renamedFile.delete()) {
            Log.w(TAG, new RuntimeException("Deleting file has failed: " + renamedFile.getAbsolutePath()));
        }
    }

    public static void clearFolderFiles(File dir) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File item : files) {
                    if (item.isFile()) {
                        item.delete();
                    }
                }
            }
        }
    }

    public static void clearFolder(File dir) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File item : files) {
                    deleteFile(item);
                }
            }
        }
    }

    /**
     * Read all lines of input stream.
     */
    public static String readStreamAsString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,
                "UTF-8"));
        StringBuilder result = new StringBuilder();
        String line = null;

        boolean first = true;
        while ((line = reader.readLine()) != null) {
            if (!first) {
                result.append('\n');
            } else {
                first = false;
            }
            result.append(line);
        }

        return result.toString();
    }

    public static void saveStringToFile(String text, File file) {
        File dirFile = file.getParentFile();
        ensureDirExists(dirFile);
        FileOutputStream outStream = null;

        try {
            outStream = new FileOutputStream(file);
            outStream.write(text.toString().getBytes("UTF-8")); // URL_CONTENT_FILE_ENCODING
            outStream.flush();
        } catch (Throwable e) {

        } finally {
            FileHelper.close(outStream);
        }
    }

    /**
     * Save the input stream into a file.</br> Note: This method will close the
     * input stream before return.
     */
    public static void saveStreamToFile(InputStream is, File file)
            throws IOException {
        File dirFile = file.getParentFile();
        ensureDirExists(dirFile);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[IO_BUF_SIZE];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
        } finally {
            FileHelper.close(fos);
            FileHelper.close(is);
        }
    }

    public static void ensureDirExists(File file) {
        if (file == null) {
            return;
        }

        if (file.isFile()) {
            deleteFile(file);
        }

        if (!file.exists() && !file.mkdirs()) {
            // check again
            if (!file.isDirectory()) {
                Log.w(TAG, new RuntimeException("Making directory has failed: " + file.getAbsolutePath()));
            }
        }
    }

    /**
     * Read all lines of input stream.
     */
    public static void readFileToStringBuilder(String filename,
                                               StringBuilder result) {
        if (result.length() > 0) {
            result.delete(0, result.length());
        }
        FileInputStream fis = null;
        BufferedReader reader = null;
        try {
            fis = new FileInputStream(filename);
            reader = new BufferedReader(new InputStreamReader(
                    fis, "UTF-8"));
            boolean first = true;
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!first) {
                    result.append('\n');
                } else {
                    first = false;
                }
                result.append(line);
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            FileHelper.close(reader);
            FileHelper.close(fis);
        }
    }

    /**
     * Read all lines of text file.
     *
     * @return null will be returned if any error happens
     */
    public static String readFileAsString(String filename) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            return readStreamAsString(fis);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            FileHelper.close(fis);
        }
        return null;
    }

    public static String readFileAsString(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return readStreamAsString(fis);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            FileHelper.close(fis);
        }
        return null;
    }

    /**
     * Read all lines of text file and trim the result string. The returned
     * string must be non-empty if not null.
     */
    public static String readFileAsStringTrim(String filename) {
        String result = readFileAsString(filename);
        if (result != null) {
            result = result.trim();
            if (result.length() == 0) {
                result = null;
            }
        }
        return result;
    }

    public static String readAssetFileAsString(String fileName, Context context) {
        InputStream in = null;
        String result = null;
        try {
            AssetManager am = context.getAssets();
            if (am != null) {
                in = am.open(fileName);
                result = readStreamAsString(in);
            }
        } catch (IOException e) {
            LibLogger.d(TAG, "fail to read asset file: " + fileName + " for " + e);
        } finally {
            FileHelper.close(in);
        }

        return result;
    }

    public static Bitmap readAssetFileAsBitmap(String fileName, Context context) {
        InputStream in = null;
        Bitmap result = null;
        try {
            AssetManager am = context.getAssets();
            if (am != null) {
                in = am.open(fileName);
                result = BitmapFactory.decodeStream(in);
                FileHelper.close(in);
            }
        } catch (IOException e) {
            LibLogger.d(TAG, "fail to read asset file: " + fileName + " for " + e);
        } finally {
            FileHelper.close(in);
        }
        return result;
    }

    public static File createExternalPath(String path) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File f = new File(path);
            ensureDirExists(f);
            return f;
        }
        return null;
    }

    public static File getSafeExternalFile(String path, String name) {
        createExternalPath(path);
        return new File(path, name);
    }

    public static String getFileExtension(String filename) {
        String extension = "";
        if (!TextUtils.isEmpty(filename)) {
            int index = filename.lastIndexOf('.');
            if (index > 0 && index < filename.length() - 1) {
                extension = filename.substring(index + 1);
            }
        }

        return extension;
    }

    /**
     * null may be returned if no extension found
     *
     * @return
     */
    public static String replaceFileExtensionName(String filename, String newExt) {
        int index = filename.lastIndexOf('.');
        if (index > 0) {
            return filename.substring(0, index + 1) + newExt;
        }
        return null;
    }

    public static long getFileSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        long size = 0;
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                for (int i = 0; i < subFiles.length; i++) {
                    size += getFileSize(subFiles[i]);
                }
            }
        } else if (file.isFile()) {
            size = file.length();
        }
        return size;
    }

    public static byte[] computeFileMd5(File file) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            MessageDigest md5;
            md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[4096];
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
            }
            return md5.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("MD5 algorithm not found");
        } finally {
            FileHelper.close(fis);
        }
    }

    public static String fileNameWrapper(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return fileName;
        }
        StringBuilder sb = new StringBuilder("\"");
        // replace " with \" and use "" wrap the file name.
        return sb.append(fileName.replace("\"", "\\\"")).append("\"")
                .toString();
    }

    public static byte[] getDataFromFile(String fileLocation) {
        File getFile = new File(fileLocation);
        FileInputStream fileIps = null;
        byte[] data = null;
        int length = 0;
        try {
            fileIps = new FileInputStream(getFile);
            length = (int) getFile.length();
            if (length != 0) {
                data = new byte[length];
                fileIps.read(data);
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            FileHelper.close(fileIps);
        }

        return data;
    }

    public static Boolean saveDataToFile(String currentDir, String currentFile,
                                         byte[] imgData) {
        FileOutputStream fileOps = null;
        File currentDirFile = new File(currentDir);
        ensureDirExists(currentDirFile);
        File saveFile;
        try {
            saveFile = new File(currentDir + currentFile);
            fileOps = new FileOutputStream(saveFile);
            fileOps.write(imgData);
            fileOps.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            FileHelper.close(fileOps);
        }
        return true;
    }

    private static File getExternalCacheDir(Context context) {
        File appCacheDir = context.getExternalCacheDir();
        if (appCacheDir != null && !appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
            ensureFileExists(new File(appCacheDir, ".nomedia"));
        }
        return appCacheDir;
    }

    public static void ensureFileExists(File file) {
        if (file == null || file.exists()) {
            return;
        }

        try {
            if (!file.createNewFile()) {
                throw new IOException();
            }
        } catch (IOException e) {
            Log.w(TAG, new RuntimeException("Creating file has failed: " + file.getAbsolutePath(), e));
        }
    }

    @TargetApi(9)
    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        }
        if (preferExternal) {
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
//                if (Environment.MEDIA_MOUNTED.equals(externalStorageState)
//                        || !Environment.isExternalStorageRemovable()) {
//                    appCacheDir = getExternalCacheDir(context);
//                }
//            } else {
//                if (Environment.MEDIA_MOUNTED.equals(externalStorageState)) {
//                    appCacheDir = getExternalCacheDir(context);
//                }
//            }
            if (Environment.MEDIA_MOUNTED.equals(externalStorageState)) {
                appCacheDir = getExternalCacheDir(context);
            }
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        File appCacheDir = getCacheDirectory(context, true);
        File individualCacheDir = new File(appCacheDir, uniqueName);
        if (!individualCacheDir.exists()) {
            if (!individualCacheDir.mkdir()) {
                individualCacheDir = appCacheDir;
            }
        }
        return individualCacheDir;
    }

    public static boolean saveBitmapToFile(Bitmap bitmap, String filePath) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(out);
        }
        return false;
    }

    public static void saveBitmapToFile(Bitmap bitmap, File file, int quality) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(out);
        }
    }

    public static boolean assetExists(Context context, String path) {
        boolean bAssetOk = false;
        InputStream stream = null;
        try {
            stream = context.getAssets().open(path);
            stream.close();
            bAssetOk = true;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            FileHelper.close(stream);
        }
        return bAssetOk;
    }

    public static boolean isLocalUrl(String url) {
        return (url != null && (url.startsWith("/") || url.toLowerCase().startsWith("file:")));
    }

    public static String getName(String inputFile) {
        int separatorIndex = inputFile.lastIndexOf(File.separator);
        return (separatorIndex < 0) ? inputFile : inputFile.substring(separatorIndex + 1, inputFile.length());
    }

    private static boolean copy(String inputFile, String outputFile) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(inputFile);
            out = new FileOutputStream(outputFile);

            byte[] buffer = new byte[4096];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            return true;
        } catch (FileNotFoundException fnfe1) {

        } catch (Exception e) {

        } finally {
            close(in);
            close(out);
        }

        return false;
    }

    // outputPath MUST have a slash("/") at the end
    public static String moveFile(String inputFile, String outputPath) {
        String outFile = outputPath + getName(inputFile);
        if (inputFile.equals(outFile)) {
            return outFile;
        }

        File dir = new File(outputPath);
        ensureDirExists(dir);

        File to = new File(outFile);
        File from = new File(inputFile);

        if (!from.renameTo(to)) {
            if (copy(inputFile, outFile)) {
                deleteFile(from);
                return outFile;
            }
            return null;
        } else {
            return outFile;
        }
    }

    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
    public static boolean generateImage(String imgStr, String path) {
        if(imgStr == null){
            return false;
        }
//        BASE64Decoder decoder = new BASE64Decoder();
        try{
            //解密
//            byte[] b = decoder.decodeBuffer(imgStr);
            byte[] b = Base64.decode(imgStr, Base64.DEFAULT);
            //处理数据
            for (int i = 0;i<b.length;++i){
                if(b[i]<0){
                    b[i]+=256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
     * @CreateTime:
     * @return
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    /**
     * @param updateDir  就是可以执行的文件
     *void  修改文件的权限，可读、可写、可执行
     * @date 2015年9月13日
     * @author liuyonghong
     */
//    public static void setUpdateDir(File updateDir) {
//        try {
//            Process p = Runtime.getRuntime().exec("chmod 777 " +  updateDir );
//            int status = p.waitFor();
//            p.destroy();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
    public static String getFileNameByUrl(String url){
        return MD5.computeMd5HexString(url);
    }
}
