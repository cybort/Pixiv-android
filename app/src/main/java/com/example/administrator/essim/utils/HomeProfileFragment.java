package com.example.administrator.essim.utils;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.models.PixivMember;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 歌手信息展示所用页面
 *
 * @author linxiao
 * @version 1.0.0
 */
public class HomeProfileFragment extends ScrollObservableFragment {

    @Bind(R.id.osvHomeRecommend)
    ObservableScrollView osvHomeRecommend;
    @Bind(R.id.author_introduction)
    TextView mTextView;
    private View contentView;

    public HomeProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Common.sHomeProfileFragment = this;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_home_profile, container, false);
            ButterKnife.bind(this, contentView);
            initView();
        }
        return contentView;
    }

    private void initView() {
        osvHomeRecommend.setOnScrollChangedListener(new ObservableScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ScrollView scrollView, int scrolledX, int scrolledY, int dx, int dy) {
                doOnScrollChanged(scrolledX, scrolledY, dx, dy);
            }
        });
    }

    public void refreshLayout(PixivMember pixivMember) {
        mTextView.setText(pixivMember.response.get(0).profile.getIntroduction());
    }

    @Override
    public void setScrolledY(int scrolledY) {
        if (osvHomeRecommend != null) {
            osvHomeRecommend.scrollTo(0, scrolledY);
        }
    }
}
