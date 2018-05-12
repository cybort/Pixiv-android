package com.example.administrator.essim.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.yalantis.phoenix.PullToRefreshView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.MainActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters.PixivAdapterGrid;
import com.example.administrator.essim.api.AppApiPixivService;
import com.example.administrator.essim.api.OAuthSecureService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.PixivOAuthResponse;
import com.example.administrator.essim.response.RecommendResponse;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.utils.Common;
import retrofit2.Call;
import retrofit2.Callback;

public class FragmentPixivLeft extends BaseFragment {

    private String next_url;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private PixivAdapterGrid mPixivAdapter;
    private SharedPreferences mSharedPreferences;
    private PullToRefreshView mPullToRefreshView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pixiv_left, container, false);
        initView(v);
        getData();
        return v;
    }

    private void initView(View v) {
        mProgressBar = v.findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = v.findViewById(R.id.pixiv_recy);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mPixivAdapter.getItemViewType(position) == 2) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mPullToRefreshView = v.findViewById(R.id.pull_wo_refresh);
        mPullToRefreshView.setOnRefreshListener(this::getNextData);
        mSharedPreferences = ((MainActivity) Objects.requireNonNull(getActivity())).mSharedPreferences;
    }

    private void getData() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<RecommendResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getRecommend(mSharedPreferences.getString("Authorization", ""));
        call.enqueue(new Callback<RecommendResponse>() {
            @Override
            public void onResponse(Call<RecommendResponse> call, retrofit2.Response<RecommendResponse> response) {
                Reference.sRecommendResponse = response.body();
                try {
                    mPixivAdapter = new PixivAdapterGrid(Reference.sRecommendResponse.getIllusts(), mContext);
                    next_url = Reference.sRecommendResponse.getNext_url();
                    Reference.sFragmentPixivRight.getData();
                    mPixivAdapter.setOnItemClickListener((view, position, viewType) -> {
                        if (position == -1) {
                            getNextData();
                        } else if (viewType == 0) {
                            Intent intent = new Intent(mContext, ViewPagerActivity.class);
                            intent.putExtra("which one is selected", position);
                            intent.putExtra("all illust", (Serializable) Reference.sRecommendResponse.getIllusts());
                            mContext.startActivity(intent);
                        } else if (viewType == 1) {
                            if (!Reference.sRecommendResponse.getIllusts().get(position).isIs_bookmarked()) {
                                ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                                ((ImageView) view).startAnimation(Common.getAnimation());
                                Common.postStarIllust(position, Reference.sRecommendResponse.getIllusts(),
                                        mSharedPreferences.getString("Authorization", ""), mRecyclerView);
                            } else {
                                ((ImageView) view).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                ((ImageView) view).startAnimation(Common.getAnimation());
                                Common.postUnstarIllust(position, Reference.sRecommendResponse.getIllusts(),
                                        mSharedPreferences.getString("Authorization", ""), mRecyclerView);
                            }
                        }
                    });
                    mRecyclerView.setAdapter(mPixivAdapter);
                    mProgressBar.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    reLogin();
                }
            }

            @Override
            public void onFailure(Call<RecommendResponse> call, Throwable throwable) {

            }
        });
    }

    private void getNextData() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<RecommendResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getNext(mSharedPreferences.getString("Authorization", ""), next_url);
        call.enqueue(new Callback<RecommendResponse>() {
            @Override
            public void onResponse(Call<RecommendResponse> call, retrofit2.Response<RecommendResponse> response) {
                Reference.sRecommendResponse = response.body();
                mPixivAdapter = new PixivAdapterGrid(Reference.sRecommendResponse.getIllusts(), mContext);
                next_url = Reference.sRecommendResponse.getNext_url();
                mPixivAdapter.setOnItemClickListener((view, position, viewType) -> {
                    if (position == -1) {
                        getNextData();
                    } else if (viewType == 0) {
                        Intent intent = new Intent(mContext, ViewPagerActivity.class);
                        intent.putExtra("which one is selected", position);
                        intent.putExtra("all illust", (Serializable) Reference.sRecommendResponse.getIllusts());
                        mContext.startActivity(intent);
                    } else if (viewType == 1) {
                        if (!Reference.sRecommendResponse.getIllusts().get(position).isIs_bookmarked()) {
                            ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                            ((ImageView) view).startAnimation(Common.getAnimation());
                            Common.postStarIllust(position, Reference.sRecommendResponse.getIllusts(),
                                    mSharedPreferences.getString("Authorization", ""), mRecyclerView);
                        } else {
                            ((ImageView) view).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            ((ImageView) view).startAnimation(Common.getAnimation());
                            Common.postUnstarIllust(position, Reference.sRecommendResponse.getIllusts(),
                                    mSharedPreferences.getString("Authorization", ""), mRecyclerView);
                        }
                    }
                });
                mRecyclerView.setAdapter(mPixivAdapter);
                mProgressBar.setVisibility(View.INVISIBLE);
                mPullToRefreshView.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RecommendResponse> call, Throwable throwable) {

            }
        });
    }

    private void reLogin() {
        Snackbar.make(mRecyclerView, "获取登录信息, 请稍候", Snackbar.LENGTH_SHORT).show();
        HashMap localHashMap = new HashMap();
        localHashMap.put("client_id", "KzEZED7aC0vird8jWyHM38mXjNTY");
        localHashMap.put("client_secret", "W9JZoJe00qPvJsiyCGT3CCtC6ZUtdpKpzMbNlUGP");
        localHashMap.put("grant_type", "password");
        localHashMap.put("username", mSharedPreferences.getString("username", ""));
        localHashMap.put("password", mSharedPreferences.getString("password", ""));
        Call<PixivOAuthResponse> call = new RestClient().getretrofit_OAuthSecure().create(OAuthSecureService.class).postAuthToken(localHashMap);
        call.enqueue(new Callback<PixivOAuthResponse>() {
            @Override
            public void onResponse(Call<PixivOAuthResponse> call, retrofit2.Response<PixivOAuthResponse> response) {
                PixivOAuthResponse pixivOAuthResponse = response.body();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                StringBuilder localStringBuilder = new StringBuilder();
                localStringBuilder.append("Bearer ");
                localStringBuilder.append(pixivOAuthResponse.getResponse().getAccess_token());
                editor.putString("Authorization", localStringBuilder.toString());
                editor.apply();
                getData();
            }

            @Override
            public void onFailure(Call<PixivOAuthResponse> call, Throwable throwable) {
            }
        });
    }
}
