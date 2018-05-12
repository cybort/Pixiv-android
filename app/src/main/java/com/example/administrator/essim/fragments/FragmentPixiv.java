package com.example.administrator.essim.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.LoginActivity;
import com.example.administrator.essim.activities.MainActivity;
import com.example.administrator.essim.activities.SearchActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentPixiv extends BaseFragment {

    private Toolbar mToolbar;
    private ViewPager vp;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private String[] list = {"为你推荐", "热门标签"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pixiv, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        mToolbar = v.findViewById(R.id.toolbar_pixiv);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> ((MainActivity) Objects.requireNonNull(getActivity())).getDrawer().openDrawer(Gravity.START, true));
        fragments.add(new FragmentPixivLeft());
        fragments.add(new FragmentPixivRight());
        FragAdapter adapter = new FragAdapter(getFragmentManager(), fragments, list);
        vp = v.findViewById(R.id.viewpager);
        vp.setAdapter(adapter);
        final TabLayout mTabLayout = v.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(vp);
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

    private class FragAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;
        private String[] mFragmentTitles;

        private FragAdapter(FragmentManager fm, List<Fragment> fragments, String[] FragmentTitles) {
            super(fm);
            this.mFragments = fragments;
            this.mFragmentTitles = FragmentTitles;
        }

        @Override
        public Fragment getItem(int arg0) {
            return mFragments.get(arg0);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles[position];
        }
    }
}