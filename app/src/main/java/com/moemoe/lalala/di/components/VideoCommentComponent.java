package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.VideoCommentModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.fragment.VideoCommentFragment;

import dagger.Component;

/**
 *
 * Created by yi on 2016/11/27.
 */
@UserScope
@Component(modules = VideoCommentModule.class,dependencies = NetComponent.class)
public interface VideoCommentComponent {
    void inject(VideoCommentFragment activity);
}
