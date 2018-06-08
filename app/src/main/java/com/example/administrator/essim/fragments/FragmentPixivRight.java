package com.example.administrator.essim.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.SearchTagActivity;
import com.example.administrator.essim.adapters.TrendingtagAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.response.TrendingtagResponse;
import com.example.administrator.essim.utils.Common;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentPixivRight extends BaseFragment {

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private TrendingtagAdapter mPixivAdapter;
    private SharedPreferences mSharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pixiv_right, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        Reference.sFragmentPixivRight = this;
        mProgressBar = v.findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = v.findViewById(R.id.pixiv_recy);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mSharedPreferences = Common.getLocalDataSet(mContext);
    }

    public void getData() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<TrendingtagResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getIllustTrendTags(mSharedPreferences.getString("Authorization", ""));
        call.enqueue(new Callback<TrendingtagResponse>() {
            @Override
            public void onResponse(Call<TrendingtagResponse> call, retrofit2.Response<TrendingtagResponse> response) {
                Reference.sTrendingtagResponse = response.body();
                try {
                    mPixivAdapter = new TrendingtagAdapter(Reference.sTrendingtagResponse.getTrend_tags(), mContext);
                    mPixivAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, int viewType) {
                            Intent intent = new Intent(mContext, SearchTagActivity.class);
                            intent.putExtra("what is the keyword", Reference.sTrendingtagResponse.getTrend_tags()
                                    .get(position).getTag());
                            mContext.startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });
                    mRecyclerView.setAdapter(mPixivAdapter);
                    mProgressBar.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TrendingtagResponse> call, Throwable throwable) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
