package com.example.administrator.essim.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments.FragmentWorkItem;
import com.example.administrator.essim.models.DataSet;

public class PixivItemActivity extends AppCompatActivity {

    public int index;
    public String dataType;
    public int dataYp;
    public Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_pixiv_item);

        Intent intent = getIntent();
        index = intent.getIntExtra("which one is selected", 0);
        dataType = intent.getStringExtra("which kind data type");

        mToolbar = findViewById(R.id.tlbar_vp_two);
        if(dataType.equals("TagResult"))
        {
            dataYp = 1;
            mToolbar.setTitle(DataSet.sSearchResult.response.get(index).getTitle());
            setSupportActionBar(mToolbar);
        }
        else if(dataType.equals("AuthorWorks"))
        {
            dataYp = 0;
            mToolbar.setTitle(DataSet.sAuthorWorks.response.get(index).getTitle());
            setSupportActionBar(mToolbar);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.pixiv_item_container);
        if(fragment == null)
        {
            fragment = new FragmentWorkItem();
            fm.beginTransaction()
                    .add(R.id.pixiv_item_container, fragment)
                    .commit();
        }
    }
}
