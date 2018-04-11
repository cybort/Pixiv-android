package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments.FragmentHitikoto;
import com.example.administrator.essim.fragments.FragmentMine;
import com.example.administrator.essim.fragments.FragmentPixiv;
import com.example.administrator.essim.utils.CloudMainActivity;
import com.example.administrator.essim.utils.Common;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentPixiv mFragmentPixiv;
    private FragmentHitikoto mFragmentHitikoto;
    private FragmentMine mFragmentMine;
    private Fragment[] mFragments;
    private int lastShowFragment;
    private ImageView mImageView;
    private Context mContext;
    public static DrawerLayout sDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_main);
        mContext = this;
        initFragments();
        BottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_pixiv:
                        if (lastShowFragment != 0) {
                            switchFrament(lastShowFragment, 0);
                            lastShowFragment = 0;
                        }
                        break;
                    case R.id.tab_hitokoto:
                        if (lastShowFragment != 1) {
                            switchFrament(lastShowFragment, 1);
                            lastShowFragment = 1;
                        }
                        break;
                    case R.id.tab_mine:
                        if (lastShowFragment != 2) {
                            switchFrament(lastShowFragment, 2);
                            lastShowFragment = 2;
                        }
                        break;
                }
            }
        });
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {

            }
        });

        sDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        View headerView = navigationView.getHeaderView(0);
        mImageView = headerView.findViewById(R.id.imageView_nav_head);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CloudMainActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    public void switchFrament(int lastIndex, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(mFragments[lastIndex]);
        if (!mFragments[index].isAdded()) {
            transaction.add(R.id.fragment_container, mFragments[index]);
        }
        transaction.show(mFragments[index]).commitAllowingStateLoss();
    }

    private void initFragments() {
        mFragmentPixiv = new FragmentPixiv();
        mFragmentHitikoto = new FragmentHitikoto();
        mFragmentMine = new FragmentMine();
        mFragments = new Fragment[]{mFragmentPixiv, mFragmentHitikoto, mFragmentMine};
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mFragmentPixiv)
                .show(mFragmentPixiv)
                .commit();
        lastShowFragment = 0;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent mIntent;

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            mIntent = new Intent(this, AboutActivity.class);
            startActivity(mIntent);
        }
        return true;
    }
}
