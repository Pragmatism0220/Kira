package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityBranchInfoBinding;
import com.moemoe.lalala.di.components.DaggerBranchInfoComponent;
import com.moemoe.lalala.di.modules.BranchInfoModule;
import com.moemoe.lalala.model.entity.BranchStoryAllEntity;
import com.moemoe.lalala.presenter.BranchInfoContract;
import com.moemoe.lalala.presenter.BranchInfoPresenter;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.base.BranchInfoBean;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import java.io.DataInput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class BranchInfoActivity extends BaseActivity implements BranchInfoContract.View {

    private ActivityBranchInfoBinding binding;
    private List<BranchInfoBean> mlists;
    private boolean isCompound;

    @Inject
    BranchInfoPresenter mPresenter;
    private BranchStoryAllEntity entity;

    @Override
    protected void initComponent() {
        DaggerBranchInfoComponent.builder()
                .branchInfoModule(new BranchInfoModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_branch_info);
        binding.setPresenter(new Presenter());
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        isCompound = getIntent().getBooleanExtra("isCompound", false);
        String id = getIntent().getStringExtra("id");
        entity = (BranchStoryAllEntity) getIntent().getSerializableExtra("entity");
        if (!isCompound) {
            mPresenter.loadBranchStoryInfo(id);
        } else {
            setData(entity);
        }
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {
    }

    private void setData(BranchStoryAllEntity entity) {
        binding.branchInfoTitle.setText(entity.getName());
        binding.branchInfoAuthorby.setText("剧本作者:" + entity.getAuthorBy());
        binding.tvHuashi.setText("画师:" + entity.getPainterBy());
        int w = (int) getResources().getDimension(R.dimen.x456);
        int h = (int) getResources().getDimension(R.dimen.y608);
        Glide.with(this)
                .load(StringUtils.getUrl(this, entity.getCoverImage(), w, h, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .into(binding.branchInfoBg);
        if (isCompound) {
            String holeName = "";
            if (entity.getHoleLevel() == 1) {
                holeName = "N";
            } else if (entity.getHoleLevel() == 2) {
                holeName = "R";
            } else if (entity.getHoleLevel() == 3) {
                holeName = "SR";
            } else if (entity.getHoleLevel() == 4) {
                holeName = "限定";
            }
            binding.branchInfoPossessCount.setText("使用" + entity.getHoleCount() + "张" + holeName + "卡进行合成");
            binding.branchInfoBg.setAlpha(0.5f);
            binding.branchInfoTitle.setAlpha(0.5f);
            binding.branchInfoAuthorby.setAlpha(0.5f);
            binding.branchInfoRecall.setImageResource(R.drawable.branch_info_recall_selector);
        } else {
            binding.branchInfoPossessCount.setText("已拥有:" + entity.getUserBranchLevelCount());
            if (entity.getUserBranchLevelCount() == 0) {
                binding.branchInfoBg.setAlpha(0.5f);
                binding.branchInfoTitle.setAlpha(0.5f);
                binding.branchInfoAuthorby.setAlpha(0.5f);
                binding.branchInfoRecall.setImageResource(R.drawable.bg_ic_home_detialplay_no);
            } else {
                binding.branchInfoBg.setAlpha(1.0f);
                binding.branchInfoTitle.setAlpha(1.0f);
                binding.branchInfoAuthorby.setAlpha(1.0f);
                binding.branchInfoRecall.setImageResource(R.drawable.bg_ic_home_detialplay);
            }
        }

        ArrayList<String> detailImage = entity.getDetailImage();
        if (isCompound) {
            binding.ivPlotMemoryFrist.setImageResource(R.drawable.ic_home_plot_memory_none);
            binding.ivPlotMemoryThree.setImageResource(R.drawable.ic_home_plot_memory_none);
            binding.ivPlotMemoryTwo.setImageResource(R.drawable.ic_home_plot_memory_none);
        } else {
            if (detailImage != null && detailImage.size() > 0) {
                int size = detailImage.size();
                int wi = (int) getResources().getDimension(R.dimen.x164);
                int he = (int) getResources().getDimension(R.dimen.y164);
                if (size == 1) {
                    Glide.with(this)
                            .load(StringUtils.getUrl(this, detailImage.get(0), wi, he, false, true))
                            .error(R.drawable.shape_gray_e8e8e8_background)
                            .placeholder(R.drawable.shape_gray_e8e8e8_background)
                            .into(binding.ivPlotMemoryFrist);
                    binding.ivPlotMemoryTwo.setVisibility(View.GONE);
                    binding.ivPlotMemoryThree.setVisibility(View.GONE);
                } else if (size == 2) {
                    Glide.with(this)
                            .load(StringUtils.getUrl(this, detailImage.get(0), wi, he, false, true))
                            .error(R.drawable.shape_gray_e8e8e8_background)
                            .placeholder(R.drawable.shape_gray_e8e8e8_background)
                            .into(binding.ivPlotMemoryFrist);

                    Glide.with(this)
                            .load(StringUtils.getUrl(this, detailImage.get(1), wi, he, false, true))
                            .error(R.drawable.shape_gray_e8e8e8_background)
                            .placeholder(R.drawable.shape_gray_e8e8e8_background)
                            .into(binding.ivPlotMemoryTwo);
                    binding.ivPlotMemoryThree.setVisibility(View.GONE);

                } else if (size == 3) {
                    Glide.with(this)
                            .load(StringUtils.getUrl(this, detailImage.get(0), wi, he, false, true))
                            .error(R.drawable.shape_gray_e8e8e8_background)
                            .placeholder(R.drawable.shape_gray_e8e8e8_background)
                            .into(binding.ivPlotMemoryFrist);

                    Glide.with(this)
                            .load(StringUtils.getUrl(this, detailImage.get(1), wi, he, false, true))
                            .error(R.drawable.shape_gray_e8e8e8_background)
                            .placeholder(R.drawable.shape_gray_e8e8e8_background)
                            .into(binding.ivPlotMemoryThree);

                    Glide.with(this)
                            .load(StringUtils.getUrl(this, detailImage.get(2), wi, he, false, true))
                            .error(R.drawable.shape_gray_e8e8e8_background)
                            .placeholder(R.drawable.shape_gray_e8e8e8_background)
                            .into(binding.ivPlotMemoryTwo);
                }
            }
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
        finish();
    }

    @Override
    public void onLoadBranchStoryInfoSuccess(BranchStoryAllEntity entities) {
        entity = entities;
        setData(entities);
    }

    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.branch_back_btn:
                    finish();
                    break;
                case R.id.branch_info_recall:
                    if (isCompound) {
                        Intent intent = new Intent(BranchInfoActivity.this, CompoundActivity.class);
                        intent.putExtra("holeCount", entity.getHoleCount());
                        intent.putExtra("holeLevel", entity.getHoleLevel());
                        intent.putExtra("branchId", entity.getId());
                        startActivity(intent);
                        finish();
                    } else {
                        if (entity.getUserBranchLevelCount() > 0) {
                            Intent i = new Intent(BranchInfoActivity.this, MapEventNewActivity.class);
                            i.putExtra("id", entity.getScriptId());
                            i.putExtra("type", true);
                            startActivity(i);
                        } else {
                            showToast("还未拥有该剧情哟~~~");
                        }
                    }
                    break;
                default:
                    break;
            }

        }
    }
}
