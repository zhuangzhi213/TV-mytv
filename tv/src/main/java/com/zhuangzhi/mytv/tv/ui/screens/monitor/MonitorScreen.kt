package com.zhuangzhi.mytv.tv.ui.screens.monitor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zhuangzhi.mytv.tv.ui.rememberChildPadding
import com.zhuangzhi.mytv.tv.ui.screens.monitor.components.MonitorFps
import com.zhuangzhi.mytv.tv.ui.theme.MyTVTheme
import com.zhuangzhi.mytv.tv.ui.tooling.PreviewWithLayoutGrids

@Composable
fun MonitorScreen(
    modifier: Modifier = Modifier,
) {
    val childPadding = rememberChildPadding()

    Box(modifier = modifier.fillMaxSize()) {
        MonitorFps(modifier = Modifier.padding(start = childPadding.start, top = childPadding.top))
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun MonitorScreenPreview() {
    MyTVTheme {
        PreviewWithLayoutGrids {
            MonitorScreen()
        }
    }
}