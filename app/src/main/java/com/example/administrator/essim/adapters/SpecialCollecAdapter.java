package com.example.administrator.essim.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response.SpecialCollectionResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.GlideUtil;

public class SpecialCollecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int ITEM_TYPE_BOTTOM = 2;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;
    private SpecialCollectionResponse mPixivRankItem;

    public SpecialCollecAdapter(SpecialCollectionResponse pixivResponse, Context context) {
        mContext = context;
        mPixivRankItem = pixivResponse;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CONTENT) {
            return new TagHolder(mLayoutInflater.inflate(R.layout.recy_special_collec, parent, false));
        } else {
            return new BottomViewHolder(mLayoutInflater.inflate(R.layout.bottom_refresh_widely, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TagHolder) {
            Glide.with(mContext).load(new GlideUtil().getHead(mPixivRankItem.body.get(position).thumbnailUrl)).asBitmap()
                    .into(((TagHolder) holder).mImageView);
            ((TagHolder) holder).mTextView.setText(mPixivRankItem.body.get(position).title);
            ((TagHolder) holder).mTextView2.setText(Common.getTime(mPixivRankItem.body.get(position).publishDate, 0));
            ((TagHolder) holder).itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position, 0));
        } else {
            ((BottomViewHolder) holder).mCardView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder.itemView, -1, 0));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= mPixivRankItem.body.size()) {
            return ITEM_TYPE_BOTTOM;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mPixivRankItem.body.size() + 1;
    }

    private static class BottomViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;

        private BottomViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_footer);
        }
    }

    private class TagHolder extends RecyclerView.ViewHolder {
        private TextView mTextView, mTextView2;
        private ImageView mImageView;
        private ViewGroup.LayoutParams params;

        private TagHolder(final View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text_title);
            mTextView2 = itemView.findViewById(R.id.text_date);
            mImageView = itemView.findViewById(R.id.imageView);
            params = mImageView.getLayoutParams();
            params.height = (mContext.getResources().getDisplayMetrics().widthPixels - mContext.getResources().getDimensionPixelSize(R.dimen.thirty_two_dp)) / 3 * 2;
            mImageView.setLayoutParams(params);
        }
    }
}
