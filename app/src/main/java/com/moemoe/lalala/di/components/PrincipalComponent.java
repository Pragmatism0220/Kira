package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.PrincipalModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.PrActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/6/11.
 */
@UserScope
@Component(modules = PrincipalModule.class, dependencies = NetComponent.class)
public interface PrincipalComponent {
    void inject(PrActivity activity);
}
