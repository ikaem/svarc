package com.imkaem.android.svarc.core.presentation.screens

import CustomTabbedView
import HomeScreenCostsActions
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imkaem.android.svarc.core.presentation.widgets.HomeScreenAllCosts
import com.imkaem.android.svarc.core.presentation.widgets.HomeScreenCurrentCosts

@Composable
fun HomeScreen(
    onNavigateToReports: () -> Unit,
) {
    val selectedTabIndex = remember { mutableIntStateOf(0) }

    Scaffold { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {

            HomeScreenCostsActions(
                onNavigateToReports,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(10.dp)
            )

            CustomTabbedView(
                currentTabIndex = selectedTabIndex.intValue,
                onTabSelected = { index -> selectedTabIndex.intValue = index },
                tabLabels = listOf("Current", "All expenses"),
                tabs = listOf(
                    { HomeScreenCurrentCosts() },
                    { HomeScreenAllCosts() }
                ),
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }


    }

}



