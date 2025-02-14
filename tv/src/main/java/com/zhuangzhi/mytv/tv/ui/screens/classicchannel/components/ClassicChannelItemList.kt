package com.zhuangzhi.mytv.tv.ui.screens.classicchannel.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.DenseListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import com.zhuangzhi.mytv.core.data.entities.channel.Channel
import com.zhuangzhi.mytv.core.data.entities.channel.ChannelGroup
import com.zhuangzhi.mytv.core.data.entities.channel.ChannelList
import com.zhuangzhi.mytv.core.data.entities.epg.EpgList
import com.zhuangzhi.mytv.core.data.entities.epg.EpgList.Companion.recentProgramme
import com.zhuangzhi.mytv.core.data.entities.epg.EpgProgramme.Companion.progress
import com.zhuangzhi.mytv.core.data.entities.epg.EpgProgrammeRecent
import com.zhuangzhi.mytv.tv.ui.material.rememberDebounceState
import com.zhuangzhi.mytv.tv.ui.screens.channel.components.ChannelItemLogo
import com.zhuangzhi.mytv.tv.ui.screens.settings.LocalSettings
import com.zhuangzhi.mytv.tv.ui.theme.MyTVTheme
import com.zhuangzhi.mytv.tv.ui.utils.handleKeyEvents
import com.zhuangzhi.mytv.tv.ui.utils.ifElse
import com.zhuangzhi.mytv.tv.ui.utils.saveFocusRestorer
import com.zhuangzhi.mytv.tv.ui.utils.saveRequestFocus
import kotlin.math.max

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ClassicChannelItemList(
    modifier: Modifier = Modifier,
    channelGroupProvider: () -> ChannelGroup = { ChannelGroup() },
    channelListProvider: () -> ChannelList = { ChannelList() },
    initialChannelProvider: () -> Channel = { Channel() },
    showChannelLogoProvider: () -> Boolean = { false },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    onChannelFocused: (Channel) -> Unit = { },
    epgListProvider: () -> EpgList = { EpgList() },
    showEpgProgrammeProgressProvider: () -> Boolean = { false },
    inFavoriteModeProvider: () -> Boolean = { false },
    onUserAction: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val channelGroup = channelGroupProvider()
    val channelList = channelListProvider()
    val initialChannel = initialChannelProvider()
    val itemFocusRequesterList =
        remember(channelList) { List(channelList.size) { FocusRequester() } }

    var hasFocused by rememberSaveable { mutableStateOf(!channelList.contains(initialChannel)) }
    var focusedChannel by remember(channelList) {
        mutableStateOf(
            if (hasFocused) channelList.firstOrNull() ?: Channel() else initialChannel
        )
    }

    val onChannelFocusedDebounce = rememberDebounceState(wait = 100L) {
        onChannelFocused(focusedChannel)
    }

    val listState = remember(channelGroup) {
        LazyListState(
            if (hasFocused) 0
            else max(0, channelList.indexOf(initialChannel) - 2)
        )
    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }.distinctUntilChanged()
            .collect { _ -> onUserAction() }
    }

    val coroutineScope = rememberCoroutineScope()
    val firstFocusRequester = remember { FocusRequester() }
    val lastFocusRequester = remember { FocusRequester() }
    fun scrollToFirst() {
        coroutineScope.launch {
            listState.scrollToItem(0)
            firstFocusRequester.saveRequestFocus()
        }
    }

    fun scrollToLast() {
        coroutineScope.launch {
            listState.scrollToItem(channelList.lastIndex)
            lastFocusRequester.saveRequestFocus()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .width(if (showChannelLogoProvider()) 280.dp else 220.dp)
            .background(MaterialTheme.colorScheme.surface.copy(0.8f))
            .ifElse(
                LocalSettings.current.uiFocusOptimize,
                Modifier.saveFocusRestorer {
                    itemFocusRequesterList[channelList.indexOf(focusedChannel)]
                },
            ),
        state = listState,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        itemsIndexed(channelList, key = { _, channel -> channel.hashCode() }) { index, channel ->
            val isSelected by remember { derivedStateOf { channel == focusedChannel } }
            val initialFocused by remember {
                derivedStateOf { !hasFocused && channel == initialChannel }
            }

            ClassicChannelItem(
                modifier = Modifier
                    .ifElse(
                        index == 0,
                        Modifier
                            .focusRequester(firstFocusRequester)
                            .handleKeyEvents(onUp = { scrollToLast() })
                    )
                    .ifElse(
                        index == channelList.lastIndex,
                        Modifier
                            .focusRequester(lastFocusRequester)
                            .handleKeyEvents(onDown = { scrollToFirst() })
                    ),
                channelProvider = { channel },
                onChannelSelected = { onChannelSelected(channel) },
                onChannelFavoriteToggle = {
                    if (inFavoriteModeProvider()) {
                        if (channelList.size == 1) {
                            focusManager.moveFocus(FocusDirection.Left)
                        } else if (channelList.first() == channel) {
                            focusManager.moveFocus(FocusDirection.Down)
                        } else if (channelList.last() == channel) {
                            focusManager.moveFocus(FocusDirection.Up)
                        } else {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    }
                    onChannelFavoriteToggle(channel)
                },
                onChannelFocused = {
                    focusedChannel = channel
                    onChannelFocusedDebounce.send()
                },
                recentEpgProgrammeProvider = { epgListProvider().recentProgramme(channel) },
                showEpgProgrammeProgressProvider = showEpgProgrammeProgressProvider,
                focusRequesterProvider = { itemFocusRequesterList[index] },
                initialFocusedProvider = { initialFocused },
                onInitialFocused = { hasFocused = true },
                isSelectedProvider = { isSelected },
                showChannelLogoProvider = showChannelLogoProvider,
            )
        }
    }
}

@Composable
private fun ClassicChannelItem(
    modifier: Modifier = Modifier,
    channelProvider: () -> Channel = { Channel() },
    showChannelLogoProvider: () -> Boolean = { false },
    onChannelSelected: () -> Unit = {},
    onChannelFavoriteToggle: () -> Unit = {},
    onChannelFocused: () -> Unit = {},
    recentEpgProgrammeProvider: () -> EpgProgrammeRecent? = { null },
    showEpgProgrammeProgressProvider: () -> Boolean = { false },
    focusRequesterProvider: () -> FocusRequester = { FocusRequester() },
    initialFocusedProvider: () -> Boolean = { false },
    onInitialFocused: () -> Unit = {},
    isSelectedProvider: () -> Boolean = { false },
) {
    val channel = channelProvider()
    val nowEpgProgramme = recentEpgProgrammeProvider()?.now
    val showEpgProgrammeProgress = showEpgProgrammeProgressProvider()
    val focusRequester = focusRequesterProvider()

    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (initialFocusedProvider()) {
            onInitialFocused()
            focusRequester.saveRequestFocus()
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showChannelLogoProvider()) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight()
            ) {
                ChannelItemLogo(
                    modifier = Modifier.align(Alignment.Center),
                    logoProvider = { channel.logo },
                )
            }
        }

        Box(modifier = modifier.clip(ListItemDefaults.shape().shape)) {
            DenseListItem(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        isFocused = it.isFocused || it.hasFocus
                        if (isFocused) onChannelFocused()
                    }
                    .handleKeyEvents(
                        onSelect = onChannelSelected,
                        onLongSelect = onChannelFavoriteToggle,
                    ),
                colors = ListItemDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.onSurface,
                    selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    selectedContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                selected = isSelectedProvider(),
                onClick = {},
                headlineContent = {
                    Row{
                        Text(text = channel.id, maxLines = 1,
                            modifier = Modifier.padding(top = 4.dp))
                        Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                            Spacer(
                                modifier = Modifier
                                    .background(Color.White)
                                // .width(1.dp)
                                // .height(20.dp),
                            )
                        }
                        Text(
                            channel.name,
                            maxLines = 1,
                            modifier = Modifier.ifElse(isFocused, Modifier.basicMarquee()),
                        )
                    }

                },
                supportingContent = {
                    Text(
                        text = nowEpgProgramme?.title ?: "精彩节目",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.ifElse(isFocused, Modifier.basicMarquee()),
                    )
                },
            )

            if (showEpgProgrammeProgress && nowEpgProgramme != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth(nowEpgProgramme.progress())
                        .height(3.dp)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)),
                )
            }
        }
    }
}

@Preview
@Composable
private fun ClassicChannelItemListPreview() {
    MyTVTheme {
        Row {
            ClassicChannelItemList(
                channelListProvider = { ChannelList.EXAMPLE },
                initialChannelProvider = { ChannelList.EXAMPLE.first() },
                epgListProvider = { EpgList.example(ChannelList.EXAMPLE) },
                showEpgProgrammeProgressProvider = { true },
            )
        }
    }
}

@Preview
@Composable
private fun ClassicChannelItemListWithChannelLogoPreview() {
    MyTVTheme {
        Row {
            ClassicChannelItemList(
                channelListProvider = { ChannelList.EXAMPLE },
                initialChannelProvider = { ChannelList.EXAMPLE.first() },
                epgListProvider = { EpgList.example(ChannelList.EXAMPLE) },
                showEpgProgrammeProgressProvider = { true },
                showChannelLogoProvider = { true },
            )
        }
    }
}