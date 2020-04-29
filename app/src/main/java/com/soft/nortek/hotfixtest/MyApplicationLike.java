package com.soft.nortek.hotfixtest;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.multidex.MultiDex;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.entry.DefaultApplicationLike;


/**
 * Created by wenjiewu on 2016/5/23.
 */
public class MyApplicationLike extends DefaultApplicationLike {

    public MyApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }


    /*public static final String APP_ID = "fd448ff4b0"; // TODO 替换成bugly上注册的appid
    public static final String APP_CHANNEL = "DEBUG"; // TODO 自定义渠道
    private static final String TAG = "OnUILifecycleListener";*/

    @Override
    public void onCreate() {
        super.onCreate();
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        Bugly.init(getApplication(), "fd448ff4b0", true);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }


   /* @Override
    public void onCreate() {
        super.onCreate();

        *//**** Beta高级设置*****//*
        *//**
         * true表示app启动自动初始化升级模块；
         * false不好自动初始化
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false
         * 在后面某个时刻手动调用
         *//*
        Beta.autoInit = true;

        *//**
         * true表示初始化时自动检查升级
         * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
         *//*
        Beta.autoCheckUpgrade = true;

        *//**
         * 设置升级周期为60s（默认检查周期为0s），60s内SDK不重复向后天请求策略
         *//*
        Beta.initDelay = 1 * 1000;

        *//**
         * 设置通知栏大图标，largeIconId为项目中的图片资源；
         *//*
        Beta.largeIconId = R.mipmap.ic_launcher;

        *//**
         * 设置状态栏小图标，smallIconId为项目中的图片资源id;
         *//*
        Beta.smallIconId = R.mipmap.ic_launcher;


        *//**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         *//*
        Beta.defaultBannerId = R.mipmap.ic_launcher;

        *//**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         *//*
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        *//**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         *//*
        Beta.showInterruptedStrategy = false;

        *//**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         *//*
        Beta.canShowUpgradeActs.add(MainActivity.class);


        *//**
         *  设置自定义升级对话框UI布局
         *  注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：
         *  标题：beta_title，如：android:tag="beta_title"
         *  升级信息：beta_upgrade_info  如： android:tag="beta_upgrade_info"
         *  更新属性：beta_upgrade_feature 如： android:tag="beta_upgrade_feature"
         *  取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
         *  确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
         *  详见layout/upgrade_dialog.xml
         *//*
//        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;

        *//**
         * 设置自定义tip弹窗UI布局
         * 注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：
         *  标题：beta_title，如：android:tag="beta_title"
         *  提示信息：beta_tip_message 如： android:tag="beta_tip_message"
         *  取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
         *  确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
         *  详见layout/tips_dialog.xml
         *//*
//        Beta.tipsDialogLayoutId = R.layout.tips_dialog;

        *//**
         *  如果想监听升级对话框的生命周期事件，可以通过设置OnUILifecycleListener接口
         *  回调参数解释：
         *  context - 当前弹窗上下文对象
         *  view - 升级对话框的根布局视图，可通过这个对象查找指定view控件
         *  upgradeInfo - 升级信息
         *//*
     *//*  Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
            @Override
            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onCreate");
                // 注：可通过这个回调方式获取布局的控件，如果设置了id，可通过findViewById方式获取，如果设置了tag，可以通过findViewWithTag，具体参考下面例子:

                // 通过id方式获取控件，并更改imageview图片
                ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
                imageView.setImageResource(R.mipmap.ic_launcher);

                // 通过tag方式获取控件，并更改布局内容
                TextView textView = (TextView) view.findViewWithTag("textview");
                textView.setText("my custom text");

                // 更多的操作：比如设置控件的点击事件
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), OtherActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStart");
            }

            @Override
            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onResume");
            }

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onPause");
            }

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStop");
            }

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onDestory");
            }
        };*//*

        *//**
         * 自定义Activity参考，通过回调接口来跳转到你自定义的Actiivty中。
         *//*
       *//* Beta.upgradeListener = new UpgradeListener() {

            @Override
            public void onUpgrade(int ret, UpgradeInfo strategy, boolean isManual, boolean isSilence) {
                if (strategy != null) {
                    Intent i = new Intent();
                    i.setClass(getApplicationContext(), UpgradeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "没有更新", Toast.LENGTH_SHORT).show();
                }
            }
        };*//*

        //监听安装包下载状态
        Beta.downloadListener = new DownloadListener() {
            @Override
            public void onReceive(DownloadTask downloadTask) {
                Log.d(TAG,"downloadListener receive apk file");
            }

            @Override
            public void onCompleted(DownloadTask downloadTask) {
                Log.d(TAG,"downloadListener download apk file success");
            }

            @Override
            public void onFailed(DownloadTask downloadTask, int i, String s) {
                Log.d(TAG,"downloadListener download apk file fail");
            }
        };

        //监听APP升级状态
        Beta.upgradeStateListener = new UpgradeStateListener(){
            @Override
            public void onUpgradeFailed(boolean b) {
                Log.d(TAG,"upgradeStateListener upgrade fail");
            }

            @Override
            public void onUpgradeSuccess(boolean b) {
                Log.d(TAG,"upgradeStateListener upgrade success");
            }

            @Override
            public void onUpgradeNoVersion(boolean b) {
                Log.d(TAG,"upgradeStateListener upgrade has no new version");
            }

            @Override
            public void onUpgrading(boolean b) {
                Log.d(TAG,"upgradeStateListener upgrading");
            }

            @Override
            public void onDownloadCompleted(boolean b) {
                Log.d(TAG,"upgradeStateListener download apk file success");
            }
        };

        *//**
         * 已经接入Bugly用户改用上面的初始化方法,不影响原有的crash上报功能;
         * init方法会自动检测更新，不需要再手动调用Beta.checkUpdate(),如需增加自动检查时机可以使用Beta.checkUpdate(false,false);
         * 参数1： applicationContext
         * 参数2：appId
         * 参数3：是否开启debug(调试时，将第三个参数改为true)
         *
         *//*
        Bugly.init(getApplication(), APP_ID, true);

        *//**
         * 如果想自定义策略，按照如下方式设置
         *//*

        *//***** Bugly高级设置 *****//*
        //        BuglyStrategy strategy = new BuglyStrategy();
        *//**
         * 设置app渠道号
         *//*
        //        strategy.setAppChannel(APP_CHANNEL);

        //        Bugly.init(getApplicationContext(), APP_ID, true, strategy);

    }*/
}
