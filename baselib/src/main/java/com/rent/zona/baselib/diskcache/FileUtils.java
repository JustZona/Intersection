package com.rent.zona.baselib.diskcache;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtils {
    public static final String TAG = "FileUtils";

    /**
     * Returns the remainder of 'reader' as a string, closing it when done.
     */
    public static String readFully(Reader reader) throws IOException {
        try {
            StringWriter writer = new StringWriter();
            char[] buffer = new char[1024];
            int count;
            while ((count = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, count);
            }
            return writer.toString();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the ASCII characters up to but not including the next "\r\n", or
     * "\n".
     *
     * @throws EOFException if the stream is exhausted before the next newline
     *                      character.
     */
    public static String readAsciiLine(InputStream in) throws IOException {
        StringBuilder result = new StringBuilder(80);
        while (true) {
            int c = in.read();
            if (c == -1) {
                throw new EOFException();
            } else if (c == '\n') {
                break;
            }

            result.append((char) c);
        }
        int length = result.length();
        if (length > 0 && result.charAt(length - 1) == '\r') {
            result.setLength(length - 1);
        }
        return result.toString();
    }

    /**
     * Closes 'closeable', ignoring any checked exceptions. Does nothing if 'closeable' is null.
     */
    public static void closeQuietly(Closeable closeable) throws IOException {
        if (closeable != null) {
            closeable.close();
        }
    }

    /**
     * Try to delete directory in a fast way.
     */
    public static void deleteDirectoryQuickly(File dir) throws IOException {

        if (!dir.exists()) {
            return;
        }
        final File to = new File(dir.getAbsolutePath() + System.currentTimeMillis());
        noCareResultRenameTo(dir, to);
        if (!dir.exists()) {
            // rebuild
            noCareResultMkdirs(dir);
        }

        // try to run "rm -r" to remove the whole directory
        if (to.exists()) {
            String deleteCmd = "rm -r " + to;
            Runtime runtime = Runtime.getRuntime();
            try {
                Process process = runtime.exec(deleteCmd);
                process.waitFor();
            } catch (IOException e) {

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!to.exists()) {
            return;
        }
        deleteDirectoryRecursively(to);
        if (to.exists()) {
            noCareResultDelete(to);
        }
    }

    public static void chmod(String mode, String path) {
        try {
            String command = "chmod " + mode + " " + path;
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command);
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * recursively delete
     *
     * @param dir
     * @throws IOException
     */
    public static void deleteDirectoryRecursively(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new IllegalArgumentException("not a directory: " + dir);
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteDirectoryRecursively(file);
            }
            if (!file.delete()) {
                throw new IOException("failed to delete file: " + file);
            }
        }
    }

    public static void deleteIfExists(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    public static boolean writeString(String filePath, String content) {
        File file = new File(filePath);
        if (!file.getParentFile().exists())
            noCareResultMkdirs(file.getParentFile());

        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            osw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {
            }
        }
        return false;
    }

    public static String readString(String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            return null;

        FileInputStream fileInput = null;
        FileChannel channel = null;
        try {
            fileInput = new FileInputStream(filePath);
            channel = fileInput.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
            channel.read(buffer);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(buffer.array());
            String string = null;
            string = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
            return string;
        } catch (Exception e) {
        } finally {

            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void noCareResultMkdirs(File dirFile) {
        boolean result = dirFile.mkdirs();
        if (!result) {
            Log.d(TAG, "file mkdirs failure");
        }
    }

    public static void noCareResultDelete(File file) {
        boolean result = file.delete();
        if (!result) {
            Log.d(TAG, "file delete failure");
        }
    }

    public static void noCareResultRenameTo(File fromFile, File toFile) {
        boolean result = fromFile.renameTo(toFile);
        if (!result) {
            Log.d(TAG, "file renameTo failure");
        }
    }

    public static File saveBitmap(Bitmap bitmap, String imageName) {
        File f = new File(Environment.getExternalStorageDirectory(), imageName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }
}
