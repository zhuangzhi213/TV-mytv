package com.zhuangzhi.mytv.tv

import android.app.Application
import com.zhuangzhi.mytv.core.data.AppData

class MyTVApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AppData.init(applicationContext)
        UnsafeTrustManager.enableUnsafeTrustManager()
    }
}
