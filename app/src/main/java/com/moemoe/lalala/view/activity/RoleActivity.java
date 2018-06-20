package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityRoleBinding;
import com.moemoe.lalala.di.components.DaggerRoleComponent;
import com.moemoe.lalala.di.modules.RoleModule;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.RoleInfoEntity;
import com.moemoe.lalala.presenter.RoleContract;
import com.moemoe.lalala.presenter.RolePresenter;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.view.adapter.RoleAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * 角色界面
 * Created by zhangyan on 2018/5/7.
 */

public class RoleActivity extends BaseActivity implements RoleContract.View {

    private RoleAdapter mAdapter;
    private ActivityRoleBinding binding;
    List<RoleInfoEntity> info = new ArrayList<>();
    private String roleId;
    private boolean isPut;
    @Inject
    RolePresenter mPresenter;

    int mPosition;
    private boolean isPause;

    @Override
    protected void initComponent() {
        DaggerRoleComponent.builder()
                .roleModule(new RoleModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_role);
        binding.setPresenter(new Presenter());
        mPresenter.getRoleInfo();
        initView();
    }

    private void initView() {

    }

    @Override
    public void getRoleInfo(final ArrayList<RoleInfoEntity> entities) {
        if (entities != null) {
            info = entities;
            mAdapter = new RoleAdapter(entities, this);
            binding.roleListRv.setLayoutManager(new GridLayoutManager(this, 3));
            binding.roleListRv.addItemDecoration(new SpacesItemDecoration(20, 16, 0));
            binding.roleListRv.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();


            //未拥有角色取消放入宅屋点击事件
            if (!entities.get(0).getIsUserHadRole()) {
                binding.roleHeartTitle.setVisibility(View.GONE);
                binding.fl.setVisibility(View.GONE);
                binding.roleDiaryBtn.setVisibility(View.GONE);
                binding.putHouseBtn.setVisibility(View.GONE);
                binding.setDeskmakeBtn.setVisibility(View.GONE);
                binding.checkClothBtn.setVisibility(View.GONE);
            } else if (entities.get(0).getIsUserHadRole()) {
                binding.roleHeartTitle.setVisibility(View.VISIBLE);
                binding.fl.setVisibility(View.VISIBLE);
                binding.roleDiaryBtn.setVisibility(View.VISIBLE);
                binding.putHouseBtn.setVisibility(View.VISIBLE);
                binding.setDeskmakeBtn.setVisibility(View.VISIBLE);
                binding.checkClothBtn.setVisibility(View.VISIBLE);
            }

            //数组为0的位置默认初始化
            if (!entities.get(0).getIsPutInHouse()) {
                binding.putHouseBtn.setBackgroundResource(R.drawable.ic_role_put_house_bg);
            } else if (entities.get(0).getIsPutInHouse()) {
                binding.putHouseBtn.setBackgroundResource(R.drawable.ic_role_move_house_bg);
            }

            if ("厌烦".equals(entities.get(0).getUserLikeRoleDefineTxt())) {
                binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_1);
            } else if ("普通".equals(entities.get(0).getUserLikeRoleDefineTxt())) {
                binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_2);
            } else if ("友好".equals(entities.get(0).getUserLikeRoleDefineTxt())) {
                binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_3);
            } else if ("信任".equals(entities.get(0).getUserLikeRoleDefineTxt())) {
                binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_4);
            } else if ("爱".equals(entities.get(0).getUserLikeRoleDefineTxt())) {
                binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_5);
            } else if ("真爱".equals(entities.get(0).getUserLikeRoleDefineTxt())) {
                binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_6);
            }
            /**
             * 好感度计时器
             */
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.roleHeartNumSmall.getLayoutParams();
            if (entities.get(0).getUserLikeRoleDefine() < entities.get(0).getUserLikeRoleDefineFull()) {
                binding.roleHeartNumSmall.setVisibility(View.VISIBLE);
                params.width = 240 * entities.get(0).getUserLikeRoleDefine() / entities.get(0).getUserLikeRoleDefineFull();
            } else if (entities.get(0).getUserLikeRoleDefine() >= entities.get(0).getUserLikeRoleDefineFull()) {
                binding.roleHeartNum.setText("");
                binding.roleHeartNum.setText(entities.get(0).getUserLikeRoleDefineFull());
            }
            binding.roleHeartNumSmall.setLayoutParams(params);


            binding.roleHeartNum.setText(entities.get(0).getUserLikeRoleDefine() + "/" + entities.get(0).getUserLikeRoleDefineFull());
            binding.roleNum.setText("编号" + entities.get(0).getRoleNumber());
            Glide.with(RoleActivity.this).load(ApiService.URL_QINIU + entities.get(0).getShowHeadIcon()).into(binding.roleImage);
            binding.roleNameText.setText(entities.get(0).getName());
            entities.get(0).setSelected(true);

            //点击事件
            mAdapter.setOnItemClickListener(new RoleAdapter.RoleItemClickListener() {
                @Override
                public void onClick(View v, int position, int which) {
                    mPosition = position;
                    //未拥有角色取消放入宅屋点击事件
                    if (!entities.get(position).getIsUserHadRole()) {
                        binding.roleHeartTitle.setVisibility(View.GONE);
                        binding.fl.setVisibility(View.GONE);
                        binding.roleDiaryBtn.setVisibility(View.GONE);
                        binding.putHouseBtn.setVisibility(View.GONE);
                        binding.setDeskmakeBtn.setVisibility(View.GONE);
                        binding.checkClothBtn.setVisibility(View.GONE);
                    } else if (entities.get(position).getIsUserHadRole()) {
                        binding.roleHeartTitle.setVisibility(View.VISIBLE);
                        binding.fl.setVisibility(View.VISIBLE);
                        binding.roleDiaryBtn.setVisibility(View.VISIBLE);
                        binding.putHouseBtn.setVisibility(View.VISIBLE);
                        binding.setDeskmakeBtn.setVisibility(View.VISIBLE);
                        binding.checkClothBtn.setVisibility(View.VISIBLE);
                    }


                    if ("厌烦".equals(entities.get(position).getUserLikeRoleDefineTxt())) {
                        binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_1);
                    } else if ("普通".equals(entities.get(position).getUserLikeRoleDefineTxt())) {
                        binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_2);
                    } else if ("友好".equals(entities.get(position).getUserLikeRoleDefineTxt())) {
                        binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_3);
                    } else if ("信任".equals(entities.get(position).getUserLikeRoleDefineTxt())) {
                        binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_4);
                    } else if ("爱".equals(entities.get(position).getUserLikeRoleDefineTxt())) {
                        binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_5);
                    } else if ("真爱".equals(entities.get(position).getUserLikeRoleDefineTxt())) {
                        binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_6);
                    }

                    binding.roleHeartNum.setText(entities.get(position).getUserLikeRoleDefine() + "/" + entities.get(position).getUserLikeRoleDefineFull());

                    /**
                     * 好感度比例计算
                     */
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.roleHeartNumSmall.getLayoutParams();
                    if (entities.get(position).getUserLikeRoleDefine() < entities.get(position).getUserLikeRoleDefineFull()) {
                        binding.roleHeartNumSmall.setVisibility(View.VISIBLE);
                        params.width = 240 * entities.get(position).getUserLikeRoleDefine() / entities.get(position).getUserLikeRoleDefineFull();
                    }
                    binding.roleHeartNumSmall.setLayoutParams(params);
                    if (entities.get(position).getUserLikeRoleDefine() >= entities.get(position).getUserLikeRoleDefineFull()) {
                        binding.roleHeartNum.setText("");
                        binding.roleHeartNum.setText(entities.get(position).getUserLikeRoleDefineFull());
                    }
                    binding.roleNum.setText("编号" + entities.get(position).getRoleNumber());
                    binding.roleNameText.setText(entities.get(position).getName());
                    Glide.with(RoleActivity.this).load(ApiService.URL_QINIU + entities.get(position).getShowHeadIcon()).into(binding.roleImage);
                    roleId = entities.get(position).getId();
                    isPut = entities.get(position).getIsPutInHouse();

                    //拿到集合中放入宅屋的个数
                    int count = 0;
                    for (int i = 0; i < entities.size(); i++) {
                        if (entities.get(i).isPutInHouse()) {
                            count++;
                        }
                    }
                    entities.get(position).setCount(count);

                    if (entities.get(position).getIsPutInHouse()) {
                        binding.putHouseBtn.setBackgroundResource(R.drawable.ic_role_move_house_bg);
                    } else {
                        binding.putHouseBtn.setBackgroundResource(R.drawable.ic_role_put_house_bg);
                    }
                    for (int i = 0; i < entities.size(); i++) {
                        entities.get(i).setSelected(i == which);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
            Log.i("asd", "getRoleInfo: " + entities);
        }
    }

    @Override
    public void setDeskMateSuccess() {
        showToast("操作成功");

    }


    @Override
    public void putInHouseSuccess() {
        mAdapter.getList().get(mPosition).setPutInHouse(true);
        mAdapter.notifyDataSetChanged();
        binding.putHouseBtn.setBackgroundResource(R.drawable.ic_role_move_house_bg);
        showToast("操作成功");
    }

    @Override
    public void removeOutHouseSuccess() {
        mAdapter.getList().get(mPosition).setPutInHouse(false);
        mAdapter.notifyDataSetChanged();
        binding.putHouseBtn.setBackgroundResource(R.drawable.ic_role_put_house_bg);
        showToast("操作成功");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {


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


    @Override
    public void onFailure(int code, String msg) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            if (requestCode == 1) {
                mPresenter.getRoleInfo();
            }
        }
    }

    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.role_back_btn:
                    finish();
                    break;
                case R.id.put_house_btn:
                    if (roleId != null) {
                        if (!isPut) {
                            if (mAdapter.getList().get(mPosition).getCount() > mAdapter.getList().get(mPosition).getMaxPutInHouseNum()) {
                                showToast("最多可以设置4个角色为同桌");
                            }
                            mPresenter.putInHouse(roleId);
                        } else {
                            mPresenter.removeOutHouse(roleId);
                        }
                    }
                    break;
                case R.id.set_deskmake_btn:
                    if (roleId != null) {
                        mPresenter.setDeskMate(roleId);
                    }
                    break;
                case R.id.check_cloth_btn:
                    if (roleId != null) {
                        Intent intent = new Intent(RoleActivity.this, ClothingActivity.class);
                        intent.putExtra("roleId", roleId);
                        startActivity(intent);
                    } else {
                        showToast("请选择相对应角色");
                    }

                    break;
                case R.id.role_diary_btn:
                    Intent diary = new Intent(RoleActivity.this, DiaryActivity.class);
                    startActivity(diary);
                    break;
                default:
                    break;
            }
        }
    }

}
