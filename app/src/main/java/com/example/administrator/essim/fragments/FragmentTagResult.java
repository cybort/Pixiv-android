package com.example.administrator.essim.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.PixivItemActivity;
import com.example.administrator.essim.activities.SingleFragmentActivity;
import com.example.administrator.essim.activities.TagResultActivity;
import com.example.administrator.essim.adapters.AuthorWorksAdapter;
import com.example.administrator.essim.models.AuthorWorks;
import com.example.administrator.essim.models.DataSet;
import com.example.administrator.essim.utils.Common;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentTagResult extends BaseFragment {

    private AuthorWorksAdapter mAuthorWorksAdapter;
    private RecyclerView mRecyclerView;
    public static final String head = "https://api.imjad.cn/pixiv/v1/?type=search&word=";
    public static final String bottom = "1000users入り&per_page=20";
    private Toolbar mToolbar;

    //5000users入り
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_result, container, false);
        int index = ((TagResultActivity) getActivity()).index;

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        mToolbar = ((SingleFragmentActivity) getActivity()).mToolbar;
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mRecyclerView = view.findViewById(R.id.tag_result_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        if(index == -1)
        {
            String word = ((TagResultActivity) getActivity()).words;
            mToolbar.setTitle(word);
            getData(head + word + bottom);
        }
        else
        {
            mToolbar.setTitle(DataSet.sTags.get(index).getName());
            getData(head + DataSet.sTags.get(index).getName() + bottom);
        }
        return view;
    }

    private void getData(String address) {
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Gson gson = new Gson();
                DataSet.sSearchResult = gson.fromJson(responseData, AuthorWorks.class);
                mAuthorWorksAdapter = new AuthorWorksAdapter(DataSet.sSearchResult, getContext(), "searchResult");
                mAuthorWorksAdapter.setOnItemClickListener(new AuthorWorksAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(mContext, PixivItemActivity.class);
                        intent.putExtra("which one is selected", position);
                        intent.putExtra("which kind data type", "TagResult");
                        mContext.startActivity(intent);
                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setAdapter(mAuthorWorksAdapter);
                    }
                });
            }
        });
    }
}
