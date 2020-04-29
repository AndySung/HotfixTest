package com.soft.nortek.hotfixtest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.crashreport.CrashReport;

public class MainActivity extends AppCompatActivity {
    private Button btnCheckUpdate,patch_test;
    private Button btnLoadUpgradeInfo,ToastBtn,ToastMsg;
    private TextView tvUpgradeInfo,testInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCheckUpdate = findViewById(R.id.btnCheckUpdate);
        btnLoadUpgradeInfo = findViewById(R.id.btnLoadUpdateInfo);
        tvUpgradeInfo = findViewById(R.id.tvUpgradeInfo);
        ToastMsg = findViewById(R.id.secondTest);
        testInfo = findViewById(R.id.textView1);
        ToastBtn = findViewById(R.id.toast);
        patch_test = findViewById(R.id.patch_test);

        testInfo.setText("我是第二次修复的bug");

        ToastMsg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"在第一个基线包上第二次修复BUG",Toast.LENGTH_SHORT).show();
            }
        });
        patch_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Beta.applyTinkerPatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
            }
        });
        ToastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testToast();
            }
        });
        btnCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Beta.checkUpgrade();
            }
        });

        btnLoadUpgradeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUpgradeInfo();
            }
        });
    }

    /**
     * 根据应用patch包前后来测试是否应用patch包成功.
     *
     * 应用patch包前，提示"This is a bug class"
     * 应用patch包之后，提示"The bug has fixed"
     */
    public void testToast() {
        Toast.makeText(this, LoadBugClass.getBugString(), Toast.LENGTH_SHORT).show();
    }

    private void loadUpgradeInfo() {
        if (tvUpgradeInfo == null)
            return;

        /***** 获取升级信息 *****/
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();

        if (upgradeInfo == null) {
            tvUpgradeInfo.setText("无升级信息");
            return;
        }

        StringBuilder info = new StringBuilder();
        info.append("id: ").append(upgradeInfo.id).append("\n");
        info.append("标题: ").append(upgradeInfo.title).append("\n");
        info.append("升级说明: ").append(upgradeInfo.newFeature).append("\n");
        info.append("versionCode: ").append(upgradeInfo.versionCode).append("\n");
        info.append("versionName: ").append(upgradeInfo.versionName).append("\n");
        info.append("发布时间: ").append(upgradeInfo.publishTime).append("\n");
        info.append("安装包Md5: ").append(upgradeInfo.apkMd5).append("\n");
        info.append("安装包下载地址: ").append(upgradeInfo.apkUrl).append("\n");
        info.append("安装包大小: ").append(upgradeInfo.fileSize).append("\n");
        info.append("弹窗间隔（ms）: ").append(upgradeInfo.popInterval).append("\n");
        info.append("弹窗次数: ").append(upgradeInfo.popTimes).append("\n");
        info.append("发布类型（0:测试 1:正式）: ").append(upgradeInfo.publishType).append("\n");
        info.append("弹窗类型（1:建议 2:强制 3:手工）: ").append(upgradeInfo.upgradeType);

        tvUpgradeInfo.setText(info);
    }

    public void testJavaCrash(View view){
        CrashReport.testJavaCrash();
    }

    public void testNativeCrash(View view){
        CrashReport.testNativeCrash();
    }

    public void testANR(View view){
        CrashReport.testANRCrash();
    }


    /**
     * 获取当前版本.
     *
     * @param context 上下文对象
     * @return 返回当前版本
     */
    public String getCurrentVersion(Context context) {
        try {
            PackageInfo packageInfo =
                    context.getPackageManager().getPackageInfo(this.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            return versionName + "." + versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }
}
