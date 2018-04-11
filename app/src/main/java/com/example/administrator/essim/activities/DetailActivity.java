package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments.FragmentPixiv;
import com.example.administrator.essim.fragments.FragmentPixivLeft;
import com.example.administrator.essim.models.PixivMember;
import com.example.administrator.essim.utils.Common;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class DetailActivity extends AppCompatActivity {

    private Context mContext;
    private PixivMember mPixivMember;
    private ImageView ivBackGround;
    private CircleImageView mCircleImageView;
    private ImageView mImageView;
    private TextView mTextView, mTextView2, mTextView3, mTextView4;
    private int index;
    private final String url = "https://api.imjad.cn/pixiv/v1/?type=member&id=";
    private android.support.v7.widget.Toolbar mToolbar;
    private NestedScrollView mNestedScrollView;
    private float trans;
    private float scroll_height;
    private float scroll_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_detail);
        mContext = this;
        Intent intent = getIntent();
        index = intent.getIntExtra("which one is selected", 0);
        scroll_height = getResources().getDimensionPixelOffset(R.dimen.scroll_height);
        mToolbar = findViewById(R.id.details_toolbar);
        mToolbar.getBackground().mutate().setAlpha(0);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(index)
        .work.user.getName()+"的主页");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mNestedScrollView = findViewById(R.id.nested_view);
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < scroll_height) {
                    trans = scrollY / scroll_height;
                    mToolbar.getBackground().mutate().setAlpha((int) (trans * 255));
                } else {
                    if (mToolbar.getBackground().getAlpha() != 255) {
                        mToolbar.getBackground().mutate().setAlpha(255);
                        Log.d("^^^^%%%%", "绘制状态栏");
                    }
                }
            }
        });
        initView();
        getData("https://api.imjad.cn/pixiv/v1/?type=member&id=" + FragmentPixivLeft.mPixivRankItem.response.get(0)
        .works.get(index).work.user.getId());
    }

    private void initView() {
        mCircleImageView = findViewById(R.id.author_circle_img);

        mImageView = findViewById(R.id.author_bg_blur);
        Glide.with(mContext).load(FragmentPixivLeft.mPixivRankItem.response.get(0)
                .works.get(index).work.image_urls.getPx_480mw())
                .bitmapTransform(new BlurTransformation(mContext, 20, 2))
                .into(mImageView);
        mTextView = findViewById(R.id.author_followers);
        mTextView2 = findViewById(R.id.author_follow);
    }

    private void refreshLayout() {
        mTextView.setText(getString(R.string.author_followers, mPixivMember.response.get(0).stats.getFollowing()));
        mTextView2.setText(getString(R.string.author_follow, mPixivMember.response.get(0).stats.getFriends()));
    }


    private void getData(final String address) {
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Gson gson = new Gson();
                mPixivMember = gson.fromJson(responseData, PixivMember.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout();
                    }
                });
            }
        });
    }


}