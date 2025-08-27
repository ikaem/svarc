package com.imkaem.android.svarc.costs.presentation.screens

import HomeScreenCostsActions
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imkaem.android.svarc.costs.presentation.widgets.HomeScreenCurrentCostsToday
import com.imkaem.android.svarc.ui.theme.ColorBlack
import com.imkaem.android.svarc.ui.theme.ColorGreyDark
import com.imkaem.android.svarc.ui.theme.ColorGreyLight
import com.imkaem.android.svarc.ui.theme.ColorWhite

@Composable
fun HomeScreen() {
    val selectedTabIndex = remember() { mutableIntStateOf(0) }




    Scaffold { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {

            HomeScreenCostsActions(
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(10.dp)
            )

//            Spacer(Modifier.height(10.dp))

            TabRow(
                /* TODO should use view model for this */
                selectedTabIndex = selectedTabIndex.intValue,
                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 10.dp)
                    .height(40.dp),
                divider = {},
                indicator = {}
            ) {


                listOf<String>("Current", "All costs").forEachIndexed { index, value ->
                    val isTabSelected = selectedTabIndex.intValue == index

                    Tab(
                        selected = isTabSelected,
                        onClick = { selectedTabIndex.intValue = index },
                        unselectedContentColor = ColorBlack,
                        selectedContentColor = ColorWhite,
                        modifier = Modifier.let {
                            if (isTabSelected) {
                                it.background(ColorGreyDark)
                            } else {
                                it
                            }
                        }

                    ) {
                        Text(
                            text = value,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

            }

            when (selectedTabIndex.intValue) {
                0 -> Column(
//            modifier = Modifier.padding(padding)
                ) {

                    /* today block */
                    HomeScreenCurrentCostsToday(
                        modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp).fillMaxWidth()
                    )
                    /* this week block */
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("This week")
                        Row {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Accumulated remainder")
                                Text("-11 EUR")
                            }
                            Column {
                                Text("Spent")
                                Text("20 EUR")
                            }
                            Column {
                                Text("Remainder")
                                Text("9 EUR")
                            }
                        }
                    }
                    /* this month block */
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("This month")
                        Row {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Accumulated remainder")
                                Text("-11 EUR")
                            }
                            Column {
                                Text("Spent")
                                Text("20 EUR")
                            }
                            Column {
                                Text("Remainder")
                                Text("9 EUR")
                            }
                        }
                    }


                }

                1 -> Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Recent expenses")
                        Text("See all")
                    }
                    LazyColumn {

                        items(
                            count = 4,
                            key = { index -> index }
                        ) { index ->

                            Text("Expense $index")

                        }


                    }
                }
            }


        }


    }

}