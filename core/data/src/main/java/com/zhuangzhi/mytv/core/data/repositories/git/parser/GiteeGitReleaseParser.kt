package com.zhuangzhi.mytv.core.data.repositories.git.parser

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import com.zhuangzhi.mytv.core.data.entities.git.GitRelease

/**
 * gitee发行版解析
 */
class GiteeGitReleaseParser : GitReleaseParser {
    override fun isSupport(url: String): Boolean {
        return url.contains("gitee.com")
    }

    override suspend fun parse(data: String): GitRelease {
        val json = Json.parseToJsonElement(data).jsonObject

        return GitRelease(
            version = json.getValue("tag_name").jsonPrimitive.content.substring(1),
            downloadUrl = json.getValue("assets").jsonArray[0].jsonObject["browser_download_url"]!!.jsonPrimitive.content,
            description = json.getValue("body").jsonPrimitive.content,
        )
    }
}