package com.zhuangzhi.mytv.core.data.repositories.epg.fetcher

import okhttp3.Response

/**
 * 缺省节目单数据获取
 */
class DefaultEpgFetcher : EpgFetcher {
    override fun isSupport(url: String): Boolean {
        return true
    }

    override suspend fun fetch(response: Response): String {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    }
}