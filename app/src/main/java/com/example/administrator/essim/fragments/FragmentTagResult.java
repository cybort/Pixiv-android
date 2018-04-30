package com.example.administrator.essim.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.PixivItemActivity;
import com.example.administrator.essim.activities.SingleFragmentActivity;
import com.example.administrator.essim.activities.TagResultActivity;
import com.example.administrator.essim.adapters.AuthorWorksAdapter;
import com.example.administrator.essim.models.AuthorWorks;
import com.example.administrator.essim.models.Reference;
import com.example.administrator.essim.utils.Common;
import com.google.gson.Gson;
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
    private String head = "https://api.imjad.cn/pixiv/v1/?type=search&per_page=20&word=",
            word, temp = " 1000users入り";
    private static final String[] arrayOfSearchType = {" 1000users入り", " 2000users入り",
            " 5000users入り", " 10000users入り"};

    //5000users入り
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_result, container, false);
        index = ((TagResultActivity) getActivity()).index;

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
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
            getData(head + word + temp + " 500users入り");
        } else {
            toolbar.setTitle(Reference.sHotTags.get(index).getName());
            word = Reference.sHotTags.get(index).getName();
            getData(head + Reference.sHotTags.get(index).getName() + temp  + "&page=" + String.valueOf(nowPage));
        }
        return view;
    }

    private void getData(String address) {
        mProgressBar.setVisibility(View.VISIBLE);
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() -> TastyToast.makeText(mContext, "数据加载失败", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Gson gson = new Gson();
                Reference.sSearchResult = gson.fromJson(responseData, AuthorWorks.class);
                mAuthorWorksAdapter = new AuthorWorksAdapter(Reference.sSearchResult, getContext(), "searchResult");
                mAuthorWorksAdapter.setOnItemClickListener((view, position) -> {
                    Intent intent = new Intent(mContext, PixivItemActivity.class);
                    intent.putExtra("which one is selected", position);
                    intent.putExtra("which kind data type", "TagResult");
                    mContext.startActivity(intent);
                });
                getActivity().runOnUiThread(() -> {
                    mRecyclerView.setAdapter(mAuthorWorksAdapter);
                    mAuthorWorksAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.INVISIBLE);
                });
            }
        });
    }

    private void createSearchTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle("筛选结果：");
        builder.setSingleChoiceItems(arrayOfSearchType, nowSearchType,
                (dialogInterface, i) -> {
                    temp = arrayOfSearchType[i];
                    togo = i;
                });
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            if (nowSearchType != togo) {
                nowPage = 1;
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
        builder.setTitle("当前位置: 第" + String.valueOf(nowPage) + "/" +
                String.valueOf(arrString.length) + "页");
        builder.setSingleChoiceItems(arrString, nowPage - 1, (dialogInterface, i) -> {
            aimPage = i + 1;
        });
        builder.setPositiveButton("跳转", (dialogInterface, i) -> {
            if (nowPage != aimPage) {
                nowPage = aimPage;
                getData(head + Reference.sHotTags.get(index).getName() + temp + "&page=" + String.valueOf(nowPage));
            } else {
                Common.showLog("什么也不做");
            }
        });
        builder.setNegativeButton("取消", (dialogInterface, i) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (dm.heightPixels * 0.6);
        p.width = (int) (dm.widthPixels * 1.0);
        dialog.getWindow().setAttributes(p);
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
