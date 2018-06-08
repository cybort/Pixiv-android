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
import com.example.administrator.essim.activities.MainActivity;
import com.example.administrator.essim.activities.SearchActivity;
import com.example.administrator.essim.activities.SettingsActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters.PixivAdapterGrid;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.IllustRankingResponse;
import com.example.administrator.essim.response.IllustfollowResponse;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.RecommendResponse;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.utils.Common;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentRank extends BaseFragment {

    private static final String[] arrayOfRankMode = {"动态", "日榜", "周榜", "月榜", "新人", "原创", "男性向", "女性向"};
    private static final String[] modelist = {"day", "week", "month", "week_rookie", "week_original", "day_male", "day_female"};
    public int currentDataType = -1;
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
                if (currentDataType != -1) {
                    currentDataType = -1;
                    getFollowUserNewIllust();
                }
                break;
            case 1:
                if (currentDataType != 0) {
                    currentDataType = 0;
                    getRankList(currentDataType);
                }
                break;
            case 2:
                if (currentDataType != 1) {
                    currentDataType = 1;
                    getRankList(currentDataType);
                }
                break;
            case 3:
                if (currentDataType != 2) {
                    currentDataType = 2;
                    getRankList(currentDataType);
                }
                break;
            case 4:
                if (currentDataType != 3) {
                    currentDataType = 3;
                    getRankList(currentDataType);
                }
                break;
            case 5:
                if (currentDataType != 4) {
                    currentDataType = 4;
                    getRankList(currentDataType);
                }
                break;
            case 6:
                if (currentDataType != 5) {
                    currentDataType = 5;
                    getRankList(currentDataType);
                }
                break;
            case 7:
                if (currentDataType != 6) {
                    currentDataType = 6;
                    getRankList(currentDataType);
                }
                break;
            default:
                break;
        }
        Common.showLog(currentDataType);
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        initView(view);
        getFollowUserNewIllust();
        return view;
    }

    private void initView(View view) {
        mSharedPreferences = Common.getLocalDataSet(mContext);
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
        toolbar.setTitle("动态");
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

    private void getFollowUserNewIllust() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<IllustfollowResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getFollowIllusts(mSharedPreferences.getString("Authorization", ""), "public");
        call.enqueue(new Callback<IllustfollowResponse>() {
            @Override
            public void onResponse(@NonNull Call<IllustfollowResponse> call,
                                   @NonNull retrofit2.Response<IllustfollowResponse> response) {
                IllustfollowResponse illustfollowResponse = response.body();
                next_url = illustfollowResponse.getNext_url();
                initAdapter(illustfollowResponse.getIllusts());
                toolbar.setTitle("动态");
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<IllustfollowResponse> call, @NonNull Throwable throwable) {

            }
        });
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
                next_url = Reference.sIllustRankingResponse.getNext_url();
                initAdapter(Reference.sIllustRankingResponse.getIllusts());
                toolbar.setTitle(arrayOfRankMode[currentDataType + 1]);
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
                    next_url = Reference.sRankList.getNext_url();
                    initAdapter(Reference.sRankList.getIllusts());
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
                        view.startAnimation(Common.getAnimation());
                        Common.postStarIllust(position, illustsBeans,
                                mSharedPreferences.getString("Authorization", ""), mContext, "public");
                    } else {
                        ((ImageView) view).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        view.startAnimation(Common.getAnimation());
                        Common.postUnstarIllust(position, illustsBeans,
                                mSharedPreferences.getString("Authorization", ""), mContext);
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!illustsBeans.get(position).isIs_bookmarked()) {
                    ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                    Common.postStarIllust(position, illustsBeans,
                            mSharedPreferences.getString("Authorization", ""), mContext, "private");
                }
            }
        });
        mRecyclerView.setAdapter(mPixivAdapter);
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
            case R.id.action_settings:
                intent = new Intent(mContext, SettingsActivity.class);
                mContext.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPixivAdapter != null) {
            mPixivAdapter.notifyDataSetChanged();
        }
    }
}