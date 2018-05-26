package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.KiraVideoModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.KiraMusicActivity;
import com.moemoe.lalala.view.activity.KiraVideoActivity;
import com.moemoe.lalala.view.activity.MusicDetailActivityV2;

import dagger.Component;

/**
 *
 * Created by yi on 2016/11/27.
 */
@UserScope
@Component(modules = KiraVideoModule.class,dependencies = NetComponent.class)
public interface KiraVideoComponent {
    void inject(KiraVideoActivity activity);
    void inject(KiraMusicActivity activity);
    void inject(MusicDetailActivityV2 activity);
}
