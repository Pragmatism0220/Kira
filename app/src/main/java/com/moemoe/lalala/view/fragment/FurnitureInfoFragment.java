package com.moemoe.lalala.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.moemoe.lalala.R;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.model.entity.AllFurnitureInfo;
import com.moemoe.lalala.model.entity.FurnitureInfoEntity;
import com.moemoe.lalala.view.adapter.FurnitureInfoAdapter;
import com.moemoe.lalala.view.base.FurnitureInfo;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangyan on 2018/5/23.
 * 家具信息Fragment
 */

@SuppressLint("ValidFragment")
public class FurnitureInfoFragment extends BaseFragment {


    @BindView(R.id.furniture_recycle_view)
    RecyclerView mRecycleView;

    private String furnId;

    private FurnitureInfoAdapter mAdapter;

    private List<AllFurnitureInfo> info = new ArrayList<>();


    public FurnitureInfoFragment() {

    }

    public FurnitureInfoFragment(ArrayList<AllFurnitureInfo> allFurnitureInfos) {
        this.info = allFurnitureInfos;

    }

    public static FurnitureInfoFragment newInstance(ArrayList<AllFurnitureInfo> allFurnitureInfos) {
        return new FurnitureInfoFragment(allFurnitureInfos);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.furniture_info_fragment;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mAdapter = new FurnitureInfoAdapter(getContext());
        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecycleView.addItemDecoration(new SpacesItemDecoration(10, 9, 0));
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), info.get(position).getName(), Toast.LENGTH_SHORT).show();
                String furnitureId = info.get(position).getId();
                EventBus.getDefault().post(furnitureId);

            }
        });
        mAdapter.setList(info);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
