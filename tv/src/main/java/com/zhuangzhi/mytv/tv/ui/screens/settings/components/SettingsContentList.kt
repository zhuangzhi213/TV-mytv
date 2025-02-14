package com.zhuangzhi.mytv.tv.ui.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.zhuangzhi.mytv.tv.ui.rememberChildPadding
import com.zhuangzhi.mytv.tv.ui.screens.settings.LocalSettings
import com.zhuangzhi.mytv.tv.ui.utils.ifElse
import com.zhuangzhi.mytv.tv.ui.utils.saveFocusRestorer

@Composable
fun SettingsContentList(
    modifier: Modifier = Modifier,
    content: LazyListScope.(FocusRequester) -> Unit,
) {
    val childPadding = rememberChildPadding()
    val firstItemFocusRequester = remember { FocusRequester() }

    LazyColumn(
        modifier = modifier.ifElse(
            LocalSettings.current.uiFocusOptimize,
            Modifier.saveFocusRestorer { firstItemFocusRequester },
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(top = 4.dp, bottom = childPadding.bottom),
    ) {
        content(firstItemFocusRequester)
    }
}