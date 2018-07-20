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
import android.widget.Toast;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerBranchComponent;
import com.moemoe.lalala.di.components.DaggerBranchNewsComponent;
import com.moemoe.lalala.di.modules.BranchNewsModule;
import com.moemoe.lalala.model.entity.BranchStoryAllEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.upDateEntity;
import com.moemoe.lalala.presenter.BranchNewsContract;
import com.moemoe.lalala.presenter.BranchNewsPresenter;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.view.activity.BranchActivity;
import com.moemoe.lalala.view.activity.BranchInfoActivity;
import com.moemoe.lalala.view.adapter.BranchFragmentListAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/5/15.
 */

@SuppressLint("ValidFragment")
public class BranchFragment extends BaseFragment implements BranchNewsContract.View {

    @BindView(R.id.branch_recycle_view)
    RecyclerView mRecycleView;

    @Inject
    BranchNewsPresenter mPresenter;

    private BranchFragmentListAdapter mAdapter;
    private ArrayList<BranchStoryAllEntity> mlists;
    private boolean isSelect;
    private Context mContext;
    private int postion;
    private String key;
    private boolean isCheckSelect;
    int mPosition;

    @SuppressLint("ValidFragment")
    public BranchFragment(Context context, ArrayList<BranchStoryAllEntity> entities) {
        this.mlists = entities;
        this.mContext = context;
    }

    public static BranchFragment newInstance(Context context, ArrayList<BranchStoryAllEntity> entities) {
        return new BranchFragment(context, entities);
    }


    public void setSelect(boolean isSelect, int position, String key) {
        this.isSelect = isSelect;
        this.postion = position;
        this.key = key;
    }

    public void setCheckSelect(boolean isCheckSelect) {
        this.isCheckSelect = isCheckSelect;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.branch_fragment;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerBranchNewsComponent.builder()
                .branchNewsModule(new BranchNewsModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        mAdapter = new BranchFragmentListAdapter(getContext(), isSelect);
        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecycleView.addItemDecoration(new SpacesItemDecoration(12, 12, 0));
        mRecycleView.setAdapter(mAdapter);
        if (isSelect) {
            branchOrCompound(mlists);
        } else {
            branchOrCompound(mlists);
        }
        EventBus.getDefault().register(this);

        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mPosition = position;
                if (mlists.get(position).isBranchShowNews() == true) {
                    upDateEntity upDateEntity = new upDateEntity("user_branch_story", mlists.get(position).getId());
                    mPresenter.updateBranchNews(upDateEntity);
                }

                if (isSelect) {
                    Intent intent = new Intent();
                    intent.putExtra("coverImage", mAdapter.getItem(position).getCoverImage());
                    intent.putExtra("roleId", mAdapter.getItem(position).getId());
                    intent.putExtra("position", postion);
                    ((BranchActivity) mContext).setResult(RESULT_OK, intent);
                    ((BranchActivity) mContext).finish();
                } else {
                    BranchStoryAllEntity entity = mAdapter.getItem(position);
                    if (entity.getLevel() == 1 && entity.getUserBranchLevelCount() <= 0) {
                        ToastUtils.showShortToast(mContext, "还未拥有该卡片~");
                        return;
                    }
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
            if (isSelect && key.equals("全部")) {
                if (objects == null || objects.size() == 0) {
                    Intent intent = new Intent();
                    intent.putExtra("isEntity", true);
                    ((BranchActivity) mContext).setResult(RESULT_OK, intent);
                    ((BranchActivity) mContext).finish();
                }
            }
            if (isSelect) {
                Iterator<BranchStoryAllEntity> iterator = objects.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getUserBranchLevelCount() == 0) {
                        iterator.remove();
                    }
                }
            } else {
                if (isCheckSelect) {
                    Iterator<BranchStoryAllEntity> iterator = objects.iterator();
                    while (iterator.hasNext()) {
                        if (iterator.next().getUserBranchLevelCount() == 0) {
                            iterator.remove();
                        }
                    }
                }
            }
            mAdapter.setList(objects);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onFailure(int code, String msg) {

    }

//    @Override
//    public void getBranchNewsSuccess(ArrayList<SearchNewListEntity> searchNewLists) {
//        if (searchNewLists != null && searchNewLists.size() > 0) {
//            Map<String, String> branchNewsMaps = new HashMap<String, String>();
//            for (int i = 0; i < searchNewLists.size(); i++) {
//                branchNewsMaps.put(searchNewLists.get(i).getTargetId(), searchNewLists.get(i).getFunName());
//            }
//            for (int i = 0; i < searchNewLists.size(); i++) {
//                if (branchNewsMaps.containsKey(searchNewLists.get(i).getTargetId())) {
//                    mlists.get(i).setBranchShowNews(true);
//                }
//            }
//
//        }
//    }

    @Override
    public void updateBranchNewsSuccess() {
        mAdapter.getList().get(mPosition).setBranchShowNews(false);
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void branchNewsEvent(ArrayList<SearchNewListEntity> entities) {
        if (entities != null && entities.size() > 0) {
            Map<String, String> branchNewsMaps = new HashMap<String, String>();
            for (int i = 0; i < entities.size(); i++) {
                branchNewsMaps.put(entities.get(i).getTargetId(), entities.get(i).getFunName());
            }
            for (int i = 0; i < mlists.size(); i++) {
                if (branchNewsMaps.containsKey(mlists.get(i).getId())) {
                    mlists.get(i).setBranchShowNews(true);
                }
            }
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
