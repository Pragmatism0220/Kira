package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.REPORT;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.fragment.BagMyFragment;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Sora on 2018/3/15.
 */

public class NewBagV2Activity extends BaseAppCompatActivity {

    @BindView(R.id.fl_laout)
    FrameLayout mFlLayout;
    @BindView(R.id.iv_back)
    ImageView mIVBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.iv_menu_list)
    ImageView mIvMenuList;
    private BagMyFragment mItem1Fragment;
    private String name;
    private BottomMenuFragment bottomFragment;
    private String uuid;
    private String type;


    @Override
    protected int getLayoutId() {
        return R.layout.ac_new_bag_v2;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);


        uuid = getIntent().getStringExtra("uuid");
        name = getIntent().getStringExtra("name");
        type = getIntent().getStringExtra("type");
        String sign;
        if (type.equals("my")){
            sign="my";
        }else {
            sign="taren";
        }
        mItem1Fragment = BagMyFragment.newInstance("my", uuid+"", sign+"");
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.fl_laout, mItem1Fragment);
        mFragmentTransaction.show(mItem1Fragment);
        mFragmentTransaction.commit();
        boolean isSelf = uuid.equals(PreferenceUtils.getUUid());
        if (isSelf) {
            mIvMenuList.setVisibility(View.GONE);
        } else {
            mIvMenuList.setVisibility(View.VISIBLE);
            initPopupMenus(isSelf);
        }

    }

    private void initPopupMenus(boolean isSelf) {
        bottomFragment = new BottomMenuFragment();
        ArrayList<MenuItem> items = new ArrayList<>();
        if (isSelf) {
            MenuItem item = new MenuItem(0, getString(R.string.label_setting));
            items.add(item);

            item = new MenuItem(1, getString(R.string.label_bag_buy_list));
            items.add(item);

            item = new MenuItem(3, getString(R.string.label_add_space));
            items.add(item);

            item = new MenuItem(5, "下载管理");
            items.add(item);
        } else {
            // 举报
            MenuItem item = new MenuItem(4, getString(R.string.label_jubao));
            items.add(item);
        }

        bottomFragment.setShowTop(false);
        bottomFragment.setMenuItems(items);
        bottomFragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        bottomFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                if (itemId == 4) {
                    Intent intent = new Intent(NewBagV2Activity.this, JuBaoActivity.class);
                    intent.putExtra(JuBaoActivity.EXTRA_NAME, "");
                    intent.putExtra(JuBaoActivity.EXTRA_CONTENT, "");
                    intent.putExtra(JuBaoActivity.EXTRA_TYPE, 3);
                    intent.putExtra(JuBaoActivity.UUID, uuid);
                    intent.putExtra("userId", uuid);
                    intent.putExtra(JuBaoActivity.EXTRA_TARGET, REPORT.BAG.toString());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mIvMenuList.setVisibility(View.VISIBLE);
        mIVBack.setVisibility(View.VISIBLE);
        mIVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvTitle.setText(name);
        mIvMenuList.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (bottomFragment != null) bottomFragment.show(getSupportFragmentManager(), "Bag");
            }
        });
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
