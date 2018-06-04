package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
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

import javax.inject.Inject;


/**
 * 角色界面
 * Created by zhangyan on 2018/5/7.
 */

public class RoleActivity extends BaseActivity implements RoleContract.View {

    private RoleAdapter mAdapter;
    private ActivityRoleBinding binding;
    private ArrayList<RoleInfoEntity> entities = new ArrayList<>();
    private ArrayList<RoleInfoEntity> putInHouseNum = new ArrayList<>();
    private String roleId;
    private boolean isPut;
    @Inject
    RolePresenter mPresenter;

    int mPosition;

    @Override
    protected void initComponent() {
        DaggerRoleComponent.builder()
                .roleModule(new RoleModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_role);
        binding.setPresenter(new Presenter());
        initView();
    }

    private void initView() {

    }

    @Override
    public void getRoleInfo(final ArrayList<RoleInfoEntity> entities) {
        this.entities = entities;
        mAdapter = new RoleAdapter(entities, this);
        binding.roleListRv.setLayoutManager(new GridLayoutManager(this, 3));
        binding.roleListRv.addItemDecoration(new SpacesItemDecoration(20, 16, 0));
        binding.roleListRv.setAdapter(mAdapter);

        //数组为0的位置默认初始化
        if (!entities.get(0).getIsPutInHouse()) {
            binding.putHouseBtn.setBackgroundResource(R.drawable.ic_role_put_house_bg);
        } else if (entities.get(0).getIsPutInHouse()) {
            binding.putHouseBtn.setBackgroundResource(R.drawable.ic_role_move_house_bg);
        }
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
                    binding.putHouseBtn.setClickable(false);
                    binding.setDeskmakeBtn.setClickable(false);
                } else if (entities.get(position).getIsUserHadRole()) {
                    binding.putHouseBtn.setClickable(true);
                    binding.putHouseBtn.setClickable(true);
                }
                binding.roleNameText.setText(entities.get(position).getName());
                Glide.with(RoleActivity.this).load(ApiService.URL_QINIU + entities.get(position).getShowHeadIcon()).into(binding.roleImage);

                roleId = entities.get(position).getId();
                isPut = entities.get(position).getIsPutInHouse();

//                //拿到集合中放入宅屋的个数
//                for (int i = 0; i < entities.size(); i++) {
//                    if (entities.get(i).isPutInHouse()) {
//                        putInHouseNum.add(entities.get(i));
//                    }
//                }
                Log.i("asd", "putInHouseNum: " + putInHouseNum.size());

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

    }

    @Override
    public void setDeskMateSuccess() {

    }

    @Override
    public void putInHouseSuccess() {
        mAdapter.getList().get(mPosition).setPutInHouse(true);
        mAdapter.notifyDataSetChanged();
        binding.putHouseBtn.setBackgroundResource(R.drawable.ic_role_move_house_bg);
    }

    @Override
    public void removeOutHouseSuccess() {
        mAdapter.getList().get(mPosition).setPutInHouse(false);
        mAdapter.notifyDataSetChanged();
        binding.putHouseBtn.setBackgroundResource(R.drawable.ic_role_put_house_bg);

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
        mPresenter.getRoleInfo();
    }


    @Override
    public void onFailure(int code, String msg) {

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
//                            if (putInHouseNum.size() > mAdapter.getList().get(mPosition).getMaxPutInHouseNum()) {
//                                ToastUtils.showShortToast(getApplicationContext(), "最多放入4个到宅屋");
//                            } else {
//                                mPresenter.putInHouse(roleId);
//                            }
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
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), ClothingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.role_diary_btn:
                    Toast.makeText(getApplicationContext(), "羁绊日记", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    }

}
