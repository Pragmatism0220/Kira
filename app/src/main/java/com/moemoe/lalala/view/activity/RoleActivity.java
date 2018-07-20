package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityRoleBinding;
import com.moemoe.lalala.di.components.DaggerRoleComponent;
import com.moemoe.lalala.di.modules.RoleModule;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.RoleInfoEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.upDateEntity;
import com.moemoe.lalala.presenter.RoleContract;
import com.moemoe.lalala.presenter.RolePresenter;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.view.adapter.RoleAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;
import com.squareup.haha.guava.collect.Maps;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


/**
 * 角色界面
 * Created by zhangyan on 2018/5/7.
 */

public class RoleActivity extends BaseActivity implements RoleContract.View {

    private RoleAdapter mAdapter;
    private ActivityRoleBinding binding;
    List<RoleInfoEntity> info = new ArrayList<>();

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
        mPresenter.getRoleInfo();
        mPresenter.getStoryInfo();
        initView();
        initGuide();
    }

    private void initGuide() {
        if (PreferenceUtils.isActivityFirstLaunch(this, "role")) {
            Intent intent = new Intent(this, MengXinActivity.class);
            intent.putExtra("type", "role");
            ArrayList<String> res = new ArrayList<>();
            res.add("roles1.jpg");
            res.add("roles2.jpg");
            res.add("roles3.jpg");
            res.add("roles4.jpg");
            res.add("roles5.jpg");
            res.add("roles6.jpg");
            res.add("roles7.jpg");
            intent.putExtra("gui", res);
            startActivity(intent);
        }
    }

    private void initView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
