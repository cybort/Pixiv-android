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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments.FragmentHitikoto;
import com.example.administrator.essim.fragments.FragmentMine;
import com.example.administrator.essim.fragments.FragmentPixiv;
import com.example.administrator.essim.fragments.FragmentRank;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.GlideUtil;
import com.roughike.bottombar.BottomBar;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private long mExitTime;
    private Context mContext;
    private DrawerLayout drawer;
    private int lastShowFragment;
    private Fragment[] mFragments;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_main);
        mContext = this;
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //判断是否有登录记录，没登录就去LoginActivity，登录了就加载视图
        if (Common.getLocalDataSet(mContext).getBoolean("islogin", false)) {
            TextView textView = navigationView.getHeaderView(0).findViewById(R.id.username);
            textView.setText(Common.getLocalDataSet(mContext).getString("username", "")
                    .equals(Common.getLocalDataSet(mContext).getString("useraccount", "")) ?
                    Common.getLocalDataSet(mContext).getString("username", "") : String.format("%s (%s)",
                    Common.getLocalDataSet(mContext).getString("username", ""),
                    Common.getLocalDataSet(mContext).getString("useraccount", "")));
            ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
            Glide.with(mContext).load(new GlideUtil().getHead(Common.getLocalDataSet(mContext).getInt("userid", 0),
                    Common.getLocalDataSet(mContext).getString("hearurl", ""))).into(imageView);
            imageView.setOnClickListener(view -> {
                if (Common.getLocalDataSet(mContext).getBoolean("islogin", false)) {
                    Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                    intent.putExtra("user id", Common.getLocalDataSet(mContext).getInt("userid", 0));
                    startActivity(intent);
                }
            });
            mImageView = navigationView.getHeaderView(0).findViewById(R.id.header_img);
            BottomBar bottomBar = findViewById(R.id.bottomBar);
            bottomBar.setOnTabSelectListener(tabId -> {
                switch (tabId) {
                    case R.id.tab_pixiv:
                        if (lastShowFragment != 0) {
                            switchFrament(lastShowFragment, 0);
                            lastShowFragment = 0;
                        }
                        break;
                    case R.id.tab_rank:
                        if (lastShowFragment != 1) {
                            switchFrament(lastShowFragment, 1);
                            lastShowFragment = 1;
                        }
                        break;
                    case R.id.tab_hitokoto:
                        if (lastShowFragment != 2) {
                            switchFrament(lastShowFragment, 2);
                            lastShowFragment = 2;
                        }
                        break;
                    case R.id.tab_mine:
                        if (lastShowFragment != 3) {
                            switchFrament(lastShowFragment, 3);
                            lastShowFragment = 3;
                        }
                        break;
                    default:
                        break;
                }
            });
            initFragments();
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
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
        FragmentPixiv fragmentPixiv = new FragmentPixiv();
        FragmentRank fragmentHitikoto = new FragmentRank();
        FragmentHitikoto fragmentHitokoto = new FragmentHitikoto();
        FragmentMine fragmentMine = new FragmentMine();
        mFragments = new Fragment[]{fragmentPixiv, fragmentHitikoto, fragmentHitokoto, fragmentMine};
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragmentPixiv)
                .show(fragmentPixiv)
                .commit();
        lastShowFragment = 0;
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("店长推荐：");
        builder.setMessage("请确认你的账号已开启R-18");
        builder.setCancelable(true);
        builder.setPositiveButton("我已经开好了", (dialogInterface, i) -> {
            Intent intent = new Intent(mContext, SearchTagActivity.class);
            intent.putExtra("what is the keyword", "R-18");
            mContext.startActivity(intent);
        });
        builder.setNegativeButton("没开", (dialogInterface, i) -> runOnUiThread(() ->
                TastyToast.makeText(MainActivity.this, "你是个好人",
                        TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show()));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            //特辑走一波
            Intent intent = new Intent(mContext, SpecialCollectionActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(mContext, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            createDialog();
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(mContext, AboutActivity.class);
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Common.getLocalDataSet(mContext).getString("header_img_path", "").length() != 0) {
            Glide.with(mContext)
                    .load(Common.getLocalDataSet(mContext).getString("header_img_path", ""))
                    .into(mImageView);
        }
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
}
