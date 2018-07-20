package com.moemoe.lalala.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.AcClothingBinding;
import com.moemoe.lalala.di.components.DaggerClothComponent;
import com.moemoe.lalala.di.modules.ClothModule;
import com.moemoe.lalala.event.KiraTabLayoutEvent;
import com.moemoe.lalala.model.entity.ClothingEntity;
import com.moemoe.lalala.model.entity.OrderEntity;
import com.moemoe.lalala.model.entity.PayReqEntity;
import com.moemoe.lalala.model.entity.PayResEntity;
import com.moemoe.lalala.model.entity.SearchListEntity;
import com.moemoe.lalala.model.entity.SearchNewListEntity;
import com.moemoe.lalala.model.entity.ClothUpDateEvent;
import com.moemoe.lalala.model.entity.upDateEntity;
import com.moemoe.lalala.presenter.ClothingContrarct;
import com.moemoe.lalala.presenter.ClothingPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.IpAdressUtils;
import com.moemoe.lalala.view.adapter.TabFragmentPagerV3Adapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.ClothingFragment;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.pingplusplus.android.Pingpp;
import com.pingplusplus.ui.PaymentHandler;
import com.pingplusplus.ui.PingppUI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


/**
 * Created by Hygge on 2018/6/1.
 */

public class ClothingActivity extends BaseActivity implements ClothingContrarct.View {

    AcClothingBinding binding;


    @Inject
    ClothingPresenter mPresenter;
    private TabFragmentPagerV3Adapter mAdapter;
    private String roleId;
    private ArrayList<ClothingEntity> titles;
    private BottomMenuFragment bottomFragment;
    private OrderEntity entit;

    public static void startActivity(Context context, String roleId) {
        Intent i = new Intent(context, ClothingActivity.class);
        i.putExtra("roleId", roleId);
        context.startActivity(i);
    }


    @Override
    protected void initComponent() {
        DaggerClothComponent.builder()
                .clothModule(new ClothModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.ac_clothing);
        binding.setPresenter(new Presenter());
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        roleId = getIntent().getStringExtra("roleId");
        if (TextUtils.isEmpty(roleId)) {
            finish();
        }
        mPresenter.loadHouseClothesAll(roleId);
        initPayMenu();
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
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }

    @Override
    public void loadHouseClothSuccess(ArrayList<ClothingEntity> entities) {
        if (entities != null) {
            List<BaseFragment> fragmentList = new ArrayList<>();
            titles = entities;
            for (ClothingEntity entity : titles) {
                fragmentList.add(ClothingFragment.newInstance(this, entity));
            }
            if (mAdapter == null) {
                mAdapter = new TabFragmentPagerV3Adapter(getSupportFragmentManager(), fragmentList, titles);
            } else {
                mAdapter.setFragments(getSupportFragmentManager(), fragmentList, titles);
            }

            binding.viewPager.setAdapter(mAdapter);
            binding.kiraBar.setViewPager(binding.viewPager);
            binding.viewPager.setCurrentItem(0);
            SearchListEntity searchListEntity = new SearchListEntity();
            searchListEntity.setFunNames(new String[]{"user_role_clothes"});
            mPresenter.getNewsCloth(searchListEntity);
            ClothingEntity entity = titles.get(0);
            binding.tvName.setText(entity.getName());
            binding.tvAcquisitonName.setText(entity.getCondition());
            if (entity.isUse()) {
                binding.ivClothNameSelect.setVisibility(View.VISIBLE);
            } else {
                binding.ivClothNameSelect.setVisibility(View.GONE);
            }
            binding.tvClothContent.setText(entity.getDescribe());

        }
    }


    @Override
    public void getClothNewSuccess(ArrayList<SearchNewListEntity> searchNewLists) {
        if (searchNewLists != null && searchNewLists.size() > 0) {
            Map<String, String> searchNewListsMap = new HashMap<String, String>();
            for (int i = 0; i < searchNewLists.size(); i++) {
                searchNewListsMap.put(searchNewLists.get(i).getTargetId(), searchNewLists.get(i).getFunName());
            }
            for (int i = 0; i < titles.size(); i++) {
                if (searchNewListsMap.containsKey(titles.get(i).getId())) {
                    titles.get(i).setClothNews(true);
                }
            }
            binding.kiraBar.notifyDataSetChanged();
        }
    }

    /**
     * 使用成功
     */
    @Override
    public void loadRoleColthesSelectSuccess(int position) {
        showToast("操作成功");
        if (titles == null) {
            return;
        }
        List<ClothingEntity> list = mAdapter.getList();
        for (int i = 0; i < list.size(); i++) {
            titles.get(i).setUse(false);
            mAdapter.getList().get(i).setUse(false);
            if (i == position) {
                titles.get(i).setUse(true);
                mAdapter.getList().get(position).setUse(true);
            }
        }
        binding.kiraBar.notifyDataSetChanged();
        binding.ivClothNameSelect.setVisibility(View.VISIBLE);
    }

    /**
     * 提交订单
     *
     * @param entity
     */
    @Override
    public void onCreateOrderSuccess(OrderEntity entity) {
        entit = entity;
        final AlertDialogUtil dialogUtil1 = AlertDialogUtil.getInstance();
        dialogUtil1.createPromptNormalDialog(ClothingActivity.this, "确定购买");
        dialogUtil1.setButtonText(getString(R.string.label_confirm), getString(R.string.label_cancel), 0);
        dialogUtil1.setOnClickListener(new AlertDialogUtil.OnClickListener() {
            @Override
            public void CancelOnClick() {
                dialogUtil1.dismissDialog();
            }

            @Override
            public void ConfirmOnClick() {
                dialogUtil1.dismissDialog();
                bottomFragment.show(getSupportFragmentManager(), "payMenu");

            }
        });
        dialogUtil1.showDialog();
    }

