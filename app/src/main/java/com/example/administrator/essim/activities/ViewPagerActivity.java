package com.example.administrator.essim.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments.FragmentPixivItem;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.utils.Common;

import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {

    public int index;
    public Toolbar mToolbar;
    public ViewPager mViewPager;
    private List<IllustsBean> mIllustsBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_view_pager);

        Intent intent = getIntent();
        index = intent.getIntExtra("which one is selected", 0);
        Common.showLog(index);
        mIllustsBeans = (List<IllustsBean>) intent.getSerializableExtra("all illust");

        mToolbar = findViewById(R.id.view_pager_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> finish());
        mViewPager = findViewById(R.id.view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return FragmentPixivItem.newInstance(mIllustsBeans.get(position), position);
            }

            @Override
            public int getCount() {
                return mIllustsBeans.size();
            }
        });
        mViewPager.setCurrentItem(index);
    }

    public int getIndexNow() {
        return mViewPager.getCurrentItem();
    }

    public void changeTitle() {
        mToolbar.setTitle(mIllustsBeans.get(mViewPager.getCurrentItem()).getTitle());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIllustsBeans = null;
    }
}
