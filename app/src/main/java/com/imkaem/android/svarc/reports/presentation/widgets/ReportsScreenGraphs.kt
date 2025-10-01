package com.imkaem.android.svarc.reports.presentation.widgets

import CustomTabbedView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imkaem.android.svarc.core.presentation.widgets.CustomOptionsField
import com.imkaem.android.svarc.core.utils.values.CustomOptionFieldValue
import com.imkaem.android.svarc.expenses.utils.values.DateSpentValue
import com.imkaem.android.svarc.reports.utils.temp.TempDateSpentsGenerator
import com.imkaem.android.svarc.reports.utils.values.GraphRowValue
import com.imkaem.android.svarc.reports.utils.values.GraphRowValueConverters
import com.imkaem.android.svarc.ui.theme.ColorGrey
import com.imkaem.android.svarc.ui.theme.ColorGreyDark
import com.imkaem.android.svarc.ui.theme.ColorGreyLighter
import com.imkaem.android.svarc.ui.theme.ColorGreyLighterEr
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

val now = Instant.now().atZone(ZoneOffset.UTC)
val year = now.year

val monthDateSpends = TempDateSpentsGenerator.getTempMonthDateSpents(
    year = year,
    month = now.monthValue,
)
val dateSpents = TempDateSpentsGenerator.fillMonthDateSpentsGaps(
    year = year,
    month = now.monthValue,
    dateSpents = monthDateSpends,
)

@Composable
fun ReportsScreenGraphs() {

    /* TODO this should be done by the view model, and i guess passed to this composable */

//    val dateSpents = List(30) { index ->
//        val month = now.monthValue
//        val day = index + 1
//
//        val zonedDateTime: ZonedDateTime = ZonedDateTime.of(
//            year,
//            month,
//            day,
//            0,
//            0,
//            0,
//            0,
//            ZoneOffset.UTC,
//        )
//
//        DateSpentValue(
//            date = zonedDateTime.toInstant(),
//            amount = ((index + 1) * 3) * 100
//        )
//    }
    val dailyBudget = 300

    val spentGraphRowValues = GraphRowValueConverters.spentGraphRowValuesFromDateSpentValues(
        dateSpents = dateSpents,
        dailyBudget = dailyBudget,
    )

    val dailyRemainderGraphRowValues =
        GraphRowValueConverters.dailyRemainderGraphRowValuesFromDateSpentValues(
            dateSpents = dateSpents,
            dailyBudget = dailyBudget,
        )

    val accumulatedRemainderGraphRowValues =
        GraphRowValueConverters.accumulatedRemainderGraphRowValuesFromDateSpendValues(
            dateSpents = dateSpents,
            dailyBudget = dailyBudget,
        )


    /* TODO maybe this should be moved inside the composable that renders CustomZTabbefView for graph types */
    val graphTypesSelectedIndex = remember {
        mutableIntStateOf(0)
    }


    Column(
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        /* period selector - month in this case */
        /* TODO extract to a composable */
        GraphsMonthSelector()
        CustomTabbedView(
            currentTabIndex = graphTypesSelectedIndex.intValue,
            onTabSelected = { index -> graphTypesSelectedIndex.intValue = index },
            tabLabels = listOf("SPENT", "DAILY REMAINDER", "ACCUMULATED REMAINDER"),
            tabLabelTextSize = 12.sp,
            tabs = listOf(
                /* TODO i guess we could integrate padding inside the CustomTabbedView, so we dont have to do it individually */
                {
                    GraphRows(
                        rows = spentGraphRowValues,
                        maxAmount = dailyBudget
                    )
                },
                {
                    GraphRows(
                        rows = dailyRemainderGraphRowValues,
                        maxAmount = dailyBudget
                    )
                },
                {
                    GraphRows(
                        rows = accumulatedRemainderGraphRowValues,
//                        maxAmount = 600,
                        maxAmount = dailyBudget * dateSpents.size, // max could be daily budget * number of days (in case of not spending anything at all
                    )
                },
            ),
            isScrollable = true,
            activeContainerColor =
                ColorGrey,
            containerColor = ColorGreyLighterEr,
//            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}


/* TODO still not sure if this should stay here */
@Composable
private fun GraphsMonthSelector() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            /* TODO thi should be come kind of constants because it is reused */
            .padding(
                vertical = 20.dp,
            )
            .height(66.dp)
            .fillMaxWidth()
    ) {
        Column(
            Modifier
                .background(ColorGreyLighter)
                .padding(5.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Daily budget",
                fontSize = 12.sp,
                color = ColorGreyDark,
            )
            Text(
                "6 EUR",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            Modifier
//                    .background(ColorGreyLight)
                .weight(1f)
                .padding(5.dp)
        ) {
//                Text("Month")
            CustomOptionsField(
                isOptionsShown = false,
                selectedOption = CustomOptionFieldValue(key = 1, label = "November, 2025"),
                options = listOf(),
                onToggleOptionsShown = {},
                onSelectOption = {},
                leadingIcon = Icons.Filled.CalendarMonth,
                label = "Select month",
            )
        }
    }
}