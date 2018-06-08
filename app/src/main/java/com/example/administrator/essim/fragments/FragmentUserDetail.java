package com.example.administrator.essim.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.FollowShowActivity;
import com.example.administrator.essim.activities.UserDetailActivity;
import com.example.administrator.essim.interf.ShowProgress;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.response.UserDetailResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.GlideUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;


public class FragmentUserDetail extends Fragment {

    public static int scrollYset;
    public static ShowProgress mShowProgress;
    private static float offset = 1f;
    private static float a;
    private static float b = 400.0f;
    private ImageView bg;
    private RelativeLayout mRelativeLayout;
    private CircleImageView head;
    private TextView mTextView, mTextView2, mTextView3;
    private Toolbar rlNavBar;
    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;
    private LinearLayout llHeader;
    private Context mContext;
    private int slidingDistance, currScrollY, currentPosition;
    private List<ScrollObservableFragment> displayFragments;
    private List<String> displayPageTitles;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getContext();
        initView(v);
        initSlidingParams();
        getUserDetail();
        return v;
    }

    private void initView(View v) {
        mProgressBar = v.findViewById(R.id.try_login);
        mShowProgress = this::showProgressNow;
        llHeader = v.findViewById(R.id.llHeader);
        tabStrip = v.findViewById(R.id.tabStrip);
        viewPager = v.findViewById(R.id.viewPager);
        rlNavBar = v.findViewById(R.id.rlNavBar);
        mTextView = v.findViewById(R.id.author_followers);
        mTextView2 = v.findViewById(R.id.author_follow);
        mTextView3 = v.findViewById(R.id.post_like_user);
        mTextView3.setVisibility(View.INVISIBLE);
        head = v.findViewById(R.id.people_head);
        mRelativeLayout = v.findViewById(R.id.follow_and_followers);
        bg = v.findViewById(R.id.people_bg);
        ((UserDetailActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(rlNavBar);
        rlNavBar.setNavigationOnClickListener(v1 -> Objects.requireNonNull(getActivity()).finish());
        displayFragments = new ArrayList<>();
        displayFragments.add(FragmentUserWorks.newInstance());
        displayFragments.add(FragmentUserFollow.newInstance());

        ScrollObservableFragment.OnScrollChangedListener onScrollChangedListener =
                (fragment, scrolledX, scrolledY, dx, dy) -> {
                    if (fragment.equals(displayFragments.get(currentPosition))) {
                        scrollChangeHeader(scrolledY);
                    }
                };
        for (ScrollObservableFragment fragment : displayFragments) {
            fragment.setOnScrollChangedListener(onScrollChangedListener);
        }
    }

    private void getUserDetail() {
        Call<UserDetailResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getUserDetail(Common.getLocalDataSet(mContext).getString("Authorization", ""), ((UserDetailActivity) getActivity()).userId);
        call.enqueue(new retrofit2.Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, retrofit2.Response<UserDetailResponse> response) {
                try {
                    Reference.sUserDetailResponse = response.body();
                    setData(Reference.sUserDetailResponse);
                } catch (Exception e) {
                    Snackbar.make(mTextView, "不存在这个用户", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable throwable) {

            }
        });
    }

    private void setData(UserDetailResponse userDetailResponse) {
        rlNavBar.setTitle(userDetailResponse.getUser().getName() + "的个人主页");
        displayPageTitles = Arrays.asList("投稿(" + String.valueOf(userDetailResponse.getProfile().getTotal_illusts() + ")"),
                "收藏(" + String.valueOf(userDetailResponse.getProfile().getTotal_illust_bookmarks_public()) + ")");
        mTextView.setText(userDetailResponse.getProfile().getTotal_follower() < 1000 ?
                String.format(getString(R.string.followers), userDetailResponse.getProfile().getTotal_follower()) :
                String.format(getString(R.string.followers_k), userDetailResponse.getProfile().getTotal_follower() / 1000));
        mTextView2.setText(userDetailResponse.getProfile().getTotal_follow_users() < 1000 ?
                String.format(getString(R.string.follow), userDetailResponse.getProfile().getTotal_follow_users()) :
                String.format(getString(R.string.follow_k), userDetailResponse.getProfile().getTotal_follow_users() / 1000));
        mTextView2.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, FollowShowActivity.class);
            intent.putExtra("user id", ((UserDetailActivity) Objects.requireNonNull(getActivity())).userId);
            mContext.startActivity(intent);
        });
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return displayFragments.get(position);
            }

            @Override
            public int getCount() {
                return displayFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return displayPageTitles.get(position);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                displayFragments.get(position).setScrolledY(currScrollY);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabStrip.setViewPager(viewPager);
        viewPager.setCurrentItem(currentPosition);
        if (userDetailResponse.getUser().getId() != Common.getLocalDataSet(mContext).getInt("userid", 0)) {
            mTextView3.setVisibility(View.VISIBLE);
            if (userDetailResponse.getUser().isIs_followed()) {
                mTextView3.setText("取消关注");
            } else {
                mTextView3.setText("+关注");
            }
            mTextView3.setOnClickListener(view -> {
                if (userDetailResponse.getUser().isIs_followed()) {
                    Common.postUnFollowUser(Common.getLocalDataSet(mContext).getString("Authorization", ""),
                            userDetailResponse.getUser().getId(), mTextView3);
                    mTextView3.setText("+关注");
                    userDetailResponse.getUser().setIs_followed(false);
                } else {
                    Common.postFollowUser(Common.getLocalDataSet(mContext).getString("Authorization", ""),
                            userDetailResponse.getUser().getId(), mTextView3, "public");
                    mTextView3.setText("取消关注");
                    userDetailResponse.getUser().setIs_followed(true);
                }
            });
            mTextView3.setOnLongClickListener(view -> {
                if (!userDetailResponse.getUser().isIs_followed()) {
                    Common.postFollowUser(Common.getLocalDataSet(mContext).getString("Authorization", ""),
                            userDetailResponse.getUser().getId(), mTextView3, "private");
                    mTextView3.setText("取消关注");
                    userDetailResponse.getUser().setIs_followed(true);
                }
                return true;
            });
        }
        Glide.with(mContext).load(new GlideUtil().getHead(userDetailResponse.getUser())).into(head);
    }

    /**
     * 初始化滑动参数,k值
     */
    private void initSlidingParams() {
        int headerSize = getResources().getDimensionPixelOffset(R.dimen.home_header_size);
        int navBarHeight = getResources().getDimensionPixelOffset(R.dimen.tabstrip_height2);
        int tabStripHeight = getResources().getDimensionPixelOffset(R.dimen.tabstrip_height);
        slidingDistance = headerSize - navBarHeight - tabStripHeight;
    }

    /**
     * 根据页面滑动距离改变Header方法
     */
    private void scrollChangeHeader(int scrolledY) {
        if (scrolledY < 0) {
            scrolledY = 0;
            scrollYset = scrolledY;
        }
        if (scrolledY < slidingDistance) {
            rlNavBar.setBackgroundColor(Color.argb(scrolledY * 192 / slidingDistance, 0x00, 0x00, 0x00));
            a = scrolledY;
            head.setAlpha(offset - a / b);
            mRelativeLayout.setAlpha(offset - a / b);
            llHeader.setPadding(0, -scrolledY, 0, 0);
            currScrollY = scrolledY;
            Common.showLog(scrolledY);
            scrollYset = scrolledY;
            Common.showLog(scrollYset);
        } else {
            rlNavBar.setBackgroundColor(Color.argb(192, 0x00, 0x00, 0x00));
            llHeader.setPadding(0, -slidingDistance, 0, 0);
            currScrollY = slidingDistance;
            scrollYset = slidingDistance;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (((UserDetailActivity) Objects.requireNonNull(getActivity())).userId ==
                Common.getLocalDataSet(mContext).getInt("userid", 0)) {
            inflater.inflate(R.menu.user_star, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (viewPager.getCurrentItem() == 1) {
            switch (item.getItemId()) {
                case R.id.action_get_public:
                    if (FragmentUserFollow.dataType != 0) {
                        FragmentUserFollow.sRefreshLayout.refreData("public");
                    }
                    break;
                case R.id.action_get_private:
                    if (FragmentUserFollow.dataType != 1) {
                        FragmentUserFollow.sRefreshLayout.refreData("private");
                    }
                    break;
                default:
                    break;

            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showProgressNow(boolean b) {
        mProgressBar.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        scrollYset = 0;
    }
}