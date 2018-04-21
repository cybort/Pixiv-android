package com.example.administrator.essim.fragments;

import android.os.Bundle;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.MainActivity;
import com.example.administrator.essim.interfaces.OnChangeDataSet;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentPixiv extends BaseFragment {

    public FloatingActionMenu menuRed;
    List<Fragment> fragments = new ArrayList<Fragment>();
    ArrayList<String> list = new ArrayList<String>();
    private TextView mTextView;
    private FloatingActionButton fab1, fab2, fab3;
    private OnChangeDataSet mChangeDataSet;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab1:
                    if (mChangeDataSet != null) {
                        mChangeDataSet.changeData(0); //获取日榜单
                    }
                    break;
                case R.id.fab2:
                    if (mChangeDataSet != null) {
                        mChangeDataSet.changeData(1); //获取周榜单
                    }
                    break;
                case R.id.fab3:
                    if (mChangeDataSet != null) {
                        mChangeDataSet.changeData(2); //获取月榜单
                    }
                    break;
            }
            menuRed.close(true);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pixiv, container, false);
        Toolbar toolbar = v.findViewById(R.id.toolbar_pixiv);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v1 -> MainActivity.sDrawerLayout.openDrawer(Gravity.START, true));
        list.add("Rank  List");
        list.add("Hot  Tags");
        fragments.add(new FragmentPixivLeft());
        fragments.add(new FragmentPixivRight());
        FragAdapter adapter = new FragAdapter(getChildFragmentManager(), fragments, list);

        ViewPager vp = v.findViewById(R.id.viewpager);
        vp.setAdapter(adapter);

        final TabLayout mTabLayout = v.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(vp);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    menuRed.hideMenuButton(true);
                } else {
                    menuRed.showMenuButton(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mTextView = v.findViewById(R.id.toolbar_title_one);
        mTextView.setOnClickListener(v12 -> MainActivity.sDrawerLayout.openDrawer(Gravity.START, true));

        menuRed = v.findViewById(R.id.menu_red);
        fab1 = v.findViewById(R.id.fab1);
        fab2 = v.findViewById(R.id.fab2);
        fab3 = v.findViewById(R.id.fab3);
        menuRed.setClosedOnTouchOutside(true);
        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_pixiv, menu);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setHasOptionsMenu(true);
    }

    public void setChangeDataSet(OnChangeDataSet changeDataSet) {
        mChangeDataSet = changeDataSet;
    }

    private class FragAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;
        private List<String> mFragmentTitles;

        private FragAdapter(FragmentManager fm, List<Fragment> fragments, List<String> FragmentTitles) {
            super(fm);
            // TODO Auto-generated constructor stub
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
            return mFragmentTitles.get(position);
        }
    }
}