package com.imkaem.android.svarc.costs.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.imkaem.android.svarc.costs.domain.models.CategoryModel
import com.imkaem.android.svarc.ui.theme.ColorBlue
import com.imkaem.android.svarc.ui.theme.ColorGrey
import com.imkaem.android.svarc.ui.theme.ColorGreyDark
import com.imkaem.android.svarc.ui.theme.ColorGreyLight
import com.imkaem.android.svarc.ui.theme.ColorGreyLighter
import com.imkaem.android.svarc.ui.theme.ColorWhite

@Composable
fun PickCategoryDialog(
    onDismissRequest: () -> Unit,
    onSelectCategory: (CategoryModel) -> Unit,
    onNewCategoryNameChange: (String) -> Unit,
    onNewCategoryAdd: () -> Unit,
    selectedCategory: CategoryModel?,
    categories: List<CategoryModel>,
    newCategoryName: String,

) {

    Dialog(
//        onDismissRequest = {
//            showCategoryPickerDialog.value = false
//        },
        onDismissRequest = onDismissRequest,
//                properties = DialogProperties(usePlatformDefaultWidth = true)
    ) {
        Column(
            modifier = Modifier
                .background(ColorWhite)
//                        .padding(horizontal = 20.dp, vertical = 10.dp),
        ) {
            Text(
                "Pick category",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
            )
            Text(
                selectedCategory?.name ?: "Selected category",
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            )


            HorizontalDivider()
            FlowRow(
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
//                        verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (i in categories) {
                    Text(
                        i.name,
                        fontSize = 14.sp,
                        modifier = Modifier
//                                    .fillMaxWidth()
                            .background(
                                ColorGreyLighter,
                                RoundedCornerShape(5.dp)
                            )
                            .let { it ->
                                if (i.id == selectedCategory?.id
                                ) {
                                    it.background(
                                        ColorGreyLight,
                                        RoundedCornerShape(5.dp)
                                    )
                                } else {
                                    it.background(
                                        ColorGreyLighter,
                                        RoundedCornerShape(5.dp)
                                    )
                                }
                            }
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .clickable {
                                onSelectCategory(i)
//                                selectedCategoryState.value = i
//                                        showCategoryPickerDialog.value = false
                            }
                    )
                }
            }
            HorizontalDivider()
//                    Spacer(Modifier.height(10.dp))
            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
            ) {
                Text(
                    "Add new category",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    TextField(
//                        value = newCategoryState.value,
                        value = newCategoryName,
                        onValueChange = { it ->
//                            newCategoryState.value = it
                            onNewCategoryNameChange(it)

                        },
                        modifier = Modifier.weight(1f)
                    )
                    Box(
//                                modifier = Modifier.border(1.dp, ColorGreyLight, RoundedCornerShape(5.dp)).background(
//                                    Color.Red).clip(RoundedCornerShape(5.dp)).padding(10.dp)
                        modifier = Modifier
//                                    .size(55.dp)
                            /* it works with or without border */
//                                    .border(0.dp, Color.Red, RoundedCornerShape(5.dp))
                            .clip(RoundedCornerShape(5.dp))
                            /* key is background in the end */
                            .background(ColorGrey)
                            .padding(5.dp)
                            .clickable {
//                                val newCategoryName = newCategoryName.trim()
//                                if(newCategoryName.isEmpty()) {
//                                    return@clickable
//                                }
//
//                                val existingCategory = categories.find { it ->
//                                    it.name.equals(newCategoryName, ignoreCase = true)
//                                }
//
//                                if(existingCategory != null) {
//                                    return@clickable
//                                }
//
//                                val newId = categories.size + 1
//                                val newCategory = CategoryModel(newId, newCategoryName)
//
//                                val updatedList = categories.toMutableList()
//                                updatedList.add(newCategory)

                                onNewCategoryAdd()
//                                onNewCategoryNameChange("")

//                                categoriesState.value = updatedList.toList()
//                                newCategoryState.value = ""
                            }
                        ,
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.AddCircleOutline,
                            contentDescription = "Add category icon",
                            tint = ColorWhite,
                            modifier = Modifier.size(40.dp)
                        )

                    }
                }
            }

            HorizontalDivider()
            Button(
                onClick = {
//                    showCategoryPickerDialog.value = false
//                    onDone()
                    onDismissRequest()
                },
                modifier = Modifier
                    .padding( 20.dp)
                    .align(Alignment.End),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorBlue
                )
            ) {
                Text("DONE")
            }
        }
    }

}