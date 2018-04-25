package com.example.administrator.essim.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.MainActivity;
import com.example.administrator.essim.interfaces.OnChangeDataSet;
import com.example.administrator.essim.models.Reference;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentPixiv extends BaseFragment {

    public int gotoPage;
    private TextView mTextView;
    public FloatingActionMenu menuRed;
    private ViewPager vp;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<String> list = new ArrayList<String>();
    private FloatingActionButton fab1, fab2, fab3;
    private OnChangeDataSet mChangeDataSet;
    final String[] arrayOfString = {"1", "2", "3", "4", "5", "6", "7", "8", "9",
            "10"};

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

        vp = v.findViewById(R.id.viewpager);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_turn:
                if (vp.getCurrentItem() == 0) {
                    createDialog();
                } else if (vp.getCurrentItem() != 0) {
                    vp.setCurrentItem(0);
                }
                return true;
            case R.id.action_search:
                if (vp.getCurrentItem() != 1) {
                    vp.setCurrentItem(1);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getRankType() {
        if (Reference.sFragmentPixivLeft.currentDataType == 0) {
            return "日榜";
        } else if (Reference.sFragmentPixivLeft.currentDataType == 1) {
            return "周榜";
        } else {
            return "月榜";
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle("当前位置：" + getRankType() + "，第" + String.valueOf(Reference.sFragmentPixivLeft.now_page - 1) + "页");
        builder.setSingleChoiceItems(arrayOfString, Reference.sFragmentPixivLeft.now_page - 2,
                (dialogInterface, i) -> gotoPage = i + 1);
        builder.setPositiveButton("跳转", (dialogInterface, i) -> {
            if (Reference.sFragmentPixivLeft.now_page - 1 != gotoPage) {
                Reference.sFragmentPixivLeft.getData(Reference.sFragmentPixivLeft.now_link_address +
                        "&page=" + String.valueOf(gotoPage), true);
                Reference.sFragmentPixivLeft.now_page = gotoPage;
            }
        })
        .setNegativeButton("取消", (dialogInterface, i) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (dm.heightPixels * 0.6);
        p.width = (int) (dm.widthPixels * 1.0);
        dialog.getWindow().setAttributes(p);
    }

    public void setChangeDataSet(OnChangeDataSet changeDataSet) {
        mChangeDataSet = changeDataSet;
    }

    private class FragAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;
        private List<String> mFragmentTitles;

        private FragAdapter(FragmentManager fm, List<Fragment> fragments, List<String> FragmentTitles) {
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
            return mFragmentTitles.get(position);
        }
    }
}