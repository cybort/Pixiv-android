package com.example.administrator.essim.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.OnItemClickListener;
import com.example.administrator.essim.response.SearchUserResponse;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.GlideUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class UserFollowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_TYPE_CONTENT = 1;
    private final int ITEM_TYPE_BOTTOM = 2;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private SharedPreferences mSharedPreferences;
    private List<SearchUserResponse.UserPreviewsBean> mBooksInfo;
    private OnItemClickListener mOnItemClickListener;

    public UserFollowAdapter(List<SearchUserResponse.UserPreviewsBean> booksInfo, Context context) {
        mBooksInfo = booksInfo;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CONTENT) {
            return new ContentViewHolder(mLayoutInflater.inflate(R.layout.user_follow_item, parent, false));
        } else {
            return new BottomViewHolder(mLayoutInflater.inflate(R.layout.bottom_refresh, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int p) {
        int position = holder.getAdapterPosition();
        if (holder instanceof ContentViewHolder) {
            ((ContentViewHolder) holder).mTextView.setText(mBooksInfo.get(position).getUser().getName());
            Glide.with(mContext).load(new GlideUtil().getHead(mBooksInfo.get(position)))
                    .into(((ContentViewHolder) holder).mCircleImageView);
            if (mBooksInfo.get(position).getIllusts().size() == 3) {
                Glide.with(mContext).load(new GlideUtil().getMediumImageUrl(mBooksInfo.get(position).getIllusts()
                        .get(0))).into(((ContentViewHolder) holder).mImageView);
                Glide.with(mContext).load(new GlideUtil().getMediumImageUrl(mBooksInfo.get(position).getIllusts()
                        .get(1))).into(((ContentViewHolder) holder).mImageView2);
                Glide.with(mContext).load(new GlideUtil().getMediumImageUrl(mBooksInfo.get(position).getIllusts()
                        .get(2))).into(((ContentViewHolder) holder).mImageView3);
            }

            if (mBooksInfo.get(position).getUser().isIs_followed()) {
                ((ContentViewHolder) holder).mButton.setText("取消关注");
            } else if (!mBooksInfo.get(position).getUser().isIs_followed()) {
                ((ContentViewHolder) holder).mButton.setText("+关注");
            }
            ((ContentViewHolder) holder).mButton.setOnClickListener(view -> {
                if (mBooksInfo.get(position).getUser().isIs_followed()) {
                    ((ContentViewHolder) holder).mButton.setText("+关注");
                    Common.postUnFollowUser(mSharedPreferences.getString("Authorization", ""),
                            mBooksInfo.get(position).getUser().getId(), ((ContentViewHolder) holder).mButton);
                    mBooksInfo.get(position).getUser().setIs_followed(false);
                } else {
                    ((ContentViewHolder) holder).mButton.setText("取消关注");
                    Common.postFollowUser(mSharedPreferences.getString("Authorization", ""),
                            mBooksInfo.get(position).getUser().getId(), ((ContentViewHolder) holder).mButton);
                    mBooksInfo.get(position).getUser().setIs_followed(true);
                }
            });
            ((ContentViewHolder) holder).itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder.itemView, position, 0));
        } else

        {
            ((BottomViewHolder) holder).mCardView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder.itemView, -1, 0));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= mBooksInfo.size()) {
            return ITEM_TYPE_BOTTOM;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public int getItemCount() {
        return mBooksInfo.size() + 1;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    private static class BottomViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;

        private BottomViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_footer);
        }
    }

    private static class ContentViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private Button mButton;
        private CircleImageView mCircleImageView;
        private ImageView mImageView, mImageView2, mImageView3;

        private ContentViewHolder(View itemView) {
            super(itemView);
            mButton = itemView.findViewById(R.id.post_like_user);
            mCircleImageView = itemView.findViewById(R.id.user_head);
            mTextView = itemView.findViewById(R.id.user_name);
            mImageView = itemView.findViewById(R.id.user_show_one);
            mImageView2 = itemView.findViewById(R.id.user_show_two);
            mImageView3 = itemView.findViewById(R.id.user_show_three);
        }
    }
}
