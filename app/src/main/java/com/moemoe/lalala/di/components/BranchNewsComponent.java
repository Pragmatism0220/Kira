package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.BranchNewsModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.fragment.BranchFragment;

import dagger.Component;

/**
 * Created by Administrator on 2018/7/19.
 */

@UserScope
@Component(modules = BranchNewsModule.class, dependencies = NetComponent.class)
public interface BranchNewsComponent {
    void inject(BranchFragment fragment);
}
