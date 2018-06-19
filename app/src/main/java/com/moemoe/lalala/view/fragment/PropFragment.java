package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerPropComponent;
import com.moemoe.lalala.di.modules.PropModule;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.model.entity.PropInfoEntity;
import com.moemoe.lalala.presenter.PropContract;
import com.moemoe.lalala.presenter.PropPresenter;
import com.moemoe.lalala.view.adapter.PropAdapter;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by zhangyan on 2018/5/21.
 * 储物箱中道具Fragment
 */

public class PropFragment extends BaseFragment implements PropContract.View {


    @BindView(R.id.storage_recycle_view)
    RecyclerView mRecycleView;

    private PropAdapter mAdapter;
    private List<PropInfoEntity> lists;

    //筛选为false的数据
    private List<PropInfoEntity> newLists;

    @Inject
    PropPresenter mPresenter;

    private CallBack callBack;
    private firstCallBack firstCallBack;

    @Override

    protected int getLayoutId() {
        return R.layout.prop_fragment;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerPropComponent.builder()
                .propModule(new PropModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        mPresenter.getPropInfo();
    }


    @Override
    public void onFailure(int code, String msg) {

    }


    @Override
    public void getPropInfoSuccess(final ArrayList<PropInfoEntity> propInfoEntities) {
        lists = new ArrayList<>();
        this.lists = propInfoEntities;

        String firstImage = propInfoEntities.get(0).getImage();
        String name = propInfoEntities.get(0).getName();
        String describe = propInfoEntities.get(0).getDescribe();
        int toolCount = propInfoEntities.get(0).getToolCount();
        String id = propInfoEntities.get(0).getId();
        firstCallBack.firstResult(id, name, firstImage, toolCount, describe);
        propInfoEntities.get(0).setSelected(true);


        mAdapter = new PropAdapter(getContext(), lists);
        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecycleView.addItemDecoration(new SpacesItemDecoration(10, 9, 0));
        mRecycleView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PropAdapter.RoleItemClickListener() {
            @Override
            public void onClick(View v, int position, int which) {
//                PropInfoEntity propInfoEntity = propInfoEntities.get(position);
//                EventBus.getDefault().post(propInfoEntity);
//
//                String id = lists.get(position).getId();
//                String name = lists.get(position).getName();
//                String image = lists.get(position).getImage();
//                int toolCount = lists.get(position).getToolCount();
//                String describe = lists.get(position).getDescribe();
//                boolean isUserHadTool = lists.get(position).isUserHadTool();
                PropInfoEntity propInfoEntity = mAdapter.getData().get(position);
                EventBus.getDefault().post(propInfoEntity);
                String id = mAdapter.getData().get(position).getId();
                String name = mAdapter.getData().get(position).getName();
                String image = mAdapter.getData().get(position).getImage();
                int toolCount = mAdapter.getData().get(position).getToolCount();
                String describe = mAdapter.getData().get(position).getDescribe();
                boolean isUserHadTool = mAdapter.getData().get(position).isUserHadTool();


                callBack.getResult(id, name, image, toolCount, describe, isUserHadTool);
                for (int i = 0; i < propInfoEntities.size(); i++) {
                    propInfoEntities.get(i).setSelected(i == which);
                }
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 显示未拥有
     */
    public void showNotHave() {
        newLists = new ArrayList<>();
        if (lists != null && lists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
                if (!lists.get(i).isUserHadTool()) {
                    newLists.add(lists.get(i));
                }
            }
        }
        mAdapter.setData(newLists);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 显示拥有
     */
    public void showHave() {
        Log.i("asd", "showHave: " + lists);
        mAdapter.setRestore(lists);
    }


    public interface CallBack {
        void getResult(String id, String name, String image, int toolCount, String describe, boolean isUserHadTool);
    }

    public interface firstCallBack {
        void firstResult(String id, String name, String image, int toolCount, String describe);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void setFirstCallBack(firstCallBack callBack) {
        this.firstCallBack = callBack;
    }
}
