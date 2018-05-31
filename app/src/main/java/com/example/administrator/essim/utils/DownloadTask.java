package com.example.administrator.essim.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.administrator.essim.response.IllustsBean;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Integer, Bitmap> {

    private ProgressDialog progressDialog;
    private File realFile;
    private Context mContext;
    private IllustsBean mIllustsBeans;

    public DownloadTask(File file, Context context, IllustsBean illustsBean) {
        realFile = file;
        mContext = context;
        mIllustsBeans = illustsBean;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("提示信息");
        progressDialog.setMessage("正在下载...");
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", (dialog, which) -> progressDialog.dismiss());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
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
            Common.sendBroadcast(mContext, realFile);   //通知相册更新最新下载的图片
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
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        progressDialog.dismiss();
        ((Activity) mContext).runOnUiThread(() -> TastyToast.makeText(mContext, "下载完成~",
                TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show());
        mContext = null;
    }
}