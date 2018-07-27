package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.NewFileXiaoShuoModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.NewFileXiaoShuo2Activity;

import dagger.Component;

/**
 * Created by yi on 2016/11/27.
 */
@UserScope
@Component(modules = NewFileXiaoShuoModule.class,dependencies = NetComponent.class)
public interface NewFolderXiaoShuoComponent {
    void inject(NewFileXiaoShuo2Activity fragment);
}
