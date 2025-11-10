package com.imkaem.android.svarc.dev.presentation.screens

import android.util.LayoutDirection
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.TableInfo
import com.imkaem.android.svarc.dev.presentation.view_models.dev_screen_view_model.DevScreenExpensesEvent
import com.imkaem.android.svarc.dev.presentation.view_models.dev_screen_view_model.DevScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevScreen(
    onNavigateBack: () -> Unit,
) {

    val viewModel: DevScreenViewModel = hiltViewModel()
    val state = viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Dev Screen")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to previous screen",
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
                start = 10.dp,
                end = 10.dp,
            )
        ) {

            Button(
                onClick = {
                    viewModel.onEvent(DevScreenExpensesEvent.AddDummyExpenses)
                },
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add expenses to DB")
            }
            if (state.value.expensesState.isLoadingAddDummyExpenses) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.onEvent(DevScreenExpensesEvent.DeleteAllExpenses)
                },
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete all expenses from DB")
            }
            if (state.value.expensesState.isLoadingDeleteAllExpenses) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}