package com.example.administrator.essim.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.provider.DocumentFile;

import com.example.administrator.essim.response.IllustsBean;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*
┌───┐   ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
│Esc│   │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│  ┌┐    ┌┐    ┌┐
└───┘   └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘  └┘    └┘    └┘
┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐ ┌───┬───┬───┐ ┌───┬───┬───┬───┐
│~ `│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp │ │Ins│Hom│PUp│ │N L│ / │ * │ - │
├───┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤ ├───┼───┼───┤ ├───┼───┼───┼───┤
│ Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ │ │Del│End│PDn│ │ 7 │ 8 │ 9 │   │
├─────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤ └───┴───┴───┘ ├───┼───┼───┤ + │
│ Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │               │ 4 │ 5 │ 6 │   │
├──────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤     ┌───┐     ├───┼───┼───┼───┤
│ Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │     │ ↑ │     │ 1 │ 2 │ 3 │   │
├─────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤ ┌───┼───┼───┐ ├───┴───┼───┤ E││
│ Ctrl│    │Alt │         Space         │ Alt│    │    │Ctrl│ │ ← │ ↓ │ → │ │   0   │ . │←─┘│
└─────┴────┴────┴───────────────────────┴────┴────┴────┴────┘ └───┴───┴───┘ └───────┴───┴───┘
*/

// 尽量多写了一些注释，主要是因为可能@Notsfsssf同学会来看

public class SDDownloadTask extends AsyncTask<String, Integer, Bitmap> {

    private ProgressDialog progressDialog;
    private File realFile;
    private Context mContext;
    private IllustsBean mIllustsBeans;
    private SharedPreferences mSharedPreferences;
    private DocumentFile finalDocument;

    public SDDownloadTask(File file, Context context, IllustsBean illustsBean, SharedPreferences sharedPreferences) {
        realFile = file;
        mContext = context;
        mIllustsBeans = illustsBean;
        mSharedPreferences = sharedPreferences;
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
        //能够下载到可插拔SD卡的最重要一步，是生成目标文件的DocumentFile
        try {
            DocumentFile document = DocumentFile.fromTreeUri(mContext, Uri.parse(mSharedPreferences.getString("treeUri", "")));
            String picturePath = mSharedPreferences.getString("download_path", "") + "/" + realFile.getName();
            Common.showLog(picturePath);
            String[] parts = picturePath.split("/");
            for (int i = 3; i < parts.length; i++) {
                DocumentFile nextDocument = document.findFile(parts[i]);
                if (nextDocument == null) {
                    if (i < parts.length - 1) {
                        nextDocument = document.createDirectory(parts[i]);
                    } else {
                        nextDocument = document.createFile(null, parts[i]);
                    }
                }
                document = nextDocument;
            }
            // 这一步，将会获取到目标文件的DocumentFile
            finalDocument = document;
        } catch (Exception e) {
            ((Activity) mContext).runOnUiThread(() -> TastyToast.makeText(mContext, "请先配置SD卡的读写权限!",
                    TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show());
            return null;
        }
        // 之后就好办了，直接输出流，到finalDocument
        try {
            FileOutputStream outStream = (FileOutputStream) mContext.getContentResolver().openOutputStream(finalDocument.getUri());
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 反防盗链
            connection.setRequestProperty("Referer", "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" +
                    mIllustsBeans.getId());
            connection.connect();
            // 获取输入流
            InputStream inputStream = connection.getInputStream();
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
                outStream.write(data, 0, len);
            }
            outStream.close();
            inputStream.close();
            Common.sendBroadcast(mContext, realFile);
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    finalDocument.getUri()));
            //以上两种方法有可能均无法使相册更新可插拔SD卡上的图片
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((Activity) mContext).runOnUiThread(() -> TastyToast.makeText(mContext, "下载完成~",
                TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show());
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        progressDialog.dismiss();
        mContext = null;
    }
}