//        mPresenter.getRoleInfo();
        SearchListEntity entity = new SearchListEntity();
        entity.setFunNames(new String[]{"user_role", "diary", "user_role_clothes"});
        mPresenter.searchRoleNew(entity);
    }

    @Override
    public void getRoleInfo(final ArrayList<RoleInfoEntity> entities) {
        if (entities != null) {
            info = entities;
            mAdapter = new RoleAdapter(entities, this);
            binding.roleListRv.setLayoutManager(new GridLayoutManager(this, 3));
            binding.roleListRv.addItemDecoration(new SpacesItemDecoration(20, 16, 0));
            binding.roleListRv.setAdapter(mAdapter);


            if (entities.get(0).getRoleType().equals("official")) {
                binding.roleGuanfang.setText("官方角色");
            } else if (entities.get(0).getRoleType().equals("cameo")) {
                binding.roleGuanfang.setText("客串角色");
            } else {
                binding.roleGuanfang.setText("联动");
            }


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

            if (entities.get(0).getUserLikeRoleDefine() < 20) {
                binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_1);
            } else if (entities.get(0).getUserLikeRoleDefine() < 40) {
                binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_2);
            } else if (entities.get(0).getUserLikeRoleDefine() < 80) {
                binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_3);
            } else if (entities.get(0).getUserLikeRoleDefine() < 160) {
                binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_4);
            } else if (entities.get(0).getUserLikeRoleDefine() < 320) {
                binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_5);
            } else if (entities.get(0).getUserLikeRoleDefine() >= 320) {
                binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_6);
            }


            binding.roleHeartNum.setText(entities.get(0).getUserLikeRoleDefine() + "/" + entities.get(0).getUserLikeRoleDefineFull());
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.roleHeartNumSmall.getLayoutParams();
            if (entities.get(0).getUserLikeRoleDefine() < entities.get(0).getUserLikeRoleDefineFull()) {
                binding.roleHeartNumSmall.setVisibility(View.VISIBLE);
                int num = 240 - (240 * entities.get(0).getUserLikeRoleDefine() / entities.get(0).getUserLikeRoleDefineFull());
                if (num > 200) {
                    binding.roleHeartNumSmall.setVisibility(View.GONE);
                    binding.fl.setBackgroundResource(R.drawable.shape_role_bg_two);
                } else {
                    binding.roleHeartNumSmall.setVisibility(View.VISIBLE);
                    binding.fl.setBackgroundResource(R.drawable.shape_role_bg);
                    params.width = num;
                }
            }
            binding.roleHeartNumSmall.setLayoutParams(params);
            if (entities.get(0).getUserLikeRoleDefine() >= 320) {
                binding.roleHeartNum.setText(entities.get(0).getUserLikeRoleDefine() + "");
                binding.fl.setBackgroundResource(R.drawable.shape_role_bg);
            } else if (entities.get(0).getUserLikeRoleDefine() == 0) {
                binding.roleHeartNum.setText(entities.get(0).getUserLikeRoleDefine() + "");
            } else {
                binding.roleHeartNum.setText(entities.get(0).getUserLikeRoleDefine() + "/" + entities.get(0).getUserLikeRoleDefineFull());
            }

            if (entities.get(0).getRoleNumber() != null) {
                binding.roleNum.setVisibility(View.VISIBLE);
                binding.roleNum.setText(String.format("编号%s", entities.get(0).getRoleNumber()));
            } else {
                binding.roleNum.setVisibility(View.GONE);
            }

            Glide.with(RoleActivity.this)
                    .load(ApiService.URL_QINIU + entities.get(0).getShowHeadIcon())
                    .error(R.drawable.shape_transparent_background)
                    .placeholder(R.drawable.shape_transparent_background)
                    .into(binding.roleImage);
            binding.roleNameText.setText(entities.get(0).getName());

            entities.get(0).setSelected(true);
            mAdapter.notifyDataSetChanged();
            SearchListEntity entity = new SearchListEntity();
            entity.setFunNames(new String[]{"user_role", "diary", "user_role_clothes"});
            mPresenter.searchRoleNew(entity);

            //点击事件
            mAdapter.setOnItemClickListener(new RoleAdapter.RoleItemClickListener() {
                @Override
                public void onClick(View v, int position, int which) {
                    mPosition = position;
                    for (int i = 0; i < entities.size(); i++) {
                        if (entities.get(position).isShowNew() == true) {
                            upDateEntity updateEntity = new upDateEntity("user_role", entities.get(position).getId());
                            mPresenter.updateNews(updateEntity);
                        }
                    }

                    if (entities.get(position).getRoleType().equals("official")) {
                        binding.roleGuanfang.setText("官方角色");
                    } else if (entities.get(position).getRoleType().equals("cameo")) {
                        binding.roleGuanfang.setText("客串角色");
                    } else {
                        binding.roleGuanfang.setText("联动");
                    }

                    if (entities.get(position).getUserLikeRoleDefine() < 20) {
                        binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_1);
                    } else if (entities.get(position).getUserLikeRoleDefine() < 40) {
                        binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_2);
                    } else if (entities.get(position).getUserLikeRoleDefine() < 80) {
                        binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_3);
                    } else if (entities.get(position).getUserLikeRoleDefine() < 160) {
                        binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_4);
                    } else if (entities.get(position).getUserLikeRoleDefine() < 320) {
                        binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_5);
                    } else if (entities.get(position).getUserLikeRoleDefine() >= 320) {
                        binding.roleHeartTitle.setBackgroundResource(R.drawable.ic_home_roles_emotion_6);
                    }
                    binding.roleHeartNum.setText(entities.get(position).getUserLikeRoleDefine() + "/" + entities.get(position).getUserLikeRoleDefineFull());
                    /**
                     * 好感度比例计算
                     */
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.roleHeartNumSmall.getLayoutParams();
                    if (entities.get(position).getUserLikeRoleDefine() < entities.get(position).getUserLikeRoleDefineFull()) {
                        binding.roleHeartNumSmall.setVisibility(View.VISIBLE);
                        int num = 240 - (240 * entities.get(position).getUserLikeRoleDefine() / entities.get(position).getUserLikeRoleDefineFull());
                        if (num > 200) {
                            binding.roleHeartNumSmall.setVisibility(View.GONE);
                            binding.fl.setBackgroundResource(R.drawable.shape_role_bg_two);
                        } else {
                            binding.roleHeartNumSmall.setVisibility(View.VISIBLE);
                            binding.fl.setBackgroundResource(R.drawable.shape_role_bg);
                            params.width = num;
                        }
                    }
                    binding.roleHeartNumSmall.setLayoutParams(params);

                    if (entities.get(position).getUserLikeRoleDefine() >= 320) {
                        binding.roleHeartNum.setText(entities.get(position).getUserLikeRoleDefine() + "");
                        binding.fl.setBackgroundResource(R.drawable.shape_role_bg);
                    } else if (entities.get(position).getUserLikeRoleDefine() == 0) {
                        binding.roleHeartNum.setText(entities.get(position).getUserLikeRoleDefine() + "");

                    } else {
                        binding.roleHeartNum.setText(entities.get(position).getUserLikeRoleDefine() + "/" + entities.get(position).getUserLikeRoleDefineFull());
                    }


                    if (entities.get(position).getRoleNumber() != null) {
                        binding.roleNum.setVisibility(View.VISIBLE);
                        binding.roleNum.setText(String.format("编号%s", entities.get(position).getRoleNumber()));
                    } else {
                        binding.roleNum.setVisibility(View.GONE);
                    }
                    binding.roleNameText.setText(entities.get(position).getName());
                    Glide.with(RoleActivity.this)
                            .load(ApiService.URL_QINIU + entities.get(position).getShowHeadIcon())
                            .error(R.drawable.shape_transparent_background)
                            .placeholder(R.drawable.shape_transparent_background)
                            .into(binding.roleImage);

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
    public void upDateNewsSuccess() {
        mAdapter.getList().get(mPosition).setShowNew(false);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 获取宅屋中新消息
     *
     * @param newListEntities
     */
    @Override
    public void getRoleNewListSuccess(ArrayList<SearchNewListEntity> newListEntities) {
        if (newListEntities != null && newListEntities.size() > 0 && info != null && info.size() > 0) {
            Map<String, String> newListEntitiesMap = new HashMap<String, String>();
            for (int i = 0; i < newListEntities.size(); i++) {
                newListEntitiesMap.put(newListEntities.get(i).getTargetId(), newListEntities.get(i).getFunName());
            }
            for (int i = 0; i < info.size(); i++) {
                if (newListEntitiesMap.containsKey(info.get(i).getId())) {
                    info.get(i).setShowNew(true);
                }
            }

            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }


            //日记、服装
            Map<String, Integer> map = Maps.newHashMap();
            for (int i = 0; i < newListEntities.size(); i++) {
                if (newListEntities.get(i).getFunName().equals("diary")) {
                    if (!map.containsKey("diary")) {
                        map.put("diary", 1);
                    }
                }
            }
            Map<String, String> clothMaps = new HashMap<String, String>();
            for (int i = 0; i < newListEntities.size(); i++) {
                if (newListEntities.get(i).getFunName().equals("user_role_clothes")) {
                    clothMaps.put(newListEntities.get(i).getTargetId(), newListEntities.get(i).getFunName());
                }
            }

//            RoleInfoEntity role = info.get(mPosition);
            for (int j = 0; j < info.get(mPosition).getUserHadClothesIds().size(); j++) {
                if (clothMaps.containsKey(info.get(mPosition).getUserHadClothesIds().get(j))) {
                    binding.roleCloseNews.setVisibility(View.VISIBLE);
                    break;
                }
            }


            if (map.get("diary") != null) {
                binding.roleDiaryNews.setVisibility(View.VISIBLE);
            } else {
                binding.roleDiaryNews.setVisibility(View.GONE);
            }


        }
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        binding.ivIntelligence.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                RoleInfoEntity entity = mAdapter.getList().get(mPosition);
                Intent intent = new Intent(RoleActivity.this, IntelligenceActivity.class);
                if (entity.getName().equals("莲") || entity.getId().equals("af3e01f8-0d88-477b-ad53-15481c8a56c6")) {
                    intent.putExtra("id", "af3e01f8-0d88-477b-ad53-15481c8a56c6");
                    intent.putExtra("name", "莲");
                } else if (entity.getName().equals("沙利尔") || entity.getId().equals("7f4ca01d-db86-443d-b637-a814029e874a")) {
                    intent.putExtra("id", "7f4ca01d-db86-443d-b637-a814029e874a");
                    intent.putExtra("name", "沙利尔");
                } else if (entity.getName().equals("美藤双树") || entity.getId().equals("21a17f3c-55de-424a-8090-c21311d9a327")) {
                    intent.putExtra("id", "21a17f3c-55de-424a-8090-c21311d9a327");
                    intent.putExtra("name", "美藤双树");
                } else if (entity.getName().equals("小野抚子") || entity.getId().equals("811337cf-6a84-4c4f-a078-8a5c88e13c39")) {
                    intent.putExtra("id", "811337cf-6a84-4c4f-a078-8a5c88e13c39");
                    intent.putExtra("name", "小野抚子");
                } else if (entity.getName().equals("莓") || entity.getId().equals("120ca372-0a7e-4811-8b5a-719c4eef8dc7")) {
                    intent.putExtra("id", "120ca372-0a7e-4811-8b5a-719c4eef8dc7");
                    intent.putExtra("name", "莓");
                } else if (entity.getName().equals("蕾姆") || entity.getId().equals("fe141680-62e6-49ee-94d1-71e993d007d5")) {
                    intent.putExtra("id", "fe141680-62e6-49ee-94d1-71e993d007d5");
                    intent.putExtra("name", "蕾姆");
                } else if (entity.getName().equals("御坂美琴") || entity.getId().equals("cebd3879-fbbb-49df-ad8c-b55a2b244850")) {
                    intent.putExtra("id", "cebd3879-fbbb-49df-ad8c-b55a2b244850");
                    intent.putExtra("name", "御坂美琴");
                }
                startActivity(intent);
            }
        });
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

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        mPresenter.getRoleInfo();
//    }

    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.role_back_btn:
                    finish();
                    break;
                case R.id.put_house_btn:
                    if (info.get(mPosition).getId() != null && mAdapter.getList().get(mPosition).getIsUserHadRole()) {
//                        if (!isPut) {
                        if (!info.get(mPosition).getIsPutInHouse()) {
                            if (mAdapter.getList().get(mPosition).getCount() > mAdapter.getList().get(mPosition).getMaxPutInHouseNum()) {
                                showToast("最多可以设置4个角色为同桌");
                            }
//                            mPresenter.putInHouse(roleId);
                            mPresenter.putInHouse(info.get(mPosition).getId());
                        } else {
                            mPresenter.removeOutHouse(info.get(mPosition).getId());
                        }
                    } else {
                        showToast("" +
                                "未拥有该角色~");
                    }
                    break;
                case R.id.set_deskmake_btn:
                    if (info.get(mPosition).getId() != null && mAdapter.getList().get(mPosition).getIsUserHadRole()) {
                        mPresenter.setDeskMate(info.get(mPosition).getId());
                    } else {
                        showToast("未拥有该角色~");
                    }
                    break;
                case R.id.check_cloth_btn:
                    if (mAdapter.getList().get(mPosition).getIsUserHadRole()) {
                        if (info.get(mPosition).getId() != null) {
                            Intent intent = new Intent(RoleActivity.this, ClothingActivity.class);
                            intent.putExtra("roleId", info.get(mPosition).getId());
                            startActivity(intent);
                        } else {
                            showToast("请选择相对应角色");
                        }
                    } else {
                        showToast("未拥有该角色~");
                    }
                    break;
                case R.id.role_diary_btn:
                    if (mAdapter.getList().get(mPosition).getIsUserHadRole()) {
                        if (info.get(mPosition).getId() != null) {
//                            if (info.get(mPosition).isShowNew() == true) {
                            upDateEntity diaryupDateEntity = new upDateEntity("diary", null);
                            mPresenter.updateNews(diaryupDateEntity);
//                            }
                            Intent diary = new Intent(RoleActivity.this, DiaryActivity.class);
                            diary.putExtra("roleId", info.get(mPosition).getId());
                            startActivity(diary);
                        }
                    } else {
                        showToast("未拥有该角色~");
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
