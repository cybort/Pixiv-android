package com.example.administrator.essim.anotherproj;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.FollowShowActivity;
import com.example.administrator.essim.api.AppApiPixivService;
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

public class HomeFragment extends Fragment {

    private static float offset = 1f;
    private static float a;
    private static float b = 400.0f;
    public SharedPreferences mSharedPreferences;
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
    private List<String> displayPageTitles = Arrays.asList("投稿", "收藏");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
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
        rlNavBar.setNavigationOnClickListener(v1 -> getActivity().finish());
        displayFragments = new ArrayList<>();
        displayFragments.add(HomeProfileFragment.newInstance());
        displayFragments.add(HomeListFragment.newInstance());

        ScrollObservableFragment.OnScrollChangedListener onScrollChangedListener =
                (fragment, scrolledX, scrolledY, dx, dy) -> {
                    if (fragment.equals(displayFragments.get(currentPosition))) {
                        scrollChangeHeader(scrolledY);
                    }
                };
        for (ScrollObservableFragment fragment : displayFragments) {
            fragment.setOnScrollChangedListener(onScrollChangedListener);
        }

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
    }

    private void getUserDetail() {
        Call<UserDetailResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getUserDetail(mSharedPreferences.getString("Authorization", ""), ((CloudMainActivity) getActivity()).userId);
        call.enqueue(new retrofit2.Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, retrofit2.Response<UserDetailResponse> response) {
                Reference.sUserDetailResponse = response.body();
                setData(Reference.sUserDetailResponse);
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable throwable) {

            }
        });
    }

    private void setData(UserDetailResponse userDetailResponse) {
        rlNavBar.setTitle(userDetailResponse.getUser().getName() + "的个人主页");
        mTextView.setText(String.format("粉丝: %d", userDetailResponse.getProfile().getTotal_follower()));
        mTextView2.setText(String.format("关注: %d", userDetailResponse.getProfile().getTotal_follow_users()));
        mTextView2.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, FollowShowActivity.class);
            intent.putExtra("user id", ((CloudMainActivity) Objects.requireNonNull(getActivity())).userId);
            mContext.startActivity(intent);
        });
        if (userDetailResponse.getUser().getId() != mSharedPreferences.getInt("userid", 0)) {
            mTextView3.setVisibility(View.VISIBLE);
            if (userDetailResponse.getUser().isIs_followed()) {
                mTextView3.setText("取消关注");
            } else {
                mTextView3.setText("+关注");
            }
            mTextView3.setOnClickListener(view -> {
                if (userDetailResponse.getUser().isIs_followed()) {
                    mTextView3.setText("取消关注");
                    Common.postUnFollowUser(mSharedPreferences.getString("Authorization", ""),
                            Reference.sUserDetailResponse.getUser().getId(), mTextView3);
                    mTextView3.setText("+关注");
                    userDetailResponse.getUser().setIs_followed(false);
                } else {
                    mTextView3.setText("+关注");
                    Common.postFollowUser(mSharedPreferences.getString("Authorization", ""),
                            Reference.sUserDetailResponse.getUser().getId(), mTextView3);
                    mTextView3.setText("取消关注");
                    userDetailResponse.getUser().setIs_followed(true);
                }
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
        Log.d("HomeFragment", "slidingDistance" + slidingDistance);
    }

    /**
     * 根据页面滑动距离改变Header方法
     */
    private void scrollChangeHeader(int scrolledY) {
        if (scrolledY < 0) {
            scrolledY = 0;
        }
        if (scrolledY < slidingDistance) {
            rlNavBar.setBackgroundColor(Color.argb(scrolledY * 192 / slidingDistance, 0x00, 0x00, 0x00));
            a = scrolledY;
            head.setAlpha(offset - a / b);
            mRelativeLayout.setAlpha(offset - a / b);
            llHeader.setPadding(0, -scrolledY, 0, 0);
            currScrollY = scrolledY;
        } else {

            rlNavBar.setBackgroundColor(Color.argb(192, 0x00, 0x00, 0x00));
            llHeader.setPadding(0, -slidingDistance, 0, 0);
            currScrollY = slidingDistance;
        }
    }
}