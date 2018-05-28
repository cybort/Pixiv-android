package com.example.administrator.essim.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
    private SharedPreferences mSharedPreferences;
    private TextView mTextView, mTextView2, mTextView3, mTextView4, mTextView5;
    private StorageChooser.Builder builder = new StorageChooser.Builder();
    private StorageChooser chooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mContext = this;
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
        mTextView5.setText(mSharedPreferences.getString("download_path", "/storage/emulated/0/PixivPictures"));

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
        mTextView5.setOnClickListener(view -> {
            chooser.setOnSelectListener(path -> {
                editor.putString("download_path", path);
                editor.apply();
                mTextView5.setText(path);
            });
            chooser.setOnCancelListener(() ->
                    TastyToast.makeText(mContext, "取消选择", Toast.LENGTH_SHORT, TastyToast.CONFUSING).show());
            chooser.show();
        });

        Common.showLog("hahahahahahahahashaushusadhuashd");
        Common.showLog(Environment.getExternalStorageDirectory().getPath());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SettingsActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 199);
        }
    }
}
