package com.example.administrator.essim.fragments;

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

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.SpecialCollectionActivity;
import com.example.administrator.essim.adapters.SpecialCollecAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.SpecialCollectionResponse;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentSpecialCollec extends BaseFragment {

    private int nowPage = 1;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private FragmentCollecDetail fragmentCollecDetail;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_collec, container, false);
        initView(view);
        getSpecialCollection(nowPage++);//默认加载第一页
        return view;
    }

    private void initView(View v) {
        mProgressBar = v.findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = v.findViewById(R.id.pixiv_recy);
        Toolbar mToolbar = v.findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> Objects.requireNonNull(getActivity()).finish());
        LinearLayoutManager linearLayout = new LinearLayoutManager(mContext);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayout);
        mRecyclerView.setHasFixedSize(true);
    }

    private void getSpecialCollection(int page) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<SpecialCollectionResponse> call = new RestClient()
                .getRetrofit_SpecialCollection()
                .create(AppApiPixivService.class)
                .getSpecialCollection("showcase", page);
        call.enqueue(new Callback<SpecialCollectionResponse>() {
            @Override
            public void onResponse(Call<SpecialCollectionResponse> call, retrofit2.Response<SpecialCollectionResponse> response) {
                SpecialCollectionResponse specialCollectionResponse = response.body();
                try {
                    SpecialCollecAdapter specialCollecAdapter = new SpecialCollecAdapter(specialCollectionResponse, mContext);
                    specialCollecAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, int viewType) {
                            if (position >= 0) {
                                fragmentCollecDetail = null;
                                assert specialCollectionResponse != null;
                                //点了某一个想看的特辑，hide特辑列表，show特辑详情
                                fragmentCollecDetail = FragmentCollecDetail.newInstance(specialCollectionResponse.body.get(position).id,
                                        specialCollectionResponse.body.get(position).title);
                                assert getFragmentManager() != null;
                                getFragmentManager().beginTransaction()
                                        .add(R.id.special_collec_container, fragmentCollecDetail)
                                        .hide(((SpecialCollectionActivity) Objects.requireNonNull(getActivity())).mFragmentSpecialCollec)
                                        .show(fragmentCollecDetail)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                getSpecialCollection(nowPage++);
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
                    Snackbar.make(mRecyclerView, "出问题了或者没数据了。。", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SpecialCollectionResponse> call, @NonNull Throwable t) {
            }
        });
    }
}
