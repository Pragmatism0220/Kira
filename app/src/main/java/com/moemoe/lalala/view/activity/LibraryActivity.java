package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.AcLibraryBinding;
import com.moemoe.lalala.di.components.DaggerLibraryComponent;
import com.moemoe.lalala.di.modules.LibraryModule;
import com.moemoe.lalala.model.entity.BannerEntity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.presenter.LibraryContract;
import com.moemoe.lalala.presenter.LibraryPresenter;
import com.moemoe.lalala.utils.BannerImageLoader;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.IntentUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.LibraryFragment;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.moemoe.lalala.view.widget.view.KiraTabLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.moemoe.lalala.utils.StartActivityConstant.REQ_FILE_UPLOAD;

/**
 * 图书馆
 * Created by Hygge on 2018/7/17.
 */

public class LibraryActivity extends BaseActivity implements LibraryContract.View {

    private AcLibraryBinding binding;
    private TabFragmentPagerAdapter mAdapter;
    private Banner banner;

    private BottomMenuFragment fragment;

    @Inject
    LibraryPresenter mPresenter;
    private String roomId;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.ac_library);
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        binding.setPresenter(new Presenter());
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerLibraryComponent.builder()
                .libraryModule(new LibraryModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        banner = findViewById(R.id.banner);
        initMenu();
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        roomId = getIntent().getStringExtra(UUID);
        mPresenter.requestBannerData(roomId);
    }

    @Override
    protected void initListeners() {
        binding.indicatorPersonData.setmTabClick(new KiraTabLayout.OnTabViewClickListener() {
            @Override
            public void onTabClick(View tabView, int position) {
                if (position == 3) {
                    binding.ivToWen.setVisibility(View.GONE);
                } else {
                    binding.ivToWen.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void initData() {


        List<BaseFragment> fragmentList = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        titles.add("漫画");
        titles.add("图集");
        titles.add("小说");
        titles.add("历史记录");

        for (int i = 0; i < titles.size(); i++) {
            String mFolderType;
            if (titles.get(i).equals("漫画")) {
                mFolderType = FolderType.MH.toString();
            } else if (titles.get(i).equals("图集")) {
                mFolderType = FolderType.TJ.toString();
            } else if (titles.get(i).equals("小说")) {
                mFolderType = FolderType.XS.toString();
            } else {
                mFolderType = "历史记录";
            }
            fragmentList.add(LibraryFragment.newInstance(mFolderType));
        }
        if (mAdapter == null) {
            mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        } else {
            mAdapter.setFragments(getSupportFragmentManager(), fragmentList, titles);
        }
        binding.viewPager.setAdapter(mAdapter);
        binding.indicatorPersonData.setViewPager(binding.viewPager);
        binding.viewPager.setCurrentItem(0);
    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }

    @Override
    public void onBannerLoadSuccess(final ArrayList<BannerEntity> bannerEntities) {
        if (bannerEntities.size() > 0) {
            banner.setVisibility(View.VISIBLE);
            banner.setImages(bannerEntities)
                    .setImageLoader(new BannerImageLoader())
                    .setDelayTime(2000)
                    .setIndicatorGravity(BannerConfig.CENTER)
                    .start();
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    BannerEntity bean = bannerEntities.get(position);
                    if (!TextUtils.isEmpty(bean.getSchema())) {
                        Uri uri = Uri.parse(bean.getSchema());
                        IntentUtils.toActivityFromUri(LibraryActivity.this, uri, null);
                    }
                }
            });
        } else {
            banner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadLibraryListSuccess(ArrayList<ShowFolderEntity> entities, boolean isPull) {

    }

    @Override
    public void onLoadLibraryNewestReadhistorySuccess(ArrayList<ShowFolderEntity> entities, boolean isPull) {

    }

    private void initMenu() {
        if (fragment == null) {
            fragment = new BottomMenuFragment();
        }

        ArrayList<MenuItem> items = new ArrayList<>();

        MenuItem item = new MenuItem(1, "漫画");
        items.add(item);
        item = new MenuItem(2, "图集");
        items.add(item);
        item = new MenuItem(3, "小说");
        items.add(item);

        fragment.setShowTop(false);
        fragment.setMenuItems(items);
        fragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        fragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                String mFolderType = FolderType.MH.toString();
                if (itemId == 1) {
                    mFolderType = FolderType.MH.toString();
                } else if (itemId == 2) {
                    mFolderType = FolderType.TJ.toString();
                } else if (itemId == 3) {
                    mFolderType = FolderType.XS.toString();
                }

                FilesUploadActivity.startActivityForResult(LibraryActivity.this, mFolderType, "", "", false, "submission", roomId);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_FILE_UPLOAD && resultCode == RESULT_OK) {
            // TODO: 2018/7/18 条目的刷新 
        }
    }

    public class Presenter {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_left:
                    finish();
                    break;
                case R.id.iv_right:

                    break;
                case R.id.ll_search:
                    Intent i3 = new Intent(LibraryActivity.this, AllSearchActivity.class);
                    i3.putExtra("type", "folder");
                    startActivity(i3);
                    break;
                case R.id.iv_to_wen:
                    if (fragment != null) fragment.show(getSupportFragmentManager(), "library");
                    break;
            }
        }
    }
}
