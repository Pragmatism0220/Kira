package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.PropModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.fragment.PropFragment;

import dagger.Component;

/**
 * Created by zhangyan on 2018/5/29.
 */

@UserScope
@Component(modules = PropModule.class, dependencies = NetComponent.class)
public interface PropComponent {
    void inject(PropFragment propFragment);
}
