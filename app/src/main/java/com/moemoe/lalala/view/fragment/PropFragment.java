package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.moemoe.lalala.R;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.view.adapter.PropAdapter;
import com.moemoe.lalala.view.base.PropInfo;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by zhangyan on 2018/5/21.
 * 储物箱中道具Fragment
 */

public class PropFragment extends BaseFragment {


    @BindView(R.id.storage_recycle_view)
    RecyclerView mRecycleView;

    private PropAdapter mAdapter;
    private List<PropInfo> infos;

    @Override
    protected int getLayoutId() {
        return R.layout.prop_fragment;

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        infos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            infos.add(new PropInfo());
        }
        mAdapter = new PropAdapter(getContext(), infos);
        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecycleView.addItemDecoration(new SpacesItemDecoration(10, 9, 0));
        mRecycleView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), infos.get(position).getName() + "", Toast.LENGTH_SHORT).show();

            }
        });
        mAdapter.notifyDataSetChanged();
    }


}
