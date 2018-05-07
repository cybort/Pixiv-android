package com.example.administrator.essim.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.models.PixivIllustItem;
import com.example.administrator.essim.models.Reference;
import com.example.administrator.essim.utils.Common;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchIDActivity extends AppCompatActivity {

    private String id;
    private Button mButton;
    private Context mContext;
    private TextView mTextView;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private File parentFile, realFile;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        mContext = this;
        Intent intent = getIntent();
        id = intent.getStringExtra("what is the id");
        Toolbar toolbar = findViewById(R.id.toolbar_pixiv);
        toolbar.setNavigationOnClickListener(view -> finish());
        mProgressBar = findViewById(R.id.my_progress);
        mImageView = findViewById(R.id.pixiv_image);
        mTextView = findViewById(R.id.illust_title);
        mButton = findViewById(R.id.down_load);
        mButton.setOnClickListener(view -> {
            parentFile = new File(Environment.getExternalStorageDirectory().getPath(), "PixivPictures");
            if (!parentFile.exists()) {
                parentFile.mkdir();
                runOnUiThread(() -> TastyToast.makeText(mContext, "文件夹创建成功~",
                        TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show());
            }
            realFile = new File(parentFile.getPath(), Reference.sPixivIllustItem.response.get(0).getTitle() + "_" +
                    Reference.sPixivIllustItem.response.get(0).getId() + "_" +
                    String.valueOf(0) + ".jpeg");
            if (realFile.exists()) {
                runOnUiThread(() -> TastyToast.makeText(mContext, "该文件已存在~",
                        TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show());
            } else {
                MyAsyncTask asyncTask = new MyAsyncTask();
                asyncTask.execute(Reference.sPixivIllustItem.response.get(0).image_urls.getLarge());
            }
        });
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("提示信息");
        progressDialog.setMessage("正在下载...");
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", (dialog, which) -> progressDialog.dismiss());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
        getData(Common.illust_item + id);
    }


    public void getData(String address) {
        mProgressBar.setVisibility(View.VISIBLE);
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mProgressBar.setVisibility(View.GONE);
                runOnUiThread(() -> TastyToast.makeText(mContext,
                        "数据加载失败", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show());
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Gson gson = new Gson();
                Reference.sPixivIllustItem = gson.fromJson(responseData, PixivIllustItem.class);
                runOnUiThread(() -> {
                    if (Reference.sPixivIllustItem.response != null) {
                        Glide.with(mContext).load(Reference.sPixivIllustItem.response.get(0).image_urls.getPx_480mw())
                                .into(mImageView);
                        mTextView.setText(Reference.sPixivIllustItem.response.get(0).getTitle());
                    } else {

                        TastyToast.makeText(mContext, "再怎么找也找不到了",
                                TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                    }
                    mProgressBar.setVisibility(View.INVISIBLE);
                });
            }
        });
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
                FileOutputStream outputStream = new FileOutputStream(realFile);
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
                Common.refreshAlbum(mContext, realFile);
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
            runOnUiThread(() -> TastyToast.makeText(mContext, "下载完成~",
                    TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show());
        }
    }
}
