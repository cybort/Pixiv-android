package com.example.administrator.essim.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private OkHttpClient okHttpClient;
    private Retrofit retrofit_AppAPI;
    private Retrofit retrofit_OAuthSecure;

    public OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor localHttpLoggingInterceptor = new HttpLoggingInterceptor(paramString -> {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("message====");
            localStringBuilder.append(paramString);
            Log.i("aaa", localStringBuilder.toString());
        });
        localHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.dns(new HttpDns());
        builder.addInterceptor(paramChain -> {
            Request localRequest = paramChain.request().newBuilder().addHeader("User-Agent", "PixivIOSApp/5.8.0").build();
            if (localRequest.header("Authorization") != null)
                Log.i("header", localRequest.header("Authorization"));
            return paramChain.proceed(localRequest);
        }).addInterceptor(localHttpLoggingInterceptor);
        okHttpClient = builder.build();
        return okHttpClient;
    }

    public Retrofit getretrofit_OAuthSecure() {
        Gson localGson = new GsonBuilder().create();
        retrofit_OAuthSecure = new Retrofit.Builder().baseUrl("https://oauth.secure.pixiv.net/")
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(localGson))
                .build();
        return retrofit_OAuthSecure;
    }

    public Retrofit getRetrofit_AppAPI() {
        Gson localGson = new GsonBuilder().create();
        retrofit_AppAPI = new Retrofit.Builder().baseUrl("https://app-api.pixiv.net/")
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(localGson))
                .build();
        return retrofit_AppAPI;
    }

    public Retrofit getRetrofit_Account() {
        Gson localGson = new GsonBuilder().create();
        retrofit_AppAPI = new Retrofit.Builder().baseUrl("https://accounts.pixiv.net/")
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(localGson))
                .build();
        return this.retrofit_AppAPI;
    }
}
