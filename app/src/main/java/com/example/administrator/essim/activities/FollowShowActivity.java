package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.UserFollowAdapter;
import com.example.administrator.essim.anotherproj.CloudMainActivity;
import com.example.administrator.essim.api.AppApiPixivService;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.response.SearchUserResponse;

import retrofit2.Call;
import retrofit2.Callback;

public class FollowShowActivity extends AppCompatActivity {

    private int userID;
    private String next_url;
    private String searchKey;
    private Toolbar mToolbar;
    private Context mContext;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private UserFollowAdapter mUserFollowAdapter;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_show);
        mContext = this;
        Intent intent = getIntent();
        userID = intent.getIntExtra("user id", 0);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        initView();
        if (userID == 0) {  //这是不传入用户id的启动方式，userID被默认置0，是从搜索页面（SearchActivity）跳转过来的
            searchKey = intent.getStringExtra("search_key");    //获取搜索页的关键词开始搜索
            getUserByName();
        } else {
            getUserFollowing();
        }
    }

    private void initView() {
        Glide.get(mContext).clearMemory();
        mProgressBar = findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = findViewById(R.id.pixiv_recy);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> finish());
        LinearLayoutManager linearLayout = new LinearLayoutManager(mContext);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayout);
        mRecyclerView.setHasFixedSize(true);
    }

    private void getUserFollowing() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<SearchUserResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getUserFollowing(mSharedPreferences.getString("Authorization", ""), userID, "public");
        call.enqueue(new Callback<SearchUserResponse>() {
            @Override
            public void onResponse(Call<SearchUserResponse> call, retrofit2.Response<SearchUserResponse> response) {
                Reference.sSearchUserResponse = response.body();
                next_url = Reference.sSearchUserResponse.getNext_url();
                mUserFollowAdapter = new UserFollowAdapter(Reference.sSearchUserResponse.getUser_previews(), mContext);
                mToolbar.setTitle(Reference.sUserDetailResponse.getUser().getName() + "的关注");
                mUserFollowAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, int viewType) {
                        if (position == -1) {
                            if (next_url != null) {
                                getNextData();
                            } else {
                                Snackbar.make(mRecyclerView, "没有其他数据了", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                Intent intent = new Intent(mContext, CloudMainActivity.class);
                                intent.putExtra("user id", Reference.sSearchUserResponse.getUser_previews().get(position)
                                        .getUser().getId());
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setAdapter(mUserFollowAdapter);
            }

            @Override
            public void onFailure(Call<SearchUserResponse> call, Throwable throwable) {

            }
        });
    }

    private void getUserByName() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<SearchUserResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getSearchUser(mSharedPreferences.getString("Authorization", ""), searchKey);
        call.enqueue(new Callback<SearchUserResponse>() {
            @Override
            public void onResponse(Call<SearchUserResponse> call, retrofit2.Response<SearchUserResponse> response) {
                Reference.sSearchUserResponse = response.body();
                next_url = Reference.sSearchUserResponse.getNext_url();
                mUserFollowAdapter = new UserFollowAdapter(Reference.sSearchUserResponse.getUser_previews(), mContext);
                mToolbar.setTitle("搜索结果");
                mUserFollowAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, int viewType) {
                        if (position == -1) {
                            if (next_url != null) {
                                getNextData();
                            } else {
                                Snackbar.make(mRecyclerView, "没有其他数据了", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                Intent intent = new Intent(mContext, CloudMainActivity.class);
                                intent.putExtra("user id", Reference.sSearchUserResponse.getUser_previews().get(position)
                                        .getUser().getId());
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setAdapter(mUserFollowAdapter);
            }

            @Override
            public void onFailure(Call<SearchUserResponse> call, Throwable throwable) {

            }
        });
    }

    private void getNextData() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<SearchUserResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getNextUser(mSharedPreferences.getString("Authorization", ""), next_url);
        call.enqueue(new Callback<SearchUserResponse>() {
            @Override
            public void onResponse(Call<SearchUserResponse> call, retrofit2.Response<SearchUserResponse> response) {
                Reference.sSearchUserResponse = response.body();
                mUserFollowAdapter = new UserFollowAdapter(Reference.sSearchUserResponse.getUser_previews(), mContext);
                next_url = Reference.sSearchUserResponse.getNext_url();
                mUserFollowAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, int viewType) {
                        if (position == -1) {
                            if (next_url != null) {
                                getNextData();
                            } else {
                                Snackbar.make(mRecyclerView, "没有其他数据了", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                Intent intent = new Intent(mContext, CloudMainActivity.class);
                                intent.putExtra("user id", Reference.sSearchUserResponse.getUser_previews().get(position)
                                        .getUser().getId());
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                mRecyclerView.setAdapter(mUserFollowAdapter);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<SearchUserResponse> call, Throwable throwable) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Reference.sSearchUserResponse = null;
        mUserFollowAdapter = null;
    }
}
