package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.PrincipalListModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.PrincipalListActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/6/11.
 */
@UserScope
@Component(modules = PrincipalListModule.class, dependencies = NetComponent.class)
public interface PrincipalListComponent {
    void inject(PrincipalListActivity activity);
}
