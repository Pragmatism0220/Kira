package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.NewSettingModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.NewSettingActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/6/27.
 */
@UserScope
@Component(modules = NewSettingModule.class, dependencies = NetComponent.class)
public interface NewSettingComponent {
    void inject(NewSettingActivity activity);
}
