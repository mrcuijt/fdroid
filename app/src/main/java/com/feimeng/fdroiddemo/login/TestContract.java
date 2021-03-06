package com.feimeng.fdroiddemo.login;

import com.feimeng.fdroid.mvp.base.FDPresenter;
import com.feimeng.fdroid.mvp.base.FDView;

public interface TestContract {
    interface View extends FDView {
        void getUserName(String username);
    }

    abstract class Presenter extends FDPresenter<View> {
        public abstract void getUserName();
    }
}
