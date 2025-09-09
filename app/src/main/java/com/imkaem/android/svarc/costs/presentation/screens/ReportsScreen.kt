package com.imkaem.android.svarc.costs.presentation.screens

import CustomTabbedView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imkaem.android.svarc.costs.presentation.widgets.ReportsScreenGraphs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onNavigateBack: () -> Unit,
) {

    val selectedTabIndex = remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            /* TODO this needs to be extracted eventually */
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNavigateBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to previous screen",
                        )
                    }
                }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {

            CustomTabbedView(
                currentTabIndex = selectedTabIndex.intValue,
                onTabSelected = { index ->
                    selectedTabIndex.intValue = index
                },
                tabLabels = listOf("Graphs", "Pies"),
                tabs = listOf(
                    { ReportsScreenGraphs() },
                    { /* TODO PiesContent() */ }
                ),
                modifier = Modifier.padding(horizontal = 10.dp)
            )

        }
    }
}