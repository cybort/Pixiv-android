package com.example.administrator.essim.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.SingleFragmentActivity;
import com.example.administrator.essim.activities.TagResultActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters.AuthorWorksAdapter;
import com.example.administrator.essim.models.AuthorWorks;
import com.example.administrator.essim.models.Reference;
import com.example.administrator.essim.utils.Common;
import com.google.gson.Gson;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nightonke.boommenu.Util;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentTagResult extends BaseFragment {


    private int index;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private int nowSearchType, togo, nowPage = 1, aimPage = 1;
    private AuthorWorksAdapter mAuthorWorksAdapter;
    private static final String[] arrayOfSearchType = {" 500users入り", " 1000users入り",
            " 5000users入り", " 10000users入り"};
    private String head = "https://api.imjad.cn/pixiv/v1/?type=search&per_page=20&word=",
            word, temp = " 500users入り";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_result, container, false);
        index = ((TagResultActivity) getActivity()).index;
        Toolbar toolbar = ((SingleFragmentActivity) getActivity()).mToolbar;
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mProgressBar = view.findViewById(R.id.my_progress);
        mRecyclerView = view.findViewById(R.id.tag_result_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        if (index == -1) {
            word = ((TagResultActivity) getActivity()).words;
            toolbar.setTitle(word);
            getData(head + word + temp);
        } else if (index == -2) {
            word = "R-18";
            toolbar.setTitle(word);
            getData(head + word + temp);
        } else {
            toolbar.setTitle(Reference.sHotTags.get(index).getName());
            word = Reference.sHotTags.get(index).getName();
            getData(head + Reference.sHotTags.get(index).getName() + temp + "&page=" + String.valueOf(nowPage));
        }
        return view;
    }

    private void getData(String address) {
        mProgressBar.setVisibility(View.VISIBLE);
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mProgressBar.setVisibility(View.GONE);
                getActivity().runOnUiThread(() -> TastyToast.makeText(mContext, "数据加载失败", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Gson gson = new Gson();
                Reference.sSearchResult = gson.fromJson(responseData, AuthorWorks.class);
                mAuthorWorksAdapter = new AuthorWorksAdapter(Reference.sSearchResult, getContext(), "searchResult");
                mAuthorWorksAdapter.setOnItemClickListener((view, position) -> {
                    Intent intent = new Intent(mContext, ViewPagerActivity.class);
                    intent.putExtra("which one is selected", position);
                    intent.putExtra("where is from", "FragmentTagResult");
                    mContext.startActivity(intent);
                });
                getActivity().runOnUiThread(() -> {
                    if (Integer.valueOf(Reference.sSearchResult.pagination.getTotal()) >= 1) {
                        mRecyclerView.setAdapter(mAuthorWorksAdapter);
                        mAuthorWorksAdapter.notifyDataSetChanged();
                        mProgressBar.setVisibility(View.INVISIBLE);
                    } else {
                        mRecyclerView.setAdapter(mAuthorWorksAdapter);
                        mAuthorWorksAdapter.notifyDataSetChanged();
                        mProgressBar.setVisibility(View.INVISIBLE);
                        TastyToast.makeText(mContext, "再怎么找也找不到了", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                    }
                });
                Common.showLog(address);
            }
        });
    }

    private void createSearchTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle("筛选结果：");
        builder.setCancelable(true);
        builder.setSingleChoiceItems(arrayOfSearchType, nowSearchType,
                (dialogInterface, i) -> {
                    temp = arrayOfSearchType[i];
                    togo = i;
                });
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            if (nowSearchType != togo) {
                nowPage = 1;
                aimPage = 1;
                getData(head + word + temp + "&page=" + String.valueOf(nowPage));
                nowSearchType = togo;
            }
        })
                .setNegativeButton("取消", (dialogInterface, i) -> {
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createChangePageDialog() {
        String[] arrString;
        arrString = new String[Common.getPageCount(Reference.sSearchResult.pagination.getTotal())];
        for (int i = 0; i < arrString.length; i++) {
            arrString[i] = String.valueOf(i + 1);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.mipmap.logo);
        builder.setCancelable(true);
        builder.setTitle("当前位置: 第" + String.valueOf(nowPage) + "/" +
                String.valueOf(arrString.length) + "页");
        builder.setSingleChoiceItems(arrString, nowPage - 1, (dialogInterface, i) -> {
            aimPage = i + 1;
        });
        builder.setPositiveButton("跳转", (dialogInterface, i) -> {
            if (nowPage != aimPage) {
                nowPage = aimPage;
                if (index >= 0) {
                    getData(head + Reference.sHotTags.get(index).getName() + temp + "&page=" + String.valueOf(nowPage));
                } else {
                    getData(head + word + temp + "&page=" + String.valueOf(nowPage));
                }
            }
        });
        builder.setNegativeButton("取消", (dialogInterface, i) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_search:
                createSearchTypeDialog();
                return true;
            case R.id.action_turn:
                createChangePageDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.tag_result, menu);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setHasOptionsMenu(true);
    }
}
