import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imkaem.android.svarc.ui.theme.ColorGreyDark
import com.imkaem.android.svarc.ui.theme.ColorGreyLighter
import com.imkaem.android.svarc.ui.theme.ColorWhite

@Composable
fun HomeScreenCostsActions(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        CostsAction(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "10 EUR",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Edit daily budget",
                fontSize = 10.sp,
                color = ColorGreyDark,
            )
        }
        CostsAction(
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Filled.BarChart,
                contentDescription = "Balance report icon",
                modifier = Modifier.size(30.dp)

            )
            Text(
                "Balance report",
                fontSize = 10.sp,
                color = ColorGreyDark,
            )
        }
        CostsAction(
            modifier = Modifier.weight(1f),
            color = ColorGreyDark,
        ) {
            Icon(
                Icons.Filled.AddCircleOutline,
                contentDescription = "Balance report icon",
                modifier = Modifier.size(30.dp),
                tint = ColorWhite,
            )
            Text(
                "Add expense",
                fontSize = 10.sp,
                color = ColorWhite,
            )
        }
    }
}

@Composable
private fun CostsAction(
    modifier: Modifier = Modifier,
    color: Color = ColorGreyLighter,
    content: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .background(color)
            .padding(5.dp)
            .fillMaxHeight()
    ) {

        content()
//        Text(
//
//            "10 EUR",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold
//        )
//        Text(
//            "Edit daily budget",
//            fontSize = 10.sp,
//            color = ColorGreyDark,
//        )
    }
}