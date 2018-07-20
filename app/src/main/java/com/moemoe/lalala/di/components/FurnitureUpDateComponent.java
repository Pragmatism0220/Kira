package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.FurnitureUpDateMoudle;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.fragment.FurnitureInfoFragment;

import dagger.Component;

/**
 * Created by Administrator on 2018/7/19.
 */
@UserScope
@Component(modules = FurnitureUpDateMoudle.class, dependencies = NetComponent.class)
public interface FurnitureUpDateComponent {
    void inject(FurnitureInfoFragment furnitureInfoFragment);
}
