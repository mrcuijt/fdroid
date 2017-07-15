package com.feimeng.fdroid.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.feimeng.fdroid.base.support.RxActivity;
import com.feimeng.fdroid.mvp.base.FDPresenter;
import com.feimeng.fdroid.mvp.base.FDView;
import com.feimeng.fdroid.utils.ActivityPageManager;
import com.feimeng.fdroid.widget.FDialog;

/**
 * Activity基类
 * Created by feimeng on 2017/1/20.
 */
public abstract class FDActivity<V extends FDView, P extends FDPresenter<V>> extends RxActivity {
    protected P mPresenter;

    /**
     * 页面布局的 根view
     */
    protected View mContentView;

    /**
     * 对话框
     */
    private Dialog mLoading;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Activity管理
        ActivityPageManager.getInstance().addActivity(this);
        // 绑定控制器
        mPresenter = initPresenter();
        if (mPresenter != null && this instanceof FDView)
            mPresenter.attach((V) this);
    }

    /**
     * 实例化控制器
     */
    protected abstract P initPresenter();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(view);
        if (mPresenter != null && mPresenter.isActive()) mPresenter.init();
    }


    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mContentView = view;
    }

    /**
     * 绘制对话框
     * 一般用于网络访问时显示(子类可重写，使用自定义对话框)
     *
     * @param message 提示的信息
     * @return Dialog 对话框
     */
    protected Dialog drawDialog(String message) {
        return new FDialog(this, message);
    }

    /**
     * 显示对话框
     */
    public void showLoadingDialog(String message) {
        if (mLoading == null) {
            mLoading = drawDialog(message);
        }
        if (!mLoading.isShowing())
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


    /**
     * 拿到最新Activity
     *
     * @return BaseActivity
     */
    public static FDActivity getLatestActivity() {
        return ActivityPageManager.getInstance().currentActivity();
    }

    /**
     * 结束所有Activity
     */
    public static void finishAll() {
        ActivityPageManager.getInstance().finishAllActivity();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoadingDialog();
        // 解绑控制器
        if (mPresenter != null) mPresenter.detach();
        else
            mPresenter = null;
        // 移除Activity管理
        ActivityPageManager.unbindReferences(mContentView);
        ActivityPageManager.getInstance().removeActivity(this);
        // 内容布局置空
        mContentView = null;
    }
}