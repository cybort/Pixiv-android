package com.example.administrator.essim.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.TagResultActivity;
import com.example.administrator.essim.adapters.HotTagAdapter;
import com.example.administrator.essim.models.DataSet;
import com.example.administrator.essim.models.Tag;
import com.example.administrator.essim.utils.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentPixivRight extends BaseFragment {

    private HotTagAdapter mHotTagAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pixiv_right, container, false);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = view.findViewById(R.id.tag_list);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        getData("https://api.imjad.cn/pixiv/v1/?type=tags");
        return view;
    }


    private void getData(String address) {
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TastyToast.makeText(mContext, "数据加载失败", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Gson gson = new Gson();
                final List<Tag> booksInfo = gson.fromJson(responseData, new TypeToken<List<Tag>>() {}.getType());
                DataSet.sTags = booksInfo.subList(0, 60);
                mHotTagAdapter = new HotTagAdapter(DataSet.sTags, getContext());
                mHotTagAdapter.setOnItemClickLitener(new HotTagAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(mContext, TagResultActivity.class);
                        intent.putExtra("which one is selected", position);
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onSearch(View view, String searchKey, int position) {
                        Intent intent = new Intent(mContext, TagResultActivity.class);
                        intent.putExtra("which one is selected", position);
                        intent.putExtra("what is searching", searchKey);
                        mContext.startActivity(intent);
                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setAdapter(mHotTagAdapter);
                    }
                });
            }
        });
    }
}