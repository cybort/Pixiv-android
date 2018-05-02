package com.example.administrator.essim.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ImageDetailActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.anotherProj.CloudMainActivity;
import com.example.administrator.essim.models.Reference;
import com.example.administrator.essim.utils.Common;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.gujun.android.taggroup.TagGroup;

public class FragmentWorkItem extends BaseFragment {

    private int index;
    private MyAsyncTask asyncTask;
    private ProgressDialog progressDialog;
    private File parentFile, realFile;
    private ImageView mImageView, mImageView2;
    private CardView mCardView, mCardView2, mCardView3, mCardView4;
    private TextView mTextView, mTextView2, mTextView3, mTextView4, mTextView5, mTextView6, mTextView7, mTextView8, mTextView9;

    public static FragmentWorkItem newInstance(int position) {
        Bundle args = new Bundle();
        args.putSerializable("index", position);
        FragmentWorkItem fragment = new FragmentWorkItem();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_item, container, false);
        index = (int) getArguments().getSerializable("index");
        if (index == ((ViewPagerActivity) getActivity()).getIndexNow()) {
            setUserVisibleHint(true);
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        reFreshLayout(view);
        return view;
    }

    private void reFreshLayout(View view) {
        mImageView = view.findViewById(R.id.item_background_img);
        mImageView2 = view.findViewById(R.id.detail_img);
        ViewGroup.LayoutParams params = mImageView2.getLayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        params.height = (((dm.widthPixels - getResources().getDimensionPixelSize(R.dimen.thirty_two_dp)) *
                Reference.tempWork.response.get(index).getHeight()) /
                Reference.tempWork.response.get(index).getWidth());
        mImageView2.setLayoutParams(params);
        mImageView2.setOnClickListener(view12 -> {
            Intent intent = new Intent(mContext, ImageDetailActivity.class);
            intent.putExtra("which one is selected", index);
            intent.putExtra("where is from", "WorkList");
            mContext.startActivity(intent);
        });
        Glide.get(mContext).clearMemory();
        Glide.with(getContext())
                .load(Reference.tempWork.response.get(index).image_urls.getPx_480mw())
                .bitmapTransform(new BlurTransformation(getContext(), 20, 2))
                .into(mImageView);
        Glide.with(getContext())
                .load(Reference.tempWork.response.get(index).image_urls.getPx_480mw())
                .into(mImageView2);
        mTextView = view.findViewById(R.id.detail_author);
        mTextView2 = view.findViewById(R.id.detail_img_size);
        mTextView3 = view.findViewById(R.id.detail_create_time);
        mTextView4 = view.findViewById(R.id.viewed);
        mTextView5 = view.findViewById(R.id.liked);
        mTextView6 = view.findViewById(R.id.illust_id);
        mTextView7 = view.findViewById(R.id.author_id);
        mTextView8 = view.findViewById(R.id.all_item_size);
        mTextView9 = view.findViewById(R.id.exit_this);
        mCardView = view.findViewById(R.id.card_first);
        mCardView2 = view.findViewById(R.id.card_second);
        mCardView3 = view.findViewById(R.id.card_left);
        mCardView3.setOnClickListener(v -> {
            parentFile = new File(Environment.getExternalStorageDirectory().getPath(), "PixivPictures");
            if (!parentFile.exists()) {
                parentFile.mkdir();
                mActivity.runOnUiThread(() -> TastyToast.makeText(mContext, "文件夹创建成功~",
                        TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show());
            }
            realFile = new File(parentFile.getPath(), Reference.tempWork.response.get(index).getTitle() + "_" + Reference.tempWork.response.get(index).getId() + "_" +
                    String.valueOf(0) + ".jpeg");
            if (realFile.exists()) {
                mActivity.runOnUiThread(() -> TastyToast.makeText(mContext, "该文件已存在~",
                        TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show());
            } else {
                asyncTask = new MyAsyncTask();
                asyncTask.execute(Reference.tempWork.response.get(index).image_urls.getLarge());
            }
        });
        mCardView4 = view.findViewById(R.id.card_right);
        if (((ViewPagerActivity) getActivity()).where_is_from.equals("FragmentTagResult")) {
            mTextView9.setText(getString(R.string.more_files));
            mCardView4.setOnClickListener(view1 -> {
                Intent intent = new Intent(mContext, CloudMainActivity.class);
                intent.putExtra("which one is selected", index);
                intent.putExtra("where is from", "TagResult");
                mContext.startActivity(intent);
            });
        } else {
            mCardView4.setOnClickListener(view1 -> getActivity().finish());
        }
        TagGroup mTagGroup = view.findViewById(R.id.tag_group);
        mTagGroup.setTags(Reference.tempWork.response.get(index).tags);
        mTagGroup.setOnTagClickListener(tag -> {
            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", tag);
            cm.setPrimaryClip(mClipData);
            TastyToast.makeText(mContext, tag + " 已复制到剪切板~", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        });
        mTextView.setText(getString(R.string.string_author, Reference.tempWork.response.get(index)
                .user.getName()));
        mTextView2.setText(getString(R.string.string_full_size, Reference.tempWork.response.get(index).getWidth(),
                Reference.tempWork.response.get(index).getHeight()));
        mTextView3.setText(getString(R.string.string_create_time, Reference.tempWork.response.get(index).getCreated_time()));
        if (Reference.tempWork.response.get(index).stats.getViews_count().length() <= 3) {
            mTextView4.setText(Reference.tempWork.response.get(index).stats.getViews_count());
        } else {
            mTextView4.setText(getString(R.string.string_viewd,
                    Reference.tempWork.response.get(index).stats.getViews_count().substring(0,
                            Reference.tempWork.response.get(index).stats.getViews_count().length() - 3)));
        }
        if (Reference.tempWork.response.get(index).stats.getScored_count().length() <= 3) {
            mTextView5.setText(Reference.tempWork.response.get(index).stats.getScored_count());
        } else {
            mTextView5.setText(getString(R.string.string_viewd,
                    Reference.tempWork.response.get(index).stats.getScored_count()
                            .substring(0, Reference.tempWork.response.get(index).stats.getScored_count().length() - 3)));
        }
        mTextView6.setText(getString(R.string.illust_id,
                Reference.tempWork.response.get(index).getId()));
        mTextView7.setText(getString(R.string.author_id,
                Reference.tempWork.response.get(index).user.getId()));
        mTextView8.setText(String.format("%sP", Reference.tempWork.response.get(index).getPage_count()));
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("提示信息");
        progressDialog.setMessage("正在下载...");
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", (dialog, which) -> progressDialog.dismiss());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null) {
                ((ViewPagerActivity) getActivity()).changeTitle();
            }
        }
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
                        Reference.tempWork.response.get(index).getId());
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
                Common.sendBroadcast(mContext, realFile, index, 1);
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
