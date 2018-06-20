package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityCompoundBinding;
import com.moemoe.lalala.di.components.DaggerBranchComponent;
import com.moemoe.lalala.di.modules.BranchModule;
import com.moemoe.lalala.model.entity.BranchStoryAllEntity;
import com.moemoe.lalala.model.entity.BranchStoryJoinEntity;
import com.moemoe.lalala.model.entity.CompoundEntity;
import com.moemoe.lalala.presenter.BranchContract;
import com.moemoe.lalala.presenter.BranchPresenter;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.CompoundAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import static com.moemoe.lalala.utils.StartActivityConstant.REQ_GO_COMPOUND_SELECT;

public class CompoundActivity extends BaseActivity implements BranchContract.View {


    @Inject
    BranchPresenter mPresenter;

    private ActivityCompoundBinding binding;
    private CompoundAdapter mAdapter;
    private String holeName;
    private ArrayList<CompoundEntity> mList;
    private int holeCount;
    private int holeLevel;
    private ArrayList<BranchStoryAllEntity> mEntiy;
    private String branchId;
    private HashMap<String, Integer> hashMap;

    @Override
    protected void initComponent() {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        DaggerBranchComponent.builder()
                .branchModule(new BranchModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_compound);
        binding.setPresenter(new Presenter());
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        holeCount = getIntent().getIntExtra("holeCount", 0);
        holeLevel = getIntent().getIntExtra("holeLevel", 0);
        branchId = getIntent().getStringExtra("branchId");
        mAdapter = new CompoundAdapter(this);
        binding.recyclerViewList.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerViewList.addItemDecoration(new SpacesItemDecoration(12, 12, 0));
        binding.recyclerViewList.setAdapter(mAdapter);
        if (holeLevel == 1) {
            holeName = "N";
        } else if (holeLevel == 2) {
            holeName = "R";
        } else if (holeLevel == 3) {
            holeName = "SR";
        } else if (holeLevel == 4) {
            holeName = "限定";
        }
        binding.compoundTitle.setText("选择" + holeCount + "张" + holeName + "卡进行合成");
        hashMap = new HashMap<>();
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mPresenter.loadBranchStoryAll(false, holeLevel);
    }

    @Override
    protected void initListeners() {
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mEntiy != null && mEntiy.size() > 0) {
                    ArrayList<BranchStoryAllEntity> entities = new ArrayList<>();
                    for (BranchStoryAllEntity entity : mEntiy) {
                        entities.add(entity.clone());
                    }
                    HashMap<String, BranchStoryAllEntity> entityHashMap = new HashMap<>();
                    for (int i = 0; i < entities.size(); i++) {
                        entityHashMap.put(entities.get(i).getId(), entities.get(i));
                    }
                    for (String key : hashMap.keySet()) {
                        entityHashMap.get(key).setUserBranchLevelCount(entityHashMap.get(key).getUserBranchLevelCount() - hashMap.get(key));
                    }
                    if (entities != null && entities.size() > 0) {
                        Intent intent = new Intent(CompoundActivity.this, BranchActivity.class);
                        intent.putExtra("isSelect", true);
                        intent.putExtra("BranchStoryAllEntity", (Serializable) entities);
                        intent.putExtra("position", position);
                        startActivityForResult(intent, REQ_GO_COMPOUND_SELECT);
                    } else {
                        showToast("没有任何角色了~~~");
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_GO_COMPOUND_SELECT && resultCode == RESULT_OK) {
            if (data != null) {
                boolean isEntity = data.getBooleanExtra("isEntity", false);
                if (isEntity) {
                    showToast("没有角色可选~~~~");
                    return;
                }
                String coverImage = data.getStringExtra("coverImage");
                String roleId = data.getStringExtra("roleId");
                int position = data.getIntExtra("position", 0);
                if (TextUtils.isEmpty(mAdapter.getItem(position).getRoleId())) {
                    if (hashMap == null || hashMap.get(roleId) == null) {
                        hashMap.put(roleId, 1);
                    } else {
                        hashMap.put(roleId, hashMap.get(roleId) + 1);
                    }
                } else {
                    if (hashMap.get(mAdapter.getItem(position).getRoleId()) == 1) {
                        hashMap.remove(mAdapter.getItem(position).getRoleId());
                    } else {
                        hashMap.put(mAdapter.getItem(position).getRoleId(), hashMap.get(mAdapter.getItem(position).getRoleId()) - 1);
                    }
                }
                mAdapter.getItem(position).setCoverImage(coverImage);
                mAdapter.getItem(position).setRoleId(roleId);

                if (mAdapter.getHeaderLayoutCount() != 0) {
                    mAdapter.notifyItemChanged(position + 1);
                } else {
                    mAdapter.notifyItemChanged(position);
                }
            }
        }
    }

    /**
     * 删除
     *
     * @param position
     */
    public void removeItem(int position) {
        CompoundEntity item = mAdapter.getItem(position);
        if (item != null && hashMap != null) {
            if (hashMap.get(item.getRoleId()) == 1) {
                hashMap.clear();
            } else {
                hashMap.put(item.getRoleId(), hashMap.get(item.getRoleId()) - 1);
            }
        }
        mAdapter.getItem(position).setRoleId("");
        mAdapter.getItem(position).setCoverImage("");
        mAdapter.getItem(position).setUserBranchLevelCount(0);
        if (mAdapter.getHeaderLayoutCount() != 0) {
            mAdapter.notifyItemChanged(position + 1);
        } else {
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < holeCount; i++) {
            CompoundEntity allEntity = new CompoundEntity();
            mList.add(allEntity);
        }
        mAdapter.setList(mList);
    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
        finish();
    }

    @Override
    public void onLoadBranchStoryAllSuccess(ArrayList<BranchStoryAllEntity> entities) {
        mEntiy = entities;
    }

    /**
     * 合成成功
     */
    @Override
    public void onLoadBranchStoryJoin() {
        Intent intent = new Intent(this, BranchInfoActivity.class);
        intent.putExtra("id", branchId);
        intent.putExtra("isCompound", false);
        startActivity(intent);
        finish();
    }

    public class Presenter {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.compound_btn_back:
                    finish();
                    break;
                case R.id.compound_btn:
                    if (hashMap != null) {
                        ArrayList<CompoundEntity> list = mAdapter.getList();
                        boolean isCount = false;
                        for (CompoundEntity entity : list) {
                            if (TextUtils.isEmpty(entity.getCoverImage())) {
                                isCount = true;
                            }
                        }
                        if (!isCount) {
                            BranchStoryJoinEntity joinEntity = new BranchStoryJoinEntity();
                            joinEntity.setSourceBranchStoryIds(hashMap);
                            joinEntity.setDestBranchStoryId(branchId);
                            mPresenter.loadBranchStoryJoin(joinEntity);
                        } else {
                            showToast("还未全部集满哟~~~");
                        }
                    } else {
                        showToast("还未全部集满哟~~~");
                    }
                    break;
            }
        }
    }
}
