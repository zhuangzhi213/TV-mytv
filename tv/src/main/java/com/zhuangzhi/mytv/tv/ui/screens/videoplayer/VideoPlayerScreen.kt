package com.zhuangzhi.mytv.tv.ui.screens.videoplayer

import android.view.SurfaceView
import android.view.TextureView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zhuangzhi.mytv.tv.ui.material.Visible
import com.zhuangzhi.mytv.tv.ui.rememberChildPadding
import com.zhuangzhi.mytv.tv.ui.screens.settings.SettingsViewModel
import com.zhuangzhi.mytv.tv.ui.screens.videoplayer.components.VideoPlayerError
import com.zhuangzhi.mytv.tv.ui.screens.videoplayer.components.VideoPlayerMetadata
import com.zhuangzhi.mytv.tv.ui.screens.videoplayer.player.VideoPlayer
import com.zhuangzhi.mytv.tv.ui.theme.MyTVTheme
import com.zhuangzhi.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import com.zhuangzhi.mytv.tv.ui.utils.Configs

@Composable
fun VideoPlayerScreen(
    modifier: Modifier = Modifier,
    state: VideoPlayerState = rememberVideoPlayerState(),
    settingsViewModel: SettingsViewModel = viewModel(),
    showMetadataProvider: () -> Boolean = { false },
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        val displayModeModifier = when (state.displayMode) {
            VideoPlayerDisplayMode.ORIGINAL -> Modifier.aspectRatio(state.aspectRatio)
            VideoPlayerDisplayMode.FILL -> Modifier.fillMaxSize()
            VideoPlayerDisplayMode.CROP -> Modifier
                .fillMaxWidth()
                .aspectRatio(state.aspectRatio)
            VideoPlayerDisplayMode.FOUR_THREE -> Modifier.aspectRatio(4f / 3)
            VideoPlayerDisplayMode.SIXTEEN_NINE -> Modifier.aspectRatio(16f / 9)
            VideoPlayerDisplayMode.WIDE -> Modifier.aspectRatio(2.35f / 1)
        }

        when (settingsViewModel.videoPlayerRenderMode) {
            Configs.VideoPlayerRenderMode.SURFACE_VIEW -> {
                AndroidView(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .then(displayModeModifier),
                    factory = { SurfaceView(context) },
                    update = { state.setVideoSurfaceView(it) },
                )
            }

            Configs.VideoPlayerRenderMode.TEXTURE_VIEW -> {
                AndroidView(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .then(displayModeModifier),
                    factory = { TextureView(context) },
                    update = { state.setVideoTextureView(it) },
                )
            }
        }
    }

    VideoPlayerScreenCover(
        showMetadataProvider = showMetadataProvider,
        metadataProvider = state::metadata,
        errorProvider = state::error,
    )
}

@Composable
private fun VideoPlayerScreenCover(
    modifier: Modifier = Modifier,
    showMetadataProvider: () -> Boolean = { false },
    metadataProvider: () -> VideoPlayer.Metadata = { VideoPlayer.Metadata() },
    errorProvider: () -> String? = { null },
) {
    val childPadding = rememberChildPadding()

    Box(modifier = modifier.fillMaxSize()) {
        Visible(showMetadataProvider) {
            VideoPlayerMetadata(
                modifier = Modifier.padding(start = childPadding.start, top = childPadding.top),
                metadataProvider = metadataProvider,
            )
        }

        VideoPlayerError(
            modifier = Modifier.align(Alignment.Center),
            errorProvider = errorProvider,
        )
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun VideoPlayerScreenCoverPreview() {
    MyTVTheme {
        PreviewWithLayoutGrids {
            VideoPlayerScreenCover(
                showMetadataProvider = { true },
                metadataProvider = { VideoPlayer.Metadata() },
                errorProvider = { "ERROR_CODE_BEHIND_LIVE_WINDOW" }
            )
        }
    }
}