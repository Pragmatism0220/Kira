package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.RoleModule;
import com.moemoe.lalala.di.modules.StorageModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.StorageActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/6/2.
 */
@UserScope
@Component(modules = StorageModule.class, dependencies = NetComponent.class)
public interface StorageComponents {
    void inject(StorageActivity activity);
}
