package com.example.administrator.essim.anotherProj;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.models.DataSet;
import com.example.administrator.essim.models.PixivMember;
import com.example.administrator.essim.utils.Common;


public class HomeProfileFragment extends ScrollObservableFragment {

    private ObservableScrollView osvHomeRecommend;
    private TextView mTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DataSet.sHomeProfileFragment = this;
        View v = inflater.inflate(R.layout.fragment_home_profile, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        mTextView = v.findViewById(R.id.author_introduction);
        osvHomeRecommend = v.findViewById(R.id.osvHomeRecommend);
        osvHomeRecommend.setOnScrollChangedListener((scrollView, scrolledX, scrolledY, dx, dy) ->
                doOnScrollChanged(scrolledX, scrolledY, dx, dy));
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
