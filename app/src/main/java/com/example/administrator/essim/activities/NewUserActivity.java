package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.api.AccountPixivService;
import com.example.administrator.essim.api.OAuthSecureService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.PixivAccountsResponse;
import com.example.administrator.essim.response.PixivOAuthResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class NewUserActivity extends AppCompatActivity {

    private CardView mCardView;
    private EditText mEditText;
    private ProgressBar mProgressBar;
    private String Authorization = "Bearer l-f9qZ0ZyqSwRyZs8-MymbtWBbSxmCu1pmbOlyisou8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_new_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        mProgressBar = findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mEditText = findViewById(R.id.login_username);
        mCardView = findViewById(R.id.card_login);
        mCardView.setOnClickListener(view -> {
            if (mEditText.getText().toString().trim().isEmpty()) {
                Snackbar.make(view, "用户名不能为空", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (!mEditText.getText().toString().trim().isEmpty()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                if (imm.isActive() && getCurrentFocus() != null) {
                    if (getCurrentFocus().getWindowToken() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                mProgressBar.setVisibility(View.VISIBLE);
                getNewAccount();
            }
        });
    }

    private void getNewAccount() {
        Call<PixivAccountsResponse> call = new RestClient()
                .getRetrofit_Account().create(AccountPixivService.class)
                .createProvisionalAccount(mEditText.getText().toString().trim(),
                        "pixiv_android_app_provisional_account",
                        Authorization);
        call.enqueue(new Callback<PixivAccountsResponse>() {
            @Override
            public void onResponse(Call<PixivAccountsResponse> call, retrofit2.Response<PixivAccountsResponse> response) {
                PixivAccountsResponse pixivOAuthResponse = response.body();
                HashMap localHashMap = new HashMap();
                localHashMap.put("client_id", "KzEZED7aC0vird8jWyHM38mXjNTY");
                localHashMap.put("client_secret", "W9JZoJe00qPvJsiyCGT3CCtC6ZUtdpKpzMbNlUGP");
                localHashMap.put("grant_type", "password");
                localHashMap.put("username", pixivOAuthResponse.getBody().getUser_account());
                localHashMap.put("password", pixivOAuthResponse.getBody().getPassword());
                localHashMap.put("device_token", pixivOAuthResponse.getBody().getDevice_token());
                Snackbar.make(mProgressBar, "创建成功，正在登陆~", Snackbar.LENGTH_SHORT).show();
                loginIn(localHashMap);
            }

            @Override
            public void onFailure(Call<PixivAccountsResponse> call, Throwable throwable) {
            }
        });
    }

    private void loginIn(HashMap localHashMap) {
        Call<PixivOAuthResponse> call = new RestClient().getretrofit_OAuthSecure().create(OAuthSecureService.class).postAuthToken(localHashMap);
        call.enqueue(new Callback<PixivOAuthResponse>() {
            @Override
            public void onResponse(Call<PixivOAuthResponse> call, retrofit2.Response<PixivOAuthResponse> response) {
                PixivOAuthResponse pixivOAuthResponse = response.body();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(NewUserActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String localStringBuilder = "Bearer " +
                        pixivOAuthResponse.getResponse().getAccess_token();
                editor.putString("Authorization", localStringBuilder);
                editor.putInt("userid", pixivOAuthResponse.getResponse().getUser().getId());
                editor.putBoolean("islogin", true);
                editor.putBoolean("ispremium", pixivOAuthResponse.getResponse().getUser().isIs_premium());
                editor.putString("username", pixivOAuthResponse.getResponse().getUser().getName());
                editor.putString("useraccount", pixivOAuthResponse.getResponse().getUser().getAccount());
                editor.putString("password", localHashMap.get("password").toString());
                editor.putString("hearurl", pixivOAuthResponse.getResponse().getUser().getProfile_image_urls().getPx_170x170());
                editor.putString("useremail", pixivOAuthResponse.getResponse().getUser().getMail_address());
                editor.putBoolean("r18on", false);
                editor.apply();
                mProgressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(NewUserActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<PixivOAuthResponse> call, Throwable throwable) {
            }
        });
    }
}
