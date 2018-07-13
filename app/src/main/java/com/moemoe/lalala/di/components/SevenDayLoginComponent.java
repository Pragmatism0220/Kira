package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.SevenDayLoginModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.SevenDayLoginActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/7/12.
 */
@UserScope
@Component(modules = SevenDayLoginModule.class, dependencies = NetComponent.class)
public interface SevenDayLoginComponent {
    void inject(SevenDayLoginActivity activity);
}
