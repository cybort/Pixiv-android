package com.example.administrator.essim.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.codekidlabs.storagechooser.Content;
import com.codekidlabs.storagechooser.StorageChooser;
import com.example.administrator.essim.R;
import com.example.administrator.essim.utils.Common;
import com.sdsmdg.tastytoast.TastyToast;

public class SettingsActivity extends AppCompatActivity {

    private Switch mSwitch;
    private String parentPath;
    private Context mContext;
    private Activity mActivity;
    private NestedScrollView mNestedScrollView;
    private SharedPreferences mSharedPreferences;
    private TextView mTextView, mTextView2, mTextView3, mTextView4, mTextView5, mTextView6, mTextView7;
    private StorageChooser.Builder builder = new StorageChooser.Builder();
    private StorageChooser chooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        mActivity = this;

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_about_card_show);
        mNestedScrollView = findViewById(R.id.nested_about);
        mNestedScrollView.startAnimation(animation);

        //初始化view
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Toolbar toolbar = findViewById(R.id.toolbar_pixiv);
        toolbar.setNavigationOnClickListener(view -> finish());
        mSwitch = findViewById(R.id.setting_switch);
        mTextView = findViewById(R.id.username);
        mTextView2 = findViewById(R.id.user_account);
        mTextView3 = findViewById(R.id.password);
        mTextView4 = findViewById(R.id.quit);
        mTextView5 = findViewById(R.id.real_path);
        mTextView6 = findViewById(R.id.setting_text_has_sdCard);
        mTextView7 = findViewById(R.id.text_has_permission);
        mSwitch.setChecked(mSharedPreferences.getBoolean("is_origin_pic", false));
        mSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("is_origin_pic", b);
            editor.apply();
        });
        mTextView.setText(mSharedPreferences.getString("username", ""));
        mTextView.setOnLongClickListener(view -> {
            Common.copyMessage(mContext, mTextView.getText().toString());
            return false;
        });
        mTextView2.setText(mSharedPreferences.getString("useraccount", ""));
        mTextView2.setOnLongClickListener(view -> {
            Common.copyMessage(mContext, mTextView2.getText().toString());
            return false;
        });
        mTextView3.setText(mSharedPreferences.getString("password", ""));
        mTextView3.setOnLongClickListener(view -> {
            Common.copyMessage(mContext, mTextView3.getText().toString());
            return false;
        });
        mTextView4.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        mTextView5.setText(mSharedPreferences.getString("download_path", "/storage/emulated/0/PixivPictures"));
        mTextView6.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            mActivity.startActivityForResult(i, 1);
            TastyToast.makeText(mContext, "请进入可插拔sd卡根目录，然后点击'确定'", Toast.LENGTH_LONG, TastyToast.DEFAULT).show();
        });
        mTextView7.setText(mSharedPreferences.getString("treeUri", "no sd card").equals("no sd card") ?
                "无SD卡读写权限或无SD卡" : "已获取SD卡读写权限");

        //初始化路径选择对话框
        Content c = new Content();
        c.setCreateLabel("Create");
        c.setInternalStorageText("内置存储");
        c.setCancelLabel("取消");
        c.setSelectLabel("就决定是你了");
        c.setOverviewHeading("选择存储器");
        builder.withActivity(this)
                .withFragmentManager(getFragmentManager())
                .setMemoryBarHeight(1.5f)
                .disableMultiSelect()
                .withContent(c);

        builder.allowCustomPath(true);
        builder.setType(StorageChooser.DIRECTORY_CHOOSER);
        chooser = builder.build();
        chooser.setOnSelectListener(path -> {
            //如果选出的路径不是机身自带路径，且未设置SD卡权限，则报错
            if (!path.contains("emulated") && mSharedPreferences.getString("treeUri", "no sd card").equals("no sd card")) {
                Snackbar.make(mTextView, "请先配置SD卡的读写权限!", Snackbar.LENGTH_SHORT).show();
            } else {
                editor.putString("download_path", path);
                editor.apply();
                mTextView5.setText(path);
            }
        });
        chooser.setOnCancelListener(() ->
                TastyToast.makeText(mContext, "取消选择", Toast.LENGTH_SHORT, TastyToast.CONFUSING).show());
        //为路径选择Textview设置点击事件
        mTextView5.setOnClickListener(view -> chooser.show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //申请读写权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SettingsActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 199);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri treeUri = data.getData();
            assert treeUri != null;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            if (":".equals(treeUri.getPath().substring(treeUri.getPath().length() - 1)) && !treeUri.getPath().contains("primary")) {
                editor.putString("treeUri", treeUri.toString());
                mTextView7.setText("已获取SD卡读写权限");
            } else {
                editor.putString("treeUri", "no sd card");
                mTextView7.setText("无SD卡读写权限或无SD卡");
            }
            editor.apply();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
