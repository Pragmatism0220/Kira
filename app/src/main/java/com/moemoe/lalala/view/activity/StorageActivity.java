package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityStorageBinding;
import com.moemoe.lalala.di.components.DaggerStorageComponents;
import com.moemoe.lalala.di.modules.StorageModule;
import com.moemoe.lalala.event.FurnitureEvent;
import com.moemoe.lalala.event.StorageDefaultDataEvent;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.AllFurnitureInfo;
import com.moemoe.lalala.model.entity.OrderEntity;
import com.moemoe.lalala.model.entity.PayReqEntity;
import com.moemoe.lalala.model.entity.PayResEntity;
import com.moemoe.lalala.model.entity.PropInfoEntity;
import com.moemoe.lalala.presenter.StorageContract;
import com.moemoe.lalala.presenter.StoragePresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.IpAdressUtils;
import com.moemoe.lalala.view.adapter.TabPageAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.fragment.FurnitureFragment;
import com.moemoe.lalala.view.fragment.PropFragment;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.pingplusplus.ui.PaymentHandler;
import com.pingplusplus.ui.PingppUI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 储物柜界面
 */

public class StorageActivity extends BaseActivity implements PropFragment.CallBack, PropFragment.firstCallBack, StorageContract.View {
    public static boolean sIsRegist = false;

    private ActivityStorageBinding binding;

    //道具
    private PropFragment mPropFragment;
    //家具
    private FurnitureFragment mFurnitureFragment;

    private List<Fragment> mFragmentLists;
    private TabPageAdapter mTabAdapter;

    private boolean isProp;


    @Inject
    StoragePresenter mPresenter;
    //家具套装
    private AllFurnitureInfo furnitureInfo;
    //道具
    private PropInfoEntity mPropInfoEntity;
    private BottomMenuFragment bottomFragment;
    private OrderEntity entit;
    private PropInfoEntity entity;
    private boolean isCheck = true;


    @Override
    protected void initComponent() {
        DaggerStorageComponents.builder()
                .storageModule(new StorageModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_storage);
        binding.setPresenter(new Presenter());
        EventBus.getDefault().register(this);
        initViewPager();
        initPayMenu();
    }

    private void initViewPager() {
        mPropFragment = new PropFragment();
        mFurnitureFragment = new FurnitureFragment();

        mPropFragment.setCallBack(this);
        mPropFragment.setFirstCallBack(this);

        mFragmentLists = new ArrayList<>();
        mFragmentLists.add(mPropFragment);
        mFragmentLists.add(mFurnitureFragment);
        mTabAdapter = new TabPageAdapter(getSupportFragmentManager(), mFragmentLists);
        binding.storageViewpager.setCurrentItem(0);
        binding.storageViewpager.setAdapter(mTabAdapter);
        binding.storageViewpager.setOnPageChangeListener(new myOnPageChangeListener());
        binding.radioGroup.setOnCheckedChangeListener(new myCheckChangeListener());
        binding.radioGroup.check(R.id.choose_prop_btn);

    }

    /**
     * 选中信息返回
     */
    @Override
    public void getResult(PropInfoEntity entity, int position) {
        if (entity != null) {
            this.entity = entity;
            binding.storageCommodityName.setText(entity.getName());
            Glide.with(this).load(ApiService.URL_QINIU + entity.getImage()).into(binding.storageImage);
            binding.storageCommodityNum.setText("X" + entity.getToolCount());
            binding.storageCommodityInfo.setText(entity.getDescribe());


        }
    }

