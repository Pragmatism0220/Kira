package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityBranchBinding;
import com.moemoe.lalala.di.components.DaggerBranchComponent;
import com.moemoe.lalala.di.modules.BranchModule;
import com.moemoe.lalala.model.entity.BranchStoryAllEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.presenter.BranchContract;
import com.moemoe.lalala.presenter.BranchPresenter;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.BranchFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;


/**
 * 支线剧情页面
 * by zhangyan
 */

public class BranchActivity extends BaseActivity implements BranchContract.View {

    private ActivityBranchBinding binding;
    private TabFragmentPagerAdapter mAdapter;

    @Inject
    BranchPresenter mPresenter;
    private BranchFragment branchFragment;
    private String branchSR;
    private String branchN;
    private String branchR;
    private boolean isSelect;
    private ArrayList<BranchStoryAllEntity> branchStoryAllEntity;
    private int position;
    private HashMap<String, ArrayList<BranchStoryAllEntity>> maoList;
    private Set<String> setName;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_branch);
        binding.setPresenter(new Presenter());
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        DaggerBranchComponent.builder()
                .branchModule(new BranchModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        branchSR = getIntent().getStringExtra("BranchSR");
        branchN = getIntent().getStringExtra("BranchN");
        branchR = getIntent().getStringExtra("BranchR");
        isSelect = getIntent().getBooleanExtra("isSelect", false);
        branchStoryAllEntity = (ArrayList<BranchStoryAllEntity>) getIntent().getSerializableExtra("BranchStoryAllEntity");
        position = getIntent().getIntExtra("position", 0);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        if (!isSelect) {
            binding.branchBottom.setVisibility(View.VISIBLE);
            binding.tvBranch.setText("N(" + branchN + ")" +
                    "R(" + branchR + ")" +
                    "SR(" + branchSR + ")");
            mPresenter.loadBranchStoryAll(true, 0);
        } else {
            binding.branchBottom.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initListeners() {
        binding.checkBoxBtn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (maoList != null && maoList.size() > 0) {
                    int currentItem = binding.branchViewpager.getCurrentItem();
                    ArrayList<String> mTitles = new ArrayList<>();
                    mTitles.add("全部");
                    mTitles.addAll(setName);
                    List<BaseFragment> fragmentList = new ArrayList<>();
                    for (String key : mTitles) {
                        branchFragment = BranchFragment.newInstance(BranchActivity.this, maoList.get(key));
                        branchFragment.setSelect(isSelect, position, key);
                        branchFragment.setCheckSelect(binding.checkBoxBtn.isChecked());
                        fragmentList.add(branchFragment);
                    }
                    if (mAdapter == null) {
                        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, mTitles);
                    } else {
                        mAdapter.setFragments(getSupportFragmentManager(), fragmentList, mTitles);
                    }
                    binding.branchViewpager.setAdapter(mAdapter);
                    binding.branchTabLayout.setViewPager(binding.branchViewpager);
                    binding.branchViewpager.setCurrentItem(currentItem);
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (isSelect && branchStoryAllEntity != null) {
            setAdapterData(branchStoryAllEntity);
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
        finish();
    }

    /**
     * 全部剧情
     *
     * @param entities
     */
    @Override
    public void onLoadBranchStoryAllSuccess(ArrayList<BranchStoryAllEntity> entities) {
        if (entities != null && entities.size() > 0) {
            setAdapterData(entities);
            SearchListEntity entity = new SearchListEntity();
            entity.setFunNames(new String[]{"user_branch_story"});
            mPresenter.searchBranchNews(entity);
        }

    }

    @Override
    public void onLoadBranchStoryJoin() {

    }

    @Override
    public void getBranchNewsSuccess(ArrayList<SearchNewListEntity> searchNewLists) {
        if (searchNewLists != null && searchNewLists.size() > 0) {
            EventBus.getDefault().post(searchNewLists);
        }
    }

    /**
     * 设置数据
     *
     * @param entities
     */
    private void setAdapterData(ArrayList<BranchStoryAllEntity> entities) {
        setName = new HashSet<>();
        for (BranchStoryAllEntity allEntity : entities) {
            setName.add(allEntity.getRoleName());
        }
        maoList = new HashMap<>();
        maoList.put("全部", entities);
        for (String name : setName) {
            ArrayList<BranchStoryAllEntity> newAllEntity = new ArrayList<>();
            for (BranchStoryAllEntity allEntity : entities) {
                if (name.equals(allEntity.getRoleName())) {
                    newAllEntity.add(allEntity);
                }
            }
            maoList.put(name, newAllEntity);
        }
        ArrayList<String> mTitles = new ArrayList<>();
        mTitles.add("全部");
        mTitles.addAll(setName);
        List<BaseFragment> fragmentList = new ArrayList<>();
        for (String key : mTitles) {
            branchFragment = BranchFragment.newInstance(this, maoList.get(key));
            branchFragment.setSelect(isSelect, position, key);
            fragmentList.add(branchFragment);
        }
        if (mAdapter == null) {
            mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, mTitles);
        } else {
            mAdapter.setFragments(getSupportFragmentManager(), fragmentList, mTitles);
        }
        binding.branchViewpager.setAdapter(mAdapter);
        binding.branchTabLayout.setViewPager(binding.branchViewpager);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SearchListEntity entity = new SearchListEntity();
        entity.setFunNames(new String[]{"user_branch_story"});
        mPresenter.searchBranchNews(entity);
    }

    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    finish();
                    break;
                default:
                    break;
            }
        }

    }
}
