package com.feimeng.fdroid.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.feimeng.fdroid.mvp.base.FDPresenter;
import com.feimeng.fdroid.mvp.base.FDView;
import com.feimeng.fdroid.widget.FDialog;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Fragment基类
 * Created by feimeng on 2017/1/20.
 */
public abstract class FDFragment<V extends FDView, P extends FDPresenter<V>> extends RxFragment {
    protected P mPresenter;

    /**
     * 对话框
     */
    private Dialog mLoading;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 绑定控制器
        mPresenter = initPresenter();
        if (mPresenter != null && this instanceof FDView)
            mPresenter.attach((V) this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null && mPresenter.isActive()) mPresenter.init();
    }

    /**
     * 实例化presenter
     */
    public abstract P initPresenter();

    /**
     * 绘制对话框
     * 一般用于网络访问时显示(子类可重写，使用自定义对话框)
     *
     * @param message 提示的信息
     * @return Dialog 对话框
     */
    protected Dialog drawDialog(String message) {
        return new FDialog(getActivity(), message == null ? "" : message);
    }

    public void showLoadingDialog() {
        showLoadingDialog(null);
    }

    /**
     * 显示对话框
     */
    public void showLoadingDialog(String message) {
        if (mLoading == null) mLoading = drawDialog(message);
        mLoading.show();
    }

    /**
     * 隐藏对话框
     */
    public void hideLoadingDialog() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
            mLoading = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideLoadingDialog();
        // 解绑控制器
        if (mPresenter != null) {
            mPresenter.detach();
            mPresenter = null;
        }
    }
}
