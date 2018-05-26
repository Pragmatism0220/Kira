package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.NoticeModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.fragment.NoticeChildFragment;

import dagger.Component;

/**
 * Created by Sora on 2018/3/24.
 */
@UserScope
@Component(modules = NoticeModule.class,dependencies = NetComponent.class)
public interface NoticeComponent {
    void inject(NoticeChildFragment fragment);
}
