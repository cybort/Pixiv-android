package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments.FragmentHitikoto;
import com.example.administrator.essim.fragments.FragmentMine;
import com.example.administrator.essim.fragments.FragmentPixiv;
import com.roughike.bottombar.BottomBar;
import com.sdsmdg.tastytoast.TastyToast;
import com.stephentuso.welcome.WelcomeHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static DrawerLayout sDrawerLayout;
    private FragmentPixiv mFragmentPixiv;
    private FragmentHitikoto mFragmentHitikoto;
    private FragmentMine mFragmentMine;
    private Fragment[] mFragments;
    private int lastShowFragment;
    private Context mContext;
    private ImageView mImageView;
    private long mExitTime;
    private WelcomeHelper welcomeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        welcomeScreen = new WelcomeHelper(this, MyWelcomeActivity.class);
        welcomeScreen.show(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initFragments();
        BottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(tabId -> {
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
        });
        bottomBar.setOnTabReselectListener(tabId -> {

        });

        sDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        mImageView = navigationView.getHeaderView(0).findViewById(R.id.imageView_nav_head);
        mImageView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                exit();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            TastyToast.makeText(MainActivity.this, "再按一次退出~", Toast.LENGTH_SHORT, TastyToast.INFO).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("店长推荐：");
        builder.setMessage("你是萝莉控吗？");
        builder.setCancelable(true);
        builder.setPositiveButton("我是", (dialogInterface, i) -> {
            Intent intent = new Intent(mContext, TagResultActivity.class);
            intent.putExtra("which one is selected", -2);
            mContext.startActivity(intent);
        })
                .setNegativeButton("我不是", (dialogInterface, i) -> runOnUiThread(() ->
                        TastyToast.makeText(MainActivity.this, "你是个好人", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show()));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent mIntent;

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_go_r_18) {
            createDialog();
        } else if (id == R.id.nav_send) {
            mIntent = new Intent(this, AboutActivity.class);
            startActivity(mIntent);
        }
        return true;
    }
}
