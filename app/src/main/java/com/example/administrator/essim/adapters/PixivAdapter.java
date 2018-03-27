package com.example.administrator.essim.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.models.PixivRankItem;

/**
 * Created by Administrator on 2018/3/23 0023.
 */
public class PixivAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int ITEM_TYPE_BOTTOM = 2;
    private PixivRankItem mPixivRankItem;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mBottomCount = 1;

    public PixivAdapter(PixivRankItem item, Context context) {
        mPixivRankItem = item;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView, mTextView2, mTextView3, mTextView4;

        public PhotoHolder(final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.pixiv_image);
            mTextView = itemView.findViewById(R.id.pixiv_title);
            mTextView2 = itemView.findViewById(R.id.pixiv_author);
            mTextView3 = itemView.findViewById(R.id.viewed);
            mTextView4 = itemView.findViewById(R.id.liked);
        }

        public void refreshLayout(int position) {
            Glide.with(mContext).load(mPixivRankItem.response.get(0).works.get(position).work.image_urls.getPx_480mw())
                    .into(mImageView);
            mTextView.setText(mPixivRankItem.response.get(0).works.get(position).work.getTitle());
            mTextView2.setText(mContext.getString(R.string.string_author,
                    mPixivRankItem.response.get(0).works.get(position).work.user.getName()));
            mTextView3.setText(mContext.getString(R.string.string_viewd,
                    mPixivRankItem.response.get(0).works.get(position).work.stats.getViews_count().substring(0, mPixivRankItem.response.get(0).works.get(position)
                            .work.stats.getViews_count().length() - 3)));
            if (mPixivRankItem.response.get(0).works.get(position).work.stats.getScored_count().length() <= 3) {
                mTextView4.setText(mPixivRankItem.response.get(0).works
                        .get(position).work.stats.getScored_count());
            } else {
                mTextView4.setText(mContext.getString(R.string.string_viewd,
                        mPixivRankItem.response.get(0).works.get(position)
                                .work.stats.getScored_count().substring(0, mPixivRankItem.response.get(0).works.get(position)
                                .work.stats.getScored_count().length() - 3)));
            }
        }
    }

    public class BottomViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;

        public BottomViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_footer);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE_BOTTOM) {
            return new BottomViewHolder(mLayoutInflater.inflate(R.layout.recy_bottom, parent, false));
        } else {
            return new PhotoHolder(mLayoutInflater.inflate(R.layout.pixiv_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof PhotoHolder) {
            ((PhotoHolder) holder).refreshLayout(position);
            if (mOnItemClickListener != null) {
                ((PhotoHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
        } else {
            ((BottomViewHolder) holder).mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();
        if (mBottomCount != 0 && position >= dataItemCount) {
            return ITEM_TYPE_BOTTOM;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    public int getContentItemCount() {
        return mPixivRankItem.response.get(0).works.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return getContentItemCount() + mBottomCount;
    }
}
