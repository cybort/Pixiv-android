package com.example.administrator.essim.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response.TrendingtagResponse;
import com.example.administrator.essim.utils.GlideUtil;

import java.util.List;

public class TrendingtagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;
    private List<TrendingtagResponse.TrendTagsBean> mPixivRankItem;

    public TrendingtagAdapter(List<TrendingtagResponse.TrendTagsBean> item, Context context) {
        mPixivRankItem = item;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TagHolder(mLayoutInflater.inflate(R.layout.tag_item_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Glide.with(mContext).load(new GlideUtil().getMediumImageUrl(mPixivRankItem.get(position)
                .getIllust()))
                .into(((TagHolder) holder).mImageView);
        ((TagHolder) holder).mTextView.setText(mPixivRankItem.get(position).getTag());
        ((TagHolder) holder).itemView.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, position, 0));
    }

    private int getContentItemCount() {
        return mPixivRankItem.size();
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return getContentItemCount();
    }

    private static class TagHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;

        private TagHolder(final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.pixiv_image);
            mTextView = itemView.findViewById(R.id.pixiv_item_size);
        }
    }
}
