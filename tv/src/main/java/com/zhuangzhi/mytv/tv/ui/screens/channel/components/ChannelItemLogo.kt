package com.zhuangzhi.mytv.tv.ui.screens.channel.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.SubcomposeAsyncImage

@Composable
fun ChannelItemLogo(
    modifier: Modifier = Modifier,
    logoProvider: () -> String?,
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = logoProvider(),
        contentDescription = null,
    )
}