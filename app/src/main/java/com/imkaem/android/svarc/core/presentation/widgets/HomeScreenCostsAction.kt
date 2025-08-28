import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.imkaem.android.svarc.ui.theme.ColorBlack
import com.imkaem.android.svarc.ui.theme.ColorGreyDark
import com.imkaem.android.svarc.ui.theme.ColorGreyLighter
import com.imkaem.android.svarc.ui.theme.ColorWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCostsActions(
    modifier: Modifier = Modifier
) {


    /* TODO this i guess should come from view model */
    val isAddExpenseDialogOpen = remember { mutableStateOf(false) }

    val isEditDailyBudgetBottomSheetOpen = remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    when {

        /* edit budget bottom sheet */
        isEditDailyBudgetBottomSheetOpen.value -> {
            ModalBottomSheet(
                onDismissRequest = {
                    isEditDailyBudgetBottomSheetOpen.value = false
                }
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            /* WHY DO WE HAVE TO DO THIS */
                            bottomSheetState.hide()
                        }.invokeOnCompletion {
                            if(bottomSheetState.isVisible) {
                                return@invokeOnCompletion
                            }

                            isEditDailyBudgetBottomSheetOpen.value = false
                        }
                    }
                ) {
                    Text("")
                }
            }
        }

        /* add expese dialog */
        isAddExpenseDialogOpen.value -> {
            Dialog(
                onDismissRequest = {
                    isAddExpenseDialogOpen.value = false
                }
            ) {
                /* https://developer.android.com/develop/ui/compose/components/dialog */
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(20.dp)
                ) {
                    Text("Add expense dialog")
                    Text(
                        "Close",
                        modifier = Modifier
                            .clickable {
                                isAddExpenseDialogOpen.value = false
                            },
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = ColorBlack,
                    )
                }
            }

            /* ---------------- */
//            AlertDialog(
//                onDismissRequest = {
//                    isAddExpenseDialogOpen.value = false
//                },
//                title = {
//                    Text("Add expense dialog")
//                },
//                text = {
//                    Text("Here will be a form to add an expense")
//                },
//                confirmButton = {
//                    Text(
//                        "Close",
//                        modifier = Modifier
//                            .padding(10.dp)
//                            .clickable {
//                                isAddExpenseDialogOpen.value = false
//                            },
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 16.sp,
//                        color = ColorBlack,
//                    )
//                }
//            )
            /* -------------- */
        }
//        else -> { /* Do nothing */
//        }
    }


    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        CostsAction(
            modifier = Modifier.weight(1f).clickable{
                isEditDailyBudgetBottomSheetOpen.value = true
            }
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
            modifier = Modifier
                .weight(1f)
                .clickable {
                    isAddExpenseDialogOpen.value = true
                },
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