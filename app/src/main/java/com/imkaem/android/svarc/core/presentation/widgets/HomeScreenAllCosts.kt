package com.imkaem.android.svarc.core.presentation.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imkaem.android.svarc.costs.presentation.widgets.CostBrief

@Composable
fun HomeScreenAllCosts() {
    Column(
        modifier = Modifier
            .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 40.dp)
            .fillMaxWidth()
    )
    {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),

        ) {
            items(
                count = 16,
                key = { index -> index }
            ) { index ->
                CostBrief()
            }
        }
    }
}
