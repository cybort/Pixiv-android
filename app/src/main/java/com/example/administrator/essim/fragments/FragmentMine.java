package com.example.administrator.essim.fragments;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.MainActivity;
import com.example.administrator.essim.adapters.ListHitokotoAdapter;
import com.example.administrator.essim.models.HitoModel;
import com.sdsmdg.tastytoast.TastyToast;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentMine extends BaseFragment {

    private Toolbar mToolbar;
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private List<HitoModel> mHitoModels;
    private ListHitokotoAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar = view.findViewById(R.id.toolbar_mine);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(v -> MainActivity.sDrawerLayout.openDrawer(Gravity.START, true));
        mTextView = view.findViewById(R.id.toolbar_title_three);
        mTextView.setOnClickListener(v -> MainActivity.sDrawerLayout.openDrawer(Gravity.START, true));
        mRecyclerView = view.findViewById(R.id.mine_recy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //读取数据库
        mHitoModels = DataSupport.findAll(HitoModel.class);
        mAdapter = new ListHitokotoAdapter(mHitoModels, mContext);
        mAdapter.setOnItemClickListener((view1, position, code) -> {
            if (code == 1) {
                createDialog(position, "这一条", 0);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setEditableMode() {
        ListHitokotoAdapter.is_editable = !ListHitokotoAdapter.is_editable;
        mAdapter.notifyDataSetChanged();
    }

    private void createDialog(final int index, String title, final int deleteType) {
        getDialog(title).setPositiveButton("确定", (dialog, which) -> {
            if (deleteType == 0) {
                DataSupport.deleteAll(HitoModel.class, "hitokoto = ?", mHitoModels.get(index).getHitokoto());
                mHitoModels.remove(index);
                mAdapter.notifyItemRemoved(index);
            } else if (deleteType == 1) {
                for (int i = 0; i < mHitoModels.size(); i++) {
                    if (mHitoModels.get(i).getSelected()) {
                        DataSupport.deleteAll(HitoModel.class, "hitokoto = ?", mHitoModels.get(i).getHitokoto());
                    }
                }
                reFreshLocalData();
                setEditableMode();
            } else if (deleteType == 2) {
                DataSupport.deleteAll(HitoModel.class);
                mHitoModels.clear();
                mAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("取消", (dialog, which) -> {}).create().show();
    }

    private void reFreshLocalData() {
        mHitoModels.clear();
        mHitoModels.addAll(DataSupport.findAll(HitoModel.class));
    }

    @Override
    public void onHiddenChanged(boolean hide) {
        if (!hide) {
            if (FragmentHitikoto.need_to_refresh) {
                reFreshLocalData();
                mRecyclerView.smoothScrollToPosition(mHitoModels.size());
                FragmentHitikoto.need_to_refresh = false;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_mine, menu);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setHasOptionsMenu(true);
    }

    private AlertDialog.Builder getDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("来自九条的提示");
        builder.setMessage("真的要删除" + title + "记录吗？");
        return builder;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == R.id.action_edit_mode) {
            setEditableMode();
        } else if (item.getItemId() == R.id.action_delete) {
            if (ListHitokotoAdapter.is_editable) {
                createDialog(0, "选中的", 1);
            } else {
                TastyToast.makeText(mContext, "请先进入<编辑>模式", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
            }
        } else if (item.getItemId() == R.id.delete_all) {
            if (mHitoModels.size() != 0) {
                createDialog(0, "所有", 2);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}