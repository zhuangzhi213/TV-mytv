package com.zhuangzhi.mytv.core.data

import android.content.Context
import com.zhuangzhi.mytv.core.data.utils.Globals
import com.zhuangzhi.mytv.core.data.utils.SP

object AppData {
    fun init(context: Context) {
        Globals.cacheDir = context.cacheDir
        SP.init(context)
    }
}