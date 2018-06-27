package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.EditMyInfoModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.EditMyInfoActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/6/27.
 */
@UserScope
@Component(modules = EditMyInfoModule.class, dependencies = NetComponent.class)
public interface EditMyInfoComponent {
    void inject(EditMyInfoActivity activity);
}
