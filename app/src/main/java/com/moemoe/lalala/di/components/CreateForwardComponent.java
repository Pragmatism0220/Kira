package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.AddAddressModule;
import com.moemoe.lalala.di.modules.CreateForwardModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.AddAddressActivity;
import com.moemoe.lalala.view.activity.CreateForwardActivity;
import com.moemoe.lalala.view.activity.CreateForwardV2Activity;

import dagger.Component;

/**
 * Created by yi on 2016/11/27.
 */
@UserScope
@Component(modules = CreateForwardModule.class,dependencies = NetComponent.class)
public interface CreateForwardComponent {
    void inject(CreateForwardActivity activity);
    void inject(CreateForwardV2Activity activity);
}
