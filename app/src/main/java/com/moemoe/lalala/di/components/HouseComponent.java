package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.HouseModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.DormitoryActivity;
import com.moemoe.lalala.view.activity.HouseActivity;

import dagger.Component;

/**
 * Created by Hygge on 2018/5/24.
 */
@UserScope
@Component(modules = HouseModule.class, dependencies = NetComponent.class)
public interface HouseComponent {
    void inject(DormitoryActivity activity);
    void inject(HouseActivity activity);
}
