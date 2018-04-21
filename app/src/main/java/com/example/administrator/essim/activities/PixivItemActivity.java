package com.example.administrator.essim.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.administrator.essim.fragments.FragmentWorkItem;
import com.example.administrator.essim.models.DataSet;

public class PixivItemActivity extends SingleFragmentActivity {

    public int index;
    public String dataType;
    public int dataYp;

    @Override
    protected Fragment createFragment() {
        return new FragmentWorkItem();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        Intent intent = getIntent();
        index = intent.getIntExtra("which one is selected", 0);
        dataType = intent.getStringExtra("which kind data type");

        if (dataType.equals("TagResult")) {
            dataYp = 1;
            mToolbar.setTitle(DataSet.sSearchResult.response.get(index).getTitle());
        } else if (dataType.equals("AuthorWorks")) {
            dataYp = 0;
            mToolbar.setTitle(DataSet.sAuthorWorks.response.get(index).getTitle());
        }
    }
}
