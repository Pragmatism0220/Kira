package com.moemoe.lalala.view.activity;

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
import com.moemoe.lalala.di.components.DaggerRolComponent;
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
    private String roleId;
    private boolean isPut;
    @Inject
    RolePresenter mPresenter;

    @Override
    protected void initComponent() {
        DaggerRolComponent.builder()
                .roleModule(new RoleModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_role);
        binding.setPresenter(new Presenter());
    }

    @Override
    public void getRoleInfo(final ArrayList<RoleInfoEntity> entities) {
        this.entities = entities;
        mAdapter = new RoleAdapter(entities, this);
        Log.i("RoleActivity", "initAdapter: " + entities);
        binding.roleListRv.setLayoutManager(new GridLayoutManager(this, 3));
        binding.roleListRv.addItemDecoration(new SpacesItemDecoration(0, 16, 0));
        binding.roleListRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RoleAdapter.RoleItemClickListener() {
            @Override
            public void onClick(View v, int position, int which) {
                Toast.makeText(getApplicationContext(), entities.get(position).getName(), Toast.LENGTH_SHORT).show();
                binding.roleNameText.setText(entities.get(position).getName());
                roleId = entities.get(position).getId();
                isPut = entities.get(position).getIsPutInHouse();
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

        isPut = true;
        binding.putHouseBtn.setBackgroundResource(R.drawable.ic_role_move_house_bg);

    }

    @Override
    public void removeOutHouseSuccess() {

        isPut = false;
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
                            mPresenter.putInHouse(roleId);
                            Toast.makeText(getApplicationContext(), "放入宅屋", Toast.LENGTH_SHORT).show();
                        } else {
                            mPresenter.removeOutHouse(roleId);
                            Toast.makeText(getApplicationContext(), "移除宅屋", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case R.id.set_deskmake_btn:
                    if (roleId != null) {
                        mPresenter.setDeskMate(roleId);
                        Toast.makeText(getApplicationContext(), "设为同桌", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.check_cloth_btn:
                    Toast.makeText(getApplicationContext(), "服装", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.role_diary_btn:
                    Toast.makeText(getApplicationContext(), "羁绊日记", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }

        }

    }

}
