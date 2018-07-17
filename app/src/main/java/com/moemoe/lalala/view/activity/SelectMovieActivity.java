package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerSelectComponents;
import com.moemoe.lalala.di.modules.SelectUpterModule;
import com.moemoe.lalala.model.entity.OrderEntity;
import com.moemoe.lalala.model.entity.PayReqEntity;
import com.moemoe.lalala.model.entity.PayResEntity;
import com.moemoe.lalala.model.entity.VideoInfo;
import com.moemoe.lalala.presenter.SelectUpterContract;
import com.moemoe.lalala.presenter.SelectUpterPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FileManager;
import com.moemoe.lalala.utils.IpAdressUtils;
import com.moemoe.lalala.utils.MusicLoader;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.pingplusplus.ui.PaymentHandler;
import com.pingplusplus.ui.PingppUI;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by yi on 2016/11/30.
 */

public class SelectMovieActivity extends BaseAppCompatActivity implements SelectUpterContract.View {

    public static final String EXTRA_SELECT_MOVIE = "select_movie";

    @BindView(R.id.list)
    ListView mList;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTitle;

    @Inject
    SelectUpterPresenter mPresenter;


    private ArrayList<VideoInfo> mMovieList;
    private BottomMenuFragment bottomFragment;
    private OrderEntity entitPay;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_one_list_samller;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));

        DaggerSelectComponents.builder()
                .selectUpterModule(new SelectUpterModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);


        mMovieList = new ArrayList<>();
        mTitle.setText(R.string.label_select_movie);
        mMovieList.addAll(FileManager.getVideos(getContentResolver()));
        mList.setAdapter(new MovieAdapter());
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setImageResource(R.drawable.btn_back_black_normal);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long bagMaxSize = PreferenceUtils.getBagMaxSize(SelectMovieActivity.this);
                long bagUserSize = PreferenceUtils.getBagUserSize(SelectMovieActivity.this);
                long size = mMovieList.get(position).getSize();
                if (!PreferenceUtils.getAuthorInfo().isVip()) {
                    if (size > bagMaxSize - bagUserSize) {
                        final AlertDialogUtil alertDialogUtilVip = AlertDialogUtil.getInstance();
                        alertDialogUtilVip.createNormalDialog(SelectMovieActivity.this, "需要VIP酱帮助你增加体力上线呢?");
                        alertDialogUtilVip.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                            @Override
                            public void CancelOnClick() {
                                alertDialogUtilVip.dismissDialog();
                            }

                            @Override
                            public void ConfirmOnClick() {
                                alertDialogUtilVip.dismissDialog();
                                mPresenter.createOrder("d61547ce-62c7-4665-993e-81a78cd32976");
                            }
                        });
                        alertDialogUtilVip.showDialog();
                    } else {
                        Intent i = new Intent();
                        i.putExtra(EXTRA_SELECT_MOVIE, mMovieList.get(position));
                        setResult(RESULT_OK, i);
                        finish();
                    }
                } else {
                    Intent i = new Intent();
                    i.putExtra(EXTRA_SELECT_MOVIE, mMovieList.get(position));
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });
    }

    @Override
    protected void initData() {
        initPayMenu();
    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }

    @Override
    public void onCreateOrderSuccess(OrderEntity entity) {
        entitPay = entity;
        if (bottomFragment != null)
            bottomFragment.show(getSupportFragmentManager(), "payMenu");
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
                PayReqEntity entity = new PayReqEntity(entitPay.getAddress().getAddress(),
                        payType,
                        IpAdressUtils.getIpAdress(SelectMovieActivity.this),
                        entitPay.getOrderId(),
                        entitPay.getAddress().getPhone(),
                        "",
                        entitPay.getAddress().getUserName());
                mPresenter.payOrder(entity);
            }
        });
    }

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
                PingppUI.createPay(SelectMovieActivity.this, entity.getCharge().toString(), new PaymentHandler() {
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
                            PreferenceUtils.getAuthorInfo().setVip(true);
                            showToast("支付成功");
                            Intent i = new Intent();
                            i.putExtra("position", -1);
                            i.putExtra("type", "pay");
                            setResult(RESULT_OK, i);
                            finish();
                        } else {
                            showToast(result);
                            finish();
                        }
                    }
                });
            }
        }
    }

    private class MovieAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMovieList.size();
        }

        @Override
        public VideoInfo getItem(int position) {
            return mMovieList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(SelectMovieActivity.this).inflate(R.layout.item_select_music, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = convertView.findViewById(R.id.iv_select_music);
                viewHolder.textView = convertView.findViewById(R.id.tv_select_music);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            VideoInfo musicInfo = getItem(position);
            viewHolder.imageView.setImageResource(R.drawable.icon_file_video);
            viewHolder.textView.setText(musicInfo.getName());
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView textView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
