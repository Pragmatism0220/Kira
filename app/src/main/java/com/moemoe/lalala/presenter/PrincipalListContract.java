package com.moemoe.lalala.presenter;

import com.moemoe.lalala.model.entity.NewStoryInfo;
import com.moemoe.lalala.model.entity.NewStoryJsonInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/11.
 */

public interface PrincipalListContract {
    interface Presenter extends BasePresenter {
        void getPrincipalListInfo(String id);

        void playStory(String scriptId);
    }

    interface View extends BaseView {
        void getPrincipalListInfoSuccess(ArrayList<NewStoryInfo> newStoryInfos);

        void playStorySuccess(NewStoryJsonInfo newStoryJsonInfo);
    }
}
