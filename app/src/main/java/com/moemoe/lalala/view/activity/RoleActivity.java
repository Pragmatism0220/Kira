//package com.moemoe.lalala.view.activity;
//
//import android.databinding.DataBindingUtil;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.GridLayoutManager;
//import android.view.View;
//import android.widget.Switch;
//import android.widget.Toast;
//
//
//import com.moemoe.lalala.R;
//import com.moemoe.lalala.databinding.ActivityRoleBinding;
//import com.moemoe.lalala.event.OnItemListener;
//import com.moemoe.lalala.service.TestRole;
//import com.moemoe.lalala.view.adapter.RoleAdapter;
//import com.moemoe.lalala.view.base.BaseActivity;
//import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * 角色界面
// * Created by zhangyan on 2018/5/7.
// */
//
//public class RoleActivity extends BaseActivity {
//
//    private RoleAdapter mAdapter;
//    private ActivityRoleBinding binding;
//    private ArrayList<TestRole> roles;
//    private int roleHeartCount = 72;
//
//
//    @Override
//    protected void initComponent() {
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_role);
////        binding.setPresenter(new Presenter());
//        getData();
//        initAdapter();
//    }
//
//    private void initAdapter() {
//        mAdapter = new RoleAdapter(roles, this);
//        binding.roleListRv.setLayoutManager(new GridLayoutManager(this, 3));
//        int rightSpace = 12;
//        int bottomSpace = 12;
//        int top = 12;
//        binding.roleListRv.addItemDecoration(new SpacesItemDecoration(rightSpace, bottomSpace, top));
//        binding.roleListRv.setAdapter(mAdapter);
//
//        mAdapter.setOnItemClickListener(new OnItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(getApplicationContext(), "点击" + roles.get(position).getmName() + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
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
//        binding.roleHeartCount.setText("(" + roleHeartCount + ")");
//
//    }
//
//    public List<TestRole> getData() {
//        roles = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            roles.add(new TestRole());
//        }
//        return roles;
//    }
//
//
//    public class Presenter {
//
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.role_back_btn:
//                    finish();
//                    break;
//                case R.id.put_house_btn:
//                    Toast.makeText(getApplicationContext(), "放入宅屋", Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.set_deskmake_btn:
//                    Toast.makeText(getApplicationContext(), "设为同桌", Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.role_cloth_btn:
//                    Toast.makeText(getApplicationContext(), "服装", Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    break;
//            }
//
//        }
//
//    }
//
//}
