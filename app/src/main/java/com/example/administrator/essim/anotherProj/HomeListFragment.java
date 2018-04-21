package com.example.administrator.essim.anotherProj;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.PixivItemActivity;
import com.example.administrator.essim.adapters.AuthorWorksAdapter;
import com.example.administrator.essim.interfaces.OnItemClickListener;
import com.example.administrator.essim.models.AuthorWorks;
import com.example.administrator.essim.models.DataSet;
import com.example.administrator.essim.utils.Common;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeListFragment extends ScrollObservableFragment {

    private Context mContext;
    private RecyclerView rcvGoodsList;
    private int scrolledX = 0, scrolledY = 0;
    private AuthorWorksAdapter mAuthorWorksAdapter;

    public static HomeListFragment newInstance() {
        HomeListFragment fragment = new HomeListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DataSet.sHomeListFragment = this;
        mContext = getContext();
        View v = inflater.inflate(R.layout.fragment_home_list, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        rcvGoodsList = v.findViewById(R.id.rcvGoodsList);
        rcvGoodsList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvGoodsList.setItemAnimator(new DefaultItemAnimator());
        rcvGoodsList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, final int dx, final int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrolledX += dx;
                scrolledY += dy;
                if (HomeListFragment.this.isResumed()) {
                    doOnScrollChanged(scrolledX, scrolledY, dx, dy);
                }
            }
        });
        String url = "https://api.imjad.cn/pixiv/v1/?type=member_illust&id=";
        getData(url + DataSet.sPixivRankItem.response.get(0).works.get(((CloudMainActivity) getActivity()).index).work
                .user.getId());
    }

    private void getData(String address) {
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() -> TastyToast.makeText(mContext, "数据加载失败", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Gson gson = new Gson();
                DataSet.sAuthorWorks = gson.fromJson(responseData, AuthorWorks.class);
                mAuthorWorksAdapter = new AuthorWorksAdapter(DataSet.sAuthorWorks, getContext(), "AuthorResult");
                mAuthorWorksAdapter.setOnItemClickListener((view, position) -> {
                    Intent intent = new Intent(mContext, PixivItemActivity.class);
                    intent.putExtra("which one is selected", position);
                    intent.putExtra("which kind data type", "AuthorWorks");
                    mContext.startActivity(intent);
                });
                getActivity().runOnUiThread(() -> rcvGoodsList.setAdapter(mAuthorWorksAdapter));
            }
        });
    }


    @Override
    public void setScrolledY(int scrolledY) {
        if (rcvGoodsList != null) {
            if (this.scrolledY >= scrolledY) {
                int scrollDistance = (this.scrolledY - scrolledY) * -1;
                rcvGoodsList.scrollBy(0, scrollDistance);
            } else {
                rcvGoodsList.scrollBy(0, scrolledY);
            }
        }
    }
}
