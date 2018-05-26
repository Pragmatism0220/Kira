package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.moemoe.lalala.R;

import butterknife.BindView;

/**
 * Created by Hygge on 2018/5/9.
 */

public class RoleFragment extends BaseFragment {

    @BindView(R.id.tv_name)
    TextView mTvName;

    public static RoleFragment newInstance(String roleName) {
        RoleFragment fragment = new RoleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", roleName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_role;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        String name = getArguments().getString("name");
        mTvName.setText(name);
    }
}
