package com.example.administrator.essim.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments.FragmentImageDetail;
import com.example.administrator.essim.interfaces.DownLoadOrigin;
import com.example.administrator.essim.models.PixivIllustItem;
import com.example.administrator.essim.models.Reference;
import com.example.administrator.essim.utils.Common;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ImageDetailActivity extends AppCompatActivity {

    public int index;
    public ViewPager mViewPager;
    private String fromWhere;
    private TextView mTextView, mTextView2;
    private DownLoadOrigin mDownLoadOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_image_detail);

        Intent intent = getIntent();
        index = intent.getIntExtra("which one is selected", 0);
        fromWhere = intent.getStringExtra("where is from");
        mTextView = findViewById(R.id.image_order);
        mTextView2 = findViewById(R.id.download_origin);
        mTextView2.setOnClickListener(view -> {
            if (mDownLoadOrigin != null) {
                mDownLoadOrigin.downOriginImage(mViewPager.getCurrentItem());
            }
        });
        mViewPager = findViewById(R.id.view_pager);
        if (fromWhere.equals("RankList")) {
            Common.showLog("RANKLIST, RANKLIST");
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == 2) {
                        mTextView.setText(String.format("%s/%s", String.valueOf(mViewPager.getCurrentItem() + 1),
                                Reference.sPixivRankItem.response.get(0).works.get(index).work.page_count));
                    }
                }
            });
            mTextView.setText(String.format("1/%s",
                    Reference.sPixivRankItem.response.get(0).works.get(index).work.page_count));
            getData("https://api.imjad.cn/pixiv/v1/?type=illust&id=" + Reference.sPixivRankItem.response.get(0)
                    .works.get(index).work.getId());
        } else if (fromWhere.equals("WorkList")) {
            Common.showLog("WorkList, WorkList");
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == 2) {
                        mTextView.setText(String.format("%s/%s", String.valueOf(mViewPager.getCurrentItem() + 1),
                                Reference.tempWork.response.get(index).getPage_count()));
                    }
                }
            });
            mTextView.setText(String.format("1/%s",
                    Reference.tempWork.response.get(index).getPage_count()));
            getData("https://api.imjad.cn/pixiv/v1/?type=illust&id=" + Reference.tempWork.response.get(index)
                    .getId());
        }
    }

    public void setDownLoadOrigin(DownLoadOrigin downLoadOrigin) {
        this.mDownLoadOrigin = downLoadOrigin;
    }

    private void getData(String address) {
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> TastyToast.makeText(ImageDetailActivity.this, "数据加载失败", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Gson gson = new Gson();
                Reference.sPixivIllustItem = gson.fromJson(responseData, PixivIllustItem.class);
                if (fromWhere.equals("RankList")) {
                    runOnUiThread(() -> mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                        @Override
                        public Fragment getItem(int position) {
                            if (Integer.valueOf(Reference.sPixivRankItem.response.get(0).works.get(index).work.page_count) != 1) {
                                return FragmentImageDetail.newInstance(position);
                            } else {
                                return FragmentImageDetail.newInstance(-1);
                            }
                        }

                        @Override
                        public int getCount() {
                            if (Integer.valueOf(Reference.sPixivRankItem.response.get(0).works.get(index).work.page_count) != 1) {
                                return Integer.valueOf(Reference.sPixivRankItem.response.get(0).works.get(index).work.page_count);
                            } else {
                                return 1;
                            }
                        }
                    }));
                } else {
                    runOnUiThread(() -> mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                        @Override
                        public Fragment getItem(int position) {
                            if (Integer.valueOf(Reference.tempWork.response.get(index).getPage_count()) != 1) {
                                return FragmentImageDetail.newInstance(position);
                            } else {
                                return FragmentImageDetail.newInstance(-1);
                            }
                        }

                        @Override
                        public int getCount() {
                            if (Integer.valueOf(Reference.tempWork.response.get(index).getPage_count()) != 1) {
                                return Integer.valueOf(Reference.tempWork.response.get(index).getPage_count());
                            } else {
                                return 1;
                            }
                        }
                    }));
                }
            }
        });
    }
}