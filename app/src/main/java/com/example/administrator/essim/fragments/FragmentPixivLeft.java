package com.example.administrator.essim.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cleveroad.pulltorefresh.firework.FireworkyPullToRefreshLayout;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters.PixivAdapter;
import com.example.administrator.essim.models.PixivRankItem;
import com.example.administrator.essim.utils.Common;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.administrator.essim.utils.Common.hasData;


/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentPixivLeft extends BaseFragment {

    private int currentDataType;
    private PixivAdapter mPixivAdapter;
    private String responseData = "";
    private RecyclerView mRecyclerView;
    public static PixivRankItem mPixivRankItem;
    private FloatingActionMenu mFloatingActionMenu;
    private Gson gson = new Gson();
    private FireworkyPullToRefreshLayout mFireworkyPullToRefreshLayout;
    private String url_rank_daily = "https://api.imjad.cn/pixiv/v1/?type=rank&content=illust&mode=daily&per_page=20&date="+Common.getLastDay();
    private String url_rank_weekly = "https://api.imjad.cn/pixiv/v1/?type=rank&content=illust&mode=weekly&per_page=20&date="+Common.getLastDay();
    private String url_rank_monthly = "https://api.imjad.cn/pixiv/v1/?type=rank&content=illust&mode=monthly&per_page=20&date="+Common.getLastDay();
    private String now_link_address;
    private int now_page = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pixiv_left, container, false);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = view.findViewById(R.id.pixiv_recy);
        mFloatingActionMenu = getActivity().findViewById(R.id.menu_red);
        mFireworkyPullToRefreshLayout = view.findViewById(R.id.pull_wo_refresh);
        mFireworkyPullToRefreshLayout.setOnRefreshListener(new FireworkyPullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (now_page <= 10) {
                    getData(now_link_address + "&page=" + String.valueOf(now_page++), true);
                } else {
                    TastyToast.makeText(mContext, "没有更多数据啦~", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                    mFireworkyPullToRefreshLayout.setRefreshing(false);
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
        ((FragmentPixiv) getParentFragment()).setChangeDataSet(new FragmentPixiv.ChangeDataSet() {
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
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseData = response.body().string();
                mPixivRankItem = gson.fromJson(responseData, PixivRankItem.class);
                mPixivAdapter = new PixivAdapter(mPixivRankItem, mContext);
                mPixivAdapter.setOnItemClickLitener(new PixivAdapter.OnItemClickListener() {
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
                            mFireworkyPullToRefreshLayout.setRefreshing(false);
                        }
                    }
                });
                now_link_address = address;
            }
        });
    }
}