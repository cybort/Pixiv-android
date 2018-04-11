package com.example.administrator.essim.utils;


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
import com.example.administrator.essim.fragments.FragmentPixivLeft;
import com.example.administrator.essim.models.PixivMember;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 首页,管理滑动显示
 *
 * @author linxiao
 * @version 1.0.0
 */
public class HomeFragment extends BaseFragment {

    private View contentView;
    public PixivMember mPixivMember;
    private static float offset = 1f;
    private static float a;
    private static float b = 400.0f;
    private String url = "https://api.imjad.cn/pixiv/v1/?type=member&id=";


    @Bind(R.id.people_bg)
    ImageView bg;

    @Bind(R.id.follow_and_followers)
    RelativeLayout mRelativeLayout;

    @Bind(R.id.people_head)
    CircleImageView head;

    @Bind(R.id.author_followers)
    TextView mTextView;

    @Bind(R.id.author_follow)
    TextView mTextView2;

    @Bind(R.id.rlNavBar)
    Toolbar rlNavBar;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.tabStrip)
    PagerSlidingTabStrip tabStrip;

    @Bind(R.id.llHeader)
    LinearLayout llHeader;

    int slidingDistance;
    int currScrollY;
    int index;
    int currentPosition;

    private List<ScrollObservableFragment> displayFragments;
    private List<String> displayPageTitles = Arrays.asList("他的作品", "个人信息");

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index = ((CloudMainActivity)getActivity()).index;
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.bind(this, contentView);

            initView();
        }
        initSlidingParams();

        return contentView;
    }

    private void initView() {
        rlNavBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        rlNavBar.setTitle(FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(index).work.user.getName()+"的主页");
        displayFragments = new ArrayList<>();
        displayFragments.add(HomeListFragment.newInstance());
        displayFragments.add(new HomeProfileFragment());

        ScrollObservableFragment.OnScrollChangedListener onScrollChangedListener = new ScrollObservableFragment.OnScrollChangedListener() {

            @Override
            public void onScrollChanged(ScrollObservableFragment fragment, int scrolledX, int scrolledY, int dx, int dy) {
                if (fragment.equals(displayFragments.get(currentPosition))) {
                    scrollChangeHeader(scrolledY);
                }
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
//                LogcatUtils.d("HomeFragment", "position = " + position);
//                displayFragments.get(position).setScrolledY(currScrollY);
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
        Glide.with(getContext()).load(FragmentPixivLeft.mPixivRankItem.response.get(0).works.
                get(index).work.image_urls.getPx_480mw())
                .bitmapTransform(new BlurTransformation(getContext(), 20, 2))
                .into(bg);
        DownLoad downLoad = new DownLoad();
        downLoad.execute(FragmentPixivLeft.mPixivRankItem.response.get(0).
                works.get(index).work.user.profile_image_urls.getPx_170x170());
        getData(url+FragmentPixivLeft.mPixivRankItem.response.get(0).works
        .get(index).work.user.getId());
    }

    private void getData(String address) {
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Gson gson = new Gson();
                mPixivMember = gson.fromJson(responseData, PixivMember.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout();
                    }
                });
            }
        });
    }

    private void refreshLayout()
    {
        mTextView.setText(getString(R.string.author_followers, mPixivMember.response.get(0).stats.getFollowing()));
        mTextView2.setText(getString(R.string.author_follow, mPixivMember.response.get(0).stats.getFriends()));
        Common.sHomeProfileFragment.refreshLayout(mPixivMember);
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
            Log.d("&&&&^^^^", "执行了一次会吐");
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

    private class DownLoad extends AsyncTask<String, Integer, String>
    {
        Bitmap mDownLoadBtBitmap;
        //onPreExecute方法在execute()后执行
        @Override
        protected void onPreExecute()
        {
        }

        //doInBackground方法内部执行后台任务,不能在里面更新UI，否则有异常。
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Referer", "https://www.pixiv.net/member.php?id=" +
                        FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(((CloudMainActivity)getActivity()).index).work.user.getId());
                connection.connect();
                // 获取输入流
                InputStream inputStream = connection.getInputStream();
                //将InputStream转换成Bitmap
                mDownLoadBtBitmap= BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            }catch(IOException e)
            {
                e.printStackTrace();
            }

            return "ok";
        }

        @Override
        protected void onPostExecute(String result)
        {
            head.setImageBitmap(mDownLoadBtBitmap);
        }
    }

}