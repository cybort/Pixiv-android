package com.example.administrator.essim.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.UserDetailActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters.CollecDetailAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.CollectionResponse;
import com.example.administrator.essim.response.IllustDetailResponse;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.utils.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentCollecDetail extends BaseFragment {

    private String id, title;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    public static FragmentCollecDetail newInstance(String id, String title) {
        Bundle args = new Bundle();
        args.putSerializable("id", id);
        args.putSerializable("title", title);
        FragmentCollecDetail fragment = new FragmentCollecDetail();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collec_detail, container, false);
        id = (String) getArguments().getSerializable("id");
        title = (String) getArguments().getSerializable("title");
        initView(view);
        getSpecialCollection();
        return view;
    }

    private void initView(View v) {
        mProgressBar = v.findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = v.findViewById(R.id.pixiv_recy);
        Toolbar mToolbar = v.findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> Objects.requireNonNull(getActivity()).finish());
        mToolbar.setTitle(title);
        LinearLayoutManager linearLayout = new LinearLayoutManager(mContext);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayout);
        mRecyclerView.setHasFixedSize(true);
        Glide.get(mContext).clearMemory();
    }

    private void getSpecialCollection() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<CollectionResponse> call = new RestClient()
                .getRetrofit_SpecialCollection()
                .create(AppApiPixivService.class)
                .getCollecDetail(id);
        call.enqueue(new Callback<CollectionResponse>() {
            @Override
            public void onResponse(Call<CollectionResponse> call, retrofit2.Response<CollectionResponse> response) {
                CollectionResponse specialCollectionResponse = response.body();
                try {
                    CollecDetailAdapter specialCollecAdapter = new CollecDetailAdapter(specialCollectionResponse, mContext);
                    specialCollecAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, int viewType) {
                            if (viewType == 0) {
                                getSingleIllust(Long.parseLong(specialCollectionResponse.body.get(0).illusts.get(position).illust_id));
                            } else if (viewType == 1) {
                                Intent intent = new Intent(mContext, UserDetailActivity.class);
                                intent.putExtra("user id", Integer.valueOf(specialCollectionResponse.body
                                        .get(0).illusts.get(position).illust_user_id));
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mRecyclerView.setAdapter(specialCollecAdapter);
                } catch (Exception e) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Snackbar.make(mRecyclerView, "服务器不稳定，出了点小问题", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CollectionResponse> call, Throwable t) {

            }
        });
    }

    private void getSingleIllust(long illustID) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<IllustDetailResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getIllust(Common.getLocalDataSet().getString("Authorization", ""),
                        illustID);
        call.enqueue(new Callback<IllustDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<IllustDetailResponse> call, @NonNull retrofit2.Response<IllustDetailResponse> response) {
                IllustDetailResponse illustDetailResponse = response.body();
                List<IllustsBean> singleIllust = new ArrayList<>();
                try {
                    singleIllust.add(illustDetailResponse.getIllust());
                    Reference.sIllustsBeans = singleIllust;
                    Intent intent = new Intent(mContext, ViewPagerActivity.class);
                    intent.putExtra("which one is selected", 0);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    Snackbar.make(mRecyclerView, "没有这个作品", Snackbar.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<IllustDetailResponse> call, @NonNull Throwable throwable) {
            }
        });
    }
}
