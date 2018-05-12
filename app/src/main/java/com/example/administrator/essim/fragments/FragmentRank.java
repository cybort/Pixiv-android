package com.example.administrator.essim.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.LoginActivity;
import com.example.administrator.essim.activities.MainActivity;
import com.example.administrator.essim.activities.SearchActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters.PixivAdapterGrid;
import com.example.administrator.essim.api.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.IllustRankingResponse;
import com.example.administrator.essim.response.RecommendResponse;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.utils.Common;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;

import java.io.Serializable;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentRank extends BaseFragment {

    private static final String[] arrayOfRankMode = {"日榜", "周榜", "月榜", "新人", "原创", "男性向", "女性向"};
    private static final String[] modelist = {"day", "week", "month", "week_rookie", "week_original", "day_male", "day_female"};
    public int currentDataType = 0;
    private String next_url;
    private SharedPreferences mSharedPreferences;
    private PixivAdapterGrid mPixivAdapter;
    private RecyclerView mRecyclerView;
    private BoomMenuButton bmb;
    private ProgressBar mProgressBar;
    private Toolbar toolbar;
    private OnBMClickListener clickListener = index -> {
        switch (index) {
            case 0:
                if (currentDataType != 0) {
                    currentDataType = 0;
                    getRankList(currentDataType);
                }
                break;
            case 1:
                if (currentDataType != 1) {
                    currentDataType = 1;
                    getRankList(currentDataType);
                }
                break;
            case 2:
                if (currentDataType != 2) {
                    currentDataType = 2;
                    getRankList(currentDataType);
                }
                break;
            case 3:
                if (currentDataType != 3) {
                    currentDataType = 3;
                    getRankList(currentDataType);
                }
                break;
            case 4:
                if (currentDataType != 4) {
                    currentDataType = 4;
                    getRankList(currentDataType);
                }
                break;
            case 5:
                if (currentDataType != 5) {
                    currentDataType = 5;
                    getRankList(currentDataType);
                }
                break;
            case 6:
                if (currentDataType != 6) {
                    currentDataType = 6;
                    getRankList(currentDataType);
                }
                break;
            default:
                break;
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        initView(view);
        getRankList(currentDataType);
        return view;
    }

    private void initView(View view) {
        mSharedPreferences = ((MainActivity) Objects.requireNonNull(getActivity())).mSharedPreferences;
        mRecyclerView = view.findViewById(R.id.pixiv_recy);
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
        toolbar = view.findViewById(R.id.toolbar_pixiv);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view1 ->
                ((MainActivity) Objects.requireNonNull(getActivity()))
                        .getDrawer().openDrawer(Gravity.START, true));
        toolbar.setTitle(arrayOfRankMode[currentDataType]);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mProgressBar = view.findViewById(R.id.try_login);
        bmb = view.findViewById(R.id.bmb);
        bmb.setUse3DTransformAnimation(true);
        bmb.setShowDuration(400);
        bmb.setHideDuration(400);
        bmb.setFrames(60);
        bmb.setNormalColor(getResources().getColor(R.color.colorAccent));
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .normalImageRes(R.drawable.ic_card_giftcard_black_24dp)
                    .imagePadding(new Rect(20, 20, 20, 60))
                    .normalText(arrayOfRankMode[i])
                    .textRect(new Rect(Util.dp2px(15), Util.dp2px(42), Util.dp2px(65), Util.dp2px(72)))
                    .textSize(16)
                    .listener(clickListener);
            bmb.addBuilder(builder);
        }
    }

    private void getRankList(int dataType) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<IllustRankingResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getIllustRanking(mSharedPreferences.getString("Authorization", ""),
                        modelist[dataType], null);
        call.enqueue(new Callback<IllustRankingResponse>() {
            @Override
            public void onResponse(@NonNull Call<IllustRankingResponse> call,
                                   @NonNull retrofit2.Response<IllustRankingResponse> response) {
                Reference.sIllustRankingResponse = response.body();
                mPixivAdapter = new PixivAdapterGrid(Reference.sIllustRankingResponse.getIllusts(), mContext);
                next_url = Reference.sIllustRankingResponse.getNext_url();
                mPixivAdapter.setOnItemClickListener((view, position, viewType) -> {
                    if (position == -1) {
                        getNextData();
                    } else if (viewType == 0) {
                        Intent intent = new Intent(mContext, ViewPagerActivity.class);
                        intent.putExtra("which one is selected", position);
                        intent.putExtra("all illust", (Serializable) Reference.sIllustRankingResponse.getIllusts());
                        mContext.startActivity(intent);
                    } else if (viewType == 1) {
                        if (!Reference.sIllustRankingResponse.getIllusts().get(position).isIs_bookmarked()) {
                            ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                            view.startAnimation(Common.getAnimation());
                            Common.postStarIllust(position, Reference.sIllustRankingResponse.getIllusts(),
                                    mSharedPreferences.getString("Authorization", ""), mRecyclerView);
                        } else {
                            ((ImageView) view).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            view.startAnimation(Common.getAnimation());
                            Common.postUnstarIllust(position, Reference.sIllustRankingResponse.getIllusts(),
                                    mSharedPreferences.getString("Authorization", ""), mRecyclerView);
                        }
                    }
                });
                mRecyclerView.setAdapter(mPixivAdapter);
                toolbar.setTitle(arrayOfRankMode[currentDataType]);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<IllustRankingResponse> call, @NonNull Throwable throwable) {

            }
        });
    }

    private void getNextData() {
        if (next_url != null) {
            if (Reference.sIllustRankingResponse != null) {
                Reference.sIllustRankingResponse = null;
            }
            mProgressBar.setVisibility(View.VISIBLE);
            Call<RecommendResponse> call = new RestClient()
                    .getRetrofit_AppAPI()
                    .create(AppApiPixivService.class)
                    .getNext(mSharedPreferences.getString("Authorization", ""), next_url);
            call.enqueue(new Callback<RecommendResponse>() {
                @Override
                public void onResponse(@NonNull Call<RecommendResponse> call,
                                       @NonNull retrofit2.Response<RecommendResponse> response) {
                    Reference.sRankList = response.body();
                    mPixivAdapter = new PixivAdapterGrid(Reference.sRankList.getIllusts(), mContext);
                    next_url = Reference.sRankList.getNext_url();
                    mPixivAdapter.setOnItemClickListener((view, position, viewType) -> {
                        if (position == -1) {
                            getNextData();
                        } else if (viewType == 0) {
                            Intent intent = new Intent(mContext, ViewPagerActivity.class);
                            intent.putExtra("which one is selected", position);
                            intent.putExtra("all illust", (Serializable) Reference.sRankList.getIllusts());
                            mContext.startActivity(intent);
                        } else if (viewType == 1) {
                            if (!Reference.sRankList.getIllusts().get(position).isIs_bookmarked()) {
                                ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                                view.startAnimation(Common.getAnimation());
                                Common.postStarIllust(position, Reference.sRankList.getIllusts(),
                                        mSharedPreferences.getString("Authorization", ""), mRecyclerView);
                            } else {
                                ((ImageView) view).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                view.startAnimation(Common.getAnimation());
                                Common.postUnstarIllust(position, Reference.sRankList.getIllusts(),
                                        mSharedPreferences.getString("Authorization", ""), mRecyclerView);
                            }
                        }
                    });
                    mRecyclerView.setAdapter(mPixivAdapter);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(@NonNull Call<RecommendResponse> call, @NonNull Throwable throwable) {

                }
            });
        } else {
            Snackbar.make(mProgressBar, "再怎么找也找不到了~", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_pixiv, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_search:
                intent = new Intent(mContext, SearchActivity.class);
                mContext.startActivity(intent);
                return true;
            case R.id.action_login_out:
                intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}