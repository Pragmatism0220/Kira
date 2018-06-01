package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.FurnitureModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.fragment.FurnitureFragment;

import dagger.Component;

/**
 * Created by Administrator on 2018/5/30.
 */
@UserScope
@Component(modules = FurnitureModule.class, dependencies = NetComponent.class)
public interface FurnitureComponent {
    void inject(FurnitureFragment furnitureFragment);
}
