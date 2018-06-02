package com.example.administrator.essim.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.activities.DirTraversal;
import com.example.administrator.essim.activities.ZipUtils;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.UgoiraMetadataResponse;
import com.example.administrator.essim.utils.Common;
import com.lchad.gifflen.Gifflen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;

public class ZIPDownloadTask extends AsyncTask<String, Integer, Bitmap> {

    private File realFile;
    private File realGifFile;
    private Context mContext;
    private int picDelay;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private IllustsBean mIllustsBeans;

    public ZIPDownloadTask(File file, Context context, IllustsBean illustsBean,
                           int delay, ProgressBar progressBar, ImageView imageView) {
        realFile = file;
        mContext = context;
        picDelay = delay;
        mProgressBar = progressBar;
        mIllustsBeans = illustsBean;
        mImageView = imageView;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        InputStream inputStream = null;

        Bitmap bitmap = null;
        try {
            FileOutputStream outputStream = new FileOutputStream(realFile);
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Referer", "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" +
                    mIllustsBeans.getId());
            connection.connect();
            // 获取输入流
            inputStream = connection.getInputStream();
            // 获取文件流大小，用于更新进度
            long file_length = connection.getContentLength();
            int len;
            int total_length = 0;
            byte[] data = new byte[1024];
            while ((len = inputStream.read(data)) != -1) {
                total_length += len;
                int value = (int) ((total_length / (float) file_length) * 100);
                // 调用update函数，更新进度
                publishProgress(value);
                outputStream.write(data, 0, len);
            }
            outputStream.close();
            //下载完成开始解压
            File file = DirTraversal.getFilePath("/storage/emulated/0/PixivPictures/gifZipFile/", realFile.getName());
            try {
                // 检测输出文件夹是否存在
                File parentFile = new File("/storage/emulated/0/PixivPictures/gifUnzipFile");
                if (!parentFile.exists()) {
                    parentFile.mkdir();
                }
                File sencondParent = new File("/storage/emulated/0/PixivPictures/gifUnzipFile/" + mIllustsBeans.getId());
                if (!sencondParent.exists()) {
                    sencondParent.mkdir();
                    List<File> allPicOfGif = ZipUtils.upZipFile(file, sencondParent.getPath());
                    Gifflen gifflen = new Gifflen.Builder()
                            .width(mIllustsBeans.getWidth())
                            .height(mIllustsBeans.getHeight())
                            .delay(picDelay)
                            .listener(new Gifflen.OnEncodeFinishListener() {
                                @Override
                                public void onEncodeFinish(String path) {
                                    Toast.makeText(mContext, "已保存gif到" + path, Toast.LENGTH_LONG).show();
                                }
                            })
                            .build();
                    realGifFile = new File(sencondParent.getPath(), mIllustsBeans.getTitle() + ".gif");
                    gifflen.encode(realGifFile.getPath(), allPicOfGif);
                } else {
                    Common.showLog("什么也不做");
                }
                // 解压到输出文件夹
            } catch (ZipException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mProgressBar.setVisibility(View.INVISIBLE);
        Glide.with(mContext).load(realGifFile).into(mImageView);
        mContext = null;
        mProgressBar = null;
    }
}