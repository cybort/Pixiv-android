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
import com.example.administrator.essim.fragments.FragmentWorkItem;
import com.example.administrator.essim.models.Reference;


public class ViewPagerActivity extends AppCompatActivity {

    public ViewPager mViewPager;
    public Toolbar mToolbar;
    public String where_is_from;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_view_pager);
        Intent intent = getIntent();
        where_is_from = intent.getStringExtra("where is from");
        if (where_is_from.equals("FragmentTagResult")) {
            Reference.tempWork = Reference.sSearchResult;
        } else if (where_is_from.equals("FragmentAuthorHome")) {
            Reference.tempWork = Reference.sAuthorWorks;
        }
        index = intent.getIntExtra("which one is selected", 0);
        mToolbar = findViewById(R.id.view_pager_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> finish());
        mViewPager = findViewById(R.id.view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                if (where_is_from.equals("FragmentLeft")) {
                    return FragmentPixivItem.newInstance(position);
                } else {
                    return FragmentWorkItem.newInstance(position);
                }
            }

            @Override
            public int getCount() {
                if (where_is_from.equals("FragmentLeft")) {
                    return Reference.sPixivRankItem.response.get(0).works.size();
                } else {
                    return Reference.tempWork.response.size();
                }
            }
        });
        mViewPager.setCurrentItem(index);
    }

    public int getIndexNow() {
        return mViewPager.getCurrentItem();
    }

    public void changeTitle() {
        if (where_is_from.equals("FragmentLeft")) {
            mToolbar.setTitle(Reference.sPixivRankItem.response.get(0)
                    .works.get(mViewPager.getCurrentItem()).work.getTitle());
        } else {
            mToolbar.setTitle(Reference.tempWork.response.get(mViewPager.getCurrentItem()).getTitle());
        }
    }
}
