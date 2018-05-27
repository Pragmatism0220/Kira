//package com.moemoe.lalala.view.activity;
//
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.moemoe.lalala.R;
//import com.moemoe.lalala.databinding.ActivityBranchBinding;
//import com.moemoe.lalala.view.adapter.BranchFragmentAdapter;
//import com.moemoe.lalala.view.base.BaseActivity;
//import com.moemoe.lalala.view.fragment.BranchFragment;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * 支线剧情页面
// * by zhangyan
// */
//
//public class BranchActivity extends BaseActivity {
//
//    private ActivityBranchBinding binding;
//    private List<Fragment> fragments = new ArrayList<>();
//    private BranchFragmentAdapter mAdapter;
//    private String[] title = {"全部", "美藤双树", "沙利尔", "美藤双树"};
//
//    @Override
//    protected void initComponent() {
//        binding = DataBindingUtil.setContentView(this,R.layout.activity_branch);
////        binding.setPresenter(new Presenter());
//        BranchFragment fragment1 = new BranchFragment();
//        BranchFragment fragment2 = new BranchFragment();
//        BranchFragment fragment3 = new BranchFragment();
//        BranchFragment fragment4 = new BranchFragment();
//        fragments.add(fragment1);
//        fragments.add(fragment2);
//        fragments.add(fragment3);
//        fragments.add(fragment4);
//
//        mAdapter = new BranchFragmentAdapter(getSupportFragmentManager(), fragments);
//        binding.branchViewpager.setAdapter(mAdapter);
//        binding.branchTabLayout.setupWithViewPager(binding.branchViewpager);
//
//        for (int i = 0; i < mAdapter.getCount(); i++) {
//            TabLayout.Tab tab = binding.branchTabLayout.getTabAt(i);
//            tab.setCustomView(R.layout.item_table_view);
//            if (i == 0) {
//                tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);
//            }
//            TextView textView = tab.getCustomView().findViewById(R.id.tab_text);
//            textView.setText(title[i]);
//        }
//        binding.branchTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);
//                binding.branchViewpager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.getCustomView().findViewById(R.id.tab_text).setSelected(false);
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//    }
//
//    @Override
//    protected void initViews(Bundle savedInstanceState) {
//
//    }
//
//    @Override
//    protected void initToolbar(Bundle savedInstanceState) {
//
//    }
//
//    @Override
//    protected void initListeners() {
//
//    }
//
//    @Override
//    protected void initData() {
//
//    }
//
//
//    public class Presenter {
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.check_box_btn:
//                    if (binding.checkBoxBtn.isChecked()) {
//                        Toast.makeText(getApplicationContext(), "未拥有", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "已拥有", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case R.id.btn_back:
//                    finish();
//                    break;
//                default:
//                    break;
//            }
//        }
//
//    }
//}
