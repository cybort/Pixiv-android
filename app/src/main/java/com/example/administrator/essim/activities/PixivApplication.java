package com.example.administrator.essim.activities;

import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.litepal.LitePalApplication;

public class PixivApplication extends LitePalApplication{

    @Override
    public void onCreate() {
        super.onCreate();
    }

   /* public static RefWatcher getRefWatcher(Context context) {
        PixivApplication application = (PixivApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;*/
}
