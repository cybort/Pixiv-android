package com.example.administrator.essim.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.AutoFieldAdapter;
import com.example.administrator.essim.api.AppApiPixivService;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.IllustDetailResponse;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.PixivResponse;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.response.TagResponse;
import com.example.administrator.essim.utils.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;

public class SearchActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    private int searchType;
    private Activity mActivity;
    private Context mContext;
    private CardView mCardView;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private MaterialSearchBar searchBar;
    private TagFlowLayout mTagFlowLayout;
    private NestedScrollView mNestedScrollView;
    private SharedPreferences mSharedPreferences;
    private AutoFieldAdapter customSuggestionsAdapter;
    private static final String url = "https://api.imjad.cn/pixiv/v1/?type=tags";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        mActivity = this;
        mContext = this;
        mCardView = findViewById(R.id.card_search);
        mRecyclerView = findViewById(R.id.recy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mProgressBar = findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        searchBar = findViewById(R.id.searchBar);
        searchBar.setPlaceHolder(getResources().getString(R.string.word_get_illust));
        searchBar.inflateMenu(R.menu.search_type);
        searchBar.getMenu().setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.type_one:
                    if (searchType != 0) {
                        searchBar.setPlaceHolder(getResources().getString(R.string.word_get_illust));
                        searchType = 0;
                    }
                    break;
                case R.id.type_two:
                    if (searchType != 1) {
                        searchBar.setPlaceHolder(getResources().getString(R.string.id_get_illust));
                        searchType = 1;
                    }
                    break;
                case R.id.type_three:
                    if (searchType != 2) {
                        searchBar.setPlaceHolder(getResources().getString(R.string.word_get_user));
                        searchType = 2;
                    }
                    break;
                case R.id.type_four:
                    if (searchType != 3) {
                        searchBar.setPlaceHolder(getResources().getString(R.string.id_get_user));
                        searchType = 3;
                    }
                    break;
                default:
                    break;
            }
            return false;
        });
        searchBar.setOnSearchActionListener(this);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchBar.getText().length() != 0) {
                    if (searchType == 0) {
                        getAutoCompleteWords();
                        mNestedScrollView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    mCardView.setVisibility(View.INVISIBLE);
                    mNestedScrollView.setVisibility(View.VISIBLE);
                }
            }
        });
        mNestedScrollView = findViewById(R.id.card_tag);
        mTagFlowLayout = findViewById(R.id.tag_group);
        getTagGroup();
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        if (searchType == 0) {
            Intent intent = new Intent(mContext, SearchTagActivity.class);
            intent.putExtra("what is the keyword", searchBar.getText().trim());
            startActivity(intent);
        } else if (searchType == 1) {
            if (Common.isNumeric(searchBar.getText().trim())) {
                getSingleIllust();
            } else {
                Snackbar.make(searchBar, "ID有误~（当前状态 ID搜作品）", Snackbar.LENGTH_SHORT).show();
            }
        } else if (searchType == 2) {
            Intent intent = new Intent(mContext, FollowShowActivity.class);
            intent.putExtra("search_key", searchBar.getText().trim());
            mContext.startActivity(intent);
        } else if (searchType == 3) {
            if (Common.isNumeric(searchBar.getText().trim())) {
                Intent intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("user id", Integer.valueOf(searchBar.getText().trim()));
                startActivity(intent);
                searchBar.setText("");
            } else {
                Snackbar.make(searchBar, "ID有误~（当前状态 ID搜画师）", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                mCardView.setVisibility(View.INVISIBLE);
                mNestedScrollView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void getTagGroup() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                // 注：该回调是子线程，非主线程.
                assert response.body() != null;
                String responseData = response.body().string();
                Gson gson = new Gson();
                try {
                    final List<TagResponse> booksInfo = gson.fromJson(responseData, new TypeToken<List<TagResponse>>() {
                    }.getType());
                    String allTag[] = new String[90];
                    for (int i = 0; i < 90; i++) {
                        allTag[i] = booksInfo.get(i).getName();
                    }
                    mTagFlowLayout.setOnTagClickListener((view, position, parent) -> {
                        Intent intent = new Intent(mContext, SearchTagActivity.class);
                        intent.putExtra("what is the keyword", allTag[position]);
                        startActivity(intent);
                        return true;
                    });
                    runOnUiThread(() -> mTagFlowLayout.setAdapter(new TagAdapter<String>(allTag) {
                        @Override
                        public View getView(FlowLayout parent, int position, String s) {
                            TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tag_textview_search,
                                    mTagFlowLayout, false);
                            tv.setText(s);
                            return tv;
                        }
                    }));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getAutoCompleteWords() {
        mCardView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        Call<PixivResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getSearchAutoCompleteKeywords(mSharedPreferences.getString("Authorization", ""),
                        searchBar.getText());
        call.enqueue(new Callback<PixivResponse>() {
            @Override
            public void onResponse(@NonNull Call<PixivResponse> call, @NonNull retrofit2.Response<PixivResponse> response) {
                Reference.sPixivResponse = response.body();
                customSuggestionsAdapter = new AutoFieldAdapter(Reference.sPixivResponse, mContext);
                customSuggestionsAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, int viewType) {
                        Intent intent = new Intent(mContext, SearchTagActivity.class);
                        intent.putExtra("what is the keyword", ((TextView) view).getText());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                mRecyclerView.setAdapter(customSuggestionsAdapter);
                mCardView.setVisibility(View.VISIBLE);
                mNestedScrollView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<PixivResponse> call, @NonNull Throwable throwable) {

            }
        });
    }

    private void getSingleIllust() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<IllustDetailResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getIllust(mSharedPreferences.getString("Authorization", ""),
                        Long.parseLong(searchBar.getText()));
        call.enqueue(new Callback<IllustDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<IllustDetailResponse> call, @NonNull retrofit2.Response<IllustDetailResponse> response) {
                IllustDetailResponse illustDetailResponse = response.body();
                List<IllustsBean> singleIllust = new ArrayList<>();
                try {
                    singleIllust.add(illustDetailResponse.getIllust());
                    Reference.sIllustsBeans = singleIllust;
                    Intent intent = new Intent(mContext, ViewPagerActivity.class);
                    intent.putExtra("which one is selected", 0);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    Snackbar.make(searchBar, "没有这个作品", Snackbar.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<IllustDetailResponse> call, @NonNull Throwable throwable) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Reference.sPixivResponse = null;
        customSuggestionsAdapter = null;
    }
}
