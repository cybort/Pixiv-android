package com.example.administrator.essim.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.essim.R;
import com.example.administrator.essim.models.HotTag;

import java.util.List;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class HotTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static String search_text;
    //模拟数据

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<HotTag> mbooksInfo;
    private int mHeaderCount = 1;//头部View个数

    public HotTagAdapter(List<HotTag> booksInfo, Context context) {
        mbooksInfo = booksInfo;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView1;
        private CardView cardView2;
        private CardView cardView3;
        private TextView textView1;
        private TextView textView2;
        private TextView textView3;

        public ContentViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.tag_one);
            textView2 = (TextView) itemView.findViewById(R.id.tag_two);
            textView3 = (TextView) itemView.findViewById(R.id.tag_three);
            cardView1 = (CardView) itemView.findViewById(R.id.card_1);
            cardView2 = (CardView) itemView.findViewById(R.id.card_2);
            cardView3 = (CardView) itemView.findViewById(R.id.card_3);
        }
    }

    //头部 ViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public EditText editText;
        private ImageButton imageButton;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            editText = (EditText) itemView.findViewById(R.id.input_tag);
            imageButton = (ImageButton) itemView.findViewById(R.id.do_search);
        }
    }

    public int getContentItemCount() {
        return mbooksInfo.size() / 3;
    }

    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();
        if (mHeaderCount != 0 && position < mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE_HEADER) {
            return new HeaderViewHolder(mLayoutInflater.inflate(R.layout.head_search_view, parent, false));
        } else {
            return new ContentViewHolder(mLayoutInflater.inflate(R.layout.tag_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search_text = ((HeaderViewHolder) holder).editText.getText().toString().trim();
                    if (!search_text.isEmpty()) {
                        mOnItemClickLitener.onSearch(((HeaderViewHolder) holder).imageButton,
                                ((HeaderViewHolder) holder).editText.getText().toString().trim(), -1);
                    } else {
                        Toast.makeText(mContext, "请输入搜索的内容！", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else if (holder instanceof ContentViewHolder) {
            ((ContentViewHolder) holder).textView1.setText(mbooksInfo.get(position * 3 - 3).getName());
            ((ContentViewHolder) holder).textView2.setText(mbooksInfo.get(position * 3 - 2).getName());
            ((ContentViewHolder) holder).textView3.setText(mbooksInfo.get(position * 3 - 1).getName());
            if (mOnItemClickLitener != null) {
                final int pos = holder.getLayoutPosition();
                ((ContentViewHolder) holder).cardView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(((ContentViewHolder) holder).cardView1, pos * 3 - 3);
                    }
                });
                ((ContentViewHolder) holder).cardView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(((ContentViewHolder) holder).cardView2, pos * 3 - 2);
                    }
                });
                ((ContentViewHolder) holder).cardView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(((ContentViewHolder) holder).cardView3, pos * 3 - 1);
                    }
                });
            }
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onSearch(View view, String searchKey, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public int getItemCount() {
        return mHeaderCount + getContentItemCount();
    }

}
