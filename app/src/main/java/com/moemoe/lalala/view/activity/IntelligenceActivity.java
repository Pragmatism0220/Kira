package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityIntelligenceBinding;
import com.moemoe.lalala.presenter.BranchInfoContract;
import com.moemoe.lalala.view.base.BaseActivity;

public class IntelligenceActivity extends BaseActivity {
    private ActivityIntelligenceBinding binding;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intelligence);
        binding.setPresenter(new Presenter());
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        String id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        if (name.equals("莲") || id.equals("af3e01f8-0d88-477b-ad53-15481c8a56c6")) {
            binding.ivCoverIntelligence.setImageResource(R.drawable.bg_inform_len);
        } else if (name.equals("沙利尔") || id.equals("7f4ca01d-db86-443d-b637-a814029e874a")) {
            binding.ivCoverIntelligence.setImageResource(R.drawable.bg_inform_sari);
        } else if (name.equals("美藤双树") || id.equals("21a17f3c-55de-424a-8090-c21311d9a327")) {
            binding.ivCoverIntelligence.setImageResource(R.drawable.bg_inform_mei);
        } else if (name.equals("小野抚子") || id.equals("811337cf-6a84-4c4f-a078-8a5c88e13c39")) {
            binding.ivCoverIntelligence.setImageResource(R.drawable.bg_inform_fuzi);
        } else if (name.equals("莓") || id.equals("120ca372-0a7e-4811-8b5a-719c4eef8dc7")) {
            binding.ivCoverIntelligence.setImageResource(R.drawable.bg_inform_ichigo);
        } else if (name.equals("蕾姆") || id.equals("fe141680-62e6-49ee-94d1-71e993d007d5")) {
            binding.ivCoverIntelligence.setImageResource(R.drawable.bg_inform_rem);
        } else {
            finish();
        }
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

    public class Presenter {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_cover_intelligence:
                    finish();
                    break;
            }
        }
    }
}
