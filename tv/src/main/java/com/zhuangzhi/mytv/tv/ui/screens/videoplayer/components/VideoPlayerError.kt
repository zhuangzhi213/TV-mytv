package com.zhuangzhi.mytv.tv.ui.screens.videoplayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.zhuangzhi.mytv.tv.ui.theme.MyTVTheme

@Composable
fun VideoPlayerError(
    modifier: Modifier = Modifier,
    errorProvider: () -> String? = { null },
) {
    val error = errorProvider() ?: return

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                shape = MaterialTheme.shapes.medium,
            )
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "播放失败",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error,
        )

        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = LocalContentColor.current.copy(alpha = 0.8f),
        )

        getErrorCodeDesc(error)?.let { nnDesc ->
            Text(
                text = nnDesc,
                style = MaterialTheme.typography.bodyMedium,
                color = LocalContentColor.current.copy(alpha = 0.8f),
            )
        }
    }
}

private fun getErrorCodeDesc(error: String): String? {
    return when (error.split("(").first()) {
        "UNSUPPORTED_TYPE" -> "不支持的视频类型，请检查视频源。"
        "LOAD_TIMEOUT" -> "加载超时，请检查网络连接或视频源。"
        "ERROR_CODE_UNSPECIFIED" -> "未知错误，请稍后再试。"
        "ERROR_CODE_REMOTE_ERROR" -> "远程错误，请稍后再试。"
        "ERROR_CODE_BEHIND_LIVE_WINDOW" -> "实时窗口错误，请稍后再试。"
        "ERROR_CODE_TIMEOUT" -> "超时错误，请稍后再试。"
        "ERROR_CODE_IO_UNSPECIFIED" -> "IO错误，请稍后再试。"
        "ERROR_CODE_IO_NETWORK_CONNECTION_FAILED" -> "网络连接异常或不支持IPv6，请检查网络设置。"
        "ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT" -> "网络连接超时，请检查网络设置。"
        "ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE" -> "HTTP请求返回错误类型，请检查视频源。"
        "ERROR_CODE_IO_BAD_HTTP_STATUS" -> "HTTP请求返回错误状态，请检查视频源。"
        "ERROR_CODE_IO_FILE_NOT_FOUND" -> "视频文件未找到，请检查视频源。"
        "ERROR_CODE_IO_NO_PERMISSION" -> "没有权限访问视频文件，请检查权限设置。"
        "ERROR_CODE_IO_CLEARTEXT_NOT_PERMITTED" -> "不允许使用明文访问视频文件，请检查权限设置。"
        "ERROR_CODE_IO_READ_POSITION_OUT_OF_RANGE" -> "视频文件读取位置超出范围，请检查视频源。"
        "ERROR_CODE_PARSING_CONTAINER_MALFORMED" -> "视频容器格式错误，请检查视频源。"
        "ERROR_CODE_PARSING_MANIFEST_MALFORMED" -> "视频流清单格式错误，请检查视频源。"
        "ERROR_CODE_PARSING_CONTAINER_UNSUPPORTED" -> "不支持的视频容器类型，请检查视频源。"
        "ERROR_CODE_PARSING_MANIFEST_UNSUPPORTED" -> "不支持的视频流清单类型，请检查视频源。"
        "ERROR_CODE_DECODER_INIT_FAILED" -> "视频解码器初始化失败，请检查视频源。"
        "ERROR_CODE_DECODER_QUERY_FAILED" -> "视频解码器查询失败，请检查视频源。"
        "ERROR_CODE_DECODING_FAILED" -> "视频解码失败，请检查视频源。"
        "ERROR_CODE_DECODING_FORMAT_EXCEEDS_CAPABILITIES" -> "视频解码格式超出能力范围，请检查视频源。"
        "ERROR_CODE_DECODING_FORMAT_UNSUPPORTED" -> "不支持的视频解码格式，请检查视频源。"
        "ERROR_CODE_DECODING_RESOURCES_RECLAIMED" -> "视频解码资源被回收，请检查视频源。"
        "ERROR_CODE_AUDIO_TRACK_INIT_FAILED" -> "音频轨道初始化失败，请检查视频源。"
        "ERROR_CODE_AUDIO_TRACK_WRITE_FAILED" -> "音频轨道写入失败，请检查视频源。"
        "ERROR_CODE_AUDIO_TRACK_OFFLOAD_WRITE_FAILED" -> "音频轨道离线写入失败，请检查视频源。"
        "ERROR_CODE_AUDIO_TRACK_OFFLOAD_INIT_FAILED" -> "音频轨道离线初始化失败，请检查视频源。"
        "ERROR_CODE_VIDEO_FRAME_PROCESSOR_INIT_FAILED" -> "视频帧处理器初始化失败，请检查视频源。"
        "ERROR_CODE_VIDEO_FRAME_PROCESSING_FAILED" -> "视频帧处理失败，请检查视频源。"
        "ERROR_CODE_FAILED_RUNTIME_CHECK" -> "运行时检查失败，请检查视频源。"
        else -> null
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun VideoPlayerErrorPreview() {
    MyTVTheme {
        VideoPlayerError(
            errorProvider = { "ERROR_CODE_IO_NETWORK_CONNECTION_FAILED(2001)" }
        )
    }
}