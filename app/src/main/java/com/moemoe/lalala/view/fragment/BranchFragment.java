package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.view.adapter.BranchFragmentListAdapter;
import com.moemoe.lalala.view.base.BranchItemBean;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/15.
 */

public class BranchFragment extends BaseFragment {

    @BindView(R.id.branch_recycle_view)
    RecyclerView mRecycleView;

    private BranchFragmentListAdapter mAdapter;
    private List<BranchItemBean> mlists;

    @Override
    protected int getLayoutId() {
        return R.layout.branch_fragment;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        getData();
        mAdapter = new BranchFragmentListAdapter(mlists, getContext());
        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        int rightSpace = 22;
        int buttonSpace = 24;
        int top = 32;
        mRecycleView.addItemDecoration(new SpacesItemDecoration(buttonSpace, rightSpace, top));
        mRecycleView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(getContext(), BranchInfoActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void getData() {
        mlists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mlists.add(new BranchItemBean());
        }
    }


}
