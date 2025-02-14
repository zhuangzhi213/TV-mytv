package com.zhuangzhi.mytv.tv.ui.screens.channel.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.zhuangzhi.mytv.tv.ui.theme.MyTVTheme

@Composable
fun ChannelNumber(
    modifier: Modifier = Modifier,
    channelNumberProvider: () -> String = { "" },
) {
    Text(
        modifier = modifier,
        text = channelNumberProvider(),
        style = MaterialTheme.typography.displayMedium,
    )
}

@Preview
@Composable
private fun ChannelNumberPreview() {
    MyTVTheme {
        ChannelNumber(channelNumberProvider = { "01" })
    }
}