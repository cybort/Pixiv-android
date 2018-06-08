package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.PixivAdapterGrid;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.RecommendResponse;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.response.SearchIllustResponse;
import com.example.administrator.essim.utils.Common;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchTagActivity extends AppCompatActivity {

    private static final String[] sort = {"popular_desc", "date_desc"};
    private static final String[] arrayOfSearchType = {" 500users入り", " 1000users入り",
            " 5000users入り", " 10000users入り"};
    public String ketWords;
    private String temp;
    private String next_url;
    private Context mContext;
    private boolean isBestSort;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private RecommendResponse moreData;
    private PixivAdapterGrid mPixivAdapter;
    private int nowSearchType = -1, togo = -1;
    private SharedPreferences mSharedPreferences;
    private AlphaAnimation alphaAnimationShowIcon;
    private SearchIllustResponse mSearchIllustResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tag);

        mContext = this;
        Intent intent = getIntent();
        ketWords = intent.getStringExtra("what is the keyword");
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Toolbar toolbar = findViewById(R.id.toolbar_pixiv);
        toolbar.setTitle(ketWords);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        mProgressBar = findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = findViewById(R.id.pixiv_recy);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mPixivAdapter.getItemViewType(position) == 2) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        alphaAnimationShowIcon = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimationShowIcon.setDuration(500);
        getData(sort[1], "");
    }

    private void getData(String rankType, String usersyori) {
        isBestSort = rankType.equals(sort[0]);
        mProgressBar.setVisibility(View.VISIBLE);
        Call<SearchIllustResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getSearchIllust(ketWords + usersyori,
                        rankType,
                        "partial_match_for_tags",
                        null,
                        null,
                        mSharedPreferences.getString("Authorization", ""));
        call.enqueue(new Callback<SearchIllustResponse>() {
            @Override
            public void onResponse(Call<SearchIllustResponse> call, retrofit2.Response<SearchIllustResponse> response) {
                mSearchIllustResponse = response.body();
                next_url = mSearchIllustResponse.getNext_url();
                initAdapter(mSearchIllustResponse.getIllusts());
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<SearchIllustResponse> call, Throwable throwable) {

            }
        });
    }

    private void getNextData() {
        if (next_url != null) {
            if (mSearchIllustResponse != null) {
                mSearchIllustResponse = null;
            }
            mProgressBar.setVisibility(View.VISIBLE);
            Call<RecommendResponse> call = new RestClient()
                    .getRetrofit_AppAPI()
                    .create(AppApiPixivService.class)
                    .getNext(mSharedPreferences.getString("Authorization", ""), next_url);
            call.enqueue(new Callback<RecommendResponse>() {
                @Override
                public void onResponse(Call<RecommendResponse> call, retrofit2.Response<RecommendResponse> response) {
                    moreData = response.body();
                    next_url = moreData.getNext_url();
                    initAdapter(moreData.getIllusts());
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<RecommendResponse> call, Throwable throwable) {

                }
            });
        } else {
            Snackbar.make(mProgressBar, "再怎么找也找不到了~", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void initAdapter(List<IllustsBean> illustsBeans) {
        mPixivAdapter = new PixivAdapterGrid(illustsBeans, mContext);
        mPixivAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int viewType) {
                if (position == -1) {
                    getNextData();
                } else if (viewType == 0) {
                    Reference.sIllustsBeans = illustsBeans;
                    Intent intent = new Intent(mContext, ViewPagerActivity.class);
                    intent.putExtra("which one is selected", position);
                    mContext.startActivity(intent);
                } else if (viewType == 1) {
                    if (!illustsBeans.get(position).isIs_bookmarked()) {
                        ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                        view.startAnimation(alphaAnimationShowIcon);
                        Common.postStarIllust(position, illustsBeans,
                                mSharedPreferences.getString("Authorization", ""), mContext, "public");
                    } else {
                        ((ImageView) view).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        view.startAnimation(alphaAnimationShowIcon);
                        Common.postUnstarIllust(position, illustsBeans,
                                mSharedPreferences.getString("Authorization", ""), mContext);
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!illustsBeans.get(position).isIs_bookmarked()) {
                    ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                    view.startAnimation(alphaAnimationShowIcon);
                    Common.postStarIllust(position, illustsBeans,
                            mSharedPreferences.getString("Authorization", ""), mContext, "private");
                }
            }
        });
        mRecyclerView.setAdapter(mPixivAdapter);
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
                nowSearchType = togo;
                getData(sort[1], arrayOfSearchType[nowSearchType]);
            }
        })
                .setNegativeButton("取消", (dialogInterface, i) -> {
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tag_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_search) {
            createSearchTypeDialog();
            return true;
        } else if (id == R.id.action_get_hot) {
            if (mSharedPreferences.getBoolean("ispremium", false)) {
                if (!isBestSort) {
                    getData(sort[0], "");
                }
            } else {
                Snackbar.make(mRecyclerView, "只有会员才能按热度排序", Snackbar.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPixivAdapter != null) {
            mPixivAdapter.notifyDataSetChanged();
        }
    }
}