    @Override
    public void firstResult(PropInfoEntity entity) {
        if (entity != null) {
            this.entity = entity;
            Glide.with(this).load(ApiService.URL_QINIU + entity.getImage()).into(binding.storageImage);
            binding.storageCommodityName.setText(entity.getName());
            binding.storageCommodityInfo.setText(entity.getDescribe());
            binding.storageCommodityNum.setText(entity.getToolCount() + "");

            if (!entity.isUserHadTool() && entity.getToolCount() == 0) {
                binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.btn_home_items_buy__null_normal);
                binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_use_null_normal);
            } else {
                binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.storage_buy_btn_bg);
                binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.storage_use_btn_bg);
            }

            if (entity.getProductId() != null) {
                binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.storage_buy_btn_bg);
                binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_use_null_normal);
            } else {
                binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.btn_home_items_buy__null_normal);
                binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_use_null_normal);
            }

            //拥有
            if (entity.getToolCount() != 0) {
                binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.storage_buy_btn_bg);
            } else {
                binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.btn_home_items_buy__null_normal);
                binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_use_null_normal);
            }

        }
    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }

    /**
     * 单品家具使用
     */
    @Override
    public void furnitureUseSuccess(int position) {
        EventBus.getDefault().post(new FurnitureEvent(position, furnitureInfo.getType()));
    }

    /**
     * 套装家具使用
     */
    @Override
    public void suitUseSuccess(int position) {
        EventBus.getDefault().post(new FurnitureEvent(position, furnitureInfo.getType()));
    }

    /**
     * 提交訂單
     *
     * @param entity
     */
    @Override
    public void onCreateOrderSuccess(OrderEntity entity) {
        entit = entity;
        final AlertDialogUtil dialogUtil1 = AlertDialogUtil.getInstance();
        dialogUtil1.createPromptNormalDialog(StorageActivity.this, "确定购买");
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
     * 支付回調
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
                PingppUI.createPay(StorageActivity.this, entity.getCharge().toString(), new PaymentHandler() {
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * RadioButton切换fragment
     */
    private class myCheckChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.choose_prop_btn:
                    isCheck = true;
                    binding.topBg.setBackgroundResource(R.drawable.bg_home_items_prop_background);
                    binding.storageCommodityNum.setVisibility(View.VISIBLE);
                    binding.storageViewpager.setCurrentItem(0, false);
                    binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.storage_use_btn_bg);
                    break;
                case R.id.choose_furniture_btn:
                    isCheck = false;
                    binding.topBg.setBackgroundResource(R.drawable.ic_role_top_bg);
                    binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.storage_put_btn_bg);
                    binding.storageCommodityNum.setVisibility(View.GONE);
                    binding.storageViewpager.setCurrentItem(1, false);
                    break;
                default:
                    break;
            }


        }
    }

    /**
     * ViewPager事件
     */

    private class myOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    RadioGroup.LayoutParams layoutParams = (RadioGroup.LayoutParams) binding.choosePropBtn.getLayoutParams();
                    layoutParams.height = (int) getResources().getDimension(R.dimen.y81);
                    binding.choosePropBtn.setLayoutParams(layoutParams);
                    RadioGroup.LayoutParams layoutParams1 = (RadioGroup.LayoutParams) binding.chooseFurnitureBtn.getLayoutParams();
                    layoutParams1.height = (int) getResources().getDimension(R.dimen.y74);
                    binding.chooseFurnitureBtn.setLayoutParams(layoutParams1);


                    binding.radioGroup.check(R.id.choose_prop_btn);
                    EventBus.getDefault().post(new StorageDefaultDataEvent(false));
                    isProp = true;
                    break;
                case 1:
                    RadioGroup.LayoutParams layoutParams2 = (RadioGroup.LayoutParams) binding.choosePropBtn.getLayoutParams();
                    layoutParams2.height = (int) getResources().getDimension(R.dimen.y74);
                    binding.choosePropBtn.setLayoutParams(layoutParams2);
                    RadioGroup.LayoutParams layoutParams3 = (RadioGroup.LayoutParams) binding.chooseFurnitureBtn.getLayoutParams();
                    layoutParams3.height = (int) getResources().getDimension(R.dimen.y81);
                    binding.chooseFurnitureBtn.setLayoutParams(layoutParams3);


                    binding.radioGroup.check(R.id.choose_furniture_btn);
                    EventBus.getDefault().post(new StorageDefaultDataEvent(true));
                    isProp = false;
                    break;
                default:
                    break;
            }

            View propView = binding.choosePropBtn;
            View furnitureView = binding.chooseFurnitureBtn;

            propView.layout(propView.getLeft(), isProp ? propView.getTop() + 6 : propView.getTop() - 6, propView.getRight(),
                    isProp ? propView.getBottom() + 6 : propView.getBottom() - 6);
            furnitureView.layout(furnitureView.getLeft(), isProp ? furnitureView.getTop() - 6 : furnitureView.getTop() + 6, furnitureView.getRight(),
                    isProp ? furnitureView.getBottom() - 6 : furnitureView.getBottom() + 6);
        }


        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

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
                        IpAdressUtils.getIpAdress(StorageActivity.this),
                        entit.getOrderId(),
                        entit.getAddress().getPhone(),
                        "",
                        entit.getAddress().getUserName());
                mPresenter.payOrder(entity);
            }
        });
    }

    @Override
    protected void initListeners() {
        binding.storageCheckBoxBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPropFragment.showNotHave();
                    mFurnitureFragment.furnShowNotHave();
                } else {
                    mPropFragment.showHave();
                    mFurnitureFragment.furnShowHave();
                }
            }
        });

    }

    @Override
    protected void initData() {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.storage_back:
                    finish();
                    break;
                case R.id.storage_commodity_buy_btn:
                    if (isCheck) {
                        if (entity != null) {//&& entity.isUserHadTool()
                            if (!TextUtils.isEmpty(entity.getProductId())) {
                                mPresenter.createOrder(entity.getProductId());
                            } else {
                                showToast("该道具还未上架~~~");
                            }
                        }

                    } else {
                        if (furnitureInfo != null) {
                            if (furnitureInfo.getType().equals("套装")) {//套装
                                if (!furnitureInfo.isUserSuitFurnitureHad()) {
                                    if (TextUtils.isEmpty(furnitureInfo.getFurnitureSuitProductId())) {
                                        showToast("该套装还未上架~~~");
                                    } else {
                                        mPresenter.createOrder(furnitureInfo.getFurnitureSuitProductId());
                                    }
                                } else {
                                    showToast("已拥有该套装~~~");
                                }
                            } else {//单件
                                if (!furnitureInfo.isUserFurnitureHad()) {
                                    if (TextUtils.isEmpty(furnitureInfo.getFurnitureProductId())) {
                                        showToast("该家具还未上架~~~");
                                    } else {
                                        mPresenter.createOrder(furnitureInfo.getFurnitureProductId());
                                    }
                                } else {
                                    showToast("已拥有该家具~~~");
                                }
                            }
                        }
                    }
                    break;
                case R.id.storage_commodity_use_btn:
                    //家具套装的使用
                    if (furnitureInfo != null) {
                        if (furnitureInfo.getType().equals("套装")) {
                            if (furnitureInfo.isUserSuitFurnitureHad()) {//套装是否拥有
                                if (!furnitureInfo.isSuitPutInHouse()) {
                                    mPresenter.suitUse(furnitureInfo.getSuitTypeId(), furnitureInfo.getPosition());
                                } else {
                                    showToast("已经使用中");
                                }
                            } else {
                                showToast("套装未拥有");
                            }
                        } else {
                            //家具是否拥有
                            if (furnitureInfo.isUserFurnitureHad()) {
                                if (!furnitureInfo.isPutInHouse()) {
                                    mPresenter.furnitureUse(furnitureInfo.getId(), furnitureInfo.getPosition());
                                } else {
                                    showToast("已经使用中");
                                }
                            } else {
                                showToast("家具未拥有");
                            }
                        }
                    }
//                    //道具的使用
//                    if (!isUserHadTool) {
//                        showToast("未拥有");
//                    } else {
//                        showToast("使用");
//                        //TODO 道具使用
//                    }
                    break;
                default:
                    break;
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void allFurnitureInfo(AllFurnitureInfo event) {
        if (event != null) {
            furnitureInfo = event;
            if (event.getType().equals("套装")) {
                binding.storageCommodityName.setText(event.getSuitTypeName());
                Glide.with(this).load(ApiService.URL_QINIU + event.getSuitTypeDetailIcon()).into(binding.storageImage);
                binding.storageCommodityInfo.setText(event.getSuitTypeDescribe());

                if (!event.isUserSuitFurnitureHad()) {//未拥有
                    binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_use_null_normal);
                    binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.storage_buy_btn_bg);
                } else {
                    binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.storage_put_btn_bg);
                    binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.btn_home_items_buy__null_normal);
                }

                if (!event.isSuitPutInHouse() && event.isUserSuitFurnitureHad()) {//没有放入宅屋  false
                    binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_use_null_normal);
                    binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.btn_home_items_buy__null_normal);
                } else {
                    binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_putin_null_normal);
                    binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.btn_home_items_buy__null_normal);
                }

                if (event.getFurnitureSuitProductId() != null) {
                    binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.storage_buy_btn_bg);
                    binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_putin_null_normal);
                }

            } else {
                binding.storageCommodityName.setText(event.getName());
                Glide.with(this).load(ApiService.URL_QINIU + event.getDetailIcon()).into(binding.storageImage);
                binding.storageCommodityInfo.setText(event.getDescribe());

                if (!event.isUserFurnitureHad()) {
                    binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_use_null_normal);
                } else {
                    binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.storage_put_btn_bg);
                }
                if (!event.isSuitPutInHouse()) {
                    binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_putin_null_normal);
                } else {
                    binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.storage_put_btn_bg);
                }
                if (event.getFurnitureProductId() != null) {
                    binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.storage_buy_btn_bg);
                    binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_putin_null_normal);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void propInfo(PropInfoEntity propEvent) {
        if (propEvent != null) {
            if (!propEvent.isUserHadTool() && propEvent.getToolCount() == 0) {
                binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.btn_home_items_buy__null_normal);
                binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_use_null_normal);
            } else {
                binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.storage_buy_btn_bg);
                binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.storage_use_btn_bg);
            }

            if (propEvent.getProductId() != null) {
                binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.storage_buy_btn_bg);
                binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_use_null_normal);
            } else {
                binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.btn_home_items_buy__null_normal);
                binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_use_null_normal);
            }

            //拥有
            if (propEvent.getToolCount() != 0) {
//                binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.storage_buy_btn_bg);
                binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.storage_use_btn_bg);
            } else {
                binding.storageCommodityBuyBtn.setBackgroundResource(R.drawable.btn_home_items_buy__null_normal);
                binding.storageCommodityUseBtn.setBackgroundResource(R.drawable.btn_home_items_use_null_normal);
            }
        }
    }

}
