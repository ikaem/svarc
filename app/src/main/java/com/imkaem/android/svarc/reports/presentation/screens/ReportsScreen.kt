package com.imkaem.android.svarc.reports.presentation.screens

import CustomTabbedView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imkaem.android.svarc.reports.presentation.widgets.ReportsScreenGraphs

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
                    {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxHeight().fillMaxWidth()
                        ) {
                            Text(
                                "Coming soon...",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                )
                        }
                    }
                ),
                modifier = Modifier.padding(horizontal = 10.dp)
            )

        }
    }
}