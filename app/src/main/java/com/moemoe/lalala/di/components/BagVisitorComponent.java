package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.BagVisitorModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.BagAllVisitorActivity;
import com.moemoe.lalala.view.fragment.NewMyBagV5Fragment;

import dagger.Component;

/**
 * Created by Sora on 2018/3/6.
 */
@UserScope
@Component(modules = BagVisitorModule.class, dependencies = NetComponent.class)
public interface BagVisitorComponent {
    void inject(NewMyBagV5Fragment fragment);
    void inject(BagAllVisitorActivity activity);
}
