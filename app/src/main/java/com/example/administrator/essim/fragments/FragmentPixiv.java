package com.example.administrator.essim.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.example.administrator.essim.utils.Common;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;

import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.essim.utils.Common.getRankType;


/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentPixiv extends BaseFragment {

    public int gotoPage;
    private TextView mTextView;
    private ViewPager vp;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<String> list = new ArrayList<String>();
    private OnChangeDataSet mChangeDataSet;
    private OnBMClickListener clickListener = new OnBMClickListener() {
        @Override
        public void onBoomButtonClick(int index) {
            if (vp.getCurrentItem() != 0) {
                vp.setCurrentItem(0);
            }
            if (mChangeDataSet != null) {
                switch (index) {
                    case 0:
                        mChangeDataSet.changeData(index);
                        break;
                    case 1:
                        mChangeDataSet.changeData(index);
                        break;
                    case 2:
                        mChangeDataSet.changeData(index);
                        break;
                    case 3:
                        mChangeDataSet.changeData(index);
                        break;
                    case 4:
                        mChangeDataSet.changeData(index);
                        break;
                    case 5:
                        mChangeDataSet.changeData(index);
                        break;
                    case 6:
                        mChangeDataSet.changeData(index);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pixiv, container, false);
        Toolbar toolbar = v.findViewById(R.id.toolbar_pixiv);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v1 -> MainActivity.sDrawerLayout.openDrawer(Gravity.START, true));
        list.add("榜单");
        list.add("热门标签");
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
                } else {
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

        BoomMenuButton bmb = v.findViewById(R.id.bmb);
        bmb.setUse3DTransformAnimation(true);
        bmb.setShowDuration(400);
        bmb.setHideDuration(550);
        bmb.setFrames(60);
        bmb.setNormalColor(getResources().getColor(R.color.colorAccent));
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .normalImageRes(R.drawable.ic_card_giftcard_black_24dp)
                    .imagePadding(new Rect(20, 20, 20, 60))
                    .normalText(Common.arrayOfRankMode[i])
                    .textRect(new Rect(Util.dp2px(15), Util.dp2px(42), Util.dp2px(65), Util.dp2px(72)))
                    .textSize(16)
                    .listener(clickListener);
            bmb.addBuilder(builder);
        }

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

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.mipmap.logo);
        builder.setCancelable(true);
        builder.setTitle("当前位置：" + getRankType() + "，第" + String.valueOf(Reference.sFragmentPixivLeft.now_page - 1) + "页");
        builder.setSingleChoiceItems(Common.arrayOfString, Reference.sFragmentPixivLeft.now_page - 2,
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