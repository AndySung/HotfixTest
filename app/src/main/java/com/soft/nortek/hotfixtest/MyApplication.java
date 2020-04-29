package com.soft.nortek.hotfixtest;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;


/**
 * Created by wenjiewu on 2016/5/23.
 */
public class MyApplication extends TinkerApplication {

    public MyApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL,"com.soft.nortek.hotfixtest.MyApplicationLike","com.tencent.tinker.loader.TinkerLoader",false);
    }
}
