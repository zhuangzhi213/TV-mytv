<<<<<<<< HEAD:app/src/main/java/top/yogiczy/mytv/utils/Downloader.kt
package com.zhuangzhi.mytv.utils
========
package com.zhuangzhi.mytv.core.util.utils
>>>>>>>> ee27a07f525f5a5f2b5114240b2ba6bfabe66f88:core/util/src/main/java/top/yogiczy/mytv/core/util/utils/Downloader.kt

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okio.BufferedSource
import okio.ForwardingSource
import okio.buffer
import java.io.File
import java.io.FileOutputStream

<<<<<<<< HEAD:app/src/main/java/top/yogiczy/mytv/utils/Downloader.kt
object Downloader : Loggable() {
========
object Downloader {
>>>>>>>> ee27a07f525f5a5f2b5114240b2ba6bfabe66f88:core/util/src/main/java/top/yogiczy/mytv/core/util/utils/Downloader.kt
    suspend fun downloadTo(url: String, filePath: String, onProgressCb: ((Int) -> Unit)?) =
        withContext(Dispatchers.IO) {
            val interceptor = Interceptor { chain ->
                val originalResponse = chain.proceed(chain.request())
                originalResponse.newBuilder()
                    .body(DownloadResponseBody(originalResponse, onProgressCb)).build()
            }

            val client = OkHttpClient.Builder().addNetworkInterceptor(interceptor).build()
            val request = okhttp3.Request.Builder().url(url).build()

            try {
                with(client.newCall(request).execute()) {
                    if (!isSuccessful) throw Exception("下载文件失败: $code")

                    val file = File(filePath)
                    FileOutputStream(file).use { fos -> fos.write(body!!.bytes()) }
                }
            } catch (ex: Exception) {
                throw Exception("下载文件失败，请检查网络连接", ex)
            }
        }

    private class DownloadResponseBody(
        private val originalResponse: okhttp3.Response,
        private val onProgressCb: ((Int) -> Unit)?,
    ) : okhttp3.ResponseBody() {
        override fun contentLength() = originalResponse.body!!.contentLength()

        override fun contentType() = originalResponse.body?.contentType()

        override fun source(): BufferedSource {
            return object : ForwardingSource(originalResponse.body!!.source()) {
                var totalBytesRead = 0L

                override fun read(sink: okio.Buffer, byteCount: Long): Long {
                    val bytesRead = super.read(sink, byteCount)
                    totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                    val progress = (totalBytesRead * 100 / contentLength()).toInt()
                    CoroutineScope(Dispatchers.IO).launch {
                        onProgressCb?.invoke(progress)
                    }
                    return bytesRead
                }
            }.buffer()
        }
    }
}