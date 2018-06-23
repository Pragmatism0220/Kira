package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.Live2dModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.Live2dActivity;
import com.moemoe.lalala.view.activity.Live3dActivity;

import dagger.Component;

/**
 * Created by yi on 2016/11/27.
 */
@UserScope
@Component(modules = Live2dModule.class,dependencies = NetComponent.class)
public interface Live2dComponent {
    void inject(Live2dActivity activity);
    void inject(Live3dActivity activity);
}
