package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.BranchInfoModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.BranchInfoActivity;

import dagger.Component;

/**
 * Created by Hygge on 2018/6/13.
 */
@UserScope
@Component(modules = BranchInfoModule.class, dependencies = NetComponent.class)
public interface BranchInfoComponent {
    void inject(BranchInfoActivity activity);
}
