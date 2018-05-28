package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.moemoe.lalala.R;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.view.adapter.FurnitureInfoAdapter;
import com.moemoe.lalala.view.base.FurnitureInfo;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangyan on 2018/5/23.
 * 家具信息Fragment
 */

public class FurnitureInfoFragment extends BaseFragment {


    @BindView(R.id.furniture_recycle_view)
    RecyclerView mRecycleView;

    private FurnitureInfoAdapter mAdapter;
    private List<FurnitureInfo> infos;

    public static FurnitureInfoFragment newInstance() {
        return new FurnitureInfoFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.furniture_info_fragment;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        initAdapter();
    }

    private void initAdapter() {
        infos = new ArrayList<>();
        infos.add(new FurnitureInfo("电脑桌", "现代风格", true));
        infos.add(new FurnitureInfo("电脑桌", "现代风格", false));//false为使用中
        infos.add(new FurnitureInfo("电脑桌", "现代风格", false));
        infos.add(new FurnitureInfo("电脑桌", "现代风格", true));
        infos.add(new FurnitureInfo("电脑桌", "现代风格", true));
        infos.add(new FurnitureInfo("电脑桌", "现代风格", true));
        infos.add(new FurnitureInfo("电脑桌", "现代风格", true));
        infos.add(new FurnitureInfo("电脑桌", "现代风格", true));
        infos.add(new FurnitureInfo("电脑桌", "现代风格", true));
        infos.add(new FurnitureInfo("电脑桌", "现代风格", true));

        mAdapter = new FurnitureInfoAdapter(getContext(), infos);
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
