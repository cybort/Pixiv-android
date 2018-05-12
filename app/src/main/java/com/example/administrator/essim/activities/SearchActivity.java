package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.AutoFieldAdapter;
import com.example.administrator.essim.api.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.PixivResponse;
import com.example.administrator.essim.response.Reference;
import com.mancj.materialsearchbar.MaterialSearchBar;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    private Context mContext;
    private CardView mCardView;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private MaterialSearchBar searchBar;
    private SharedPreferences mSharedPreferences;
    private AutoFieldAdapter customSuggestionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
    }

    private void initView() {
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
                    getAutoCompleteWords();
                } else {
                    mCardView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Intent intent = new Intent(mContext, SearchTagActivity.class);
        intent.putExtra("what is the keyword", searchBar.getText().trim());
        startActivity(intent);
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                mCardView.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
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
            public void onResponse(Call<PixivResponse> call, retrofit2.Response<PixivResponse> response) {
                Reference.sPixivResponse = response.body();
                customSuggestionsAdapter = new AutoFieldAdapter(Reference.sPixivResponse, mContext);
                customSuggestionsAdapter.setOnItemClickListener((view, position, viewType) -> {
                    Intent intent = new Intent(mContext, SearchTagActivity.class);
                    intent.putExtra("what is the keyword", ((TextView) view).getText());
                    startActivity(intent);
                });
                mRecyclerView.setAdapter(customSuggestionsAdapter);
                mCardView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<PixivResponse> call, Throwable throwable) {

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
