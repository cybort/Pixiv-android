package com.example.administrator.essim.utils;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.PixivApplication;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.BookmarkAddResponse;
import com.example.administrator.essim.response.IllustsBean;
import com.github.ybq.android.spinkit.style.Wave;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Administrator on 2017/10/28 0028.
 */

/*
//       quu..__
//        $$$b  `---.__
//         "$$b        `--.                          ___.---uuudP
//          `$$b           `.__.------.__     __.---'      $$$$"              .
//            "$b          -'            `-.-'            $$$"              .'|
//              ".                                       d$"             _.'  |
//                `.   /                              ..."             .'     |
//                  `./                           ..::-'            _.'       |
//                   /                         .:::-'            .-'         .'
//                  :                          ::''\          _.'            |
//                 .' .-.             .-.           `.      .'               |
//                 : /'$$|           .@"$\           `.   .'              _.-'
//                .'|$u$$|          |$$,$$|           |  <            _.-'
//                | `:$$:'          :$$$$$:           `.  `.       .-'
//                :                  `"--'             |    `-.     \
//               :##.       ==             .###.       `.      `.    `\
//               |##:                      :###:        |        >     >
//               |#'     `..'`..'          `###'        x:      /     /
//                \                                   xXX|     /    ./
//                 \                                xXXX'|    /   ./
//                 /`-.                                  `.  /   /
//                :    `-  ...........,                   | /  .'
//                |         ``:::::::'       .            |<    `.
//                |             ```          |           x| \ `.:``.
//                |                         .'    /'   xXX|  `:`M`M':.
//                |    |                    ;    /:' xXXX'|  -'MMMMM:'
//                `.  .'                   :    /:'       |-'MMMM.-'
//                 |  |                   .'   /'        .'MMM.-'
//                 `'`'                   :  ,'          |MMM<
//                   |                     `'            |tbap\
//                    \                                  :MM.-'
//                     \                 |              .''
//                      \.               `.            /
//                       /     .:::::::.. :           /
//                      |     .:::::::::::`.         /
//                      |   .:::------------\       /
//                     /   .''               >::'  /
//                     `',:                 :    .'
//                                          `:.:'
*/


public class Common {

    public static AlphaAnimation getAnimation() {
        AlphaAnimation alphaAnimationShowIcon = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimationShowIcon.setDuration(500);
        return alphaAnimationShowIcon;
    }

    public static void sendOkhttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void postStarIllust(int position, List<IllustsBean> illustsBeans, String token, Context context, String starType) {
        List<String> illustTag = new ArrayList();
        Iterator localIterator = illustsBeans.get(position).getTags().iterator();
        while (localIterator.hasNext()) {
            illustTag.add(((IllustsBean.TagsBean) localIterator.next()).getName());
        }
        Call<BookmarkAddResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .postLikeIllust(token, illustsBeans.get(position).getId(), starType, illustTag);
        call.enqueue(new Callback<BookmarkAddResponse>() {
            @Override
            public void onResponse(Call<BookmarkAddResponse> call, retrofit2.Response<BookmarkAddResponse> response) {
                illustsBeans.get(position).setIs_bookmarked(true);
                if (starType.equals("private")) {
                    TastyToast.makeText(context, "成功添加到非公开收藏~",
                            TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                } else {
                    TastyToast.makeText(context, "成功添加到公开收藏~",
                            TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                }
            }

            @Override
            public void onFailure(Call<BookmarkAddResponse> call, Throwable throwable) {
            }
        });
    }

    public static void postUnstarIllust(int position, List<IllustsBean> illustsBeans, String token, Context context) {
        Call<ResponseBody> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .postUnlikeIllust(token, illustsBeans.get(position).getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                illustsBeans.get(position).setIs_bookmarked(false);
                TastyToast.makeText(context, "取消收藏~",
                        TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    public static void postFollowUser(String auth, int userID, View view, String followType) {
        Call<BookmarkAddResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .postFollowUser(auth, userID, followType);
        call.enqueue(new Callback<BookmarkAddResponse>() {
            @Override
            public void onResponse(Call<BookmarkAddResponse> call, retrofit2.Response<BookmarkAddResponse> response) {
                if (followType.equals("public")) {
                    Snackbar.make(view, "关注成功~(公开的)", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, "关注成功~(非公开的)", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookmarkAddResponse> call, Throwable throwable) {
            }
        });
    }

    public static void postUnFollowUser(String auth, int userID, View view) {
        Call<ResponseBody> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .postUnfollowUser(auth, userID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Snackbar.make(view, "取消关注~", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
            }
        });
    }

    //接收时间戳，格式化时间并返回
    public static String getTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        if (time.length() == 13) {
            return sdf.format(Long.parseLong(time));
        }
        if (time.length() == 10) {
            return sdf.format(new Date(Integer.parseInt(time) * 1000L));
        }
        return "没有日期数据哦";
    }

    public static String getTime(String time, int timeType) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        if (time.length() == 13) {
            return sdf.format(Long.parseLong(time));
        }
        if (time.length() == 10) {
            return sdf.format(new Date(Integer.parseInt(time) * 1000L));
        }
        return "没有数据";
    }

    //得到当前时间回退两天的日期
    public static String getLastDay() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -2);
        Date today = now.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(today);
    }

    //快速打LOG
    public static <T> void showLog(T t) {
        Log.d("a line of my log", String.valueOf(t));
    }

    //返回分页的页数
    public static int getPageCount(String itemCount) {
        if (Integer.valueOf(itemCount) < 20) {
            return 1;
        } else if ((Integer.valueOf(itemCount) / 20 < 20) && (Integer.valueOf(itemCount) / 20 >= 1)) {
            return Integer.valueOf(itemCount) / 20;
        } else {
            return 20;
        }
    }

    public static void clearLocalData(Context context) {
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    //通知相册更新图片
    public static void sendBroadcast(Context context, File file) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));
    }

    public static void sendBroadcast(Context context, Uri uri) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }

    public static void copyMessage(Context mContext, String tag) {
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", tag);
        assert cm != null;
        cm.setPrimaryClip(mClipData);
        TastyToast.makeText(mContext, tag + " 已复制到剪切板~", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
    }

    public static SharedPreferences getLocalDataSet() {
        return PreferenceManager.getDefaultSharedPreferences(PixivApplication.getContext());
    }

    public static Wave getLoaderAnimation(Context context) {
        Wave wave = new Wave();
        wave.setColor(context.getResources().getColor(R.color.colorPrimary));
        return wave;
    }

    public static void saveBitmap(Context context, File realFile, Bitmap mBitmap, IllustsBean illustsBean, int index) {
        try {
            FileOutputStream fos = new FileOutputStream(realFile);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Common.sendBroadcast(context, realFile);
            TastyToast.makeText(context, "下载完成~", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File generatePictureFile(Context context, IllustsBean illustsBean, int positionInIllust) {
        //检验父文件夹是否存在在，若不存在则创建
        File parentFile = new File(Common.getLocalDataSet().getString("download_path",
                "/storage/emulated/0/PixivPictures"));
        if (!parentFile.exists()) {
            parentFile.mkdir();
            TastyToast.makeText(context, "文件夹创建成功~",
                    TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        }
        //File file = new File(路径， 文件名); 此格式生成具体的文件，File其他构造方法会生成文件夹无法写入
        //文件命名方式： illust id + "_" + 第几p + ".jpeg"
        return new File(parentFile.getPath(), illustsBean.getId() + "_" + String.valueOf(positionInIllust) + ".jpeg");
    }
}
