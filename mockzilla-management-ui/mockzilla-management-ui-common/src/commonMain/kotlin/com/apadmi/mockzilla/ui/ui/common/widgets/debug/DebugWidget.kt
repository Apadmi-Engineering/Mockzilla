@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.widgets.debug

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import com.apadmi.mockzilla.lib.InternalMockzillaApi

import kotlinx.coroutines.launch

private enum class Tabs {
    Colors,
    Typography,
    ;
}

@InternalMockzillaApi
@Composable
public fun DebugWidget() {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { Tabs.entries.size })
    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SecondaryScrollableTabRow(
            selectedTabIndex.value,
            Modifier.fillMaxWidth(),
            rememberScrollState(),
            TabRowDefaults.primaryContainerColor,
            TabRowDefaults.primaryContentColor,
            TabRowDefaults.ScrollableTabRowEdgeStartPadding,
            @Composable { HorizontalDivider() }) {
            Tabs.entries.forEachIndexed { index, currentTab ->
                Tab(
                    selected = selectedTabIndex.value == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(currentTab.ordinal)
                        }
                    },
                    text = { Text(text = currentTab.toString()) }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (Tabs.entries[selectedTabIndex.value]) {
                    Tabs.Colors -> DebugColorsWidget()
                    Tabs.Typography -> DebugTypographyWidget()
                }
            }
        }
    }
}
