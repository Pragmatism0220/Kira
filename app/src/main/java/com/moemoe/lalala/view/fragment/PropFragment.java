package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerPropComponent;
import com.moemoe.lalala.di.modules.PropModule;
import com.moemoe.lalala.model.entity.PropInfoEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.upDateEntity;
import com.moemoe.lalala.presenter.PropContract;
import com.moemoe.lalala.presenter.PropPresenter;
import com.moemoe.lalala.view.adapter.PropAdapter;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import retrofit2.http.POST;

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
    int mPosition;
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
        if (propInfoEntities != null && propInfoEntities.size() > 0) {

            lists = new ArrayList<>();
            this.lists = propInfoEntities;
            PropInfoEntity infoEntity = propInfoEntities.get(0);
            firstCallBack.firstResult(infoEntity);
            propInfoEntities.get(0).setSelected(true);
            mAdapter = new PropAdapter(getContext(), lists);
            mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 4));
            mRecycleView.addItemDecoration(new SpacesItemDecoration(10, 9, 0));
            mRecycleView.setAdapter(mAdapter);

            final SearchListEntity entity = new SearchListEntity();
            entity.setFunNames(new String[]{"user_tool"});
            mPresenter.searchHouseNew(entity);

            mAdapter.setOnItemClickListener(new PropAdapter.RoleItemClickListener() {
                @Override
                public void onClick(View v, int position, int which) {
                    mPosition = position;
                    if (propInfoEntities.get(position).isShowNews() == true) {
                        upDateEntity updateEntity = new upDateEntity("user_tool", propInfoEntities.get(position).getId());
                        mPresenter.updateNews(updateEntity);
                    }
                    PropInfoEntity propInfoEntity = mAdapter.getData().get(position);
                    EventBus.getDefault().post(propInfoEntity);
                    callBack.getResult(propInfoEntity, position);
                    for (int i = 0; i < propInfoEntities.size(); i++) {
                        propInfoEntities.get(i).setSelected(propInfoEntity.getId().equals(propInfoEntities.get(i).getId()));
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void getHouseNewSuccess(ArrayList<SearchNewListEntity> searchNewLists) {
        if (searchNewLists != null && searchNewLists.size() > 0) {
            Map<String, String> searchNewListMaps = new HashMap<String, String>();
            for (int i = 0; i < searchNewLists.size(); i++) {
                searchNewListMaps.put(searchNewLists.get(i).getTargetId(), searchNewLists.get(i).getFunName());
            }
            for (int i = 0; i < lists.size(); i++) {
                if (searchNewListMaps.containsKey(lists.get(i).getId())) {
                    lists.get(i).setShowNews(true);
                }
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void updateSuccess() {
        mAdapter.getList().get(mPosition).setShowNews(false);
        mAdapter.notifyDataSetChanged();
    }

    public void setLists() {
        mAdapter.getList().get(mPosition).setToolCount(mAdapter.getList().get(mPosition).getToolCount() - 1);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 显示未拥有
     */
    public void showNotHave() {
        newLists = new ArrayList<>();
        if (lists != null && lists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
//                if (!lists.get(i).isUserHadTool()) {//显示未拥有
                if (lists.get(i).isUserHadTool()) {
                    newLists.add(lists.get(i));
                }
            }
        }
        mAdapter.setData(newLists);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.unregister();
        }
    }

    /**
     * 显示拥有
     */
    public void showHave() {
        mAdapter.setRestore(lists);
    }


    public interface CallBack {
        void getResult(PropInfoEntity entity, int position);
    }

    public interface firstCallBack {
        void firstResult(PropInfoEntity entity);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void setFirstCallBack(firstCallBack callBack) {
        this.firstCallBack = callBack;
    }
}
