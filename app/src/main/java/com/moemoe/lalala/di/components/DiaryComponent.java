package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.DiaryModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.DiaryActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/7/5.
 */
@UserScope
@Component(modules = DiaryModule.class, dependencies = NetComponent.class)
public interface DiaryComponent {
    void inject(DiaryActivity activity);
}
