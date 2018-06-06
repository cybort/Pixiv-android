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

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters.PixivAdapterGrid;
import com.example.administrator.essim.api.AppApiPixivService;
import com.example.administrator.essim.api.OAuthSecureService;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.PixivOAuthResponse;
import com.example.administrator.essim.response.RecommendResponse;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.utils.Common;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.HashMap;
import java.util.List;

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
        mSharedPreferences = Common.getLocalDataSet(mContext);
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
                    next_url = Reference.sRecommendResponse.getNext_url();
                    Reference.sFragmentPixivRight.getData();
                    initAdapter(Reference.sRecommendResponse.getIllusts());
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
                next_url = Reference.sRecommendResponse.getNext_url();
                initAdapter(Reference.sRecommendResponse.getIllusts());
                mProgressBar.setVisibility(View.INVISIBLE);
                mPullToRefreshView.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RecommendResponse> call, Throwable throwable) {

            }
        });
    }

    private void initAdapter(List<IllustsBean> illustsBeans) {
        mPixivAdapter = new PixivAdapterGrid(illustsBeans, mContext);
        mPixivAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int viewType) {
                if (position == -1) {
                    getNextData();
                } else if (viewType == 0) {
                    Reference.sIllustsBeans = Reference.sRecommendResponse.getIllusts();
                    Intent intent = new Intent(mContext, ViewPagerActivity.class);
                    intent.putExtra("which one is selected", position);
                    mContext.startActivity(intent);
                } else if (viewType == 1) {
                    if (!illustsBeans.get(position).isIs_bookmarked()) {
                        ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                        view.startAnimation(Common.getAnimation());
                        Common.postStarIllust(position, illustsBeans,
                                mSharedPreferences.getString("Authorization", ""), mContext, "public");
                    } else {
                        ((ImageView) view).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        view.startAnimation(Common.getAnimation());
                        Common.postUnstarIllust(position, illustsBeans,
                                mSharedPreferences.getString("Authorization", ""), mContext);
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!illustsBeans.get(position).isIs_bookmarked()) {
                    ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                    Common.postStarIllust(position, illustsBeans,
                            mSharedPreferences.getString("Authorization", ""), mContext, "private");
                }
            }
        });
        mRecyclerView.setAdapter(mPixivAdapter);
    }

    private void reLogin() {
        Snackbar.make(mRecyclerView, "获取登录信息, 请稍候", Snackbar.LENGTH_SHORT).show();
        HashMap localHashMap = new HashMap();
        localHashMap.put("client_id", "KzEZED7aC0vird8jWyHM38mXjNTY");
        localHashMap.put("client_secret", "W9JZoJe00qPvJsiyCGT3CCtC6ZUtdpKpzMbNlUGP");
        localHashMap.put("grant_type", "password");
        localHashMap.put("username", mSharedPreferences.getString("useraccount", ""));
        localHashMap.put("password", mSharedPreferences.getString("password", ""));
        Call<PixivOAuthResponse> call = new RestClient().getretrofit_OAuthSecure().create(OAuthSecureService.class).postAuthToken(localHashMap);
        call.enqueue(new Callback<PixivOAuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<PixivOAuthResponse> call, retrofit2.Response<PixivOAuthResponse> response) {
                PixivOAuthResponse pixivOAuthResponse = response.body();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                try {
                    assert pixivOAuthResponse != null;
                    String localStringBuilder = "Bearer " +
                            pixivOAuthResponse.getResponse().getAccess_token();
                    editor.putString("Authorization", localStringBuilder);
                    editor.apply();
                    getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(mRecyclerView, "出了点小问题", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PixivOAuthResponse> call, @NonNull Throwable throwable) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPixivAdapter != null) {
            mPixivAdapter.notifyDataSetChanged();
        }
    }
}
