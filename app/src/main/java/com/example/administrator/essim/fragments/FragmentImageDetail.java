package com.example.administrator.essim.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ImageDetailActivity;
import com.example.administrator.essim.models.Reference;
import com.example.administrator.essim.utils.Common;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FragmentImageDetail extends BaseFragment {

    private int index;
    private String pathOne, pathTwo;
    private File parentFile, realFile;
    private ProgressDialog progressDialog;

    public static FragmentImageDetail newInstance(int position) {
        Bundle args = new Bundle();
        args.putSerializable("index", position);
        FragmentImageDetail fragment = new FragmentImageDetail();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_detail, container, false);
        index = (int) getArguments().getSerializable("index");
        ImageView imageView = view.findViewById(R.id.image);
        imageView.setOnLongClickListener(view1 -> {
            createDialog(index);
            return false;
        });
        Glide.get(mContext).clearMemory();
        if (index == -1) {
            Glide.with(mContext)
                    .load(Reference.sPixivIllustItem.response.get(0).image_urls.getPx_480mw())
                    .into(imageView);
        } else {
            Glide.with(mContext)
                    .load(Reference.sPixivIllustItem.response.get(0).metadata.pages.get(index).image_urls.getPx_480mw())
                    .into(imageView);
        }
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("提示信息");
        progressDialog.setMessage("正在下载...");
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", (dialog, which) -> progressDialog.dismiss());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
        ((ImageDetailActivity) getActivity()).setDownLoadOrigin(i ->
                createDialog(((ImageDetailActivity) getActivity()).mViewPager.getCurrentItem()));
        return view;
    }

    private void createDialog(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle("下载原图");
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            parentFile = new File(Environment.getExternalStorageDirectory().getPath(), "PixivPictures");
            if (!parentFile.exists()) {
                parentFile.mkdir();
                mActivity.runOnUiThread(() -> TastyToast.makeText(mContext, "文件夹创建成功~",
                        TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show());
            }
            if (Reference.sPixivIllustItem.response.get(0).getPage_count().equals(String.valueOf(1))) {
                realFile = new File(parentFile.getPath(), Reference.sPixivIllustItem.response.get(0).getTitle() + "_" +
                        Reference.sPixivIllustItem.response.get(0).getId() + "_" +
                        String.valueOf(0) + ".jpeg");
                if (realFile.exists()) {
                    mActivity.runOnUiThread(() -> TastyToast.makeText(mContext, "该文件已存在~",
                            TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show());
                } else {
                    MyAsyncTask asyncTask = new MyAsyncTask();
                    asyncTask.execute(Reference.sPixivIllustItem.response.get(0).image_urls.getLarge());
                }
            } else {
                realFile = new File(parentFile.getPath(), Reference.sPixivIllustItem.response.get(0).getTitle() + "_" +
                        Reference.sPixivIllustItem.response.get(0).getId() + "_" +
                        String.valueOf(index) + ".jpeg");
                if (realFile.exists()) {
                    mActivity.runOnUiThread(() -> TastyToast.makeText(mContext, "该文件已存在~",
                            TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show());
                } else {
                    MyAsyncTask asyncTask = new MyAsyncTask();
                    asyncTask.execute(Reference.sPixivIllustItem.response.get(0).metadata.pages.get(index).image_urls.getLarge());
                }
            }
        })
                .setNegativeButton("取消", (dialogInterface, i) -> {
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            InputStream inputStream = null;

            Bitmap bitmap = null;
            try {
                FileOutputStream outputStream;
                outputStream = new FileOutputStream(realFile);
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Referer", "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" +
                        Reference.sPixivIllustItem.response.get(0).getId());
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
                if (index == -1) {
                    Common.sendBroadcast(mContext, realFile, index, 2);
                } else {
                    Common.sendBroadcast(mContext, realFile, index, 3);
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
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressDialog.dismiss();
            mActivity.runOnUiThread(() -> TastyToast.makeText(mContext, "下载完成~",
                    TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show());
        }
    }
}
