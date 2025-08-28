package com.imkaem.android.svarc.core.presentation.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imkaem.android.svarc.ui.theme.ColorGreyLight

@Composable
fun HomeScreenCurrentCosts() {
    Column(
    ) {
        HomeScreenCurrentCostsToday(
            modifier = Modifier
                .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 40.dp)
                .fillMaxWidth()
        )
        HomeScreenCurrentCostsPeriod(
            periodLabel = "THIS WEEK",
            modifier = Modifier.fillMaxWidth()
        )
        HomeScreenCurrentCostsPeriod(
            periodLabel = "THIS MONTH",
            backgroundColor = ColorGreyLight,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
