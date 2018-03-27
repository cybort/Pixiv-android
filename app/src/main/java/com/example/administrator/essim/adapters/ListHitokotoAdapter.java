package com.example.administrator.essim.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.models.HitoModel;
import com.example.administrator.essim.models.HitokotoType;

import java.util.List;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class ListHitokotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<HitoModel> mBooksInfo;
    private int data_size;
    private AlphaAnimation alphaAnimationShowIcon;
    public static boolean is_editable = false;

    public ListHitokotoAdapter(List<HitoModel> booksInfo, Context context) {
        mBooksInfo = booksInfo;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        alphaAnimationShowIcon = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimationShowIcon.setDuration(500);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mTextView, mTextView2, mTextView3;
        ImageView mImageView;
        CheckBox mCheckBox;

        private ContentViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_view_item_recycler_view);
            mTextView = itemView.findViewById(R.id.tv_recycler_item_3);
            mTextView2 = itemView.findViewById(R.id.local_number);
            mTextView3 = itemView.findViewById(R.id.tv_recycler_item_1);
            mImageView = itemView.findViewById(R.id.delete_item);
            mCheckBox = itemView.findViewById(R.id.select_item);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.recy_local_hiko_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (!is_editable) {
            ((ContentViewHolder) holder).mCheckBox.setVisibility(View.GONE);
            ((ContentViewHolder) holder).mImageView.setVisibility(View.VISIBLE);
        } else {
            ((ContentViewHolder) holder).mImageView.setVisibility(View.GONE);
            ((ContentViewHolder) holder).mCheckBox.setVisibility(View.VISIBLE);
        }
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_recycler_item_show);
        ((ContentViewHolder) holder).itemView.startAnimation(animation);
        ((ContentViewHolder) holder).mTextView.setText(mBooksInfo.get(position).getHitokoto());
        ((ContentViewHolder) holder).mTextView2.setText(String.valueOf(position + 1));
        ((ContentViewHolder) holder).mTextView3.setText(HitokotoType.getType(mBooksInfo.get(position).getType()));
        ((ContentViewHolder) holder).mCheckBox.setChecked(mBooksInfo.get(position).getSelected());

        ((ContentViewHolder) holder).mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position, 1);
            }
        });

        ((ContentViewHolder) holder).mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_editable) {
                    if (!((ContentViewHolder) holder).mCheckBox.isChecked()) {
                        mBooksInfo.get(position).setSelected(true);
                        ((ContentViewHolder) holder).mCheckBox.setChecked(true);
                    } else {
                        mBooksInfo.get(position).setSelected(false);
                        ((ContentViewHolder) holder).mCheckBox.setChecked(false);
                    }
                } else {
                    mOnItemClickListener.onItemClick(v, position, 0);
                }
            }
        });

        ((ContentViewHolder) holder).mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ContentViewHolder) holder).mCheckBox.isChecked()) {
                    mBooksInfo.get(position).setSelected(true);
                } else {
                    mBooksInfo.get(position).setSelected(false);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemClickListener.onItemLongClick(holder.itemView, position);
                return false;
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int code);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return data_size = mBooksInfo.size();
    }

}
