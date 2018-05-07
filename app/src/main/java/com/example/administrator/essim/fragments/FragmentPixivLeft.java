package com.example.administrator.essim.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters.PixivAdapterGrid;
import com.example.administrator.essim.models.PixivRankItem;
import com.example.administrator.essim.models.Reference;
import com.example.administrator.essim.utils.Common;
import com.google.gson.Gson;
import com.nightonke.boommenu.BoomMenuButton;
import com.sdsmdg.tastytoast.TastyToast;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.administrator.essim.utils.Common.url_rank_daily;
import static com.example.administrator.essim.utils.Common.url_rank_female;
import static com.example.administrator.essim.utils.Common.url_rank_male;
import static com.example.administrator.essim.utils.Common.url_rank_monthly;
import static com.example.administrator.essim.utils.Common.url_rank_original;
import static com.example.administrator.essim.utils.Common.url_rank_rookie;
import static com.example.administrator.essim.utils.Common.url_rank_weekly;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentPixivLeft extends BaseFragment {

    private static final int PER_PAGE_SIZE = Common.arrayOfString.length;
    public int now_page = 1;
    public int currentDataType;
    public String now_link_address;
    private PixivAdapterGrid mPixivAdapter;
    private String responseData = "";
    private RecyclerView mRecyclerView;
    private BoomMenuButton bmb;
    private Gson gson = new Gson();
    private PullToRefreshView mPullToRefreshView;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pixiv_left, container, false);
        Reference.sFragmentPixivLeft = this;

        mRecyclerView = view.findViewById(R.id.pixiv_recy);
        bmb = getActivity().findViewById(R.id.bmb);
        mProgressBar = view.findViewById(R.id.loading);
        mPullToRefreshView = view.findViewById(R.id.pull_wo_refresh);
        mPullToRefreshView.setOnRefreshListener(() -> {
            if (now_page <= PER_PAGE_SIZE) {
                getData(now_link_address + "&page=" + String.valueOf(now_page), true);
            } else {
                TastyToast.makeText(mContext, "没有更多数据啦~", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                mPullToRefreshView.setRefreshing(false);
            }
        });

        /*
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));*/
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mPixivAdapter.getItemViewType(position) == 2 || mPixivAdapter.getItemViewType(position) == 3) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {

                } else {
                }
            }
        });
        now_link_address = url_rank_daily;
        getData(url_rank_daily, false);
        currentDataType = 0;
        ((FragmentPixiv) getParentFragment()).setChangeDataSet(dataType -> {
            switch (dataType) {
                case 0:
                    if (currentDataType != 0) {
                        now_page = 1;
                        now_link_address = url_rank_daily;
                        getData(url_rank_daily, false);
                        currentDataType = 0;
                    }
                    break;
                case 1:
                    if (currentDataType != 1) {
                        now_page = 1;
                        now_link_address = url_rank_weekly;
                        getData(url_rank_weekly, false);
                        currentDataType = 1;
                    }
                    break;
                case 2:
                    if (currentDataType != 2) {
                        now_page = 1;
                        now_link_address = url_rank_monthly;
                        getData(url_rank_monthly, false);
                        currentDataType = 2;
                    }
                    break;
                case 3:
                    if (currentDataType != 3) {
                        now_page = 1;
                        now_link_address = url_rank_rookie;
                        getData(url_rank_rookie, false);
                        currentDataType = 3;
                    }
                    break;
                case 4:
                    if (currentDataType != 4) {
                        now_page = 1;
                        now_link_address = url_rank_original;
                        getData(url_rank_original, false);
                        currentDataType = 4;
                    }
                    break;
                case 5:
                    if (currentDataType != 5) {
                        now_page = 1;
                        now_link_address = url_rank_male;
                        getData(url_rank_male, false);
                        currentDataType = 5;
                    }
                    break;
                case 6:
                    if (currentDataType != 6) {
                        now_page = 1;
                        now_link_address = url_rank_female;
                        getData(url_rank_female, false);
                        currentDataType = 6;
                    }
                    break;
            }
        });
        return view;
    }

    public void getData(String address, boolean stopRefresh) {
        mProgressBar.setVisibility(View.VISIBLE);
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mProgressBar.setVisibility(View.GONE);
                getActivity().runOnUiThread(() -> TastyToast.makeText(mContext, "数据加载失败", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseData = response.body().string();
                Reference.sPixivRankItem = gson.fromJson(responseData, PixivRankItem.class);
                mPixivAdapter = new PixivAdapterGrid(Reference.sPixivRankItem, mContext);
                mPixivAdapter.setOnItemClickListener((view, position) -> {
                    if (position == -1) {
                        if (now_page <= PER_PAGE_SIZE) {
                            getData(now_link_address + "&page=" + String.valueOf(now_page), true);
                        } else {
                            TastyToast.makeText(mContext, "没有更多数据啦~", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                        }
                    } else {
                        Intent intent = new Intent(mContext, ViewPagerActivity.class);
                        intent.putExtra("which one is selected", position);
                        intent.putExtra("where is from", "FragmentLeft");
                        mContext.startActivity(intent);
                    }
                });
                getActivity().runOnUiThread(() -> {
                    mRecyclerView.setAdapter(mPixivAdapter);
                    mPixivAdapter.notifyDataSetChanged();
                    if (stopRefresh) {
                        mPullToRefreshView.setRefreshing(false);
                    }
                    mProgressBar.setVisibility(View.GONE);
                });
                ((FragmentPixiv) getParentFragment()).gotoPage = now_page;
                now_page++;
                Common.showLog(now_link_address);
            }
        });
    }
}