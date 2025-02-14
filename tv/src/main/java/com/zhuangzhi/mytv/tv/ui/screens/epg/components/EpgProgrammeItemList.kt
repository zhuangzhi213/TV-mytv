package com.zhuangzhi.mytv.tv.ui.screens.epg.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import com.zhuangzhi.mytv.core.data.entities.channel.Channel
import com.zhuangzhi.mytv.core.data.entities.epg.Epg
import com.zhuangzhi.mytv.core.data.entities.epg.EpgProgramme
import com.zhuangzhi.mytv.core.data.entities.epg.EpgProgramme.Companion.isLive
import com.zhuangzhi.mytv.core.data.entities.epg.EpgProgrammeList
import com.zhuangzhi.mytv.core.data.entities.epg.EpgProgrammeReserveList
import com.zhuangzhi.mytv.tv.ui.theme.MyTVTheme
import kotlin.math.max

@Composable
fun EpgProgrammeItemList(
    modifier: Modifier = Modifier,
    epgProgrammeListProvider: () -> EpgProgrammeList = { EpgProgrammeList() },
    epgProgrammeReserveListProvider: () -> EpgProgrammeReserveList = { EpgProgrammeReserveList() },
    supportPlaybackProvider: () -> Boolean = { false },
    currentPlaybackProvider: () -> EpgProgramme? = { null },
    onPlayback: (EpgProgramme) -> Unit = {},
    onReserve: (EpgProgramme) -> Unit = {},
    focusOnLive: Boolean = true,
    onUserAction: () -> Unit = {},
) {
    val epgProgrammeList = epgProgrammeListProvider()
    val itemFocusRequesterList = List(epgProgrammeList.size) { FocusRequester() }

    val listState = LazyListState(max(0, epgProgrammeList.indexOfFirst { it.isLive() } - 2))
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { _ -> onUserAction() }
    }

    LazyColumn(
        modifier = modifier,
        // FIXME 闪退
        // .focusRestorer {
        //     itemFocusRequesterList[max(0, epgProgrammeList.indexOfFirst { it.isLive() })]
        // },
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        itemsIndexed(
            epgProgrammeList,
            key = { _, programme -> programme.hashCode() },
        ) { index, programme ->
            EpgProgrammeItem(
                modifier = Modifier.focusRequester(itemFocusRequesterList[index]),
                epgProgrammeProvider = { programme },
                supportPlaybackProvider = supportPlaybackProvider,
                isPlaybackProvider = { currentPlaybackProvider() == programme },
                hasReservedProvider = { epgProgrammeReserveListProvider().firstOrNull { it.programme == programme.title } != null },
                onPlayback = { onPlayback(programme) },
                onReserve = { onReserve(programme) },
                focusOnLive = focusOnLive,
            )
        }
    }
}

@Preview
@Composable
private fun EpgProgrammeItemListPreview() {
    MyTVTheme {
        EpgProgrammeItemList(
            epgProgrammeListProvider = { EpgProgrammeList(Epg.example(Channel.EXAMPLE).programmeList) }
        )
    }
}