package com.example.administrator.essim.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters.PixivAdapter;
import com.example.administrator.essim.interfaces.OnChangeDataSet;
import com.example.administrator.essim.interfaces.OnItemClickListener;
import com.example.administrator.essim.models.DataSet;
import com.example.administrator.essim.models.PixivRankItem;
import com.example.administrator.essim.utils.Common;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentPixivLeft extends BaseFragment {

    private int currentDataType;
    private PixivAdapter mPixivAdapter;
    private String responseData = "";
    private RecyclerView mRecyclerView;
    private FloatingActionMenu mFloatingActionMenu;
    private Gson gson = new Gson();
    private PullToRefreshView mPullToRefreshView;
    private ProgressBar mProgressBar;
    private String url_rank_daily = "https://api.imjad.cn/pixiv/v1/?type=rank&content=illust&mode=daily&per_page=20&date=" + Common.getLastDay();
    private String url_rank_weekly = "https://api.imjad.cn/pixiv/v1/?type=rank&content=illust&mode=weekly&per_page=20&date=" + Common.getLastDay();
    private String url_rank_monthly = "https://api.imjad.cn/pixiv/v1/?type=rank&content=illust&mode=monthly&per_page=20&date=" + Common.getLastDay();
    private String now_link_address;
    private int now_page = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pixiv_left, container, false);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = view.findViewById(R.id.pixiv_recy);
        mFloatingActionMenu = getActivity().findViewById(R.id.menu_red);
        mProgressBar = view.findViewById(R.id.loading);
        mPullToRefreshView = view.findViewById(R.id.pull_wo_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (now_page <= 10) {
                    getData(now_link_address + "&page=" + String.valueOf(now_page++), true);
                } else {
                    TastyToast.makeText(mContext, "没有更多数据啦~", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                    mPullToRefreshView.setRefreshing(false);
                }
            }
        });

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    mFloatingActionMenu.hideMenuButton(true);
                } else {
                    mFloatingActionMenu.showMenuButton(true);
                }
            }
        });
        getData(url_rank_daily, false);
        currentDataType = 0;
        ((FragmentPixiv) getParentFragment()).setChangeDataSet(new OnChangeDataSet() {
            @Override
            public void changeData(int dataType) {
                if (dataType == 0) {
                    if (currentDataType != 0) {
                        now_page = 2;
                        getData(url_rank_daily, false);
                        currentDataType = 0;
                    }
                } else if (dataType == 1) {
                    if (currentDataType != 1) {
                        now_page = 2;
                        getData(url_rank_weekly, false);
                        currentDataType = 1;
                    }
                } else if (dataType == 2) {
                    if (currentDataType != 2) {
                        now_page = 2;
                        getData(url_rank_monthly, false);
                        currentDataType = 2;
                    }
                }
            }
        });
        return view;
    }

    private void getData(final String address, final boolean stopRefresh) {
        mProgressBar.setVisibility(View.VISIBLE);
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseData = response.body().string();
                DataSet.sPixivRankItem = gson.fromJson(responseData, PixivRankItem.class);
                mPixivAdapter = new PixivAdapter(DataSet.sPixivRankItem, mContext);
                mPixivAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (position == 20) {
                            if (now_page <= 10) {
                                getData(now_link_address + "&page=" + String.valueOf(now_page++), true);
                            } else {
                                TastyToast.makeText(mContext, "没有更多数据啦~", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                            }
                        } else {
                            Intent intent = new Intent(mContext, ViewPagerActivity.class);
                            intent.putExtra("which_one_is_touched", position);
                            mContext.startActivity(intent);
                        }
                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setAdapter(mPixivAdapter);
                        mPixivAdapter.notifyDataSetChanged();
                        if (stopRefresh) {
                            mPullToRefreshView.setRefreshing(false);
                        }
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
                now_link_address = address;
            }
        });
    }
}