package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.BagVisitorModule;
import com.moemoe.lalala.di.modules.NewUploadModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.BagAllVisitorActivity;
import com.moemoe.lalala.view.fragment.NewMyBagV5Fragment;
import com.moemoe.lalala.view.fragment.NewUploadFragment;

import dagger.Component;

/**
 * Created by Sora on 2018/3/6.
 */
@UserScope
@Component(modules = NewUploadModule.class, dependencies = NetComponent.class)
public interface NewUploadComponent {
    void inject(NewUploadFragment fragment);
}
