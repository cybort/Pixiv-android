package com.example.administrator.essim.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.IllustCommentAdapter;
import com.example.administrator.essim.anotherproj.CloudMainActivity;
import com.example.administrator.essim.api.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.IllustCommentsResponse;
import com.example.administrator.essim.utils.DividerItemDecoration;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CommentActivity extends AppCompatActivity {

    private String title;
    private int illustID;
    private Context mContext;
    private EditText mEditText;
    private int parentCommentID;
    private boolean isLoadingMore;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private SharedPreferences mSharedPreferences;
    private IllustCommentAdapter illustCommentAdapter;
    private IllustCommentsResponse mIllustCommentsResponse;
    private List<IllustCommentsResponse.CommentsBean> mCommentsBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commetn);

        mContext = this;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        illustID = intent.getIntExtra("id", 0);
        initView();
        getIllustComment();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_pixiv);
        toolbar.setNavigationOnClickListener(view -> finish());
        toolbar.setTitle(title + "的评论");
        mRecyclerView = findViewById(R.id.recy_comment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mProgressBar = findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = illustCommentAdapter.getItemCount();
                //lastVisibleItem >= totalItemCount - 1 表示剩下1个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    if (!isLoadingMore) {
                        getMoreComment();
                        isLoadingMore = true;
                    }
                }
            }
        });
        mEditText = findViewById(R.id.comment_edit);
        ImageView imageView1 = findViewById(R.id.img_comment);
        imageView1.setOnClickListener(view -> postComment());
        ImageView imageView3 = findViewById(R.id.clear_hint);
        imageView3.setOnClickListener(view -> {
            if (mEditText.getText().toString().trim().length() != 0) {
                mEditText.setText("");
            } else {
                if (parentCommentID != 0) {
                    parentCommentID = 0;
                    mEditText.setHint("留下你的评论吧~");
                }
            }
        });
    }

    private void getMoreComment() {
        if (mIllustCommentsResponse.getNext_url() != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            Call<IllustCommentsResponse> call = new RestClient()
                    .getRetrofit_AppAPI()
                    .create(AppApiPixivService.class)
                    .getNextComment(mSharedPreferences.getString("Authorization", ""), mIllustCommentsResponse.getNext_url());
            call.enqueue(new Callback<IllustCommentsResponse>() {
                @Override
                public void onResponse(Call<IllustCommentsResponse> call, retrofit2.Response<IllustCommentsResponse> response) {
                    mIllustCommentsResponse = response.body();
                    mCommentsBeanList.addAll(mIllustCommentsResponse.getComments());
                    illustCommentAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    isLoadingMore = false;
                }

                @Override
                public void onFailure(Call<IllustCommentsResponse> call, Throwable throwable) {

                }
            });
        } else {
            Snackbar.make(mRecyclerView, "再怎么找也找不到了~", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void postComment() {
        if (mEditText.getText().toString().trim().length() != 0) {
            if (parentCommentID == 0) {
                Call<ResponseBody> call = new RestClient()
                        .getRetrofit_AppAPI()
                        .create(AppApiPixivService.class)
                        .postIllustComment(mSharedPreferences.getString("Authorization", ""), illustID,
                                mEditText.getText().toString().trim(), null);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                        TastyToast.makeText(mContext, "评论成功~", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        getIllustComment();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {

                    }
                });
            } else {
                Call<ResponseBody> call = new RestClient()
                        .getRetrofit_AppAPI()
                        .create(AppApiPixivService.class)
                        .postIllustComment(mSharedPreferences.getString("Authorization", ""), illustID,
                                mEditText.getText().toString().trim(), parentCommentID);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                        TastyToast.makeText(mContext, "评论成功~", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        getIllustComment();
                        mEditText.setHint("留下你的评论吧~");
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {

                    }
                });
            }
            mEditText.setText("");
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            // 隐藏软键盘
            assert imm != null;
            imm.hideSoftInputFromWindow(((Activity) mContext).getWindow().getDecorView().getWindowToken(), 0);
        } else {
            TastyToast.makeText(mContext, "评论不能为空~", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
        }
    }

    private void getIllustComment() {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<IllustCommentsResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getIllustComments(mSharedPreferences.getString("Authorization", ""), illustID);
        call.enqueue(new Callback<IllustCommentsResponse>() {
            @Override
            public void onResponse(Call<IllustCommentsResponse> call, retrofit2.Response<IllustCommentsResponse> response) {
                mIllustCommentsResponse = response.body();
                mCommentsBeanList.clear();
                mCommentsBeanList.addAll(mIllustCommentsResponse.getComments());
                illustCommentAdapter = new IllustCommentAdapter(mCommentsBeanList, mContext);
                illustCommentAdapter.setOnItemClickListener((view, position, viewType) -> {
                    if (viewType == 0) {
                        if(mEditText.getText().toString().trim().length() == 0) {
                            parentCommentID = mCommentsBeanList.get(position).getId();
                            mEditText.setHint(String.format("回复@%s", mCommentsBeanList.get(position).getUser().getName()));
                        }
                    } else if (viewType == 1) {
                        Intent intent = new Intent(mContext, CloudMainActivity.class);
                        intent.putExtra("user id", mCommentsBeanList.get(position).getUser().getId());
                        startActivity(intent);
                    }
                });
                mRecyclerView.setAdapter(illustCommentAdapter);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<IllustCommentsResponse> call, @NonNull Throwable throwable) {

            }
        });
    }
}
