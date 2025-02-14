package com.zhuangzhi.mytv.tv.ui.screens.webview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.zhuangzhi.mytv.tv.ui.theme.MyTVTheme

@Composable
fun WebViewPlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.8f)),
    ) {
        Text(
            text = "混合模式（webview）",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun WebViewPlaceholderPreview() {
    MyTVTheme {
        WebViewPlaceholder()
    }
}