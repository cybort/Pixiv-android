package com.example.administrator.essim.anotherProj;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.administrator.essim.models.PixivMember;
import com.example.administrator.essim.models.Reference;
import com.example.administrator.essim.utils.Common;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private static float offset = 1f;
    private static float a;
    private static float b = 400.0f;
    public PixivMember mPixivMember;
    private ImageView bg;
    private RelativeLayout mRelativeLayout;
    private CircleImageView head;
    private TextView mTextView, mTextView2;
    private Toolbar rlNavBar;
    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;
    private LinearLayout llHeader;
    private int slidingDistance, currScrollY, index, currentPosition;
    private String url = "https://api.imjad.cn/pixiv/v1/?type=member&id=";
    private List<ScrollObservableFragment> displayFragments;
    private List<String> displayPageTitles = Arrays.asList("他的作品", "个人信息");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index = ((CloudMainActivity) getActivity()).index;
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        initView(v);
        initSlidingParams();
        return v;
    }

    private void initView(View v) {
        llHeader = v.findViewById(R.id.llHeader);
        tabStrip = v.findViewById(R.id.tabStrip);
        viewPager = v.findViewById(R.id.viewPager);
        rlNavBar = v.findViewById(R.id.rlNavBar);
        mTextView = v.findViewById(R.id.author_followers);
        mTextView2 = v.findViewById(R.id.author_follow);
        head = v.findViewById(R.id.people_head);
        mRelativeLayout = v.findViewById(R.id.follow_and_followers);
        bg = v.findViewById(R.id.people_bg);
        rlNavBar.setNavigationOnClickListener(v1 -> getActivity().finish());
        rlNavBar.setTitle(Reference.sPixivRankItem.response.get(0).works.get(index).work.user.getName() + "的主页");
        displayFragments = new ArrayList<>();
        displayFragments.add(HomeListFragment.newInstance());
        displayFragments.add(new HomeProfileFragment());

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
        Glide.with(getContext()).load(Reference.sPixivRankItem.response.get(0).works.
                get(index).work.image_urls.getPx_480mw())
                .bitmapTransform(new BlurTransformation(getContext(), 20, 2))
                .into(bg);
        DownLoad downLoad = new DownLoad();
        downLoad.execute(Reference.sPixivRankItem.response.get(0).
                works.get(index).work.user.profile_image_urls.getPx_170x170());
        getData(url + Reference.sPixivRankItem.response.get(0).works
                .get(index).work.user.getId());
    }

    private void getData(String address) {
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() -> TastyToast.makeText(getContext(), "数据加载失败", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Gson gson = new Gson();
                mPixivMember = gson.fromJson(responseData, PixivMember.class);
                getActivity().runOnUiThread(() -> refreshLayout());
            }
        });
    }

    private void refreshLayout() {
        mTextView.setText(getString(R.string.author_followers, mPixivMember.response.get(0).stats.getFollowing()));
        mTextView2.setText(getString(R.string.author_follow, mPixivMember.response.get(0).stats.getFriends()));
        Reference.sHomeProfileFragment.refreshLayout(mPixivMember);
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

    private class DownLoad extends AsyncTask<String, Integer, String> {
        Bitmap mDownLoadBtBitmap;

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Referer", "https://www.pixiv.net/member.php?id=" +
                        Reference.sPixivRankItem.response.get(0).works.get(((CloudMainActivity) getActivity()).index).work.user.getId());
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                mDownLoadBtBitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "ok";
        }

        @Override
        protected void onPostExecute(String result) {
            head.setImageBitmap(mDownLoadBtBitmap);
        }
    }
}