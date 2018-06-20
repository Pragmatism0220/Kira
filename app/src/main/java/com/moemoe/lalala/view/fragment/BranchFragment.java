package com.moemoe.lalala.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.BranchStoryAllEntity;
import com.moemoe.lalala.view.activity.BranchActivity;
import com.moemoe.lalala.view.activity.BranchInfoActivity;
import com.moemoe.lalala.view.adapter.BranchFragmentListAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/5/15.
 */

@SuppressLint("ValidFragment")
public class BranchFragment extends BaseFragment {

    @BindView(R.id.branch_recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.check_box_btn)
    CheckBox mCheckBox;
    @BindView(R.id.tv_branch)
    TextView mBranch;
    @BindView(R.id.branch_bottom)
    RelativeLayout mBranchBottom;

    private BranchFragmentListAdapter mAdapter;
    private ArrayList<BranchStoryAllEntity> mlists;
    private String branchN;
    private String branchR;
    private String branchSR;
    private boolean isSelect;
    private Context mContext;
    private int postion;
    private String key;

    @SuppressLint("ValidFragment")
    public BranchFragment(Context context, ArrayList<BranchStoryAllEntity> entities) {
        this.mlists = entities;
        this.mContext = context;
    }

    public static BranchFragment newInstance(Context context, ArrayList<BranchStoryAllEntity> entities) {
        return new BranchFragment(context, entities);
    }

    public void setBranch(String N, String R, String SR) {
        this.branchN = N;
        this.branchR = R;
        this.branchSR = SR;
    }

    public void setSelect(boolean isSelect, int position, String key) {
        this.isSelect = isSelect;
        this.postion = position;
        this.key = key;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.branch_fragment;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mAdapter = new BranchFragmentListAdapter(getContext(), isSelect);
        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecycleView.addItemDecoration(new SpacesItemDecoration(12, 12, 0));
        mRecycleView.setAdapter(mAdapter);
        if (isSelect) {
            mBranchBottom.setVisibility(View.GONE);
            branchOrCompound(mlists);
        } else {
            mBranchBottom.setVisibility(View.VISIBLE);
            branchOrCompound(mlists);
        }

        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isSelect) {
                    Intent intent = new Intent();
                    intent.putExtra("coverImage", mAdapter.getItem(position).getCoverImage());
                    intent.putExtra("roleId", mAdapter.getItem(position).getId());
                    intent.putExtra("position", postion);
                    ((BranchActivity) mContext).setResult(RESULT_OK, intent);
                    ((BranchActivity) mContext).finish();
                } else {
                    Intent intent = new Intent(getContext(), BranchInfoActivity.class);
                    intent.putExtra("entity", (Serializable) mAdapter.getItem(position));
                    intent.putExtra("id", mAdapter.getItem(position).getId());
                    intent.putExtra("isCompound", mAdapter.getItem(position).isShowJoinButton());
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void branchOrCompound(final ArrayList<BranchStoryAllEntity> entities) {
        ArrayList<BranchStoryAllEntity> objects = new ArrayList<>();
        objects.addAll((List) entities.clone());
        if (objects != null) {
            Iterator<BranchStoryAllEntity> iterator = objects.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getUserBranchLevelCount() == 0) {
                    iterator.remove();
                }
            }
            if (isSelect && key.equals("全部")) {
                if (objects == null || objects.size() == 0) {
                    Intent intent = new Intent();
                    intent.putExtra("isEntity", true);
                    ((BranchActivity) mContext).setResult(RESULT_OK, intent);
                    ((BranchActivity) mContext).finish();
                }
            }
            mAdapter.setList(objects);
        }

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCheckBox.isChecked()) {
                    mAdapter.setList(entities);
                } else {
                    ArrayList<BranchStoryAllEntity> objects1 = new ArrayList<>();
                    objects1.addAll(entities);
                    for (int i = 0; i < objects1.size(); i++) {
                        if (objects1.get(i).getUserBranchLevelCount() == 0) {
                            objects1.remove(objects1.get(i));
                            i--;
                        }
                    }
                    mAdapter.setList(objects1);
                }
            }
        });
        mBranch.setText("N(" + branchN + ")" +
                "R(" + branchR + ")" +
                "+SR(" + branchSR + ")");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
