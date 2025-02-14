package com.zhuangzhi.mytv.data.entities

import androidx.compose.runtime.Immutable

import java.util.concurrent.atomic.AtomicInteger

object IdGenerator {
    private val counter = AtomicInteger(-1)

    fun generateId(): String = counter.incrementAndGet().toString()
}

/**
 * 直播源
 */
@Immutable
data class Iptv(
    /**
     * 直播源名称
     */
    val name: String = "",

    /**
     * 直播源ID
     */
    val id: String = IdGenerator.generateId(), // 使用序号生成 ID

    /**
     * 频道名称，用于查询节目单
     */
    val channelName: String = "",

    /**
     * 播放地址
     */
    val urlList: List<String> = emptyList(),
) {
    companion object {
        val EXAMPLE = Iptv(
            name = "CCTV-1",
            channelName = "cctv1",
            urlList = listOf(
                "http://dbiptv.sn.chinamobile.com/PLTV/88888890/224/3221226231/index.m3u8",
                "http://[2409:8087:5e01:34::20]:6610/ZTE_CMS/00000001000000060000000000000131/index.m3u8?IAS",
            ),
        )
    }
}