package com.bsoft.baselib.core;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.cyc.eventbus.subscriberindexdemo.MyEventBusIndex;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.yjhealth.libs.core.R;
import com.yjhealth.libs.core.constants.CoreConstants;
import com.yjhealth.libs.core.log.CoreLogTag;
import com.yjhealth.libs.core.log.LogUtil;
import com.yjhealth.libs.core.shapref.CoreSharpref;
import com.yjhealth.libs.core.view.refreshlayout.DynamicTimeFormat;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by chenkai on 2018/6/19.
 */

public class CoreAppInit {
    private static Application application;
    private boolean isInit;

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.yjhealth_core_bg, R.color.yjhealth_core_text_sub);
                return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    private static class CoreAppInitHolder {
        private static final CoreAppInit INSTANCE = new CoreAppInit();
    }

    public static CoreAppInit getInstance() {
        return CoreAppInit.CoreAppInitHolder.INSTANCE;
    }

    public static Application getApplication() {
        return application;
    }

    public boolean isInit() {
        return isInit;
    }

    /**
     * 如果已经init，则取消
     *
     * @param app
     */
    public void init(Application app) {
        if (!isInit) {
            forceInit(app);
            isInit = true;
        }
    }

    /**
     * 强制init
     *
     * @param app
     */
    public void forceInit(Application app) {
        application = app;
        CoreConstants.isDebug = CoreSharpref.getInstance().getDebug();
        LogUtil.init();

        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
        Fresco.initialize(app);
        QMUISwipeBackActivityManager.init(app);

        Log.d(CoreLogTag.TAG, "yunxin CoreConstants.isDebug=" + CoreConstants.isDebug);

        isInit = true;
    }
}
