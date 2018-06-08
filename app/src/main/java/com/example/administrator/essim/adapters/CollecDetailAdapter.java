package com.example.administrator.essim.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response.CollectionResponse;
import com.example.administrator.essim.utils.GlideUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class CollecDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;
    private CollectionResponse mPixivRankItem;

    public CollecDetailAdapter(CollectionResponse pixivResponse, Context context) {
        mContext = context;
        mPixivRankItem = pixivResponse;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagHolder(mLayoutInflater.inflate(R.layout.pixiv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Glide.with(mContext).load(new GlideUtil().getMediumImageUrl(mPixivRankItem.body.get(0).illusts.get(position).illust_id,
                mPixivRankItem.body.get(0).illusts.get(position).url.get_$1200x1200())).into(((TagHolder) holder).mImageView);
        Glide.with(mContext).load(new GlideUtil().getHead(Long.parseLong(mPixivRankItem.body.get(0).illusts.get(position).illust_user_id),
                mPixivRankItem.body.get(0).illusts.get(position).user_icon)).into(((TagHolder) holder).mImageView2);
        ((TagHolder) holder).mTextView.setText(mPixivRankItem.body.get(0).illusts.get(position).illust_title);
        ((TagHolder) holder).mTextView2.setText(mPixivRankItem.body.get(0).illusts.get(position).user_name);
        ((TagHolder) holder).itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position, 0));
        ((TagHolder) holder).mImageView2.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position, 1));
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mPixivRankItem.body.get(0).illusts.size();
    }

    private class TagHolder extends RecyclerView.ViewHolder {
        private TextView mTextView, mTextView2;
        private ImageView mImageView;
        private CircleImageView mImageView2;
        private ViewGroup.LayoutParams params;

        private TagHolder(final View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.pixiv_title);
            mTextView2 = itemView.findViewById(R.id.pixiv_author);
            mImageView = itemView.findViewById(R.id.pixiv_image);
            mImageView2 = itemView.findViewById(R.id.pixiv_head);
            params = mImageView.getLayoutParams();
            params.height = mContext.getResources().getDisplayMetrics().widthPixels - mContext.getResources().getDimensionPixelSize(R.dimen.fourty_eight_dip);
            mImageView.setLayoutParams(params);
        }
    }
}
