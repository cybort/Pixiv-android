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
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ImageDetailActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.anotherproj.CloudMainActivity;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.utils.DownloadTask;
import com.example.administrator.essim.utils.GlideUtil;
import jp.wasabeef.glide.transformations.BlurTransformation;
import me.gujun.android.taggroup.TagGroup;


/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class FragmentPixivItem extends BaseFragment {

    private int index;
    private DownloadTask asyncTask;
    private IllustsBean mIllustsBeans;
    private File parentFile, realFile;
    private ImageView mImageView, mImageView2;
    private CardView mCardView, mCardView2, mCardView3, mCardView4;
    private TextView mTextView, mTextView2, mTextView3, mTextView4, mTextView5, mTextView6, mTextView7, mTextView8;

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
        index = (int) getArguments().getSerializable("index");
        mIllustsBeans = (IllustsBean) getArguments().getSerializable("illust item");
        if (index == ((ViewPagerActivity) getActivity()).getIndexNow()) {
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
        mImageView = view.findViewById(R.id.item_background_img);
        mImageView2 = view.findViewById(R.id.detail_img);
        ViewGroup.LayoutParams params = mImageView2.getLayoutParams();
        params.height = (((getResources().getDisplayMetrics().widthPixels - getResources().getDimensionPixelSize(R.dimen.thirty_two_dp)) *
                mIllustsBeans.getHeight()) / mIllustsBeans.getWidth());
        mImageView2.setLayoutParams(params);
        mImageView2.setOnClickListener(view12 -> {
            Intent intent = new Intent(mContext, ImageDetailActivity.class);
            intent.putExtra("illust", mIllustsBeans);
            mContext.startActivity(intent);
        });
        Glide.get(mContext).clearMemory();
        Glide.with(getContext()).load(new GlideUtil().getMediumImageUrl(mIllustsBeans))
                .bitmapTransform(new BlurTransformation(mContext, 20, 2))
                .into(mImageView);
        Glide.with(getContext()).load(new GlideUtil().getMediumImageUrl(mIllustsBeans))
                .into(mImageView2);
        mTextView = view.findViewById(R.id.detail_author);
        mTextView2 = view.findViewById(R.id.detail_img_size);
        mTextView3 = view.findViewById(R.id.detail_create_time);
        mTextView4 = view.findViewById(R.id.viewed);
        mTextView5 = view.findViewById(R.id.liked);
        mTextView6 = view.findViewById(R.id.illust_id);
        mTextView7 = view.findViewById(R.id.author_id);
        mTextView8 = view.findViewById(R.id.all_item_size);
        mCardView = view.findViewById(R.id.card_first);
        mCardView2 = view.findViewById(R.id.card_second);
        mCardView3 = view.findViewById(R.id.card_left);
        mCardView3.setOnClickListener(v -> {
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
        mCardView4 = view.findViewById(R.id.card_right);
        mCardView4.setOnClickListener(view1 -> {
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
            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", tag);
            cm.setPrimaryClip(mClipData);
            TastyToast.makeText(mContext, tag + " 已复制到剪切板~", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        });
        mTextView.setText(getString(R.string.string_author,
                mIllustsBeans.getUser().getName()));
        mTextView2.setText(getString(R.string.string_full_size, mIllustsBeans.getWidth(), mIllustsBeans.getHeight()));
        mTextView3.setText(getString(R.string.string_create_time, mIllustsBeans.getCreate_date()));
        if (mIllustsBeans.getTotal_view() >= 1000) {
            mTextView4.setText(getString(R.string.string_viewd,
                    String.valueOf(mIllustsBeans.getTotal_view() / 1000)));
        } else {
            mTextView4.setText(String.valueOf(mIllustsBeans.getTotal_view()));
        }
        if (mIllustsBeans.getTotal_bookmarks() >= 1000) {
            mTextView5.setText(getString(R.string.string_viewd,
                    String.valueOf(mIllustsBeans.getTotal_bookmarks() / 1000)));

        } else {
            mTextView5.setText(String.valueOf(mIllustsBeans.getTotal_bookmarks()));
        }
        mTextView6.setText(getString(R.string.illust_id, String.valueOf(mIllustsBeans.getId())));
        mTextView7.setText(getString(R.string.author_id, String.valueOf(mIllustsBeans.getUser().getId())));
        if (mIllustsBeans.getPage_count() > 1) {
            mTextView8.setText(String.format("%sP", String.valueOf(mIllustsBeans.getPage_count())));
        } else {
            mTextView8.setVisibility(View.INVISIBLE);
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