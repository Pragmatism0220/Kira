package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.RoleModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.RoleActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/5/25.
 */

@UserScope
@Component(modules = RoleModule.class, dependencies = NetComponent.class)
public interface RolComponent {
    void inject(RoleActivity activity);
}
