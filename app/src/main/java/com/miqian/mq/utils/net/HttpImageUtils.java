package com.miqian.mq.utils.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.miqian.mq.utils.BDebug;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片加载工具
 */
public class HttpImageUtils {
    private static final String TAG = "HttpImageUtils";

    /**
     * 从网络下载图片【bitmap】
     * @param url 图片url
     * @return
     */
    public static Bitmap downloadNetworkBitmap(String url) {
        byte[] data = HttpImageUtils.downloadImageFromNetwork(url);
        if (data == null || data.length == 0) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPurgeable = true;// 允许可清除
        options.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果
        options.inJustDecodeBounds = true;// 获取图片宽高，该属性在decode的时候，仅返回图片的宽高，不生产bitmap对象
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        if (options.outHeight > 800) {
            options.inSampleSize = 2;
            options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 从网络下载图片【字节数组】
     * @param url
     * @return
     */
    public static byte[] downloadImageFromNetwork(String url){
        return downloadImageByOkHttp(url);
    }

    /**
     * 从网络中获取 图片字节码
     *
     * @param url
     *            图片Url
     * @return
     */
    private static byte[] downloadImageByOkHttp(String url) {
        InputStream is = null;
        byte[] data = null;
        BDebug.d(TAG, "downloadImage, url=" + url);
        try {
            Request request = new Request.Builder().url(url).get().build();
            Response response = OkHttpStack.getInstance().newCall(request).execute();
            if (response.isSuccessful()) {
                is = response.body().byteStream();
                data = transStreamToBytes(is,4096);
            }
        } catch (Exception e) {
            BDebug.w(TAG,
                "downloadImageFromNetwork error, url=" + url + "\nerror message=" + e.toString());
        }
        return data;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 将输入流转化为字节数组
     *
     * @param is
     * @param buffSize
     * @return
     */
    private static byte[] transStreamToBytes(InputStream is, int buffSize) {
        if (is == null) {
            return null;
        }
        if (buffSize <= 0) {
            throw new IllegalArgumentException("buffSize can not less than zero.....");
        }
        byte[] data = null;
        byte[] buffer = new byte[buffSize];
        int actualSize = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while ((actualSize = is.read(buffer)) != -1) {
                baos.write(buffer, 0, actualSize);
            }
            data = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 保存bitmap到本地
     *
     * @param bitmap
     * @param localPath
     * @return
     */
    public static boolean bitmapToLocalFile(Bitmap bitmap, String localPath) {
        BDebug.d("bitmap", "" + bitmap);
        BDebug.d("localPath", localPath);
        boolean isLoaded = false;
        FileOutputStream fos = null;
        File f = new File(localPath);
        try {
            f.createNewFile();
            fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            isLoaded = true;
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isLoaded;
    }

    /**
     * 读取本地图片
     *
     * @param localPath
     * @return
     */
    public static Bitmap getBitMap(String localPath) {

        BDebug.d("localPath", localPath);
        Bitmap bitmap = null;
        try {
            File file = new File(localPath);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(localPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