    /**
     * 去支付
     *
     * @param entity
     */
    @Override
    public void onPayOrderSuccess(PayResEntity entity) {
        if (entity.isSuccess()) {
            finalizeDialog();
            showToast("支付成功");
            Intent i = new Intent();
            i.putExtra("position", -1);
            i.putExtra("type", "pay");
            setResult(RESULT_OK, i);
            finish();
        } else {
            if (entity.getCharge() != null) {
//                if("qpay".equals(entity.getCharge().get("channel"))){
//                    Pingpp.createPayment(OrderActivity.this, entity.getCharge().toString(),"qwallet1104765197");
//                }else {
//                    Pingpp.createPayment(OrderActivity.this, entity.getCharge().toString());
//                }
                PingppUI.createPay(ClothingActivity.this, entity.getCharge().toString(), new PaymentHandler() {
                    @Override
                    public void handlePaymentResult(Intent intent) {
                        String result = intent.getExtras().getString("result");
                        if (result.contains("success")) {
                            result = "success";
                        } else if (result.contains("fail")) {
                            result = "fail";
                        } else if (result.contains("cancel")) {
                            result = "cancel";
                        } else if (result.contains("invalid")) {
                            result = "invalid";
                        }
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                        //      String errorMsg = intent.getExtras().getString("error_msg"); // 错误信息
                        //      String extraMsg = intent.getExtras().getString("extra_msg"); // 错误信息
                        finalizeDialog();
                        if ("success".equals(result)) {
                            showToast("支付成功");
                            Intent i = new Intent();
                            i.putExtra("position", -1);
                            i.putExtra("type", "pay");
                            setResult(RESULT_OK, i);
                            finish();
                        } else {
                            showToast(result);
                        }
                    }
                });
            }
        }
    }


    @Override
    public void updateSuccess() {
        showToast("更新成功");
        binding.kiraBar.notifyDataSetChanged();
    }

    private void initPayMenu() {
        ArrayList<MenuItem> items = new ArrayList<>();
        MenuItem item = new MenuItem(0, getString(R.string.label_alipay));
        items.add(item);
        item = new MenuItem(1, getString(R.string.label_wx));
        items.add(item);
        item = new MenuItem(2, getString(R.string.label_qpay));
        items.add(item);
        bottomFragment = new BottomMenuFragment();
        bottomFragment.setShowTop(true);
        bottomFragment.setTopContent("选择支付方式");
        bottomFragment.setMenuItems(items);
        bottomFragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        bottomFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                createDialog();
                String payType = "";
                if (itemId == 0) {
                    payType = "alipay";
                } else if (itemId == 1) {
                    payType = "wx";
                } else if (itemId == 2) {
                    payType = "qpay";
                }
                PayReqEntity entity = new PayReqEntity(entit.getAddress().getAddress(),
                        payType,
                        IpAdressUtils.getIpAdress(ClothingActivity.this),
                        entit.getOrderId(),
                        entit.getAddress().getPhone(),
                        "",
                        entit.getAddress().getUserName());
                mPresenter.payOrder(entity);
            }
        });
    }

    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_cloth_back_btn:
                    finish();
                    break;
                case R.id.iv_cloth_select:
                    int viewPosition = binding.kiraBar.getViewPosition();
                    if (titles.get(viewPosition).isUserClothesHad()) {
                        mPresenter.loadRoleColthesSelect(viewPosition, roleId, titles.get(viewPosition).getId());
                    } else {
                        if (TextUtils.isEmpty(titles.get(viewPosition).getProductId())) {
                            showToast("该服装还未上架~~~");
                        } else {
                            mPresenter.createOrder(titles.get(viewPosition).getProductId());
                        }
                    }
                    break;
                case R.id.iv_left:
                    int position = binding.kiraBar.getViewPosition();
                    if (position >= 1) {
                        binding.kiraBar.setOnTabClick(binding.kiraBar.getTabView(position - 1), position - 1);
                    }
                    break;
                case R.id.iv_right:
                    int position2 = binding.kiraBar.getViewPosition();
                    if (position2 <= titles.size() - 2) {
                        binding.kiraBar.setOnTabClick(binding.kiraBar.getTabView(position2 + 1), position2 + 1);
                    }
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                binding.ivClothSelect.setText("选择");
                binding.ivClothSelect.setBackgroundResource(R.drawable.cloth_select_btn_shop);
                int viewPosition = binding.kiraBar.getViewPosition();
                titles.get(viewPosition).setUserClothesHad(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.release();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void kiraTabLayoutEvent(KiraTabLayoutEvent event) {
        if (event != null) {
            int position = event.getPosition();
            ClothingEntity entity = titles.get(position);
            binding.tvName.setText(entity.getName());
            binding.tvAcquisitonName.setText(entity.getCondition());
            if (entity.isUse()) {
                binding.ivClothNameSelect.setVisibility(View.VISIBLE);
            } else {
                binding.ivClothNameSelect.setVisibility(View.GONE);
            }
            binding.tvClothContent.setText(entity.getDescribe());
            if (entity.isUserClothesHad()) {
                binding.ivClothSelect.setText("选择");
                binding.ivClothSelect.setBackgroundResource(R.drawable.cloth_select_btn_shop);
            } else {
                binding.ivClothSelect.setText("购买");
                binding.ivClothSelect.setBackgroundResource(R.drawable.cloth_select_btn);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ClothingEvent(ClothUpDateEvent event) {
        if (event != null) {
            if (titles.get(event.getPosition()).isClothNews() == true) {
                upDateEntity updateEntity = new upDateEntity("user_role_clothes", titles.get(event.getPosition()).getId());
                mPresenter.updateNewsCloth(updateEntity);
            }

        }
    }
}
