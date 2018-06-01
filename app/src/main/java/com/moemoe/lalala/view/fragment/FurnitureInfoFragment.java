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

    private FurnitureInfoAdapter mAdapter;

    private List<FurnitureInfoEntity> lists;
    private List<AllFurnitureInfo> info = new ArrayList<>();

    public static FurnitureInfoFragment newInstance() {
        return new FurnitureInfoFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.furniture_info_fragment;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);//注册EventBus
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(FurnitureInfoEntity entity) {
        Log.i("FurnitureInfoFragment", "onEvent: " + entity);
        lists = new ArrayList<>();
        if (entity != null) {
            lists.add(entity);
        }
        lists.add(entity);

        Log.i("FurnitureInfoFragment", "onEvent to list: " + lists);
        Iterator<ArrayList<AllFurnitureInfo>> allinfo = entity.getAllFurnitures().values().iterator();
        while (allinfo.hasNext()) {
            info.addAll(allinfo.next());
        }
        Log.i("FurnitureInfoFragment", "valuse: " + info);
        initAdapter();
    }


    private void initAdapter() {
        mAdapter = new FurnitureInfoAdapter(getContext(), info);
        Log.i("FurnitureInfoFragment", "initAdapter: " + info);
        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecycleView.addItemDecoration(new SpacesItemDecoration(10, 9, 0));
        mRecycleView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(getContext(), l.get(position).getName() + "", Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
