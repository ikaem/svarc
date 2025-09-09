import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imkaem.android.svarc.ui.theme.ColorBlack
import com.imkaem.android.svarc.ui.theme.ColorGreyDark
import com.imkaem.android.svarc.ui.theme.ColorGreyLight
import com.imkaem.android.svarc.ui.theme.ColorGreyLighter
import com.imkaem.android.svarc.ui.theme.ColorWhite

@Composable
fun CustomTabbedView(
    currentTabIndex: Int,
    onTabSelected: (index: Int) -> Unit,
    tabLabels: List<String>,
    tabs: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    tabLabelTextSize: TextUnit = 16.sp,
    isScrollable: Boolean = false,
    containerColor: Color = Color.Transparent,
    activeContainerColor: Color = ColorGreyDark,
) {

    CustomTabRow(
        /* TODO should use view model for this */
        currentTabIndex = currentTabIndex,
        isScrollable = isScrollable,
        containerColor = containerColor,
        modifier = modifier
            .fillMaxWidth()
//            .padding(horizontal = 5.dp)
            /* TODO what does this do */
//            .wrapContentHeight()
//            .padding(horizontal = 10.dp)
            .height(40.dp),
    ) {


//        listOf<String>("Current", "All expenses").forEachIndexed { index, value ->
        tabLabels.forEachIndexed { index, value ->
//            val isTabSelected = selectedTabIndex.intValue == index
            val isTabSelected = currentTabIndex == index

            Tab(
                selected = isTabSelected,
//                onClick = { selectedTabIndex.intValue = index },
                onClick = { onTabSelected(index) },
                unselectedContentColor = ColorBlack,
                selectedContentColor = ColorWhite,
//                modifier = Modifier.width(100.dp)
                modifier = Modifier
                    .let {
                        if (isTabSelected) {
                            it.background(activeContainerColor)
                        } else {
                            it
                        }
                    }
//                    .width(200.dp)
            ) {
                Text(
                    text = value,
                    fontSize = tabLabelTextSize,
                    fontWeight = if (isTabSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

    }

    val currentComposable = tabs[currentTabIndex]
    currentComposable()

//    when (selectedTabIndex.intValue) {
//        0 -> HomeScreenCurrentCosts()
//
//        1 -> HomeScreenAllCosts()
//    }


}

@Composable
fun CustomTabRow(
    currentTabIndex: Int,
    isScrollable: Boolean,
    containerColor: Color,
    modifier: Modifier = Modifier,
    tabs: @Composable () -> Unit,

//    onTabSelected: (index: Int) -> Unit,
//    tabLabels: List<String>,
//    tabLabelTextSize: TextUnit,
) {

    when (isScrollable) {
        true -> ScrollableTabRow(
            selectedTabIndex =  currentTabIndex,
            modifier = modifier,
            containerColor = containerColor,
            divider = {},
            indicator = {},
//            edgePadding = 10.dp,
//            edgePadding = TabRowDefaults.ScrollableTabRowEdgeStartPadding
            edgePadding = 0.dp,
        ) {
            tabs()
        }
        else -> TabRow(
            selectedTabIndex = currentTabIndex,
            modifier = modifier,
            containerColor = containerColor,
            divider = {},
            indicator = {},
        ) {
            tabs()
        }
    }

}