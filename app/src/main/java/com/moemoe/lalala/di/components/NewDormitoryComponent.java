package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.NewDormitorModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.presenter.NewDormitioryContract;
import com.moemoe.lalala.view.activity.DormitoryDramaActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/6/11.
 */
@UserScope
@Component(modules = NewDormitorModule.class, dependencies = NetComponent.class)
public interface NewDormitoryComponent {
    void inject(DormitoryDramaActivity activity);
}
