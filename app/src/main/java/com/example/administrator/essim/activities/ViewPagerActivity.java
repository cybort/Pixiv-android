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

import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments.FragmentPixivItem;
import com.example.administrator.essim.response.Reference;

public class ViewPagerActivity extends AppCompatActivity {

    public Toolbar mToolbar;
    public ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_view_pager);

        Intent intent = getIntent();
        int index = intent.getIntExtra("which one is selected", 0);

        mToolbar = findViewById(R.id.view_pager_toolbar);
        mToolbar.setNavigationOnClickListener(v -> finish());
        mViewPager = findViewById(R.id.view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return FragmentPixivItem.newInstance(position);
            }

            @Override
            public int getCount() {
                return Reference.sIllustsBeans.size();
            }
        });
        mViewPager.setCurrentItem(index);
    }

    public void changeTitle() {
        mToolbar.setTitle(Reference.sIllustsBeans.get(mViewPager.getCurrentItem()).getTitle());
    }
}
