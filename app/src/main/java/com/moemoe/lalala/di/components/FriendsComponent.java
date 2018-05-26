package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.FriendsModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.FriendsActivity;

import dagger.Component;

/**
 * Created by Sora on 2018/4/2.
 */
@UserScope
@Component(modules = FriendsModule.class, dependencies = NetComponent.class)
public interface FriendsComponent {
    void inject(FriendsActivity activity);
}
