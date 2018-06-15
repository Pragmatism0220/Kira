package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.BranchModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.BranchActivity;
import com.moemoe.lalala.view.activity.CompoundActivity;

import dagger.Component;

/**
 * Created by Hygge on 2018/6/12.
 */
@UserScope
@Component(modules = BranchModule.class,dependencies = NetComponent.class)
public interface BranchComponent {
    void inject(BranchActivity activity);
    void inject(CompoundActivity activity);
}
