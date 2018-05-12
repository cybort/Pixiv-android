package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import com.example.administrator.essim.R;
import com.example.administrator.essim.anotherproj.CloudMainActivity;
import com.example.administrator.essim.fragments.FragmentHitikoto;
import com.example.administrator.essim.fragments.FragmentMine;
import com.example.administrator.essim.fragments.FragmentPixiv;
import com.example.administrator.essim.fragments.FragmentRank;
import com.roughike.bottombar.BottomBar;
import com.sdsmdg.tastytoast.TastyToast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private long mExitTime;
    private boolean isLogin;
    private Context mContext;
    private BottomBar bottomBar;
    private DrawerLayout drawer;
    private ImageView mImageView;
    private int lastShowFragment;
    private Fragment[] mFragments;
    private TextView mTextView, mTextView2;
    public SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_main);
        mContext = this;
        initFragments();

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        isLogin = mSharedPreferences.getBoolean("islogin", false);
        if (isLogin) {
            mTextView = navigationView.getHeaderView(0).findViewById(R.id.username);
            mTextView2 = navigationView.getHeaderView(0).findViewById(R.id.useremail);
            if (mSharedPreferences.getString("username", "")
                    .equals(mSharedPreferences.getString("useraccount", ""))) {
                mTextView.setText(mSharedPreferences.getString("username", ""));
            } else {
                mTextView.setText(String.format("%s (%s)", mSharedPreferences.getString("username", ""),
                        mSharedPreferences.getString("useraccount", "")));
            }
            mTextView2.setText(String.format("密码：%s", mSharedPreferences.getString("password", "")));
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mImageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        mImageView.setOnClickListener(view -> {
            if (mSharedPreferences.getBoolean("islogin", false)) {
                Intent intent = new Intent(MainActivity.this, CloudMainActivity.class);
                intent.putExtra("user id", mSharedPreferences.getInt("userid", 0));
                startActivity(intent);
            }
        });

        bottomBar = findViewById(R.id.bottomBar);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("店长推荐：");
        builder.setMessage("你是萝莉控吗？");
        builder.setCancelable(true);
        builder.setPositiveButton("我是", (dialogInterface, i) -> {
            Intent intent = new Intent(mContext, SearchTagActivity.class);
            intent.putExtra("what is the keyword", "R-18");
            mContext.startActivity(intent);
        });
        builder.setNegativeButton("我不是", (dialogInterface, i) -> runOnUiThread(() ->
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

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Snackbar.make(bottomBar, "别想了，有个锤子的新世界", Snackbar.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(mContext, AboutActivity.class);
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
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
