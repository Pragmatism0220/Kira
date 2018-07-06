package com.moemoe.lalala.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.event.FurnitureEvent;
import com.moemoe.lalala.event.FurnitureSelectEvent;
import com.moemoe.lalala.model.entity.AllFurnitureInfo;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.view.adapter.FurnitureInfoAdapter;
import com.moemoe.lalala.view.adapter.PropAdapter;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
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
    private boolean isUse;

    private FurnitureInfoAdapter mAdapter;

    private List<AllFurnitureInfo> info = new ArrayList<>();

    int mPosition;

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
        info.get(0).setSelected(true);
        EventBus.getDefault().post(new FurnitureSelectEvent(info.get(0).getId()));
        mAdapter = new FurnitureInfoAdapter(getContext());
        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecycleView.addItemDecoration(new SpacesItemDecoration(10, 9, 0));
        mRecycleView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PropAdapter.RoleItemClickListener() {
            @Override
            public void onClick(View v, int position, int which) {
                mPosition = position;
                AllFurnitureInfo infoEvent = mAdapter.getData().get(position);
                infoEvent.setPosition(position);
                EventBus.getDefault().post(infoEvent);

                for (int i = 0; i < info.size(); i++) {
                    if (info.get(position).getType().equals("套装")) {
                        info.get(i).setSelected(infoEvent.getSuitTypeId().equals(info.get(i).getSuitTypeId()));
                    } else {
                        info.get(i).setSelected(infoEvent.getId().equals(info.get(i).getId()));
                    }

                    if (info.get(position).getType().equals("套装")) {
                        if (infoEvent.getSuitTypeId().equals(info.get(i).getSuitTypeId())) {
                            EventBus.getDefault().post(new FurnitureSelectEvent(infoEvent.getId()));
                        }
                    } else {
                        if (infoEvent.getId().equals(info.get(i).getId())) {
                            EventBus.getDefault().post(new FurnitureSelectEvent(infoEvent.getId()));
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setList(info);
        EventBus.getDefault().register(this);
    }

    public void showHava(boolean isOnlyShowNotHave) {
        if (mAdapter != null && info != null && info.size() > 0) {
            List<AllFurnitureInfo> newList = new ArrayList<>();
            if (isOnlyShowNotHave) {
                for (AllFurnitureInfo allFurnitureInfo : info) {
                    if ("套装".equals(allFurnitureInfo.getType())) {
//                        if (!allFurnitureInfo.isUserSuitFurnitureHad()) {//显示未拥有
                        if (allFurnitureInfo.isUserSuitFurnitureHad()) {
                            newList.add(allFurnitureInfo);
                        }
                    } else {
//                        if (!allFurnitureInfo.isUserFurnitureHad()) {//未拥有
                        if (allFurnitureInfo.isUserFurnitureHad()) {
                            newList.add(allFurnitureInfo);
                        }
                    }
                }
            } else {
                newList.addAll(info);
            }
            mAdapter.setList(newList);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void updateData() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void furnitureEvent(FurnitureEvent event) {
        if (event != null) {
            if (event.getType().equals("套装")) {
                mAdapter.getList().get(event.getPosition()).setSuitPutInHouse(true);
            } else {
                mAdapter.getList().get(event.getPosition()).setPutInHouse(true);
            }

            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mAdapter != null) {
            mAdapter.unRegister();
        }
    }
}
