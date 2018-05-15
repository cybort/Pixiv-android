package com.example.administrator.essim.fragments;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ImageDetailActivity;
import com.example.administrator.essim.activities.SearchTagActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.anotherproj.CloudMainActivity;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.utils.DownloadTask;
import com.example.administrator.essim.utils.GlideUtil;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.gujun.android.taggroup.TagGroup;


/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class FragmentPixivItem extends BaseFragment {

    private DownloadTask asyncTask;
    private IllustsBean mIllustsBeans;
    private File parentFile, realFile;

    public static FragmentPixivItem newInstance(IllustsBean illustsBean, int index) {
        Bundle args = new Bundle();
        args.putSerializable("index", index);
        args.putSerializable("illust item", illustsBean);
        FragmentPixivItem fragment = new FragmentPixivItem();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pixiv_item, container, false);
        assert getArguments() != null;
        int index = (int) getArguments().getSerializable("index");
        mIllustsBeans = (IllustsBean) getArguments().getSerializable("illust item");
        if (index == ((ViewPagerActivity) Objects.requireNonNull(getActivity())).getIndexNow()) {
            setUserVisibleHint(true);
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        reFreshLayout(view);
        return view;
    }

    private void reFreshLayout(View view) {
        ImageView imageView = view.findViewById(R.id.item_background_img);
        ImageView imageView2 = view.findViewById(R.id.detail_img);
        ViewGroup.LayoutParams params = imageView2.getLayoutParams();
        params.height = (((getResources().getDisplayMetrics().widthPixels - getResources().getDimensionPixelSize(R.dimen.thirty_two_dp)) *
                mIllustsBeans.getHeight()) / mIllustsBeans.getWidth());
        imageView2.setLayoutParams(params);
        imageView2.setOnClickListener(view12 -> {
            Intent intent = new Intent(mContext, ImageDetailActivity.class);
            intent.putExtra("illust", mIllustsBeans);
            mContext.startActivity(intent);
        });
        Glide.get(mContext).clearMemory();
        Glide.with(getContext()).load(new GlideUtil().getMediumImageUrl(mIllustsBeans))
                .bitmapTransform(new BlurTransformation(mContext, 20, 2))
                .into(imageView);
        Glide.with(getContext()).load(new GlideUtil().getMediumImageUrl(mIllustsBeans))
                .into(imageView2);
        TextView textView = view.findViewById(R.id.detail_author);
        TextView textView2 = view.findViewById(R.id.detail_img_size);
        TextView textView3 = view.findViewById(R.id.detail_create_time);
        TextView textView4 = view.findViewById(R.id.viewed);
        TextView textView5 = view.findViewById(R.id.liked);
        TextView textView6 = view.findViewById(R.id.illust_id);
        TextView textView7 = view.findViewById(R.id.author_id);
        TextView textView8 = view.findViewById(R.id.all_item_size);
        CardView cardView = view.findViewById(R.id.card_first);
        CardView cardView2 = view.findViewById(R.id.card_second);
        CardView cardView3 = view.findViewById(R.id.card_left);
        cardView3.setOnClickListener(v -> {
            parentFile = new File(Environment.getExternalStorageDirectory().getPath(), "PixivPictures");
            if (!parentFile.exists()) {
                parentFile.mkdir();
                mActivity.runOnUiThread(() -> TastyToast.makeText(mContext, "文件夹创建成功~",
                        TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show());
            }
            realFile = new File(parentFile.getPath(), mIllustsBeans.getTitle() + "_" +
                    mIllustsBeans.getId() + "_" +
                    String.valueOf(0) + ".jpeg");
            if (realFile.exists()) {
                mActivity.runOnUiThread(() -> TastyToast.makeText(mContext, "该文件已存在~",
                        TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show());
            } else {
                asyncTask = new DownloadTask(realFile, mContext, mIllustsBeans);
                if (mIllustsBeans.getPage_count() > 1) {
                    asyncTask.execute(mIllustsBeans.getMeta_pages().get(0).getImage_urlsX().getOriginal());
                } else {
                    asyncTask.execute(mIllustsBeans.getMeta_single_page().getOriginal_image_url());
                }
                asyncTask = null;
            }
        });
        CardView cardView4 = view.findViewById(R.id.card_right);
        cardView4.setOnClickListener(view1 -> {
            Intent intent = new Intent(mContext, CloudMainActivity.class);
            intent.putExtra("user id", mIllustsBeans.getUser().getId());
            mContext.startActivity(intent);
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        TagGroup mTagGroup = view.findViewById(R.id.tag_group);
        List<String> tagTemp = new ArrayList<>();
        for (int i = 0; i < mIllustsBeans.getTags().size(); i++) {
            tagTemp.add(mIllustsBeans.getTags().get(i).getName());
        }
        mTagGroup.setTags(tagTemp);
        mTagGroup.setOnTagClickListener(tag -> {
            Intent intent = new Intent(mContext, SearchTagActivity.class);
            intent.putExtra("what is the keyword", tag);
            mContext.startActivity(intent);
        });
        textView.setText(getString(R.string.string_author,
                mIllustsBeans.getUser().getName()));
        textView2.setText(getString(R.string.string_full_size, mIllustsBeans.getWidth(), mIllustsBeans.getHeight()));
        textView3.setText(getString(R.string.string_create_time, mIllustsBeans.getCreate_date().substring(0,
                mIllustsBeans.getCreate_date().length()-9)));
        textView4.setText(mIllustsBeans.getTotal_view() >= 1000 ? getString(R.string.string_viewd,
                String.valueOf(mIllustsBeans.getTotal_view() / 1000)) : String.valueOf(mIllustsBeans.getTotal_view()));
        textView5.setText(mIllustsBeans.getTotal_bookmarks() >= 1000 ? getString(R.string.string_viewd,
                String.valueOf(mIllustsBeans.getTotal_bookmarks() / 1000)) : String.valueOf(mIllustsBeans.getTotal_bookmarks()));
        textView6.setText(getString(R.string.illust_id, String.valueOf(mIllustsBeans.getId())));
        textView7.setText(getString(R.string.author_id, String.valueOf(mIllustsBeans.getUser().getId())));
        if (mIllustsBeans.getPage_count() > 1) {
            textView8.setText(String.format("%sP", String.valueOf(mIllustsBeans.getPage_count())));
        } else {
            textView8.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null) {
                ((ViewPagerActivity) getActivity()).changeTitle();
            }
        }
    }
}