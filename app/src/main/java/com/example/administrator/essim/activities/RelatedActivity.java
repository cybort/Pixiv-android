package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.PixivAdapterGrid;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.response.RelatedIllust;
import com.example.administrator.essim.utils.Common;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RelatedActivity extends AppCompatActivity {

    private Context mContext;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private PixivAdapterGrid mPixivAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related);

        mContext = this;
        Intent intent = getIntent();
        RelatedIllust relatedIllust = (RelatedIllust) intent.getSerializableExtra("illust set");
        String illustTitle = intent.getStringExtra("illust title");

        initView(illustTitle);
        initAdapter(relatedIllust.illusts);
    }

    private void initView(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title + "的相关作品");
        mProgressBar = findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = findViewById(R.id.pixiv_recy);
        toolbar.setNavigationOnClickListener(view -> finish());
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
    }

    private void initAdapter(List<IllustsBean> illustsBeans) {
        mPixivAdapter = new PixivAdapterGrid(illustsBeans, mContext);
        mPixivAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, int position, int viewType) {
                if (position == -1) {
                    Snackbar.make(mProgressBar, "再怎么找也找不到了~", Snackbar.LENGTH_SHORT).show();
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
                                Common.getLocalDataSet().getString("Authorization", ""), mContext, "public");
                    } else {
                        ((ImageView) view).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        view.startAnimation(Common.getAnimation());
                        Common.postUnstarIllust(position, illustsBeans,
                                Common.getLocalDataSet().getString("Authorization", ""), mContext);
                    }
                }
            }

            @Override
            public void onItemLongClick(@NotNull View view, int position) {
                if (!illustsBeans.get(position).isIs_bookmarked()) {
                    ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                    Common.postStarIllust(position, illustsBeans,
                            Common.getLocalDataSet().getString("Authorization", ""), mContext, "private");
                }
            }
        });
        mRecyclerView.setAdapter(mPixivAdapter);
    }
}
