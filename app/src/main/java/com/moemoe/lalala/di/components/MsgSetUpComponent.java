package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.MsgSetUpModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.MsgSetUpActivity;

import dagger.Component;

/**
 *
 */
@UserScope
@Component(modules = MsgSetUpModule.class,dependencies = NetComponent.class)
public interface MsgSetUpComponent {
    void inject(MsgSetUpActivity activity);
}
