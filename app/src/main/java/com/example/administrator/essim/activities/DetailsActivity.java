package com.example.administrator.essim.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.administrator.essim.R;


public class DetailsActivity extends AppCompatActivity {

    private Context mContext;
    private android.support.v7.widget.Toolbar mToolbar;
    private NestedScrollView mNestedScrollView;
    private float trans;
    private float scroll_height;
    private float scroll_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_details);
        mContext = this;
        scroll_height = getResources().getDimensionPixelOffset(R.dimen.scroll_height);

        mToolbar = findViewById(R.id.details_toolbar);
        mToolbar.getBackground().mutate().setAlpha(0);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mNestedScrollView = findViewById(R.id.nested_view);
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < scroll_height) {
                    scroll_now = scrollY;
                    trans = scroll_now / scroll_height;
                    mToolbar.getBackground().setAlpha((int) (trans * 255));
                } else {
                    if (mToolbar.getBackground().getAlpha() != 255) {
                        mToolbar.getBackground().setAlpha(255);
                        Log.d("^^^^%%%%", "绘制状态栏");
                    }
                }
            }
        });
    }
}
